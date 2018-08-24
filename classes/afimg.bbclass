inherit image_types

#
# Create an image that can by written onto a SD card using dd.
#
# Here's the format of the rootfs we're making
#
# name     size  szSec startSec  endSec type
# -------------------------------------------------
# MBR       1MB   2048        0    2047
# Boot     33MB  67584     2048   69631 vfat
# AferoNV  16MB  32768    69632  102399 ext3
# Root1                  102400         ext3
# Root2                                 ext3

# Use an uncompressed ext3 by default as rootfs
AFIMG_ROOTFS_TYPE ?= "ext3"

# This image depends on the rootfs image
IMAGE_TYPEDEP_afimg = "${AFIMG_ROOTFS_TYPE}"

AFIMG_ROOTFS = "${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.${AFIMG_ROOTFS_TYPE}"

do_image_afimg[depends] = " \
			parted-native:do_populate_sysroot \
			mtools-native:do_populate_sysroot \
			dosfstools-native:do_populate_sysroot \
			e2fsprogs-native:do_populate_sysroot \
			virtual/kernel:do_deploy \
"

# SD card image name
AFIMG = "${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.afimg"

IMAGE_CMD_afimg () {
	set -x
	# first get sizes
	REAL_ROOTFS_SIZE=$(wc -c ${AFIMG_ROOTFS} | awk '{ print $1 }')
	ROOTFS_SIZE_MB=$(expr ${REAL_ROOTFS_SIZE} / 1048576 + 21)  # Round to next 1MB and add 20MB extra space
	BOOT_SIZE_MB=33
	AFERO_NV_SIZE_MB=16
	TOTAL_SIZE_MB=$(expr 1 + ${BOOT_SIZE_MB} + ${AFERO_NV_SIZE_MB} + ${ROOTFS_SIZE_MB} + ${ROOTFS_SIZE_MB})

	# create image and partition with a MBR
	dd if=/dev/zero of=${AFIMG} bs=1M count=${TOTAL_SIZE_MB}

	# create boot partition
	BOOT_LOC=1
	BOOT_LOC_SEC=$(expr ${BOOT_LOC} * 2048)
	END=$(expr ${BOOT_SIZE_MB} * 2048 + ${BOOT_LOC_SEC} - 1)
	parted ${AFIMG} mklabel msdos
	parted ${AFIMG} unit MiB mkpart primary fat32 ${BOOT_LOC_SEC}s ${END}s
	parted ${AFIMG} set 1 boot on

	# create Afero NV partition
	AFERO_NV_LOC=$(expr ${BOOT_LOC} + ${BOOT_SIZE_MB})
	AFERO_NV_LOC_SEC=$(expr ${AFERO_NV_LOC} * 2048)
	END=$(expr ${AFERO_NV_SIZE_MB} * 2048 + ${AFERO_NV_LOC_SEC} - 1)
	parted ${AFIMG} mkpart primary ext2 ${AFERO_NV_LOC_SEC}s ${END}s

	# create rootfs1 partition
	ROOT1_LOC=$(expr ${AFERO_NV_LOC} + ${AFERO_NV_SIZE_MB})
	ROOT1_LOC_SEC=$(expr ${ROOT1_LOC} * 2048)
	END=$(expr ${ROOTFS_SIZE_MB} * 2048 + ${ROOT1_LOC_SEC} - 1)
	parted ${AFIMG} mkpart primary ext2 ${ROOT1_LOC_SEC}s ${END}s

	# create rootfs2 partition
	ROOT2_LOC=$(expr ${ROOT1_LOC} + ${ROOTFS_SIZE_MB})
	ROOT2_LOC_SEC=$(expr ${ROOT2_LOC} * 2048)
	END=$(expr ${ROOTFS_SIZE_MB} * 2048 + ${ROOT2_LOC_SEC} - 1)
	parted ${AFIMG} mkpart primary ext2 ${ROOT2_LOC_SEC}s ${END}s

	# print out partition table in megabytes
	parted ${AFIMG} unit MiB print

	# populate the boot partition
	rm -f ${WORKDIR}/boot.img
	dd if=/dev/zero of=${WORKDIR}/boot.img bs=1M count=${BOOT_SIZE_MB}
	mkfs.vfat -F 32 -n "boot" ${WORKDIR}/boot.img

	# copy files onto the FAT boot partition
	# relies on uEnv.txt install in Arago Afero image
	mcopy -i ${WORKDIR}/boot.img -s ${DEPLOY_DIR_IMAGE}/MLO ::/
	mcopy -i ${WORKDIR}/boot.img -s ${DEPLOY_DIR_IMAGE}/u-boot.img ::/
	rm -f ${WORKDIR}/uEnv.txt
	# boot from mmcblk0p2
	echo "fdtfile=am335x-bonegreen-wireless.dtb" > ${WORKDIR}/uEnv.txt
	echo "bootpart=0:3" >> ${WORKDIR}/uEnv.txt
	echo "finduuid=part uuid mmc 0:3 uuid" >> ${WORKDIR}/uEnv.txt
	mcopy -i ${WORKDIR}/boot.img -s ${WORKDIR}/uEnv.txt ::/

	# create the afero_nv partition
	dd if=/dev/zero of=${WORKDIR}/afero_nv.img bs=1M count=${AFERO_NV_SIZE_MB}
	mkfs.ext3 -F ${WORKDIR}/afero_nv.img

	# resize the rootfs partition so it fits the entire space
	#e2fsck -f -p ${AFIMG_ROOTFS} || echo e2fsck returned $?
	#resize2fs -f ${AFIMG_ROOTFS} 160M

	dd if=${WORKDIR}/boot.img of=${AFIMG} bs=1M seek=${BOOT_LOC}
	dd if=${WORKDIR}/afero_nv.img of=${AFIMG} bs=1M seek=${AFERO_NV_LOC}
	dd if=${AFIMG_ROOTFS} of=${AFIMG} bs=1M seek=${ROOT1_LOC}
	dd if=${AFIMG_ROOTFS} of=${AFIMG} bs=1M seek=${ROOT2_LOC}
}

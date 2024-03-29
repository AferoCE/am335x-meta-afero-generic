SECTION = "kernel"
DESCRIPTION = "Linux kernel for TI devices"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

inherit kernel

PROVIDES += "virtual/kernel kernel kernel-base kernel-image kernel-dev kernel-vmlinux kernel-misc kernel-modules perf-dbg perf"
require recipes-kernel/linux/linux-dtb.inc
require recipes-kernel/linux/setup-defconfig.inc
#require recipes-kernel/linux/cmem.inc
require recipes-kernel/linux/ti-uio.inc
#require recipes-kernel/linux/copy-defconfig.inc

# Look in the generic major.minor directory for files
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-4.4:"

# Pull in the devicetree files into the rootfs
RDEPENDS_kernel-base += "kernel-devicetree"

# Add a run-time dependency for the PM firmware to be installed
# on the target file system.
RDEPENDS_kernel-base_append_ti33x = " amx3-cm3"
RDEPENDS_kernel-base_append_ti43x = " amx3-cm3"

# Add a run-time dependency for the VPE VPDMA firmware to be installed
# on the target file system.
RDEPENDS_kernel-base_append_dra7xx = " vpdma-fw"

# Install boot-monitor skern file into /boot dir of rootfs
RDEPENDS_kernel-base_append_keystone = " boot-monitor"

# Install ti-sci-fw into /boot dir of rootfs
RDEPENDS_kernel-base_append_k2g-evm = " ti-sci-fw"

# Add run-time dependency for SerDes firmware to the rootfs
RDEPENDS_kernel-base_append_keystone = " serdes-fw"

# Add run-time dependency on netcp-sa-fw for keystone-sa driver
RDEPENDS_kernel-base_append_keystone = " netcp-sa-fw"

# Add run-time dependency for NETCP PA firmware to the rootfs
RDEPENDS_kernel-base_append_k2hk-evm = " netcp-pa-fw"
RDEPENDS_kernel-base_append_k2e-evm = " netcp-pa-fw"
RDEPENDS_kernel-base_append_k2l-evm = " netcp-pa-fw"

# Add run-time dependency for PRU Ethernet firmware to the rootfs
RDEPENDS_kernel-base_append_am57xx-evm = " prueth-fw"
RDEPENDS_kernel-base_append_am437x-evm = " prueth-fw"
RDEPENDS_kernel-base_append_am335x-evm = " prueth-fw"

# Default is to package all dtb files for ti33x devices unless building
# for the specific beaglebone machine.
#KERNEL_DEVICETREE_ti33x = "am335x-evm.dtb am335x-evmsk.dtb am335x-bone.dtb am335x-boneblack.dtb am335x-bonegreen.dtb am335x-icev2.dtb am335x-bonegreen-wireless.dtb"
KERNEL_DEVICETREE_ti33x = "am335x-bonegreen-wireless.dtb"
KERNEL_DEVICETREE_ti43x = "am43x-epos-evm.dtb am437x-gp-evm.dtb am437x-gp-evm-hdmi.dtb am437x-sk-evm.dtb am437x-idk-evm.dtb"
KERNEL_DEVICETREE_beaglebone = "am335x-bone.dtb am335x-boneblack.dtb am335x-bonegreen.dtb am335x-bonegreen-wireless.dtb"
KERNEL_DEVICETREE_omap5-evm = "omap5-uevm.dtb"
KERNEL_DEVICETREE_dra7xx-evm = "dra7-evm.dtb dra7-evm-lcd-lg.dtb dra7-evm-lcd-osd.dtb dra72-evm.dtb dra72-evm-revc.dtb dra72-evm-lcd-lg.dtb dra72-evm-lcd-osd.dtb"
KERNEL_DEVICETREE_dra7xx-hs-evm = "${KERNEL_DEVICETREE_dra7xx-evm}"
KERNEL_DEVICETREE_am57xx-evm = "am57xx-beagle-x15.dtb am57xx-beagle-x15-revb1.dtb am57xx-evm.dtb am57xx-evm-reva3.dtb am571x-idk.dtb am572x-idk.dtb am571x-idk-lcd-osd.dtb am572x-idk-lcd-osd.dtb"
KERNEL_DEVICETREE_omap3 = "omap3-beagle.dtb omap3-beagle-xm.dtb omap3-beagle-xm-ab.dtb omap3-evm.dtb omap3-evm-37xx.dtb am3517-evm.dtb"
KERNEL_DEVICETREE_am3517-evm = "am3517-evm.dtb"
KERNEL_DEVICETREE_am37x-evm = "omap3-evm-37xx.dtb"
KERNEL_DEVICETREE_beagleboard = "omap3-beagle.dtb omap3-beagle-xm.dtb omap3-beagle-xm-ab.dtb"
KERNEL_DEVICETREE_pandaboard = "omap4-panda.dtb omap4-panda-a4.dtb omap4-panda-es.dtb"
KERNEL_DEVICETREE_k2hk-evm = "keystone-k2hk-evm.dtb"
KERNEL_DEVICETREE_k2e-evm = "keystone-k2e-evm.dtb"
KERNEL_DEVICETREE_k2g-evm = "keystone-k2g-evm.dtb"
KERNEL_DEVICETREE_k2l-evm = "keystone-k2l-evm.dtb"

KERNEL_EXTRA_ARGS += "LOADADDR=${UBOOT_ENTRYPOINT}"

COMPATIBLE_MACHINE = "ti33x|ti43x|omap-a15|omap3|omap4|keystone"

S = "${WORKDIR}/git"

SRC_URI = "git://git.ti.com/processor-sdk/processor-sdk-linux.git;protocol=git;branch=processor-sdk-linux-03.00.00"
SRC_URI += " file://0001-Create-local-branch.patch"
SRC_URI += " file://0002-add-wilink8-configuration-to-sdk3-defconfig.patch"
SRC_URI += " file://0003-add-iptables-nat.patch"
SRC_URI += " file://0004-add-BBGW-support.patch"
SRC_URI += " file://0005-Workaround-for-edma_ccerrint-kernel-panic.patch"
SRC_URI += " file://0006-HUB-848-Support-IPV4-Advanced-Router.patch"

SRCREV = "3639bea54a4a1e1c572a1bde78facc4e37839c12"

PV = "4.4.12+git${SRCPV}"

# Append to the MACHINE_KERNEL_PR so that a new SRCREV will cause a rebuild
MACHINE_KERNEL_PR_append = "d"
PR = "${MACHINE_KERNEL_PR}"

#KERNEL_CONFIG_DIR = "${S}/ti_config_fragments"

#KERNEL_CONFIG_FRAGMENTS_append_ti33x = " ${KERNEL_CONFIG_DIR}/am33xx_only.cfg"
#KERNEL_CONFIG_FRAGMENTS_append_ti43x = " ${KERNEL_CONFIG_DIR}/am43xx_only.cfg"
#KERNEL_CONFIG_FRAGMENTS_append_dra7xx = " ${KERNEL_CONFIG_DIR}/dra7_only.cfg"
#KERNEL_CONFIG_FRAGMENTS_append_k2g-evm = " ${KERNEL_CONFIG_DIR}/k2g_only.cfg"

MULTI_CONFIG_BASE_SUFFIX = ""

do_configure() {
    echo ${KERNEL_LOCALVERSION} > ${B}/.scmversion
    echo ${KERNEL_LOCALVERSION} > ${S}/.scmversion
    echo ${KERNEL_LOCALVERSION}

    oe_runmake -C ${S} O=${B} tisdk_am335x-evm_defconfig
    [ -h ${STAGING_KERNEL_BUILDDIR}/build ] || ln -s ${B} ${STAGING_KERNEL_BUILDDIR}/build
}


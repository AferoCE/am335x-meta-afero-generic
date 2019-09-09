# Copyright (C) 2017 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Linux Backports"
HOMEPAGE = "https://backports.wiki.kernel.org"
SECTION = "kernel/modules"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

SRC_URI = "git://git@github.com/AferoCE/backports-ti-wl18xx;protocol=ssh"
SRCREV = "01fc65e54ba91fdd408776c1337f12e8efae4dba"

SRC_URI[md5sum] = "dca31e267b7cdad8fa4ea32960ef7993"
SRC_URI[sha256sum] = "df479dc8f7c25ca4ce47ecd85707f44d3e55524401daf09f48b9b7188eab22a1"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = "KLIB_BUILD=${STAGING_KERNEL_BUILDDIR}/build KLIB=${D} DESTDIR=${D}"

DEPENDS += " virtual/kernel"

inherit module-base

do_configure[depends] += "virtual/kernel:do_deploy"

do_configure_prepend() {
	CC="${BUILD_CC}" oe_runmake defconfig-wl18xx
}

do_configure_append() {
	oe_runmake
	sed -i "s#@./scripts/update-initramfs## " Makefile
	sed -i "s#@./scripts/update-initramfs $(KLIB)## " Makefile.real
	sed -i "s#@./scripts/check_depmod.sh## " Makefile.real
	sed -i "s#@/sbin/depmod -a## " Makefile.real
}

do_compile() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	oe_runmake KERNEL_PATH=${STAGING_KERNEL_DIR}   \
		   KERNEL_SRC=${STAGING_KERNEL_DIR}    \
		   KERNEL_VERSION=${KERNEL_VERSION}    \
		   CC="${KERNEL_CC}" LD="${KERNEL_LD}" \
		   AR="${KERNEL_AR}" \
		   ${MAKE_TARGETS}
}

do_install() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	oe_runmake DEPMOD=echo INSTALL_MOD_PATH="${D}" \
	           KERNEL_SRC=${STAGING_KERNEL_DIR} \
	           CC="${KERNEL_CC}" LD="${KERNEL_LD}" \
	           modules_install
}

FILES_${PN} += "${nonarch_base_libdir}/udev \
                ${sysconfdir}/udev \
		${nonarch_base_libdir} \
               "

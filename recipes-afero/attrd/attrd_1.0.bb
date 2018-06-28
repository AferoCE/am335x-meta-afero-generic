# Copyright (C) 2017 Afero, Inc. All rights reserved

DESCRIPTION = "Afero Attribute Deamon"
SECTION = "examples"
DEPENDS = "libevent af-util af-ipc"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI += " file://attrd.service"

inherit externalsrc autotools systemd
EXTERNALSRC = "${TOPDIR}/../attrd/pkg"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = " attrd.service"

EXTRA_OECONF = "BUILD_TARGET=${BUILD_TARGET}"

PARALLEL_MAKE = ""

do_install_append() {
    if [ "x${BUILD_HW}x" != "xcubex" ] ; then
        install -d ${D}${systemd_system_unitdir}
        install -d ${D}${systemd_system_unitdir}/multi-user.target.wants/
        install -m 644 ${WORKDIR}/attrd.service ${D}${systemd_system_unitdir}
        ln -s ${systemd_system_unitdir}/attrd.service ${D}${systemd_system_unitdir}/multi-user.target.wants/
    fi
}

FILES_${PN} += " ${systemd_system_unitdir}/attrd.service ${systemd_system_unitdir}/multi-user.target.wants/attrd.service"

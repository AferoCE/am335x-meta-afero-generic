# Copyright (C) 2017 Afero, Inc. All rights reserved

DESCRIPTION = "OTA Manager Sample"
SECTION = "examples"
DEPENDS = "libevent af-util af-ipc attrd"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI += " file://otamgr.service"

inherit externalsrc autotools systemd
EXTERNALSRC = "${TOPDIR}/../otamgr/pkg"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = " otamgr.service"

EXTRA_OECONF = "BUILD_TARGET=${BUILD_TARGET}"

PARALLEL_MAKE = ""

do_install_append() {
    install -d ${D}${systemd_system_unitdir}
    install -m 644 ${WORKDIR}/otamgr.service ${D}${systemd_system_unitdir}
}

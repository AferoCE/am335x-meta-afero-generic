# Copyright (C) 2017-2018 Afero, Inc. All rights reserved

DESCRIPTION = "edge device library"
SECTION = "examples"
DEPENDS = "libevent af-util af-ipc json-c"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI = "git://git@github.com/AferoCE/am335x-binaries-af-edge;protocol=ssh"
SRCREV = "dd3f010372b79a58c604107cb71a5acc61bef49d"
SRC_URI += " file://afedged.service"

S = "${WORKDIR}/git"

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = " afedged.service"

# TODO add control to start up afedged
# TODO add control to include afedged

do_install() {
    # Install aflib (libaf_edge.so)
    install -d ${D}${libdir}
    install -Dm 755 ${S}/${BUILD_TARGET}${libdir}/libaf_edge.so.0.0.0 ${D}${libdir}
    ln -s libaf_edge.so.0.0.0 ${D}${libdir}/libaf_edge.so
    ln -s libaf_edge.so.0.0.0 ${D}${libdir}/libaf_edge.so.0

    # Install aflib.h
    install -d ${D}${includedir}
    install -Dm 644 ${S}/${BUILD_TARGET}${includedir}/aflib.h ${D}${includedir}

    # Install edge daemon
    install -d ${D}${bindir}
    install -Dm 755 ${S}/${BUILD_TARGET}${bindir}/afedged ${D}${bindir}

    # Install afedged systemd script
    install -d ${D}${systemd_system_unitdir}
    install -Dm 644 ${WORKDIR}/afedged.service ${D}${systemd_system_unitdir}
}

FILES_${PN} += " ${libdir}/libaf_edge.so ${libdir}/libaf_edge.so.0"

# Copyright (C) 2017-2018 Afero, Inc. All rights reserved

DESCRIPTION = "edge device library"
SECTION = "examples"
DEPENDS = "libevent af-util af-ipc json-c"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI = "git://git@github.com/AferoCE/am335x-binaries-af-edge;protocol=ssh"
SRCREV = "f8900c4eb8ce2612bede86735af320624681ce8b"
SRC_URI += " file://edged.service"
SRC_URI += " file://edge_watcher"

S = "${WORKDIR}/git"

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = " edged.service"

do_install() {
    # Install aflib (libaf_edge.so)
    install -d ${D}${libdir}
    install -Dm 755 ${S}/${BUILD_TARGET}${libdir}/libaf_edge.so.0.0.0 ${D}${libdir}
    ln -s libaf_edge.so.0.0.0 ${D}${libdir}/libaf_edge.so
    ln -s libaf_edge.so.0.0.0 ${D}${libdir}/libaf_edge.so.0

    # Install aflib.h
    install -d ${D}${includedir}
    install -Dm 644 ${S}/${BUILD_TARGET}${includedir}/*.h ${D}${includedir}

    # Install edge daemon
    install -d ${D}${bindir}
    install -Dm 755 ${WORKDIR}/edge_watcher ${D}${bindir}

    # Install afedged systemd script
    install -d ${D}${systemd_system_unitdir}
    install -Dm 644 ${WORKDIR}/edged.service ${D}${systemd_system_unitdir}
}

FILES_${PN} += " ${libdir}/libaf_edge.so ${libdir}/libaf_edge.so.0"

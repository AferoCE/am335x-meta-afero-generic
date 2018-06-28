# Copyright (C) 2018 Afero, Inc. All rights reserved

DESCRIPTION = "Afero binaries"
SECTION = "examples"
DEPENDS = "libevent json-c zlib openssl curl af-ipc af-util"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI = "git://git@github.com/AferoCE/am335x-binaries-generic;protocol=ssh"
SRCREV = "7264903cf7a0e9dbb95c1a8adfb8395dbd46cb4b"

SRC_URI += " file://afsecd.service"
SRC_URI += " file://afedged.service"
SRC_URI += " file://hubby.service"

S = "${WORKDIR}/git"

#SYSTEMD_PACKAGES = "${PN}"
#SYSTEMD_SERVICE_${PN} = " afsecd.service"
#SYSTEMD_PACKAGES = "${PN}"
#SYSTEMD_SERVICE_${PN} = " afedged.service"

#EXTRA_OECONF = "BUILD_TARGET=${BUILD_TARGET}"

#PARALLEL_MAKE = ""

do_install() {
    install -d ${D}${bindir}
    install -d ${D}${sysconfdir}
    install -d ${D}${libdir}
    install -d ${D}${includedir}
    install -Dm 755 ${S}/${BUILD_TARGET}${bindir}/afsecd ${D}${bindir}
    install -Dm 755 ${S}/${BUILD_TARGET}${bindir}/afedged ${D}${bindir}
    install -Dm 755 ${S}/${BUILD_TARGET}${bindir}/hubby ${D}${bindir}
    install -Dm 755 ${S}/${BUILD_TARGET}${bindir}/provision ${D}${bindir}
    install -Dm 644 ${S}/${BUILD_TARGET}${sysconfdir}/hub.profile ${D}${sysconfdir}
    install -Dm 644 ${S}/${BUILD_TARGET}${sysconfdir}/app_ota_record.json ${D}${sysconfdir}
    install -Dm 644 ${S}/${BUILD_TARGET}${libdir}/libaf_edge.so.0.0.0 ${D}${libdir}
    ln -s libaf_edge.so.0.0.0 ${D}${libdir}/libaf_edge.so
    ln -s libaf_edge.so.0.0.0 ${D}${libdir}/libaf_edge.so.0
    install -Dm 644 ${S}/${BUILD_TARGET}${includedir}/aflib.h ${D}${includedir}
    install -Dm 644 ${S}/${BUILD_TARGET}${libdir}/libafwp.so.0.0.0 ${D}${libdir}
    ln -s libafwp.so.0.0.0 ${D}${libdir}/libafwp.so
    ln -s libafwp.so.0.0.0 ${D}${libdir}/libafwp.so.0
    install -Dm 644 ${S}/${BUILD_TARGET}${includedir}/afwp.h ${D}${includedir}

    # install systemd scripts by hand
    install -d ${D}${systemd_system_unitdir}
    install -d ${D}${systemd_system_unitdir}/multi-user.target.wants/
    install -m 644 ${WORKDIR}/afsecd.service ${D}${systemd_system_unitdir}
    ln -s ${systemd_system_unitdir}/afsecd.service ${D}${systemd_system_unitdir}/multi-user.target.wants/
    install -m 644 ${WORKDIR}/afedged.service ${D}${systemd_system_unitdir}
    ln -s ${systemd_system_unitdir}/afedged.service ${D}${systemd_system_unitdir}/multi-user.target.wants/
    install -m 644 ${WORKDIR}/hubby.service ${D}${systemd_system_unitdir}
    ln -s ${systemd_system_unitdir}/hubby.service ${D}${systemd_system_unitdir}/multi-user.target.wants/
}

FILES_${PN} += " ${systemd_system_unitdir}/afsecd.service ${systemd_system_unitdir}/multi-user.target.wants/afsecd.service"
FILES_${PN} += " ${systemd_system_unitdir}/afedged.service ${systemd_system_unitdir}/multi-user.target.wants/afedged.service"
FILES_${PN} += " ${systemd_system_unitdir}/hubby.service ${systemd_system_unitdir}/multi-user.target.wants/hubby.service"

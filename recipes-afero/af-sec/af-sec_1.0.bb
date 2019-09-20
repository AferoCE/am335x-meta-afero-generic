# Copyright (C) 2017-2018 Afero, Inc. All rights reserved

DESCRIPTION = "Afero security daemon and library"
SECTION = "examples"
DEPENDS = "af-ipc af-util openssl libevent"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI = "git://git@github.com/AferoCE/am335x-binaries-af-sec;protocol=ssh"
SRCREV = "05b16cd43e5d048653e9728c3869f68f885dbe36"
SRC_URI += " file://afsecd.service"

S = "${WORKDIR}/git"

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = " afsecd.service"

do_install() {
    # install libafwp.so
    install -d ${D}${libdir}
    install -Dm 755 ${S}/${BUILD_TARGET}${libdir}/libafwp.a ${D}${libdir}
    #ln -s libafwp.so.0.0.0 ${D}${libdir}/libafwp.so
    #ln -s libafwp.so.0.0.0 ${D}${libdir}/libafwp.so.0

    # install afwp.h
    install -d ${D}${includedir}
    install -Dm 644 ${S}/${BUILD_TARGET}${includedir}/afwp.h ${D}${includedir}

    # install afsecd and provision
    install -d ${D}${bindir}
    install -Dm 755 ${S}/${BUILD_TARGET}${bindir}/afsecd ${D}${bindir}
    install -Dm 755 ${S}/${BUILD_TARGET}${bindir}/provision ${D}${bindir}

    # install afsecd systemd script
    install -d ${D}${sysconfdir}
    install -d ${D}${systemd_system_unitdir}
    install -m 644 ${WORKDIR}/afsecd.service ${D}${systemd_system_unitdir}
}

#FILES_${PN} += " ${libdir}/libafwp.so ${libdir}/libafwp.so.0"

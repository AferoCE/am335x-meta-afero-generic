# Copyright (C) 2017-2018 Afero, Inc. All rights reserved

DESCRIPTION = "Afero hub application"
SECTION = "examples"
DEPENDS = "libevent json-c zlib openssl curl af-conn af-sec af-ipc af-util attrd af-edge"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI = "git://git@github.com/AferoCE/am335x-binaries-hubby;protocol=ssh"
SRCREV = "f134cc40e600b4ef880ac19db329faf8beb6b4a5"
SRC_URI += " file://hubby.service"

S = "${WORKDIR}/git"

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = " hubby.service"

do_install() {
    # install profile
    install -d ${D}${sysconfdir}
    install -Dm 644 ${S}/${BUILD_TARGET}${sysconfdir}/hub.profile ${D}${sysconfdir}

    # install hubby
    install -d ${D}${bindir}
    install -Dm 755 ${S}/${BUILD_TARGET}${bindir}/hubby ${D}${bindir}

    # Install the hubby systemd script
    install -d ${D}${systemd_system_unitdir}
    install -Dm 644 ${WORKDIR}/hubby.service ${D}${systemd_system_unitdir}/hubby.service
}

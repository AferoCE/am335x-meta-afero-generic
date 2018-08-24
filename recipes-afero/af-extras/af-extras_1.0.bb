# Copyright (C) 2018 Afero, Inc. All rights reserved

DESCRIPTION = "Afero Extras - configuration and script files"
SECTION = "examples"
DEPENDS = ""
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

TARGET_CFLAGS += "${@base_conditional('BUILD_TARGET','release','-DBUILD_TARGET_RELEASE','-DBUILD_TARGET_DEBUG', d)}"

SRC_URI += " file://logpush"
SRC_URI += " file://otamgr.sh"
SRC_URI += " file://sysupgrade"

S = "${WORKDIR}"

do_install() {
    install -d ${D}${bindir}
    install -d ${D}${sysconfdir}
    install -d ${D}${base_sbindir}

    install -Dm 755 ${WORKDIR}/logpush ${D}${bindir}
    install -Dm 755 ${WORKDIR}/otamgr.sh ${D}${bindir}
    install -Dm 755 ${WORKDIR}/sysupgrade ${D}${base_sbindir}
}


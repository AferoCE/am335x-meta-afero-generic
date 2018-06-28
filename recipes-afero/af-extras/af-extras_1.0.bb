# Copyright (C) 2018 Afero, Inc. All rights reserved

DESCRIPTION = "Afero Dlink Extra - configuration and script files"
SECTION = "examples"
DEPENDS = ""
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

inherit systemd

TARGET_CFLAGS += "${@base_conditional('BUILD_TARGET','release','-DBUILD_TARGET_RELEASE','-DBUILD_TARGET_DEBUG', d)}"

SRC_URI += " file://logpush"
SRC_URI += " file://otamgr.sh"
SRC_URI += " file://af_events.conf"

S = "${WORKDIR}"

# install the files into the destination directory so it can be packaged
# correctly
do_install() {
    echo "do_install() af-extras-dlink script and configuration files ...."

    install -d ${D}${bindir}
    install -d ${D}${sysconfdir}

    install -Dm 755 ${WORKDIR}/logpush ${D}${bindir}
    install -Dm 755 ${WORKDIR}/otamgr.sh ${D}${bindir}
    install -Dm 755 ${WORKDIR}/af_events.conf ${D}${bindir}
}


# Copyright (C) 2017-2018 Afero, Inc. All rights reserved

DESCRIPTION = "Afero Connectivity"
SECTION = "examples"
DEPENDS = "attrd af-ipc af-util libevent libpcap openssl af-sec"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

inherit externalsrc autotools systemd
EXTERNALSRC = "${TOPDIR}/../af-conn/pkg"

EXTRA_OECONF = "WAN_RIL=ELS61 BUILD_TARGET=${BUILD_TARGET}"

PARALLEL_MAKE = ""
SRC_URI += " file://af-conn.service"
SRC_URI += " file://af_conn_watcher"
SRC_URI += " file://configure_wifi.sh"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = " af-conn.service"

do_install_append() {
    install -d ${D}${sysconfdir}
    install -d ${D}${sysconfdir}/af-conn/
    install -d ${D}${libdir}
    install -d ${D}${libdir}/af-conn/

    install -Dm 755 ${WORKDIR}/af_conn_watcher ${D}${libdir}/af-conn/
    install -Dm 755 ${WORKDIR}/configure_wifi.sh ${D}${libdir}/af-conn/

    install -d ${D}${systemd_system_unitdir}
    install -Dm 644 ${WORKDIR}/af-conn.service ${D}${systemd_system_unitdir}

    # see af-conn-files repo
    # Install directories
    for i in `find ${EXTERNALSRC}/../files/bbgw -type d | sed 's/.*bbgw//'` ; do
        install -d ${D}${i}
    done
    # Install files
    for i in `find ${EXTERNALSRC}/../files/bbgw -type f | sed 's/.*bbgw//'` ; do
        cp ${EXTERNALSRC}/../files/bbgw${i} ${D}${i}
    done
    # Install symlinks
    for i in `find ${EXTERNALSRC}/../files/bbgw -type l | sed 's/.*bbgw//'` ; do
        cp -dp ${EXTERNALSRC}/../files/bbgw${i} ${D}${i}
    done

    # install the release/prod version of the whitelist
    cp ${D}${sysconfdir}/af-conn/whitelist.prod ${D}${sysconfdir}/af-conn/whitelist

    rm ${D}${sysconfdir}/af-conn/whitelist.dev
    rm ${D}${sysconfdir}/af-conn/whitelist.prod
}

FILES_${PN} += " ./usr/lib/af-conn/* ./etc/af-conn/*"

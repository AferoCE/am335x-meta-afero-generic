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
SRC_URI += " file://carriers"
SRC_URI += " file://fwcfg"
SRC_URI += " file://switch_route_to.sh"
SRC_URI += " file://wancontrol"
SRC_URI += " file://wannetwork"
SRC_URI += " file://af-conn.service"
SRC_URI += " file://af_conn_watcher"
SRC_URI += " file://afero_net_cap"
SRC_URI += " file://configure_wifi.sh"
SRC_URI += " file://wifi_event.sh"
SRC_URI += " file://afero_get_netif_names"
SRC_URI += " file://afero_netif_names"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = " af-conn.service"

do_install_append() {
    install -d ${D}${bindir}
    install -d ${D}${sysconfdir}
    install -d ${D}${sysconfdir}/config/
    install -d ${D}${base_libdir}
    install -d ${D}${sysconfdir}/wan/
    install -Dm 755 ${WORKDIR}/fwcfg  ${D}${bindir}
    install -Dm 755 ${WORKDIR}/switch_route_to.sh  ${D}${bindir}
    install -Dm 755 ${WORKDIR}/wancontrol  ${D}${bindir}
    install -Dm 755 ${WORKDIR}/wannetwork  ${D}${bindir}
    install -Dm 755 ${WORKDIR}/wifi_event.sh ${D}${bindir}
    install -Dm 644 ${WORKDIR}/afero_get_netif_names ${D}${base_libdir}
    install -Dm 644 ${WORKDIR}/afero_netif_names ${D}${base_libdir}
    install -Dm 755 ${WORKDIR}/af_conn_watcher ${D}${bindir}
    install -Dm 644 ${WORKDIR}/carriers ${D}${sysconfdir}/wan/
    install -Dm 755 ${WORKDIR}/configure_wifi.sh ${D}${bindir}
    install -Dm 755 ${WORKDIR}/afero_net_cap ${D}${bindir}

    install -d ${D}${systemd_system_unitdir}
    install -Dm 644 ${WORKDIR}/af-conn.service ${D}${systemd_system_unitdir}
}

FILES_${PN} += " ${base_libdir}/afero_get_netif_names ${base_libdir}/afero_netif_names"

# Copyright (C) 2017 Afero, Inc. All rights reserved

DESCRIPTION = "Afero Connectivity"
SECTION = "examples"
DEPENDS = "attrd af-ipc af-util libevent libpcap openssl afero-binaries"
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
SRC_URI += " file://wifi_watcher"
SRC_URI += " file://afero_net_cap"
SRC_URI += " file://wl18xx-conf-bbgw.bin"
SRC_URI += " file://start_wifi"
SRC_URI += " file://wifi_event.sh"
SRC_URI += " file://wifistad.service"
SRC_URI += " file://wpa_supp.service"
SRC_URI += " file://connmgr.service"
SRC_URI += " file://afero_get_netif_names"
SRC_URI += " file://afero_netif_names"
SRC_URI += " file://wand.service"

# install the files into the destination directory so it can be packaged
# correctly
do_install_append() {
    echo "do_install() af-conn directories and configuration files ...."

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
    install -Dm 755 ${WORKDIR}/wifi_watcher ${D}${bindir}
    install -Dm 755 ${WORKDIR}/start_wifi ${D}${bindir}
    install -Dm 644 ${WORKDIR}/carriers ${D}${sysconfdir}/wan/

    install -d ${D}${base_libdir}/firmware
    install -d ${D}${base_libdir}/firmware/ti-connectivity
    install -Dm 644 ${WORKDIR}/wl18xx-conf-bbgw.bin ${D}${base_libdir}/firmware/ti-connectivity/wl18xx-conf.bin
    install -d ${D}${systemd_system_unitdir}
    install -d ${D}${systemd_system_unitdir}/multi-user.target.wants/
    install -Dm 644 ${WORKDIR}/wifistad.service ${D}${systemd_system_unitdir}
    install -Dm 644 ${WORKDIR}/wpa_supp.service ${D}${systemd_system_unitdir}
    install -Dm 644 ${WORKDIR}/connmgr.service ${D}${systemd_system_unitdir}
    ln -s ${systemd_system_unitdir}/wifistad.service ${D}${systemd_system_unitdir}/multi-user.target.wants/
    ln -s ${systemd_system_unitdir}/wpa_supp.service ${D}${systemd_system_unitdir}/multi-user.target.wants/
    ln -s ${systemd_system_unitdir}/connmgr.service ${D}${systemd_system_unitdir}/multi-user.target.wants/
#    install -Dm 644 ${WORKDIR}/wand.service ${D}${systemd_system_unitdir}
#    ln -s ${systemd_system_unitdir}/wand.service ${D}${systemd_system_unitdir}/multi-user.target.wants/
    install -Dm 755 ${WORKDIR}/afero_net_cap ${D}${bindir}
}

FILES_${PN} += " ${systemd_system_unitdir}/wifistad.service ${systemd_system_unitdir}/multi-user.target.wants/wifistad.service"
FILES_${PN} += " ${systemd_system_unitdir}/wpa_supp.service ${systemd_system_unitdir}/multi-user.target.wants/wpa_supp.service"
FILES_${PN} += " ${systemd_system_unitdir}/connmgr.service ${systemd_system_unitdir}/multi-user.target.wants/connmgr.service"
FILES_${PN} += " ${systemd_system_unitdir}/wand.service ${systemd_system_unitdir}/multi-user.target.wants/wand.service"
FILES_${PN} += " ${base_libdir}/afero_netif_names ${base_libdir}/afero_get_netif_names"
FILES_${PN} += " ${base_libdir}/firmware/ti-connectivity/wl18xx-conf.bin"

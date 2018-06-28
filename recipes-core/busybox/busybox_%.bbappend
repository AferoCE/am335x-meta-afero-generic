# Copyright (C) 2018 Afero, Inc. All rights reserved

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://enable-crontab.cfg"
SRC_URI += "file://disable-logrotate.cfg"
SRC_URI += "file://busybox-crond.service"
SRC_URI += "file://syslog-startup.conf"
SRC_URI += "file://busybox-syslog.service"
SRC_URI += "file://root"

inherit systemd

# install the files into the destination directory so it can be packaged
# correctly
#
do_install_append() {
    echo "do_install_append() busybox cron and syslog service and configuration files ...."

    install -d ${D}${sysconfdir}
    install -d ${D}${sysconfdir}/crontabs
    install -d ${D}${systemd_unitdir}/system
    install -d ${D}${systemd_system_unitdir}/multi-user.target.wants/

    install -Dm 644 ${WORKDIR}/busybox-crond.service  ${D}${systemd_unitdir}/system
    install -Dm 644 ${WORKDIR}/busybox-syslog.service ${D}${systemd_unitdir}/system
    install -Dm 644 ${WORKDIR}/root  ${D}${sysconfdir}/crontabs
    install -Dm 644 ${WORKDIR}/syslog-startup.conf  ${D}${sysconfdir}

    ln -s ${systemd_system_unitdir}/busybox-crond.service ${D}${systemd_system_unitdir}/multi-user.target.wants/

    echo "adding crontab dir -- /var/spool/cron"
    mkdir -p ${D}/var/spool/cron
    ln -s ${sysconfdir}/crontabs ${D}/var/spool/cron/crontabs
}


FILES_${PN} += " ${systemd_system_unitdir}/multi-user.target.wants/busybox-crond.service"
FILES_${PN} += " ${systemd_unitdir}/system/busybox-crond.service"
FILES_${PN} += " ${systemd_unitdir}/system/busybox-syslog.service"
FILES_${PN} += " ${sysconfdir}/crontabs/root"
FILES_${PN} += " ${sysconfdir}/syslog-startup.conf"


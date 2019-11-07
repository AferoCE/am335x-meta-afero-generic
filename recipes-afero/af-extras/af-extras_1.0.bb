# Copyright (C) 2018 Afero, Inc. All rights reserved

DESCRIPTION = "Afero Extras - configuration and script files"
SECTION = "examples"
DEPENDS = ""
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""
RDEPENDS_${PN} += "bash"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

TARGET_CFLAGS += "${@base_conditional('BUILD_TARGET','release','-DBUILD_TARGET_RELEASE','-DBUILD_TARGET_DEBUG', d)}"

SRC_URI += " file://logpush"
SRC_URI += " file://logpush.conf"
SRC_URI += " file://logpush.attrd"
SRC_URI += " file://otamgr.sh"
SRC_URI += " file://sysupgrade"
SRC_URI += " file://check-af-services.sh"

S = "${WORKDIR}"

do_install() {
    install -d ${D}${bindir}
    install -d ${D}${sysconfdir}
    install -d ${D}${base_sbindir}
    install -d ${D}${sysconfdir}/af_attr.d/

    install -Dm 755 ${WORKDIR}/logpush ${D}${bindir}
    install -Dm 644 ${WORKDIR}/logpush.conf ${D}${sysconfdir}
    install -Dm 755 ${WORKDIR}/otamgr.sh ${D}${bindir}
    install -Dm 755 ${WORKDIR}/sysupgrade ${D}${base_sbindir}
    install -Dm 644 ${WORKDIR}/logpush.attrd ${D}${sysconfdir}/af_attr.d/logpush

    install -Dm 755 ${WORKDIR}/check-af-services.sh ${D}${bindir}
    
}


#*****************************************************************************************
#script run immediately after installing a package on the target or during image creation
# - Note: shell is supported
pkg_postinst_${PN}(){
#!/bin/sh -e
    echo "Checking for OTA record files"
    FULL_OTA_REC_FILE="${TMPDIR}/full_ota_record.json"
    APP_OTA_REC_FILE="${TMPDIR}/app_ota_record.json"

    if [ -e $FULL_OTA_REC_FILE ]; then
       echo " *** find the full_ota_record.json file, install it ***"
       cp $FULL_OTA_REC_FILE  $D${sysconfdir}/
    else
        echo "WARNING: Afero OTA Record file doesn't exists. Run python script"
        echo "Run script: python partner-ota-hub-uploader.py -n <buildNumber> --createOTARecord -c partner-ota-conf.json"
    fi

    if [ -e $APP_OTA_REC_FILE ]; then
       echo " *** find the app_ota_record.json file, install it ***"
       cp $APP_OTA_REC_FILE  $D${sysconfdir}/
     fi
}

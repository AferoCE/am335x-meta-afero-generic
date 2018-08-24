SRCREV = "5040274cae5e88303e8a895c2707628fa72d58e8"
PV = "R8.7"
PR = "r0"
do_install_append() {
    install -d ${D}${sbindir}
    ln -s ${bindir}/wlconf ${D}${sbindir}/wlconf
    install -d ${D}${base_libdir}
    install -d ${D}${base_libdir}/firmware
    install -d ${D}${base_libdir}/firmware/ti-connectivity
    install -m 644 ${S}/../hw/firmware/wl1271-nvs.bin ${D}${base_libdir}/firmware/ti-connectivity
}

FILES_${PN} += " ${sbindir}/wlconf ${base_libdir}/firmware/ti-connectivity/wl1271-nvs.bin"

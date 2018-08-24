SRCREV = "a07b6e711d2a70608101d3d6cdc5749c4d8a96d5"
BRANCH = "sitara-scripts"
PV = "R8.7"
PR = "r0"

do_install() {
    install -d ${D}${datadir}/wl18xx/
    install -m 755 ${S}/drv/* ${D}${datadir}/wl18xx
    install -m 755 ${S}/sta/* ${D}${datadir}/wl18xx
    install -m 755 ${S}/conf/* ${D}${datadir}/wl18xx
}

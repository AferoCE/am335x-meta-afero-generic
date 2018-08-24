SRCREV = "ee8fbdb840d95e048f58fb62bf3b5472041b5417"
BRANCH = "upstream_25_rebase"
PV = "R8.7"
PR = "r0"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://../COPYING;md5=292eece3f2ebbaa25608eed8464018a3 \
                    file://../README;md5=3f01d778be8f953962388307ee38ed2b \
                    file://wpa_supplicant.c;beginline=1;endline=11;md5=06180ad70fe4a9bed87faeb55519e0dc"
FILES_${PN} += "${systemd_unitdir}/system/*"

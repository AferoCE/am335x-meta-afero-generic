#!/bin/sh

if [ ! -f /etc/af-conn/wifi-configured ] ; then
	/sbin/rmmod wlcore_sdio
	cd /usr/sbin/wlconf
	./wlconf -o /lib/firmware/ti-connectivity/wl18xx-conf.bin -I /usr/sbin/wlconf/official_inis/WL1835MOD_INI.ini
	./wlconf -i /lib/firmware/ti-connectivity/wl18xx-conf.bin -o /lib/firmware/ti-connectivity/wl18xx-conf.bin -s wl18xx.phy.number_of_assembled_ant2_4=2
	./wlconf -i /lib/firmware/ti-connectivity/wl18xx-conf.bin -o /lib/firmware/ti-connectivity/wl18xx-conf.bin -s wl18xx.phy.number_of_assembled_ant5=0
	./wlconf -i /lib/firmware/ti-connectivity/wl18xx-conf.bin -o /lib/firmware/ti-connectivity/wl18xx-conf.bin -s wl18xx.phy.high_band_component_type=0x09
	./wlconf -i /lib/firmware/ti-connectivity/wl18xx-conf.bin -o /lib/firmware/ti-connectivity/wl18xx-conf.bin -s wl18xx.ht.mode=0
	./wlconf -i /lib/firmware/ti-connectivity/wl18xx-conf.bin -o /lib/firmware/ti-connectivity/wl18xx-conf.bin -s core.conn.sta_sleep_auth=0
	cd -
	/sbin/modprobe wlcore_sdio
	/bin/touch /etc/af-conn/wifi-configured
fi

#!/bin/bash
#
# [DEV-2430] : this is specific to BBGW TI-SDK for (DLINK based) build.
# Per [HUB=948] the memory leak is due to a bug in libresolv-2.21 
# (And it is not easy to upgrade to libresolv-2.24. This is a hacky fix
# that monitor the memory usage and restart the daemons and let the init
# script restart the daemon).


## check afsecd daemon running 
if [ ! "$(pidof afsecd)" ] 
then
  /usr/bin/afsecd -D /dev/i2c-2 -d
fi
HUBBY_SIZE=`/bin/ps | /bin/grep -w hubby | /bin/grep -v grep | /usr/bin/awk '{ print $3 }'`
if [ $HUBBY_SIZE -ge 60000 ] ; then
logger "killing hubby because its VSIZE is $HUBBY_SIZE..." 
killall hubby
fi

CONNMGR_SIZE=`/bin/ps | /bin/grep -w connmgr | /bin/grep -v grep | /usr/bin/awk '{ print $3 }'`
if [ $CONNMGR_SIZE -ge 30000 ] ; then
logger "killing connmgr because its VSIZE is $CONNMGR_SIZE..." 
killall connmgr
fi

WIFISTAD_SIZE=`/bin/ps | /bin/grep -w wifistad | /bin/grep -v grep | /usr/bin/awk '{ print $3 }'`
if [ $WIFISTAD_SIZE -ge 40000 ] ; then
logger "killing wifistad because its VSIZE is $WIFISTAD_SIZE..." 
killall wifistad
fi

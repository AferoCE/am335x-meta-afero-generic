#!/bin/sh

#
# This script creates the iptables rules supporting the afero whitelist
#

WHITELIST_FILE="/etc/config/afero_whitelist.txt"
IPTABLES="/usr/sbin/iptables"
FWCFG="/usr/bin/fwcfg"


# Master wifi interface
# By default - the bento is standalong and it is a client to the
# customer AP. Bento can became an extender
#
. /lib/afero_get_netif_names
is_bento_ap=0


## input chain whitelist name
AFERO_WHITELIST_INPUT_CHAIN="AFERO_ALLOW_WHITELIST_INPUT"

## output chain whitelist name
AFERO_WHITELIST_OUTPUT_CHAIN="AFERO_ALLOW_WHITELIST_OUTPUT"


is_bento_a_wifi_ap()
{
	local result

	result=`cat /proc/net/dev | grep -i ${WIFIAP_INTERFACE_0} `
	if [ -n "$result" ]; then
		echo "create_afero_whitelist:: this bento is: master"
		logger "create_afero_whitelist:: this bento is: master"
		is_bento_ap=1
	else
		echo "create_afero_whitelist:: this bento is: station"
		logger "create_afero_whitelist:: this bento is: station"
		is_bento_ap=0
	fi
}


# Create a rule for allowing incoming traffic for supported whitelist IP/addr
# needs two arguments
#  wl_addr=$1
#
create_input_whitelist_rules()
{
	local wl_addr=$1

	echo "$IPTABLES --wait -A $AFERO_WHITELIST_INPUT_CHAIN -s $wl_addr -m state --state ESTABLISHED,RELATED -j ACCEPT"
	$IPTABLES --wait -A $AFERO_WHITELIST_INPUT_CHAIN  -s $wl_addr  \
         -m state --state ESTABLISHED,RELATED -j ACCEPT \
		 -m comment --comment "$wl_addr"
}


# Create a rule for allowing outgoing traffic for supported whitelist IP/addr
# needs two arguments
#  wl_addr=$1
#
create_output_whitelist_rules()
{
    local wl_addr=$1

    echo "$IPTABLES --wait -A $AFERO_WHITELIST_OUTPUT_CHAIN -d $wl_addr -m state --state NEW,ESTABLISHED,RELATED -j ACCEPT"
    $IPTABLES --wait -A $AFERO_WHITELIST_OUTPUT_CHAIN  -d $wl_addr \
         -m state --state NEW,ESTABLISHED,RELATED -j ACCEPT \
		 -m comment --comment "$wl_addr"
}


#
# Check to make sure the whitelist file exists
#
if [ -f $WHITELIST_FILE ]; then

	is_bento_a_wifi_ap

	# read the whitelist from the file
	# - remove service name with wildcard as iptables cannot resolve dns name
	#   with wildcard in it.
	#
	result=`grep -vE '^(\s*$|#)|\*' $WHITELIST_FILE`

	echo "create_afero_whitelist:: permit service: $result"
	logger "create_afero_whitelist:: permit service: $result"

	#
	# there are entries from the whitelist file
	#
	if [ -n "$result" ]; then
		#
		# Flush the iptable chains for the whitelist
		#
		logger "Whitelist services: Flash WHTIELIST INPUT and OUTPUT"
		$IPTABLES -F $AFERO_WHITELIST_INPUT_CHAIN
		$IPTABLES -F $AFERO_WHITELIST_OUTPUT_CHAIN

		#
		# for each ADDR/IP specified in the whitelist
		#
        for addr in $result;
        do
            echo "whitelisting service:: Updating WHTIELIST INPUT and OUTPUT for service: $addr"
            logger "Whitelist services:: Updating WHTIELIST INPUT and OUTPUT for service: $addr"

            create_input_whitelist_rules $addr

            create_output_whitelist_rules $addr

            # the BENTO is an AP (i.e - master to an extender)
            # Let's define the FORWARDING accept rules
            if [ $is_bento_ap -eq 1 ]; then

                # adding a rule for this service addr
                echo "Whitelist services:: add forwarding rule: $addr"
                logger "Whitelist services:: add forwarding rule: $addr"

                $FWCFG add_forwarding $addr

            fi
        done
	fi

else
    echo "Whitelist services failed: No such file: $WHITELIST_FILE"
    logger "Whitelist services failed: No such file: $WHITELIST_FILE"
    exit 1
fi

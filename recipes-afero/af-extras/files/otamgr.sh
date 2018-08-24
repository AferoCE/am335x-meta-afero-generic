#!/bin/sh

# Attribute IDs
HUBBY_STATE=51611

# Paths
REBOOT_REASON_PATH=/afero_nv/reboot_reason

announce_hubby_state() {
    logger "otamgr.sh:hubby_state=$1"
}

perform_ota_update() {
    image_path="$1.img"
#    header_path="$1.hdr"

    # Push the logs
    /usr/bin/logpush < /dev/null

    # Update the reboot reason
    /bin/echo "full_ota" > $REBOOT_REASON_PATH

    # Perform the upgrade
    /sbin/sysupgrade $image_path
}

reboot_system() {
    logger "rebooting"
    /bin/sleep 5
    /bin/echo "reboot_command" > $REBOOT_REASON_PATH
    /bin/sync
    /usr/bin/logpush
    /sbin/reboot
}

clear_credentials() {
    # wifistad clears the credentials; we only need to restart hubby
    /bin/sleep 4
    /usr/bin/killall hubby
}

perform_command() {
    case $1 in
        01) reboot_system ;;
        02) clear_credentials ;;
        03) enter_factory_test_mode ;;
        *) ;;
    esac
}

enter_factory_test_mode() {
    # here we would enter factory test mode
    logger enter_factory_test_mode did nothing
}

handle_get() {
    echo "/tmp"
}

handle_notify() {
    logger "handle_notify $1 $2"
    case $1 in
        51611) announce_hubby_state $2 ;;
        51612) perform_ota_update $2 ;;
        65012) perform_command $2 ;;
        *) ;;
    esac
}

[ "x$1x" == "xx" ] && exit 1

case $1 in
    get)  handle_get $2 $3 ;;
    notify) handle_notify $2 $3 ;;
    *) ;;
esac

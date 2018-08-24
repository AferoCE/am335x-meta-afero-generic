# Arago Afero Image
# gives you a small image with kernel and Afero stuff

LICENSE = "CLOSED"

# The size of the uncompressed ramdisk is 8MB
ROOTFS_SIZE = "10240"

IMAGE_INSTALL = " \
    packagegroup-core-boot \
    kernel-modules \
    bash \
    udev-extraconf \
    base-files \
    module-init-tools \
    mtd-utils \
    mtd-utils-ubifs \
    curl \
    thermal-init \
    dbus \
    expat \
    glib-2.0 \
    libxml2 \
    libpcre \
    iptables \
    arago-gpl-notice \
    util-linux-fsck \
    i2c-tools \
    usbutils \
    zlib \
    libevent \
    json-c \
    libstdc++ \
    pru-icss \
    wl18xx-target-scripts \
    wpa-supplicant-wl18xx \
    bt-firmware \
    wl18xx-firmware \
    wlconf \
    iw \
    crda \
    backports \
    af-ipc \
    af-util \
    attrd \
    af-conn \
    af-sec \
    af-edge \
    otamgr \
    hubby \
    af-extras \
    dropbear \
    lrzsz \
"

IMAGE_FEATURES += " package-management debug-tweaks tools-debug eclipse-debug"
IMAGE_FSTYPES = "tar.gz afimg"
export IMAGE_BASENAME = "arago-afero-image"

inherit core-image
inherit afimg

ROOTFS_POSTPROCESS_COMMAND += " afero_dl_generate_sysctl_config ; afero_fix_systemd_network_scripts "

afero_dl_generate_sysctl_config() {
    # systemd sysctl config
    #
    IMAGE_SYSCTL_CONF="${IMAGE_ROOTFS}${libdir_native}/sysctl.d/50-default.conf"

    test -e ${IMAGE_SYSCTL_CONF} && \
        sed -e "/net.ipv4.conf.all.rp_filter/d" -i ${IMAGE_SYSCTL_CONF}
    test -e ${IMAGE_SYSCTL_CONF} && \
        sed -e "/net.ipv4.conf.all.accept_local/d" -i ${IMAGE_SYSCTL_CONF}
    test -e ${IMAGE_SYSCTL_CONF} && \
        sed -e "/net.ipv4.ip_forward/d" -i ${IMAGE_SYSCTL_CONF}

    echo "" >> ${IMAGE_SYSCTL_CONF} && echo "net.ipv4.conf.all.rp_filter = 2" >> ${IMAGE_SYSCTL_CONF}
    echo "" >> ${IMAGE_SYSCTL_CONF} && echo "net.ipv4.conf.all.accept_local = 1" >> ${IMAGE_SYSCTL_CONF}
    echo "" >> ${IMAGE_SYSCTL_CONF} && echo "net.ipv4.ip_forward = 1" >> ${IMAGE_SYSCTL_CONF}
}


afero_fix_systemd_network_scripts() {
    IMAGE_10_ETH_NETWORK="${IMAGE_ROOTFS}${sysconfdir}/systemd/network/10-eth.network"
    if [ -e ${IMAGE_10_ETH_NETWORK} ] ; then
        echo >> ${IMAGE_10_ETH_NETWORK}
        echo "[DHCP]" >> ${IMAGE_10_ETH_NETWORK}
        echo "ClientIdentifier=mac" >> ${IMAGE_10_ETH_NETWORK}
    fi
    IMAGE_30_WLAN_NETWORK="${IMAGE_ROOTFS}${sysconfdir}/systemd/network/30-wlan.network"
    if [ -e ${IMAGE_30_WLAN_NETWORK} ] ; then
        echo >> ${IMAGE_30_WLAN_NETWORK}
        echo "[DHCP]" >> ${IMAGE_30_WLAN_NETWORK}
        echo "ClientIdentifier=mac" >> ${IMAGE_30_WLAN_NETWORK}
    fi
    IMAGE_60_USB_NETWORK="${IMAGE_ROOTFS}${sysconfdir}/systemd/network/60-usb.network"
    if [ -e ${IMAGE_60_USB_NETWORK} ] ; then
        echo >> ${IMAGE_60_USB_NETWORK}
        echo "[DHCP]" >> ${IMAGE_60_USB_NETWORK}
        echo "ClientIdentifier=mac" >> ${IMAGE_60_USB_NETWORK}
    fi
}

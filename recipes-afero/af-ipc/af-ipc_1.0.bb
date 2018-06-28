# Copyright (C) 2017 Afero, Inc. All rights reserved

DESCRIPTION = "Afero IPC"
SECTION = "examples"
DEPENDS = "libevent af-util"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

inherit externalsrc autotools
EXTERNALSRC = "${TOPDIR}/../af-ipc/pkg"

EXTRA_OECONF = "BUILD_TARGET=${BUILD_TARGET}"

PARALLEL_MAKE = ""


# Copyright (C) 2017 Clif Liu <clif@afero.io>

DESCRIPTION = "Afero Binaries Image"
LICENSE = "CLOSED"
SECTION = "Examples"
DEPENDS = "af-sec hubby otamgr af-edge"

IMAGE_INSTALL = "af-sec-dev hubby otamgr af-edge-dev"

inherit core-image

PACKAGE_CLASSES = "package_ipk package_tar"

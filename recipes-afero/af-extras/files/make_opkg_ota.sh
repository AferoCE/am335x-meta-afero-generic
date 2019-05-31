#!/bin/sh

PACKAGES=" \
    af-extras_1.0-r0.*_cortexa8hf-neon.ipk \
    libaf-util0_1.0-r0.*_cortexa8hf-neon.ipk \
    af-ipc_1.0-r0.*_cortexa8hf-neon.ipk \
    attrd_1.0-r0.*_cortexa8hf-neon.ipk \
    af-sec_1.0-r0.*_cortexa8hf-neon.ipk \
    af-conn_1.0-r0.*_cortexa8hf-neon.ipk \
    otamgr_1.0-r0.*_cortexa8hf-neon.ipk \
    hubby_1.0-r0.*_cortexa8hf-neon.ipk \
    af-edge_1.0-r0.*_cortexa8hf-neon.ipk \
"

usage() {
    echo "usage -- make_opkg_ota.sh <outputdir>"
    exit 1
}

[ "x$1" = "x" ] && usage

OUTDIR=$1

if [ ! -e $OUTDIR ] ; then
    echo "$OUTDIR does not exist"
    exit 1
fi

if [ ! -d $OUTDIR ] ; then
    echo "$OUTDIR is not a directory"
    exit 1
fi

SAVE_DIR=$PWD
while [ $PWD != '/' -a ! -d ./build ] ; do
    cd ..
done

if [ $PWD == "/" ] ; then
    echo "$0 does not appear to be in the Yocto directory hierarchy"
    cd $SAVE_DIR
    exit 1
fi

OPKG_DIR=$PWD/build/arago-tmp-external-linaro-toolchain/deploy/ipk/cortexa8hf-neon
cd $SAVE_DIR

CONTENTS=$OUTDIR/contents_$$
rm -rf $CONTENTS
mkdir $CONTENTS

set -e

# Copy the desired ipks from the IPK directory
for i in $PACKAGES ; do
    cp $OPKG_DIR/$i $CONTENTS
done

# tar up the data
tar cf $OUTDIR/update.tar -C $CONTENTS .

rm -rf $CONTENTS

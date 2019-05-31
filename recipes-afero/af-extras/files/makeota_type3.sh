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
	echo "usage -- makeota.sh -o <output_directory> -i <ipk_directory> -l <label> -n <ota_file_name>"
	exit 1
}

output_script() {
	local TMPFILE=$1_tmp

	for i in `echo $PACKAGES` ; do
		echo $i | cut -d '_' -f 1 >> $TMPFILE
	done

	echo '#!/bin/sh' > $1

	for i in `cat $TMPFILE | tac` ; do
		echo "/usr/bin/opkg remove $i" >> $1
	done

	for i in $PACKAGES ; do
		echo "/usr/bin/opkg install $i" >> $1
	done

	chmod 755 $1

	rm $TMPFILE
}

[ "x`which makeself`" = "x" ] && echo "makeself is missing; try sudo apt-get install makeself" && exit 1

while getopts ":o:i:l:n:" opt; do
	case $opt in
		o)
			OUTDIR=$OPTARG ;;
		i)
			IPKDIR=$OPTARG ;;
		l)
			LABEL=$OPTARG ;;
		n)
			NAME=$OPTARG ;;
		\?)
			usage ;;
		:)
			usage ;;
	esac
done

[ "x$OUTDIR" = "x" ] && usage
[ "x$IPKDIR" = "x" ] && usage
[ "x$LABEL" = "x" ] && usage
[ "x$NAME" = "x" ] && usage

if [ ! -d $OUTDIR ] ; then
	echo "output directory $OUTDIR does not exist"
	exit 1
fi

if [ ! -d $IPKDIR ] ; then
	echo "ipk directory $IPKDIR does not exist"
	exit 1
fi

CONTENTS=$OUTDIR/contents_$$
rm -rf $CONTENTS
mkdir $CONTENTS

set -e

# Copy the desired ipks from the IPK directory
for i in $PACKAGES ; do
	cp $IPKDIR/$i $CONTENTS
done

# Create the af_sw.sh script
output_script $CONTENTS/af_sw.sh

# Make the OTA
makeself --gzip --nomd5 --nocrc $CONTENTS $OUTDIR/$NAME "$label" ./af_sw.sh

rm -rf $CONTENTS

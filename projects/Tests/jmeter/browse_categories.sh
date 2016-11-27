#!/bin/sh
#
#
# @author istvan


# -- read config file
LOCALDIR=`dirname $0`;

source "$LOCALDIR"/optutil.sh
source "$LOCALDIR"/calcutil.sh

source "$LOCALDIR"/browse.conf

DISTRO="$CATEGORY_DISTRO";
DISTRO_SAMPLES="$CATEGORY_DISTRO_SAMPLES";

# -- parse command line
#  -s SERVER
#  -v VARMAX
#  -n MAXKEYS
#  -r SEED
#  -d OUTDIR
#  -w WORKDIR
parseopts "s n r d w j" $*;

# -- check for valid values
checkvars "s n r d w j x X";

CSVFILE="$WORKDIR"/"browse_categories_""$SEED"".csv";

# -- retrieve products
sh -e "$LOCALDIR"/fd_browsecats.sh -s "$SERVER" -n "$MAXKEYS" -o "$CSVFILE" -r "$SEED" -x "$DISTRO" -X "$DISTRO_SAMPLES";

HOST=`echo "$SERVER" | cut -f1 -d:`;
PORT=`echo "$SERVER" | cut -f2 -d:`;

if [ -z "$PORT" -o "$PORT" = "$HOST" ]; then
   PORT=80;
fi

RESULTFILE="$WORKDIR"/browsecategories.out;
rm -f "$RESULTFILE";

# -- run test
sh -e "$LOCALDIR"/jmeter.sh -jar "$JMETERJAR" \
      -Jhostname="$HOST" -Jport="$PORT" \
      -Jbrowser_threads="$BROWSER_THREADS" -Jn="$LOOP_COUNTER" \
      -Jtest_user="$TESTUSER" -Juser_pass="$USERPASS" \
      -Jbrowse_data="$CSVFILE" -Jbrowse_output="$RESULTFILE" \
      -l "$WORKDIR/browsecategories_jmeter.log" -n \
      -t "$LOCALDIR"/browse.jmx 

calc_total /category\.jsp/ "$RESULTFILE" > "$OUTDIR"/"$BROWSE_CATEGORIES_STEM"_total.property;
calc_error /category\.jsp/ "$RESULTFILE" 200 > "$OUTDIR"/"$BROWSE_CATEGORIES_STEM"_error.property

cat "$RESULTFILE" | calc_response_and_latency `dirname $0` /category\.jsp/ "$BROWSE_CATEGORIES_STEM" "$OUTDIR"



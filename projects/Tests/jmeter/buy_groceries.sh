#!/bin/sh
#
#
# @author istvan


# -- read config file
LOCALDIR=`dirname $0`;

source "$LOCALDIR"/optutil.sh
source "$LOCALDIR"/calcutil.sh

source "$LOCALDIR"/buy.conf

# -- parse command line
#  -s SERVER
#  -v VARMAX
#  -n MAXKEYS
#  -r SEED
#  -d OUTDIR
#  -w WORKDIR
parseopts "s v n r d w j" $*;

# -- check for valid values
checkvars "s v n r d w j x X";

CSVFILE="$WORKDIR"/"groceries_""$SEED"".csv";

# -- retrieve products
sh -e "$LOCALDIR"/fd_grocprods.sh -s "$SERVER" -v "$VARMAX" -n "$MAXKEYS" -o "$CSVFILE" -r "$SEED" -x "$DISTRO" -X "$DISTRO_SAMPLES";

HOST=`echo "$SERVER" | cut -f1 -d:`;
PORT=`echo "$SERVER" | cut -f2 -d:`;

if [ -z "$PORT" -o "$PORT" = "$HOST" ]; then
   PORT=80;
fi

RESULTFILE="$WORKDIR"/groceries.out;
rm -f "$RESULTFILE";
# -- run test
sh -e "$LOCALDIR"/jmeter.sh -jar "$JMETERJAR" \
      -Jhostname="$HOST" -Jport="$PORT" \
      -Jtest_user="$TESTUSER" -Juser_pass="$USERPASS" \
      -Jgrocery_threads="$GROCERY_THREADS" -Jgrocery_outerloop_counter="$GROCERY_OUTERLOOP_COUNTER" \
      -Jgrocery_loginbuy_counter="$GROCERY_LOGINBUYLOOP_COUNTER" -Jgrocery_saveclear_counter="$GROCERY_SAVECLEAR_COUNTER" \
      -Jgrocery_additem_counter="$GROCERY_ADDITEM_COUNTER" \
      -Jgrocery_data="$CSVFILE" -Jgrocery_output="$RESULTFILE" \
      -l "$WORKDIR/grocery_jmeter.log" -n \
      -t "$LOCALDIR"/buy_groceries.jmx 


calc_total /category\.jsp/ "$RESULTFILE" > "$OUTDIR"/"$GROCERY_STEM"_total.property;
calc_error /category\.jsp/ "$RESULTFILE" 302 > "$OUTDIR"/"$GROCERY_STEM"_error.property

cat "$RESULTFILE" | calc_response_and_latency `dirname $0` /category\.jsp/ "$GROCERY_STEM" "$OUTDIR"


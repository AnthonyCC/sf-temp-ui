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

CSVFILE="$WORKDIR"/"perishables_""$SEED"".csv";

# -- retrieve products
sh -e "$LOCALDIR"/fd_perprods.sh -s "$SERVER" -v "$VARMAX" -n "$MAXKEYS" -o "$CSVFILE" -r "$SEED" -x "$DISTRO" -X "$DISTRO_SAMPLES"; 

HOST=`echo "$SERVER" | cut -f1 -d:`;
PORT=`echo "$SERVER" | cut -f2 -d:`;

if [ -z "$PORT" -o "$PORT" = "$HOST" ]; then
   PORT=80;
fi

RESULTFILE="$WORKDIR"/perishables.out;
rm -f "$RESULTFILE";
# -- run test
sh -e "$LOCALDIR"/jmeter.sh -jar "$JMETERJAR" \
      -Jhostname="$HOST" -Jport="$PORT" \
      -Jtest_user="$TESTUSER" -Juser_pass="$USERPASS" \
      -Jperishable_threads="$PERISHABLE_THREADS" -Jperishable_outerloop_counter="$PERISHABLE_OUTERLOOP_COUNTER" \
      -Jperishable_loginbuy_counter="$PERISHABLE_LOGINBUYLOOP_COUNTER" -Jperishable_saveclear_counter="$PERISHABLE_SAVECLEAR_COUNTER" \
      -Jperishable_additem_counter="$PERISHABLE_ADDITEM_COUNTER" \
      -Jperishable_data="$CSVFILE" -Jperishable_output="$RESULTFILE" \
      -l "$WORKDIR/perishable_jmeter.log" -n \
      -t "$LOCALDIR"/buy_perishables.jmx 


calc_total /product\.jsp/ "$RESULTFILE" > "$OUTDIR"/"$PERISHABLE_STEM"_total.property;
calc_error /product\.jsp/ "$RESULTFILE" 302 > "$OUTDIR"/"$PERISHABLE_STEM"_error.property

cat "$RESULTFILE" | calc_response_and_latency `dirname $0` /product\.jsp/ "$PERISHABLE_STEM" "$OUTDIR"


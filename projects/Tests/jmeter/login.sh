#!/bin/bash
#
#
# @author istvan


# -- read config file
LOCALDIR=`dirname $0`;

source "$LOCALDIR"/optutil.sh
source "$LOCALDIR"/calcutil.sh

source "$LOCALDIR"/login.conf

# -- parse command line
#  -s SERVER
#  -v VARMAX
#  -n MAXKEYS
#  -r SEED
#  -d OUTDIR
#  -w WORKDIR
parseopts "s d w j" $*;

# -- check for valid values
checkvars "s d w j";

HOST=`echo "$SERVER" | cut -f1 -d:`;
PORT=`echo "$SERVER" | cut -f2 -d:`;

if [ -z "$PORT" -o "$PORT" = "$HOST" ]; then
   PORT=80;
fi

RESULTFILE="$WORKDIR"/login_user.out;
rm -f "$RESULTFILE";

# -- run test
bash -e "$LOCALDIR"/jmeter.sh -jar "$JMETERJAR" \
      -Jhostname="$HOST" -Jport="$PORT" \
      -Jbrowser_threads="$BROWSER_THREADS" -Jn="$LOOP_COUNTER" \
      -Jtest_user="$TESTUSER" -Juser_pass="$USERPASS" \
      -Jlogin_output="$RESULTFILE" \
      -l "$WORKDIR/login_jmeter.log" -n \
      -t "$LOCALDIR"/login.jmx 

calc_total /login\.jsp/ "$RESULTFILE" > "$OUTDIR"/"$LOGIN_STEM"_total.property;
calc_error /login\.jsp/ "$RESULTFILE" 200 > "$OUTDIR"/"$LOGIN_STEM"_error.property

cat "$RESULTFILE" | calc_response_and_latency `dirname $0` /login\.jsp/ "$LOGIN_STEM" "$OUTDIR"



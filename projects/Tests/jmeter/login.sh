#!/bin/sh
#
#
# @author saik
echo -1
# -- read config file
echo `dirname $0`
LOCALDIR=`dirname $0`;
source "$LOCALDIR"/optutil.sh
echo 2
source "$LOCALDIR"/calcutil.sh
echo 3
source "$LOCALDIR"/login.conf
echo 4

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
echo 5
PORT=`echo "$SERVER" | cut -f2 -d:`;
echo 6
if [ -z "$PORT" -o "$PORT" = "$HOST" ]; then
   PORT=80;
fi

RESULTFILE="$WORKDIR"/login_user.out;
rm -f "$RESULTFILE";
echo $JMETERJAR

# -- run test
sh -e "$LOCALDIR"/jmeter.sh -jar "$JMETERJAR" \
      -Jhostname="$HOST" -Jport="$PORT" \
      -Jlogin_threads="$LOGIN_THREADS" -Jlogin_outerloop_counter="$LOOP_COUNTER" \
      -Jtest_user="$TESTUSER" -Juser_pass="$USERPASS" \
      -Jlogin_output="$RESULTFILE" \
      -l "$WORKDIR/login_jmeter.log" -n \
      -t "$LOCALDIR"/login.jmx 

calc_total /login\.jsp/ "$RESULTFILE" > "$OUTDIR"/"$LOGIN_STEM"_total.property;
calc_error /login\.jsp/ "$RESULTFILE" 302 > "$OUTDIR"/"$LOGIN_STEM"_error.property

cat "$RESULTFILE" | calc_response_and_latency `dirname $0` /login\.jsp/ "$LOGIN_STEM" "$OUTDIR"



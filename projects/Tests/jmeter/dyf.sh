#!/bin/sh
#
#
# @author segabor

# -- read config file
LOCALDIR=`dirname $0`;
source "$LOCALDIR"/optutil.sh

# source "$LOCALDIR"/calcutil.sh
source "$LOCALDIR"/dyf.conf


# -- parse command line
#  -s SERVER
#  -v VARMAX
#  -n MAXKEYS
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

if [ -z "$LOCALDIR" ]; then
	LOCALDIR=.
fi

CSVFILE="$WORKDIR"/"dyf.csv";
rm -f "$CSVFILE";

# -- run test
sh -e "$LOCALDIR"/jmeter.sh -jar "$JMETERJAR" \
      -Jhostname="$HOST" -Jport="$PORT" \
      -Jdyf_threads="$DYF_THREADS" -Jcounter="$LOOP_COUNTER" \
      -Jdyf_data="$CSVFILE" -Jdyf_output="$CSVFILE" \
      -Jtest_user="$TESTUSER" -Juser_pass="$USERPASS" \
      -l "$WORKDIR/dyf_jmeter.log" -n \
      -t "$LOCALDIR"/dyf.jmx 


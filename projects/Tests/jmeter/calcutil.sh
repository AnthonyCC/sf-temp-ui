#!/bin/sh
#
# @provides calc_response_and_latency <awk-dir> <match> <stem> <outdir>  [ reads data from inputstream ]
# @provides calc_total <match> <data>
# @provides calc_error <match> <data>
#
# @author
#


function calc_total() {
   local MATCH="$1";
   local OUTF="$2";
   awk -F, 'BEGIN {N = 0; } '"$MATCH"' {  ++N; } END { printf("YVALUE=%d\n",N); }' "$OUTF";
}

function calc_error() {
   local MATCH="$1";
   local OUTF="$2";
   local CODE="$3";
   awk -F, 'BEGIN {E = 0; } '"$MATCH"' {  if ($3 != "true" || $2 != '"$CODE"') ++E; } END { printf("YVALUE=%d\n",E); }' "$OUTF";
}

function calc_response_and_latency() {
   local DIRNAME="$1";
   local MATCH="$2";
   local STEM="$3";
   local OUTDIR="$4";

   awk -F, "$MATCH"' { print; }' | awk -F, -v outdir="$OUTDIR" -v columns=1:5 -v files="$STEM"_response:"$STEM"_latency -f "$DIRNAME"/stat.awk 
}

#!/bin/sh
#
# @author istvan

# -- load option utils
source `dirname $0`/optutil.sh

# -- declare help
function help() {
   echo "Run performance test scripts.";
   echo;
   echo "$0 -s <server>[:<port>] -d <output-dir> -w <work-dir> -j <jmeter-jar> -p <prog1> [ -p <prog2>  ... ]";
   echo;
   echo "The parameters -s, -d and -w will be passed to each program.";
   echo;
}

# -- parse command line
parseopts "s d p w j h" $*

# -- check param values
checkvars "s d p j w"

# -- run test scripts
runprogs "$PROGS" 


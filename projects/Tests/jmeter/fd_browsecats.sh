#!/bin/sh
#
# Retrieve product browse urls.
# Command line options:
#
# -s <host>[:<port>] : server hostname and port (COMPULSORY)
# -o <outfile>       : absolute path where the data is to be saved (DEFAULT stdout)
# -n <records>       : maximum number of records to retrieve (DEFAULT ALL)
# -v <varmax>        : the maximum number of configurations to generate,
#                      when the product has multiple skus, sales units and options
#                      (note that this could be extremely large), use -v "" for ALL
# -x <distro>        : name of distribution to use
# -X <sample-size>   : number of smaples to take from distribution
# -r <seed>          : random seed to use (DEFALUT $RANDOM)
# -u                 : print url only
# -h                 : print usage only
#
# @athor istvan

PROG=$0;
BLURB="Retrieve browse category data from server.";

TYPE="categories";
source `dirname $0`/fd_retopts.sh
retrieve "$SERVICE""&notunaiv=true&nothidden=true&produce=true&column_order=bpath&bvar_max=1&output=csv""$DISTROOPTIONS";




#!/bin/sh
#
# Retrieve perishable product data from server.
# Command line options:
#
# -s <host>[:<port>] : server hostname and port (COMPULSORY)
# -o <outfile>       : absolute path where the data is to be saved (DEFAULT stdout)
# -a                 : append file
# -n <records>       : maximum number of records to retrieve (DEFAULT ALL)
# -v <varmax>        : the maximum number of configurations to generate,
#                      when the product has multiple skus, sales units and options
#                      (note that this could be extremely large), use -v "" for ALL
# -r <seed>          : random seed to use (DEFALUT $RANDOM)
# -x <distro>        : name of distribution to use
# -X <sample-size>   : number of smaples to take from distribution
# -u                 : print url only
# -h                 : print usage only
#
# @athor istvan



PROG=$0;
BLURB="Retrieve perishabble product data from server."

source `dirname $0`/fd_retopts.sh

retrieve "$SERVICE""&notunaiv=true&nothidden=true&groc=false&layout=PERISHABLE""$DEFAULTOPTIONS""$VAROPTIONS""$USEROPTIONS""$DISTROOPTIONS";


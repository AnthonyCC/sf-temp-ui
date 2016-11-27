#!/bin/sh
#
# Parse command-line options for retrieving product data from server.
#
# @param PROG              name of program, used in printing error messages
# @param BLURB             one liner program description
#
# @return SERVICE          the URL of the data generator service (e.g. http://www.freshdirect.com/test/data/data.jsp)
# @return DEFAULTOPTIONS   some very likely default options to the data generator service, such as column order, CSV output
# @return USEROPTIONS      number of products to retrieve, random seeds and variations options (for the case when the product
#                          has multiple skus, sales units and options)
# @return U                print only the URL, "" for false, "true" for true
# @return A                append, "" for false, "true" for true
# 
# @provides rempty()       a function which removes empty lines
# @provides help()         a function which prints usage
# @provides retrieve()     retrieve data 
#
# @author istvan


function help() {
   if [ ! -z "$BLURB" ]; then
      echo "$BLURB";
      echo;
   fi
   echo "$PROG -h";
   echo "$PROG -s <host>[:<port>] [-n <records> ] [ -o <outfile> ] [ -v <varmax> ] \\";
   echo "      [ -o <seed> ] [ -u ] [ -a ] [ -x <distro> -X <samples> ]";
   echo;
   echo "where"
   echo "   -h                 : this message";
   echo "   -s <host>[:<port>] : running freshdirect server";
   echo "   -v <varmax>*       : maximum numbef of configurations to generate from the same product (default 1)";
   echo "   -n <records>       : maximum number of records to retrieve (default ALL)";
   echo "   -o <outfile>       : output file (default stdout)";
   echo "   -x <distro>        : name of distribution file (default __none)";
   echo "   -X <samples>       : number of samples to take from distribution (default 1000), ignored when <distro> is __none";
   echo "   -a                 : append to existing file (default FALSE)";
   echo "   -r <seed>          : random seed (default \$RANDOM)";
   echo "   -u                 : only print url and do not retrieve data";
   echo;
   echo "* the tool can generate all possible configurations of a given product id (which can be enourmous"
   echo "  for products with many skus and options). -v 0 will generate ALL options.";
   echo;
}

source `dirname $0`/optutil.sh

function rempty() {
   awk '/[,a-zA-Z0-9_]/ {print $0;}';
}

# -- defaults
if [ -z "$VARMAX" ]; then
   VARMAX=1;
fi

if [ -z "$SEED" ]; then
   SEED="$RANDOM";
fi

if [ -z "$DISTRO" ]; then
   DISTRO="__none";
fi

if [ -z "$DISTRO_SAMPLES" ]; then
   DISTRO_SAMPLES="1000";
fi

# -- parse opts
parseopts "s v n o a r u h x X" $*;

# -- ensure we have a server
checkvars "s"

if [ -z "$TYPE" ]; then
   TYPE="products";
fi

SERVICE="http://$SERVER/test/data/data.jsp?type=$TYPE";
DEFAULTOPTIONS="&produce=true&column_order=product_id.category_id.perm_skus.min_quantity.perm_salesunits.perm_options&output=csv";

if [ ! -z "$DISTRO" ]; then
   DISTROOPTIONS="&distro=""$DISTRO""&sample_size=""$DISTRO_SAMPLES";
fi

if [ -z "$VARMAX" -o "$VARMAX" -eq 0 ]; then
   VAROPTIONS="&varperm=all";
elif [ "$VARMAX" -eq 1 ]; then
   VAROPTIONS="&varperm=one";
else
   VAROPTIONS="&varperm=number&varmax=$VARMAX";
fi

USEROPTIONS="&max_content=$MAXKEYS&seed=$SEED";

function retrieve() {

   local URL="$1";
   if [ -z "$URL" ]; then return; fi;
   if [ "$U" = "true" ]; then
      echo "$URL";
   else
      if [ -z "$OUTPUT" ]; then
         wget -O"-" "$URL" 2>/dev/null | rempty;
      elif [ -z "$A" ]; then
         wget -O"-" "$URL" 2>/dev/null | rempty > $OUTPUT;
      else 
         wget -O"-" "$URL" 2>/dev/null | rempty >> $OUTPUT;
      fi
   fi
}



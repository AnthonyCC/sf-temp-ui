#!/bin/sh
#
#  Options utility for FD performance test scripts.
#  
#  Provides the following functions:
#
#  checkfile <file>: 
#       checks whether file exists, otherwise exits program
#       with return code 1.
#
#  checkfir <dir>:
#       checks whether directory exists, otherwise exits program
#       with return code 1.
#
#  parseopts <opts> <command-line>
#       parases the command line parameters, opts should be a space
#       separated list of option characters. Their semantics is
#       described below. The corresponding variables will be set.
#       e.g. parseopts "s o f w" $*
#       Any error is printed on stderr and the program exists with
#       return code 1.
#
#  checkexists <var> <var-name>
#       checks whether the variable var is not zero. Otherwise prints
#       an error message and exits the program with return code 1.
#
#  checkvars <opts>
#       checks whether the variables corresponding to the options opts
#       are all set; otherwise prints an error on stderr and exits the
#       program with return code 1.
#
#  runprogs <progs>
#       runs the programs (as sh -e <prog>) in progs with the parameters
#       -s, -d, and -w; these parameters are also checked for sensible
#       values.
#
#  -- OPTIONS --
#  syntax       command-line-arg      variable     semantics
#  
#   -s         <server>[:port]        SERVER       FD server
#   -o         <output-file>          OUTPUT       
#   -f         <input-file>           INPUT
#   -w         <work-dir>             WORKDIR
#   -d         <output-dir>           OUTDIR
#   -j         <jmeter-jar-file>      JMETERJAR
#   -r         <seed>                 SEED
#   -x         <distribution>         DISTRO
#   -X         <sample-size>          DISTRO_SAMPLES
#   -p         <program>              PROGS        programs to execute
#   -c         <conf-file>            CONF         configuration file
#   -v         <var-max>              VARMAX       max variations from one prod
#   -n         <records>              MAXKEYS      max content keys to match
#   -a                                A            append
#   -u                                U            URL only
#   -h                                H            help
#
# @author istvan

function checkfile() {
   if [ ! -f "$1" ]; then
      echo "File $1 does not exist." 1>&2;
      exit 1;
   fi
}

function checkdir() {
   if [ ! -d "$1" ]; then
      echo "Directory $1 does not exist." 1>&2;
      exit 1;
   fi
}

function parseopts() {
   local C=":";
   local OPTS="$1";   
   if [ -z "$OPTS" ]; then return; fi;
   shift 1;
   for option in $OPTS; do
      case $option in
         s) C="$C""s:";;
         o) C="$C""o:";;
         f) C="$C""f:";;
         w) C="$C""w:";;
         d) C="$C""d:";;
	 x) C="$C""x:";;
	 X) C="$C""X:";;
         p) C="$C""p:";;
         r) C="$C""r:";;
         c) C="$C""c:";;
         n) C="$C""n:";;
         v) C="$C""v:";;
         j) C="$C""j:";;
         a) C="$C""a" ;;
         u) C="$C""u" ;;
         h) C="$C""h" ;;
         ?) echo "Internal: invalid option parameter $option" 1>&2; exit 1;;
      esac
   done

   if [ -z "$C" ]; then return; fi;
   while getopts :"$C" option; do
      case $option in
         s) SERVER="$OPTARG";;
         o) OUTPUT="$OPTARG";;
         f) INPUT="$OPTARG";
            checkfile "$INPUT";;
         w) WORKDIR="$OPTARG";
            checkdir "$WORKDIR";;
         d) OUTDIR="$OPTARG";
            checkdir "$OUTDIR";;
         p) PROGS="$PROGS"" ""$OPTARG";
            checkfile "$OPTARG";;
         c) CONF="$OPTARG";
            checkfile "$CONF";;
         n) MAXKEYS="$OPTARG";;
	 x) DISTRO="$OPTARG";;
	 X) DISTRO_SAMPLES="$OPTARG";;
         v) VARMAX="$OPTARG";;
         j) JMETERJAR="$OPTARG";;
         r) SEED="$OPTARG";;
         a) A="true";;
         u) U="true";;
         h) help; exit 0;;
         ?) echo "Invalid option: $OPTARG." 1>&2; exit 1;;
      esac
   done
}

function checkexists() {
   if [ -z "$1" ]; then
      echo "Internal: variable $2 not assigned" 1>&2;
      exit 1;
   fi
}

function checkvars() {
   local OPTS="$1";
   for option in $OPTS; do
      case $option in
         s) checkexists "$SERVER" SERVER;;
         o) checkexists "$OUTPUT" OUTPUT;; 
         f) checkexists "$INPUT" INPUT;
            checkfile "$INPUT";;
         w) checkexists "$WORKDIR" WORKDIR;
            checkdir "$WORKDIR";;
         d) checkexists "$OUTDIR" OUTDIR;
            checkdir "$OUTDIR";;
         p) checkexists "$PROGS" PROGS; 
            for program in $PROGS; do
               checkexists "$program";
            done;;
         c) checkexists "$CONF" CONF; 
            checkfile "$CONF";;
         v) checkexists "$VARMAX" VARMAX;;
         j) checkexists "$JMETERJAR" JMETERJAR;
            checkfile "$JMETERJAR";;
         n) checkexists "$MAXKEYS" MAXKEYS;;
         r) checkexists "$SEED" SEED;
      esac
   done
}

function runprogs() {
   PROGS="$1";
   checkvars "s d w j";
   for program in $PROGS; do
      sh -e $program -d "$OUTDIR" -s "$SERVER" -w "$WORKDIR" -j "$JMETERJAR";
   done
}


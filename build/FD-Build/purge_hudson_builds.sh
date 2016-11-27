#!/bin/sh
#-------------------------------------------------------------------------------
#  A script to purge hudson builds.
#
#  Purging is done by removing the build artifacts from hudson builds that
#  are too old. The build itself is retained, with the build output,
#  statistics, etc. Only stored explicit artifacts are remvoed.
#-------------------------------------------------------------------------------


#-------------------------------------------------------------------------------
#  Print the usage of this script
#-------------------------------------------------------------------------------
printUsage() {
    echo "Script to purge hudson builds.";
    echo "This script will remove artifacts from hudson builds, but keep";
    echo "the builds themselves.";
    echo "";
    echo "options:";
    echo "  -b, --builddirs the base directory of where the hudson builds are.";
    echo "  -d, --days      the number of days to keep artifacts.";
    echo "                  all artifacts older than these days are deleted.";
    echo "  -h, --help      print this message.";
    echo "  -n, --noop      don't purge, just show what would be done";
    echo "";
}


#-------------------------------------------------------------------------------
#  Tell if a hudson build dir is too old, and thus needs to be purged
#
#  @param date - the purge date, in YYYYMMDD format
#  @param dirname - the hudson build directory name, in YYYY-MM-DD_HH-MM-SS
#         format
#  @return 0 of the directory need not be purged, non-0 otherwise
#-------------------------------------------------------------------------------
needsToBePurged() {
    date=$1
    dirname=$2

    # generate the date in short form from the directory name
    # that is, YYYYMMDD
    shortdirname=`echo "$dirname" | cut -b -10 | sed -e s/-//g`

    if [ "$shortdirname" -lt "$date" ]; then
        return 1;
    else
        return 0;
    fi
}
    

#-------------------------------------------------------------------------------
#  Process comamnd line parameters.
#-------------------------------------------------------------------------------
CMD=${0##*/}

opts=$(getopt -o b:d:hn -l builddirs:,days:,help,noop -n $CMD -- "$@") || exit 1
eval set -- "$opts"
while true; do
    case "$1" in
        -b|--builddirs)
            build_directory=$2;
            shift; shift;;
        -d|--days)
            days=$2;
            shift; shift;;
        -h|--help)
            printUsage;
            exit 0;;
        -n|--noop)
            do_purge="false";
            shift;;
        --)
            shift;
            break;;
        *)
            echo "Unrecognized option $1";
            printUsage;
            exit 1;
    esac
done


if [ "x$build_directory" == "x" ]; then
    echo "Required parameter builddirs not specified.";
    printUsage;
    exit 1;
fi

if [ "x$days" == "x" ]; then
    echo "Required parameter days not specified.";
    printUsage;
    exit 1;
fi

if [ "x$do_purge" == "x" ]; then
    do_purge="true";
fi


echo "Purging hudson builds with the following parameters:";
echo "";
echo "    build directory: $build_directory";
echo "    keep days:       $days";
echo "    really purging:  $do_purge";
echo "";


#-------------------------------------------------------------------------------
#  Calculate the cutoff date in YYYYMMDD format (one big number)
#-------------------------------------------------------------------------------
cutoff_date=`date -d "now - $days days" +%Y%m%d`


#-------------------------------------------------------------------------------
#  Look at the directories, and purge them if needed
#-------------------------------------------------------------------------------
cd $build_directory
dirs=`ls`

for dir in $dirs; do
    needsToBePurged $cutoff_date $dir;
    if [ ! "$?" -eq "0" ]; then
        echo -n "purging build $dir ...";
        if [ "$do_purge" == "true" ]; then
            rm -rf $dir/archive;
        fi
        echo " done.";
    else
        echo "not purging build $dir ...";
    fi
done


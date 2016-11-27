#!/bin/bash
export JAVA_HOME=/usr/local/j2sdk1.4.2_04
export PATH=$JAVA_HOME/bin:$PATH

export RUN_HOME=/home/oracle/xls

FILEDATE=`date '+%y%m%d%H%M%S'`

LOG=$RUN_HOME/run.log

export FILE=$RUN_HOME/$1/$1_$FILEDATE.prn

echo "`date` Running for $1 $FILEDATE" >> $LOG
export CLASSPATH=$RUN_HOME/classes12.jar:$RUN_HOME/dom4j.jar:$RUN_HOME/jaxen-1.1-beta-4.jar:$RUN_HOME/poi.jar:$RUN_HOME/fdxls.jar

java -Xmx768m com.freshdirect.flatfile.Processor $RUN_HOME/$1/template.xml $FILE >> $LOG

export EMAIL=ApplicationDevelopment@freshdirect.com

LINES=`cat $FILE | wc -l | grep 1`

if [ "$LINES" != "      1" ]
then
  /usr/local/bin/mutt -s "$3 $1: $FILEDATE" $2 <  $FILE >> $LOG 
fi

echo "`date` Task complete for $1 $FILEDATE" >> $LOG

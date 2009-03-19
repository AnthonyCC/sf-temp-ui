#!/bin/sh
#
# @author Gabor Sebestyen (segabor@euedge.com)
#

# Put these environment variables to file <FD source root>/fd_env
#
# JAVA_HOME=<JDK 1.4.2 home path>
# WL_HOME=<WebLogic 8.1 server home>   ex.: /bea/weblogic81/server
# YK_HOME=<YourKit installation home>  ex.: /home/segabor/Apps/yjp-7.5.11


if [ ! -d projects -o ! -d properties ]; then
  echo "Please run this script from the top FD source folder."
	exit 1
fi

if [ ! -f properties/fd_env ]; then
  echo "Missing file fd_env, exiting ..."
	exit 1
fi

. ./properties/fd_env

FD_BASE=`pwd`
FD_LIBS=$FD_BASE/lib

MYCP=$JAVA_HOME/lib/tools.jar:\
$WL_HOME/lib/weblogic_sp.jar:\
$WL_HOME/lib/weblogic.jar:\
$WL_HOME/lib/webservices.jar:\
$WL_HOME/lib/ojdbc14.jar:\
$WL_HOME/lib/webserviceclient.jar:\
$FD_LIBS/thirdparty/oscache.jar:\
$FD_LIBS/thirdparty/struts.jar:\
$FD_LIBS/thirdparty/log4j.jar:\
$FD_LIBS/thirdparty/commons-lang-2.3.jar:\
$FD_LIBS/thirdparty/commons-httpclient-2.0.2.jar:\
$FD_LIBS/thirdparty/commons-net-1.0.jar:\
$FD_LIBS/thirdparty/commons-logging-1.0.3.jar:\
$FD_LIBS/thirdparty/jakarta-oro-2.0.5.jar:\
$FD_LIBS/thirdparty/hivemind-1.1.1.jar:\
$FD_LIBS/thirdparty/hivemind-lib-1.1.1.jar:\
$FD_LIBS/thirdparty/ognl-2.6.7.jar:\
$FD_LIBS/thirdparty/xpp3-1.1.3.4d_b4_min.jar:\
$FD_LIBS/thirdparty/xstream-1.1.1.jar:\
$FD_LIBS/thirdparty/commons-collections-3.1.jar:\
$FD_LIBS/thirdparty/commons-beanutils-1.6.1.jar:\
$FD_LIBS/thirdparty/commons-digester-1.5.jar:\
$FD_LIBS/thirdparty/commons-codec-1.3.jar:\
$FD_LIBS/thirdparty/bsf-2.3.0.jar:\
$FD_LIBS/thirdparty/aopalliance-1.0.jar:\
$FD_LIBS/thirdparty/backport-util-concurrent.jar:\
$FD_LIBS/thirdparty/quartz.jar:\
$FD_LIBS/thirdparty/hibernate3.jar:\
$FD_LIBS/thirdparty/asm.jar:\
$FD_LIBS/thirdparty/cglib-2.1.jar:\
$FD_LIBS/thirdparty/dom4j-1.6.1.jar:\
$FD_LIBS/thirdparty/jaxen-1.1-beta-4.jar:\
$FD_LIBS/thirdparty/bsh-2.0b4.jar:\
$FD_LIBS/thirdparty/ehcache-1.1.jar:\
$FD_LIBS/thirdparty/commons-fileupload-1.1.1.jar:\
$FD_LIBS/thirdparty/commons-io-1.3.2.jar:\
$FD_LIBS/thirdparty/sapjco.jar:\
$FD_LIBS/thirdparty/lcc.jar:\
$FD_LIBS/thirdparty/jgroups-2.2.7.jar:\
$FD_LIBS/thirdparty/slide/jakarta-slide-webdavlib-2.1.jar:\
$FD_LIBS/thirdparty/jug-lgpl-2.0.0.jar:\
$FD_LIBS/thirdparty/freemarker-2.3.8.jar:\
$FD_LIBS/thirdparty/poi.jar:\
$FD_LIBS/thirdparty/lucene-core-2.2.0.jar:\
$FD_LIBS/thirdparty/lucene-spellchecker-2.2.0.jar:\
$FD_LIBS/thirdparty/lucene-queries-2.2.0.jar:\
$FD_LIBS/thirdparty/javassist-3.4.jar:\
$FD_LIBS/thirdparty/jsonrpc-1.0.jar:\
$FD_LIBS/thirdparty/jxl.jar:\
$FD_LIBS/thirdparty/opencsv-1.8.jar:\
$FD_LIBS/thirdparty/microba-0.4.4.2.jar:\
$FD_LIBS/thirdparty/spring.jar:\
$FD_LIBS/thirdparty/JFEP-0.1.0.jar:\
$FD_LIBS/thirdparty/axis.jar:\
$FD_LIBS/thirdparty/axis-ant.jar:\
$FD_LIBS/thirdparty/jaxrpc.jar:\
$FD_LIBS/thirdparty/saaj.jar:\
$FD_LIBS/thirdparty/wsdl4j-1.5.1.jar:\
$FD_LIBS/thirdparty/commons-discovery-0.2.jar:\
$FD_LIBS/thirdparty/commons-logging-1.0.4.jar:\
$FD_LIBS/thirdparty/roadnetservices.jar:\
$FD_LIBS/thirdparty/tapestry-4.0.2.jar:\
$FD_LIBS/thirdparty/tapestry-contrib-4.0.2.jar:\
$FD_LIBS/thirdparty/tacos-core-4.0.2-20070420.095420-14.jar:\
$FD_LIBS/thirdparty/slide/slide-kernel-2.1.jar:\
$FD_LIBS/thirdparty/slide/slide-webdavservlet-2.1.jar:\
$FD_LIBS/thirdparty/commons-dbcp-1.2.1.jar:\
$FD_LIBS/thirdparty/commons-pool-1.2.jar:\
$FD_LIBS/thirdparty/commons-transaction-1.0.jar:\
$FD_LIBS/thirdparty/slide/geronimo-jta.jar:\
$FD_LIBS/thirdparty/slide/jakarta-slide-commandline-2.1.jar:\
$FD_LIBS/thirdparty/slide/jaxen-core.jar:\
$FD_LIBS/thirdparty/slide/jaxen-jdom.jar:\
$FD_LIBS/thirdparty/slide/jdom-1.0.jar:\
$FD_LIBS/thirdparty/slide/PDFBox-0.6.5.jar:\
$FD_LIBS/thirdparty/slide/saxpath.jar:\
$FD_LIBS/thirdparty/slide/slide-jaas-2.1.jar:\
$FD_LIBS/thirdparty/slide/slide-jdk14logger-2.1.jar:\
$FD_LIBS/thirdparty/slide/slide-log4jlogger-2.1.jar:\
$FD_LIBS/thirdparty/slide/slide-roles-2.1.jar:\
$FD_LIBS/thirdparty/slide/slide-stores-2.1.jar:\
$FD_LIBS/thirdparty/slide/tm-extractors-0.4.jar:\
$FD_LIBS/thirdparty/slide/xml-im-exporter1.1.jar:\
$FD_BASE/properties:\
$FD_BASE/projects/Framework/bin:\
$FD_BASE/projects/ERPSWebApp/bin:\
$FD_BASE/projects/OCF/bin:\
$FD_BASE/projects/DlvAdmin/bin:\
$FD_BASE/projects/RulesAdmin/bin:\
$FD_BASE/projects/CMS/bin:\
$FD_BASE/projects/Tools/bin:\
$FD_BASE/projects/FDStore/bin:\
$FD_BASE/projects/ERPServices/bin:\
$FD_BASE/projects/cms-slide/bin:\
$FD_BASE/projects/DataLoader/bin:\
$FD_BASE/projects/RoutingServices/bin:\
$FD_BASE/projects/Tests/bin:\
$FD_BASE/projects/cms-tap/bin:\
$FD_BASE/projects/Delivery/bin:\
$FD_BASE/projects/Resources


cd "$FD_BASE/domains/freshdirect"


LD_LIBRARY_PATH="$YK_HOME/bin/linux-x86-32":$LD_LIBRARY_PATH
export LD_LIBRARY_PATH


$JAVA_HOME/bin/java -Xdebug -Xnoagent -Xms256m -Xmx1536m \
-Dorg.apache.tapestry.disable-caching=false \
-Dbea.home="$WL_HOME" \
-Dweblogic.Name=myserver \
-Dweblogic.management.username=weblogic \
-Dweblogic.management.password=weblogic \
-Dweblogic.ProductionModeEnabled=false \
-Djava.security.policy=$WL_HOME/lib/weblogic.policy \
-Xrunyjpagent:disablestacktelemetry,delay=10000,sessionname=WebLogic \
-classpath $MYCP \
weblogic.Server

set POI_JAR=C:\Software\xls\poi.jar
set DOM4J_JAR=C:\Software\xls\dom4j.jar
set JAXEN_JAR=C:\Software\xls\jaxen-1.1-beta-4.jar
set ORA_JAR=C:\Software\xls\classes12.jar

set XLS_JARS=C:\Software\xls\fdxls.jar

set CLASSPATH=%POI_JAR%;%DOM4J_JAR%;%JAXEN_JAR%;%ORA_JAR%;.

set CLASSPATH=%CLASSPATH%;%XLS_JARS%

java com.freshdirect.xls.Processor blank.xls template.xml output.xls
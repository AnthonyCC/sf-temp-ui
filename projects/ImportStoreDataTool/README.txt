=============
OVERVIEW
=============

This tool can import CMS data into a DB for a chosen user from XML file published by another CMS editor.
It also enables the user to switch the CMS DB user which provides the CMS data for all other systems.
As a result of these the tool enables its users to maintain multiple CMS data set instances in parallel.

=============
KNOWN ISSUES
=============                                  
--create_schema throws an error if CMSStoreDef.xml contains labels with apostrophes (e.g. President's Picks)

TODO 		change XmlToDbDataDef.xsl:344 and below to duplicate apostrophes
Workaround:	duplicate apostrophes in [base path]/CMSStoreDef.xml before running tool


================
BUILD WITH MAVEN
================

Issue the following command in your terminal window:

  cd projects
  mvn -Dwl.home=<WebLogic server home> package -pl ImportStoreDataTool -am

where wl.home points to your WebLogic server installation directory.


=============
CONFIGURATION
=============

The tool must be configured before using it!
 
Create a file called importtool.properties based on the template in the same folder.

Property         | Description
---------------------------------------------------------------------------------------------------------
jdbc.url         : The jdbc URL to access the DB the tool is working on. 
                   E.g.: jdbc:oracle:thin:@zetor:1521:DBEU02

cms.user         : The name of the DB user where the CMS data will be stored. Warning! All DB objects 
                   own by this user will be dropped when calling --drop_schema. E.g.cms_01

cms.password     : The password of the DB user where the CMS data will be stored. E.g.: cms_01

grant.user       : The names of the users which will be granted permissions to access objects of cms.user.
                   The synonyms of these users will be redirected to the objects of cms.user. The current 
                   system requires this property to be: fdstore_prda,fdstore_prdb

Adjust log4j.xml in case you want toggle log message levels.


=============
USAGE
=============

First of all the tool has to be built and deployed using the regular build/build.xml script. This step is
done automatically by Hudson/Jenkins (although it can be performed manually by calling the importtool-app 
target). The tool can be run by the following shell command:

java -jar ImportStoreDataTool.jar [arguments]

The tools offers the following features which can be accessed with these arguments. All are optional.

Argument         | Description
---------------------------------------------------------------------------------------------------------
--create_user    : Prints a script for DB user creation. This script should be executed manually in the
                   CMS DB with the system user. In case of this argument all other arguments are ignored.

--create_schema  : Creates the CMS schema in DB with the user specified in the properties file. The CMS 
                   DB has to already exist when calling this option. To overwrite a previous schema it 
                   needs to be called with or after --drop_schema.

--drop_schema    : Drops the CMS schema in DB with the user specified in the properties file. Undos the 
                   affects of --create_schema.

--load_definition: Imports CMS data definitions into the DB from the CMSStoreDef.xml. Needs to be called
                   with or after --create_schema option.

--import_data    : Imports CMS data of all stores into the DB from the Store.xml.gz files under 'storedata' folder.
                   Needs to be called with or after --load_definition option.
                   The permission and draft related tables have to be filled with data manually. An example
                   script can be found in the cmsadmin project under src/main/resources/import.sql

--import_media   : Imports CMS media into the DB from the Media.xml.gz.
                   Needs to be called with or after --load_definition option.

--switch_cms     : Prints several scripts for changing the linked CMS user in the DB and for adjusting
                   permissions. After this all systems using this DB will use this user as source of CMS
                   data. This scripts should be executed manually in the CMS DB with the users specified
                   in the scripts. Needs to be called with or after --import_data option.

[base path]      : If set, this directory will be searched for when trying to open CMSStoreDef.xml, 
                   Store.xml.gz and Media.xml.gz can be found. If not set the current directory is used.
                   Store.xml.gz and Media.xml.gz are outputs of a CMS publish while CMSStoreDef.xml can 
                   be found under projects/CMS/src/com/freshdirect/cms/resource/CMSStoreDef.xml in every 
                   branch of the source code.

                                  
Alternatively the tool can be run from Eclipse using a launch file. Right click ImportTool.launch in the
explorer and select "Run as" then "Run configurations...". Program arguments can be set on the 
Arguments sheet. Once the arguments are set, click Run to execute.

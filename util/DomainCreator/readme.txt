see following pages for more details
-http://home.freshdirect.com/confluence/display/web/Create+WebLogic+domain+with+DomainCreator
-http://jira.freshdirect.com:8080/jira/browse/APPDEV-1266 Create WebLogic domain programatically



Weblogic times out during app deployment (phase #3) work log
============================================================

Current deployment steps:
-------------------------

-comment out cms-gwt in .../projects/DevServer/META-INF/application.xml
-run domain creator, deployment should be successful
-after a successful deployment put cms-gwt back to application.xml
-start the server .../domain_dir/startWebLogic.sh

-successful log message e.g.:
	Deploying application from /home/tamas/src/FreshDirect/01/projects/DevServer to targets tyukasz,crm@crmtyukasz (upload=false) ...
	<Apr 26, 2011 1:37:19 PM CEST> <Info> <J2EE Deployment SPI> <BEA-260121> <Initiating deploy operation for application, DevServer [archive: /home/tamas/src/FreshDirect/01/projects/DevServer], to crmtyukasz tyukasz .> 
	.........Completed the deployment of Application with status completed

-deadlock log message (number of dots in the last row is increasing) e.g.:
	Deploying application from /home/tamas/src/FreshDirect/01/projects/DevServer to targets tyukasz,crm@crmtyukasz (upload=false) ...
	<Apr 26, 2011 1:37:19 PM CEST> <Info> <J2EE Deployment SPI> <BEA-260121> <Initiating deploy operation for application, DevServer [archive: /home/tamas/src/FreshDirect/01/projects/DevServer], to crmtyukasz tyukasz .> 
	...............................................................................



Useful info:
------------

Modifications: 
-DomainCreator: bin/startWeblogic.sh writes its output to wl.log under the domain directory.
-stage3.py: no need for edit(), startEdit(), save() and activate() when calling deploy() - these have been removed
-stage3.py: timeout=0 - so it doesn't time out

-wlst reference: http://download.oracle.com/docs/cd/E13222_01/wls/docs92/config_scripting/reference.html

-manual wlst:
	start: .../bea/wlserver_10.3/common/bin/wlst.sh
	type in commands, e.g.:
	connect('weblogic','weblogic','t3://tyukasz:7001')
	deploy('DevServer','/home/tamas/src/FreshDirect/01/projects/DevServer',targets='tyukasz,crm@crmtyukasz', timeout=0, block='true')

-tools: gdb, lsof, jconsole 



Fast debugging:
---------------

-make a copy of domain dir after first two stages:
	-put breakpoint into DomainCreator.java before: runStageThree()
	-start app in debug mode
	-when app stops at breakpoint copy domain_dir (domain_dir_pre_stage_3)
	-let app complete

-run stage three only	
	-comment out creator.create(); in DomainCretator.main()
	-uncomment creator.runStageThree(); in DomainCretator.main()
	-comment out } catch (CreateDomainException e) { in DomainCretator.main()
 	-delete domain dir
 	-copy domain_dir_pre_stage_3 to domain_dir
 	-start app



Modifications tried out to solve deadlock unsuccessfully:
---------------------------------------------------------

-comment out everything in weblogic-application.xml (deadlock)

-deploy one module in wlst (doesn't deploy everything): 
	deploy('DevServer',FD_HOME+'/projects/DevServer',targets='../ERPServices/bin@'+serverName+',../CRM/docroot@'+vHostName, timeout=0, block='true', stageMode='nostage')

-deploy modules one-by-one (exception):
	deploy('DevServer',FD_HOME+'/projects/DevServer',targets='../ERPServices/bin@'+serverName, timeout=0, block='true', stageMode='nostage')
	deploy('DevServer',FD_HOME+'/projects/DevServer',targets='../Delivery/bin@'+serverName, timeout=0, block='true', stageMode='nostage')
	deploy('DevServer',FD_HOME+'/projects/DevServer',targets='../DataLoader/bin@'+serverName, timeout=0, block='true', stageMode='nostage')
	deploy('DevServer',FD_HOME+'/projects/DevServer',targets='../FDStore/bin@'+serverName, timeout=0, block='true', stageMode='nostage')
	deploy('DevServer',FD_HOME+'/projects/DevServer',targets='../StandingOrdersService/bin@'+serverName, timeout=0, block='true', stageMode='nostage')
	deploy('DevServer',FD_HOME+'/projects/DevServer',targets='../FDWebSite/docroot@'+serverName, timeout=0, block='true', stageMode='nostage')
	deploy('DevServer',FD_HOME+'/projects/DevServer',targets='../FDIntegrationServices/docroot@'+serverName, timeout=0, block='true', stageMode='nostage')
	deploy('DevServer',FD_HOME+'/projects/DevServer',targets='../ERPSAdmin/docroot@'+serverName, timeout=0, block='true', stageMode='nostage')
	deploy('DevServer',FD_HOME+'/projects/DevServer',targets='../CRM/docroot@'+vHostName, timeout=0, block='true', stageMode='nostage')
	deploy('DevServer',FD_HOME+'/projects/DevServer',targets='../RefAdmin/docroot@'+serverName, timeout=0, block='true', stageMode='nostage')
	deploy('DevServer',FD_HOME+'/projects/DevServer',targets='../cms-gwt/target/docroot@'+serverName, timeout=0, block='true', stageMode='nostage')

-comment out Erpservices only in application.xml (deploy is successful with cms-gwt, but doesn't run)

-use other jvm instead of JRockit (same result)
	insert following into bin/startWebLogic.sh after line 'echo "starting weblogic with Java version:"'
	JAVA_VM=''
	JAVA_HOME='/usr'

-export WLS_REDIRECT_LOG for logging (redirects output to file, DomainCreator app hangs) - use wl.log instead (see 'Modifications')

-remove $FD_BASE jars from startWebLogic.sh (deploy fails)

-server is started manually and wlst commands are executed manually (deadlock still appears)
	-one cannot connect with jconsole
	-after wlst process is killed, weblogic is still ok, one can connect to it jconsole
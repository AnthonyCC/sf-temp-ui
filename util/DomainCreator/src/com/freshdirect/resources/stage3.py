#
# STAGE 3 Script
# Deploy apps
#
# @param domainName Domain name
#
print 'wl_user: '+wl_user
print 'wl_pwd: '+wl_pwd
print 'wl_url: '+wl_url
connect(wl_user, wl_pwd, wl_url)
##
## Users and Roles
##
#edit()
#startEdit()
# Deploy goes here ...
cd('Servers')
print "FD_HOME="+FD_HOME
print "target="+serverName
#+',crm@'+vHostName

#progress=

print " === Starting deployment === "
print " If the process is stuck for a while then it probably deadlocked. Hopefully the deployment was successful before that, just restart weblogic and start the deployment from the wl console."

#deploy('DevServer',FD_HOME+'/projects/DevServer',targets=serverName+',crm@'+vHostName, timeout=0, block='true', stageMode='nostage')
deploy('DevServer',FD_HOME+'/projects/DevServer',targets=serverName, timeout=0, block='true', stageMode='nostage')

print " === Finished deployment === "

#save()
#activate(-1)

#disconnect()

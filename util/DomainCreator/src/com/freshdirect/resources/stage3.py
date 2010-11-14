#
# STAGE 3 Script
# Deploy apps
#
# @param domainName Domain name
#
connect(wl_user, wl_pwd, wl_url)
##
## Users and Roles
##
edit()
startEdit()
# Deploy goes here ...
cd('Servers')
print "FD_HOME="+FD_HOME
print "target="+serverName+',crm@'+vHostName
deploy('DevServer',FD_HOME+'/projects/DevServer',targets=serverName+',crm@'+vHostName)

save()
activate()

disconnect()

#
# STAGE 2 Script
# Add Users and Roles
#
# @param domainName Domain name
#
connect(wl_user, wl_pwd, wl_url)
##
## Users and Roles
##
domainConfig()

# no .. cd('/SecurityConfiguration/' + domainName + '/Realms/myrealm')

from weblogic.management.security.authentication import UserEditorMBean

atnr=cmo.getSecurityConfiguration().getDefaultRealm().lookupAuthenticationProvider("DefaultAuthenticator")
atnr.createUser('root', '12345678', 'Default Admin User')

cd('/SecurityConfiguration/' + domainName + '/Realms/myrealm/RoleMappers/XACMLRoleMapper')
## from weblogic.management.security.authentication import RoleEditorMBean

cmo.createRole(None, 'cms_admin', 'Usr(root)')
cmo.createRole(None, 'cms_editor', 'Usr(root)')
cmo.createRole(None, 'cms_viewer', None)

disconnect()

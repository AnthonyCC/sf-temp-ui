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
domainConfig()

# Deploy goes here ...

disconnect()

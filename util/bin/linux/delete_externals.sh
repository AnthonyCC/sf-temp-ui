#!/bin/sh

# delete directories that were linked with svn:externals
#
# run this script when migrating from svn:externals to using symbolic links
# on your working copy
#
# run this script from the root FreshDirect directory

rm -rf projects/DlvAdmin/docroot/WEB-INF/shared
rm -rf projects/RefAdmin/docroot/WEB-INF/shared
rm -rf projects/FDWebSite/docroot/WEB-INF/shared
rm -rf projects/DlvConfirm/docroot/WEB-INF/shared
rm -rf projects/CRM/docroot/WEB-INF/shared
rm -rf projects/ERPSAdmin/docroot/WEB-INF/shared

rm -rf projects/CRM/docroot/help
rm -rf projects/CRM/docroot/survey
rm -rf projects/CRM/docroot/includes/delivery
rm -rf projects/CRM/docroot/includes/product
rm -rf projects/CRM/docroot/includes/registration
rm -rf projects/CRM/docroot/includes/promotions
rm -rf projects/CRM/docroot/supervisor/test/debug
rm -rf projects/CRM/docroot/api

rm -rf projects/CRM/docroot/shared
rm -rf projects/FDWebSite/docroot/shared

rm -rf projects/FDWebSite/docroot/assets
rm -rf projects/FDWebSite/docroot/ccassets
rm -rf projects/FDWebSite/docroot/media_stat
rm -rf projects/CRM/docroot/assets
rm -rf projects/CRM/docroot/ccassets
rm -rf projects/CRM/docroot/media_stat

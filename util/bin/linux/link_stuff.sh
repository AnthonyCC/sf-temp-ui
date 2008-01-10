#!/bin/sh

# create the symbolic links for shared assets
#
# run this script from the root FreshDirect directory

ln -sf  ../../../../projects/WebAppCommon/docroot/WEB-INF/shared        projects/DlvAdmin/docroot/WEB-INF/shared
ln -sf  ../../../../projects/WebAppCommon/docroot/WEB-INF/shared        projects/RefAdmin/docroot/WEB-INF/shared
ln -sf  ../../../../projects/WebAppCommon/docroot/WEB-INF/shared        projects/FDWebSite/docroot/WEB-INF/shared
ln -sf  ../../../../projects/WebAppCommon/docroot/WEB-INF/shared        projects/DlvConfirm/docroot/WEB-INF/shared
ln -sf  ../../../../projects/WebAppCommon/docroot/WEB-INF/shared        projects/CRM/docroot/WEB-INF/shared
ln -sf  ../../../../projects/WebAppCommon/docroot/WEB-INF/shared        projects/ERPSAdmin/docroot/WEB-INF/shared

ln -sf  ../../../projects/FDWebSite/docroot/help                        projects/CRM/docroot/help
ln -sf  ../../../projects/FDWebSite/docroot/survey                      projects/CRM/docroot/survey
ln -sf  ../../../../projects/FDWebSite/docroot/includes/delivery        projects/CRM/docroot/includes/delivery
ln -sf  ../../../../projects/FDWebSite/docroot/includes/product         projects/CRM/docroot/includes/product
ln -sf  ../../../../projects/FDWebSite/docroot/includes/registration    projects/CRM/docroot/includes/registration
ln -sf  ../../../../projects/FDWebSite/docroot/includes/promotions      projects/CRM/docroot/includes/promotions
ln -sf  ../../../../../projects/FDWebSite/docroot/test/debug            projects/CRM/docroot/supervisor/test/debug
ln -sf  ../../../projects/FDWebSite/docroot/api                         projects/CRM/docroot/api

ln -sf  ../../../projects/WebAppCommon/docroot/shared                   projects/CRM/docroot/shared
ln -sf  ../../../projects/WebAppCommon/docroot/shared                   projects/FDWebSite/docroot/shared

ln -sf  ../../../projects/Media/docroot/assets                          projects/FDWebSite/docroot/assets
ln -sf  ../../../projects/Media/docroot/ccassets                        projects/FDWebSite/docroot/ccassets
ln -sf  ../../../projects/Media/docroot/media_stat                      projects/FDWebSite/docroot/media_stat
ln -sf  ../../../projects/Media/docroot/assets                          projects/CRM/docroot/assets
ln -sf  ../../../projects/Media/docroot/ccassets                        projects/CRM/docroot/ccassets
ln -sf  ../../../projects/Media/docroot/media_stat                      projects/CRM/docroot/media_stat



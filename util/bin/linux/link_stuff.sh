#!/bin/sh

# create the symbolic links for shared assets
#
# run this script from the root FreshDirect directory

CURRENTDIR=$(pwd -P)

if [ ! -d "$CURRENTDIR/projects" ]; then
    echo "please run this only from the root directory that contains the projects folder"
    exit 1
fi

ln -s  "$CURRENTDIR"/projects/WebAppCommon/docroot/WEB-INF/shared        "$CURRENTDIR"/projects/DlvAdmin/docroot/WEB-INF/shared
ln -s  "$CURRENTDIR"/projects/WebAppCommon/docroot/WEB-INF/shared        "$CURRENTDIR"/projects/FDWebSite/docroot/WEB-INF/shared
ln -s  "$CURRENTDIR"/projects/WebAppCommon/docroot/WEB-INF/shared        "$CURRENTDIR"/projects/DlvConfirm/docroot/WEB-INF/shared
ln -s  "$CURRENTDIR"/projects/WebAppCommon/docroot/WEB-INF/shared        "$CURRENTDIR"/projects/CRM/docroot/WEB-INF/shared
ln -s  "$CURRENTDIR"/projects/WebAppCommon/docroot/WEB-INF/shared        "$CURRENTDIR"/projects/ERPSAdmin/docroot/WEB-INF/shared

ln -s  "$CURRENTDIR"/projects/FDWebSite/docroot/help                     "$CURRENTDIR"/projects/CRM/docroot/help
ln -s  "$CURRENTDIR"/projects/FDWebSite/docroot/survey                   "$CURRENTDIR"/projects/CRM/docroot/survey
ln -s  "$CURRENTDIR"/projects/FDWebSite/docroot/includes/delivery        "$CURRENTDIR"/projects/CRM/docroot/includes/delivery
ln -s  "$CURRENTDIR"/projects/FDWebSite/docroot/includes/product         "$CURRENTDIR"/projects/CRM/docroot/includes/product
ln -s  "$CURRENTDIR"/projects/FDWebSite/docroot/includes/registration    "$CURRENTDIR"/projects/CRM/docroot/includes/registration
ln -s  "$CURRENTDIR"/projects/FDWebSite/docroot/includes/promotions      "$CURRENTDIR"/projects/CRM/docroot/includes/promotions
ln -s  "$CURRENTDIR"/projects/FDWebSite/docroot/test/debug               "$CURRENTDIR"/projects/CRM/docroot/supervisor/test/debug
ln -s  "$CURRENTDIR"/projects/FDWebSite/docroot/api                      "$CURRENTDIR"/projects/CRM/docroot/api

ln -s  "$CURRENTDIR"/projects/WebAppCommon/docroot/shared                   "$CURRENTDIR"/projects/CRM/docroot/shared
ln -s  "$CURRENTDIR"/projects/WebAppCommon/docroot/shared                   "$CURRENTDIR"/projects/FDWebSite/docroot/shared
ln -s  "$CURRENTDIR"/projects/Media/docroot/assets                          "$CURRENTDIR"/projects/FDWebSite/docroot/assets
ln -s  "$CURRENTDIR"/projects/Media/docroot/ccassets                        "$CURRENTDIR"/projects/FDWebSite/docroot/ccassets
ln -s  "$CURRENTDIR"/projects/Media/docroot/media_stat                      "$CURRENTDIR"/projects/FDWebSite/docroot/media_stat
ln -s  "$CURRENTDIR"/projects/Media/docroot/assets                          "$CURRENTDIR"/projects/CRM/docroot/assets
ln -s  "$CURRENTDIR"/projects/Media/docroot/ccassets                        "$CURRENTDIR"/projects/CRM/docroot/ccassets
ln -s  "$CURRENTDIR"/projects/Media/docroot/media_stat                      "$CURRENTDIR"/projects/CRM/docroot/media_stat
ln -s  "$CURRENTDIR"/projects/Media/docroot/media                           "$CURRENTDIR"/projects/FDWebSite/docroot/media           


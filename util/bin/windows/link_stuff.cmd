@echo off
rem Windows script to set up shared directories in the development environment
rem using junctions.
rem
rem Invoke this script from the FreshDirect root directory.
rem
rem for more on junctions,
rem see http://www.microsoft.com/technet/sysinternals/FileAndDisk/Junction.mspx

set DEL=rd /s /q
set JUNCTION=util\bin\windows\junction.exe


rem first delete the junction targets, if anything was there..
rem note: the del command can not (and will not) delete junctions
rem       thus this is safe to run even if junctions are already there

%DEL% projects\DlvAdmin\docroot\WEB-INF\shared
%DEL% projects\RefAdmin\docroot\WEB-INF\shared
%DEL% projects\FDWebSite\docroot\WEB-INF\shared
%DEL% projects\DlvConfirm\docroot\WEB-INF\shared
%DEL% projects\CRM\docroot\WEB-INF\shared
%DEL% projects\ERPSAdmin\docroot\WEB-INF\shared

%DEL% projects\CRM\docroot\help
%DEL% projects\CRM\docroot\survey
%DEL% projects\CRM\docroot\includes\delivery
%DEL% projects\CRM\docroot\includes\product
%DEL% projects\CRM\docroot\includes\registration
%DEL% projects\CRM\docroot\includes\promotions
%DEL% projects\CRM\docroot\supervisor\test\debug
%DEL% projects\CRM\docroot\api

%DEL% projects\CRM\docroot\shared
%DEL% projects\FDWebSite\docroot\shared

%DEL% projects\FDWebSite\docroot\assets
%DEL% projects\FDWebSite\docroot\ccassets
%DEL% projects\FDWebSite\docroot\media_stat
%DEL% projects\CRM\docroot\assets
%DEL% projects\CRM\docroot\ccassets
%DEL% projects\CRM\docroot\media_stat


rem create the junctions themselves

%JUNCTION% projects\DlvAdmin\docroot\WEB-INF\shared        projects\WebAppCommon\docroot\WEB-INF\shared
%JUNCTION% projects\RefAdmin\docroot\WEB-INF\shared        projects\WebAppCommon\docroot\WEB-INF\shared
%JUNCTION% projects\FDWebSite\docroot\WEB-INF\shared       projects\WebAppCommon\docroot\WEB-INF\shared
%JUNCTION% projects\DlvConfirm\docroot\WEB-INF\shared      projects\WebAppCommon\docroot\WEB-INF\shared
%JUNCTION% projects\CRM\docroot\WEB-INF\shared             projects\WebAppCommon\docroot\WEB-INF\shared
%JUNCTION% projects\ERPSAdmin\docroot\WEB-INF\shared       projects\WebAppCommon\docroot\WEB-INF\shared

%JUNCTION% projects\CRM\docroot\help                       projects\FDWebSite\docroot\help
%JUNCTION% projects\CRM\docroot\survey                     projects\FDWebSite\docroot\survey
%JUNCTION% projects\CRM\docroot\includes\delivery          projects\FDWebSite\docroot\includes\delivery
%JUNCTION% projects\CRM\docroot\includes\product           projects\FDWebSite\docroot\includes\product
%JUNCTION% projects\CRM\docroot\includes\registration      projects\FDWebSite\docroot\includes\registration
%JUNCTION% projects\CRM\docroot\includes\promotions        projects\FDWebSite\docroot\includes\promotions
%JUNCTION% projects\CRM\docroot\supervisor\test\debug      projects\FDWebSite\docroot\test\debug
%JUNCTION% projects\CRM\docroot\api                        projects\FDWebSite\docroot\api

%JUNCTION% projects\CRM\docroot\shared                     projects\WebAppCommon\docroot\shared
%JUNCTION% projects\FDWebSite\docroot\shared               projects\WebAppCommon\docroot\shared

%JUNCTION% projects\FDWebSite\docroot\assets               projects\Media\docroot\assets
%JUNCTION% projects\FDWebSite\docroot\ccassets             projects\Media\docroot\ccassets
%JUNCTION% projects\FDWebSite\docroot\media_stat           projects\Media\docroot\media_stat
%JUNCTION% projects\CRM\docroot\assets                     projects\Media\docroot\assets
%JUNCTION% projects\CRM\docroot\ccassets                   projects\Media\docroot\ccassets
%JUNCTION% projects\CRM\docroot\media_stat                 projects\Media\docroot\media_stat


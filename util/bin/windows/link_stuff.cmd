@echo off
rem Windows script to set up shared directories in the development environment
rem using junctions.
rem
rem Invoke this script from the FreshDirect root directory.
rem
rem for more on junctions,
rem see http://www.microsoft.com/technet/sysinternals/FileAndDisk/Junction.mspx

set JUNCTION=util\bin\windows\junction.exe


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


@echo off
rem delete directories that were linked with svn:externals
rem
rem run this script when migrating from svn:externals to using symbolic links
rem on your working copy
rem
rem run this script from the root FreshDirect directory

set DEL=rd /s /q

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


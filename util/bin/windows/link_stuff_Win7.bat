@echo off
rem Windows 7 script to set up shared directories in the development environment
rem using built-in Win7 command MKLINK

rem Assume the drive where the script is run holds the source code
set JUNCROOTDRIVE=%CD:~0,2%

rem The root folder holding our source code
set JUNCROOTSRC=%JUNCROOTDRIVE%\Storefront_2_0

rem The root folder holding our '\media' folder
set JUNCROOTMEDIA=%JUNCROOTDRIVE%\FreshDirect\content

rmdir %JUNCROOTSRC%\projects\FDWebSite\docroot\WEB-INF\shared	
rmdir %JUNCROOTSRC%\projects\FDWebSite\docroot\shared			
rmdir %JUNCROOTSRC%\projects\FDWebSite\docroot\assets			
rmdir %JUNCROOTSRC%\projects\FDWebSite\docroot\ccassets			
rmdir %JUNCROOTSRC%\projects\FDWebSite\docroot\media_stat		
rmdir %JUNCROOTSRC%\projects\FDWebSite\docroot\WEB-INF\tags	


rem Media common DAM store
MKLINK /D /J  %JUNCROOTSRC%\projects\Media\docroot\media				%JUNCROOTMEDIA%\media

MKLINK /D /J %JUNCROOTSRC%\projects\FDWebSite\docroot\WEB-INF\shared	%JUNCROOTSRC%\projects\WebAppCommon\docroot\WEB-INF\shared
MKLINK /D /J  %JUNCROOTSRC%\projects\FDWebSite\docroot\shared			%JUNCROOTSRC%\projects\WebAppCommon\docroot\shared
MKLINK /D /J  %JUNCROOTSRC%\projects\FDWebSite\docroot\assets			%JUNCROOTSRC%\projects\Media\docroot\assets
MKLINK /D /J  %JUNCROOTSRC%\projects\FDWebSite\docroot\ccassets			%JUNCROOTSRC%\projects\Media\docroot\ccassets
MKLINK /D /J  %JUNCROOTSRC%\projects\FDWebSite\docroot\media_stat		%JUNCROOTSRC%\projects\Media\docroot\media_stat
MKLINK /D /J %JUNCROOTSRC%\projects\FDWebSite\docroot\WEB-INF\tags	%JUNCROOTSRC%\projects\WebAppCommon\docroot\WEB-INF\shared\tags

rem Dynamic media
MKLINK /D /J  %JUNCROOTSRC%\projects\FDWebsite\docroot\media			%JUNCROOTMEDIA%\media


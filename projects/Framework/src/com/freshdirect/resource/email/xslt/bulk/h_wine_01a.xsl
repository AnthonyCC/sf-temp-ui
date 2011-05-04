<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:doc="http://xsltsl.org/xsl/documentation/1.0"
	xmlns:str="http://xsltsl.org/string"
	extension-element-prefixes="doc str">
	<xsl:output method="html"/>
	<xsl:include href='h_optout_footer.xsl'/>
	<xsl:include href='h_footer_v2.xsl'/>
	<xsl:include href='string.xsl' />
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">

<html>
	<head>
        <BASE href="http://www.freshdirect.com" />
		<title>Wine now available!</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
		<style type="text/css">
		.fdEmail    { font-size: 12px; color: #000000; font-family: Verdana, Arial, Helvetica, sans-serif; }
		a.fdEmail:link {color: #336600; text-decoration: underline; }
		a.fdEmail:visited {color: #336600; text-decoration: underline; }
		a.fdEmail:active {color: #FF9933; text-decoration: underline; }
		a.fdEmail:hover {color: #336600; text-decoration: underline; }
		.fdFooter_s    { font-size: 11px; color: #333333; font-family: Verdana, Arial, sans-serif; }
		a.fdFooter_s:link {color: #336600; text-decoration: underline; }
		a.fdFooter_s:visited {color: #336600; text-decoration: underline; }
		a.fdFooter_s:active {color: #FF9933; text-decoration: underline; }
		a.fdFooter_s:hover {color: #336600; text-decoration: underline; }
		</style>
	</head>
<body bgcolor="#FFFFFF" text="#333333">
	<xsl:call-template name="mail_body" />
</body>
</html>

</xsl:template>

<xsl:template name="mail_body">

<xsl:comment>

www.FRESHDIRECT.com                                      It's all about the food
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Best Cellars
WINE NOW AVAILABLE!
Just in time for the Holidays

Now you can order a selection of great wines for everyday through Best Cellars at FreshDirect and have your wine delivered to your door along with the rest of your food order.

Start shopping for wine: http://www.freshdirect.com/department.jsp?deptId=win&amp;trk=wine_01a

. . . . . . . . . . 

Don't forget to also check out
OUR HOLIDAY MENU

Celebrate the Holiday season in style and ease with our Chef's special menu, featuring delicious, traditional appetizers, entr&#xE9;es, and more.

Shop from our Holiday Menu: http://www.freshdirect.com/category.jsp?catId=hmr_xmas&amp;trk=wine_01a


======

QUICK LINKS:

Go to FreshDirect
http://www.freshdirect.com

Contact Us
http://www.freshdirect.com/help/contact_fd.jsp

Frequently Asked Questions
http://www.freshdirect.com/help/faq_home.jsp

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
(c) 2002, 2003 FRESHDIRECT. All Rights Reserved.

</xsl:comment>

<table width="619" cellpadding="0" cellspacing="0" align="center">
	<tr>
		<td colspan="3"><a href="http://www.freshdirect.com?trk=wine_01a"><img src="http://www.freshdirect.com/media_stat/images/logos/fd_logo_md.gif" width="216" height="42" alt="FreshDirect" border="0"/></a></td>
		<td colspan="3" align="right" valign="bottom"><img src="http://www.freshdirect.com/media_stat/images/logos/fd_tag.gif" width="170" height="12" alt="It's All About The Food" border="0"/></td>
	</tr>
	<tr><td colspan="6"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8" border="0"/></td></tr>
	<tr><td colspan="6" bgcolor="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/></td></tr>
	<tr align="center"><td colspan="6"><a href="http://www.freshdirect.com/department.jsp?deptId=win&amp;trk=wine_01a"><img src="http://www.freshdirect.com/media/images/promotions/wine_01a.gif" width="619" height="128" border="0" vspace="14" alt="Best Cellars WINE NOW AVAILABLE! Just in time for the Holidays"/></a></td></tr>
	<tr align="center">
		<td colspan="6" class="fdEmail">
		Now you can order a selection of great wines for everyday through Best Cellars at FreshDirect and have your wine delivered to your door along with the rest of your food order.
		<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="4"/><br/>
		<a href="http://www.freshdirect.com/department.jsp?deptId=win&amp;trk=wine_01a"><img src="http://www.freshdirect.com/media_stat/images/template/email/wine/wine_start_shopping.gif" width="352" height="27" border="0" vspace="10" alt="CLICK HERE TO START SHOPPING FOR WINE"/></a><br/>
		<img src="http://www.freshdirect.com/media_stat/images/template/email/wine/wine_bottles.jpg" width="555" height="208" border="0" usemap="#wine_nav"/><br/><br/>
		</td>
	</tr>
	<tr bgcolor="#CCCCCC">
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="68" height="1"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="70" height="1"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="171" height="1"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="172" height="1"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="70" height="1"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="68" height="1"/></td>
	</tr>
	<tr><td colspan="6"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="18"/></td></tr>
	<tr align="center">
		<td align="right"><a href="http://www.freshdirect.com/category.jsp?catId=hmr_xmas&amp;trk=wine_01a"><img src="http://www.freshdirect.com/media_stat/images/template/email/holiday/turkey.jpg" width="62" height="49" border="0"/></a></td>
		<td><a href="http://www.freshdirect.com/category.jsp?catId=hmr_xmas&amp;trk=wine_01a"><img src="http://www.freshdirect.com/media_stat/images/template/email/holiday/ham.jpg" width="62" height="49" border="0"/></a></td>
		<td colspan="2"><a href="http://www.freshdirect.com/category.jsp?catId=hmr_xmas&amp;trk=wine_01a"><img src="http://www.freshdirect.com/media_stat/images/template/email/holiday/our_holiday_menu.gif" width="335" height="60" border="0" alt="Don't forget to also check out OUR HOLIDAY MENU"/></a></td>
		<td><a href="http://www.freshdirect.com/category.jsp?catId=hmr_xmas&amp;trk=wine_01a"><img src="http://www.freshdirect.com/media_stat/images/template/email/holiday/shrimp.jpg" width="62" height="49" border="0"/></a></td>
		<td align="left"><a href="http://www.freshdirect.com/category.jsp?catId=hmr_xmas&amp;trk=wine_01a"><img src="http://www.freshdirect.com/media_stat/images/template/email/holiday/crudites.jpg" width="62" height="49" border="0"/></a></td>
	</tr>
	<tr align="center">
		<td colspan="6" class="fdEmail"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8"/><br/>
		Celebrate the Holiday season in style and ease with our Chef's special menu,
		featuring delicious, traditional appetizers, entr&#xE9;es, and more.<br/><br/>
		<a href="http://www.freshdirect.com/category.jsp?catId=hmr_xmas&amp;trk=wine_01a"><font color="#336600"><u><b>Click here to shop from our Holiday Menu.</b></u></font></a><br/><br/>
		<img src="http://www.freshdirect.com/media_stat/images/template/email/holiday/line.gif" width="587" height="11" border="0"/>
		</td>
	</tr>
	<tr><td colspan="6" class="fdEmail"><br/><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/><br/><br/></td></tr>
</table>
<MAP NAME="wine_nav">
<AREA SHAPE="RECT" COORDS="0,0,67,208" HREF="http://www.freshdirect.com/category.jsp?catId=win_fizzy&amp;trk=wine_01a" ALT="fizzy"/>
<AREA SHAPE="RECT" COORDS="67,0,136,208" HREF="http://www.freshdirect.com/category.jsp?catId=win_fresh&amp;trk=wine_01a" ALT="fresh"/>
<AREA SHAPE="RECT" COORDS="137,0,204,208" HREF="http://www.freshdirect.com/category.jsp?catId=win_soft&amp;trk=wine_01a" ALT="soft"/>
<AREA SHAPE="RECT" COORDS="204,0,270,208" HREF="http://www.freshdirect.com/category.jsp?catId=win_luscious&amp;trk=wine_01a" ALT="luscious"/>
<AREA SHAPE="RECT" COORDS="270,0,334,208" HREF="http://www.freshdirect.com/category.jsp?catId=win_juicy&amp;trk=wine_01a" ALT="juicy"/>
<AREA SHAPE="RECT" COORDS="334,0,407,208" HREF="http://www.freshdirect.com/category.jsp?catId=win_smooth&amp;trk=wine_01a" ALT="smooth"/>
<AREA SHAPE="RECT" COORDS="407,0,471,208" HREF="http://www.freshdirect.com/category.jsp?catId=win_big&amp;trk=wine_01a" ALT="big"/>
<AREA SHAPE="RECT" COORDS="471,0,554,207" HREF="http://www.freshdirect.com/category.jsp?catId=win_sweet&amp;trk=wine_01a" ALT="sweet"/>
</MAP>

</xsl:template>

</xsl:stylesheet>
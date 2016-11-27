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
		<title>Organic department now open!</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
		<style type="text/css">
		.mainLink    { font-size: 14px; color: #333333; font-family: Verdana, Arial, Helvetica, sans-serif; }
		a.mainLink:link {color: #336600; text-decoration: underline; }
		a.mainLink:visited {color: #336600; text-decoration: underline; }
		a.mainLink:active {color: #FF9933; text-decoration: underline; }
		a.mainLink:hover {color: #336600; text-decoration: underline; }
		.fdFooter_s    { font-size: 11px; color: #333333; font-family: Verdana, Arial, sans-serif; }
		a.fdFooter_s:link {color: #336600; text-decoration: underline; }
		a.fdFooter_s:visited {color: #336600; text-decoration: underline; }
		a.fdFooter_s:active {color: #FF9933; text-decoration: underline; }
		a.fdFooter_s:hover {color: #336600; text-decoration: underline; }
		</style>
	</head>
<body bgcolor="#FFFFFF" text="#000000" marginheight="0" topmargin="0" marginwidth="0" leftmargin="0">
	<xsl:call-template name="mail_body" />
</body>
</html>
</xsl:template>

<xsl:template name="mail_body">
<table width="640" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr>
		<td colspan="11"><a href="http://www.freshdirect.com?trk=organic04"><img src="http://www.freshdirect.com/media_stat/images/template/email/layout/fd_logo.gif" width="196" height="39" border="0" vspace="6"/></a></td>
	</tr>
	<tr>
		<td colspan="2" rowspan="2"><img src="http://www.freshdirect.com/media_stat/images/template/email/layout/cor_top_left_336600.gif" width="8" height="7" border="0"/></td>
		<td colspan="7" bgcolor="#336600"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/></td>
		<td colspan="2" rowspan="2"><img src="http://www.freshdirect.com/media_stat/images/template/email/layout/cor_top_right_336600.gif" width="8" height="7" border="0"/></td>
	</tr>
	<tr>
		<td colspan="7"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="6"/></td>
	</tr>
	<tr>
		<td rowspan="4" bgcolor="#336600"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8"/></td>
		<td rowspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="7" height="8"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="12" height="8"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="111" height="8"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="280" height="8"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="8"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8"/></td>
		<td rowspan="4"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="10" height="8"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="190" height="8"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="7" height="8"/></td>
		<td rowspan="4" bgcolor="#336600"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8"/></td>
	</tr>
	<tr>
		<td colspan="4"><a href="http://www.freshdirect.com/department.jsp?deptId=orgnat&amp;trk=organic04"><img src="http://www.freshdirect.com/media/images/promotions/email/organic04.gif" width="405" height="84" border="0" hspace="4" alt="ORGANIC &amp; ALL-NATURAL! - CHECK OUT OUR NEW DEPARTMENT!"/></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="6"/></td>
		<td rowspan="3" bgcolor="#336600"></td>
		<td rowspan="3" align="center" valign="top">
		<img src="http://www.freshdirect.com/media_stat/images/template/email/new/org04_just_for_fathers_day.gif" width="160" height="40" border="0" vspace="6" alt="JUST FOR FATHER'S DAY"/><br/>
		SUNDAY, JUNE 20,<br/>IS JUST DAYS AWAY!<br/>
		<a href="http://www.freshdirect.com/product.jsp?productId=cake_fathermousse&amp;catId=bak_cake_fd&amp;trk=organic04">
		<img src="http://www.freshdirect.com/media_stat/images/template/email/new/org04_cake_fathersday.jpg" width="135" height="119" border="0" vspace="8" alt="FATHER'S DAY CHOCOLATE MOUSSE CAKE"/><br/>
		<font color="#336600"><b>FATHER'S DAY<br/>CHOCOLATE MOUSSE CAKE<br/>$10.99/ea</b></font></a><br/><br/><br/>
		<img src="http://www.freshdirect.com/media_stat/images/template/email/new/org04_also_in_season.gif" width="138" height="44" border="0" vspace="4" alt="ALSO IN SEASON!"/><br/>
		<a href="http://www.freshdirect.com/product.jsp?productId=br_nystrawbrry&amp;catId=br&amp;trk=organic04"><img src="http://www.freshdirect.com/media_stat/images/template/email/new/org04_strawberries.jpg" width="100" height="73" border="0" vspace="4" alt="LOCAL NEW YORK STATE STRAWBERRIES"/><br/>
		<font color="#336600"><b>LOCAL NEW YORK STATE<br/>STRAWBERRIES<br/>$3.49/ea</b></font></a><br/><br/>
		<a href="http://www.freshdirect.com/product.jsp?productId=fstk_slmn_cpprwk&amp;catId=fstk&amp;trk=organic04"><img src="http://www.freshdirect.com/media_stat/images/template/email/new/org04_salmon.jpg" width="95" height="82" border="0" vspace="4" alt="COPPER RIVER WILD KING SALMON STEAKS"/><br/>
		<font color="#336600"><b>COPPER RIVER WILD<br/>KING SALMON STEAKS<br/>$16.99/lb</b></font></a>
		</td>
	</tr>
	<tr>
		<td colspan="3"><a href="http://www.freshdirect.com/department.jsp?deptId=orgnat&amp;trk=organic04"><img src="http://www.freshdirect.com/media_stat/images/template/email/new/org04_apple.jpg" width="113" height="200" border="0" vspace="8"/></a></td>
		<td>We're proud to announce the launch of our Organic and All-Natural Department.
		<br/><br/>
		We've added a wide selection of Organic fruit and vegetables as well as a hormone- and antibiotic-free meat and deli items. We've also gathered together hundreds of carefully selected All-Natural and Certified Organic grocery items &#151; from shade-grown coffee to phosphate-free laundry detergent.
		<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8"/><br/>
		<a href="http://www.freshdirect.com/department.jsp?deptId=orgnat&amp;trk=organic04" class="mainLink"><font color="#336600"><b>Click here to visit our new<br/>
		Organic &amp; All-Natural Department.</b></font></a></td>
		<td rowspan="2"></td>
	</tr>
	<tr>
		<td colspan="2"></td>
		<td colspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/999999.gif" width="391" height="1" vspace="14"/><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="6"/><div align="center"><a href="http://www.freshdirect.com/category.jsp?catId=menu&amp;trk=organic04"><img src="http://www.freshdirect.com/media_stat/images/template/email/new/org04_summer_grilling_menu.gif" width="368" height="52" border="0" alt="CHECK OUT OUR SUMMER GRILLING MENU!"/><br/><img src="http://www.freshdirect.com/media_stat/images/template/email/new/org04_summer_grill.jpg" width="368" height="104" border="0" vspace="10" alt="SUMMER GRILL ITEMS"/></a></div>It just wouldn't be summer without the hiss and sizzle of the grill. To make your party planning less fuss and more fun, we've gathered a special collection of our summer grill favorites from burgers and brats to steaks and salads. Eat, drink, and grill on!<div align="center"><br/><a href="http://www.freshdirect.com/category.jsp?catId=menu&amp;trk=organic04"><b>Click here to learn more</b></a></div><br/><br/></td>
	</tr>
	<tr>
		<td colspan="2" rowspan="2"><img src="http://www.freshdirect.com/media_stat/images/template/email/layout/cor_bot_left_336600.gif" width="8" height="7" border="0"/></td>
		<td colspan="7"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="6"/></td>
		<td colspan="2" rowspan="2"><img src="http://www.freshdirect.com/media_stat/images/template/email/layout/cor_bot_right_336600.gif" width="8" height="7" border="0"/></td>
	</tr>
	<tr>
		<td colspan="7" bgcolor="#336600"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/></td>
	</tr>
	<tr><td colspan="11"><br/><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/><br/></td></tr>
</table>

</xsl:template>

</xsl:stylesheet>
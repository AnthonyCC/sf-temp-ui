<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:doc="http://xsltsl.org/xsl/documentation/1.0"
	xmlns:str="http://xsltsl.org/string"
	extension-element-prefixes="doc str">
	<xsl:output method="html"/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_optout_footer.xsl'/>
	<xsl:include href='h_footer_v2.xsl'/>
	<xsl:include href='string.xsl' />
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">

<html>
	<head>
        <BASE href="http://www.freshdirect.com" />
		<title>Our first wine and cheese seminar!</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
	</head>
<body bgcolor="#FFFFFF" text="#333333">
	<xsl:call-template name="mail_body" />
</body>
</html>

</xsl:template>

<xsl:template name="mail_body">

<xsl:comment>

</xsl:comment>
<table width="600" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr>
		<td colspan="3"><a href="http://www.freshdirect.com"><img src="http://www.freshdirect.com/media_stat/images/logos/fd_logo_md.gif" width="216" height="42" alt="FreshDirect" border="0"/></a></td>
		<td colspan="2" align="right" valign="bottom"><img src="http://www.freshdirect.com/media_stat/images/logos/fd_tag.gif" width="170" height="12" alt="It's All About The Food" border="0"/></td>
	</tr>
	
	<tr>
		<td colspan="5" align="center"><img src="http://www.freshdirect.com/media_stat/images/layout/cccccc.gif" width="600" height="1" border="0" vspace="10" /><br/><a href="http://www.artisanalcheese.com/artisanal/education/classes_tastings.cfm"><img src="http://www.freshdirect.com/media_stat/images/template/email/event/tasting_hdr.gif" width="487" height="49" border="0" vspace="8"/></a></td>
	</tr>
	
	<tr>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="180" height="1" border="0" /></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="46" height="1" border="0" /></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="148" height="1" border="0" /></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="46" height="1" border="0" /></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="180" height="1" border="0" /></td>
	</tr>
	
	<tr>
		<td colspan="3" style="font-size: 11px;">
		We're proud to bring together the Artisanal Cheese Center and Best Cellars to promote a night of exquisite cheese and wine tasting education hosted by some of the country's leading experts.<br/><br/>
This special tasting will be the first of several sponsored by FreshDirect, and will feature a standout offering from each of Best Cellars eight taste categories (FIZZY, FRESH, SOFT, LUSCIOUS, JUICY, SMOOTH, BIG, and SWEET) accompanied by an equal number of fine cheeses from Artisanal's unique selection. Moving beyond the limitations imposed by predetermined pairings, the class will explore a broader and simpler taste-based approach to the art of matching wine with cheese.<br/><br/>
The seminar instructors will include Josh Wesson, award winning sommelier and co-owner of Best Cellars; 3-star Chef Terrance Brennan, of the Artisanal Cheese Center, and restaurants Artisanal and Picholine; and Max McCalman, Ma&#238;tre Fromager and author of <i>The Cheese Plate</i>, the definitive book on wine and cheese pairing.
		</td>
		<td colspan="2"><a href="http://www.artisanalcheese.com/artisanal/education/classes_tastings.cfm"><img src="http://www.freshdirect.com/media_stat/images/template/email/event/wine_cheese.jpg" width="226" height="232" border="0"/></a></td>
	</tr>
	
	<tr>
		<td colspan="5" align="center"><img src="http://www.freshdirect.com/media_stat/images/template/email/event/stars_line.gif" width="599" height="10" border="0" vspace="14"/></td>
	</tr>
	
	<tr>
		<td align="right"><a href="http://www.artisanalcheese.com/artisanal/education/classes_tastings.cfm"><img src="http://www.freshdirect.com/media_stat/images/template/email/event/artisanal_logo.gif" width="105" height="81" border="0" hspace="20"/></a></td>
		<td colspan="3" align="center">
			<font color="#990000"><b>Event:</b></font><br/>An Evening with<br/>Josh Wesson and Max McCalman<br/><br/>
			<font color="#990000"><b>When:</b></font><br/>Tuesday, May 25, 2004<br/>Doors open at 7:00pm<br/><br/>
			<font color="#990000"><b>Where:</b></font><br/>Artisanal Cheese Center<br/>500 West 37th St. (10th Ave)<br/>2nd Floor, NYC<br/><br/>
			<font color="#990000"><b>Price:</b></font><br/>Tickets cost $75.00<br/><br/>
		</td>
		<td><a href="http://www.artisanalcheese.com/artisanal/education/classes_tastings.cfm"><img src="http://www.freshdirect.com/media_stat/images/template/email/event/bc_logo.gif" width="92" height="83" border="0" hspace="20"/></a></td>
	</tr>
	
	<tr>
		<td colspan="5" align="center"><img src="http://www.freshdirect.com/media_stat/images/template/email/event/stars_line.gif" width="599" height="10" border="0" vspace="10"/>
		<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /><br/>
		<b><span style="font-size: 15px; color: #990000;" >SEATING IS LIMITED!</span><br/>
		<img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="4" border="0" /><br/>
		TO ORDER TICKETS TO THIS SPECIAL EVENT CONTACT THE ARTISANAL CHEESE CENTER<br/>
		<img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="4" border="0" /><br/>
		Phone: 212-239-1200 or online at:</b><br/>
		<img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="4" border="0" /><br/>
		<a href="http://www.artisanalcheese.com/artisanal/education/classes_tastings.cfm">http://www.artisanalcheese.com/artisanal/education/classes_tastings.cfm</a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="28" border="0" />
</td>
	</tr>
	
	<tr>
		<td colspan="5" align="center">
			<xsl:call-template name="h_optout_footer"/>
			<xsl:call-template name="h_footer_v2"/>
		</td>
	</tr>
</table>

</xsl:template>

</xsl:stylesheet>
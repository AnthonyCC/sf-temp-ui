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
		<title>We're Kosher!</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
		<style type="text/css">
			.mainLink    { font-size: 16px; color: #336600; font-weight: bold; font-family: Arial, Helvetica, sans-serif; }
			a.mainLink:link {color: #336600; text-decoration: underline; }
			a.mainLink:visited {color: #336600; text-decoration: underline; }
			a.mainLink:active {color: #FF9933; text-decoration: underline; }
			a.mainLink:hover {color: #336600; text-decoration: underline; }
		</style>
	</head>
<body bgcolor="#FFFFFF" text="#333333">
	<xsl:call-template name="mail_body" />
</body>
</html>

</xsl:template>

<xsl:template name="mail_body">

<table width="613" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr>
		<td align="right"><a href="http://www.freshdirect.com?trk=kosher"><img src="/media_stat/images/template/email/kosher/kos_logo.gif" width="200" height="39" vspace="6" border="0" alt="FreshDirect"/></a></td>
	</tr>
	<tr>
		<td background="/media_stat/images/template/email/kosher/kos_star.gif"><img src="/media_stat/images/layout/clear.gif" width="1" height="24"/></td>
	</tr>
	<tr>
		<td><a href="http://www.freshdirect.com/department.jsp?deptId=kos&amp;trk=kosher"><img src="/media_stat/images/template/email/kosher/kos_header.gif" width="613" height="230" vspace="8" border="0" alt="Kosher Department Now Open! Deliveries start on Monday, September 22, 2003"/></a>
		</td>
	</tr>
	<tr><td class="bodyCopySmall">
		<img src="/media_stat/images/layout/clear.gif" width="1" height="4"/><br/><b>You ask, we listen.</b> We've prepared a full line of custom-cut Glatt Kosher meat (seafood coming soon) just in time for the holidays. We've partnered with Rubashkin's - home of Aaron's Best Glatt Kosher meat to bring you and your family the finest. Hand-cut just for your order, all of our Kosher meat is certified under the watchful eyes of OU and KAJ supervision and of course we have your favorite Kosher brands such as Manischewitz and Osem.
		<br/><br/>
		<div align="center">
		<a href="http://www.freshdirect.com/department.jsp?deptId=kos&amp;trk=kosher" class="mainLink">Click here to visit our Kosher Department.</a>
		</div><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="6"/>
		</td>
	</tr>
	<tr>
		<td background="/media_stat/images/template/email/kosher/kos_star.gif"><img src="/media_stat/images/layout/clear.gif" width="1" height="24"/></td>
	</tr>
	<tr>
		<td align="center"><a href="http://www.freshdirect.com/department.jsp?deptId=kos&amp;trk=kosher"><img src="/media_stat/images/template/email/kosher/kos_icons.jpg" width="575" height="58" vspace="8" border="0"/></a>
		<br/><img src="/media_stat/images/layout/clear.gif" width="1" height="4"/>
		</td>
	</tr>
	<tr><td><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/></td></tr>
</table>

</xsl:template>

</xsl:stylesheet>
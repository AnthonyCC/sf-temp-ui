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
		<title> Tell us what you think.</title>
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

<table cellpadding="0" cellspacing="0">
<tr>
<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
<td><xsl:call-template name="h_header_v1" />
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
			<td WIDTH="100%"><table WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0"><tr><td BGCOLOR="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1" border="0" alt="" /></td></tr></table></td>
		</tr>
	</table><br/>
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
		<td class="fdEmail">	
			<p>In an effort to improve our service and quality, we would like to hear what you think about FreshDirect. We're working with Linescale Research, a New York City research company, to conduct an anonymous survey that will help us better serve our customers. It will only take a few minutes to complete the survey, and your feedback will be greatly appreciated. Your answers are anonymous, all information is kept private, and you have our promise that we'll listen to what you have to say. Plus, when you complete the survey, you will automatically be entered into Linescale's $100 prize drawing.</p>

			<p>To participate, just click on this link: <a href="http://linescale.com/freshdirect6001">http://linescale.com/freshdirect6001</a> and Linescale Research will let you know right away if the survey is still open.</p>

			<p>Thank you for your help,
			<br/>Joe Fedele
			<br/>CEO and Creator, FreshDirect
			<br/>Co-founder of Fairway Uptown</p>
			
			<p><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/></p>
		</td>
	</tr>
</table>
</td>
<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
</tr>
</table>

</xsl:template>

</xsl:stylesheet>

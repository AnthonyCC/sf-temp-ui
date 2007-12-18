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
		<title>Thank you for your interest in FreshDirect AT THE OFFICE</title>
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

<table cellpadding="0" cellspacing="0">
<tr>
<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
<td><xsl:call-template name="h_header_v1" />
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
		<td width="100%"><table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td bgcolor="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1" border="0" alt="" /></td></tr></table></td>
		</tr>
	</table><br/>
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr valign="top">
			<td>
			<p>
			<img src="http://www.freshdirect.com/media_stat/images/template/corp_svc/logo.gif" align="left" width="173" height="46" border="0" hspace="15" />
			You recently expressed interest in the FreshDirect AT THE OFFICE program for office deliveries. We have asked a select group of respondents to fill out a short survey so that we can better understand the food and beverage needs of your business.
			<br/><br/>
			Please take a few moments to fill out our <a href="http://www.freshdirect.com/survey/cos.jsp">FreshDirect AT THE OFFICE survey</a>. 
			<br/><br/>
			If you wish, you may also forward the link (<a href="http://www.freshdirect.com/survey/cos.jsp">http://www.freshdirect.com/survey/cos.jsp</a>) to someone in your company or organization for completion. 
			<br/><br/>
			In either case, we greatly appreciate your time and interest.
			</p>
			<p><xsl:call-template name="h_footer_v2"/></p>
	        </td>
	    </tr>
	</table>
</td>
<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
</tr>
</table>

</xsl:template>

</xsl:stylesheet>
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
		<title>Message regarding your order for June 17, 2004</title>
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
			Dear FreshDirect Customer, 
			<br/><br/>
			Thank you for your order. As always, we strive to provide you with the highest quality products, at great prices, delivered right to your door. 
			<br/><br/>
			Due to a gap in the picking season of cherries from one growing area to another, we may be unable to deliver the cherries that you ordered for tomorrow. We wanted to let you know, and give you the opportunity to replace the cherries with another fruit of the season. 
			<br/><br/>
			<a href="http://www.freshdirect.com/department.jsp?deptId=fru">http://www.freshdirect.com/department.jsp?deptId=fru</a>
			<br/><br/>
			If you have any questions, please contact us via email at <a href="mailto:service@freshdirect.com">service@freshdirect.com</a> or by phone at 1-866-283-7374. We're here Monday through Thursday from 8:00AM to 1:00AM, Friday from 8:00AM to 10:00PM and Saturday from 7:30AM to 10:00PM. Sunday we're here from 7:30AM to 1:00AM. 
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
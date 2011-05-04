<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:doc="http://xsltsl.org/xsl/documentation/1.0"
	xmlns:str="http://xsltsl.org/string"
	extension-element-prefixes="doc str">
	<xsl:output method="html"/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_footer_v2.xsl'/>
	<xsl:include href='string.xsl' />
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">

<html>
	<head>
        <BASE href="http://www.freshdirect.com" /> 
		<title>Broadband network issues affecting many FreshDirect customers</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
	</head>
<body bgcolor="#FFFFFF" text="#333333">
	<xsl:call-template name="mail_body" />
</body>
</html>

</xsl:template>

<xsl:template name="mail_body">

<table cellpadding="0" cellspacing="0">
<tr>
<!-- =============== START LEFT SPACER =============== -->
<td><img src="/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END LEFT SPACER ================= -->

<!-- ~~~~~~~~~~~~~~~ START CONTENT ~~~~~~~~~~~~~~~ -->
<td><xsl:call-template name="h_header_v1" />
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
		<td width="100%"><table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0" alt="" /></td></tr></table></td>
		</tr>
	</table><br/>
	<table cellpadding="0" cellspacing="0" width="90%">
                <tr>
                <td>
                <p>Dear <xsl:value-of select="customer/firstName"/>,</p>

<p>It has come to our attention that recent network problems for some Road Runner, Earthlink and AOL/Time Warner Broadband customers have prevented them from accessing our website. Please be assured that all of our systems are up and running and that all orders placed will be delivered as normal. These difficulties are caused by a network problem outside of our control - disrupting access not only to FreshDirect but to many other sites as well.</p>

<p>We hope this issue to be resolved shortly. In the meantime, if you have been unable to access www.freshdirect.com please call us toll free at 1-866-2UFRESH. We understand the inconvenience that this must cause during the busy holiday season and have Customer Service Representatives standing by who can place an order on your behalf or answer questions about your account. You may also use a dial-up account to access our site, as these problems only affect some broadband subscribers.</p>

<p>If you experience problems accessing our site at <a href="http://www.freshdirect.com">http://www.freshdirect.com</a>, please let us know by responding to this email.</p>

<p>Sincerely,<br/>
Dean Furbush<br/>
Chief Operating Officer, FreshDirect</p>

<p><xsl:call-template name="h_footer_v2"/></p>
                </td>
                </tr>
</table>
</td>
<!-- ~~~~~~~~~~~~~~~ END CONTENT ~~~~~~~~~~~~~~~ -->

<!-- =============== BEGIN RIGHT SPACER =============== -->
<td><img src="/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END RIGHT SPACER ================= -->
</tr>
</table>

</xsl:template>

</xsl:stylesheet>
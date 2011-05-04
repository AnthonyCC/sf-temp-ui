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
		<title>FreshDirect Brooklyn rollout update.</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
	</head>
<body bgcolor="#FFFFFF" text="#333333">
	<xsl:call-template name="mail_body" />
</body>
</html>

</xsl:template>

<xsl:template name="mail_body">

<xsl:comment>

www.FRESHDIRECT.com
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Dear <xsl:value-of select="customer/firstName"/>,

Last Saturday, FreshDirect opened the first of our Brooklyn service areas. An email was sent to everyone who - like yourself - had requested notification when we came to the neighborhood. Alas, technical difficulties and surprisingly high demand combined to make delivery unavailable for some customers who tried to place orders for Brooklyn. Rest assured that these problems have been addressed and that we are very happy to have begun our long-awaited Brooklyn expansion.

As a reminder, you can order online today at www.FreshDirect.com and get $50 worth of free fresh food on your first order of $100 or more*. To top it off, your first delivery is free.

Sincerely,

FreshDirect

*See website for details.

======
When you signed up with FreshDirect, you indicated an interest in receiving newsletters and updates. You may change this preference online in Your Account or REPLY via email and write UNSUBSCRIBE in the subject line.
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

<table width="90%" cellpadding="0" cellspacing="0">
<tr>
<td><img src="/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
<td><xsl:call-template name="h_header_v1" />
	<table cellpadding="0" cellspacing="0" width="100%">
		<tr>
			<td WIDTH="100%"><table WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0"><tr><td BGCOLOR="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0" alt="" /></td></tr></table></td>
		</tr>
	</table><br/>
			<p><b>Dear <xsl:value-of select="customer/firstName"/>,</b></p>

			<p>Last Saturday, FreshDirect opened the first of our Brooklyn service areas. An email was sent to everyone who - like yourself - had requested notification when we came to the neighborhood. Alas, technical difficulties and surprisingly high demand combined to make delivery unavailable for some customers who tried to place orders for Brooklyn. Rest assured that these problems have been addressed and that we are very happy to have begun our long-awaited Brooklyn expansion.</p>
			
			<p>As a reminder, you can order online today at <a href="http://www.freshdirect.com">www.FreshDirect.com</a> and get $50 worth of free fresh food on your first order of $100 or more*. To top it off, your first delivery is free.</p>
			
			<p>Sincerely,</p>

			<p><b>FreshDirect</b></p>
			
			<p>*See <a href="http://www.freshdirect.com">website</a> for details.</p>

			<p><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/></p>
</td>
<td><img src="/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
</tr>
</table>

</xsl:template>

</xsl:stylesheet>
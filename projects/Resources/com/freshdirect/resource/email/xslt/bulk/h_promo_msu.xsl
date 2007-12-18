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
		<title>Free Pizza</title>
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
			<td width="100%">
			<p>
			Dear <xsl:value-of select="customer/firstName"/>,
			<br/><br/>
			Thank you for your participation in the Michigan State University survey about FreshDirect. Knowing what our customers think is tremendously important to us and we appreciate the time you took to complete the survey. 
			<br/><br/>
			To receive a free pizza with your next order, just enter the following promotional code on the shopping cart page the next time you check out:*
			<br/><br/>
			Promo Code: <b>PIZZA4</b>
			<br/><br/>
			Once again, thank you for your feedback. 
			<br/><br/>
			Sincerely,<br/>
			FreshDirect
			<br/><br/>
			* Pizzas will be given to all participants who enter a valid ID and complete the survey. Approximate retail value of pizza is $5.99. Survey participants must be registered customers. One pizza per household. When you've completed the survey you'll receive a promotion code via email that you can enter on the Shopping Cart page the next time you order from FreshDirect. The pizza will be delivered with the rest of your food. Offer good for limited time only. Promotion expires on August 1. Terms of FreshDirect Customer Agreement apply
			</p>
	        <xsl:call-template name="h_footer_v2"/></td></tr>
	</table>
</td>
<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
</tr>
</table>

</xsl:template>

</xsl:stylesheet>
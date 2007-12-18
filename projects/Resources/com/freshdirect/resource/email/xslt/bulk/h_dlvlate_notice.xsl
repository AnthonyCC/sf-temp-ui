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
		<title>FreshDirect Delivery Notice</title>
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
				<p>Dear FreshDirect Customer,</p>
				
				<p>Last night we had a number of late deliveries in your neighborhood and we at FreshDirect apologize for any inconveniences we may have caused you.</p>
				
				<p>We strive to deliver each and every order on time every day. However, occasionally we are subject to unforeseen circumstances causing a delay in deliveries. Last night, we were faced with two problems, a delivery personnel shortage and the rainy weather. We take responsibility for the shortage of delivery personnel and apologize for not having your order to you on time. We are adding extra delivery personnel to your neighborhood to avoid similar situations.</p>  
				
				<p>We appreciate your understanding and thank you for being a FreshDirect customer.</p> 
				
				<p>If you have any other questions, please call us toll-free at 1-866 2UFRESH (1-866 283-7374). We're here Sunday-Friday 8am-1am and Saturdays 8am-9pm. Or, you may email us at <a href="mailto:service@freshdirect.com">service@freshdirect.com</a>.</p>
				
				<p>The FreshDirect Customer Service Team</p>
				
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
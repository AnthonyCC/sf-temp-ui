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
		<title>$10 Free Fresh Food Offer - Expires September 1, 2003!!</title>
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
<td><img src="/media_stat/images/layout/clear.gif" alt="" width="20" height="1" border="0" /></td>
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

                <p>We hope you enjoyed FreshDirect's high quality food and incredible savings.  Now we're giving you 10 good reasons to come back for seconds.</p>
                
				<p>Just place your next web order ($40 minimum), and give us your thoughts. We'll give you $10 more free fresh food.* But in order to receive your <b>$10 of free fresh food, you must place your next order by September 1, 2003</b>.  After this date the offer expires and you'll miss your chance to sample the freshest produce, meat, seafood, coffee and so much more for <b>free</b>.</p>
				
                <p>Log onto <a href="http://www.freshdirect.com">www.freshdirect.com</a> and receive your $10 of free fresh food today!</p>
                
                <p>Happy eating,<br/>
				Joe Fedele<br/>
				CEO<br/>
				<a href="http://www.freshdirect.com">FreshDirect</a>
				</p>
				
				<p class="bodyCopySmall">*Web orders and home delivery orders only. Offer expires September 1, 2003. One per household. Certain fresh food items may be excluded, $15 limit per item.  Delivery and billing address must match. Available in selected zones.</p>
                </td>
                </tr>
		<tr>
		<td>
			<br/><p><xsl:call-template name="h_footer_v2"/></p>
		</td>
	</tr>
</table>
</td>
<!-- ~~~~~~~~~~~~~~~ END CONTENT ~~~~~~~~~~~~~~~ -->

<!-- =============== BEGIN RIGHT SPACER =============== -->
<td><img src="/media_stat/images/layout/clear.gif" alt="" width="20" height="1" border="0" /></td>
<!-- =============== END RIGHT SPACER ================= -->
</tr>
</table>

</xsl:template>

</xsl:stylesheet>
<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_common_functions_v2.xsl'/>
	<xsl:include href='h_footer_v1.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<html lang="en-US" xml:lang="en-US">
<head>
	<title>Credit Card Authorization Failure For Your Order</title>
	<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
</head>
<body bgcolor="#FFFFFF">

<table cellpadding="0" cellspacing="0" width="100%">
<tr>
<!-- =============== START LEFT SPACER =============== -->
<td><img src="/images/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END LEFT SPACER ================= -->

<!-- ~~~~~~~~~~~~~~~ START CONTENT ~~~~~~~~~~~~~~~ -->
<td><xsl:call-template name="h_header_v1" />
	<table cellpadding="0" cellspacing="0" width="90%">
				<tr><td width="100%" bgcolor="#CCCCCC"><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></td>
				</tr>
	</table><br/>
	<table cellpadding="5" cellspacing="0" width="90%">
				<tr>
					<td colspan="2">
						<p><b>Dear <xsl:value-of select="customer/firstName"/></b>,</p><br />
					</td>
				</tr>
				<tr>
					<td valign="top">
						<p>We are unable to process your order <b>(#<xsl:value-of select="orderNumber"/>)</b> using the Gift Card you have chosen.
The card may be on hold, or there may be a network error. So that we can process your order as soon as possible, please call customer service at <xsl:value-of select="customer/customerServiceContact"/>. </p>
<br/>

<p>To be sure that your order is delivered, please make any changes before <xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="cutoffTime"/></xsl:call-template> on <b><xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="cutoffTime"/></xsl:call-template></b>.
						If you need further assistance, <xsl:for-each select="contactHours/contactHours"><xsl:value-of select="daysDisplay"/><xsl:value-of select="hoursDisplay"/>;</xsl:for-each></p>
					</td>
					
				</tr>
				<tr>
					<td colspan="2">
							<p>Sincerely,
							<br/>
							FreshDirect Customer Service<br/>
							www.freshdirect.com</p>
							<p><b> To view the FreshDirect Gift Cards Terms and Conditions, <a href="http://www.freshdirect.com/media/editorial/giftcards/media_includes/terms_and_conditions.html">Please click here</a>.</b></p>
				<p><xsl:call-template name="h_footer_v1"/></p>
				</td></tr>
			</table>
		</td>
<!-- ~~~~~~~~~~~~~~~ END CONTENT ~~~~~~~~~~~~~~~ -->

<!-- =============== BEGIN RIGHT SPACER =============== -->
<td><img src="/images/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END RIGHT SPACER ================= -->
</tr>
</table>
</body>
</html>
</xsl:template>

</xsl:stylesheet>
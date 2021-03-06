<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_invoice_info_v1.xsl'/>
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
						<p>We are unable to process your order <b>(#<xsl:value-of select="orderNumber"/>)</b>
						, scheduled for delivery between
						<b><xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="deliveryStartTime"/></xsl:call-template>
						and <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="deliveryEndTime"/></xsl:call-template></b>
						on <b><xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="deliveryStartTime"/></xsl:call-template></b>
						using the payment method you have chosen. Credit card authorization can fail for a number of reasons -- often the cause is an incorrect expiration date.
						So that we can process your order as soon as possible, please call customer service at <xsl:value-of select="customer/customerServiceContact"/>.</p>
						<p>To be sure that your order is delivered, please make any changes before <xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="cutoffTime"/></xsl:call-template> on <b><xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="cutoffTime"/></xsl:call-template></b>.
						If you need further assistance, we're here:</p>
					</td>
					<td width="200px" valign="top" style="border: 1px solid #666; background-color: #eee; padding: 5px;">
						<table align="center" cellspacing="0" width="200px">
							<tr align="center">
								<td><strong>Customer Service Hours</strong></td>
							</tr>
							<tr align="center">
								<td><div style="border-top: 1px solid #666; padding: 1px;"><br /></div></td>
							</tr>
							<xsl:for-each select="contactHours/contactHours">

							<tr align="center">
								<td style="border: 1px solid #666; padding: 0px; background-color: #ccc;"><strong><xsl:value-of select="daysDisplay"/></strong></td>
							</tr>
							<tr align="center" style="background-color: #eee;">
								<td><xsl:value-of select="hoursDisplay"/></td>
							</tr>
							</xsl:for-each>
							
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="2">
							<p>Sincerely,
							<br/>
							FreshDirect Customer Service<br/>
							www.freshdirect.com</p>
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
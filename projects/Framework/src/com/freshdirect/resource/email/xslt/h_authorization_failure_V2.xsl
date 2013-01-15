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
<html>
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
						<p>Unfortunately, the credit or debit card you selected for your upcoming FreshDirect order, <b>(#<xsl:value-of select="orderNumber"/>)</b>,  appears to be incorrect, and we are not able to process your payment. Without a valid payment method, we will not be able to deliver your order.</p>
						<p>To avoid order cancellation, please go to www.freshdirect.com now, to correct your payment information or select a different payment method.</p>
						<p>Please modify your pending order at www.freshdirect.com. At Step 3 of the checkout process, you will have a few options:
							 Correct the current credit or debit card number
							 Select a different payment method that's already on your account
							 Add a new payment method</p>
						<p>.<xsl:element name = "a"><xsl:attribute name = "href">http://www.freshdirect.com/your_account/order_details.jsp?orderId=<xsl:value-of select="orderNumber"/></xsl:attribute> Update this order.</xsl:element></p>
						<p>Be sure to re-submit your order with a new payment type before <xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="cutoffTime"/></xsl:call-template> on <b><xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="cutoffTime"/></xsl:call-template></b> to avoid cancellation.
						</p>
						<p>We accept Visa, MasterCard, American Express, Discover and online check payments.</p>
						<p>If you need further assistance, Please call our Customer Service Team  at  <b><xsl:value-of select="customer/customerServiceContact"/></b>.</p>
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
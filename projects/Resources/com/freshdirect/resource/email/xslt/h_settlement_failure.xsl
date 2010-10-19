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
	<title>E-Check Settlement Failure For Your Order</title>
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
						<p>We are unable to process the e-Check payment for your order <b>(#<xsl:value-of select="orderNumber"/>)</b>
						, which was delivered on <b><xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="deliveryStartTime"/></xsl:call-template>.</b>
						 Please contact customer service at <xsl:choose><xsl:when test="customer/chefsTable = 'true'">1-866-511-1240</xsl:when><xsl:otherwise>1-212-796-8002</xsl:otherwise></xsl:choose>,
						within the next 24 hours so that we may help you resolve the payment discrepancy. Often times, these issues are caused by an input error.
						   Pursuant to your agreement with us, should we not hear from you within 24 hours, we will attempt to settle payment using the other payments methods listed within your account.
						   Thank you.</p>
<p>If you need further assistance, we're here:</p>
					</td>
					<td width="200px" valign="top" style="border: 1px solid #666; background-color: #eee; padding: 5px;">
						<table align="center" cellspacing="0" width="200px">
							<tr align="center">
								<td><strong>Customer Service Hours</strong></td>
							</tr>
							<tr align="center">
								<td><div style="border-top: 1px solid #666; padding: 1px;"><br /></div></td>
							</tr>
							<tr align="center">
								<td style="border: 1px solid #666; padding: 0px; background-color: #ccc;"><strong>Monday-Thursday</strong></td>
							</tr>
							<tr align="center" style="background-color: #eee;">
								<td>6:30 AM to 12 AM</td>
							</tr>
							<tr align="center">
								<td style="border: 1px solid #666; padding: 0px; background-color: #ccc;"><strong>Friday</strong></td>
							</tr>
							<tr align="center">
								<td>6:30 AM to 11 PM</td>
							</tr>
							<tr align="center">
								<td style="border: 1px solid #666; padding: 0px; background-color: #ccc;"><strong>Saturday</strong></td>
							</tr>
							<tr align="center" style="background-color: #eee;">
								<td>7:30 AM to 8 PM</td>
							</tr>
							<tr align="center">
								<td style="border: 1px solid #666; padding: 0px; background-color: #ccc;"><strong>Sunday</strong></td>
							</tr>
							<tr align="center" style="background-color: #eee;">
								<td>7:30 AM to 12 AM</td>
							</tr>
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
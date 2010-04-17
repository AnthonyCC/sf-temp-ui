<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_order_info_v1.xsl'/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_common_functions_v2.xsl'/>
	<xsl:include href='h_footer_v1.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<html>
<head>
	<title>Reminder, your standing order for <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime" /></xsl:call-template></title>
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
		<tr><td><br/></td></tr>
					<tr>
						<td>
							<p><b>Hello <xsl:value-of select="customer/firstName"/></b>,</p>

							<p>Just a friendly reminder that
							<xsl:choose>
								<xsl:when test="order/deliveryType != 'H' and order/deliveryType != 'C'">you can pick up your <a href="http://www.freshdirect.com/quickshop/so_details.jsp?ccListId={standingOrder/customerListId}">standing order</a></xsl:when>
								<xsl:otherwise>your <a href="http://www.freshdirect.com/quickshop/so_details.jsp?ccListId={standingOrder/customerListId}">standing order</a> is scheduled for delivery</xsl:otherwise>
							</xsl:choose>
							between <b><xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template>
							and <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/endTime"/></xsl:call-template>
							</b> on <b><xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime" /></xsl:call-template></b>.
							</p>

							<p>As soon as we select and weigh your items, we'll send you an e-mail with the final order total. We'll also include an itemized, printed receipt with your delivery.</p>

							<xsl:if test="order/paymentMethod/paymentType != 'M'">
							<p>If you have last-minute updates or additions to your order, go to <a href="http://www.freshdirect.com/your_account/manage_account.jsp">your account</a> to make changes before <xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/cutoffTime" /></xsl:call-template> on <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/cutoffTime" /></xsl:call-template>.</p>
							</xsl:if>
							
							<p>We hope you enjoy everything in your order. Please <a href="http://www.freshdirect.com/">come back</a> soon.</p>

							<p><b>Happy eating!</b><br/>
							<br/>
							FreshDirect<br/>
							<xsl:choose><xsl:when test="order/deliveryType != 'C'">Customer Service Group</xsl:when><xsl:otherwise>Corporate Services Group</xsl:otherwise></xsl:choose></p>

							<p><xsl:call-template name="h_order_info_v1"/></p>
							
							<p>NOTE: If this email does not print out clearly, please go to <a href="https://www.freshdirect.com/your_account/order_history.jsp">https://www.freshdirect.com/your_account/order_history.jsp</a> for a printer-friendly version of your order details.</p>

							<p><xsl:call-template name="h_footer_v1"/></p>

						</td>
					</tr>
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

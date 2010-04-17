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
	<title>Your standing order for <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime" /></xsl:call-template><xsl:if test="hasUnavailableItems"> (some items unavailable)</xsl:if></title>
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
							<!-- FIRST ORDER TEXT -->
							<xsl:choose>
								<!-- 	FOURTH  PLUS ORDER TEXT -->
								<xsl:when test="customer/numberOfOrders &gt; 3">
									<p><b>Hello <xsl:value-of select="customer/firstName"/></b>,</p>
	
									<p>Your standing order has been scheduled for delivery.
									<xsl:choose>
										<xsl:when test="order/deliveryType != 'H' and order/deliveryType != 'C'">You can pick up</xsl:when>
										<xsl:otherwise>We will deliver</xsl:otherwise>
									</xsl:choose>
									your food between <b><xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template>
									and <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/endTime"/></xsl:call-template>
									</b> on <b><xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime" /></xsl:call-template></b>.
									<xsl:if test="order/paymentMethod/paymentType = 'M'">
									<b>Please note that you are not being charged for this order. The amount displayed below, as well as your account with FreshDirect, will reflect a zero balance within the next 48 hours.</b>
									</xsl:if>
									</p>


									<p>As soon as we select and weigh your items, we'll send you an e-mail with the final order total. We'll also include an itemized, printed receipt with your delivery.</p>
                                                                        
                                    <xsl:choose>
                                        <xsl:when test="order/deliveryType != 'H' and order/deliveryType != 'C'"> You'll need to bring photo ID to pick up your food. Just present it to the attendant when you arrive. You are under no obligation to tip but have the option of providing a nominal tip if you feel that you've received exceptional service.</xsl:when>
                                        <xsl:otherwise>You'll know your order has arrived when a uniformed FreshDirect delivery person appears at your door bearing boxes of fresh food. You are under no obligation to tip but have the option of providing a nominal tip if you feel that you've received exceptional service.</xsl:otherwise>
                                    </xsl:choose>
									
									<xsl:if test="order/deliveryAddress/unattendedDeliveryFlag = 'OPT_IN'">
									<p>
									<b>Unattended delivery service.</b> If you need to run out during your delivery time slot, we will leave your order at your home. Alcohol cannot be left unattended, and will be removed from your order if you are not home.
									</p>
									</xsl:if>

									<xsl:if test="order/paymentMethod/paymentType != 'M'">
									<p>
									<xsl:if test="hasUnavailableItems">
									<b style="color: #990000;">Please note that some of the items in your standing order list were not available when your order was placed. Please check the content of the order below to make sure you're getting everything you need.</b>
									</xsl:if>
									If you have last-minute updates or additions to your order, go to <a href="http://www.freshdirect.com/your_account/manage_account.jsp">your account</a> to make changes before <xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/cutoffTime" /></xsl:call-template> on <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/cutoffTime" /></xsl:call-template>.</p>
									</xsl:if>
									
									<p>We hope you enjoy everything in your order. Please <a href="http://www.freshdirect.com/">come back</a> soon.</p>
								</xsl:when>
								<!-- SECOND / THIRD ORDER TEXT -->
								<xsl:otherwise>
									<p><b>Dear <xsl:value-of select="customer/firstName"/></b>,</p>

									<p>Your standing order has been scheduled for delivery.
									<xsl:choose>
										<xsl:when test="order/deliveryType != 'H' and order/deliveryType != 'C'">You can pick up</xsl:when>
										<xsl:otherwise>We will deliver</xsl:otherwise>
									</xsl:choose>
									your food between <b><xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template> 
									and <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/endTime"/></xsl:call-template></b> 
									on <b><xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime" /></xsl:call-template></b>.
									<xsl:if test="order/paymentMethod/paymentType = 'M'">
									<b>Please note that you are not being charged for this order. The amount displayed below, as well as your account with FreshDirect, will reflect a zero balance within the next 48 hours.</b>
									</xsl:if>
									</p>

									<xsl:if test="number(order/totalDiscountValue) &gt; 0">
										<p>We've taken $<xsl:value-of select='format-number(order/totalDiscountValue, "###,##0.00", "USD")'/> off your order.</p>
									</xsl:if>

									
									<p>As soon as we select and weigh your items, we'll send you an e-mail with the final order amount. We'll also include an itemized, printed receipt with your delivery.</p>

									<xsl:choose>
										<xsl:when test="order/deliveryType != 'H' and order/deliveryType != 'C'">
											<p>You'll need to bring photo ID to pick up your food. Just present it to the attendant when you arrive. You are under no obligation to tip but have the option of providing a nominal tip if you feel that you've received exceptional service.</p>
										</xsl:when>
										<xsl:otherwise>
											<p>You'll know your order has arrived when a uniformed FreshDirect delivery person appears at your door bearing boxes of fresh food. You are under no obligation to tip but have the option of providing a nominal tip if you feel that you've received exceptional service.</p>
										</xsl:otherwise>
									</xsl:choose>

									<xsl:if test="order/deliveryAddress/unattendedDeliveryFlag = 'OPT_IN'">
									<p>
									<b>Unattended delivery service.</b> If you need to run out during your delivery time slot, we will leave your order at your home. Alcohol cannot be left unattended, and will be removed from your order if you are not home.
									</p>
									</xsl:if>

									<xsl:if test="order/paymentMethod/paymentType != 'M'">
									<p>
									<xsl:if test="hasUnavailableItems">
									<b style="color: #990000;">Please note that some of the items in your standing order list were not available when your order was placed. Please check the content of the order below to make sure you're getting everything you need.</b>
									</xsl:if>
									If you have last-minute updates or additions to your order, go to <a href="http://www.freshdirect.com/your_account/manage_account.jsp">your account</a> to make changes before <xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/cutoffTime" /></xsl:call-template> on <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/cutoffTime" /></xsl:call-template>.</p>
									</xsl:if>
									
									<p>We hope you enjoy everything in your order. Please <a href="http://www.freshdirect.com/">come back</a> soon.</p>
								</xsl:otherwise>
							</xsl:choose>

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

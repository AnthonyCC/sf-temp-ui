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
<html lang="en-US" xml:lang="en-US">
<head>
	<title>Your standing order for <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime" /></xsl:call-template><xsl:if test="hasUnavailableItems"> (some items unavailable)</xsl:if></title>
</head>
<body bgcolor="#FFFFFF">
<table cellpadding="0" cellspacing="0" width="100%">
<tr>
<!-- =============== START LEFT SPACER =============== -->
<td><img src="/images/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END LEFT SPACER ================= -->

<!-- ~~~~~~~~~~~~~~~ START CONTENT ~~~~~~~~~~~~~~~ -->
<td><xsl:call-template name="h_header_v1" />
	<table cellpadding="0" cellspacing="0" width="90%" style="padding:0;margin:0;border-collapse:collapse;border-spacing:0;border-style:none;">
		<tr><td width="100%" height="1"  bgcolor="#CCCCCC"><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></td>
		</tr>
			<tr>
				<td style="font-family: Verdana, Arial, sans-serif;font-size:12px;">
					<br />
					<b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;">Hello <xsl:value-of select="customer/firstName"/></b>,<br />
					<br />
					
					Your standing order (<xsl:value-of select="standingOrder/customerListName"/>) has been scheduled for delivery.
					<xsl:choose>
						<xsl:when test="order/deliveryType != 'H' and order/deliveryType != 'C'">You can pick up</xsl:when>
						<xsl:otherwise>We will deliver</xsl:otherwise>
					</xsl:choose>
					your food between <b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;"><xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template> 
					and <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/endTime"/></xsl:call-template></b> 
					on <b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;"><xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime" /></xsl:call-template></b>.
					<br />
					<br />

					<xsl:if test="number(order/totalDiscountValue) &gt; 0">
						We've taken $<xsl:value-of select='format-number(order/totalDiscountValue, "###,##0.00", "USD")'/> off your order.
						<br />
						<br />
					</xsl:if>


					<xsl:if test="order/paymentMethod/paymentType != 'M' and hasUnavailableItems = 'true'">
					<span style="font-weight:bold;color:#990000;font-family: Verdana, Arial, sans-serif;font-size:12px;">Unfortunately, the following items in your standing order were unavailable:</span>
					<br />
					<br />
					<table class="unavTable" cellpadding="0" cellspacing="0">
						<xsl:for-each select="unavailableItems/unavailableItems">
						<tr>
							<td class="descr-cell" style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;"><xsl:value-of select="description"/> <xsl:if test="configurationDesc != 'null'">&#160;&#160;&#160;(<xsl:value-of select="configurationDesc"/>)</xsl:if></td>
							<td width="20">&#160;</td>
							<td style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;">(<xsl:value-of select="unitPrice"/>)</td>
						</tr>
						</xsl:for-each>
					</table>
					<br />
					Please check the contents of your order below to make sure you're getting everything you need. If you would like to make changes or additions, modify this order in <a href="http://www.freshdirect.com/your_account/order_history.jsp" ><span style="color:#360;text-decoration:underline;font-family: Verdana, Arial, sans-serif;font-size:12px;">Your Orders</span></a>, located in Your Account, before <xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/cutoffTime" /></xsl:call-template> on <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/cutoffTime" /></xsl:call-template>.
					<br />
					<br />
					</xsl:if>

					<xsl:if test="order/paymentMethod/paymentType != 'M' and hasUnavailableItems = 'false'">
					If you would like to make updates or additions to your order, go to <a href="http://www.freshdirect.com/your_account/manage_account.jsp"><span style="color:#360;text-decoration:underline;font-family: Verdana, Arial, sans-serif;font-size:12px;">Your Account</span></a> to make changes before <xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/cutoffTime" /></xsl:call-template> on <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/cutoffTime" /></xsl:call-template>.
					<br />
					<br />
					</xsl:if>
					
					
					<br />
					As soon as we select and weigh your items, we'll send you an e-mail with the final order amount. We'll also include an itemized, printed receipt with your delivery.
					<br />
					<br />

					<xsl:choose>
						<xsl:when test="order/deliveryType != 'H' and order/deliveryType != 'C'">
							You'll need to bring photo ID to pick up your food. Just present it to the attendant when you arrive. You are under no obligation to tip but have the option of providing a tip if you feel that you've received exceptional service. FreshDirect delivery personnel are not permitted to solicit tips under any circumstances.
							<br />
							<br />
						</xsl:when>
						<xsl:otherwise>
							You'll know your order has arrived when a uniformed FreshDirect delivery person appears at your door bearing boxes of fresh food. You are under no obligation to tip but have the option of providing a tip if you feel that you've received exceptional service. FreshDirect delivery personnel are not permitted to solicit tips under any circumstances. The delivery fee is not a gratuity for any FreshDirect employee who delivers or is otherwise involved with the delivery of your order and will not be given to any such employee as a gratuity.
							<br />
							<br />
						</xsl:otherwise>
					</xsl:choose>

					<xsl:if test="order/deliveryAddress/unattendedDeliveryFlag = 'OPT_IN'">
					
					<b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;">Unattended delivery service.</b> If you need to run out during your delivery time slot, we will leave your order at your home. Alcohol cannot be left unattended, and will be removed from your order if you are not home.

					<br />
					<br />
					</xsl:if>

					We hope you enjoy everything in your order. Please <a href="http://www.freshdirect.com/"><span style="color:#360;text-decoration:underline;font-family: Verdana, Arial, sans-serif;font-size:12px;">shop again soon</span></a>.
					<br />
					<br />

					<b style="font-weight:bold;">Happy eating!</b><br/>
					<br/>
					Your Customer Service Team at <a href="http://www.freshdirect.com/"><span style="color:#360;text-decoration:underline;font-family: Verdana, Arial, sans-serif;font-size:12px;">FreshDirect</span></a><br/>
					<br />

					<xsl:call-template name="h_order_info_v1"/>
					<br />
					<br />
					
					NOTE: If this email does not print out clearly, please go to <a href="https://www.freshdirect.com/your_account/order_history.jsp"><span style="color:#360;text-decoration:underline;font-family: Verdana, Arial, sans-serif;font-size:12px;">https://www.freshdirect.com/your_account/order_history.jsp</span></a> for a printer-friendly version of your order details.
					<br />
					<br />

					<xsl:call-template name="h_footer_v1"/>
					<br />
					<br />

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

<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_order_info_v1.xsl'/>
	<xsl:include href='x_common_functions_v2.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/> 
<xsl:template match="fdemail">

<xsl:call-template name="x_header"/>

Hello <xsl:value-of select="customer/firstName"/>,

Your standing order has been scheduled for delivery. <xsl:choose><xsl:when test="order/deliveryType != 'H' and order/deliveryType != 'C'">You can pick up</xsl:when><xsl:otherwise>We will deliver</xsl:otherwise></xsl:choose> your food between <xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template> and <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/endTime"/></xsl:call-template> on <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime" /></xsl:call-template>.

<xsl:if test="order/paymentMethod/paymentType = 'M'">
Please note that you are not being charged for this order. The amount displayed below, as well as your account with FreshDirect, will reflect a zero balance within the next 48 hours.
</xsl:if>

<xsl:if test="order/paymentMethod/paymentType != 'M'"><xsl:if test="hasUnavailableItems != 'false'">Some of the items in your standing order list were not available when your order was placed. Please check the content of the order below to make sure you're getting everything you need. </xsl:if>If you would like to make updates or additions to your order, go to your account to make changes before <xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/cutoffTime" /></xsl:call-template> on <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/cutoffTime" /></xsl:call-template>.</xsl:if>

As soon as we select and weigh your items, we'll send you an e-mail with the final order total. We'll also include an itemized, printed receipt with your delivery.

<xsl:choose>
<xsl:when test="order/deliveryType != 'H' and order/deliveryType != 'C'"> You'll need to bring photo ID to pick up your food. Just present it to the attendant when you arrive. You are under no obligation to tip but have the option of providing a nominal tip if you feel that you've received exceptional service.</xsl:when>
<xsl:otherwise>You'll know your order has arrived when a uniformed FreshDirect delivery person appears at your door bearing boxes of fresh food. You are under no obligation to tip but have the option of providing a nominal tip if you feel that you've received exceptional service.</xsl:otherwise>
</xsl:choose>

<xsl:if test="order/deliveryAddress/unattendedDeliveryFlag = 'OPT_IN'">

Unattended delivery service! If you need to run out during your delivery time slot, we will leave your order at your home. Alcohol cannot be left unattended, and will be removed from your order if you are not home.

</xsl:if>

We hope you enjoy everything in your order. Please shop again soon.

Happy eating!

Your Customer Service Team at FreshDirect
<xsl:call-template name="x_order_info_v1"/>

NOTE: If this email does not print out clearly, please go to https://www.freshdirect.com/your_account/order_history.jsp for a printer-friendly version of your order details.

<xsl:call-template name="x_footer_v1"/>

</xsl:template>

</xsl:stylesheet>

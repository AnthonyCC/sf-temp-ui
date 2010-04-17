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

Just a friendly reminder that <xsl:choose><xsl:when test="order/deliveryType != 'H' and order/deliveryType != 'C'">you can pick up your standing order (http://www.freshdirect.com/quickshop/so_details.jsp?ccListId=<xsl:value-of select="standingOrder/customerListId"/>)</xsl:when><xsl:otherwise>your standing order (http://www.freshdirect.com/quickshop/so_details.jsp?ccListId=<xsl:value-of select="standingOrder/customerListId"/>) will be delivered</xsl:otherwise></xsl:choose> between <xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template> and <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/endTime"/></xsl:call-template> on <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime" /></xsl:call-template>.

As soon as we select and weigh your items, we'll send you an e-mail with the final order total. We'll also include an itemized, printed receipt with your delivery.
<xsl:if test="order/paymentMethod/paymentType != 'M'">
If you have last-minute updates or additions to your order, go to your account to make changes before <xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/cutoffTime" /></xsl:call-template> on <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/cutoffTime" /></xsl:call-template>.
</xsl:if>
We hope you enjoy everything in your order. Please come back soon.


Happy eating!

FreshDirect
<xsl:choose><xsl:when test="order/deliveryType != 'C'">Customer Service Group</xsl:when><xsl:otherwise>Corporate Services Group</xsl:otherwise></xsl:choose>
<xsl:call-template name="x_order_info_v1"/>

NOTE: If this email does not print out clearly, please go to https://www.freshdirect.com/your_account/order_history.jsp for a printer-friendly version of your order details.

<xsl:call-template name="x_footer_v1"/>

</xsl:template>

</xsl:stylesheet>

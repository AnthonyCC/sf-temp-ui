<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_invoice_info_v1.xsl'/>	
	<xsl:include href='x_common_functions_v2.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text" indent="no"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">

<xsl:if test="order/deliveryReservation/deliveryETA/emailETAenabled = 'true'">
	<xsl:text><b>NEW! More time for you!</b> We’ve cut your wait time in half. Your order’s Estimated Time of Arrival (ETA) is between </xsl:text>
	<xsl:text><b><xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/deliveryETA/startTime"/></xsl:call-template></b></xsl:text> 
	<xsl:text> to </xsl:text>
	<xsl:text><b><xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/deliveryETA/endTime"/></xsl:call-template></b></xsl:text><br/><br/>
	<xsl:text>. We’ll meet you at the door!</xsl:text>
</xsl:if>

<xsl:text>Dear </xsl:text><xsl:value-of select="customer/firstName"/><xsl:text>,
</xsl:text>

<xsl:choose>
<xsl:when test="order/deliveryType = 'P'">	
			<xsl:text>Hello again! Your order (#</xsl:text><xsl:value-of select="order/erpSalesId"/><xsl:text>)</xsl:text>
	<xsl:choose>
		<xsl:when test="order/depotFacility = 'FreshDirect Pickup'">
			<xsl:text>is ready for pickup. Stop by our facility anytime between </xsl:text><xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template>
		</xsl:when>
		<xsl:otherwise>
			<xsl:text>is ready for pickup. Stop by the designated pickup location anytime between </xsl:text><xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template>
		</xsl:otherwise>
	</xsl:choose>
	<xsl:text> and </xsl:text><xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/endTime" /></xsl:call-template>
	<xsl:text> on </xsl:text><xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime" /></xsl:call-template>
	<xsl:text>to pick up your order. You'll need to bring <xsl:if test="order/paymentMethod/paymentMethodType = 'EBT'"> your EBT card ready to complete the purchase and a valid </xsl:if>photo ID to pick up your food. Just present it to the attendant when you arrive. 
	<xsl:if test="order/paymentMethod/paymentMethodType = 'EBT'"> If you need more information on the EBT purchase process, <a href="http://www.freshdirect.com/category.jsp?catId=about_ebt">click here</a> to see our EBT Info Page.</xsl:if>
	We hope you find everything absolutely fresh and delicious.

	</xsl:text>
</xsl:when>
<xsl:when test="order/deliveryReservation/deliveryETA/emailETAenabled = 'true'">
	<xsl:text>Hello again! Your order (#</xsl:text><xsl:value-of select="order/erpSalesId"/><xsl:text>) is on its way to you. </xsl:text>
	<xsl:text>Its ETA is between </xsl:text><xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/deliveryETA/startTime"/></xsl:call-template>
	<xsl:text> and </xsl:text><xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/deliveryETA/endTime" /></xsl:call-template>
	<xsl:text> on </xsl:text><xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime" /></xsl:call-template><xsl:text>. <xsl:if test="order/paymentMethod/paymentMethodType = 'EBT'">When your order arrives, please make sure you have your EBT card ready to complete the purchase.  If you need more information on the EBT purchase process, <a href="http://www.freshdirect.com/category.jsp?catId=about_ebt">click here</a> to see our EBT Info Page. </xsl:if></xsl:text>
	<xsl:text>We hope you find everything absolutely fresh and delicious.</xsl:text>
</xsl:when>
<xsl:otherwise>
	<xsl:text>Hello again! Your order (#</xsl:text><xsl:value-of select="order/erpSalesId"/><xsl:text>) is on its way to you. </xsl:text>
	<xsl:text>It will be delivered between </xsl:text><xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template>
	<xsl:text> and </xsl:text><xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/endTime" /></xsl:call-template>
	<xsl:text> on </xsl:text><xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime" /></xsl:call-template><xsl:text>. <xsl:if test="order/paymentMethod/paymentMethodType = 'EBT'">When your order arrives, please make sure you have your EBT card ready to complete the purchase.  If you need more information on the EBT purchase process, <a href="http://www.freshdirect.com/category.jsp?catId=about_ebt">click here</a> to see our EBT Info Page. </xsl:if></xsl:text>
	<xsl:text>We hope you find everything absolutely fresh and delicious.
		
</xsl:text>
</xsl:otherwise>
</xsl:choose>

<xsl:if test="count(order/shortedItems/shortedItems) > 0">
	<xsl:text>Unfortunately the following items were not available.  You will not be charged for these items.
	
	</xsl:text>
	<xsl:for-each select="order/shortedItems/shortedItems">
			<xsl:text>&#160;</xsl:text>
			<xsl:value-of select="orderedQuantity - deliveredQuantity" />&#160;<xsl:value-of select="unitsOfMeasure" />
			<xsl:value-of select="description" /><xsl:if test="configurationDesc != '' "><xsl:text> - </xsl:text>(<xsl:value-of select="configurationDesc"/>)
			</xsl:if>
	</xsl:for-each>
<xsl:text>
</xsl:text>
</xsl:if>

<xsl:choose>
	<xsl:when test="order/paymentMethod/paymentType = 'M'">Please note that you are not being charged for this order. The amount displayed below, as well as your account with FreshDirect, will reflect a zero balance within the next 48 hours.</xsl:when>
	<xsl:otherwise>
		<xsl:choose>
			<xsl:when test="order/paymentMethod/paymentMethodType = 'EBT'">
			<xsl:text>Your EBT Purchase Total is </xsl:text><b>$<xsl:value-of select='format-number(order/ebtPurchaseAmount, "###,##0.00", "USD")'/></b>.
			</xsl:when>
			<xsl:otherwise>
			<xsl:text>Your final total is $</xsl:text><b>$<xsl:value-of select='format-number(order/invoicedTotal, "###,##0.00", "USD")'/></b>.
			</xsl:otherwise>
		</xsl:choose>
	</xsl:otherwise>
</xsl:choose>
<xsl:text>View order details online: </xsl:text>

<xsl:text>http://www.freshdirect.com/your_account/order_history.jsp

</xsl:text>
<xsl:text>Come back again soon! Reordering is easy -- shop your top items and past orders to pick the items you want to buy. Yup, it's that fast.

</xsl:text>
		<xsl:text>Reorder the easy way:
</xsl:text>
		<xsl:text>http://www.freshdirect.com/quickshop/index.jsp
		
</xsl:text>
		<xsl:text>Thank you for your order and happy eating!
</xsl:text>


FreshDirect
<xsl:choose><xsl:when test="order/deliveryType != 'C'">Customer Service Group</xsl:when><xsl:otherwise>Corporate Services Group</xsl:otherwise></xsl:choose>
<xsl:call-template name="x_invoice_info_v1"/>

NOTE: If this email does not print out clearly, please go to https://www.freshdirect.com/your_account/order_details.jsp?orderId=<xsl:value-of select="order/erpSalesId"/> for a printer-friendly version of your order details.

<xsl:call-template name="x_footer_v1"/>

</xsl:template>

</xsl:stylesheet>
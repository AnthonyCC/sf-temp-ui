<?xml version="1.0"?>
<!DOCTYPE xsl:stylesheet [
    <!ENTITY nbsp "&#160;">
    <!ENTITY dot  "&#183;">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:output method="text"/>
	<xsl:template name="x_gc_order_info_v1">

PAYMENT INFORMATION FOR ORDER NUMBER #<xsl:value-of select="order/erpSalesId"/>

FreshDirect Store Credit:
$<xsl:value-of select='format-number(order/customerCreditsValue, "###,##0.00", "USD")'/>*
			
Order Total:
$<xsl:value-of select='format-number(order/total, "###,##0.00", "USD")'/>*
		
<xsl:if test="order/paymentMethod/paymentType != 'M'">
<xsl:value-of select="order/paymentMethod/paymentMethodType"/>
<xsl:call-template name="format-payment-method"><xsl:with-param name="paymentMethod" select="order/paymentMethod" /></xsl:call-template>			
</xsl:if>

BILLING ADDRESS
<xsl:value-of select="order/paymentMethod/name"/>
<xsl:call-template name="format-billing-address"><xsl:with-param name="billingAddress" select="order/paymentMethod" /></xsl:call-template>
<!--<xsl:call-template name="x_gc_recipient_info_v1"/>-->

<xsl:if test="order/paymentMethod/paymentType = 'M'">
Please note that you are not being charged for this order. The amount displayed above, as well as your account with FreshDirect, will reflect a zero balance within the next 48 hours.

</xsl:if>

</xsl:template>

</xsl:stylesheet>

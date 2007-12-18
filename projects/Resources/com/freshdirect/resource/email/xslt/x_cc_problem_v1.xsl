<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_common_functions_v1.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="erp-email">
Hello <xsl:value-of select="@first-name"/>,

We are unable to process your order (#<xsl:value-of select="@order-id"/>) scheduled for between <xsl:call-template name="format-delivery-start"/> and <xsl:call-template name="format-delivery-end"/> on <xsl:call-template name="format-delivery-date"/> using the payment method you have chosen.

Your food is packed and ready to go. Please call us at 1-866-283-7374 as soon as possible so that we can complete the delivery of your order.

Sincerely,

FreshDirect
Customer Service Group

<xsl:call-template name="x_footer_v1"/>

</xsl:template>

<!-- ORDER DATE FORMATTER -->
<!--<xsl:template name="format-order-date">
	<xsl:param name="year"><xsl:value-of select="substring(@order-date,1,4)"/></xsl:param>
	<xsl:param name="month"><xsl:value-of select="substring(@order-date,6,2)"/></xsl:param>
	<xsl:param name="day"><xsl:value-of select="substring(@order-date,9,2)"/></xsl:param>
	<xsl:param name="format" select="'%A, %B %d'"/>
	<xsl:call-template name="dt:format-date-time">
		<xsl:with-param name="year" select="$year"/>
		<xsl:with-param name="month" select="$month"/>
		<xsl:with-param name="day" select="$day"/>
		<xsl:with-param name="format" select="$format"/>
	</xsl:call-template>
</xsl:template>-->

</xsl:stylesheet>
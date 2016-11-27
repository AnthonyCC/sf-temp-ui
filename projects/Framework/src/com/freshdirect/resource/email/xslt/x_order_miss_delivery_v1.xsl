<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_common_functions_v1.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="erp-email">
<xsl:choose>
	<!-- 	*******************************************************
		(CUSTOMER AT FAULT == TRUE) TEXT
		*******************************************************-->
	<xsl:when test="@customer-at-fault='true'">
Dear <xsl:value-of select="@first-name"/>,

Unfortunately, since no one was available to receive our delivery, we were unable to complete your order (#<xsl:value-of select="@order-id"/>) scheduled for between <xsl:call-template name="format-delivery-start"/> and <xsl:call-template name="format-delivery-end"/> on <xsl:call-template name="format-delivery-date"/> to:

<xsl:apply-templates select="order/delivery-info/address"/>

We have attempted to reach you but were unable to do so. Please contact us as soon as possible at 1-866-283-7374 to discuss alternate delivery options.

Sincerely,

FreshDirect
Customer Service Group
	</xsl:when>
	<!-- 	*******************************************************
		(CUSTOMER AT FAULT == FALSE) TEXT
		*******************************************************-->
	<xsl:otherwise>
Dear <xsl:value-of select="@first-name"/>,

We were unable to deliver your order (#<xsl:value-of select="@order-id"/>) scheduled for between <xsl:call-template name="format-delivery-start"/> and <xsl:call-template name="format-delivery-end"/> on <xsl:call-template name="format-delivery-date"/> to:

<xsl:apply-templates select="order/delivery-info/address"/>

We are sorry for any inconvenience this has caused you. <xsl:value-of select="notes"/>

Please contact us as soon as possible at 1-866-283-7374 to reschedule delivery.

Our apologies,

FreshDirect
Customer Service Group
	</xsl:otherwise>
</xsl:choose>

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

<!-- DELIVERY START DATE/TIME FORMATTER -->
<!--<xsl:template name="format-delivery-start">
	<xsl:param name="str"><xsl:value-of select="order/delivery-info/@delivery-start-time"/></xsl:param>
	<xsl:param name="format" select="'%I:%M %p'"/>
	<xsl:call-template name="dt:format-date-time">
		<xsl:with-param name='hour' select='substring($str, 12, 2)'/>
		<xsl:with-param name='minute' select='substring($str, 15, 2)'/>
		<xsl:with-param name="format" select="$format"/>
	</xsl:call-template>
</xsl:template>-->

<!-- DELIVERY END DATE/TIME FORMATTER -->
<!--<xsl:template name="format-delivery-end">
	<xsl:param name="str"><xsl:value-of select="order/delivery-info/@delivery-end-time"/></xsl:param>
	<xsl:param name="format" select="'%I:%M %p'"/>
	<xsl:call-template name="dt:format-date-time">
		<xsl:with-param name='hour' select='substring($str, 12, 2)'/>
		<xsl:with-param name='minute' select='substring($str, 15, 2)'/>
		<xsl:with-param name="format" select="$format"/>
	</xsl:call-template>
</xsl:template>-->

<!--<xsl:template name="format-delivery-address">
	<xsl:value-of select="order/delivery-info/address/@address1"/><xsl:text> </xsl:text><xsl:if test="element-available('order/delivery-info/address/@apartment')"><xsl:value-of select="order/delivery-info/address/@apartment"/></xsl:if><xsl:text>
</xsl:text>
<xsl:if test="element-available('order/delivery-info/address/@address2')"><xsl:value-of select="order/delivery-info/address/@address2"/><xsl:text>
</xsl:text></xsl:if>
<xsl:value-of select="order/delivery-info/address/@city"/>,<xsl:text> </xsl:text><xsl:value-of select="order/delivery-info/address/@state"/><xsl:text> </xsl:text><xsl:value-of select="order/delivery-info/address/@zip-code"/><xsl:text>
</xsl:text>
</xsl:template>-->

</xsl:stylesheet>
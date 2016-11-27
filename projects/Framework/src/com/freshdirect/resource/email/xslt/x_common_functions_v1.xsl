<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:output method="text"/>

	<!-- DELIVERY DATE FORMATTER -->
	<xsl:template name="format-delivery-date">
		<xsl:param name="year"><xsl:value-of select="substring(order/delivery-info/@delivery-start-time,1,4)"/></xsl:param>
		<xsl:param name="month"><xsl:value-of select="substring(order/delivery-info/@delivery-start-time,6,2)"/></xsl:param>
		<xsl:param name="day"><xsl:value-of select="substring(order/delivery-info/@delivery-start-time,9,2)"/></xsl:param>
		<xsl:param name="format" select="'%A, %B %d'"/>
		<xsl:call-template name="dt:format-date-time">
			<xsl:with-param name="year" select="$year"/>
			<xsl:with-param name="month" select="$month"/>
			<xsl:with-param name="day" select="$day"/>
			<xsl:with-param name="format" select="$format"/>
		</xsl:call-template>
	</xsl:template>

	<!-- DELIVERY START TIME FORMATTER -->
	<xsl:template name="format-delivery-start">
		<xsl:param name="str"><xsl:value-of select="order/delivery-info/@delivery-start-time"/></xsl:param>
		<xsl:param name="format" select="'%I:%M %p'"/>
		<xsl:call-template name="dt:format-date-time">
			<xsl:with-param name='hour' select='substring($str, 12, 2)'/>
			<xsl:with-param name='minute' select='substring($str, 15, 2)'/>
			<xsl:with-param name="format" select="$format"/>
		</xsl:call-template>
	</xsl:template>

	<!-- DELIVERY END TIME FORMATTER -->
	<xsl:template name="format-delivery-end">
		<xsl:param name="str"><xsl:value-of select="order/delivery-info/@delivery-end-time"/></xsl:param>
		<xsl:param name="format" select="'%I:%M %p'"/>
		<xsl:call-template name="dt:format-date-time">
			<xsl:with-param name='hour' select='substring($str, 12, 2)'/>
			<xsl:with-param name='minute' select='substring($str, 15, 2)'/>
			<xsl:with-param name="format" select="$format"/>
		</xsl:call-template>
	</xsl:template>

<!-- DELIVERY ADDRESS FORMATTER -->
<!--<xsl:template name="format-delivery-address">
<xsl:value-of select="order/delivery-info/address/@address1"/><xsl:text> </xsl:text><xsl:if test="element-available('order/delivery-info/address/@apartment')"><xsl:value-of select="order/delivery-info/address/@apartment"/></xsl:if><xsl:text>
</xsl:text>
<xsl:if test="element-available('order/delivery-info/address/@address2')"><xsl:value-of select="order/delivery-info/address/@address2"/><xsl:text>
</xsl:text></xsl:if>
<xsl:value-of select="order/delivery-info/address/@city"/>,<xsl:text> </xsl:text><xsl:value-of select="order/delivery-info/address/@state"/><xsl:text> </xsl:text><xsl:value-of select="order/delivery-info/address/@zip-code"/><xsl:text>
</xsl:text>
</xsl:template>-->

<!-- PAYMENT METHOD FORMATTER -->
<xsl:template name="format-payment-method">
<xsl:value-of select="order/payment-method/@name"/><xsl:text>
</xsl:text><xsl:value-of select="order/payment-method/@brand"/> # <xsl:value-of select="order/payment-method/@account-number"/>
</xsl:template>

<!-- DELIVERY ADDRESS TEMPLATE -->
<xsl:template match="address">
<xsl:value-of select="@address1"/><xsl:text> </xsl:text><xsl:if test="@apartment"><xsl:value-of select="@apartment"/></xsl:if><xsl:text> 
</xsl:text>
<xsl:if test="@address2"><xsl:value-of select="@address2"/></xsl:if><xsl:text>
</xsl:text>
<xsl:value-of select="@city"/>,<xsl:text> </xsl:text><xsl:value-of select="@state"/><xsl:text> </xsl:text><xsl:value-of select="@zip-code"/><xsl:text>
</xsl:text>
Phone: <xsl:value-of select="phone/@phone"/> <xsl:if test="phone/@ext != ''"> Ext. <xsl:value-of select="phone/@ext"/></xsl:if><xsl:text>
</xsl:text>
</xsl:template>

</xsl:stylesheet>
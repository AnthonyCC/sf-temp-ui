<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:output method="html"/>
	
	<!-- DELIVERY DATE FORMATTER -->
	<xsl:template name="format-delivery-date">
		<xsl:param name="dateTime"/>

		<xsl:variable name="format" select="'%A, %B %d, %Y'"/>

		<xsl:call-template name="dt:format-date-time">
			<xsl:with-param name="year" select="substring($dateTime,1,4)"/>
			<xsl:with-param name="month" select="number(substring($dateTime,6,2))"/>
			<xsl:with-param name="day" select="substring($dateTime,9,2)"/>
			<xsl:with-param name="format" select="$format"/>
		</xsl:call-template>
			
	</xsl:template>
	
	<!-- DELIVERY START TIME FORMATTER -->
	<xsl:template name="format-delivery-start">
		<xsl:param name="dateTime" />
		
		<xsl:variable name="format" select="'%I:%M %p'"/>
		
		<xsl:call-template name="dt:format-date-time">
			<xsl:with-param name='hour' select='substring($dateTime, 12, 2)'/>
			<xsl:with-param name='minute' select='substring($dateTime, 15, 2)'/>
			<xsl:with-param name="format" select="$format"/>
		</xsl:call-template>
		
	</xsl:template>

	<!-- DELIVERY END TIME FORMATTER -->
	<xsl:template name="format-delivery-end">
		<xsl:param name="dateTime" />
		
		<xsl:variable name="format" select="'%I:%M %p'"/>
		
		<xsl:call-template name="dt:format-date-time">
			<xsl:with-param name='hour' select='substring($dateTime, 12, 2)'/>
			<xsl:with-param name='minute' select='substring($dateTime, 15, 2)'/>
			<xsl:with-param name="format" select="$format"/>
		</xsl:call-template>
		
	</xsl:template>

	<!-- DELIVERY ADDRESS FORMATTER -->
	<!--<xsl:template name="format-delivery-address">
		<xsl:param name="deliveryAddress" />
		<xsl:value-of select="$deliveryAddress/address1"/><xsl:text> </xsl:text><xsl:if test="string-length('order/delivery-info/address/@apartment') &gt; 0"><xsl:value-of select="order/delivery-info/address/@apartment"/></xsl:if><br/>
		<xsl:if test="string-length('order/delivery-info/address/@address2') &gt; 0"><xsl:value-of select="order/delivery-info/address/@address2"/><br/></xsl:if>
		<xsl:value-of select="order/delivery-info/address/@city"/>,<xsl:text> </xsl:text><xsl:value-of select="order/delivery-info/address/@state"/><xsl:text> </xsl:text><xsl:value-of select="order/delivery-info/address/@zip-code"/><br/>
	</xsl:template>-->

	<!-- PAYMENT FORMATTER -->
	<xsl:template name="format-payment-method">
		<xsl:param name="paymentMethod" />
		<xsl:value-of select="$paymentMethod/name"/><br/>
		<xsl:value-of select="$paymentMethod/cardType"/> # <xsl:value-of select="$paymentMethod/maskedAccountNumber"/><br/>
	</xsl:template>

	<!-- DELIVERY ADDRESS TEMPLATE -->
	<xsl:template name="format-delivery-address">
		<xsl:param name="dlvAddress" />
		
		<xsl:value-of select="$dlvAddress/address1"/><xsl:text> </xsl:text><xsl:if test="$dlvAddress/apartment"><xsl:value-of select="$dlvAddress/apartment"/></xsl:if><br/>
		<xsl:if test="$dlvAddress/address2 != ''"><xsl:value-of select="$dlvAddress/address2"/><br/></xsl:if>
		<xsl:value-of select="$dlvAddress/city"/>,<xsl:text> </xsl:text><xsl:value-of select="$dlvAddress/state"/><xsl:text> </xsl:text><xsl:value-of select="$dlvAddress/zipCode"/><br/>
		<br/>
		<xsl:if test="$dlvAddress/phone">
		Phone: <xsl:value-of select="$dlvAddress/phone"/><br/>
		</xsl:if>

	
	
	</xsl:template>

	<!-- BILLING ADDRESS TEMPLATE -->
	<xsl:template name="format-billing-address">
		<xsl:param name="billingAddress" />
		
		<xsl:value-of select="$billingAddress/address1"/><xsl:text> </xsl:text><xsl:if test="$billingAddress/apartment"><xsl:value-of select="$billingAddress/apartment"/></xsl:if><br/>
		<xsl:if test="$billingAddress/address2 != ''"><xsl:value-of select="$billingAddress/address2"/><br/></xsl:if>
		<xsl:value-of select="$billingAddress/city"/>,<xsl:text> </xsl:text><xsl:value-of select="$billingAddress/state"/><xsl:text> </xsl:text><xsl:value-of select="$billingAddress/zipCode"/><br/>
		<br/>
	
	</xsl:template>

</xsl:stylesheet>
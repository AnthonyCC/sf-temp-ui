<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_common_functions_v1.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
Hello <xsl:value-of select="customer/firstName"/>,

Please use the following link to change your password (just paste it into your browser's address window and hit "Enter" on your keyboard):

<xsl:value-of select="passwordLink"/>

To protect your account, this link will expire at <xsl:call-template name="format-expiration-time"><xsl:with-param name="dateTime" select="expirationTime"/></xsl:call-template>, so be sure to use it before then.

If you have any questions, call us at 1-866-283-7374.

Cheers,

FreshDirect
Customer Service Group

<xsl:call-template name="x_footer_v1"/>
</xsl:template>

<xsl:template name="format-expiration-time">
	<xsl:param name="dateTime" />
	<xsl:param name="format" select="'%I:%M %p, %B %d'"/>
	<xsl:call-template name="dt:format-date-time">
		<xsl:with-param name="year" select="substring($dateTime, 1,4)"/>
		<xsl:with-param name="month" select="substring($dateTime, 6,2)"/>
		<xsl:with-param name="day" select="substring($dateTime, 9,2)"/>
		<xsl:with-param name="hour" select="substring($dateTime, 12, 2)"/>
		<xsl:with-param name="minute" select="substring($dateTime, 15, 2)"/>
		<xsl:with-param name="format" select="$format"/>
	</xsl:call-template>
</xsl:template>

</xsl:stylesheet>
<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_order_info_v1.xsl'/>
	<xsl:include href='x_common_functions_v1.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="erp-email">
Hello <xsl:value-of select="@first-name"/>,

Thanks for rescheduling your order. We apologize for any inconvenience this has caused you. [print next sentence only if credit issued for order]To make up for your inconvenience, we are giving you $[total credit amount] off your next order.[/end of optional sentence]

Your order (#<xsl:value-of select="@order-id"/>) will be delivered between <xsl:call-template name="format-delivery-start"/> and <xsl:call-template name="format-delivery-end"/> on [date] to the address below:
<xsl:call-template name="format-delivery-address"/>

If you have last-minute updates or additions to your order, go to your account and make the changes before <xsl:call-template name="format-cutoff-date-time"/>.

View your account:
http://www.freshdirect.com/your_account/manage_account.jsp

We hope you enjoy everything in your order. Please come back soon for more of the freshest, highest-quality food at the best prices in New York.

Happy eating!

FreshDirect
Customer Service Group

Order Information: 
<xsl:call-template name="x_order_info_v1"/>

<xsl:call-template name="x_footer_v1"/>
</xsl:template>

<!-- DELIVERY CUTOFF DATE/TIME FORMATTER -->
<xsl:template name="format-cutoff-date-time">
	<xsl:param name="year"><xsl:value-of select="substring(order/delivery-info/@delivery-cutoff-time,1,4)"/></xsl:param>
	<xsl:param name="month"><xsl:value-of select="substring(order/delivery-info/@delivery-cutoff-time,6,2)"/></xsl:param>
	<xsl:param name="day"><xsl:value-of select="substring(order/delivery-info/@delivery-cutoff-time,9,2)"/></xsl:param>
	<xsl:param name="hour"><xsl:value-of select="substring(order/delivery-info/@delivery-cutoff-time, 12, 2)"/></xsl:param>
	<xsl:param name="minute"><xsl:value-of select="substring(order/delivery-info/@delivery-cutoff-time, 15, 2)"/></xsl:param>
	<xsl:param name="format" select="'%I:%M %p, %B %d'"/>
	<xsl:call-template name="dt:format-date-time">
		<xsl:with-param name="year" select="$year"/>
		<xsl:with-param name="month" select="$month"/>
		<xsl:with-param name="day" select="$day"/>
		<xsl:with-param name="hour" select="$hour"/>
		<xsl:with-param name="minute" select="$minute"/>
		<xsl:with-param name="format" select="$format"/>
	</xsl:call-template>
</xsl:template>

</xsl:stylesheet>
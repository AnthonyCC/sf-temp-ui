<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_common_functions_v1.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="erp-email">
Hello <xsl:value-of select="@first-name"/>,

Your order (#<xsl:value-of select="@order-id"/>) scheduled for between <xsl:value-of select="order/delivery-info/@formatted-delivery-start-time"/> and <xsl:value-of select="order/delivery-info/@formatted-delivery-end-time"/> on <xsl:call-template name="format-delivery-date"/> has been canceled. The order will not be delivered and you won't be charged. All of the items from this order will be stored as "Your Last Order" in "Reorder."

For more details please contact us at 1-866-283-7374. We're here <xsl:for-each select="contactHours/contactHours"><xsl:value-of select="daysDisplay"/><xsl:value-of select="hoursDisplay"/>;</xsl:for-each>

Sincerely,

FreshDirect
Customer Service Group

<xsl:call-template name="x_footer_v1"/>
</xsl:template>

</xsl:stylesheet>
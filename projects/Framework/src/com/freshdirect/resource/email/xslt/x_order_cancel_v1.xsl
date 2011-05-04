<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_common_functions_v2.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
Hello <xsl:value-of select="customer/firstName"/>,

We received your cancellation request for your order (#<xsl:value-of select="orderNumber"/>) scheduled for between <xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="deliveryStartTime" /></xsl:call-template> and <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="deliveryEndTime"/></xsl:call-template> on <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="deliveryStartTime"/></xsl:call-template>. The order will not be delivered and you won't be charged. All of the items from this order will be stored as "Your Last Order" in Quickshop.

Reorder with Quickshop:
http://www.freshdirect.com/quickshop/index.jsp

We're always trying to improve and your feedback is important to us. If you have a moment, please e-mail us at <xsl:choose><xsl:when test="customer/chefsTable = 'true'">chefstable@freshdirect.com</xsl:when><xsl:otherwise>service@freshdirect.com</xsl:otherwise></xsl:choose> and tell us your reasons for canceling. (You can also call us at <xsl:choose><xsl:when test="customer/chefsTable = 'true'">1-866-511-1240</xsl:when><xsl:otherwise>1-212-796-8002</xsl:otherwise></xsl:choose>.)

We hope you come back soon.

Best Regards,

FreshDirect
Customer Service Group

<xsl:call-template name="x_footer_v1"/>
</xsl:template>
</xsl:stylesheet>
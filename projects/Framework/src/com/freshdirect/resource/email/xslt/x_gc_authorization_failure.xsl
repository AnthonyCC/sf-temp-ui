<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_common_functions_v2.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text" indent="no"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">

Dear <xsl:value-of select="customer/firstName"/>,

We are unable to process your order (#<xsl:value-of select="orderNumber"/>), using the Gift Card you have chosen. The card may be on hold, or there may be a network error. So that we can process your order as soon as possible, please call customer service at <xsl:choose><xsl:when test="customer/chefsTable = 'true'">1-866-511-1240</xsl:when><xsl:otherwise>1-212-796-8002</xsl:otherwise></xsl:choose>.

To be sure that your order is delivered, please make any changes before  <xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="cutoffTime"/></xsl:call-template> on <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="cutoffTime"/></xsl:call-template>. If you need further assistance, we're here: Monday-Thursday 6:30 AM to 12 AM; Friday 6:30 AM to 11 PM; Saturday 7:30 AM to 8 PM; Sunday 7:30 AM to 12 AM.


	Sincerely,

	FreshDirect Customer Service
	www.freshdirect.com

		<xsl:call-template name="x_footer_v1"/>
</xsl:template>

</xsl:stylesheet>

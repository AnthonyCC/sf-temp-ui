<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt="http://xsltsl.org/date-time" version="1.0">
	<xsl:import href="stdlib.xsl"/>
	<xsl:include href="x_header.xsl"/>
	<xsl:include href="x_common_functions_v2.xsl"/>
	<xsl:include href="x_optout_footer.xsl"/>
	<xsl:include href="x_footer_v2.xsl"/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear <xsl:value-of select="customer/firstName"/>,

We were unable to process your standing order when we tried to schedule a delivery between <xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="standingOrder/startTime"/></xsl:call-template> and <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="standingOrder/endTime"/></xsl:call-template> on <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="standingOrder/nextDeliveryDate" /></xsl:call-template>.

<xsl:value-of select="standingOrder/errorHeader"/><xsl:text> </xsl:text><xsl:value-of select="standingOrder/errorDetail"/>

http://www.freshdirect.com/quickshop/so_details.jsp?ccListId=<xsl:value-of select="standingOrder/customerListId"/>


Sincerely

Your Customer Service Team at FreshDirect

<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>


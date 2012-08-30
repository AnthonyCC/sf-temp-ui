<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_common_functions_v2.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
Hello <xsl:value-of select="customer/firstName"/>,

This is FreshDirect's automated notification system.

This e-mail was sent to you because our delivery team would like to speak with you about your order.

Please call us at <xsl:value-of select="customer/customerServiceContact"/> at your earliest convenience

Press option 1.

Your order number is (#<xsl:value-of select="orderNumber"/>) 

							
Thank you,
Your FreshDirect Customer Service Team

<xsl:call-template name="x_footer_v1"/>
</xsl:template>
</xsl:stylesheet>
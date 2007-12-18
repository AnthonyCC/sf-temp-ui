<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear <xsl:value-of select="customer/firstName"/>,
	
Your order for today is on its way and we are making our best efforts to deliver all orders as quickly as possible through the snow and heavy traffic. Many deliveries will arrive late today and we apologize for any inconvenience this may cause. We appreciate your patience and understanding.

Sincerely,

The FreshDirect Customer Service Team
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

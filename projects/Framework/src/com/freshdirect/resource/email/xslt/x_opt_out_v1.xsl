<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_common_functions_v1.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="erp-email">
Hello <xsl:value-of select="@first-name"/>,

We received your request to remove your e-mail address from our subscription list.

From now on, the only e-mails we'll send are those necessary to process your orders. If you experience additional problems, please call us at 1-866-283-7374.

We hope you come back soon.

Cheers,

FreshDirect
Customer Service Group

<xsl:call-template name="x_footer_v1"/>
</xsl:template>

</xsl:stylesheet>
<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_common_functions_v1.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
Hi <xsl:value-of select="customer/firstName"/>,

		<p>We just wanted to confirm that you’ve successfully made a change to the email address associated with your account. If you didn't make this change or have any related questions, please call us at 1-866-283-7374.</p>

		<p>Happy Shopping,<br/>
		<br/>
		FreshDirect<br/>
		Customer Service Group</p>

<xsl:call-template name="x_footer_v1"/>
</xsl:template>

</xsl:stylesheet>
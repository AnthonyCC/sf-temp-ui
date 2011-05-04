<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear <xsl:value-of select="customer/firstName"/>,
			
To accommodate your holiday schedule, this week only FreshDirect will deliver to CMP on Thursday, July 3 earlier than usual, between 11:30 am and 2 pm. FreshDirect customer service will be closed on Friday evening, July 4 and all day Saturday, July 5. Our regular schedule will resume next week. If you have any questions, please contact us at 1-866-511-1240.

We wish everyone a safe and happy July 4 weekend!

Sincerely,

Your FreshDirect Customer Service Team
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>
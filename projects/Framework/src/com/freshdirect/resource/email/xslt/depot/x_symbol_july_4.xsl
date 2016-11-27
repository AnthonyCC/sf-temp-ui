<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear <xsl:value-of select="customer/firstName"/>,
			
This week only, FreshDirect will deliver to Symbol on Wednesday, July 2 instead of Thursday, July 3. We will deliver to the Bohemia office between 3:30 pm and 4:30 pm and to Holtsville between 5 pm and 7 pm. FreshDirect customer service will be closed on Friday evening, July 4 and all day Saturday, July 5. Our regular schedule for all locations will resume next week. If you have any questions, please contact us at 1-866-511-1240.

We wish everyone a safe and happy July 4 weekend!

Sincerely,

Your FreshDirect Customer Service Team
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>


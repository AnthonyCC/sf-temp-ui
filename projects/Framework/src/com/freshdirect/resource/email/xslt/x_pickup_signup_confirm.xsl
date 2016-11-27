<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Hello <xsl:value-of select="customer/firstName"/>,

Thank you for signing up with FreshDirect.

We've hired New York's finest food experts, built the perfect environment for food, and found the shortest distance from farms, dairies, and fisheries to your table. We have all the irresistibly fresh foods you could want, plus popular grocery brands, all for 10% to 35% less than you're paying now. 

So come back soon, place your first order ( http://www.freshdirect.com ), and get ready for the freshest, highest-quality food at the best prices in New York. 

Happy eating! 
 
FreshDirect 

<xsl:call-template name="x_footer_v1"/>
</xsl:template>
</xsl:stylesheet>


<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>

Dear <xsl:value-of select="customer/firstName"/>, 

Now that you've tried FreshDirect, we thought you might want to learn about where your food comes from.

We've recently added photographs of our fully refrigerated, 300,000 square foot facility to our web site. Learn more about FreshDirect, one of the cleanest, most technologically advanced food facilities in the country.

- 12 climate zones, ranging from 62 to -40 degrees, providing optimal conditions for fresh food
- We follow USDA guidelines and the HACCP food safety system
- In-house bakery &amp; state-of-the-art USDA kitchen
- A full-time microbiologist and onsite laboratory

Take a photographic tour and learn more about our food facility:
http://www.freshdirect.com/about/plant_tour/index.jsp?catId=about_tour

Happy Holidays,

Joe Fedele, CEO and Co-Founder

FreshDirect
It's All About the Food

<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>
<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear <xsl:value-of select="customer/firstName"/>, 

We are grateful for your recent understanding regarding cancelled orders last Saturday, April 3. 

As a token of our appreciation, we hope you'll enjoy a complimentary Three-Cheese Lasagna with Bolognese Sauce (one of our chef's favorite dishes). The lasagna will be automatically put in your next order, if you order within the next three weeks. 

We thank you for being a FreshDirect customer and appreciate your continued patronage. If you have any questions, please call us toll-free at 1-866-279-2451. 

Thank you, 
Jason Ackerman 
Co-Founder, FreshDirect 
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

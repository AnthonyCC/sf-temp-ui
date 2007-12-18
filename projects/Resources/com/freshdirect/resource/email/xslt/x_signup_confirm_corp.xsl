<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
Hello <xsl:value-of select="customer/firstName"/>,

Thank you for signing up with FreshDirect Corporate Services.

We've hired New York's finest food experts, built the perfect environment for food, and found the shortest distance from farms, dairies, and fisheries to your business. We have all the irresistibly fresh foods you could want, including delicious catering platters, all for 10% to 35% less than you're paying now. And we bring it to your door.

We also have a full selection of grocery and household brands, so you can do all your shopping in one stop. So come back soon to www.freshdirect.com, place your first order, and get ready for the freshest, highest-quality food at the best prices in New York.

Happy eating!  

FreshDirect 
Corporate Services Group 
<xsl:call-template name="x_footer_v1"/>

</xsl:template>

</xsl:stylesheet>
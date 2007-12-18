<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
KOSHER SEAFOOD NOW AVAILABLE! 

Succulent salmon fillets, thick rosy tuna steaks, festive striped bass - we have the freshest kosher seafood in New York! Whether you are planning to grill a whole fish or make a hearty stew, seafood makes a quick, healthy meal. All our fish is custom-prepared and Certified Kosher by the Orthodox Union ("OU") and Breuer Kahal Adas Jeshurun ("KAJ"). 

Shop for Kosher Seafood: 
http://www.freshdirect.com/category.jsp?catId=kosher_seafood&amp;trk=ksea01
<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

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
			
Summer is over. Fortunately, we're here to make getting back into the grind a little easier!

Just a friendly reminder to place your next FreshDirect order and get better food at a better price.

http://www.freshdirect.com?trk=remind01 to start shopping from our homepage.

To see what's new and back in stock, http://www.freshdirect.com/newproducts.jsp?trk=remind01

You can also use Quickshop ( http://www.freshdirect.com/quickshop/index.jsp?trk=remind01 ) to shop directly from your last order.

Happy eating,

Joe Fedele
FreshDirect
It's all about the food.
<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear FreshDirect Customer,
Your comments don't go unheard! Our records indicate that you've purchased our cr&#233;me caramel in the past (http://www.freshdirect.com/product.jsp?productId=pudfl_flan_new&amp;catId=pudfl), and we wanted to let you know that we have redesigned the packaging to better protect it en route to you. We now put each cr&#233;me caramel in its own sealed package and then put the two in a secured container. We anticipate that this new packaging will prevent any shipping damage.  

Enjoy,
FreshDirect
<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear <xsl:value-of select="customer/firstName"/>, 

Our records indicate that a while ago you registered with us at www.FreshDirect.com, but have not yet placed an order. We pride ourselves on offering better food at better prices and we want you to experience what other New Yorkers are raving about.

Since we launched, we've extended some remarkable promotional offers that would enable you to sample some of the finest fresh food around. Currently there is a limited time promotional offer of $50 worth of free fresh food when you order $100 or more.* We wanted to let you know that this offer will expire on Tuesday, April 13th at 11:59 pm and we don't want you to miss out on it! 

Please feel free to contact us if you have any questions at service@freshdirect.com. We look forward to feeding you in the near future. 

The FreshDirect Team 

* See website for details
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>
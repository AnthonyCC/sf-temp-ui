<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>

A few weeks ago you signed up for more information about FreshDirect.
			
We still want to welcome you to FreshDirect with $50 worth of free fresh food.*

We've already treated hundreds of your colleagues to our sweet fruit, crisp vegetables, custom cut meats, fine cheeses, and freshly roasted coffees. Once you get a taste of us, we're confident that you'll want seconds.

If you're having problems with our Web site, we're standing by to help you. Call toll free 1-866-511-1240.
			
Log on to www.freshdirect.com/ca  Access Code: CA321

Start shopping for your favorite things, and get ready to have $50 worth of free fresh food.

You can also contact us via email at caservice@freshdirect.com
     
Enjoy,

FreshDirect
It's all about the food.

*See website for details.

<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

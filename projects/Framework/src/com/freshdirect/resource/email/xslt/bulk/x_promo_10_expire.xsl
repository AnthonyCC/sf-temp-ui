<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear <xsl:value-of select="customer/firstName"/>,

We hope you enjoyed FreshDirect's high quality food and incredible savings.  Now we're giving you 10 good reasons to come back for seconds.

Just place your next web order ($40 minimum), and give us your thoughts. We'll give you $10 more free fresh food.* But in order to receive your $10 of free fresh food, you must place your next order by September 1, 2003.  After this date the offer expires and you'll miss your chance to sample the freshest produce, meat, seafood, coffee and so much more for free.

Log onto www.freshdirect.com and receive your $10 of free fresh food today!

Happy eating,

Joe Fedele
CEO
FreshDirect


*Web orders and home delivery orders only. Offer expires September 1, 2003. One per household. Certain fresh food items may be excluded.  Delivery and billing address must match. Available in selected zones.
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

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
			
We hope your first FreshDirect experience was nothing short of excellent. Now we're giving you 10 good reasons to come back for seconds.
			
Just place your next web order ($40 minimum), and give us your thoughts. We'll give you $10 more free fresh food.* Plus, you have free delivery for two more orders.

Did somebody say Reorder? It’s easy to shop from your top items, past orders, and shopping lists to stay stocked on all your favorites.

GO AHEAD, START SHOPPING.

Log on to www.freshdirect.com/ca  Access Code: CA321

You can also contact us via email at caservice@freshdirect.com

See you soon,

FreshDirect
It's all about the food.

* Web orders only. Limited time offer. One per household. Certain perishables may be excluded.
<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>
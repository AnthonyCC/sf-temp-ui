<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear Symbol Customer,

Some time has passed since you first registered to experience the FreshDirect way of food shopping. Our mission from the onset has been to provide great fresh food at great prices delivered to you at work.

Each day FreshDirect introduces dozens of new products to our already extensive line of fresh food and groceries.  Check out our "new" or "back in season" items in all of our departments such as prepared foods and bakery, produce, meats, seafood, grocery, deli and dairy. 

You are important to us and we would like to know about your FreshDirect experience, your view and your opinion.

We invite you to speak with us next Wednesday, November 19th, when we visit Symbol.  FreshDirect representatives will be set up in the company cafeteria starting at 11:30am until 2:00pm. We will have some tasty items from our bakery.

Please do stop by. 

Thank you.
<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>


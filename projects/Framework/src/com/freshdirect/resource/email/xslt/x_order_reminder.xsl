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

Just a little reminder to place your next FreshDirect order.
			
We've stored all the details of your last order, so you can reorder in minutes.
Click here to view your last order in Quickshop!
http://www.freshdirect.com/quickshop/index.jsp?trk=remind1

Based on feedback from customers like you we've also recently added a whole line of organic and all-natural dairy products.
http://www.freshdirect.com/category.jsp?catId=dai_organic&amp;trk=remind1
We are also working around the clock to add beer and a full line of organic fruits and vegetables to our selection. 

Happy eating,

FreshDirect
It's all about the food.

<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>


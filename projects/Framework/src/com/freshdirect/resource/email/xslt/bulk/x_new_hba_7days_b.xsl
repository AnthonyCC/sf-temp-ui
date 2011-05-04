<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
HEALTH &amp; BEAUTY PRODUCTS!
FreshDirect is pleased to announce the opening of our newest department, Health &amp; Beauty.
Now you can order your favorite health and beauty products at great prices along with your food. We'll be adding many new items over the next few months, so check back often. 

Go to Health &amp; Beauty: http://www.freshdirect.com/department.jsp?deptId=hba&amp;trk=hba7_b 

.......................................... 

WE NOW DELIVER SEVEN DAYS A WEEK! 
That's right. Now you can place your order for delivery or pick-up for any day of the week (including Wednesday). 

<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>
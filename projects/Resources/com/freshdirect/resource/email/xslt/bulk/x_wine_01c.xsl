<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Best Cellars
WINE NOW AVAILABLE!
Just in time for the Holidays

Now you can order a selection of great wines for everyday through Best Cellars at FreshDirect and have your wine delivered to your door along with the rest of your food order.

Start shopping for wine: http://www.freshdirect.com/department.jsp?deptId=win&amp;trk=wine_01c

. . . . . . . . . . 

Don't forget to also check out
OUR HOLIDAY MENU

Celebrate the Holiday season in style and ease with our Chef's special menu, featuring delicious, traditional appetizers, entr&#xE9;es, and more.

Shop from our Holiday Menu: http://www.freshdirect.com/category.jsp?catId=hmr_xmas&amp;trk=wine_01c
<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

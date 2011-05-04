<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear Computer Associates Customer,

Winter has arrived! And FreshDirect is here to help keep you warm this season.  

Just a friendly reminder to place your next order with FreshDirect and receive better food at better prices...delivered to the trunk of your car.

Don't forget to checkout our new Wine section: 
http://www.freshdirect.com/department.jsp?deptId=win

Our Winter Picks: 
http://www.freshdirect.com/department.jsp?deptId=our_picks

And our Super Bowl Menu - designed so that you can spend less time in the kitchen and more time with your family and friends. It features delicious, traditional appetizers, heros and more! 
http://www.freshdirect.com/category.jsp?catId=hmr_sb   

Also, be on the look out for our special Valentine's Day Fondue Menu coming soon! 

As always, we'll see you on Fridays between 4:30 and 7:30pm. 

FreshDirect
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>
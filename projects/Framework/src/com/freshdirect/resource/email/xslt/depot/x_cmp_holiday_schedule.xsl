<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear CMP Corporate Depot Customer,

This year both Christmas and New Years fall on a Thursday. To accommodate business closings, we plan to make a change in delivery schedules for both weeks.

For the week of Christmas, we plan to deliver on Tuesday, December 23rd between the hours of 4:00pm and 7:00pm. We will deliver at the same hours on Tuesday, December 30th. We hope this meets all of your shopping needs.

From all of us here at Freshdirect, have a wonderful and safe holiday season.
<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

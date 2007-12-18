<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
~ KOSHER DEPARTMENT NOW OPEN! ~

Deliveries start on Monday, September 22, 2003.

You ask, we listen. We've prepared a full line of custom-cut Glatt Kosher meat (seafood coming soon) just in time for the holidays. We've partnered with Rubashkin's - home of Aaron's Best Glatt Kosher meat to bring you and your family the finest. Hand-cut just for your order, all of our Kosher meat is certified under the watchful eyes of OU and KAJ supervision and of course we have your favorite Kosher brands such as Manischewitz and Osem.

Visit our Kosher Department: http://www.freshdirect.com/department.jsp?deptId=kos&amp;trk=kosher
<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

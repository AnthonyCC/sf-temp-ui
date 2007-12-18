<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_common_functions_v1.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="erp-email">
Good things come to those who wait.

FreshDirect is now delivering to Roosevelt Island! Place your first Web order and get $50 worth of free fresh food* just for trying us.

Choose from over 3,000 irresistibly fresh items, plus a full selection of organic foods and popular grocery and household brands. All delivered to your door, exactly the way you want, with 100% satisfaction guaranteed.

To top it off, delivery is free for the first three orders, and our drivers won't accept tips.

You've waited long enough. Log on to www.FreshDirect.com today.

Enjoy,

Joe Fedele
CEO and Creator, FreshDirect
Co-founder of Fairway Uptown

<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

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
			
We hope you've had a great summer and are gearing up for a restful Labor Day. Please be advised that due to the extended Labor Day weekend, Freshdirect will be re-scheduling our normal delivery time slot. Instead of delivering on Friday, August 29th, between 4:30 and 7:30pm, we will deliver on Thursday, August 28th, between 4:30 and 7:30pm. We will resume our usual Friday delivery schedule the following Friday, September 5th.

We hope our revised schedule does not inconvenience you.  Have a wonderful Labor Day.

Happy Eating,

FreshDirect
<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>
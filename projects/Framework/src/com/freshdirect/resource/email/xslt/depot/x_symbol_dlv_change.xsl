<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear <xsl:value-of select="customer/firstName"/>,
			
Please be advised that we are making a change to the present FreshDirect Depot delivery schedule to Symbol Technologies. Because participation at the Bohemia office has been very low for some time now, Thursday, October 30th will be the last FreshDirect delivery to this location. 

Delivery to Holtsville will continue on a modified schedule. Starting Thursday, November 6th, the FreshDirect truck will be present between the hours of 5:30 p.m. and 7 p.m. 

This new delivery schedule will be posted on the FreshDirect Web site. You should also feel welcome to call Customer Service at 1-866-511-1240 with any questions that you may have.

Thank you for your participation.

Sincerely, 

FreshDirect Customer Service
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>


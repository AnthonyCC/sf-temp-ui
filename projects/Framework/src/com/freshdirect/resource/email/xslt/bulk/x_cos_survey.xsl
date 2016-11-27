<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
You recently expressed interest in the FreshDirect AT THE OFFICE program for office deliveries. We have asked a select group of respondents to fill out a short survey so that we can better understand the food and beverage needs of your business.

Please take a few moments to fill out our FreshDirect AT THE OFFICE survey: http://www.freshdirect.com/survey/cos.jsp. 

If you wish, you may also forward the link (http://www.freshdirect.com/survey/cos.jsp) to someone in your company or organization for completion. 

In either case, we greatly appreciate your time and interest. 
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

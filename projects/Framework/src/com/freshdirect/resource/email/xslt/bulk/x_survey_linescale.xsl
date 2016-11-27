<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
In an effort to improve our service and quality, we would like to hear what you think about FreshDirect. We're working with Linescale Research, a New York City research company, to conduct an anonymous survey that will help us better serve our customers. It will only take a few minutes to complete the survey, and your feedback will be greatly appreciated. Your answers are anonymous, all information is kept private, and you have our promise that we'll listen to what you have to say. Plus, when you complete the survey, you will automatically be entered into Linescale's $100 prize drawing.

To participate, just click on this link: http://linescale.com/freshdirect6001 and Linescale Research will let you know right away if the survey is still open.

Thank you for your help,
Joe Fedele
CEO and Creator, FreshDirect
Co-founder of Fairway Uptown
<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>


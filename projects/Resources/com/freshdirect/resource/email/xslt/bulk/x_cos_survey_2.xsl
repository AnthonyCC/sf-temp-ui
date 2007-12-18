<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear valued FreshDirect customer:

Thank you for expressing your interest in FreshDirect Corporate Services. Whether you're hosting an important client meeting, stocking your office pantry, or simply want a week's worth of delicious fruit, FreshDirect Corporate Services could be your convenient one-stop source. FreshDirect Corporate Services offers: 

- Streamlined and consolidated services that will eliminate the need for multiple vendors 
- Chef-prepared catering platters perfect for business meetings or events 
- Popular brands of snacks, beverages, and pantry-stocking items 
- Delicious restaurant-quality individual meals 
- Convenient one-hour morning and afternoon delivery windows 

We are now selecting companies to participate in the first phase of our program. To better understand your needs and the location of your company, we ask that you provide us with some additional information by filling out a quick three-question survey. 

http://www.freshdirect.com/survey/cos.jsp 
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

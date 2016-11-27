<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear Valued FreshDirect Customer: 

We noticed that you often purchase organic products from us at FreshDirect. While we do sell numerous organic items, we are in the process of developing an entire area dedicated to organic foods. This means many more options of fruits, vegetables, meats, dairy, snacks, frozen meals and much more. 

We value your opinion and would appreciate you taking a minute of your time to fill out our organic food survey by clicking on the link below. Your feedback will help in developing our organic food offerings.  

http://www.freshdirect.com/survey/organic.jsp 

Thank you in advance for your time and support. 

The FreshDirect Team 
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

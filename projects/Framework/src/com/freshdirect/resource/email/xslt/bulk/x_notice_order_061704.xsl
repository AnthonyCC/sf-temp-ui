<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear FreshDirect Customer, 

Thank you for your order. As always, we strive to provide you with the highest quality products, at great prices, delivered right to your door. 

Due to a gap in the picking season of cherries from one growing area to another, we may be unable to deliver the cherries that you ordered for tomorrow. We wanted to let you know, and give you the opportunity to replace the cherries with another fruit of the season. 

http://www.freshdirect.com/your_account/manage_account.jsp 

If you have any questions, please contact us via email at service@freshdirect.com or by phone at 1-866-283-7374. We're here Monday through Thursday from 8:00AM to 1:00AM, Friday from 8:00AM to 10:00PM and Saturday from 7:30AM to 10:00PM. Sunday we're here from 7:30AM to 1:00AM. 
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

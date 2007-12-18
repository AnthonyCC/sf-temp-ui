<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear <xsl:value-of select="customer/firstName"/>,

Just a friendly reminder that our Customer Service Team is always standing by to address any questions you may have about your FreshDirect order, the Web site, or any other topic of concern. Our toll free number is 1-866-511-1240. If you would prefer to send email, our address is cmpservice@freshdirect.com. 

We've also dedicated a FreshDirect depot service specialist, Angelo, who will be available Monday through Friday for any specific concerns. General inquiries may be addressed seven days a week by calling our Customer Service department. 

This summer we look forward to helping you get your food shopping out of the way during the week so that you can enjoy sunny weekends with friends and family instead of trekking to the store. Whether it's a party, picnic, or barbecue, we deliver the steaks, lobsters, cold cuts, salads and other fresh foods that will ensure a tasty time is had by all.

We hope you enjoy everything in your next order. Please come back soon!

Sincerely,

Your FreshDirect Customer Service Team
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear <xsl:value-of select="customer/firstName"/>,

Please note that FreshDirect delivery will now be available on Fridays only -- all Tuesday delivery slots have been suspended for the summer months. The time of delivery remains the same, 4:30 pm to 7:30 pm.

This summer we look forward to helping you get your food shopping out of the way during the week so that you can enjoy sunny weekends with friends and family instead of trekking to the store. Whether it's a party, picnic, or barbecue, we deliver the steaks, lobsters, cold cuts, salads and other fresh foods that will ensure a tasty time is had by all.

Our Customer Service Team is always standing by to address any questions you may have about your FreshDirect order, the Web site, or any other topic of concern. Our toll free number is 1-866-511-1240. If you would prefer to send email, our address is caservice@freshdirect.com. 

We've also dedicated a FreshDirect depot service specialist, Angelo, who will be available Monday through Friday for any specific concerns. General inquiries may be addressed seven days a week by calling our Customer Service department. 

We hope you enjoy everything in your next order. Please come back soon!

Sincerely,

Your FreshDirect Customer Service Team
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

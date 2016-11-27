<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear FreshDirect Customer,

Last night we had a number of late deliveries in your neighborhood and we at FreshDirect apologize for any inconveniences we may have caused you.

We strive to deliver each and every order on time every day. However, occasionally we are subject to unforeseen circumstances causing a delay in deliveries. Last night, we were faced with two problems, a delivery personnel shortage and the rainy weather. We take responsibility for the shortage of delivery personnel and apologize for not having your order to you on time. We are adding extra delivery personnel to your neighborhood to avoid similar situations.  

We appreciate your understanding and thank you for being a FreshDirect customer. 

If you have any other questions, please call us toll-free at 1-866 2UFRESH (1-866 283-7374). We're here Sunday-Friday 8am-1am and Saturdays 8am-9pm. Or, you may email us at service@freshdirect.com.

The FreshDirect Customer Service Team
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

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

Thank you for shopping with FreshDirect.

Due to a delay in our computer system we were unable to send a printed invoice along with your order today. Your order will be delivered on time and an email will be sent with an electronic invoice by the end of the day.

We apologize for any inconvenience this might have caused.
If you have any other questions, please call us toll-free at 1-866 2UFRESH (1-866 283-7374). We're here Sunday-Friday 8am-1am and on Saturdays from 8am-9pm. Or, you may email us at service@freshdirect.com 

Sincerely, 
FreshDirect Customer Service
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>
<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear Valued Customer,

Due to the Blackout of 2003, FreshDirect canceled all orders placed for delivery Friday August 15, 2003 and Saturday August 16, 2003. If you placed an order for either day, you will not be charged. If you would like delivery for Sunday or any future date, please place a new order online.

As a reminder, FreshDirect does not charge your credit card until your order is delivered to you.

Thank you for your patience, as we and the rest of NYC return to our normal schedules.

Our 800 phone number is currently not working and we can not receive phone calls. Please view our web site for updated information.


Sincerely,
FreshDirect Customer Service Group
www.freshdirect.com
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>
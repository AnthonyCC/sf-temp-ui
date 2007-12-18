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

All orders placed for delivery on Thursday August 14, 2003 that were not delivered due to the Blackout, will be delivered today Friday August 15 as close to the original delivery time slot as possible, if you are living in an area that has power.
Fortunately, we were able to keep all deliveries refrigerated during the Blackout. 

If for any reason you are not able to accept your order today, you will not be charged.
We will not be delivering on Saturday; however, if you would like delivery for Sunday or any future date, please place a new order online.

As a reminder, FreshDirect does not charge your credit card until your order is delivered to you. 
Thank you for your patience, as we and the rest of NYC return to our normal schedules.

Our 800 phone number is currently not working, please view our web site for updated information. 


Sincerely,

FreshDirect Customer Service
www.freshdirect.com
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

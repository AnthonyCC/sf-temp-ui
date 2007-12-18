<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
July 23, 2003

Dear North Shore - LIJ Health System Employee,

It is with great regret that FreshDirect announces the indefinite suspension of our fresh food delivery service to North Shore - LIJ.  As of Wednesday, July 23rd, 2003 all service is indefinitely discontinued and we will not be accepting new orders moving forward, with the exception of any orders already in our system for delivery Friday, July 25th, 2003.

The suspension of the service is due to low participation rates amongst North Shore - LIJ employees.  While our regular North Shore - LIJ customers rave about FreshDirect's fresh food, quality and convenience, we can no longer justify our high delivery costs to your facilities.

Currently, FreshDirect and North Shore - LIJ's Human Resources Department are assessing the overall program.  To help us determine how we could have serviced you better we would greatly appreciate any feedback you would like to send to us.  It is your invaluable feedback that will allow us to hopefully service you again in the future.  Please send all of your comments, positive or negative to us at: nslijservice@freshdirect.com.

For those of you who cannot live without FreshDirect or who still want to try us, we have opened a FreshDirect pick up window at our facility.  Anyone in the Tri-State area can now place an order online for pickup at FreshDirect located just outside the Midtown Tunnel in Long Island City, Queens.  For all details please go to: http://www.freshdirect.com/help/delivery_lic_pickup.jsp.

We thoroughly enjoyed working with North Shore - LIJ and hope to do so again in the future.

Sincerely,

FreshDirect
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear Valued Customer:

Thank you for your recent order with FreshDirect.  

We observed you placed an order scheduled for delivery on Monday, March 24th.  Earlier, we experienced some minor technical problems, which resulted in the unavailability of certain time periods for Thursday, March 20th and Friday, March 21st.  This has been resolved.  If you would like to attempt to reschedule delivery for an earlier date you may follow the instructions below.  Please note this is subject to time slot availability.

Log into your account from the main screen, select the link, "Your Account", then select "Your Orders".  On the next screen, you will see an orange button that reads, "Change this order".  You will be asked to click this button a second time on the next screen.  Proceed through the checkout process and select the delivery time slot desired.

We appreciate your patience and understanding.  If you have any other questions, please call us toll free at 1-866-2UFRESH (1-866-283-7374).  We're here Sunday-Friday, 9am to midnight and Saturdays from 9am to 9pm.  Or, you may email us 24 hours a day at service@freshdirect.com

Sincerely,
FreshDirect
Customer Service Group
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

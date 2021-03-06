<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_common_functions_v2.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text" indent="no"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">

Dear <xsl:value-of select="customer/firstName"/>,

Unfortunately, the credit or debit card number you entered for your upcoming FreshDirect order (#<xsl:value-of select="orderNumber"/>),
appears to be incorrect, and we are not able to process your payment. Without a valid payment method, we will not be able to deliver your order.

To avoid order cancellation, go to www.freshdirect.com now to correct your payment information or select a different payment method.

Please modify your pending order at www.freshdirect.com. At Step 3 of the checkout process, you will have a few options:
Correct the current credit or debit card number
Select a different payment method that is already on your account
Add a new payment method

Update this order at http://www.freshdirect.com/your_account/order_details.jsp?orderId=<xsl:value-of select="orderNumber"/>

Be sure to re-submit your order with a new payment type before <xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="cutoffTime"/></xsl:call-template> on <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="cutoffTime"/></xsl:call-template>, to avoid cancellation.

We accept Visa, MasterCard, American Express, Discover and online check payments.

If you need assistance with this process, please call our Customer Service Team now at  <xsl:value-of select="customer/customerServiceContact"/>, <xsl:for-each select="contactHours/contactHours"><xsl:value-of select="daysDisplay"/><xsl:value-of select="hoursDisplay"/>;</xsl:for-each>


Sincerely,

Your FreshDirect Customer Service Team
www.freshdirect.com

<xsl:call-template name="x_footer_v1"/>
</xsl:template>

</xsl:stylesheet>

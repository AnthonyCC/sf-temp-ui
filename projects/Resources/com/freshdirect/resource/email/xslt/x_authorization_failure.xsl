<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_common_functions_v2.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text" indent="no"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">

Dear <xsl:value-of select="customer/firstName"/>,

We are unable to process your order (#<xsl:value-of select="orderNumber"/>), scheduled for delivery between <xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="deliveryStartTime"/></xsl:call-template> and <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="deliveryEndTime"/></xsl:call-template> on <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="deliveryStartTime"/></xsl:call-template> using the payment method you have chosen. Credit card authorization can fail for a number of reasons -- often the cause is an incorrect expiration date. So that we can process your order as soon as possible, to make changes to the information you submitted or to enter a different credit card, please go to http://www.freshdirect.com/your_account/order_details.jsp?orderId=<xsl:value-of select="orderNumber"/>.

To be sure that your order  is delivered, please make any changes before  <xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="cutoffTime"/></xsl:call-template> on <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="cutoffTime"/></xsl:call-template>.  If you would like to speak with a Customer Service Representative please call us toll-free at 1-866-283-7374.  We're here Sunday-Friday 8am-1am and Saturdays from 8am-9pm.

	Sincerely,

	FreshDirect Customer Service
	www.freshdirect.com

		<xsl:call-template name="x_footer_v1"/>
</xsl:template>

</xsl:stylesheet>

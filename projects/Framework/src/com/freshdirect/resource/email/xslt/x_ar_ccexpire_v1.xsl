<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt="http://xsltsl.org/date-time" version="1.0">
	<xsl:import href="stdlib.xsl"/>
	<xsl:include href="x_header.xsl"/>
	<xsl:include href="x_common_functions_v2.xsl"/>
	<xsl:include href="x_optout_footer.xsl"/>
	<xsl:include href="x_footer_v2.xsl"/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear <xsl:value-of select="customer/firstName"/>,

Unfortunately, we were unable to process your auto-renewal of FreshDirect DeliveryPass because the credit card in your account has expired.

To ensure that you will continue receiving the fee-free deliveries you receive with FreshDirect DeliveryPass membership, please follow the steps below:

1) Go to Your Account at FreshDirect to log in.

2) Click into "Payment Options" to update your expiration date or add a new payment type.

3) Place an order with a new DeliveryPass membership in your shopping cart.

If you have questions or need assistance, please contact our Customer Service Team via email: customerservice@freshdirect.com

Sincerely,

Your Customer Service Team at FreshDirect

<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>


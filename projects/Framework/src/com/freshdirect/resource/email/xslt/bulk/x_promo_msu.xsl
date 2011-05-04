<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear <xsl:value-of select="customer/firstName"/>, 

Thank you for your participation in the Michigan State University survey about FreshDirect. Knowing what our customers think is tremendously important to us and we appreciate the time you took to complete the survey. 

To receive a free pizza with your next order, just enter the following promotional code on the shopping cart page the next time you check out:*

Promo Code: PIZZA4

Once again, thank you for your feedback. 

Sincerely,
FreshDirect

* Pizzas will be given to all participants who enter a valid ID and complete the survey. Approximate retail value of pizza is $5.99. Survey participants must be registered customers. One pizza per household. When you've completed the survey you'll receive a promotion code via email that you can enter on the Shopping Cart page the next time you order from FreshDirect. The pizza will be delivered with the rest of your food. Offer good for limited time only. Promotion expires on August 1. Terms of FreshDirect Customer Agreement apply

<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

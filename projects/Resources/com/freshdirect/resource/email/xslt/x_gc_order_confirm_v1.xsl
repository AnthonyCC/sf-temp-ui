<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_gc_order_info_v1.xsl'/>
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_common_functions_v2.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:include href='x_gc_recipient_info_v1.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">

<xsl:call-template name="x_header"/>			
<!-- FIRST ORDER TEXT -->
Dear <xsl:value-of select="customer/firstName"/>,

Thank you for your Gift Card order (#<xsl:value-of select="order/erpSalesId"/>). Your Recipient List and payment details are below. The final total is $<xsl:value-of select='format-number(order/subTotal, "###,##0.00", "USD")'/>.

PLEASE NOTE: It may take up to two hours or more to activate your Gift Cards. Until the cards are active your recipients will not be able to use their gift card numbers. We will send confirmation to you via email once your newly purchased Gift Cards are active. Thank you for your patience.


View this order (and get printable versions of your gifts) in Your Account: 
http://www.freshdirect.com/your_account/manage_account.jsp

We hope your recipients enjoy their gifts.

FreshDirect
<xsl:choose>
	<xsl:when test="order/deliveryType != 'C'">Customer Service Group</xsl:when>
	<xsl:otherwise>Corporate Services Group</xsl:otherwise></xsl:choose>
<xsl:call-template name="x_gc_order_info_v1"/>

NOTE: If this email does not print out clearly, please go to https://www.freshdirect.com/your_account/order_history.jsp for a printer-friendly version of your order details.
<xsl:call-template name="x_gc_recipient_info_v1"/>

<xsl:call-template name="x_footer_v1"/>
			<!-- ~~~~~~~~~~~~~~~ END CONTENT ~~~~~~~~~~~~~~~ -->

</xsl:template>

</xsl:stylesheet>

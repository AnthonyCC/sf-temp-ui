<?xml version="1.0"?>
<!DOCTYPE xsl:stylesheet [
    <!ENTITY nbsp "&#160;">
    <!ENTITY dot  "&#183;">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:output method="text"/>
	<xsl:template name="x_gc_bulk_recipient_info_v1">

DETAILS FOR ORDER NUMBER #<xsl:value-of select="order/erpSalesId"/>

View this order (and get printable versions of your gifts) in Your Account: 
http://www.freshdirect.com/your_account/order_history.jsp

Card Type     Amount    Qty     SubTotal
<xsl:for-each select="order/giftCardBulkRecipients/recipents/recipents">
<xsl:value-of select="templateId"/>  $<xsl:value-of select='format-number(amount, "###,##0.00", "USD")'/>    <xsl:value-of select="quantity"/>  $<xsl:value-of select='format-number(subTotal, "###,##0.00", "USD")'/>
</xsl:for-each>

Total: $<xsl:value-of select='format-number(order/total, "###,##0.00", "USD")'/>*
</xsl:template>

</xsl:stylesheet>
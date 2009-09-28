<?xml version="1.0"?>
<!DOCTYPE xsl:stylesheet [
    <!ENTITY nbsp "&#160;">
    <!ENTITY dot  "&#183;">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:output method="text"/>
	<xsl:template name="x_gc_recipient_info_v1">
RECIPIENT LIST FOR ORDER NUMBER #<xsl:value-of select="order/erpSalesId"/>

View this order (and get printable versions of your gifts) in Your Account.

	<xsl:for-each select="order/giftCardRecipients/recipents/recipents">
<!--	<xsl:value-of select="position()"/>.-->
	<xsl:value-of select="recipientName"/>..........................................................................$<xsl:value-of select='format-number(amount, "###,##0.00", "USD")'/>  
	</xsl:for-each>
Total:  $<xsl:value-of select='format-number(order/total, "###,##0.00", "USD")'/>*
</xsl:template>

</xsl:stylesheet>

<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear <xsl:value-of select="customer/firstName"/>,

As you requested, we're sending this note as a friendly reminder to place your next FreshDirect order.

http://www.freshdirect.com?trk=remind to start shopping from our homepage.

To see what's new and back in stock, http://www.freshdirect.com/newproducts.jsp?trk=remind

You can also use http://www.freshdirect.com/quickshop/index.jsp?trk=remind to shop directly from http://www.freshdirect.com/quickshop/shop_from_order.jsp?orderId=<xsl:value-of select="customer/lastOrderId"/>&amp;trk=remind your last order.

Regards,

FreshDirect
Customer Service Group

If you'd like to change or cancel this reminder service, http://www.freshdirect.com/your_account/reminder_service.jsp?trk=remind

<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>
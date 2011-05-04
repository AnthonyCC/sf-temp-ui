<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
We hope you enjoyed our $50.00 free food trial offer and that your FreshDirect experience was nothing short of excellent.

Please note that our $25.00 free fresh food offer* on the second order will become $10.00 effective on Wednesday, March 19, 2003.

If you would like to take advantage of our $25.00 promotional offer, you must place your second order on www.FreshDirect.com before Wednesday, March 19. If you place your order on or after March 19 you will receive the $10.00 free fresh food promotion.

We look forward to becoming your source for the best food at the best prices.

Sincerely,
Joe Fedele
CEO and Creator
FreshDirect
It's all about the food.

P.S. We've made reordering easy. We automatically store your past orders, details and all, so you can reorder in minutes. Just log on to www.FreshDirect.com and use our Quickshop feature, select your previous order, choose the items you want and add them to your cart in a click. Yup, it's that easy.


*See website for details.
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

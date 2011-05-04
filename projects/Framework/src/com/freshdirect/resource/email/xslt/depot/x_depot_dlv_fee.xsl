<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear <xsl:value-of select="customer/firstName"/>, 
			
Hope you had a great summer.  If you are receiving this email, you are one of the many corporate depot customers that have enjoyed the free $50 fresh food offer from FreshDirect. In addition to the free fresh food promotion, we had also postponed initiating our delivery fee. The time has now come for us to activate the delivery fee. Our standard delivery fee is $3.95. However, we are discounting your delivery fee to $2.99. The delivery fee will be effective on all orders placed after September 7, 2003. 

Also, just as a reminder, we've made reordering easy. We automatically store your past orders, details and all, so you can reorder in minutes. Just log on to www.FreshDirect.com and use our Quickshop feature, select your previous order, choose the items you want and add them to your cart in a click. Yup, it's that easy.

Happy eating, 

FreshDirect
<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>
<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_common_functions_v2.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">


Dear <xsl:value-of select="customer/firstName"/>,

There was an error processing your recent Gift Card purchase (Order #<xsl:value-of select="order/erpSalesId"/>). This order has been placed on hold. For more information regarding this action, please email FreshDirect Customer Service at <a href="mailto:service@freshdirect.com">service@freshdirect.com</a> or call 1-866-511-1240.

For details of your Gift Card purchases, please sign in and visit Your Account.

Sincerly,
FreshDirect Customer Service

<xsl:call-template name="x_footer_v1"/>
</xsl:template>
</xsl:stylesheet>
<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_order_info_v1.xsl'/>
	<xsl:include href='x_common_functions_v2.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
Hello <xsl:value-of select="customer/firstName"/>,

We've updated your order information. Please look over the details below.

If you'd like to make further changes:
http://www.freshdirect.com/your_account/manage_account.jsp

Enjoy,

FreshDirect
Customer Service Group

<xsl:call-template name="x_order_info_v1"/>

<xsl:call-template name="x_footer_v1"/>

</xsl:template>

</xsl:stylesheet>
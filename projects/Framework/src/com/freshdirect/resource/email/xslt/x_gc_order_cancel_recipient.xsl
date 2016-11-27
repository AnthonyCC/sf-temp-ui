<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_common_functions_v2.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">


<xsl:call-template name="x_header"/>

Dear <xsl:value-of select="gcDlvInfo/recepientModel/recipientName"/>,

There was an error with the Gift Card you recently used (Certificate #<xsl:value-of select="gcDlvInfo/certificationNumber"/>) and the corresponding order has been placed on hold. Please contact the Gift Card purchaser for more information.


We apologize for any inconvenience this may cause you.

Sincerly,
FreshDirect Customer Service
<xsl:call-template name="x_footer_v1"/>
</xsl:template>
</xsl:stylesheet>
<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_common_functions_v2.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text" indent="no"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">

Dear <xsl:value-of select="recipientName"/>,

We have successfully transfered the balance of your FreshDirect Gift Card to a new card.  Your old Gift Card redemption code will no longer work.  We will email your new Gift Card as soon as it finishes processing. Please be patient: this process may take up to two or more hours.

Thank you for shopping with FreshDirect.


Sincerely,
FreshDirect Customer Service
www.freshdirect.com

<xsl:call-template name="x_footer_v1"/>
</xsl:template>
</xsl:stylesheet>

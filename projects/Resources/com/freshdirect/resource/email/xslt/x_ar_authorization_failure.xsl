<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_common_functions_v2.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text" indent="no"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">

Dear <xsl:value-of select="customer/firstName"/>,

We are unable to renew your FreshDirect Unlimited DeliveryPass membership because we could not authorize the charge against your most recently-used credit card. Unfortunately, direct payment from checking cannot be used for membership renewals..

At your earliest convenience, please call our Customer Service team at <xsl:choose><xsl:when test="customer/chefsTable = 'true'">1-866-511-1240</xsl:when><xsl:otherwise>1-212-796-8002</xsl:otherwise></xsl:choose>. A representative will help you update your account and renew your Unlimited DeliveryPass membership. 

We're here to assist you Monday-Thursday 6:30 AM to 12 AM; Friday 6:30 AM to 11 PM; Saturday 7:30 Am to 8 PM; Sunday 7:30 AM - 12 AM.


	Sincerely,

	FreshDirect Customer Service
	www.freshdirect.com

		<xsl:call-template name="x_footer_v1"/>
</xsl:template>

</xsl:stylesheet>

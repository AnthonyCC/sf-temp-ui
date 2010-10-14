<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_common_functions_v2.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text" indent="no"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">

Dear <xsl:value-of select="customer/firstName"/>,

We were unable to process the e-Check payment for your order (#<xsl:value-of select="orderNumber"/>), delivered on <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="deliveryStartTime"/></xsl:call-template>.
Please contact customer service at <xsl:choose><xsl:when test="customer/chefsTable = 'true'">1-866-511-1240</xsl:when><xsl:otherwise>1-212-796-8002</xsl:otherwise></xsl:choose>,within the next 24 hours so that we may help you resolve the payment discrepancy.
 Often times, these issues are caused by an input error. Pursuant to your agreement with us, should we not hear from you within 24 hours, we will attempt to settle payment using the other payments methods listed within your account.
Thank you.
 
Sincerely,

FreshDirect Customer Service
www.freshdirect.com

		<xsl:call-template name="x_footer_v1"/>
</xsl:template>

</xsl:stylesheet>

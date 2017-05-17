<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_invoice_info_fdx.xsl'/>
	<xsl:include href='h_header_fdx.xsl'/>
	<xsl:include href='h_common_functions_v2.xsl'/>
	<xsl:include href='h_footer_fdx.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<html>
<head>
	<title>Order up! We're Coming At Ya</title>
	<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
</head>
	<body bgcolor="#CCCCCC">
		<div style="background-color: #ffffff; border: 1px solid #bbbbbb; margin: 5px; padding: 20px;">
			<div style="margin: 0 0 15px 0;"><xsl:call-template name="h_header_fdx" /></div>
			
			<div style="color: #732484; font-size: 36px; font-weight: bold; margin: 15px 0;">Your food is on the way.</div>
			<div style="margin: 15px 0; font-size: 16px;">
				(Is there any better phrase in the English language?) Expect our Kickers to arrive between <xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template> - <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/endTime" /></xsl:call-template>.<br />
				<br />
				Your final total is <xsl:choose><xsl:when test="order/invoicedTotal &lt;= order/total">$<xsl:value-of select='format-number(order/invoicedTotal, "###,##0.00", "USD")'/></xsl:when><xsl:otherwise>$<xsl:value-of select='format-number(order/invoicedTotal, "###,##0.00", "USD")'/></xsl:otherwise></xsl:choose>. Hope everything is absolutely Instagram-worthy. <span style="font-weight: bold;">#FoodKick</span>
			</div>
			<!-- <div class="email-footer-block" style="height: 30px; margin: 10px 0;"><a class="email-body-button" href="#">ADD MORE STUFF</a></div> -->
		</div>

		<xsl:call-template name="h_invoice_info_fdx"/>

		<xsl:call-template name="h_footer_fdx"/>
	</body>
</html>
</xsl:template>

</xsl:stylesheet>
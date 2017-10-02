<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_header_fdx.xsl'/>
	<xsl:include href='h_common_functions_v2.xsl'/>
	<xsl:include href='h_footer_fdx.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<html lang="en-US" xml:lang="en-US">
<head>
	<title>We Were Unable to Process Your Payment</title>
	<base href="http://www.freshdirect.com/" />
	<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"></link>
</head>
<body bgcolor="#CCCCCC">
	
	<div style="background-color: #ffffff; border: 1px solid #bbbbbb; margin: 5px; padding: 20px;">
		<div style="margin: 0 0 15px 0;"><xsl:call-template name="h_header_fdx" /></div>
		
		<div style="color: #732484; font-size: 36px; font-weight: bold; margin: 15px 0;">Hi <xsl:value-of select="customer/firstName"/>,</div>
		<div style="margin: 15px 0; font-size: 16px;">
			<span style="font-weight: bold;">So the bad news first:</span> we weren't able to process the e-check payment for your order <xsl:value-of select="orderNumber"/> on <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="deliveryStartTime"/></xsl:call-template>. The good news: it's usually just an input error so <span style="font-weight: bold;">give us a call</span> and we'll figure it out.
			<br /><br />
			If we don't hear from you in 24 hours, we'll attempt to settle the payment with one of your other account payment methods.
			<br /><br />
			Your FoodKick Sidekicks<br />
			<xsl:value-of select="//customer/customerServiceContact"/>
		</div>
		<!-- <div class="email-footer-block" style="height: 30px; margin: 10px 0;"><a class="email-body-button" href="#">ADD MORE STUFF</a></div> -->
	</div>
	
	<xsl:call-template name="h_footer_fdx"/>
	
</body>
</html>
</xsl:template>

</xsl:stylesheet>
<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_header_fdx.xsl'/>
	<xsl:include href='h_common_functions_v2.xsl'/>
	<xsl:include href='h_footer_fdx.xsl'/>
	<xsl:include href='h_order_info_fdx.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<html lang="en-US" xml:lang="en-US">
<head>
	<title>We Got Your Changes</title>
	<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
</head>
<body bgcolor="#CCCCCC">
	
	<div style="background-color: #ffffff; border: 1px solid #bbbbbb; margin: 5px; padding: 20px;">
		<div style="margin: 0 0 15px 0;"><xsl:call-template name="h_header_fdx" /></div>
		
		<div style="color: #732484; font-size: 36px; font-weight: bold; margin: 15px 0;">Hey <xsl:value-of select="customer/firstName"/>,</div>
		<div style="margin: 15px 0; font-size: 16px;">
			Your order was already great and you made it better! You're pretty much winning at life.
		</div>
		<!-- <div class="email-footer-block" style="height: 30px; margin: 10px 0;"><a class="email-body-button" href="#">MORE CHANGES?</a></div> -->
	</div>
	
	<xsl:call-template name="h_order_info_fdx"/>
	
	<xsl:call-template name="h_footer_fdx"/>
</body>
</html>
</xsl:template>
</xsl:stylesheet>
<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_order_info_fdx.xsl'/>
	<xsl:include href='h_header_fdx.xsl'/>
	<xsl:include href='h_common_functions_v2.xsl'/>
	<xsl:include href='h_footer_fdx.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<html>
<head>
	<title>We Got Your Order. Get Excited!</title>
	<base href="http://www.freshdirect.com/"/>
	<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
</head>
<body bgcolor="#CCCCCC">

	<div style="background-color: #ffffff; border: 1px solid #bbbbbb; margin: 5px; padding: 20px;">
		<div style="margin: 0 0 15px 0;"><xsl:call-template name="h_header_fdx" /></div>
		
		<div style="color: #732484; font-size: 36px; font-weight: bold; margin: 15px 0;">Hey you,</div>
		<div style="margin: 15px 0; font-size: 16px;"><span style="font-weight: bold;">Your order looks ahhhmazing.</span> Check out the deets below.</div>
		<!-- <div style="text-align: center; padding: 3px; height: 30px; margin: 10px 0;"><a style="height: 100%; width: 100%; display: inline-block; padding: 0; margin: 0; font-size: 14px; color: #732484; font-weight: bold; line-height: 28px; text-decoration: none;" href="#">ADD MORE STUFF</a></div> -->
	</div>

	<xsl:call-template name="h_order_info_fdx"/>

	<xsl:call-template name="h_footer_fdx"/>

</body>
</html>
</xsl:template>

</xsl:stylesheet>

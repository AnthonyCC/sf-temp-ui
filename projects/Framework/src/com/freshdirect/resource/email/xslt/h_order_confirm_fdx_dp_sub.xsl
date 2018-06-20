<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_order_info_fdx_dp.xsl'/>
	<xsl:include href='h_header_fdx.xsl'/>
	<xsl:include href='h_common_functions_v2.xsl'/>
	<xsl:include href='h_footer_fdx.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<html lang="en-US" xml:lang="en-US">
<head>
	<title>No. More. Delivery. Fees. Welcome to DeliveryPass®!</title>
	<base href="http://www.freshdirect.com/"/>
	<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
</head>
<body bgcolor="#CCCCCC">

	<div style="background-color: #ffffff; border: 1px solid #bbbbbb; margin: 5px; padding: 20px;">
		<div style="margin: 0 0 15px 0;"><xsl:call-template name="h_header_fdx" /></div>
		
		<div style="color: #732484; font-size: 36px; font-weight: bold; margin: 15px 0;"><b>Hey <xsl:value-of select="customer/firstName"/></b>,</div>
		<div style="margin: 15px 0; font-size: 16px;"> Heads up: <span style="font-weight: bold;">Your FoodKick DeliveryPass® subscription is active! </span> </div>
		<div style="margin: 15px 0; font-size: 16px;"> Now you can ‘Kick it as many times as you want with no delivery fees. Even if you order more than once a day. ‘Cause life is busy + you never know when you’ll need meal inspo, crave snacks, or run out of wine**. Get it all on demand. </div>
		<div style="margin: 15px 0; font-size: 16px;"> Keep an eye out for special offers— another perk of your FoodKick DeliveryPass®.</div>
		<div style="margin: 15px 0; font-size: 16px;"> Happy ‘Kicking! </div>
		
		<div style="margin: 15px 0; font-size: 16px;"> Sincerely,</div>
		<div style="margin: 15px 0; font-size: 16px;"> Your  <a href="http://www.foodkick.com/">FoodKick</a>  SideKicks </div>
		
		<!-- <div style="text-align: center; padding: 3px; height: 30px; margin: 10px 0;"><a style="height: 100%; width: 100%; display: inline-block; padding: 0; margin: 0; font-size: 14px; color: #732484; font-weight: bold; line-height: 28px; text-decoration: none;" href="#">ADD MORE STUFF</a></div> -->
	</div>
<div style="margin: 15px 0; font-size: 16px;">
<b>Manage your FoodKick DeliveryPass®:  <a href="http://www.foodkick.com/account/deliveryPass/">  go to the DeliveryPass® page</a>  in Your Account.</b>
<br/>
<b><a href="http://www.foodkick.com/account/deliveryPass/">DeliveryPass®: Terms + Conditions </a>.</b>
<br/>
</div>
	<xsl:call-template name="h_order_info_fdx_dp"/>

	<xsl:call-template name="h_footer_fdx"/>

</body>
</html>
</xsl:template>

</xsl:stylesheet>

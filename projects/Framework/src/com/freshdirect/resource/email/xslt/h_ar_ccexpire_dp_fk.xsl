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
<HEAD>
	<TITLE>Subscription cancelled</TITLE>
	<base href="http://www.freshdirect.com/"/>
	<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
</HEAD>
<body bgcolor="#CCCCCC">


	<div style="background-color: #ffffff; border: 1px solid #bbbbbb; margin: 5px; padding: 20px;">
		<div style="margin: 0 0 15px 0;"><xsl:call-template name="h_header_fdx" /></div>

<P>Heads up: We couldn’t renew your FoodKick DeliveryPass® + we apologize for the inconvenience. The reason: We couldn’t authorize the amount on your most default payment method. </P>

<P>Don’t stress it. We’ll sort out your renewal ASAP. Just go to LiveChat on FoodKick.com anytime from 6:30am to 12am, or call 718.513.2785 from 6:30am to 12pm.</P>

<p>Thanks for ‘Kicking it with us + don’t hesitate to give us a shout if there’s anything else we can do to help.</p>
<P>Sincerely,
<br/>
Your  <a href="http://www.foodkick.com/">FoodKick</a> SideKicks
</P>

<P><xsl:call-template name="h_footer_fdx"/></P>
</div>
</body>
</html>
</xsl:template>

</xsl:stylesheet>

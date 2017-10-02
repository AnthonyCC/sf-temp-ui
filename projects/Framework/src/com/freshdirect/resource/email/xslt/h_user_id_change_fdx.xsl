<?xml version="1.0"?>
<!DOCTYPE xsl:stylesheet [
    <!ENTITY nbsp "&#160;">
    <!ENTITY dot  "&#183;">
    <!ENTITY mdash  "&#8212;">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_common_functions_v2.xsl'/>
	<xsl:include href='h_header_fdx.xsl'/>
	<xsl:include href='h_footer_fdx.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<html lang="en-US" xml:lang="en-US">
<head>
	<title>Email Address Updated</title>
	<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
</head>
	<body bgcolor="#eeeeee" text="#333333">
		<xsl:call-template name="mail_body" />
		
	</body>
</html>
</xsl:template>
	
<xsl:template name="mail_body">
	
	<div style="background-color: #ffffff; border: 1px solid #bbbbbb; margin: 5px; padding: 20px;">
		<div style="margin: 0 0 15px 0;"><xsl:call-template name="h_header_fdx" /></div>
		
		
		<div style="color: #732484; font-size: 36px; font-weight: bold; margin: 15px 0;">Hey <xsl:value-of select="customer/firstName"/>,</div>
		
		<div style="margin: 15px 0; font-size: 16px;">
			<span style="font-weight: bold;">#Success!</span> You just updated an email address for your account.<br />
			<br />
			If you didn't make this change or have any questions, open up 'Me' within the FoodKick app to check your settings. Tap 'Help -> Contact Us' to chat with one of our SideKicks so we can clear things up.<br />
			<br />
			Catch ya later,<br />
			Your FoodKick SideKicks
		</div>
	</div>

	<xsl:call-template name="h_footer_fdx"/>
</xsl:template>
	
</xsl:stylesheet>
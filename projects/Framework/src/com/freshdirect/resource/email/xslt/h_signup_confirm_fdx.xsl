<?xml version="1.0"?>
<!DOCTYPE xsl:stylesheet [
    <!ENTITY nbsp "&#160;">
    <!ENTITY dot  "&#183;">
    <!ENTITY mdash  "&#8212;">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='h_header_fdx.xsl'/>
	<xsl:include href='h_footer_fdx.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">

<html>
	<head>
		<title>Congrats! You're In!</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css" />
	</head>
<body bgcolor="#eeeeee" text="#333333">
	<xsl:call-template name="mail_body" />
</body>
</html>

</xsl:template>

<xsl:template name="mail_body">

	<div style="background-color: #ffffff; border: 1px solid #bbbbbb; margin: 5px; padding: 20px;">
		<div style="margin: 0 0 15px 0;"><xsl:call-template name="h_header_fdx" /></div>
		
		<div style="color: #732484; font-size: 36px; font-weight: bold; margin: 15px 0;">Welcome to FoodKick.</div>
		<div style="margin: 15px 0; font-size: 16px;">
			We're here to bring you #allthefood and #allthebooze &mdash; whenever and wherever you want it.<br />
			<br />
			<span style="font-weight: bold;">You ready to kick it?</span>
		</div>
		<!-- <div style="text-align: center; padding: 3px; height: 30px; margin: 10px 0;"><a style="height: 100%; width: 100%; display: inline-block; padding: 0; margin: 0; font-size: 14px; color: #732484; font-weight: bold; line-height: 28px; text-decoration: none;" href="#">START HERE</a></div> -->
	</div>
	
	<xsl:call-template name="h_footer_fdx"/>

</xsl:template>

</xsl:stylesheet>
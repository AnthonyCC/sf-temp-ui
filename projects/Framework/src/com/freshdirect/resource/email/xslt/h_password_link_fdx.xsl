<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_header_fdx.xsl'/>
	<xsl:include href='h_footer_fdx.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<html lang="en-US" xml:lang="en-US">
<head>
	<title>Forgetting something?</title>
	<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
</head>
<body bgcolor="#CCCCCC">
	
	<div style="background-color: #ffffff; border: 1px solid #bbbbbb; margin: 5px; padding: 20px;">
		<div style="margin: 0 0 15px 0;"><xsl:call-template name="h_header_fdx" /></div>
		
		<div style="color: #732484; font-size: 36px; font-weight: bold; margin: 15px 0;">Hey there,</div>
		<div style="margin: 15px 0; font-size: 16px;">
			<span style="font-weight: bold;">Remembering stuff is hard.</span> Please click on the following link to change your password:
			<br /><br />
			<a href="{passwordLink}"><xsl:value-of select="passwordLink"/></a>
			<br /><br />
			To protect your account, this link will expire at <b><xsl:call-template name="format-expiration-time"><xsl:with-param name="dateTime" select="expirationTime"/></xsl:call-template></b>, so be sure to use it before then.
			<br /><br />
			Happy Shopping,<br />
			Your FoodKick Sidekicks
		</div>
	</div>
	
	<xsl:call-template name="h_footer_fdx"/>
</body>
</html>
</xsl:template>

<xsl:template name="format-expiration-time">
	<xsl:param name="dateTime" />
	<xsl:param name="format" select="'%I:%M %p, %B %d'"/>
	<xsl:call-template name="dt:format-date-time">
		<xsl:with-param name="year" select="substring($dateTime, 1,4)"/>
		<xsl:with-param name="month" select="substring($dateTime, 6,2)"/>
		<xsl:with-param name="day" select="substring($dateTime, 9,2)"/>
		<xsl:with-param name="hour" select="substring($dateTime, 12, 2)"/>
		<xsl:with-param name="minute" select="substring($dateTime, 15, 2)"/>
		<xsl:with-param name="format" select="$format"/>
	</xsl:call-template>
</xsl:template>

</xsl:stylesheet>
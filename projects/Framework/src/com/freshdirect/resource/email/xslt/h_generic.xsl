<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href='h_header_v1.xsl'/>
<xsl:include href='h_footer_v1.xsl'/>
<xsl:output method="html"/>

<xsl:template match="fdemail">
<html>
<head>
	<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
</head>
<body bgcolor="#ffffff">

	<xsl:call-template name="h_header_v1"/>
	
	<xsl:apply-templates select="body" />
	
	<xsl:call-template name="h_footer_v1"/>

</body>
</html>
</xsl:template>

<xsl:template match="body//firstName">
	<b><xsl:value-of select="//customer/firstName"/></b>
</xsl:template>

<xsl:template match="body//lastName">
	<b><xsl:value-of select="//customer/lastName"/></b>
</xsl:template>

<xsl:template match="*">
	<xsl:copy>
		<xsl:for-each select="@*">
			<xsl:attribute name="{name()}"><xsl:value-of select="."/></xsl:attribute>
		</xsl:for-each>
		<xsl:apply-templates />
	</xsl:copy>
</xsl:template>

</xsl:stylesheet>
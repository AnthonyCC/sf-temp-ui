<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href='x_footer_v1.xsl'/>
<xsl:output method="text"/>

<xsl:template match="fdemail">
<xsl:apply-templates select="body" />
	
<xsl:call-template name="x_footer_v1"/>
</xsl:template>

<xsl:template match="body//firstName">
	<xsl:value-of select="//customer/firstName"/>
</xsl:template>

<xsl:template match="body//lastName">
	<xsl:value-of select="//customer/lastName"/>
</xsl:template>

<xsl:template match="b">*<xsl:apply-templates />*</xsl:template>

<xsl:template match="a"><xsl:apply-templates /> (<xsl:value-of select="@href"/>)</xsl:template>

<!--
<xsl:template match="*">
		<xsl:apply-templates />
</xsl:template>
-->
</xsl:stylesheet>
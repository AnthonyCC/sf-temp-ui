<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:output method="text"/>

<xsl:template match="fdemail">
	<xsl:value-of select="body"/>
</xsl:template>

</xsl:stylesheet>
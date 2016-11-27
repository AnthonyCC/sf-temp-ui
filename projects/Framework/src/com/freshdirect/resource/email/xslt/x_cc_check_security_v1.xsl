<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:output method="text"/>
	<xsl:template match="fdemail">

There are <xsl:value-of select='emailvo/ccCheckInfoSize' /> CC/EC decryptions detected for <xsl:value-of select="emailvo/forDate"/> as of <xsl:value-of select="emailvo/asOfTime"/>
<xsl:text>
</xsl:text>

<xsl:if test="emailvo/ccInfoSize >0">
Credit Card: <xsl:value-of select='emailvo/ccInfoSize' /> instances by
<xsl:text>
</xsl:text>

<xsl:for-each select="emailvo/ccInfo/ccInfo">

<xsl:value-of select="position()"/>.Agent :<xsl:value-of select='agentId' /> | Customer: <xsl:value-of select='customerFirstName' />  <xsl:value-of select='customerLastName' />
<xsl:text>
</xsl:text>



</xsl:for-each>
</xsl:if>

<xsl:if test="emailvo/ECheckInfoSize >0">
<xsl:text>
</xsl:text>

E-Check: <xsl:value-of select='emailvo/ECheckInfoSize' /> instances by
<xsl:text>
</xsl:text>

<xsl:for-each select="emailvo/ECheckInfo/ECheckInfo">

<xsl:value-of select="position()"/>.Agent :<xsl:value-of select='agentId' /> | Customer: <xsl:value-of select='customerFirstName' />  <xsl:value-of select='customerLastName' />
<xsl:text>
</xsl:text>

</xsl:for-each>
</xsl:if>

</xsl:template>

</xsl:stylesheet>

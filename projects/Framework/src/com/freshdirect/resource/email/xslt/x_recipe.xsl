<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href='x_footer_v1.xsl'/>
<xsl:output method="text"/>

<xsl:template match="fdemail">
Hello Recipes!
<xsl:call-template name="x_footer_v1"/>
</xsl:template>

</xsl:stylesheet>

<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='x_order_info_v1.xsl'/>
	<xsl:include href='x_common_functions_v1.xsl'/>
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="erp-email">
<xsl:call-template name="x_header"/>

Dear <xsl:value-of select="@first-name"/>, 

We've created a menu of restaurant-quality lunch and dinner classics at terrific prices. Chef David McInerney (formerly of Bouley and One If by Land, Two If by Sea) and his team make everything by hand using fresh, natural ingredients. All you have to do is heat and enjoy.

Whether you're tired of the neighborhood fare or want to supplement your own cooking, go to our Meals Department to check out our menu visit:

http://www.freshdirect.com/department.jsp?deptId=hmr

We're adding new items all the time, so keep checking back for our new line of seasonal dishes, appetizers, and soups. And remember to save a little room for a homemade dessert.

See you soon,

FreshDirect

<xsl:call-template name="x_footer_v1"/>
</xsl:template>
</xsl:stylesheet>
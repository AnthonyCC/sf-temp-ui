<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_footer_v1.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
Hello <xsl:value-of select="customer/firstName"/>,

Thank you for signing up with FreshDirect.

We've hired New York's finest food experts, built the perfect environment for food, and found the shortest distance from farms, dairies, and fisheries to your table. We have all the irresistibly fresh foods you could want, plus popular grocery brands, all for up to 25% less than supermarket prices. And we bring it to your door.
<!--
<xsl:if test="customer/eligibleForPromotion = 'true'">
We'd like to welcome you with $50 worth of our irresistibly fresh fruit and vegetables, meat and seafood cut to order, fine cheeses and deli goods, or any of our other perishables - FREE!*
</xsl:if>-->
We also have a full selection of grocery and household brands, so you can do all your shopping in one stop. So come back soon, place your first order ( http://www.freshdirect.com ), and get ready for the freshest, highest-quality food at the best prices in New York.

Happy eating! 

FreshDirect

<!--<xsl:if test="customer/eligibleForPromotion = 'true'">
* Web orders and home delivery orders only. Limited time offer. One per household. Delivery and billing address must match. Certain fresh food items may be excluded. Perishable food is fresh food. $15 limit per item. Available in selected zones. See Web site for full details.</xsl:if>
-->
<xsl:call-template name="x_footer_v1"/>

</xsl:template>

</xsl:stylesheet>
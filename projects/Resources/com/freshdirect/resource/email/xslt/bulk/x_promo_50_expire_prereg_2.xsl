<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
We wanted to remind you that as of September 1, 2003, the $50 of free fresh food* promotional offer expires.

Upon registering with FreshDirect you are qualified to receive $50 of free fresh food* and we don't want you to miss out on this fantastic offer. So in order to receive your $50 of free fresh food, you must place your first order by September 1, 2003. After this date the offer expires and you'll miss your chance to sample the freshest produce, meat, seafood, coffee and so much more for free.

Log onto www.freshdirect.com and receive your $50 of free fresh food today!

Happy eating,

Joe Fedele
CEO
FreshDirect


*Web orders and home delivery orders only. Offer expires September 1, 2003. One per household. Certain fresh food items may be excluded.  Delivery and billing address must match. Available in selected zones.
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>
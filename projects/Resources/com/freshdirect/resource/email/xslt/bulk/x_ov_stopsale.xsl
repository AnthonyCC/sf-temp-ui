<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Our records indicate you recently purchased Organic Valley Milk. Please note that Organic Valley Farms, has issued a Stop Sale Notice on a recent shipment of the following:

Organic Valley Whole Milk          -- Expiration date - 6/22/2003 &amp; 6/24/2003
Organic Valley Low fat 1% Milk   -- Expiration date - 6/22/2003 &amp; 6/29/2003
Organic Valley Skim Nonfat Milk -- Expiration date - 6/22/2003, 6/24/2003, &amp; 6/29/2003

We ask you to check your Organic Valley milk containers and if any of the expiration dates match the items listed above, please discard the milk. 

Organic Valley assures us that these products pose no health hazards and are withdrawn as a proactive measure due to performance characteristics of a reduced shelf life.

If you have any additional questions or concerns you may contact Organic Valley toll free at 1-888-444-6455 ext. 368. We have also attached the Stop Sale Notice we received from Organic Valley yesterday, May 20, 2003.

We are issuing your account a credit on the product purchased. 

Sincerely,

FreshDirect
Customer Service Group

Organic Valley Farms' Stop Sale Notice:
http://www.freshdirect.com/media_stat/images/template/email/organicvalley_stopsale.jpg

<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>


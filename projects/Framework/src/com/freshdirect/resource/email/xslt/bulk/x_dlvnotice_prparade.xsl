<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Due to street closures and heavy traffic around the National Puerto Rican Day Parade taking place on 5th Avenue, from 44th - 86th Street, we expect delays for deliveries around your area on Sunday, June 8.

We will make every effort to deliver your order on time but circumstances beyond our control may result in late deliveries or even cancellations for Sunday morning and afternoon.  We recommend modifying your order for Sunday evening to ensure timely delivery.

If you have any questions please contact us toll free 1-866-2UFRESH.

Sincerely,
FreshDirect Customer Service
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

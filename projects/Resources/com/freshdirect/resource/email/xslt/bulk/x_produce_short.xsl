<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear <xsl:value-of select="customer/firstName"/>,

At FreshDirect we have a simple philosophy: we will not send our customers a product we would not feed to our own families. Unfortunately, over the holiday weekend, we were shipped some produce items that did not meet these standards.

As such, these produce items were not sent in your order today. The rest of your order will be delivered as scheduled. We apologize for any inconveniences this may have caused. Please be assured that if an item is not sent, you will not be charged for it.
	
If you have any questions, please call us toll-free at 1-866-2UFRESH (1-866-283-7374).

Sincerely,

FreshDirect Customer Service
www.freshdirect.com

<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

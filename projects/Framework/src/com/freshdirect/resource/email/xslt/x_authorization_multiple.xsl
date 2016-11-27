<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear <xsl:value-of select="customer/firstName"/>,

A problem with our payment processor this weekend may have resulted in more than one authorization on your credit or debit card for your most recent order. Rest assured that you will never be charged more than once for any order you place with FreshDirect. We are closely coordinating with them to correct this situation as quickly as possible. If you have any questions, please call us toll-free at 1-866-283-7374. We apologize for any inconvenience this might have caused. 

Sincerely,

FreshDirect Customer Service
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>
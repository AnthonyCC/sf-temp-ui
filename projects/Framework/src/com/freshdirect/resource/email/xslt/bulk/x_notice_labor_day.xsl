<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear <xsl:value-of select="customer/firstName"/>, 

We hope you've had a great summer and are gearing up for a restful Labor Day.  We wanted to let you know about some changes in our delivery / pick-up schedule for the week of Labor Day. Our revised Labor Day delivery / pick-up schedule is as follows:

Day			Date			Status
Tuesday		8/26/03		Open
Wednesday		8/27/03		Open
Thursday		8/28/03		Open	
Friday		8/29/03		Closed
Saturday		8/30/03		Open
Sunday		8/31/03		Open
Monday		9/1/02		Closed
Tuesday		9/2/03		Open
Wednesday		9/3/03		Open
Thursday		9/4/04		Open

Our normal delivery schedule will resume on Thursday 9/4/03. We hope our revised schedule does not inconvenience you.  Have a wonderful Labor Day.


Happy eating,

FreshDirect

<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>
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

It has come to our attention that recent network problems for some Road Runner, Earthlink and AOL/Time Warner Broadband customers have prevented them from accessing our website. Please be assured that all of our systems are up and running and that all orders placed will be delivered as normal. These difficulties are caused by a network problem outside of our control - disrupting access not only to FreshDirect but to many other sites as well.

We hope this issue to be resolved shortly. In the meantime, if you have been unable to access www.freshdirect.com please call us toll free at 1-866-2UFRESH. We understand the inconvenience that this must cause during the busy holiday season and have Customer Service Representatives standing by who can place an order on your behalf or answer questions about your account. You may also use a dial-up account to access our site, as these problems only affect some broadband subscribers.

If you experience problems accessing our site at http://www.freshdirect.com, please let us know by responding to this email.


Sincerely,

Dean Furbush
Chief Operating Officer, FreshDirect

<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

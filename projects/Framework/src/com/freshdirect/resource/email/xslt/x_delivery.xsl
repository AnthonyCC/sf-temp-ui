<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>

To our FreshDirect Family,

Heightened security in and around New York City may result in delays for scheduled deliveries. Unusually heavy traffic, street closures, and random inspection of vehicles (particularly trucks) are resulting in late deliveries beyond our control.

Along with all New Yorkers, we hope this situation improves soon. In the meantime, rest assured that we are doing our best to deliver orders in a timely manner. We appreciate your patience and understanding.

Sincerely,
Joe Fedele
CEO and Co-founder, FreshDirect

PS. If you would like to reschedule your delivery you may modify your order online in Your Account.
http://www.freshdirect.com/your_account/manage_account.jsp 

<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>


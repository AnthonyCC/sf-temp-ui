<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:doc="http://xsltsl.org/xsl/documentation/1.0"
	xmlns:str="http://xsltsl.org/string"
	extension-element-prefixes="doc str">
	<xsl:output method="html"/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_optout_footer.xsl'/>
	<xsl:include href='h_footer_v2.xsl'/>
	<xsl:include href='string.xsl' />
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">

<html>
	<head>
        <BASE href="http://www.freshdirect.com" /> 
		<title>A friendly reminder from FreshDirect.</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
	</head>
<body bgcolor="#FFFFFF" text="#333333">
	<xsl:call-template name="mail_body" />
</body>
</html>

</xsl:template>

<xsl:template name="mail_body">

<table width="100%" cellpadding="0" cellspacing="0">
<tr>
<td><img src="/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
<td><xsl:call-template name="h_header_v1" />
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
			<td WIDTH="100%"><table WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0"><tr><td BGCOLOR="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0" alt="" /></td></tr></table></td>
		</tr>
	</table><br/>
	<table width="90%" cellpadding="0" cellspacing="0"><tr><td>
			<p><b>Dear <xsl:value-of select="customer/firstName"/>,</b></p>

			<p>As you requested, we're sending this note as a friendly reminder to place your next FreshDirect order.</p>
			
			<p><a href="http://www.freshdirect.com?trk=remind">Click here</a> to start shopping from our homepage.</p>
			
			<p>To see what's new and back in stock, <a href="http://www.freshdirect.com/newproducts.jsp?trk=remind">click here</a>.</p>
			
			<p>You can also use <a href="http://www.freshdirect.com/quickshop/index.jsp?trk=remind">Quickshop</a> to shop directly from <a href="http://www.freshdirect.com/quickshop/shop_from_order.jsp?orderId={customer/lastOrderId}&amp;trk=remind">your last order</a>.</p>
			
			<p><b>Regards</b>,</p>
			
			<p>FreshDirect<br/>
			Customer Service Group</p>
			
			<p>If you'd like to change or cancel this reminder service, <a href="http://www.freshdirect.com/your_account/reminder_service.jsp?trk=remind">click here</a>.</p>
			<br/><xsl:call-template name="h_footer_v2"/>
	</td></tr>
	</table>
</td>
<td><img src="/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
</tr>
</table>

</xsl:template>

</xsl:stylesheet>
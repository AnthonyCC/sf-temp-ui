<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:doc="http://xsltsl.org/xsl/documentation/1.0"
	xmlns:str="http://xsltsl.org/string"
	extension-element-prefixes="doc str">
	<xsl:output method="html"/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_footer_v2.xsl'/>
	<xsl:include href='string.xsl' />
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">

<html>
	<head>
		<title>Winter is here!</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
	</head>
<body bgcolor="#FFFFFF" text="#333333">
	<xsl:call-template name="mail_body" />
</body>
</html>

</xsl:template>

<xsl:template name="mail_body">

<table cellpadding="0" cellspacing="0">
<tr>
<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>

<td><xsl:call-template name="h_header_v1" />
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
			<td width="100%"><table width="100%" cellpadding="0" cellspacingG="0" border="0"><tr><td bgcolor="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1" border="0" alt="" /></td></tr></table></td>
		</tr>
	</table><br/>
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
		<td>		
			<p>Dear Computer Associates Customer,</p>
			
			<p>Winter has arrived! And FreshDirect is here to help keep you warm this season.</p>
			
			<p>Just a friendly reminder to place your next order with FreshDirect and receive better food at better prices...delivered to the trunk of your car.
			Don't forget to checkout our new <a href="http://www.freshdirect.com/department.jsp?deptId=win">Wine section</a>, our <a href="http://www.freshdirect.com/department.jsp?deptId=our_picks">Winter Picks</a> and our <a href="http://www.freshdirect.com/category.jsp?catId=hmr_sb">Super Bowl Menu</a> - designed so that you can spend less time in the kitchen and more time with your family and friends. It features delicious, traditional appetizers, heros and more! </p>
			
			<p>Also, be on the look out for our special Valentine's Day Fondue Menu coming soon!</p>
			
			<p>As always, we'll see you on Fridays between 4:30 and 7:30pm.</p>
			
			<p>FreshDirect</p>
			<p><xsl:call-template name="h_footer_v2"/></p>
		</td>
	</tr>
</table>
</td>

<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
</tr>
</table>

</xsl:template>

</xsl:stylesheet>
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
		<title>FreshDirect Customer Satisfaction</title>
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
			<td WIDTH="100%"><table WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0"><tr><td BGCOLOR="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1" border="0" alt="" /></td></tr></table></td>
		</tr>
	</table><br/>
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
		<td>
			<p>Dear Symbol Customer,</p>

			<p>Some time has passed since you first registered to experience the FreshDirect way of food shopping. Our mission from the onset has been to provide great fresh food at great prices delivered to you at work.</p>
			
			<p>Each day FreshDirect introduces dozens of new products to our already extensive line of fresh food and groceries. Check out our "new" or "back in season" items in all of our departments such as prepared foods and bakery, produce, meats, seafood, grocery, deli and dairy.</p> 
			
			<p>You are important to us and we would like to know about your FreshDirect experience, your view and your opinion.</p>
			
			<p>We invite you to speak with us next Wednesday, November 19th, when we visit Symbol. FreshDirect representatives will be set up in the company cafeteria starting at 11:30am until 2:00pm. We will have some tasty items from our bakery.</p>
			
			<p>Please do stop by.</p>
			
			<p>Thank you.</p>
		</td>
	</tr>
	<tr><td colspan="3"><br/><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/></td></tr>
</table>
</td>
<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
</tr>
</table>

</xsl:template>

</xsl:stylesheet>

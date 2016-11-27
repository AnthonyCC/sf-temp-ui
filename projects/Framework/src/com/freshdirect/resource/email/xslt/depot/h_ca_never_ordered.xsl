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
		<title>Your colleagues already love us.</title>
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
<!-- =============== START LEFT SPACER =============== -->
<td><img src="/images/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END LEFT SPACER ================= -->

<!-- ~~~~~~~~~~~~~~~ START CONTENT ~~~~~~~~~~~~~~~ -->
<td><xsl:call-template name="h_header_v1" />
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
			<td WIDTH="100%"><table WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0"><tr><td BGCOLOR="#CCCCCC"><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></td></tr></table></td>
		</tr>
	</table><br/>
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
		<td>
			<p>A few weeks ago you signed up for more information about <a href="http://www.freshdirect.com/ca">FreshDirect</a>.</p>
			
			<p>We still want to welcome you to <a href="http://www.freshdirect.com/ca">FreshDirect</a> with <b>$50 worth of free fresh food.</b>*</p>
			
            <p>We've already treated hundreds of your colleagues to our sweet fruit, crisp vegetables, custom cut meats, fine cheeses, and freshly roasted coffees. Once you get a taste of us, we're confident that you'll want seconds.</p>
			
            <p>If you're having problems with our Web site, we're standing by to help you. Call toll free 1-866-511-1240.</p>
			
            <p>Log on to <a href="http://www.freshdirect.com/ca">www.freshdirect.com/ca</a><xsl:text disable-output-escaping="yes">&amp;nbsp;&amp;nbsp;</xsl:text>Access Code: CA321</p>

        <p>Start shopping for your favorite things, and get ready to have $50 worth of free fresh food.</p>
			
        <p>You can also contact us via email at <a href="mailto:caservice@freshdirect.com">caservice@freshdirect.com</a></p>
            
            <p><b>Enjoy,</b></p>
			
            <p>FreshDirect
			<br/>
            It's all about the food.</p>
			
            <p>*See <a href="http://www.freshdirect.com/ca">website</a> for details.</p>
			
			<p><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/></p>
		</td>
	</tr>
</table>
</td>
<!-- ~~~~~~~~~~~~~~~ END CONTENT ~~~~~~~~~~~~~~~ -->

<!-- =============== BEGIN RIGHT SPACER =============== -->
<td><img src="/images/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END RIGHT SPACER ================= -->
</tr>
</table>

</xsl:template>

</xsl:stylesheet>
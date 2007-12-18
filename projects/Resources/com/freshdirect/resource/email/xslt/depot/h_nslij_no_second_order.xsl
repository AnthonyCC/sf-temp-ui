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
		<title>Get $10 off your second order!</title>
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
			<p>Dear <xsl:value-of select="customer/firstName"/>,</p>
			
			<p>We hope your first <a href="http://www.freshdirect.com/nslij">FreshDirect</a> experience was nothing short of excellent. Now we're giving you 10 good reasons to come back for seconds.</p>
			
            <p>Just place your next web order ($40 minimum), and give us your thoughts. We'll give you <b>$10 more free fresh food.</b>* Plus, you have free delivery for two more orders.</p>
			
            <p>Make sure to try out <a href="http://www.freshdirect.com/quickshop/index.jsp">Quickshop</a>. We've stored all the details of your last order, so you can reorder in minutes. And of course you'll find the same great selection of better food for up to 35% less than your local supermarket prices.</p>
			
            <p>GO AHEAD, START SHOPPING.</p>
            
			<p>Log on to <a href="http://www.freshdirect.com/nslij">www.freshdirect.com/nslij</a><xsl:text disable-output-escaping="yes">&amp;nbsp;&amp;nbsp;</xsl:text>Access Code: NSLIJ747</p>
			
			<p>You can also contact us via email at <a href="mailto:nslijservice@freshdirect.com">nslijservice@freshdirect.com</a></p>
			
            <p>See you soon,</p>
			
            <p>FreshDirect
			<br/>
            It's all about the food.</p>
			
            <p>* Web orders only. Limited time offer. One per household. Certain perishables may be excluded.</p>
			
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

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
		<title>As part of our family.</title>
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
			
			<p>Now that you've tried <a href="http://www.freshdirect.com/ca">FreshDirect</a>, I'd like to find out personally about your experience.</p>
			
            <p>I created <a href="http://www.freshdirect.com/ca">FreshDirect</a> to give New Yorker's the best food at the best possible price. I also believe in treating customers like my own family, so I want to know the good and the bad. Are you pleased with the food quality? Is our selection satisfactory? How were our prices?  Is there anything we can do better? <a href="mailto:joe@freshdirect.com">Drop me an e-mail</a> and let me know your honest thoughts.</p>
			
            <p>Direct feedback is the most valuable thing I can think of to ensure that your FreshDirect food shopping experience is the best you've ever had.</p>
			
            <p>Log on to <a href="http://www.freshdirect.com/ca">www.freshdirect.com/ca</a><xsl:text disable-output-escaping="yes">&amp;nbsp;&amp;nbsp;</xsl:text>Access Code: CA321</p>
			
			<p>You can also contact us via email at <a href="mailto:caservice@freshdirect.com">caservice@freshdirect.com</a></p>
			
			<p>I look forward to hearing from you.</p>
			
            <p>Joe Fedele
			<br/>
			Creator and CEO
			<br/>
			FreshDirect
			<br/>
			It's all about the food.</p>
		</td>
		<td><img src="/images/clear.gif" width="20" height="1" border="0" /></td>
		<td valign="top"><br/><br/>
		<img src="http://www.freshdirect.com/media_stat/images/template/about/joe_photo.jpg" width="136" height="250" border="0"/>
        <br/>
        <img src="http://www.freshdirect.com/media_stat/images/template/about/joe_title.gif" width="130" height="30" border="0"/></td>
	</tr>
	<tr><td colspan="3"><br/><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/></td></tr>
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
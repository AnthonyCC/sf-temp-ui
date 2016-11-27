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
		<title>How are we doing?</title>
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
<td><img src="/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END LEFT SPACER ================= -->

<!-- ~~~~~~~~~~~~~~~ START CONTENT ~~~~~~~~~~~~~~~ -->
<td><xsl:call-template name="h_header_v1" />
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
			<td WIDTH="100%"><table WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0"><tr><td BGCOLOR="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0" alt="" /></td></tr></table></td>
		</tr>
	</table><br/>
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
		<td>
			<p>Dear <xsl:value-of select="customer/firstName"/>,</p>
			
			<p>Now that you've tried <a href="http://www.freshdirect.com/symbol">FreshDirect</a>, I'd like to find out personally about your experience.</p>
			
            <p><a href="http://www.freshdirect.com/symbol">FreshDirect</a> was created to give New Yorker's the best food at the best possible price. We are committed to providing the highest service levels possible and as such we are interested in how you judge our performance and to provide us with feedback accordingly.  Are you pleased with the food quality? Is our selection satisfactory? How were our prices?  Is there anything we can do better? How is delivery? Are there items you would like us carry? <a href="mailto:symbolservice@freshdirect.com">Drop us an e-mail</a> at <a href="mailto:symbolservice@freshdirect.com">symbolservice@freshdirect.com</a> and let us know your honest thoughts.</p>
			
            <p>Direct feedback is the most valuable thing I can think of to ensure that your FreshDirect food shopping experience is the best you've ever had.</p>
			
            <p>Log onto <a href="http://www.freshdirect.com/symbol">www.freshdirect.com/symbol</a> Access Code: Sym123</p>
			
			<p>I look forward to hearing from you.</p>
			
            <p>Joe Fedele
			<br/>
			Creator and CEO
			<br/>
			<a href="http://www.freshdirect.com/symbol">FreshDirect</a>
			<br/>
			It's all about the food.</p>
		</td>
		<td><img src="/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
		<td valign="top"><br/><br/>
		<img src="/media_stat/images/template/about/joe_photo.jpg" width="136" height="250" border="0"/>
        <br/>
        <img src="/media_stat/images/template/about/joe_title.gif" width="130" height="30" border="0"/></td>
	</tr>
	<tr><td colspan="3"><br/><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/></td></tr>
</table>
</td>
<!-- ~~~~~~~~~~~~~~~ END CONTENT ~~~~~~~~~~~~~~~ -->

<!-- =============== BEGIN RIGHT SPACER =============== -->
<td><img src="/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END RIGHT SPACER ================= -->
</tr>
</table>

</xsl:template>

</xsl:stylesheet>

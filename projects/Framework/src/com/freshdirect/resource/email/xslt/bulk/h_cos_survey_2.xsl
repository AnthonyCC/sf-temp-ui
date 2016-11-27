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
		<title>FreshDirect for your Office</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
	</head>
<body bgcolor="#FFFFFF" text="#333333">
	<xsl:call-template name="mail_body" />
</body>
</html>

</xsl:template>

<xsl:template name="mail_body">

<xsl:comment>

</xsl:comment>

<table cellpadding="0" cellspacing="0">
<tr>
<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
<td><xsl:call-template name="h_header_v1" />
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
		<td width="100%"><table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td bgcolor="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1" border="0" alt="" /></td></tr></table></td>
		</tr>
	</table><br/>
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr valign="top">
			<td>
			<p>
			Dear valued FreshDirect customer:
			<br/><br/>
			Thank you for expressing your interest in FreshDirect Corporate Services. Whether you're hosting an important client meeting, stocking your office pantry, or simply want a week's worth of delicious fruit, FreshDirect Corporate Services could be your convenient one-stop source. FreshDirect Corporate Services offers:
			<ul>
			<li>Streamlined and consolidated services that will eliminate the need for multiple vendors</li>
			<li>Chef-prepared catering platters perfect for business meetings or events</li>
			<li>Popular brands of snacks, beverages, and pantry-stocking items</li> 
			<li>Delicious restaurant-quality individual meals</li>
			<li>Convenient one-hour morning and afternoon delivery windows</li>
			</ul>

			We are now selecting companies to participate in the first phase of our program. To better understand your needs and the location of your company, we ask that you provide us with some additional information by filling out a quick three-question <a href="http://www.freshdirect.com/survey/cos.jsp">survey</a>.
			<br/><br/>
			<a href="http://www.freshdirect.com/survey/cos.jsp"><b>http://www.freshdirect.com/survey/cos.jsp</b></a> 
			</p>
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
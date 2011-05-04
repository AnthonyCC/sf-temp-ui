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
		<title>Great news from FreshDirect!</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
		<style type="text/css">
		
		.fdNews    { font-size: 14px; color: #000000; font-family: Verdana, Arial, Helvetica, sans-serif; }
		a.fdNews:link {color: #336600; text-decoration: underline; }
		a.fdNews:visited {color: #336600; text-decoration: underline; }
		a.fdNews:active {color: #FF9933; text-decoration: underline; }
		a.fdNews:hover {color: #336600; text-decoration: underline; }

		.fdLink    { font-size: 16px; color: #000000; font-family: Verdana, Arial, Helvetica, sans-serif; }
		a.fdLink:link {color: #336600; text-decoration: underline; }
		a.fdLink:visited {color: #336600; text-decoration: underline; }
		a.fdLink:active {color: #FF9933; text-decoration: underline; }
		a.fdLink:hover {color: #336600; text-decoration: underline; }
		
		.fdFooter_s    { font-size: 11px; color: #333333; font-family: Verdana, Arial, sans-serif; }
		a.fdFooter_s:link {color: #336600; text-decoration: underline; }
		a.fdFooter_s:visited {color: #336600; text-decoration: underline; }
		a.fdFooter_s:active {color: #FF9933; text-decoration: underline; }
		a.fdFooter_s:hover {color: #336600; text-decoration: underline; }
		</style>
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
			<td width="100%"><table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td bgcolor="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1" border="0" alt="" /></td></tr></table></td>
		</tr>
	</table><br/>
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
		<td align="center"><a href="http://www.freshdirect.com/department.jsp?deptId=hba&amp;trk=hba7_a">
			<img src="http://www.freshdirect.com/media/images/promotions/hba7_a.gif" width="545" height="88" border="0" alt="Now Available - HEALTH &amp; BEAUTY PRODUCTS!"/><br/>
			<img src="http://www.freshdirect.com/media_stat/images/template/email/new/hba_products.jpg" width="485" height="125" vspace="6" border="0" alt="Health &amp; Beauty Products"/></a><br/>
			<span class="fdNews">FreshDirect is pleased to announce the opening of our newest department, Health &amp; Beauty.<br/>
			Now you can order your favorite health and beauty products at great prices along with your food.<br/>
			We'll be adding many new items over the next few months, so check back often.</span>
			<br/><br/>
			<a href="http://www.freshdirect.com/department.jsp?deptId=hba&amp;trk=hba7_a" class="fdLink"><font color="#336600"><b>Click here for Health &amp; Beauty.</b></font></a>
			<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="20" border="0"/><br/>
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td width="100%"><table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td bgcolor="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="687" height="1" border="0"/></td></tr></table></td>
				</tr>
			</table>
			<br/>
			<a href="http://www.freshdirect.com?trk=hba7_a"><img src="http://www.freshdirect.com/media_stat/images/template/email/new/7days.gif" width="553" height="53" border="0" vspace="6" alt="We now deliver seven days a week!"/></a><br/>
			<span class="fdNews">That's right. Now you can place your order for delivery or pick-up<br/>
			for any day of the week (including Wednesday).</span>
			<br/><br/><br/>
			<span class="bodyCopy">
			<xsl:call-template name="h_optout_footer"/>
			<xsl:call-template name="h_footer_v2"/>
			</span>
		</td>
	</tr>
</table>
</td>

<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
</tr>
</table>


</xsl:template>

</xsl:stylesheet>
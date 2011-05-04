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
		<title>Great News for Cr&#233;me Caramel Lovers</title>
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
			<td width="100%">
			<p>
			<b>Dear FreshDirect Customer,</b><br/>
			Your comments don't go unheard! Our records indicate that you've purchased our <a href="http://www.freshdirect.com/product.jsp?productId=pudfl_flan_new&amp;catId=pudfl"><b>cr&#233;me caramel</b></a> in the past, and we wanted to let you know that we have redesigned the packaging to better protect it en route to you. We now put each <a href="http://www.freshdirect.com/product.jsp?productId=pudfl_flan_new&amp;catId=pudfl"><b>cr&#233;me caramel</b></a> in its own sealed package and then put the two in a secured container. We anticipate that this new packaging will prevent any shipping damage.  
			<br/><br/>
			<b>Enjoy,</b><br/>
			FreshDirect
			</p>
	        </td>
			<td width="200"><a href="http://www.freshdirect.com/product.jsp?productId=pudfl_flan_new&amp;catId=pudfl"><img src="http://www.freshdirect.com/media_stat/images/template/email/new/pack_cremecaramel.jpg" width="200" height="200" border="0" alt="New Cr&#233;me Caramel packaging!"/></a><br/><br/></td>
	    </tr>
		<tr><td colspan="2"><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/></td></tr>
	</table>
</td>
<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
</tr>
</table>

</xsl:template>

</xsl:stylesheet>
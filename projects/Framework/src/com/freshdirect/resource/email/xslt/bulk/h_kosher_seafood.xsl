<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:doc="http://xsltsl.org/xsl/documentation/1.0"
	xmlns:str="http://xsltsl.org/string"
	extension-element-prefixes="doc str">
	<xsl:output method="html"/>
	<xsl:include href='h_optout_footer.xsl'/>
	<xsl:include href='h_footer_v2.xsl'/>
	<xsl:include href='string.xsl' />
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">

<html>
	<head>
        <BASE href="http://www.freshdirect.com" />
		<title>Kosher Fish Now Available!</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
		<style type="text/css">
		.promoProduct    { font-size: 18px; color: #000000; font-family: Verdana, Arial, Helvetica, sans-serif; }
		a.promoProduct:link {color: #336600; text-decoration: underline; }
		a.promoProduct:visited {color: #336600; text-decoration: underline; }
		a.promoProduct:active {color: #FF9933; text-decoration: underline; }
		a.promoProduct:hover {color: #336600; text-decoration: underline; }
		.fdFooter_s    { font-size: 11px; color: #333333; font-family: Verdana, Arial, sans-serif; }
		a.fdFooter_s:link {color: #336600; text-decoration: underline; }
		a.fdFooter_s:visited {color: #336600; text-decoration: underline; }
		a.fdFooter_s:active {color: #FF9933; text-decoration: underline; }
		a.fdFooter_s:hover {color: #336600; text-decoration: underline; }
		</style>
	</head>
<body bgcolor="#FFFFFF" text="#000000" marginheight="0" topmargin="0" marginwidth="0" leftmargin="0">
	<xsl:call-template name="mail_body" />
</body>
</html>
</xsl:template>

<xsl:template name="mail_body">
	<br/><br/>
	<table width="627" cellpadding="0" cellspacing="0" align="center">
		<tr valign="bottom">
			<td colspan="2"><a href="http://www.freshdirect.com?trk=ksea01"><img src="http://www.freshdirect.com/media_stat/images/logos/fd_logo_md.gif" width="216" height="42" alt="FreshDirect" border="0"/></a></td>
			<td colspan="2" align="right"><img src="http://www.freshdirect.com/media_stat/images/logos/fd_tag.gif" width="170" height="12" alt="It's All About The Food" border="0"/></td>
		</tr>
		
		<tr>
			<td><img src="http://www.freshdirect.com/media_stat/images/layout/cccccc.gif" width="42" height="1" vspace="8"/></td>
			<td><img src="http://www.freshdirect.com/media_stat/images/layout/cccccc.gif" width="272" height="1" vspace="8"/></td>
			<td><img src="http://www.freshdirect.com/media_stat/images/layout/cccccc.gif" width="271" height="1" vspace="8"/></td>
			<td><img src="http://www.freshdirect.com/media_stat/images/layout/cccccc.gif" width="42" height="1" vspace="8"/></td>
		</tr>
		
    	<tr align="center">
			<td colspan="4"><br/><a href="http://www.freshdirect.com/category.jsp?catId=kosher_seafood&amp;trk=ksea01">
			<img src="http://www.freshdirect.com/media_stat/images/template/email/kosher/now_avail_stars.gif" width="627" height="23" border="0" alt="NOW AVAILABLE!"/><br/>
			<img src="http://www.freshdirect.com/media_stat/images/template/email/kosher/kosher_fishes.jpg" width="627" height="69" border="0" vspace="10" alt="Kosher Fishes"/><br/>
			<img src="http://www.freshdirect.com/media_stat/images/template/email/kosher/kosher_seafood.gif" width="627" height="76" border="0" alt="KOSHER SEAFOOD"/></a>
			</td>
        </tr>
		
		<tr align="center">
			<td><img src="http://www.freshdirect.com/media_stat/images/template/email/kosher/u.gif" width="28" height="28"/></td>
			<td colspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="14"/><br/>
			Succulent salmon fillets, thick rosy tuna steaks, festive striped bass - we have the freshest kosher seafood in New York! Whether you are planning to grill a whole fish or make a hearty stew, seafood makes a quick, healthy meal. All our fish is custom-prepared and <b>Certified Kosher</b> by the <b>Orthodox Union ("OU")</b> and <b>Breuer Kahal Adas Jeshurun ("KAJ")</b>.<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="18"/></td>
			<td><img src="http://www.freshdirect.com/media_stat/images/template/email/kosher/kaj.gif" width="28" height="28"/></td>
		</tr>
		
		<tr>
			<td colspan="4" align="center">
			<img src="http://www.freshdirect.com/media_stat/images/template/email/kosher/stars.gif" width="627" height="22"/>
			<br/><br/>
			<a href="http://www.freshdirect.com/category.jsp?catId=kosher_seafood&amp;trk=ksea01" class="promoProduct"><font color="#336600"><b><u>Click here to shop for Kosher Seafood.</u></b></font></a>
			<br/><br/><br/>
			</td>
		</tr>
		
       	<tr>
       		<td colspan="4">
       			<p><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/></p>
       		</td>
        </tr>
	</table>

</xsl:template>

</xsl:stylesheet>

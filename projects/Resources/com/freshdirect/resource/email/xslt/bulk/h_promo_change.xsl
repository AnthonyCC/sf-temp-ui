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
		<title>Use it or lose it.</title>
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
		<p>We hope you enjoyed our $50.00 free food trial offer and that your FreshDirect experience was nothing short of excellent.</p>
			
		<p><b>Please note that our $25.00 free fresh food offer* on the second order will become $10.00 effective on Wednesday, March 19, 2003.</b></p>
			
                <p>If you would like to take advantage of our $25.00 promotional offer, you must place your second order on <a href="http://www.freshdirect.com">www.FreshDirect.com</a> before Wednesday, March 19. If you place your order on or after March 19 you will receive the $10.00 free fresh food promotion.</p>
            
                <p>We look forward to becoming your source for the best food at the best prices.</p>
            
                <p>Sincerely,
                <br/>Joe Fedele
                <br/>CEO and Creator
                <br/><b>FreshDirect</b>
                <br/>It's all about the food.
                </p>
			
                <p>P.S. We've made reordering easy. We automatically store your past orders, details and all, so you can reorder in minutes. Just log on to <a href="http://www.freshdirect.com">www.FreshDirect.com</a> and <b>use our Quickshop feature</b>, select your previous order, choose the items you want and add them to your cart in a click. Yup, it's that easy.</p>

                <p>*See <a href="http://www.freshdirect.com">website</a> for details.</p>
			
			<p><xsl:call-template name="h_footer_v2"/></p>
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
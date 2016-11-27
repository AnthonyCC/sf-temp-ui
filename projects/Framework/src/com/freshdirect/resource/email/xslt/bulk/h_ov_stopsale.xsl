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
		<title>Important: Organic Valley Milk Notice</title>
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
<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END LEFT SPACER ================= -->

<!-- ~~~~~~~~~~~~~~~ START CONTENT ~~~~~~~~~~~~~~~ -->
<td><xsl:call-template name="h_header_v1" />
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
			<td WIDTH="100%"><table WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0"><tr><td BGCOLOR="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1" border="0" alt="" /></td></tr></table></td>
		</tr>
	</table><br/>
	<table cellpadding="0" cellspacing="0" width="90%">
     <tr>
          <td>
               Our records indicate you recently purchased Organic Valley Milk. Please note that Organic Valley Farms, has issued a Stop Sale Notice on a recent shipment of the following:<br/><br/>
               
               <table cellpadding="0" cellspacing="5" border="0">
               <tr><td><b>Organic Valley Whole Milk</b></td><td> -- </td><td><b>Expiration date - 6/22/2003 &amp; 6/24/2003</b></td></tr>
               <tr><td><b>Organic Valley Low fat 1% Milk</b></td><td> -- </td><td><b>Expiration date - 6/22/2003 &amp; 6/29/2003</b></td></tr>
               <tr><td><b>Organic Valley Skim Nonfat Milk</b></td><td> -- </td><td><b>Expiration date - 6/22/2003, 6/24/2003, &amp; 6/29/2003</b></td></tr>
               </table><br/>
               
               <p>We ask you to check your Organic Valley milk containers and if any of the expiration dates match the items listed above, <b>please discard the milk</b>.</p>
               
               <p>Organic Valley assures us that <b>these products pose no health hazards</b> and are withdrawn as a proactive measure due to performance characteristics of a reduced shelf life.</p>
               
               <p>If you have any additional questions or concerns you may contact Organic Valley toll free at 1-888-444-6455 ext. 368. We have also attached the Stop Sale Notice we received from Organic Valley yesterday, May 20, 2003.</p>
               
               <p><b>We are issuing your account a credit on the product purchased.</b></p>
               
               <p>Sincerely,</p>
               
               <p><b>FreshDirect</b><br/>Customer Service Group</p>
               
               <img src="http://www.freshdirect.com/media_stat/images/template/email/organicvalley_stopsale.jpg" width="580" height="321" alt="Organic Valley Stop Sale Notice" />
               
               <p><xsl:call-template name="h_footer_v2"/></p>
		</td>
	</tr>
</table>
</td>
<!-- ~~~~~~~~~~~~~~~ END CONTENT ~~~~~~~~~~~~~~~ -->

<!-- =============== BEGIN RIGHT SPACER =============== -->
<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END RIGHT SPACER ================= -->
</tr>
</table>

</xsl:template>

</xsl:stylesheet>

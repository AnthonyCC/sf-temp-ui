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
		<title>FreshDirect is now available in the Upper West Side.</title>
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
<td><img src="http://www.freshdirect.com/images/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END LEFT SPACER ================= -->

<!-- ~~~~~~~~~~~~~~~ START CONTENT ~~~~~~~~~~~~~~~ -->
<td><xsl:call-template name="h_header_v1" />
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
		<td width="100%"><table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td bgcolor="#CCCCCC"><img src="http://www.freshdirect.com/images/clear.gif" width="1" height="1" border="0" alt="" /></td></tr></table></td>
		</tr>
	</table><br/>
	<table cellpadding="0" cellspacing="0" width="90%">
                <tr><td colspan="3" class="header"><font size="4"><b>Good things come to those who wait.</b><br/>FreshDirect will be movin' on up to the West Side!</font><br/><img src="http://www.freshdirect.com/images/clear.gif" width="1" height="18" /></td></tr>
                <tr valign="top">
                <td width="328"><img src="http://www.freshdirect.com/media_stat/images/template/email/westside_86_96.gif" width="328" height="209" /><br/><img src="http://www.freshdirect.com/images/clear.gif" width="1" height="18" /></td>
                <td width="18"><img src="http://www.freshdirect.com/images/clear.gif" width="18" height="1" /></td>
                <td width="100%"><img src="http://www.freshdirect.com/media_stat/images/template/homepages/truck.gif" width="61" height="43"/>
                <br/><img src="http://www.freshdirect.com/images/clear.gif" width="1" height="8" /><br/>
                <b>First deliveries start:</b>
                <br/><img src="http://www.freshdirect.com/images/clear.gif" width="1" height="8" /><br/>
                <font color="#CC0000" class="red12px"><b>Monday, March 24, 2003</b></font><br/>
                86th St. to 96th St., Central Park West to Riverside Dr.
                <br/><img src="http://www.freshdirect.com/images/clear.gif" width="1" height="8" /><br/>
                Orders may be placed on the web starting on Tuesday, March 18 (up to one week) prior to delivery.
                </td>
                </tr>
		<tr>
		<td colspan="3">
                <b>A note about our delivery areas:</b><br/>
                <font color="#CC0000" class="red12px">We look forward to delivering to all of the Upper West Side in the very near future. We thank you for your patience and will send you an email as we expand our delivery area.</font><br/><br/>
                <table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td bgcolor="#CCCCCC"><img src="http://www.freshdirect.com/images/clear.gif" width="1" height="1" border="0" alt="" /></td></tr></table>           
                
                <p>Order online at <a href="http://www.freshdirect.com">www.FreshDirect.com</a> and get <b>$50 worth of free fresh food*</b> just for trying us. Choose from over 3,000 irresistibly fresh items, plus a full selection of organic foods and popular grocery and household brands. All delivered to your door, exactly the way you want, with <b>100% satisfaction guaranteed</b>.</p>

                <p>To top it off, delivery is free for the first three orders, and our drivers won't accept tips. You've waited long enough. Log on to <a href="http://www.freshdirect.com">www.FreshDirect.com</a> today. </p>  
        
                <p>Enjoy,</p>
                
                <p>Joe Fedele<br/>
                CEO and Creator, FreshDirect<br/>
                Co-founder of Fairway Uptown</p>
                
                <p>*See <a href="http://www.freshdirect.com">website</a> for details.</p>
			
			<p><xsl:call-template name="h_footer_v2"/></p>
		</td>
	</tr>
</table>
</td>
<!-- ~~~~~~~~~~~~~~~ END CONTENT ~~~~~~~~~~~~~~~ -->

<!-- =============== BEGIN RIGHT SPACER =============== -->
<td><img src="http://www.freshdirect.com/images/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END RIGHT SPACER ================= -->
</tr>
</table>

</xsl:template>

</xsl:stylesheet>
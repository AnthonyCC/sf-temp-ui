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

<html lang="en-US" xml:lang="en-US">
	<head>
		<title>FreshDirect expands delivery on April 15th</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
	</head>
<body bgcolor="#FFFFFF" text="#333333">
	<xsl:call-template name="mail_body" />
</body>
</html>

</xsl:template>

<xsl:template name="mail_body">

<xsl:comment>

www.FRESHDIRECT.com
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

FreshDirect expands delivery in the Upper West Side!

FreshDirect is now delivering to zip code 10069.  

Order online at www.FreshDirect.com and get $50 worth of free fresh food* just for trying us. Choose from over 3,000 irresistibly fresh items, plus a full selection of organic foods and popular grocery and household brands. All delivered to your door, exactly the way you want, with 100% satisfaction guaranteed.

To top it off, delivery is free for the first three orders, and our drivers won't accept tips. You've waited long enough. Log on to www.FreshDirect.com today.  

Enjoy,

Joe Fedele
CEO and Creator, FreshDirect
Co-founder of Fairway Uptown

* See website for details

======

QUICK LINKS:

Go to FreshDirect
http://www.freshdirect.com

Contact Us
http://www.freshdirect.com/help/contact_fd.jsp

Frequently Asked Questions
http://www.freshdirect.com/help/faq_home.jsp

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
(c) 2002, 2003 FRESHDIRECT. All Rights Reserved.

</xsl:comment>

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
                <tr><td><p><font class="header" size="4"><b>FreshDirect expands delivery in the Upper West Side!</b></font></p>
                <p>FreshDirect is now delivering to zip code 10069. </p>
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
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
        <BASE href="http://www.freshdirect.com" />
		<title>FreshDirect expands delivery to Cobble Hill &amp; Carroll Gardens!</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
	</head>
<body bgcolor="#FFFFFF" text="#333333">
	<xsl:call-template name="mail_body" />
</body>
</html>

</xsl:template>

<xsl:template name="mail_body">

<xsl:comment>

www.FRESHDIRECT.com                                      It's all about the food
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Better Food. Better Prices. Delivered to Cobble Hill &amp; Carroll Gardens!

Brooklyn Delivery Details

Deliveries Begin
Thursday Oct. 9th, 2003

Orders may be placed on the web today!

A note about our delivery areas:
We look forward to delivering to additional parts of Brooklyn in the very near future. We thank you for your patience and will send you an email as we continue to expand our delivery area.

Order online at www.FreshDirect.com and get $50 worth of free fresh food on your first order of $100 or more.* Just for trying us. Choose from over 3,000 irresistibly fresh items, plus a full selection of organic foods and popular grocery and household brands. All delivered to your door, exactly the way you want, with 100% satisfaction guaranteed.

To top it off, your first delivery is free.  You've waited long enough. Log on to www.FreshDirect.com today. 

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
<td><img src="/media_stat/images/layout/clear.gif" alt="" width="20" height="1" border="0" /></td>
<td><xsl:call-template name="h_header_v1" />
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
		<td width="100%"><table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0" alt="" /></td></tr></table></td>
		</tr>
	</table><br/>
	<table cellpadding="0" cellspacing="0" width="90%">
                <tr><td colspan="3" class="header"><font size="4"><b>Better Food. Better Prices. Delivered to Cobble Hill &amp; Carroll Gardens!</b></font></td></tr>
                <tr valign="top">
                <td width="100%"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="18" /><br/><img src="/media_stat/images/template/homepages/truck.gif" width="61" height="43"/>
                <br/><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" /><br/>
                <b>Brooklyn Delivery Details</b>
                <br/><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="12" /><br/>
                <font color="#CC0000" class="red12px"><b>Deliveries Begin<br/>Thursday Oct. 9th, 2003</b></font>
                <br/><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="12" /><br/>
                Orders may be placed on the web today!
				<br/><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="14" />
				<table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0" alt="" /></td></tr></table>
                <img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="12" /><br/>
                <b>A note about our delivery areas:</b><br/><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="4" /><br/>
                <font color="#CC0000" class="red12px">We look forward to delivering to additional parts of Brooklyn in the very near future. We thank you for your patience and will send you an email as we continue to expand our delivery area.</font>
                <br/><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="13" />
                <table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0" alt="" /></td></tr></table>
                <img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="14" /><br/>
                Order online at <a href="http://www.freshdirect.com">www.FreshDirect.com</a> and get <b>$50 worth of free fresh food on your first order of $100 or more.*</b> Just for trying us. Choose from over 3,000 irresistibly fresh items, plus a full selection of organic foods and popular grocery and household brands. All delivered to your door, exactly the way you want, with <b>100% satisfaction guaranteed.</b>
                <p>To top it off, your first delivery is free.  You've waited long enough. Log on to <a href="http://www.freshdirect.com">www.FreshDirect.com</a> today.</p>  
        
                <p>Enjoy,</p>
                
                <p><b>Joe Fedele</b><br/>
                CEO and Creator, FreshDirect<br/>
                Co-founder of Fairway Uptown</p>
                
                <p>*See <a href="http://www.freshdirect.com">website</a> for details.<br/><br/></p>
                </td>
				<td width="18"><img src="/media_stat/images/layout/clear.gif" alt="" width="18" height="1" /></td>
				<td width="318" class="bodyCopySmall"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="14" /><br/><img src="/media_stat/images/template/email/cobblehill_carolgardens.gif" width="318" height="512" alt="Map"/><br/><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="18" /></td>
                </tr>
        		<tr>
        		<td colspan="3">
        			<p><xsl:call-template name="h_footer_v2"/></p>
        		</td>
        	    </tr>
	</table>
</td>
<td><img src="/media_stat/images/layout/clear.gif" alt="" width="20" height="1" border="0" /></td>
</tr>
</table>

</xsl:template>

</xsl:stylesheet>
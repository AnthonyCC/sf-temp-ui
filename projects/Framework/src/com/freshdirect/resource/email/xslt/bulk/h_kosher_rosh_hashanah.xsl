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
		<title>Our chef's Jewish holiday menu!</title>
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

ROSH HASHANAH MENU
Delivery Starts Thursday, September 25!

Celebrate a Sweet New Year with our chef's special holiday menu. We have all your favorites - Holiday Brisket with Gravy, Fresh Roasted Turkey, Matzo Ball Soup, Noodle Pudding, and Potato Pancakes just to name a few. Our chefs have done all the work, all you'll have to do is heat and serve.

Please note: Most of the items in our Rosh Hashanah menu are not certified kosher. Items that are certified kosher are shown with their kosher symbol.

To see our entire holiday menu: http://www.freshdirect.com/category.jsp?catId=hmr_khm&amp;trk=rosh

======
When you signed up with FreshDirect, you indicated an interest in receiving newsletters and updates. You may change this preference online in Your Account or REPLY via email and write UNSUBSCRIBE in the subject line.
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

<table width="615" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr>
		<td><a href="http://www.freshdirect.com/category.jsp?catId=hmr_khm&amp;trk=rosh"><img src="/media_stat/images/template/email/kosher/rosh_hashanah_hdr.gif" width="615" height="122" vspace="6" border="0" alt="FreshDirect - Rosh Hashanah Menu - Delivery Starts Thursday, September 25!"/></a></td>
	</tr>
	<tr><td class="bodyCopySmall">
		Celebrate a Sweet New Year with our chef's special holiday menu. We have all your favorites - <br/>
		<a href="http://www.freshdirect.com/product.jsp?productId=hmr_hol_brisk&amp;catId=hmr_khm_entree&amp;trk=rosh">Holiday Brisket with Gravy</a>, 
		<a href="http://www.freshdirect.com/product.jsp?productId=hmr_hol_turkeymd&amp;catId=hmr_khm_entree&amp;trk=rosh">Fresh Roasted Turkey</a>, 
		<a href="http://www.freshdirect.com/product.jsp?productId=hmrsp_matzobl&amp;catId=hmr_khm_apps&amp;trk=rosh">Matzo Ball Soup</a>, 
		<a href="http://www.freshdirect.com/product.jsp?productId=hmr_kside_noopud&amp;catId=hmr_khm_sides&amp;trk=rosh">Noodle Pudding</a>, 
		and <a href="http://www.freshdirect.com/product.jsp?productId=hmrveg_latke&amp;catId=hmr_khm_apps&amp;trk=rosh">Potato Pancakes</a> just to name a few. 
		Our chefs have done all the work, <b>all you'll have to do is heat and serve</b>.   
		<a href="http://www.freshdirect.com/category.jsp?catId=hmr_khm&amp;trk=rosh"><b>Click here to see our menu</b></a>.
		</td>
	</tr>
	<tr>
		<td align="center"><a href="http://www.freshdirect.com/category.jsp?catId=hmr_khm&amp;trk=rosh">
		<img src="/media_stat/images/template/email/kosher/turkey.jpg" width="197" height="155" vspace="8" border="0" alt="Whole Roasted Turkey with Gravy &amp; Cranberry Sauce"/>
		<img src="/media_stat/images/template/email/kosher/brisket.jpg" width="197" height="155" hspace="12" vspace="8" border="0" alt="Holiday Brisket with Gravy"/>
		<img src="/media_stat/images/template/email/kosher/salmon.jpg" width="197" height="155" vspace="8" border="0" alt="Salmon with Dill, Lemon &amp; Capers (Ready-to-Bake)"/></a></td>
	</tr>
	<tr>
		<td background="/media_stat/images/template/email/kosher/kos_star.gif"><img src="/media_stat/images/layout/clear.gif" width="1" height="24"/></td>
	</tr>
	<tr><td align="center"><a href="http://www.freshdirect.com/category.jsp?catId=hmr_khm&amp;trk=rosh">
		<img src="/media_stat/images/template/email/kosher/matzo_ball.jpg" width="113" height="90" vspace="8" border="0" alt="Matzo Ball Soup"/>
		<img src="/media_stat/images/template/email/kosher/latkes.jpg" width="113" height="90" hspace="12" vspace="8" border="0" alt="Latkes"/>
		<img src="/media_stat/images/template/email/kosher/glazed_carrots.jpg" width="113" height="90" vspace="8" border="0" alt="Butter-Glazed Carrots"/>
		<img src="/media_stat/images/template/email/kosher/kugel.jpg" width="113" height="90" hspace="12" vspace="8" border="0" alt="Noodle Pudding (Kugel)"/>
		<img src="/media_stat/images/template/email/kosher/whitefish.jpg" width="113" height="90" vspace="8" border="0" alt="White Fish Salad"/></a>
		</td>
	</tr>
	<tr>
		<td align="center" bgcolor="#6699CC" class="bodyCopySmall"><img src="/media_stat/images/layout/clear.gif" width="1" height="4"/><br/>
		<font color="#FFFFFF"><b>Please note:</b> Most of the items in our Rosh Hashanah menu are <b>not certified kosher</b>.<br/>Items that are certified kosher are shown with their kosher symbol.</font>
		<br/><img src="/media_stat/images/layout/clear.gif" width="1" height="4"/>
		</td>
	</tr>
	<tr><td class="mainLink" align="center"><br/>
		<a href="http://www.freshdirect.com/category.jsp?catId=hmr_khm&amp;trk=rosh">Click here to see our entire holiday menu!</a><br/><br/>
	</td>
	</tr>
	<tr><td><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/></td></tr>
</table>

</xsl:template>

</xsl:stylesheet>
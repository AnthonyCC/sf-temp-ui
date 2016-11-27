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
		<title>Our Thanksgiving Feast!</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
		<style type="text/css">
		.promoProduct    { font-size: 12px; color: #333333; font-family: Verdana, Arial, Helvetica, sans-serif; }
		.promoProduct.link {color: #336600; text-decoration: underline; }
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
	<table width="610" cellpadding="0" cellspacing="0" align="center" bgcolor="#FFFFFF" class="promoProduct">
		<tr>
			<td rowspan="5"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="1"/></td>
			<td colspan="2" align="center"><br/>
			<a href="http://www.freshdirect.com/category.jsp?catId=hmr_thanks&amp;trk=thanks_03uhd"><img src="http://www.freshdirect.com/media_stat/images/logos/turkey.gif" width="195" height="81" border="0" alt="FreshDirect"/><br/>
			<img src="http://www.freshdirect.com/media/images/promotions/thanks_03uhd.gif" width="610" height="61" border="0" vspace="6" alt="Our Thanksgiving Feast!"/></a>
			</td>
			<td rowspan="5"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="1"/></td>
		</tr>
		<tr>
			<td colspan="2" class="promoProduct">
			Your turn to cook this year? We have you covered - from gorgeous all-natural fresh roasted turkey to spiced pumpkin pie, with every glorious morsel in between.
			Our chefs David McInerney (formerly of Bouley and One If by Land, Two If by Sea) and Michael Stark (formerly of Tribeca Grill) will be preparing every dish using top quality ingredients, family recipes and professional know-how so it's bound to be exceptionally delicious. When we bring it to your door, <b>all you'll have to do is heat and serve</b>, so you can spend time with your family.
			<br/><br/>
			</td>
		</tr>
		<tr valign="top">
			<td class="promoProduct">
			<img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/choose_turkey.gif" width="225" height="10"/><br/>
			<img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8"/><br/>
			<b>$119.99</b> - <a href="http://www.freshdirect.com/product.jsp?catId=hmr_thanks&amp;productId=hmr_thanks_bx_01&amp;trk=thanks_03uhd" class="promoProduct"><font color="#336600"><u>12-14lb Turkey Dinner (Serves 4-6)</u></font></a><br/>
			<img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="4"/><br/>
			<b>$199.99</b> - <a href="http://www.freshdirect.com/product.jsp?catId=hmr_thanks&amp;productId=hmr_thanks_bx_02&amp;trk=thanks_03uhd" class="promoProduct"><font color="#336600"><u>16-20lb Turkey Dinner (Serves 8-12)</u></font></a><br/>
			<img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="4"/><br/>
			<b>$259.99</b> - <a href="http://www.freshdirect.com/product.jsp?catId=hmr_thanks&amp;productId=hmr_thanks_bx_03&amp;trk=thanks_03uhd" class="promoProduct"><font color="#336600"><u>22-24lb Turkey Dinner (Serves 14-18)</u></font></a><br/>
			<br/><br/>
			<b>Included Side Dishes:</b><br/>
			Cranberry Sauce, Parbaked Dinner Rolls, Zucchini Bread
			<br/><br/>
			<b>Includes your choice of Stuffing:</b><br/>
			Classic Cornbread Stuffing, Sausage Herb Stuffing or Dried Fruit Stuffing
			<br/><br/>
			<b>Includes your choice of Gravy:</b><br/>
			Traditional Turkey Gravy or Au Jus
			<br/><br/>
			<b>Includes your choice of four Side Dishes:</b><br/>
			Candied Yams, Mashed Potatoes, Green Beans, Glazed Carrots or Creamed Corn
			<br/><br/>
			<b>Includes your choice of Desserts:</b><br/>
			Pumpkin Pie, Carolina Pecan Pie, New England Apple Pie or Dark Double Chocolate Layer Cake
			</td>
			<td align="center"><a href="http://www.freshdirect.com/category.jsp?catId=hmr_thanks&amp;trk=thanks_03uhd"><img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/turkey.jpg" width="215" height="155" border="0"/><br/><img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/complete_dinners.gif" width="171" height="14" border="0" vspace="8"/></a></td>
		</tr>
		<tr>
			<td colspan="2"><br/>
			<table width="100%">
				<tr align="center">
					<td align="left"><a href="http://www.freshdirect.com/category.jsp?catId=hmr_thanks&amp;trk=thanks_03uhd"><img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/corn.jpg" width="90" height="71" border="0" alt="Creamed Corn"/></a></td>
					<td><a href="http://www.freshdirect.com/category.jsp?catId=hmr_thanks&amp;trk=thanks_03uhd"><img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/yam.jpg" width="90" height="71" border="0" alt="Candied Yams"/></a></td>
					<td><a href="http://www.freshdirect.com/category.jsp?catId=hmr_thanks&amp;trk=thanks_03uhd"><img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/cranberry.jpg" width="90" height="71" border="0" alt="Cranberry Sauce"/></a></td>
					<td><a href="http://www.freshdirect.com/category.jsp?catId=hmr_thanks&amp;trk=thanks_03uhd"><img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/au_jus.jpg" width="90" height="71" border="0" alt="Au Jus"/></a></td>
					<td><a href="http://www.freshdirect.com/category.jsp?catId=hmr_thanks&amp;trk=thanks_03uhd"><img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/stuffing.jpg" width="90" height="71" border="0" alt="Sausage Herb Stuffing"/></a></td>
					<td align="right"><a href="http://www.freshdirect.com/category.jsp?catId=hmr_thanks&amp;trk=thanks_03uhd"><img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/potato.jpg" width="90" height="71" border="0" alt="Mashed Potatoes"/></a></td>
				</tr>
			</table>
			<a href="http://www.freshdirect.com/category.jsp?catId=hmr_thanks&amp;trk=thanks_03uhd"><img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/order_dates.gif" width="610" height="46" border="0" vspace="8"/></a>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center" class="promoProduct"><br/>
			<a href="http://www.freshdirect.com/category.jsp?catId=hmr_thanks&amp;trk=thanks_03uhd" class="mainLink" style="font-size: 16px; color: #336600; font-weight: bold; font-family: Arial, Helvetica, sans-serif;"><font color="#336600">Click here to see our entire Thanksgiving menu!</font></a>
			<br/><br/>
			<a href="http://www.freshdirect.com?trk=thanks_03uhd"><img src="http://www.freshdirect.com/media_stat/images/template/email/logo_url.gif" width="264" height="35" vspace="6" border="0" alt="www.FreshDirect.com"/></a><br/>
			Price and availability subject to change. Please see Web site for current 
			prices and availability.<br/>
			<img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8"/><br/>
			<xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/><br/>
			</td>
		</tr>
	</table>
</xsl:template>

</xsl:stylesheet>
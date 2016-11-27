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
		<title>Our Thanksgiving Favorites!</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
		<style type="text/css">
		.subText    { font-size: 9px; color: #000000; font-weight: bold; font-family: Verdana, Arial, Helvetica, sans-serif; }
		.picks    { font-size: 18px; color: #336600; font-weight: bold; font-family: Arial, Helvetica, sans-serif; }
		.picks.link {color: #336600; text-decoration: underline; }
		a.picks:link {color: #336600; text-decoration: underline; }
		a.picks:visited {color: #336600; text-decoration: underline; }
		a.picks:active {color: #FF9933; text-decoration: underline; }
		a.picks:hover {color: #336600; text-decoration: underline; }
		.promoProduct    { font-size: 12px; color: #000000; font-family: Verdana, Arial, Helvetica, sans-serif; }
		.promoProduct.link {color: #336600; text-decoration: underline; }
		a.promoProduct:link {color: #336600; text-decoration: underline; }
		a.promoProduct:visited {color: #336600; text-decoration: underline; }
		a.promoProduct:active {color: #FF9933; text-decoration: underline; }
		a.promoProduct:hover {color: #336600; text-decoration: underline; }
		.promoProduct_s    { font-size: 11px; color: #000000; font-family: Verdana, Arial, Helvetica, sans-serif; }
		.promoProduct_s.link {color: #336600; text-decoration: underline; }
		a.promoProduct_s:link {color: #336600; text-decoration: underline; }
		a.promoProduct_s:visited {color: #336600; text-decoration: underline; }
		a.promoProduct_s:active {color: #FF9933; text-decoration: underline; }
		a.promoProduct_s:hover {color: #336600; text-decoration: underline; }
		.fdFooter_s    { font-size: 11px; color: #333333; font-family: Verdana, Arial, sans-serif; }
		a.fdFooter_s:link {color: #336600; text-decoration: underline; }
		a.fdFooter_s:visited {color: #336600; text-decoration: underline; }
		a.fdFooter_s:active {color: #FF9933; text-decoration: underline; }
		a.fdFooter_s:hover {color: #336600; text-decoration: underline; }
		</style>
	</head>
<body bgcolor="#663300" text="#333333" marginheight="0" topmargin="0" marginwidth="0" leftmargin="0">
	<xsl:call-template name="mail_body" />
</body>
</html>

</xsl:template>

<xsl:template name="mail_body">
<table width="100%" cellpadding="10" cellspacing="0" border="0" bgcolor="#663300"><tr><td>
<table width="656" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" align="center" class="promoProduct">
	<tr><td colspan="16" align="center"><br/><a href="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media_stat/images/logos/turkey.gif" width="195" height="81" border="0" alt="FreshDirect"/><br/>
	<img src="http://www.freshdirect.com/media/images/promotions/picks_thanksgiving_6b.gif" width="610" height="34" vspace="6" border="0" alt="Our Thanksgiving Favorites!"/></a><br/>
	<img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/leaf_line.gif" width="616" height="13" vspace="2"/><br/><br/></td>
	</tr>	
	
	<tr valign="top">
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="38"/></td>
		<td colspan="6"><img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/talking_turkey.gif" width="295" height="23" alt="We're Talking Turkey"/></td>
		<td rowspan="5"></td>
		<td rowspan="5" bgcolor="#000000"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/></td>
		<td rowspan="5"></td>
		<td colspan="5" rowspan="2"><a href="http://www.freshdirect.com/product.jsp?productId=cake_pmpkn_cheese&amp;catId=bak_cake_fd&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/cheesecake.gif" width="178" height="50" border="0" alt="Pumpkin Cheesecake with Cranberry Chutney - $9.99/each"/></a></td>
		<td rowspan="5"></td>
	</tr>
	
	<tr valign="top">
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="12"/></td>
		<td colspan="4" rowspan="2" class="promoProduct">
		<a href="http://www.freshdirect.com/product.jsp?productId=trk_fresh&amp;catId=mt_trky&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/fresh_turkey.gif" width="149" height="25" border="0" alt="Fresh Grade A All Natural Turkey - $1.19/lb"/><br/>
		<img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="4" border="0"/><br/>
		<font color="#336600"><u><b>Click here to buy!</b></u></font></a><br/>
		<img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="13" border="0"/><br/>
		<a href="http://www.freshdirect.com/product.jsp?productId=ptrk_whl&amp;catId=mt_trky&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/frozen_turkey.gif" width="150" height="24" border="0" alt="Frozen Grade A All Natural Turkey - $0.99/lb"/><br/>
		<img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="4" border="0"/><br/>
		<font color="#336600"><u><b>Click here to buy!</b></u></font></a><br/>
		<img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="13" border="0"/><br/>
		<a href="http://www.freshdirect.com/product.jsp?productId=kos_turk_whole&amp;catId=kosher_meat_poultry&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/kosher_turkey.gif" width="157" height="26" border="0" alt="Aaron's Glatt Kosher Fresh Turkey - $1.99/lb"/><br/>
		<img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="4" border="0"/><br/>
		<font color="#336600"><u><b>Click here to buy!</b></u></font></a><br/>
		<img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="6" border="0"/>
		</td>
		<td colspan="2" rowspan="2" align="right"><a href="http://www.freshdirect.com/category.jsp?catId=mt_trky&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/raw_turkey.jpg" width="140" height="113" border="0" alt="Fresh Turkey"/></a></td>
	</tr>
	
	<tr valign="top">
		<td rowspan="3"></td>
		<td colspan="3" class="promoProduct_s"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="4"/><br/>Rich and creamy New York cheesecake takes an autumnal turn when it meets pumpkin and cranberries. The tangy flavor of fresh cranberry chutney is a perfect counterpoint to the warm, delicate flavors of pumpkin and spice.
		<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="4"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=cake_pmpkn_cheese&amp;catId=bak_cake_fd&amp;trk=epicks06b" class="promoProduct_s"><font color="#336600"><u><b>Click here to buy!</b></u></font></a></td>
		<td colspan="2" align="right"><a href="http://www.freshdirect.com/product.jsp?productId=cake_pmpkn_cheese&amp;catId=bak_cake_fd&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/cheesecake.jpg" width="125" height="105" border="0" alt="Pumpkin Cheesecake with Cranberry Chutney"/></a></td>
	</tr>
	
	<tr valign="top">
		<td colspan="6" rowspan="2" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/000000.gif" width="297" height="1" vspace="14"/><br/><a href="http://www.freshdirect.com/category.jsp?catId=hmr_thanks&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/turkey_dinner.gif" width="278" height="33" border="0" alt="Don't Feel Like Cooking? Try Our Chef's Complete Turkey Dinners."/></a><br/>
		<img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8"/><br/>
		<a href="http://www.freshdirect.com/category.jsp?catId=hmr_thanks&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/turkey_dinner.jpg" width="100" height="72" border="0" align="left" alt="Turkey Dinner"/></a><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="2" height="102" align="left"/>
		Our chefs will be preparing every dish using top quality ingredients, family recipes and professional know-how. <b>All you'll have to do is heat and serve</b>.<br/>
		<img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="4"/><br/><a href="http://www.freshdirect.com/category.jsp?catId=hmr_thanks&amp;trk=epicks06b" class="promoProduct"><font color="#336600"><u><b>Click here to learn more!</b></u></font></a>
		</td>
		<td colspan="5" valign="bottom"><a href="http://www.freshdirect.com/product.jsp?productId=cake_hween_ckie&amp;catId=bak_cookies_fd&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/cookies.gif" width="199" height="34" border="0" vspace="3" alt="Autumn Sugar Cookies - $3.99/6pk"/></a></td>
	</tr>
	
	<tr valign="top">
		<td colspan="3" class="promoProduct_s">Crispy and sweet, you will mistake these buttery-tasting cookies for homemade. The edges are baked until crunchy, but the centers are still soft. Dunk into a glass of milk, serve alongside a dish of vanilla ice cream or pack into a lunch box.<br/>
		<img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="4"/><br/>
		<a href="http://www.freshdirect.com/product.jsp?productId=cake_hween_ckie&amp;catId=bak_cookies_fd&amp;trk=epicks06b" class="promoProduct_s"><font color="#336600"><u><b>Click here to buy!</b></u></font></a></td>
		<td colspan="2" align="right"><a href="http://www.freshdirect.com/product.jsp?productId=cake_hween_ckie&amp;catId=bak_cookies_fd&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/cookies.jpg" width="100" height="79" border="0" alt="Autumn Sugar Cookies"/></a></td>
	</tr>
	
	<tr><td colspan="16" align="center"><br/><img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/leaf_line.gif" width="616" height="13"/><br/><br/></td></tr>

	<tr align="center" valign="top" class="promoProduct"> 
		<td colspan="2" rowspan="7" align="left"><img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/native_alvin.gif" width="50" height="125" alt="Alvin"/></td>
		<td colspan="2" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=pot_yukon&amp;catId=pot_pot&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media/images/product/vegetables/potatoes/pot_yukon_c.jpg" width="70" height="70" border="0" alt="Yukon Gold Potato"/><br/><font color="#336600"><u>Yukon Gold Potato</u></font></a><br/><b>$0.99/lb</b></td>
		<td colspan="2" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=roll_soft&amp;catId=bak_rolls&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media/images/product/bakery/rolls/roll_soft_c.jpg" width="70" height="70" border="0" alt="Soft Dinner Rolls"/><br/><font color="#336600"><u>Soft Dinner Rolls</u></font></a><br/><b>$2.79/8pk</b></td>
		<td colspan="5" class="promoProduct"><a href="http://www.freshdirect.com/product.jsp?productId=pietrt_pump&amp;catId=bak_pie_fd&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media/images/product/pastry/pies_tarts/pietrt_pump_c.jpg" width="80" height="80" border="0" alt="FreshDirect Pumpkin Pie"/><br/><font color="#336600"><u>Pumpkin Pie</u></font></a><br/><b>$9.99/ea</b></td>
		<td class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=del_marcel_dmouss&amp;catId=pate&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media/images/product/deli/pate/del_marcel_dmouss_c.jpg" width="70" height="70" border="0" alt="Marcel &amp; Henri Duck Mousse with Truffles"/><br/><font color="#336600"><u><b>Marcel &amp; Henri</b> Duck Mousse with Truffles</u></font></a><br/><b>$9.99/lb</b></td>
		<td colspan="2" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=bns_green&amp;catId=pea&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media/images/product/vegetables/beans/bns_green_c.jpg" width="70" height="70" border="0" alt="Green Beans"/><br/><font color="#336600"><u>Green Beans</u></font></a><br/><b>$1.99/lb</b></td>
		<td colspan="2" rowspan="4" align="right"><img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/pilgrim_marc.gif" width="57" height="106" vspace="40" alt="Marc"/></td>
	</tr>
	
	<tr><td colspan="12"><br/></td></tr>
	
	<tr align="center" valign="top">
		<td colspan="2" class="promoProduct"><a href="http://www.freshdirect.com/category.jsp?catId=gro_canne_fruit&amp;prodCatId=gro_canne_fruit&amp;productId=gro_ocean_regular_01&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media/images/product/grocery/gro_ocean_regular_01_c.jpg" width="80" height="80" border="0" alt="Ocean Spray Jellied Cranberry Sauce"/><br/><font color="#336600"><u><b>Ocean Spray</b> Jellied Cranberry Sauce</u></font></a><br/><b>$1.49/ea</b></td>
		<td colspan="2" class="promoProduct"><a href="http://www.freshdirect.com/category.jsp?catId=gro_baker_stuff&amp;prodCatId=gro_baker_stuff&amp;productId=gro_pfarm_hrbstuf&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media/images/product/grocery_three/gro_pfarm_hrbstuf_c.jpg" width="80" height="80" border="0" alt="Pepperidge Farm Herb Stuffing"/><br/><font color="#336600"><u><b>Pepperidge Farm</b> Herb Stuffing</u></font></a><br/><b>$2.79/ea</b></td>
		<td colspan="5" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=bl_french&amp;catId=cof_by_rst_fr_reg&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media/images/product/coffee/blends/bl_french_c.jpg" width="70" height="70" border="0" alt="FreshDirect French Roast"/><br/><font color="#336600"><u><b>FreshDirect</b> French Roast</u></font></a><br/><b>$3.99/lb</b></td>
		<td class="promoProduct"><a href="http://www.freshdirect.com/product.jsp?productId=cake_hween_cpcke&amp;catId=bak_cake_fd&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media/images/product/pastry/cakes_cupcakes/cake_hween_cpcke_c.jpg" width="80" height="80" border="0" alt="Autumn Cupcakes"/><br/><font color="#336600"><u>Autumn Cupcakes</u></font></a><br/><b>$4.99/4pk</b></td>
		<td colspan="2" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=pot_yam&amp;catId=pot_sweet&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media/images/product/vegetables/potatoes/pot_yam_c.jpg" width="70" height="70" border="0" alt="Jewel Yam"/><br/><font color="#336600"><u>Jewel Yam</u></font></a><br/><b>$0.99/lb</b></td>
	</tr>
	
	<tr><td colspan="12"><br/></td></tr>
	
	<tr align="center" valign="top">
		<td colspan="2" class="promoProduct"><a href="http://www.freshdirect.com/category.jsp?catId=gro_bever&amp;groceryVirtual=gro_bever&amp;brandValue=bd_fizzy_lizzy&amp;prodCatId=gro_bever_juice_spark&amp;productId=spe_fizliz_cran&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media/images/product/grocery_two/spe_fizliz_cran_c.jpg" width="80" height="80" border="0" alt="Fizzy Lizzy Cranberry Sparkling Juice"/><br/><font color="#336600"><u><b>Fizzy Lizzy</b> Cranberry Sparkling Juice</u></font></a><br/><b>$4.49/4pk></b></td>
		<td colspan="2" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=net_gda_raw_02&amp;catId=gouda&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media/images/product/cheese/netherlands_sweden/net_gda_raw_02_c.jpg" width="70" height="70" border="0" alt="4 Year Gouda"/><br/><font color="#336600"><u>4 Year Gouda</u></font></a><br/><b>$9.99/lb</b></td>
		<td colspan="5" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=nut_chestnut&amp;catId=cfnnutshlon&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media/images/product/fruit/nuts/nut_chestnut_c.jpg" width="70" height="70" border="0" alt="Chestnuts"/><br/><font color="#336600"><u>Chestnuts</u></font></a><br/><b>$3.99/lb</b></td>
		<td class="promoProduct"><a href="http://www.freshdirect.com/category.jsp?catId=dai_butte_regu&amp;prodCatId=dai_butte_regu&amp;productId=dai_kellers_trkybtr&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media/images/product/dairy/dai_kellers_trkybtr_c.jpg" width="80" height="80" border="0" alt="Keller's Turkey Shaped Butter Sculpture"/><br/><font color="#336600"><u><b>Keller's</b> Turkey Shaped Butter Sculpture</u></font></a><br/><b>$3.19/ea</b></td>
		<td colspan="2" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="9"/><br/><a href="http://www.freshdirect.com/product.jsp?catId=hmr_stock&amp;productId=hmrstck_chkn&amp;trk=epicks06b"><img src="http://www.freshdirect.com/media/images/product/meals/stock_ingredients/hmrstck_chkn_c.jpg" width="90" height="71" border="0" alt="FreshDirect Chicken Stock"/><br/><font color="#336600"><u><b>FreshDirect</b> Chicken Stock</u></font></a><br/><b>$2.99/ea</b></td>
		<td colspan="2" rowspan="3" align="right" valign="middle"><img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/pilgrim_melissa.gif" width="46" height="88" vspace="12" alt="Melissa"/></td>
	</tr>
	
	<tr><td colspan="12" align="center"><br/><br/><a href="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks06b" class="mainLink" style="font-size: 18px; color: #336600; font-weight: bold; font-family: Arial, Helvetica, sans-serif;"><font color="#336600"><u>Click here for all of our Thanksgiving Picks!</u></font></a><br/><br/><br/></td></tr>
	
	<tr><td colspan="12" align="center">
	<table width="100%" cellspacing="2" cellpadding="0" border="0" class="fdFooter_s">
		<tr>
		<td colspan="2" class="fdFooter_s"><div align="center"><b>OUR SCHEDULE FOR THE THANKSGIVING WEEK</b></div><br/>
		<i>Due to high demand, we recommend placing your order early to guarantee delivery for Tuesday 11/25 or Wednesday 11/26.</i><br/><br/>
		</td>
		</tr>
		<tr valign="top">
		<td width="30%" class="fdFooter_s">Tuesday 11/25</td><td class="fdFooter_s">Regular delivery schedule</td>
		</tr>
		<tr valign="top">
		<td class="fdFooter_s">Wednesday 11/26</td><td class="fdFooter_s">Deliveries will have 3-hour windows from 9:00 am - 11:00 pm</td>
		</tr>
		<tr valign="top">
		<td class="fdFooter_s">Thursday 11/27</td><td class="fdFooter_s">Thanksgiving Day - FreshDirect will be closed</td>
		</tr>
		<tr valign="top">
		<td class="fdFooter_s">Friday 11/28</td><td class="fdFooter_s">FreshDirect will be closed but customer service will be open from 8:00 am - 10:00 pm</td>
		</tr>
		<tr valign="top">
		<td class="fdFooter_s">Saturday 11/29</td><td class="fdFooter_s">Normal delivery schedules resume</td>
		</tr>
		<tr>
		<td colspan="2" align="center" class="fdFooter_s"><br/>Happy Thanksgiving from all of us at FreshDirect!</td>
		</tr>
	</table><br/>
	</td></tr>
	
	<tr><td colspan="16" align="center"><img src="http://www.freshdirect.com/media_stat/images/template/email/thanksgiving/leaf_line.gif" width="616" height="13"/></td></tr>
	
	<tr bgcolor=""> 
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="38" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="66" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="42" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="11" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="97" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="43" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="10" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="11" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="43" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="108" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="21" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="87" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="38" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="10"/></td>
	</tr>
	
	<tr>
		<td colspan="16" align="center" class="promoProduct">
	    <a href="http://www.freshdirect.com?trk=epicks06b"><img src="http://www.freshdirect.com/media_stat/images/template/email/logo_url.gif" width="264" height="35" vspace="6" border="0" alt="www.FreshDirect.com"/></a><br/>
	    Price and availability subject to change. Please see Web site for current 
	    prices and availability.<br/>
	    <img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8"/>
		</td>
	</tr>
	<tr><td></td><td colspan="14"><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/><br/><br/></td><td></td></tr>
</table>
</td></tr>
</table>
</xsl:template>

</xsl:stylesheet>
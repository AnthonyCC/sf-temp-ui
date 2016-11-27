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
		<title>Autumn Picks for Oktoberfest!</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
		<style type="text/css">
		.subText    { font-size: 9px; color: #000000; font-weight: bold; font-family: Verdana, Arial, Helvetica, sans-serif; }
		.suggestion    { font-size: 16px; color: #336600; font-weight: bold; font-family: Arial, Helvetica, sans-serif; }
		.suggestion.link {color: #336600; text-decoration: underline; }
		a.suggestion:link {color: #336600; text-decoration: underline; }
		a.suggestion:visited {color: #336600; text-decoration: underline; }
		a.suggestion:active {color: #FF9933; text-decoration: underline; }
		a.suggestion:hover {color: #336600; text-decoration: underline; }
		.featurePromo  { font-size: 13px; color: #336600; font-family: Verdana, Arial, Helvetica, sans-serif; }
		.featurePromo.link {color: #336600; text-decoration: underline; }
		a.featurePromo:link {color: #336600; text-decoration: underline; }
		a.featurePromo:visited {color: #336600; text-decoration: underline; }
		a.featurePromo:active {color: #FF9933; text-decoration: underline; }
		a.featurePromo:hover {color: #336600; text-decoration: underline; }
		.promoProduct    { font-size: 11px; color: #336600; font-family: Verdana, Arial, Helvetica, sans-serif; }
		.promoProduct.link {color: #336600; text-decoration: underline; }
		a.promoProduct:link {color: #336600; text-decoration: underline; }
		a.promoProduct:visited {color: #336600; text-decoration: underline; }
		a.promoProduct:active {color: #FF9933; text-decoration: underline; }
		a.promoProduct:hover {color: #336600; text-decoration: underline; }
		</style>
	</head>
<body bgcolor="#FFFFFF" text="#333333">
	<xsl:call-template name="mail_body" />
</body>
</html>

</xsl:template>

<xsl:template name="mail_body">

<table width="668" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr><td colspan="5"><img src="/media_stat/images/template/email/seasonal/autumn_email_nav.gif" width="668" height="118" border="0" usemap="#email_nav" alt="We've also added an Autumn Picks section to our website!" /></td></tr>
	<tr><td colspan="5"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0" /></td></tr>
	<tr>
		<td colspan="2" rowspan="2"><img src="/media_stat/images/layout/top_left_curve.gif" width="6" height="6" border="0"/></td>
		<td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0" /></td>
		<td colspan="2" rowspan="2"><img src="/media_stat/images/layout/top_right_curve.gif" width="6" height="6" border="0"/></td>
	</tr>
	<tr>
		<td><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0" /></td>
	</tr>
	<tr>
		<td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0" /></td>
		<td colspan="3" align="center">
			<table width="96%" cellpadding="0" cellspacing="0" border="0">
				<tr><td colspan="5" align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="10" border="0" /><br/><a href="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks04a"><img src="/media/images/promotions/oktober_picks_hdr_a.gif" width="503" height="48" alt="Autumn Picks for Oktoberfest!" border="0"/></a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="10" border="0" /></td></tr>
				<tr><td colspan="5" background="/media/editorial/picks/summer/autumn_line.gif"><img src="/media_stat/images/layout/clear.gif" width="1" height="12" border="0" /></td></tr>
				<tr><td colspan="5"><br/><br/></td></tr>
				<tr valign="top">
				
					<td width="20%" align="center" class="bodyCopySmall"><a href="/category.jsp?catId=gro_beer_impor&amp;prodCatId=gro_beer_impor&amp;productId=beer_hoffbrau_oct_sxbt&amp;trk=epicks04a" class="promoProduct"><img src="/media/images/product/grocery_two/beer_hoffbrau_oct_sxbt_c.jpg" width="80" height="80" border="0" /><br/>Hofbr&#228;u Octoberfest</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$6.49/ea</b></td>

					<td width="20%" align="center" class="bodyCopySmall"><a href="/category.jsp?catId=del_pkghdchix&amp;prodCatId=del_pkghdchix&amp;productId=dai_karlehmer_brat&amp;trk=epicks04a" class="promoProduct"><img src="/media/images/product/dairy/dai_karlehmer_brat_c.jpg" width="80" height="80" border="0" /><br/>Karl Ehmer Bratwurst, Cooked</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$3.99/ea</b></td>

					<td width="20%" align="center" class="bodyCopySmall"><a href="/category.jsp?catId=gro_condi_musta&amp;prodCatId=gro_condi_musta&amp;productId=spe_maille_dijon_01&amp;trk=epicks04a" class="promoProduct"><img src="/media/images/product/grocery_three/spe_maille_dijon_01_c.jpg" width="80" height="80" border="0" /><br/>Maille Dijon Mustard</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$2.29/ea</b></td>
					
					<td width="20%" align="center" class="bodyCopySmall"><a href="/category.jsp?catId=gro_beer_impor&amp;prodCatId=gro_beer_impor&amp;productId=beer_hacker_weisse_sxbt&amp;trk=epicks04a" class="promoProduct"><img src="/media/images/product/grocery_three/beer_hacker_weisse_sxbt_c.jpg" width="80" height="80" border="0" /><br/>Hacker-Pschorr Weisse</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$10.99/ea</b></td>

					<td width="20%" align="center" class="bodyCopySmall"><a href="/category.jsp?catId=gro_beer_impor&amp;prodCatId=gro_beer_impor&amp;productId=beer_grol_sxteenbt&amp;trk=epicks04a" class="promoProduct"><img src="/media/images/product/grocery_two/beer_grol_sxteenbt_c.jpg" width="80" height="80" border="0" /><br/>Grolsch Swingtop Bottles</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$8.49/ea</b></td>

				</tr>
				<tr><td colspan="5"><img src="/media_stat/images/layout/clear.gif" width="1" height="20" border="0" /></td></tr>
				<tr valign="top">
				
					<td align="center" class="bodyCopySmall"><img src="/media_stat/images/layout/clear.gif" width="1" height="4" border="0" /><br/><a href="/product.jsp?productId=hmrmeat_rdwnebrsdrib&amp;catId=hmr_entreesnew&amp;trk=epicks04a" class="promoProduct"><img src="/media/images/product/meals/meat_entree/hmrmeat_rdwnebrsdrib_c.jpg" width="90" height="71" border="0" /><br/>Beer-Braised Boneless Short Ribs</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$13.99/ea</b></td>

					<td align="center" class="bodyCopySmall"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0" /><br/><a href="/product.jsp?productId=ppry_pmpry&amp;catId=ppry&amp;trk=epicks04a" class="promoProduct"><img src="/media/images/product/bakery/pumpernickle_rye/ppry_pmpry_c.jpg" width="70" height="70" border="0" /><br/>Parbaked Pumpernickel Rye Bread</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$2.49/ea</b></td>

					<td align="center" class="bodyCopySmall"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0" /><br/><a href="/product.jsp?productId=cchm_karlehmer_bfham&amp;catId=cchm_smoked&amp;trk=epicks04a" class="promoProduct"><img src="/media/images/product/deli/coldcuts_curedmeat/ham_coldcuts/cchm_karlehmer_bfham_c.jpg" width="70" height="70" border="0" /><br/>Karl Ehmer Black Forest Ham</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$7.49/lb</b></td>

					<td align="center" class="bodyCopySmall"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0" /><br/><a href="/product.jsp?productId=usa_gda_smkd&amp;catId=gouda&amp;trk=epicks04a" class="promoProduct"><img src="/media/images/product/cheese/usa/usa_gda_smkd_c.jpg" width="70" height="70" border="0" /><br/>Smoked Gouda</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$3.99/lb</b></td>

					<td align="center" class="bodyCopySmall"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0" /><br/><a href="/product.jsp?productId=picl_sauer&amp;catId=deli_pickle_fresh&amp;trk=epicks04a" class="promoProduct"><img src="/media/images/product/deli/pickles/picl_sauer_c.jpg" width="70" height="70" border="0" /><br/>Fresh Sauerkraut</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$1.29/lb</b></td> 

				</tr>
				<tr><td colspan="5"><img src="/media_stat/images/layout/clear.gif" width="1" height="20" border="0" /></td></tr>
				<tr valign="top">
				
					<td align="center" class="bodyCopySmall"><img src="/media_stat/images/layout/clear.gif" width="1" height="10" border="0" /><br/><a href="/product.jsp?productId=pr_comice&amp;catId=pr&amp;trk=epicks04a" class="promoProduct"><img src="/media/images/product/fruit/pears/pr_comice_c.jpg" width="70" height="70" border="0" /><br/>Comice Pear</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$1.99/lb</b></td>

					<td align="center" class="bodyCopySmall"><img src="/media_stat/images/layout/clear.gif" width="1" height="9" border="0" /><br/><a href="/product.jsp?productId=psld_blue_gmnpota&amp;catId=psld&amp;trk=epicks04a" class="promoProduct"><img src="/media/images/product/deli/antipasti_delisalads/psld_blue_gmnpota_c.jpg" width="90" height="71" border="0" /><br/>German Potato Salad</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$2.99/ea</b></td>

					<td align="center" class="bodyCopySmall"><img src="/media_stat/images/layout/clear.gif" width="1" height="10" border="0" /><br/><a href="/product.jsp?productId=plum_prune&amp;catId=plum&amp;trk=epicks04a" class="promoProduct"><img src="/media/images/product/fruit/stonefruit/plum_prune_c.jpg" width="70" height="70" border="0" /><br/>Italian Plum</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$1.49/lb</b></td>

					<td align="center" class="bodyCopySmall"><img src="/media_stat/images/layout/clear.gif" width="1" height="2" border="0" /><br/><a href="/category.jsp?catId=fro_appet_pret&amp;prodCatId=fro_appet_pret&amp;productId=fro_super_frozen_p_01&amp;trk=epicks04a" class="promoProduct"><img src="/media/images/product/frozen/fro_super_frozen_p_01_c.jpg" width="80" height="78" border="0" /><br/>Superpretzel Soft Pretzels</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$2.69/ea</b></td>

					<td align="center" class="bodyCopySmall"><a href="/category.jsp?catId=gro_candy_chocb&amp;prodCatId=gro_candy_chocb&amp;productId=spe_ritter_wchaz&amp;trk=epicks04a" class="promoProduct"><img src="/media/images/product/grocery_three/spe_ritter_wchaz_c.jpg" width="80" height="80" border="0" /><br/>Ritter Sport White Chocolate with Hazelnuts</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$1.89/ea</b></td> 

				</tr>
			</table>
			<table width="96%" border="0" cellspacing="0" cellpadding="0">
				<tr><td colspan="5" align="center"><br/><a href="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks04a" class="suggestion"><b>Click here to see all of our Oktoberfest suggestions!</b></a><br/><br/><br/></td></tr>
				<tr><td colspan="5" background="/media/editorial/picks/summer/autumn_line.gif"><img src="/media_stat/images/layout/clear.gif" width="1" height="12" border="0" /></td></tr>
				<tr><td colspan="5"><img src="/media_stat/images/layout/clear.gif" width="1" height="18" border="0" /></td></tr>
				<tr>
					<td width="50%" class="bodyCopySmall" align="center">
					<a href="/seasonal_guide.jsp?deptId=fru&amp;catId=fru_season_guide&amp;trk=epicks04a"><img src="/media/editorial/picks/promos/fruit_seasonal_guide.gif" alt="FRUIT SEASONAL GUIDE" width="246" height="81" border="0" vspace="4" /></a>
						<table width="246" cellpadding="0" cellspacing="0" border="0" align="center">
							<tr>
								<td class="bodyCopySmall">
									Buying "in season" is probably the best way to ensure delicious fruit. Let our experts guide you to the best that nature has to offer all year round.<br/><img src="/media_stat/images/layout/clear.gif" width="1" height="4" alt="" border="0" /><br/>
									<a href="/seasonal_guide.jsp?deptId=fru&amp;catId=fru_season_guide&amp;trk=epicks04a" class="featurePromo"><b>Click here!</b></a>
								</td>
							</tr>
						</table>
					</td>
					<td><img src="/media_stat/images/layout/clear.gif" width="15" height="1" border="0" /></td>
					<td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0" /></td>
					<td><img src="/media_stat/images/layout/clear.gif" width="15" height="1" border="0" /></td>
					<td width="50%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr valign="top">
							<td>
							<a href="/newproducts.jsp?trk=epicks04a" class="featurePromo"><img src="/media/editorial/picks/promos/new_products_ad.gif" alt="NEW PRODUCTS!" width="160" height="33" border="0" />
							<br/><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0" /><br/><b>Click here!</b></a>
							</td>
							<td align="center">
							<a href="/newproducts.jsp?trk=epicks04a"><img src="/media/editorial/picks/promos/new_star.gif" alt="NEW" width="52" height="51" border="0" /></a>
							</td>
						</tr>
						<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="13" border="0" /></td></tr>
						<tr bgcolor="#CCCCCC">
							<td><img src="/media_stat/images/layout/clear.gif" width="160" height="1" border="0" /></td>
							<td><img src="/media_stat/images/layout/clear.gif" width="55" height="1" border="0" /></td>
						</tr>
						<tr><td colspan="2"><img src="/media_stat/images/layout/clear.gif" width="1" height="13" border="0" /></td></tr>
						<tr valign="top">
							<td><a href="mailto:web_ideas@freshdirect.com?subject=Website%20Suggestions!&amp;body=Please%20send website%20comments%20only.%20Feedback%20regarding%20orders,%20credits,%20or%20delivery%20should%20be%20sent%20to%20service@freshdirect.com%20or%20call%20toll%20free%201-866-279-2451." class="featurePromo"><img src="/media/editorial/picks/promos/suggestions.gif" alt="SUGGESTIONS?" width="143" height="30" border="0" /><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0" /><br/><b>Email our Editor.</b></a><br/></td>
							<td align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="6" border="0" /><br/><a href="mailto:web_ideas@freshdirect.com?subject=Website%20Suggestions!&amp;body=Please%20send website%20comments%20only.%20Feedback%20regarding%20orders,%20credits,%20or%20delivery%20should%20be%20sent%20to%20service@freshdirect.com%20or%20call%20toll%20free%201-866-279-2451."><img src="/media/editorial/picks/promos/suggestions_env.gif" alt="" width="37" height="24" border="0" /></a></td>
						</tr>
						</table>
					</td>
				</tr>
			</table>
			<br/>
		</td>
		<td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0" /></td>
	</tr>
	<tr>
		<td colspan="2" rowspan="2"><img src="/media_stat/images/layout/bottom_left_curve.gif" width="6" height="6" border="0"/></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0" /></td>
		<td colspan="2" rowspan="2"><img src="/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6" border="0"/></td>
	</tr>
	<tr>
		<td bgcolor="#999966"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0" /></td>
	</tr>
	<tr>
		<td colspan="5"><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0" /><br/><img src="/media_stat/images/template/email/seasonal/summer_email_bottom.gif" width="668" height="23" border="0" alt="Price and availability subject to change. Please see Web site for current prices and availability." /></td>
	</tr>
	<tr>
		<td><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="5" height="8" border="0" /></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="656" height="8" border="0" /></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="5" height="8" border="0" /></td>
		<td><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
	</tr>
	<tr><td colspan="5"><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/></td></tr>
</table>


<MAP NAME="email_nav">
<AREA SHAPE="RECT" COORDS="0,32,202,76" HREF="http://www.freshdirect.com?trk=epicks04a" ALT="Home"/>
<AREA SHAPE="RECT" COORDS="0,77,21,97" HREF="http://www.freshdirect.com?trk=epicks04a" ALT="Home" />
<AREA SHAPE="RECT" COORDS="20,77,60,97" HREF="http://www.freshdirect.com/department.jsp?deptId=fru&amp;trk=epicks04a" ALT="Fruit" />
<AREA SHAPE="RECT" COORDS="59,77,125,97" HREF="http://www.freshdirect.com/department.jsp?deptId=veg&amp;trk=epicks04a" ALT="Vegetables" />
<AREA SHAPE="RECT" COORDS="124,77,158,98" HREF="http://www.freshdirect.com/department.jsp?deptId=mea&amp;trk=epicks04a" ALT="Meat" />
<AREA SHAPE="RECT" COORDS="157,77,212,97" HREF="http://www.freshdirect.com/department.jsp?deptId=sea&amp;trk=epicks04a" ALT="Seafood" />
<AREA SHAPE="RECT" COORDS="211,77,241,98" HREF="http://www.freshdirect.com/department.jsp?deptId=del&amp;trk=epicks04a" ALT="Deli" />
<AREA SHAPE="RECT" COORDS="240,77,284,98" HREF="http://www.freshdirect.com/department.jsp?deptId=che&amp;trk=epicks04a" ALT="Cheese" />
<AREA SHAPE="RECT" COORDS="283,77,321,98" HREF="http://www.freshdirect.com/department.jsp?deptId=dai&amp;trk=epicks04a" ALT="Dairy" />
<AREA SHAPE="RECT" COORDS="320,77,364,98" HREF="http://www.freshdirect.com/department.jsp?deptId=cof&amp;trk=epicks04a" ALT="Coffee" />
<AREA SHAPE="RECT" COORDS="363,77,390,97" HREF="http://www.freshdirect.com/department.jsp?deptId=tea&amp;trk=epicks04a" ALT="Tea" />
<AREA SHAPE="RECT" COORDS="389,77,428,97" HREF="http://www.freshdirect.com/department.jsp?deptId=pas&amp;trk=epicks04a" ALT="Pasta" />
<AREA SHAPE="RECT" COORDS="427,77,472,96" HREF="http://www.freshdirect.com/department.jsp?deptId=bak&amp;trk=epicks04a" ALT="Bakery" />
<AREA SHAPE="RECT" COORDS="471,77,513,97" HREF="http://www.freshdirect.com/department.jsp?deptId=hmr&amp;trk=epicks04a" ALT="Meals" />
<AREA SHAPE="RECT" COORDS="512,77,565,97" HREF="http://www.freshdirect.com/department.jsp?deptId=gro&amp;trk=epicks04a" ALT="Grocery" />
<AREA SHAPE="RECT" COORDS="564,77,611,97" HREF="http://www.freshdirect.com/department.jsp?deptId=fro&amp;trk=epicks04a" ALT="Frozen" />
<AREA SHAPE="RECT" COORDS="610,77,668,98" HREF="http://www.freshdirect.com/department.jsp?deptId=spe&amp;trk=epicks04a" ALT="Specialty" />
<AREA SHAPE="RECT" COORDS="453,46,521,74" HREF="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks04a" ALT="AUTUMN PICKS" />
<AREA SHAPE="RECT" COORDS="523,45,600,75" HREF="http://www.freshdirect.com/your_account/manage_account.jsp?trk=epicks04a" ALT="YOUR ACCOUNT" />
<AREA SHAPE="RECT" COORDS="609,40,664,75" HREF="http://www.freshdirect.com/help/index.jsp?trk=epicks04a" ALT="GET HELP" />
<AREA SHAPE="RECT" COORDS="448,0,652,23" HREF="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks04a" ALT="AUTUMN PICKS" />
</MAP>

</xsl:template>

</xsl:stylesheet>
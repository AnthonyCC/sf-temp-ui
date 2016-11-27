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
		<title>Happy Holidays!</title>
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
		.promoProduct    { font-size: 11px; color: #333333; font-family: Verdana, Arial, Helvetica, sans-serif; }
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
<body bgcolor="#FFFFFF" text="#333333">
	<xsl:call-template name="mail_body" />
</body>
</html>

</xsl:template>

<xsl:template name="mail_body">

<table width="520" cellpadding="0" cellspacing="0" border="0" align="center">
	<tr><td colspan="5"><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/winter_email_nav.gif" width="668" height="118" border="0" usemap="#email_nav" alt="We've also added a Winter Picks section to our website!" /></td></tr>
	<tr><td colspan="5"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="5" border="0" /></td></tr>
	<tr>
		<td colspan="2" rowspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/top_left_curve.gif" width="6" height="6" border="0"/></td>
		<td bgcolor="#999966"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1" border="0" /></td>
		<td colspan="2" rowspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/top_right_curve.gif" width="6" height="6" border="0"/></td>
	</tr>
	<tr>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="5" border="0" /></td>
	</tr>
	<tr>
		<td bgcolor="#999966"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1" border="0" /></td>
		<td colspan="3" align="center">
			<table width="520" cellpadding="0" cellspacing="0" border="0">
				<tr><td colspan="6" align="center"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="5" border="0" /><br/><a href="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks07b"><img src="http://www.freshdirect.com/media/images/promotions/picks_holiday_7b.gif" width="519" height="54" alt="HAVE A SAFE &amp; HAPPY HOLIDAYS! FROM YOUR FRESHDIRECT FAMILY" border="0" vspace="10"/></a></td></tr>
				<tr><td colspan="6" align="center"><img src="http://www.freshdirect.com/media/editorial/picks/winter_line.gif" width="514" height="14" border="0" /></td></tr>
				<tr><td colspan="6" align="center" class="promoProduct"><br/><b>Just a reminder we'll be closed for Christmas and<br/>will resume normal delivery hours on Friday, 12/26.</b><br/><br/></td></tr>
				<tr valign="top">
				
					<td width="20%" align="center" class="promoProduct"><a href="http://www.freshdirect.com/product.jsp?productId=ckibni_xmas&amp;catId=bak_cookies_fd&amp;trk=epicks07b" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/pastry/cookies_brownies/ckibni_xmas_c.jpg" width="80" height="60" border="0" vspace="10" /><br/><font color="#336600"><u>Christmas<br/>Sugar Cookies</u></font></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$3.99/6pk</b></td>

					<td width="20%" align="center" class="promoProduct"><a href="http://www.freshdirect.com/category.jsp?catId=egg_nog&amp;prodCatId=egg_nog&amp;productId=dai_farm_eggnog&amp;trk=epicks07b" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/dairy/dai_farm_eggnog_c.jpg" width="80" height="80" border="0" /><br/><font color="#336600"><u><b>Farmland</b><br/>Egg Nog</u></font></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$1.99/ea</b></td>

					<td width="20%" colspan="2" align="center" class="promoProduct"><a href="http://www.freshdirect.com/product.jsp?productId=dghcrst_chcchp&amp;catId=bak_cookies_fd&amp;trk=epicks07b" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/pastry/doughs_crusts/dghcrst_chcchp_c.jpg" width="70" height="70" border="0" vspace="5"/><br/><font color="#336600"><u><b>FreshDirect</b><br/>Frozen Chocolate Chip Cookie Dough</u></font></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$5.99/ea</b></td>
					
					<td width="20%" align="center" class="promoProduct"><a href="http://www.freshdirect.com/product.jsp?productId=chclt_strwbrry&amp;catId=bak_cookies_fd&amp;trk=epicks07b" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/pastry/chocolates/chclt_strwbrry_c.jpg" width="70" height="70" border="0" vspace="5" /><br/><font color="#336600"><u><b>FreshDirect</b><br/>Dark Chocolate-Dipped Strawberries</u></font></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$7.49/6pk</b></td>

					<td width="20%" align="center" class="promoProduct"><a href="http://www.freshdirect.com/product.jsp?productId=cake_stollen&amp;catId=bak_cake_fd&amp;trk=epicks07b" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/pastry/cakes_cupcakes/cake_stollen_c.jpg" width="70" height="70" border="0" vspace="5" /><br/><font color="#336600"><u>Fruit Stollen, Frozen (Thaw &amp; Serve)</u></font></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$6.99/ea</b></td>

				</tr>
				<tr><td colspan="6"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="20" border="0" /></td></tr>
				<tr valign="top">
				
					<td align="center" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1" border="0" /><br/><a href="http://www.freshdirect.com/product.jsp?productId=pgs_whlgoos&amp;catId=mt_noduck&amp;trk=epicks07b" class="promoProduct"><img src="http://www.freshdirect.com/media/images/navigation/department/meat/meat_cat/poultry_cat/pgs_cat.jpg" width="48" height="79" border="0" /><br/><font color="#336600"><u>Goose, Frozen</u></font></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$5.99/lb</b></td>

					<td align="center" class="promoProduct"><a href="http://www.freshdirect.com/product.jsp?productId=lrst_crwn&amp;catId=lrstbnin&amp;trk=epicks07b" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/meat/lamb/roast/lrst_crwn_c.jpg" width="70" height="70" border="0" vspace="5" /><br/><font color="#336600"><u>Lamb Crown Roast</u></font></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$19.99/lb</b></td>

					<td colspan="2" align="center" class="promoProduct"><a href="http://www.freshdirect.com/product.jsp?productId=fflt_slmn_org&amp;catId=fflt&amp;trk=epicks07b" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/seafood/fish_fillets/fflt_slmn_org_c.jpg" width="80" height="60" border="0" vspace="10" /><br/><font color="#336600"><u><b>Clare Island</b><br/>Certified Organic Salmon Fillet</u></font></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$9.99/lb</b></td>

					<td align="center" class="promoProduct"><a href="http://www.freshdirect.com/product.jsp?productId=ita_form_tart&amp;catId=semisoft&amp;trk=epicks07b" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/cheese/italy/ita_form_tart_c.jpg" width="70" height="70" border="0" vspace="5"/><br/><font color="#336600"><u>Formaggio<br/>al Tartufo</u></font></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$18.99/lb</b></td>

					<td align="center" class="promoProduct"><a href="http://www.freshdirect.com/product.jsp?productId=pate_hv_foie_mous&amp;catId=pate&amp;trk=epicks07b" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/deli/pate/pate_hv_foie_mous_c.jpg" width="70" height="70" border="0" vspace="5" /><br/><font color="#336600"><u><b>Hudson Valley</b><br/>Foie Gras Mousse</u></font></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$11.99/ea</b></td> 

				</tr>
				<tr><td colspan="6"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="20" border="0" /></td></tr>
				<tr valign="top">
				
					<td align="center" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="5" border="0" /><br/><a href="http://www.freshdirect.com/product.jsp?productId=slbsr_lbstr_bzlfzn&amp;catId=slbsr&amp;trk=epicks07b" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/seafood/lobster/slbsr_lbstr_bzlfzn_c.jpg" width="80" height="60" border="0" vspace="15" /><br/><font color="#336600"><u>Brazilian<br/>Lobster Tails</u></font></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$25.99/lb</b></td>

					<td width="25%" colspan="2" align="center" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="5" border="0" /><br/><a href="http://www.freshdirect.com/category.jsp?catId=gro_beer_impor&amp;prodCatId=gro_beer_impor&amp;productId=beer_samsmith_box&amp;trk=epicks07b" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/grocery_three/beer_samsmith_box_c.jpg" width="80" height="80" border="0" vspace="5" /><br/><font color="#336600"><u><b>Samuel Smith</b><br/>Selection Box with Glass</u></font></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$9.99/ea</b></td>

					<td width="35%" colspan="2" align="center" class="promoProduct"><a href="http://www.freshdirect.com/product.jsp?productId=che_arti_course_six&amp;catId=che_arti&amp;trk=epicks07b" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/cheese/artisanal/che_arti_course_six_c.jpg" width="120" height="95" border="0" /><br/><font color="#336600"><u><b>Artisanal</b><br/>Four-Cheese Plate</u></font></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$33.99/ea</b></td>


					<td align="center" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="5" border="0" /><br/><a href="http://www.freshdirect.com/product.jsp?productId=scf_move_wss_scot&amp;catId=scf_sliside&amp;trk=epicks07b" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/deli/smoked_curedfish/scf_move_scotside_c.jpg" width="70" height="70" border="0" vspace="10" /><br/><font color="#336600"><u><b>Chef Alain's</b><br/>Scottish Salmon, Whole Sliced Side
</u></font></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$12.99/lb</b></td> 

				</tr>
			</table>
			<table width="520" border="0" cellspacing="0" cellpadding="0">
				<tr><td colspan="5" align="center"><br/><a href="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks07b" class="suggestion"><font color="#336600"><u><b>Click here to see all of our Holiday suggestions!</b></u></font></a><br/><br/><br/></td></tr>
				<tr><td colspan="5"><img src="http://www.freshdirect.com/media/editorial/picks/winter_line.gif" width="514" height="14" border="0" /></td></tr>
				<tr><td colspan="5"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="18" border="0" /></td></tr>
				<tr>
					<td width="50%" class="bodyCopySmall" align="center">
					<a href="http://www.freshdirect.com/department.jsp?deptId=win&amp;trk=epicks07b"><img src="http://www.freshdirect.com/media/editorial/picks/bc_wine_now.gif" alt="BEST CELLARS - WINE NOW AVAILABLE" width="254" height="51" border="0" /><br/>
<img src="http://www.freshdirect.com/media/editorial/picks/5bottles.jpg" width="233" height="110" border="0" alt="Wines"/></a>
					</td>
					<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="15" height="1" border="0" /></td>
					<td bgcolor="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1" border="0" /></td>
					<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="15" height="1" border="0" /></td>
					<td width="50%">
						<table border="0" cellspacing="0" cellpadding="0">
						<tr valign="top">
							<td>
							<a href="http://www.freshdirect.com/newproducts.jsp?trk=epicks07b" class="featurePromo">
							<img src="http://www.freshdirect.com/media/editorial/picks/promos/new_products_ad.gif" alt="NEW PRODUCTS!" width="160" height="33" border="0"/>
							<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="5" border="0" /><br/>
							<font color="#336600"><u><b>Click here!</b></u></font></a>
							</td>
							<td align="center">
							<a href="http://www.freshdirect.com/newproducts.jsp?trk=epicks07b"><img src="http://www.freshdirect.com/media/editorial/picks/promos/new_star.gif" alt="NEW" width="52" height="51" border="0" /></a>
							</td>
						</tr>
						<tr><td colspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="13" border="0" /></td></tr>
						<tr bgcolor="#CCCCCC">
							<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="170" height="1" border="0" /></td>
							<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="55" height="1" border="0" /></td>
						</tr>
						<tr><td colspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="13" border="0" /></td></tr>
						<tr valign="top">
							<td><a href="mailto:web_ideas@freshdirect.com?subject=Website%20Suggestions!&amp;body=Please%20send website%20comments%20only.%20Feedback%20regarding%20orders,%20credits,%20or%20delivery%20should%20be%20sent%20to%20service@freshdirect.com%20or%20call%20toll%20free%201-866-279-2451." class="featurePromo"><img src="http://www.freshdirect.com/media/editorial/picks/promos/suggestions.gif" alt="SUGGESTIONS?" width="143" height="30" border="0" /><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="5" border="0" /><br/><font color="#336600"><u><b>Email our Editor.</b></u></font></a><br/></td>
							<td align="center"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="6" border="0" /><br/><a href="mailto:web_ideas@freshdirect.com?subject=Website%20Suggestions!&amp;body=Please%20send website%20comments%20only.%20Feedback%20regarding%20orders,%20credits,%20or%20delivery%20should%20be%20sent%20to%20service@freshdirect.com%20or%20call%20toll%20free%201-866-279-2451."><img src="http://www.freshdirect.com/media/editorial/picks/promos/suggestions_env.gif" alt="" width="37" height="24" border="0" /></a></td>
						</tr>
						</table>
					</td>
				</tr>
			</table>
			<br/>
		</td>
		<td bgcolor="#999966"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1" border="0" /></td>
	</tr>
	<tr>
		<td colspan="2" rowspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/bottom_left_curve.gif" width="6" height="6" border="0"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="5" border="0" /></td>
		<td colspan="2" rowspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6" border="0"/></td>
	</tr>
	<tr>
		<td bgcolor="#999966"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1" border="0" /></td>
	</tr>
	<tr>
		<td colspan="5"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="5" border="0" /><br/><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/summer_email_bottom.gif" width="668" height="23" border="0" alt="Price and availability subject to change. Please see Web site for current prices and availability." /></td>
	</tr>
	<tr>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="5" height="8" border="0" /></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="656" height="8" border="0" /></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="5" height="8" border="0" /></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
	</tr>
	<tr><td colspan="5"><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/></td></tr>
</table>

<MAP NAME="email_nav">
<AREA SHAPE="RECT" COORDS="0,28,199,79" HREF="http://www.freshdirect.com?trk=epicks07b" ALT="Home"/>
<AREA SHAPE="RECT" COORDS="445,0,629,25" HREF="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks07b" ALT="WINTER PICKS"/>
<AREA SHAPE="RECT" COORDS="456,45,523,78" HREF="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks07b" ALT="WINTER PICKS"/>
<AREA SHAPE="RECT" COORDS="522,44,601,78" HREF="http://www.freshdirect.com/your_account/manage_account.jsp?trk=epicks07b" ALT="YOUR ACCOUNT"/>
<AREA SHAPE="RECT" COORDS="601,43,668,78" HREF="http://www.freshdirect.com/help/index.jsp?trk=epicks07b" ALT="GET HELP"/>
<AREA SHAPE="RECT" COORDS="0,78,30,117" HREF="http://www.freshdirect.com?trk=epicks07b" ALT="Home"/>
<AREA SHAPE="RECT" COORDS="29,78,72,98" HREF="http://www.freshdirect.com/department.jsp?deptId=fru&amp;trk=epicks07b" ALT="Fruit"/>
<AREA SHAPE="RECT" COORDS="72,77,140,98" HREF="http://www.freshdirect.com/department.jsp?deptId=veg&amp;trk=epicks07b" ALT="Vegetables"/>
<AREA SHAPE="RECT" COORDS="141,78,175,98" HREF="http://www.freshdirect.com/department.jsp?deptId=mea&amp;trk=epicks07b" ALT="Meat"/>
<AREA SHAPE="RECT" COORDS="176,78,233,98" HREF="http://www.freshdirect.com/department.jsp?deptId=sea&amp;trk=epicks07b" ALT="Seafood"/>
<AREA SHAPE="RECT" COORDS="234,77,263,98" HREF="http://www.freshdirect.com/department.jsp?deptId=del&amp;trk=epicks07b" ALT="Deli"/>
<AREA SHAPE="RECT" COORDS="264,77,310,98" HREF="http://www.freshdirect.com/department.jsp?deptId=che&amp;trk=epicks07b" ALT="Cheese"/>
<AREA SHAPE="RECT" COORDS="311,77,350,98" HREF="http://www.freshdirect.com/department.jsp?deptId=dai&amp;trk=epicks07b" ALT="Dairy"/>
<AREA SHAPE="RECT" COORDS="351,78,389,98" HREF="http://www.freshdirect.com/department.jsp?deptId=pas&amp;trk=epicks07b" ALT="Pasta"/>
<AREA SHAPE="RECT" COORDS="390,78,434,98" HREF="http://www.freshdirect.com/department.jsp?deptId=cof&amp;trk=epicks07b" ALT="Coffee"/>
<AREA SHAPE="RECT" COORDS="435,77,463,98" HREF="http://www.freshdirect.com/department.jsp?deptId=tea&amp;trk=epicks07b" ALT="Tea"/>
<AREA SHAPE="RECT" COORDS="463,77,561,98" HREF="http://www.freshdirect.com/department.jsp?deptId=bak&amp;trk=epicks07b" ALT="Bakery"/>
<AREA SHAPE="RECT" COORDS="562,77,658,98" HREF="http://www.freshdirect.com/department.jsp?deptId=hmr&amp;trk=epicks07b" ALT="Meals"/>
<AREA SHAPE="RECT" COORDS="30,96,90,118" HREF="http://www.freshdirect.com/department.jsp?deptId=gro&amp;trk=epicks07b" ALT="Grocery"/>
<AREA SHAPE="RECT" COORDS="90,97,148,118" HREF="http://www.freshdirect.com/department.jsp?deptId=spe&amp;trk=epicks07b" ALT="Specialty"/>
<AREA SHAPE="RECT" COORDS="146,97,196,118" HREF="http://www.freshdirect.com/department.jsp?deptId=fro&amp;trk=epicks07b" ALT="Frozen"/>
<AREA SHAPE="RECT" COORDS="195,96,230,118" HREF="http://www.freshdirect.com/department.jsp?deptId=win&amp;trk=epicks07b" ALT="Wine"/>
<AREA SHAPE="RECT" COORDS="606,97,658,118" HREF="http://www.freshdirect.com/department.jsp?deptId=kos&amp;trk=epicks07b" ALT="Kosher"/>
</MAP>


</xsl:template>

</xsl:stylesheet>
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
		<title>Summer Picks from FreshDirect!</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
		<style type="text/css">
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
	<tr><td colspan="5"><img src="/media_stat/images/template/email/seasonal/summer_email_nav.gif" width="668" height="98" border="0" usemap="#summer_email" alt="We've also added a Summer Picks section to our website!" /></td></tr>
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
				<tr><td colspan="6" align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="10" border="0" /><br/><a href="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks01"><img src="/media/editorial/picks/summer/summer_picks_hdr.gif" width="326" height="55" border="0" alt="SUMMER PICKS" /></a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="10" border="0" /></td></tr>
				<tr><td colspan="6" background="/media/editorial/picks/summer/line_summer.gif"><img src="/media_stat/images/layout/clear.gif" width="1" height="13" border="0" /></td></tr>
				<tr><td colspan="6" align="center" class="bodyCopy"><img src="/media_stat/images/layout/clear.gif" width="1" height="12" border="0" /><br/><b>Here are just a few of our manager's picks.</b><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="10" border="0" /></td></tr>
				<tr valign="top">
					<td width="16%" align="center" class="bodyCopySmall"><a href="http://www.freshdirect.com/product.jsp?catId=bgril&amp;productId=bstk_tbone&amp;trk=epicks01" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/meat/beef/steaks/bstk_tbone_c.jpg" width="70" height="70" border="0" /><br/>T-Bone Steak</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$7.99/lb</b></td>
					<td width="16%" align="center" class="bodyCopySmall"><a href="http://www.freshdirect.com/product.jsp?productId=bgrnd_ptty_frsh&amp;catId=bptty&amp;trk=epicks01" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/meat/beef/groundbeef/bgrnd_ptty_frsh_c.jpg" width="70" height="70" border="0" /><br/>Fresh Beef Hamburgers</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$1.89/lb</b></td>
					<td width="16%" align="center" class="bodyCopySmall"><img src="/media_stat/images/layout/clear.gif" width="1" height="10" border="0" /><br/><a href="http://www.freshdirect.com/product.jsp?productId=fstk_swrdfsh&amp;catId=fstk&amp;trk=epicks01" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/seafood/fish_steaks/fstk_swrdfsh_c.jpg" width="80" height="60" border="0" /><br/>Swordfish</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$10.99/lb</b></td>
					<td width="16%" align="center" class="bodyCopySmall"><img src="/media_stat/images/layout/clear.gif" width="1" height="10" border="0" /><br/><a href="http://www.freshdirect.com/product.jsp?productId=frtc_slmn_cake&amp;catId=sea_cakes&amp;trk=epicks01" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/seafood/ready_to_cook/frtc_slmn_cake_c.jpg" width="80" height="60" border="0" /><br/>Salmon Burgers</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$6.59/2pk</b></td>
					<td width="16%" align="center" class="bodyCopySmall"><a href="http://www.freshdirect.com/product.jsp?productId=picl_sauer&amp;catId=picl&amp;trk=epicks01" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/deli/pickles/picl_sauer_c.jpg" width="70" height="70" border="0" /><br/>Fresh Sauerkraut</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$1.29/lb</b></td>
					<td width="16%" align="center" class="bodyCopySmall"><a href="http://www.freshdirect.com/product.jsp?productId=picl_bread&amp;catId=picl&amp;trk=epicks01" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/deli/pickles/picl_bread_c.jpg" width="70" height="70" border="0" /><br/>Bread &amp; Butter Pickle Chips</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$2.29/lb</b></td>
				</tr>
				<tr><td colspan="6"><img src="/media_stat/images/layout/clear.gif" width="1" height="20" border="0" /></td></tr>
				<tr valign="top">
					<td align="center" class="bodyCopySmall"><a href="http://www.freshdirect.com/product.jsp?productId=pr_taylor&amp;catId=pr&amp;trk=epicks01" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/fruit/pears/pr_taylor_c.jpg" width="70" height="70" border="0" /><br/>Taylor Gold<br/>New Zealand Pear</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$1.99/lb</b></td>
					<td align="center" class="bodyCopySmall"><a href="http://www.freshdirect.com/product.jsp?productId=mln_wtr_sdls&amp;catId=mln&amp;trk=epicks01" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/fruit/melons/mln_wtr_sdls_c.jpg" width="70" height="70" border="0" /><br/>Seedless Watermelon</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$0.39/lb</b></td>
					<td align="center" class="bodyCopySmall"><a href="http://www.freshdirect.com/product.jsp?productId=chrry_red&amp;catId=br&amp;trk=epicks01" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/fruit/stonefruit/chrry_red_cr.jpg" width="70" height="70" border="0" /><br/>Bing Cherries</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$1.99/lb</b></td>
					<td align="center" class="bodyCopySmall"><a href="http://www.freshdirect.com/product.jsp?productId=egplnt_sicilian&amp;catId=egplnt&amp;trk=epicks01" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/vegetables/eggplant/egplnt_sicilian_c.jpg" width="70" height="70" border="0" /><br/>Sicilian Eggplant</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$1.29/lb</b></td>
					<td align="center" class="bodyCopySmall"><a href="http://www.freshdirect.com/product.jsp?productId=ltc_org_sprngmix&amp;catId=ltc_letuc&amp;trk=epicks01" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/vegetables/mushrooms/ltc_org_sprngmix_c.jpg" width="70" height="70" border="0" /><br/>Spring Salad Mix,<br/>Organic,<br/>Packaged</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$3.49/ea</b></td>
					<td align="center" class="bodyCopySmall"><a href="http://www.freshdirect.com/product.jsp?productId=tm_vripe&amp;catId=tm&amp;trk=epicks01" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/vegetables/tomatoes/tm_vripe_c.jpg" width="70" height="70" border="0" /><br/>Vine Ripened<br/>(not on vine)</a><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="3" border="0" /><br/><b>$1.49/lb</b></td>
				</tr>
			</table>
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
				<tr><td colspan="3" align="center"><br/><br/><a href="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks01" class="suggestion"><b>Click here to see all of our summer suggestions!</b></a><br/><br/><br/></td></tr>
				<tr valign="top">
					<td width="100"><img src="/media_stat/images/template/email/seasonal/lil_tony_picks.jpg" width="93" height="99" border="0" /><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0" /></td>
					<td width="468" align="center" class="bodyCopySmall"><img src="/media_stat/images/layout/cccccc.gif" width="225" height="1" /><br/><br/>
					<font class="bodyCopy"><b>Tony and Eddie's Summer produce picks!</b></font><br/>
					Summer's here! Which means you just can't go wrong with <a href="http://www.freshdirect.com/category.jsp?catId=tm&amp;trk=epicks01" class="promoProduct">tomatoes</a>, <a href="http://www.freshdirect.com/category.jsp?catId=sq_zuch&amp;trk=epicks01" class="promoProduct">zucchinis</a>, and <a href="http://www.freshdirect.com/category.jsp?catId=egplnt&amp;trk=epicks01" class="promoProduct">eggplant</a>.
					Summer fruits are also starting and we recommend <a href="http://www.freshdirect.com/category.jsp?catId=mln&amp;trk=epicks01" class="promoProduct">melons</a> as well as <a href="http://www.freshdirect.com/category.jsp?catId=br&amp;trk=epicks01" class="promoProduct">berries &amp; cherries</a>.
					<a href="http://www.freshdirect.com/product.jsp?productId=pr_taylor&amp;catId=pr&amp;trk=epicks01" class="promoProduct">Taylor Gold pears</a> - from New Zealand - are also fantastic.
					</td>
					<td width="100"><img src="/media_stat/images/layout/clear.gif" width="93" height="1" border="0" /></td>
				</tr>
			</table>
			<table width="96%" border="0" cellspacing="0" cellpadding="0">
				<tr><td colspan="5" background="/media/editorial/picks/summer/line_summer.gif"><img src="/media_stat/images/layout/clear.gif" width="1" height="13" border="0" /></td></tr>
				<tr><td colspan="5"><img src="/media_stat/images/layout/clear.gif" width="1" height="18" border="0" /></td></tr>
				<tr>
					<td width="60%" align="center" class="bodyCopySmall"><a href="http://www.freshdirect.com/category.jsp?catId=gro_beer&amp;trk=epicks01"><img src="/media/editorial/picks/promos/beer_is_here.gif" alt="BEER IS HERE!" width="251" height="34" border="0" /></a><br/>
					<img src="/media_stat/images/layout/clear.gif" width="1" height="2" alt="" border="0" /><br/>
					Now you can get beer with your FreshDirect<br/>delivery through WBL, Inc. beer distributors.<br/>
					<a href="http://www.freshdirect.com/category.jsp?catId=gro_beer&amp;trk=epicks01" class="featurePromo"><img src="/media/editorial/picks/promos/beer_mugs.jpg" alt="" width="257" height="52" border="0" vspace="5" /><br/>
					<img src="/media_stat/images/layout/clear.gif" width="1" height="2" alt="" border="0" /><br/>
					<b>Click here for beer.</b></a><br/></td>
					<td><img src="/media_stat/images/layout/clear.gif" width="15" height="1" border="0" /></td>
					<td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0" /></td>
					<td><img src="/media_stat/images/layout/clear.gif" width="15" height="1" border="0" /></td>
					<td width="40%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr valign="top">
							<td>
							<a href="http://www.freshdirect.com/newproducts.jsp?trk=epicks01" class="featurePromo"><img src="/media/editorial/picks/promos/new_products_ad.gif" alt="NEW PRODUCTS!" width="160" height="33" border="0" />
							<br/><img src="/media_stat/images/layout/clear.gif" width="1" height="5" border="0" /><br/><b>Click here!</b></a>
							</td>
							<td align="center">
							<a href="http://www.freshdirect.com/newproducts.jsp?trk=epicks01"><img src="/media/editorial/picks/promos/new_star.gif" alt="NEW" width="52" height="51" border="0" /></a>
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


<MAP NAME="summer_email">
<AREA SHAPE="RECT" COORDS="0,32,202,76" HREF="http://www.freshdirect.com?trk=epicks01" ALT="Home"/>
<AREA SHAPE="RECT" COORDS="0,77,21,97" HREF="http://www.freshdirect.com?trk=epicks01" ALT="Home" />
<AREA SHAPE="RECT" COORDS="20,77,60,97" HREF="http://www.freshdirect.com/department.jsp?deptId=fru&amp;trk=epicks01" ALT="Fruit" />
<AREA SHAPE="RECT" COORDS="59,77,125,97" HREF="http://www.freshdirect.com/department.jsp?deptId=veg&amp;trk=epicks01" ALT="Vegetables" />
<AREA SHAPE="RECT" COORDS="124,77,158,98" HREF="http://www.freshdirect.com/department.jsp?deptId=mea&amp;trk=epicks01" ALT="Meat" />
<AREA SHAPE="RECT" COORDS="157,77,212,97" HREF="http://www.freshdirect.com/department.jsp?deptId=sea&amp;trk=epicks01" ALT="Seafood" />
<AREA SHAPE="RECT" COORDS="211,77,241,98" HREF="http://www.freshdirect.com/department.jsp?deptId=del&amp;trk=epicks01" ALT="Deli" />
<AREA SHAPE="RECT" COORDS="240,77,284,98" HREF="http://www.freshdirect.com/department.jsp?deptId=che&amp;trk=epicks01" ALT="Cheese" />
<AREA SHAPE="RECT" COORDS="283,77,321,98" HREF="http://www.freshdirect.com/department.jsp?deptId=dai&amp;trk=epicks01" ALT="Dairy" />
<AREA SHAPE="RECT" COORDS="320,77,364,98" HREF="http://www.freshdirect.com/department.jsp?deptId=cof&amp;trk=epicks01" ALT="Coffee" />
<AREA SHAPE="RECT" COORDS="363,77,390,97" HREF="http://www.freshdirect.com/department.jsp?deptId=tea&amp;trk=epicks01" ALT="Tea" />
<AREA SHAPE="RECT" COORDS="389,77,428,97" HREF="http://www.freshdirect.com/department.jsp?deptId=pas&amp;trk=epicks01" ALT="Pasta" />
<AREA SHAPE="RECT" COORDS="427,77,472,96" HREF="http://www.freshdirect.com/department.jsp?deptId=bak&amp;trk=epicks01" ALT="Bakery" />
<AREA SHAPE="RECT" COORDS="471,77,513,97" HREF="http://www.freshdirect.com/department.jsp?deptId=hmr&amp;trk=epicks01" ALT="Meals" />
<AREA SHAPE="RECT" COORDS="512,77,565,97" HREF="http://www.freshdirect.com/department.jsp?deptId=gro&amp;trk=epicks01" ALT="Grocery" />
<AREA SHAPE="RECT" COORDS="564,77,611,97" HREF="http://www.freshdirect.com/department.jsp?deptId=fro&amp;trk=epicks01" ALT="Frozen" />
<AREA SHAPE="RECT" COORDS="610,77,668,98" HREF="http://www.freshdirect.com/department.jsp?deptId=spe&amp;trk=epicks01" ALT="Specialty" />
<AREA SHAPE="RECT" COORDS="453,46,521,74" HREF="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks01" ALT="SUMMER PICKS" />
<AREA SHAPE="RECT" COORDS="523,45,600,75" HREF="http://www.freshdirect.com/your_account/manage_account.jsp?trk=epicks01" ALT="YOUR ACCOUNT" />
<AREA SHAPE="RECT" COORDS="609,40,664,75" HREF="http://www.freshdirect.com/help/index.jsp?trk=epicks01" ALT="GET HELP" />
<AREA SHAPE="RECT" COORDS="448,0,652,23" HREF="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks01" ALT="SUMMER PICKS" />
</MAP>

</xsl:template>

</xsl:stylesheet>
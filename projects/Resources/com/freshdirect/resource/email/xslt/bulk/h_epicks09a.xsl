<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:doc="http://xsltsl.org/xsl/documentation/1.0"
	xmlns:str="http://xsltsl.org/string"
	extension-element-prefixes="doc str">
	<xsl:output method="html"/>
	<xsl:include href='h_optout_footer.xsl'/>
	<xsl:include href='h_footer_v2.xsl'/>
	<xsl:include href='string.xsl' />
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">

<html>
	<head>
        <BASE href="http://www.freshdirect.com" />
		<title>Our Spring Picks</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
		<style type="text/css">
		.promoProduct    { font-size: 12px; color: #333333; font-family: Verdana, Arial, Helvetica, sans-serif; }
		a.promoProduct:link {color: #336600; text-decoration: underline; }
		a.promoProduct:visited {color: #336600; text-decoration: underline; }
		a.promoProduct:active {color: #FF9933; text-decoration: underline; }
		a.promoProduct:hover {color: #336600; text-decoration: underline; }
		.promoLink    { font-size: 10px; color: #333333; font-family: Verdana, Arial, Helvetica, sans-serif; }
		a.promoLink:link {color: #336600; text-decoration: underline; }
		a.promoLink:visited {color: #336600; text-decoration: underline; }
		a.promoLink:active {color: #FF9933; text-decoration: underline; }
		a.promoLink:hover {color: #336600; text-decoration: underline; }
		.mainLink    { font-size: 14px; color: #333333; font-family: Verdana, Arial, Helvetica, sans-serif; }
		a.mainLink:link {color: #336600; text-decoration: underline; }
		a.mainLink:visited {color: #336600; text-decoration: underline; }
		a.mainLink:active {color: #FF9933; text-decoration: underline; }
		a.mainLink:hover {color: #336600; text-decoration: underline; }
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
<table width="668" border="0" cellpadding="0" cellspacing="0" align="center">
	<tr>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="5" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="328" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="328" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="5" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/></td>
	</tr>
	<tr valign="bottom">
		<td colspan="3"><a href="http://www.freshdirect.com?trk=epicks09a"><img src="http://www.freshdirect.com/media_stat/images/logos/spring.gif" width="195" height="81" border="0" alt="FreshDirect"/></a></td>
		<td colspan="3" align="right">
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td><a href="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks09a"><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/nav_spring_picks.gif" width="68" height="26" border="0" alt="Spring Picks"/></a></td>
					<td><img src="http://www.freshdirect.com/media_stat/images/layout/999966.gif" width="1" height="24" hspace="6"/></td>
					<td><a href="http://www.freshdirect.com/your_account/manage_account.jsp?trk=epicks09a"><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/nav_your_account.gif" width="70" height="24" border="0" alt="Your Account"/></a></td>
					<td><img src="http://www.freshdirect.com/media_stat/images/layout/999966.gif" width="1" height="24" hspace="6"/></td>
					<td><a href="http://www.freshdirect.com/help/index.jsp?trk=epicks09a"><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/nav_get_help.gif" width="47" height="25" border="0" alt="Get Help"/></a></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td colspan="6"><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/nav_departments.gif" width="668" height="41" border="0" vspace="4" usemap="#departmentNav"/></td>
	</tr>
	<tr>
		<td colspan="2" rowspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/top_left_curve.gif" width="6" height="6"/></td>
		<td colspan="2" bgcolor="#999966"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/></td>
		<td colspan="2" rowspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/top_right_curve.gif" width="6" height="6"/></td>
	</tr>
	<tr>
		<td colspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="5"/></td>
	</tr>
	<tr>
		<td bgcolor="#999966"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/></td>
		<td colspan="4" align="center">
			<table width="517" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td colspan="5" align="center"><br/>
					<a href="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks09a"><img src="http://www.freshdirect.com/media/images/promotions/email/epicks09a.gif" width="372" height="39" vspace="5" border="0" alt="Our Spring Picks"/></a><br/>
					<img src="http://www.freshdirect.com/media/editorial/picks/spring_line.gif" width="517" height="14" vspace="8"/>
					</td>
				</tr>
				
				<tr valign="top" align="center">
					<td width="20%" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="20"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=br_strwbrry&amp;catId=br&amp;trk=epicks09a" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/fruit/berries/br_strwbrry_c.jpg" width="80" height="60" border="0" alt="Strawberries" vspace="3"/><br/><font color="#336600"><u>Strawberries</u></font></a><br/><b>$1.69/pkg</b></td>

					<td width="20%" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=grns_org_bbyspnpk&amp;catId=grns&amp;trk=epicks09a" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/vegetables/greenleafs/grns_org_bbyspnpk_c.jpg" width="70" height="70" border="0" alt="Baby Spinach, Organic, Packaged" vspace="3"/><br/><font color="#336600"><u>Baby Spinach, Organic, Packaged</u></font></a><br/><b>$4.29/ea</b></td>

					<td width="20%" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/><br/><a href="http://www.freshdirect.com/product.jsp?catId=lrstbnlss&amp;productId=lrst_leg_bndrldtd&amp;trk=epicks09a" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/meat/lamb/roast/lrst_leg_bndrldtd_c.jpg" width="70" height="70" border="0" alt="Boned, Rolled, &amp; Tied Leg of Lamb" vspace="3"/><br/><font color="#336600"><u>Boned, Rolled, &amp; Tied Leg of Lamb</u></font></a><br/><b>$6.69/lb</b></td>

					<td width="20%" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="9"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=hmr_hol_lglmb&amp;catId=hmr_ent_entree&amp;trk=epicks09a" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/meals/meat_entree/hmr_hol_lglmb_c.jpg" width="90" height="71" border="0" alt="Leg of Lamb with Sauce" vspace="3"/><br/><font color="#336600"><u>Leg of Lamb with Sauce</u></font></a><br/><b>$99.00/ea</b></td>

					<td width="20%" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="20"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=fflt_hlbt_flt&amp;catId=fflt&amp;trk=epicks09a" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/seafood/fish_fillets/fflt_hlbt_flt_c.jpg" width="80" height="60" border="0" alt="Halibut Fillet" vspace="3"/><br/><font color="#336600"><u>Halibut Fillet</u></font></a><br/><b>$14.99/lb</b></td>
				</tr>
				
				<tr>
					<td colspan="5"><br/></td>
				</tr>
				
				<tr valign="top" align="center">
					<td class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/><br/><a href="http://www.freshdirect.com/category.jsp?catId=gro_candy_choco&amp;prodCatId=gro_candy_choco&amp;productId=gro_choc_foilrbtsolid&amp;trk=epicks09a" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/grocery_five/gro_choc_foilrbtsolid_c.jpg" width="70" height="70" border="0" alt="Foil Solid Chocolate Sitting Rabbit" vspace="3"/><br/><font color="#336600"><u>Foil Solid Chocolate Sitting Rabbit</u></font></a><br/><b>$4.79/ea</b></td>
					
					<td class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/><br/><a href="http://www.freshdirect.com/category.jsp?catId=gro_candy_candy&amp;prodCatId=gro_candy_candy&amp;productId=gro_cadbury_crmegg&amp;trk=epicks09a" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/candy_fruit_nuts/candy/gro_cadbury_crmegg_c.jpg" width="70" height="70" border="0" alt="Cadbury Creme Eggs" vspace="3"/><br/><font color="#336600"><u><b>Cadbury</b><br/>Creme Eggs</u></font></a><br/><b>$0.99/ea</b></td>

					<td class="promoProduct"><a href="http://www.freshdirect.com/product.jsp?productId=cake_easter_cpcke&amp;catId=bak_cake_fd&amp;trk=epicks09a" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/pastry/cakes_cupcakes/cake_easter_cpcke_c.jpg" width="80" height="80" border="0" alt="Easter Cupcakes" vspace="3"/><br/><font color="#336600"><u>Easter Cupcakes</u></font></a><br/><b>$3.99/4pk</b></td>
					
					<td class="promoProduct"><a href="http://www.freshdirect.com/product.jsp?productId=ckibni_easter&amp;catId=bak_cookies_fd&amp;trk=epicks09a" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/pastry/cookies_brownies/ckibni_easter_c.jpg" width="80" height="80" border="0" alt="Easter Cookies" vspace="3"/><br/><font color="#336600"><u>Easter Cookies</u></font></a><br/><b>$3.99/7pk</b></td>

					<td class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/><br/><a href="http://www.freshdirect.com/category.jsp?catId=gro_candy_bulk&amp;prodCatId=gro_candy_bulk&amp;productId=gro_jelly_jbeans&amp;trk=epicks09a" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/candy_fruit_nuts/candy/gro_jelly_jbeans_c.jpg" width="70" height="70" border="0" alt="Large Traditional Jelly Beans" vspace="3"/><br/><font color="#336600"><u>Large Traditional Jelly Beans</u></font></a><br/><b>$0.79/ea</b></td>
				</tr>
				
				<tr>
					<td colspan="5" align="center"><br/>
					<a href="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks09a" class="mainLink"><b><u>Click here to see all of our Spring Picks!</u></b></a>
					<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/><br/><img src="http://www.freshdirect.com/media/editorial/picks/spring_line.gif" width="517" height="14" vspace="6"/></td>
				</tr>
				<tr>
					<td colspan="5">
						<table width="100%" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="266" height="4"/></td>
								<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="10" height="4"/></td>
								<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="4"/></td>
								<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="10" height="4"/></td>
								<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="230" height="4"/></td>
							</tr>
							<tr>
								<td align="center"><a href="http://www.freshdirect.com/category.jsp?catId=kosher_passover&amp;trk=epicks09a" class="fdFooter_s"><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/kosher_passover.jpg" width="255" height="74" border="0" alt="Kosher for Passover items"/><br/><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/kosher_passover.gif" width="254" height="31" border="0" vspace="5" alt="Shop from our Kosher for Passover section"/></a></td>
								<td rowspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/></td>
								<td rowspan="2" bgcolor="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/></td>
								<td rowspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/></td>
								<td rowspan="2"><a href="http://www.freshdirect.com/newproducts.jsp?trk=epicks09a" class="fdFooter_s"><img src="http://www.freshdirect.com/media/editorial/picks/new_products_ad.gif" width="160" height="33" alt="We're always adding new products!" vspace="4" border="0"/><br/><font color="#336600"><u><b>Click here!</b></u></font></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/cccccc.gif" width="230" height="1" vspace="18"/><br/><a href="mailto:web_ideas@freshdirect.com?subject=Website%20Suggestions!&amp;body=Please%20send website%20comments%20only.%20Feedback%20regarding%20orders,%20credits,%20or%20delivery%20should%20be%20sent%20to%20service@freshdirect.com%20or%20call%20toll%20free%201-866-279-2451." class="fdFooter_s"><img src="http://www.freshdirect.com/media/editorial/picks/suggestions.gif" width="143" height="30" alt="Website suggestions?" vspace="4" border="0"/><br/><font color="#336600"><u><b>Email our Editor.</b></u></font></a></td>
							</tr>
							<tr>
								<td align="center"><a href="http://www.freshdirect.com/category.jsp?catId=kosher_passover&amp;trk=epicks09a" class="fdFooter_s"><font color="#336600"><u><b>Click here to shop for<br/>Kosher for Passover items!</b></u></font></a></td>
							</tr>
						</table>
						<br/>
					</td>
				</tr>
			</table>
		</td>
		<td bgcolor="#999966"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/></td>
	</tr>
	<tr>
		<td colspan="2" rowspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/bottom_left_curve.gif" width="6" height="6"/></td>
		<td colspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="5"/></td>
		<td colspan="2" rowspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/bottom_right_curve.gif" width="6" height="6"/></td>
	</tr>
	<tr>
		<td colspan="2" bgcolor="#999966"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/></td>
	</tr>
	<tr>
		<td colspan="6">
		<img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/price_availability.gif" width="668" height="23" vspace="5"/><br/>
		<xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/><br/>
		</td>
	</tr>
</table>


<MAP NAME="departmentNav">
<AREA SHAPE="RECT" COORDS="0,0,30,39" HREF="http://www.freshdirect.com/index.jsp?trk=epicks09a" ALT="Home"/>
<AREA SHAPE="RECT" COORDS="31,1,70,21" HREF="http://www.freshdirect.com/department.jsp?deptId=fru&amp;trk=epicks09a" ALT="Fruit"/>
<AREA SHAPE="RECT" COORDS="71,1,139,21" HREF="http://www.freshdirect.com/department.jsp?deptId=veg&amp;trk=epicks09a" ALT="Vegetables"/>
<AREA SHAPE="RECT" COORDS="140,1,176,21" HREF="http://www.freshdirect.com/department.jsp?deptId=mea&amp;trk=epicks09a" ALT="Meat"/>
<AREA SHAPE="RECT" COORDS="177,1,233,21" HREF="http://www.freshdirect.com/department.jsp?deptId=sea&amp;trk=epicks09a" ALT="Seafood"/>
<AREA SHAPE="RECT" COORDS="234,1,264,21" HREF="http://www.freshdirect.com/department.jsp?deptId=del&amp;trk=epicks09a" ALT="Deli"/>
<AREA SHAPE="RECT" COORDS="265,1,308,21" HREF="http://www.freshdirect.com/department.jsp?deptId=che&amp;trk=epicks09a" ALT="Cheese"/>
<AREA SHAPE="RECT" COORDS="309,1,350,21" HREF="http://www.freshdirect.com/department.jsp?deptId=dai&amp;trk=epicks09a" ALT="Dairy"/>
<AREA SHAPE="RECT" COORDS="351,1,389,21" HREF="http://www.freshdirect.com/department.jsp?deptId=pas&amp;trk=epicks09a" ALT="Pasta"/>
<AREA SHAPE="RECT" COORDS="389,1,434,21" HREF="http://www.freshdirect.com/department.jsp?deptId=cof&amp;trk=epicks09a" ALT="Coffee"/>
<AREA SHAPE="RECT" COORDS="435,1,462,21" HREF="http://www.freshdirect.com/department.jsp?deptId=tea&amp;trk=epicks09a" ALT="Tea"/>
<AREA SHAPE="RECT" COORDS="463,1,561,21" HREF="http://www.freshdirect.com/department.jsp?deptId=bak&amp;trk=epicks09a" ALT="Bakery"/>
<AREA SHAPE="RECT" COORDS="562,1,655,21" HREF="http://www.freshdirect.com/department.jsp?deptId=hmr&amp;trk=epicks09a" ALT="Meals"/>
<AREA SHAPE="RECT" COORDS="29,22,90,41" HREF="http://www.freshdirect.com/department.jsp?deptId=gro&amp;trk=epicks09a" ALT="Grocery"/>
<AREA SHAPE="RECT" COORDS="91,22,152,41" HREF="http://www.freshdirect.com/department.jsp?deptId=spe&amp;trk=epicks09a" ALT="Specialty"/>
<AREA SHAPE="RECT" COORDS="153,22,204,41" HREF="http://www.freshdirect.com/department.jsp?deptId=fro&amp;trk=epicks09a" ALT="Frozen"/>
<AREA SHAPE="RECT" COORDS="202,22,304,41" HREF="http://www.freshdirect.com/department.jsp?deptId=hba&amp;trk=epicks09a" ALT="Health and Beauty"/>
<AREA SHAPE="RECT" COORDS="305,22,343,41" HREF="http://www.freshdirect.com/department.jsp?deptId=win&amp;trk=epicks09a" ALT="Wine"/>
<AREA SHAPE="RECT" COORDS="610,22,659,41" HREF="http://www.freshdirect.com/department.jsp?deptId=kos&amp;trk=epicks09a" ALT="Kosher"/>
</MAP>

</xsl:template>

</xsl:stylesheet>
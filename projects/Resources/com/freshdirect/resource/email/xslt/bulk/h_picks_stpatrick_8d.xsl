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
		<title>Our St. Patrick's Day Picks</title>
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
		<td colspan="3"><a href="http://www.freshdirect.com?trk=epicks08d"><img src="http://www.freshdirect.com/media_stat/images/logos/st_pats.gif" width="195" height="81" border="0"/></a></td>
		<td colspan="3" align="right">
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td><a href="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks08d"><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/nav_winter_picks.gif" width="62" height="24" border="0" alt="Winter Picks"/></a></td>
					<td><img src="http://www.freshdirect.com/media_stat/images/layout/999966.gif" width="1" height="24" hspace="6"/></td>
					<td><a href="http://www.freshdirect.com/your_account/manage_account.jsp?trk=epicks08d"><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/nav_your_account.gif" width="70" height="24" border="0" alt="Your Account"/></a></td>
					<td><img src="http://www.freshdirect.com/media_stat/images/layout/999966.gif" width="1" height="24" hspace="6"/></td>
					<td><a href="http://www.freshdirect.com/help/index.jsp?trk=epicks08d"><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/nav_get_help.gif" width="47" height="25" border="0" alt="Get Help"/></a></td>
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
					<a href="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks08d"><img src="http://www.freshdirect.com/media/images/promotions/picks_stpatrick_8d.gif" width="509" height="26" vspace="5" border="0"/></a><br/>
					<img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/clover_line.gif" width="517" height="14" vspace="8"/>
					</td>
				</tr>
				
				<tr valign="top" align="center">
					<td width="20%" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=bfruitnutherb_soda&amp;catId=bfruitnutherb&amp;trk=epicks08d" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/bakery/fruit_nut_herb/bfruitnutherb_soda_c.jpg" width="70" height="70" border="0" alt="Irish Soda Bread" vspace="3"/><br/><font color="#336600"><u>Irish Soda Bread</u></font></a><br/><b>$3.99/ea</b></td>

					<td width="20%" class="promoProduct"><a href="http://www.freshdirect.com/product.jsp?productId=ckibni_stpat&amp;catId=bak_cookies_fd&amp;trk=epicks08d" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/pastry/cookies_brownies/ckibni_stpat_c.jpg" width="80" height="80" border="0" alt="St Patrick's Day Cookies" vspace="3"/><br/><font color="#336600"><u>St Patrick's Day Cookies</u></font></a><br/><b>$3.99/7pk</b></td>

					<td width="20%" class="promoProduct"><a href="http://www.freshdirect.com/product.jsp?productId=cake_stpat_cpcke&amp;catId=bak_cake_fd&amp;trk=epicks08d" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/pastry/cakes_cupcakes/cake_stpat_cpcke_c.jpg" width="80" height="80" border="0" alt="St. Patrick's Day Cupcakes" vspace="3"/><br/><font color="#336600"><u>St. Patrick's Day Cupcakes</u></font></a><br/><b>$4.99/4pk</b></td>

					<td width="20%" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=ire_cashel_blu&amp;catId=blues&amp;trk=epicks08d" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/cheese/germany_israel/ire_cashel_blu_c.jpg" width="70" height="70" border="0" alt="Cashel Irish Blue Cheese" vspace="3"/><br/><font color="#336600"><u>Cashel Irish Blue Cheese</u></font></a><br/><b>$9.99/lb</b></td>

					<td width="20%" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="20"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=fflt_slmn_org&amp;catId=fflt&amp;trk=epicks08d" class="promoProduct"><img src="http://www1.dev.nyc1.freshdirect.com/media/images/product/seafood/fish_fillets/fflt_slmn_org_c.jpg" width="80" height="60" border="0" alt="Clare Island Irish Organic Salmon Fillet" vspace="3"/><br/><font color="#336600"><u><b>Clare Island</b><br/>Irish Organic Salmon Fillet</u></font></a><br/><b>$9.99/lb</b></td>
				</tr>
				
				<tr>
					<td colspan="5"><br/></td>
				</tr>
				
				<tr valign="top" align="center">
					<td class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="9"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=hmrmeat_lambstew_new&amp;catId=hmr_entree_meat&amp;trk=epicks08d" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/meals/meat_entree/hmrmeat_lambstew_c.jpg" width="90" height="71" border="0" alt="Lamb Stew" vspace="3"/><br/><font color="#336600"><u>Lamb Stew</u></font></a><br/><b>$8.99/ea</b></td>
					
					<td class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="9"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=hmrside_mshdpot&amp;catId=hmr_potside&amp;trk=epicks08d" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/meals/side_dish/hmrside_mshdpot_c.jpg" width="90" height="71" border="0" alt="Lamb Stew" vspace="3"/><br/><font color="#336600"><u>Mashed Potatoes</u></font></a><br/><b>$6.99/ea</b></td>

					<td class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=fl_ir_creme&amp;catId=cof_fld_reg&amp;trk=epicks08d" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/coffee/flavored/fl_ir_creme_c.jpg" width="70" height="70" border="0" alt="Irish Cr&#233;me Coffee" vspace="3"/><br/><font color="#336600"><u>Irish Cr&#233;me Coffee</u></font></a><br/><b>$4.99/lb</b></td>
					
					<td class="promoProduct"><a href="http://www.freshdirect.com/category.jsp?catId=gro_cerea&amp;groceryVirtual=gro_cerea&amp;brandValue=bd_lucky_charms&amp;prodCatId=gro_cerea_kids&amp;productId=gro_lucky_general_01&amp;trk=epicks08d" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/grocery/gro_lucky_general_01_c.jpg" width="80" height="80" border="0" alt="General Mills Lucky Charms Cereal" vspace="3"/><br/><font color="#336600"><u><b>General Mills</b><br/>Lucky Charms Cereal</u></font></a><br/><b>$4.39/ea</b></td>

					<td class="promoProduct"><a href="http://www.freshdirect.com/category.jsp?catId=gro_beer_impor&amp;prodCatId=gro_beer_impor&amp;productId=beer_guinness_pub_fourcan&amp;trk=epicks08d" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/grocery/gro_guinness_pub_drau_01_c.jpg" width="80" height="80" border="0" alt="Guinness Pub Draught" vspace="3"/><br/><font color="#336600"><u><b>Guinness</b><br/>Pub Draught</u></font></a><br/><b>$7.99/4pk</b></td>
				</tr>
				
				<tr>
					<td colspan="5" align="center"><br/>
					<a href="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks08d" class="mainLink"><b><u>Click here to see all of our St. Patrick's Day suggestions!</u></b></a><br/>
					<br/><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/clover_line.gif" width="517" height="14" vspace="8"/></td>
				</tr>
				<tr>
					<td colspan="5">
						<table width="100%" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="266" height="10"/></td>
								<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="10" height="10"/></td>
								<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/></td>
								<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="10" height="10"/></td>
								<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="230" height="10"/></td>
							</tr>
							<tr>
								<td align="center"><a href="http://www.freshdirect.com/category.jsp?catId=kosher_seafood&amp;trk=epicks08d" class="fdFooter_s"><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/kosher_seafood.gif" width="264" height="45" border="0"/><br/>
								<img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/kosher_fishes.jpg" width="266" height="34" border="0" vspace="4"/></a></td>
								<td rowspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/></td>
								<td rowspan="2" bgcolor="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/></td>
								<td rowspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/></td>
								<td rowspan="2"><a href="http://www.freshdirect.com/newproducts.jsp?trk=epicks08d" class="promoLink"><img src="http://www.freshdirect.com/media/editorial/picks/new_products_ad.gif" width="160" height="33" alt="We're always adding new products!" vspace="4" border="0"/><br/><font color="#336600"><u><b>Click here!</b></u></font></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/cccccc.gif" width="230" height="1" vspace="18"/><br/><a href="mailto:web_ideas@freshdirect.com?subject=Website%20Suggestions!&amp;body=Please%20send website%20comments%20only.%20Feedback%20regarding%20orders,%20credits,%20or%20delivery%20should%20be%20sent%20to%20service@freshdirect.com%20or%20call%20toll%20free%201-866-279-2451." class="promoLink"><img src="http://www.freshdirect.com/media/editorial/picks/suggestions.gif" width="143" height="30" alt="Website suggestions?" vspace="4" border="0"/><br/><font color="#336600"><u><b>Email our Editor.</b></u></font></a></td>
							</tr>
							<tr>
								<td align="center" class="fdFooter_s">Check out our full line of your favorite Kosher fish &#8212; all hand-cut and wrapped to order under the watchful eye of OU and KAJ supervision.<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="6"/><br/><a href="http://www.freshdirect.com/category.jsp?catId=kosher_seafood&amp;trk=epicks08d" class="fdFooter_s"><font color="#336600"><u>Click here for Kosher Seafood.</u></font></a></td>
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
<AREA SHAPE="RECT" COORDS="0,0,30,39" HREF="http://www.freshdirect.com/index.jsp?trk=epicks08d" ALT="Home"/>
<AREA SHAPE="RECT" COORDS="31,1,70,21" HREF="http://www.freshdirect.com/department.jsp?deptId=fru&amp;trk=epicks08d" ALT="Fruit"/>
<AREA SHAPE="RECT" COORDS="71,1,139,21" HREF="http://www.freshdirect.com/department.jsp?deptId=veg&amp;trk=epicks08d" ALT="Vegetables"/>
<AREA SHAPE="RECT" COORDS="140,1,176,21" HREF="http://www.freshdirect.com/department.jsp?deptId=mea&amp;trk=epicks08d" ALT="Meat"/>
<AREA SHAPE="RECT" COORDS="177,1,233,21" HREF="http://www.freshdirect.com/department.jsp?deptId=sea&amp;trk=epicks08d" ALT="Seafood"/>
<AREA SHAPE="RECT" COORDS="234,1,264,21" HREF="http://www.freshdirect.com/department.jsp?deptId=del&amp;trk=epicks08d" ALT="Deli"/>
<AREA SHAPE="RECT" COORDS="265,1,308,21" HREF="http://www.freshdirect.com/department.jsp?deptId=che&amp;trk=epicks08d" ALT="Cheese"/>
<AREA SHAPE="RECT" COORDS="309,1,350,21" HREF="http://www.freshdirect.com/department.jsp?deptId=dai&amp;trk=epicks08d" ALT="Dairy"/>
<AREA SHAPE="RECT" COORDS="351,1,389,21" HREF="http://www.freshdirect.com/department.jsp?deptId=pas&amp;trk=epicks08d" ALT="Pasta"/>
<AREA SHAPE="RECT" COORDS="389,1,434,21" HREF="http://www.freshdirect.com/department.jsp?deptId=cof&amp;trk=epicks08d" ALT="Coffee"/>
<AREA SHAPE="RECT" COORDS="435,1,462,21" HREF="http://www.freshdirect.com/department.jsp?deptId=tea&amp;trk=epicks08d" ALT="Tea"/>
<AREA SHAPE="RECT" COORDS="463,1,561,21" HREF="http://www.freshdirect.com/department.jsp?deptId=bak&amp;trk=epicks08d" ALT="Bakery"/>
<AREA SHAPE="RECT" COORDS="562,1,655,21" HREF="http://www.freshdirect.com/department.jsp?deptId=hmr&amp;trk=epicks08d" ALT="Meals"/>
<AREA SHAPE="RECT" COORDS="29,22,90,41" HREF="http://www.freshdirect.com/department.jsp?deptId=gro&amp;trk=epicks08d" ALT="Grocery"/>
<AREA SHAPE="RECT" COORDS="91,22,152,41" HREF="http://www.freshdirect.com/department.jsp?deptId=spe&amp;trk=epicks08d" ALT="Specialty"/>
<AREA SHAPE="RECT" COORDS="153,22,204,41" HREF="http://www.freshdirect.com/department.jsp?deptId=fro&amp;trk=epicks08d" ALT="Frozen"/>
<AREA SHAPE="RECT" COORDS="202,22,304,41" HREF="http://www.freshdirect.com/department.jsp?deptId=hba&amp;trk=epicks08d" ALT="Health and Beauty"/>
<AREA SHAPE="RECT" COORDS="305,22,343,41" HREF="http://www.freshdirect.com/department.jsp?deptId=win&amp;trk=epicks08d" ALT="Wine"/>
<AREA SHAPE="RECT" COORDS="610,22,659,41" HREF="http://www.freshdirect.com/department.jsp?deptId=kos&amp;trk=epicks08d" ALT="Kosher"/>
</MAP>

</xsl:template>

</xsl:stylesheet>
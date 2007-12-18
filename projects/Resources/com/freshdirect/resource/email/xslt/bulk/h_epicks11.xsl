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
		<title>Memorial Day Picks</title>
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
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="25"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="5" height="25"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="328" height="25"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="328" height="25"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="5" height="25"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="25"/></td>
	</tr>
	<tr valign="bottom">
		<td colspan="3"><a href="http://www.freshdirect.com?trk=epicks11"><img src="http://www.freshdirect.com/media_stat/images/logos/fd_logo_sm_gl_nv.gif" width="195" height="38" border="0" alt="FreshDirect"/></a></td>
		<td colspan="3" align="right">
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td><a href="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks11"><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/nav_spring_picks.gif" width="68" height="26" border="0" alt="Spring Picks"/></a></td>
					<td><img src="http://www.freshdirect.com/media_stat/images/layout/999966.gif" width="1" height="24" hspace="6"/></td>
					<td><a href="http://www.freshdirect.com/your_account/manage_account.jsp?trk=epicks11"><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/nav_your_account.gif" width="70" height="24" border="0" alt="Your Account"/></a></td>
					<td><img src="http://www.freshdirect.com/media_stat/images/layout/999966.gif" width="1" height="24" hspace="6"/></td>
					<td><a href="http://www.freshdirect.com/help/index.jsp?trk=epicks11"><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/nav_get_help.gif" width="47" height="25" border="0" alt="Get Help"/></a></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td colspan="6"><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/nav_all.gif" width="668" height="41" border="0" vspace="4" usemap="#departmentNav"/></td>
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
			<table width="518" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td colspan="5" align="center">
					<a href="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks11"><img src="http://www.freshdirect.com/media/images/promotions/email/epicks11.gif" width="474" height="102" vspace="5" border="0" alt="Memorial Day Picks"/></a><br/>
					<img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/sun_line.gif" width="517" height="12" vspace="4"/><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="4"/>
					</td>
				</tr>
				
				<tr valign="top" align="center">
					<td width="20%" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=bstk_rbeye_bnls&amp;catId=bgril&amp;trk=epicks11" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/meat/beef/steaks/bstk_rbeye_bnls_c.jpg" width="70" height="70" border="0" alt="Rib Eye Steak" vspace="3"/><br/><font color="#336600"><u>Rib Eye Steak</u></font></a><br/><b>$9.99/lb</b></td>

					<td width="20%" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=bgrnd_ptty_rnd&amp;catId=bptty&amp;trk=epicks11" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/meat/beef/groundbeef/bgrnd_ptty_rnd_c.jpg" width="70" height="70" border="0" alt="Fresh Round Hamburgers" vspace="3"/><br/><font color="#336600"><u>Fresh Round Hamburgers</u></font></a><br/><b>$2.99/lb</b></td>

					<td width="20%" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=ptrk_ptty&amp;catId=mt_trky&amp;trk=epicks11" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/meat/poultry/turkey/ptrk_ptty_c.jpg" width="70" height="70" border="0" alt="Fresh Turkey Burgers" vspace="3"/><br/><font color="#336600"><u>Fresh Turkey Burgers</u></font></a><br/><b>$4.99/lb</b></td>

					<td width="20%" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="11"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=ffss_shrmp_kbob&amp;catId=ready_to&amp;trk=epicks11" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/seafood/fish_fillets/ffss_shrmp_kbob_c.jpg" width="80" height="60" border="0" alt="Shrimp Kabobs" vspace="3"/><br/><font color="#336600"><u>Shrimp Kabobs</u></font></a><br/><b>$11.99/3pk</b></td>

					<td width="20%" class="promoProduct"><a href="http://www.freshdirect.com/product.jsp?productId=hmr_kbob_veg3pk&amp;catId=cut_veg&amp;trk=epicks11" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/meals/grill_packs/hmr_kbob_veg3pk_c.jpg" width="90" height="71" border="0" alt="Vegetable Kabobs" vspace="3"/><br/><font color="#336600"><u>Vegetable Kabobs</u></font></a><br/><b>$9.99/3pk</b></td>
				</tr>
				
				<tr>
					<td colspan="5"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="5"/></td>
				</tr>
				
				<tr valign="top" align="center">
					<td class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="30"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=mln_wtr_sdls&amp;catId=mln&amp;trk=epicks11" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/fruit/melons/mln_wtr_sdls_c.jpg" width="70" height="70" border="0" alt="Seedless Watermelon" vspace="3"/><br/><font color="#336600"><u>Seedless Watermelon</u></font></a><br/><b>$0.39/lb</b></td>
					
					<td class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="20"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=pietrt_mxbrry&amp;catId=bak_pie_fd&amp;trk=epicks11" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/pastry/pies_tarts/pietrt_mxbrry_c.jpg" width="80" height="80" border="0" alt="Seasonal Fruit Tart" vspace="3"/><br/><font color="#336600"><u>Seasonal Fruit Tart</u></font></a><br/><b>$12.99/ea</b></td>

					<td class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="20"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=cake_rwbcup&amp;catId=bak_cake_fd&amp;trk=epicks11" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/bakery/cake/cake_rwbcup_c.jpg" width="80" height="80" border="0" alt="Star-Spangled Cupcakes" vspace="3"/><br/><font color="#336600"><u>Star-Spangled Cupcakes</u></font></a><br/><b>$3.99/4pk</b></td>
					
					<td class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="20"/><br/><a href="http://www.freshdirect.com/category.jsp?catId=gro_beer_domes&amp;prodCatId=gro_beer_domes&amp;productId=beer_brooklyn_pilsner_sxbt&amp;trk=epicks11" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/grocery_two/beer_brooklyn_pilsner_sxbt_c.jpg" width="80" height="80" border="0" alt="Brooklyn Brewery Pilsner" vspace="3"/><br/><font color="#336600"><u><b>Brooklyn Brewery</b><br/>Pilsner</u></font></a><br/><b>$7.99/6pk</b></td>

					<td class="promoProduct"><a href="http://www.freshdirect.com/product.jsp?productId=wine_bc_domlau_pin&amp;catId=win_fresh&amp;trk=epicks11" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/wine/wine_bc_domlau_pin_a.jpg" width="43" height="100" border="0" alt="Domaine des Lauriers Picpoul de Pinet" vspace="3"/><br/><font color="#336600"><u>Domaine des Lauriers Picpoul de Pinet</u></font></a><br/><b>$9.50/ea</b></td>
				</tr>
				
				<tr>
					<td colspan="5" align="center"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="15"/><br/>
					<a href="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks11" class="mainLink"><font color="#336600"><b><u>Click here to see all of our Memorial Day Picks!</u></b></font></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/><br/><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/sun_line.gif" width="517" height="12" vspace="6"/></td>
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
								<td align="center" class="fdFooter_s"><a href="http://www.freshdirect.com/department.jsp?deptId=cat&amp;trk=epicks11"><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/catering.gif" width="255" height="24" border="0" vspace="2" alt="Catering Now Open!"/><br/><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/catering.jpg" width="255" height="50" border="0" vspace="4" alt="Catering Platters"/></a><br/>We've created the perfect platters for entertaining, parties, and business meetings.<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="3"/><br/>
<a href="http://www.freshdirect.com/department.jsp?deptId=hba&amp;trk=epicks11" class="fdFooter_s"><font color="#336600">Click here to see our menu!</font></a></td>
								<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/></td>
								<td bgcolor="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/></td>
								<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/></td>
								<td><a href="http://www.freshdirect.com/newproducts.jsp?trk=epicks11" class="fdFooter_s"><img src="http://www.freshdirect.com/media/editorial/picks/new_products_ad.gif" width="160" height="33" alt="We're always adding new products!" vspace="4" border="0"/><br/><font color="#336600"><u><b>Click here!</b></u></font></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/cccccc.gif" width="230" height="1" vspace="18"/><br/><a href="mailto:web_ideas@freshdirect.com?subject=Website%20Suggestions!&amp;body=Please%20send website%20comments%20only.%20Feedback%20regarding%20orders,%20credits,%20or%20delivery%20should%20be%20sent%20to%20service@freshdirect.com%20or%20call%20toll%20free%201-866-279-2451." class="fdFooter_s"><img src="http://www.freshdirect.com/media/editorial/picks/suggestions.gif" width="143" height="30" alt="Website suggestions?" vspace="4" border="0"/><br/><font color="#336600"><u><b>Email our Editor.</b></u></font></a></td>
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
<AREA SHAPE="RECT" COORDS="0,0,30,39" HREF="http://www.freshdirect.com/index.jsp?trk=epicks11" ALT="Home"/>
<AREA SHAPE="RECT" COORDS="31,1,70,21" HREF="http://www.freshdirect.com/department.jsp?deptId=fru&amp;trk=epicks11" ALT="Fruit"/>
<AREA SHAPE="RECT" COORDS="71,1,139,21" HREF="http://www.freshdirect.com/department.jsp?deptId=veg&amp;trk=epicks11" ALT="Vegetables"/>
<AREA SHAPE="RECT" COORDS="140,1,176,21" HREF="http://www.freshdirect.com/department.jsp?deptId=mea&amp;trk=epicks11" ALT="Meat"/>
<AREA SHAPE="RECT" COORDS="177,1,233,21" HREF="http://www.freshdirect.com/department.jsp?deptId=sea&amp;trk=epicks11" ALT="Seafood"/>
<AREA SHAPE="RECT" COORDS="234,1,264,21" HREF="http://www.freshdirect.com/department.jsp?deptId=del&amp;trk=epicks11" ALT="Deli"/>
<AREA SHAPE="RECT" COORDS="265,1,308,21" HREF="http://www.freshdirect.com/department.jsp?deptId=che&amp;trk=epicks11" ALT="Cheese"/>
<AREA SHAPE="RECT" COORDS="309,1,350,21" HREF="http://www.freshdirect.com/department.jsp?deptId=dai&amp;trk=epicks11" ALT="Dairy"/>
<AREA SHAPE="RECT" COORDS="351,1,389,21" HREF="http://www.freshdirect.com/department.jsp?deptId=pas&amp;trk=epicks11" ALT="Pasta"/>
<AREA SHAPE="RECT" COORDS="389,1,434,21" HREF="http://www.freshdirect.com/department.jsp?deptId=cof&amp;trk=epicks11" ALT="Coffee"/>
<AREA SHAPE="RECT" COORDS="435,1,462,21" HREF="http://www.freshdirect.com/department.jsp?deptId=tea&amp;trk=epicks11" ALT="Tea"/>
<AREA SHAPE="RECT" COORDS="463,1,561,21" HREF="http://www.freshdirect.com/department.jsp?deptId=bak&amp;trk=epicks11" ALT="Bakery"/>
<AREA SHAPE="RECT" COORDS="562,1,655,21" HREF="http://www.freshdirect.com/department.jsp?deptId=hmr&amp;trk=epicks11" ALT="Meals"/>
<AREA SHAPE="RECT" COORDS="29,22,90,41" HREF="http://www.freshdirect.com/department.jsp?deptId=gro&amp;trk=epicks11" ALT="Grocery"/>
<AREA SHAPE="RECT" COORDS="91,22,152,41" HREF="http://www.freshdirect.com/department.jsp?deptId=spe&amp;trk=epicks11" ALT="Specialty"/>
<AREA SHAPE="RECT" COORDS="153,22,204,41" HREF="http://www.freshdirect.com/department.jsp?deptId=fro&amp;trk=epicks11" ALT="Frozen"/>
<AREA SHAPE="RECT" COORDS="202,22,304,41" HREF="http://www.freshdirect.com/department.jsp?deptId=hba&amp;trk=epicks11" ALT="Health and Beauty"/>
<AREA SHAPE="RECT" COORDS="305,22,343,41" HREF="http://www.freshdirect.com/department.jsp?deptId=win&amp;trk=epicks11" ALT="Wine"/>
<AREA SHAPE="RECT" COORDS="545,21,596,41" HREF="http://www.freshdirect.com/department.jsp?deptId=kos&amp;trk=epicks11" ALT="Kosher"/>
<AREA SHAPE="RECT" COORDS="596,20,658,41" HREF="http://www.freshdirect.com/department.jsp?deptId=cat&amp;trk=epicks11" ALT="Catering"/>
</MAP>

</xsl:template>

</xsl:stylesheet>
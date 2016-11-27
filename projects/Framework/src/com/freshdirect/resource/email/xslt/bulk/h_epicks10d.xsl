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
		<title>Our Knicks Picks</title>
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
		<td colspan="3"><a href="http://www.freshdirect.com?trk=epicks10d"><img src="http://www.freshdirect.com/media_stat/images/logos/spring.gif" width="195" height="81" border="0" alt="FreshDirect"/></a></td>
		<td colspan="3" align="right">
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td><a href="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks10d"><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/nav_spring_picks.gif" width="68" height="26" border="0" alt="Spring Picks"/></a></td>
					<td><img src="http://www.freshdirect.com/media_stat/images/layout/999966.gif" width="1" height="24" hspace="6"/></td>
					<td><a href="http://www.freshdirect.com/your_account/manage_account.jsp?trk=epicks10d"><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/nav_your_account.gif" width="70" height="24" border="0" alt="Your Account"/></a></td>
					<td><img src="http://www.freshdirect.com/media_stat/images/layout/999966.gif" width="1" height="24" hspace="6"/></td>
					<td><a href="http://www.freshdirect.com/help/index.jsp?trk=epicks10d"><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/nav_get_help.gif" width="47" height="25" border="0" alt="Get Help"/></a></td>
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
					<a href="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks10d"><img src="http://www.freshdirect.com/media/images/promotions/email/epicks10d.gif" width="487" height="60" vspace="5" border="0" alt="Go NY Go! Our Knicks Picks"/></a><br/>
					<img src="http://www.freshdirect.com/media/images/promotions/knicks_line.gif" width="522" height="16" vspace="4"/><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="4"/>
					</td>
				</tr>
				
				<tr valign="top" align="center">
					<td width="20%" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="9"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=hmr_hero_amer&amp;catId=hmr_ent_entree&amp;trk=epicks10d" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/meals/heros/hmr_hero_amer_c.jpg" width="90" height="71" border="0" alt="All American Super Hero" vspace="3"/><br/><font color="#336600"><u>All American Super Hero</u></font></a><br/><b>$49.99/ea</b></td>

					<td width="20%" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="9"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=hmr_cater_shrmctl&amp;catId=hmr_apps&amp;trk=epicks10d" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/meals/appetizers/hmr_cater_shrmctl_c.jpg" width="90" height="71" border="0" alt="Party Size Shrimp Cocktail" vspace="3"/><br/><font color="#336600"><u>Party Size Shrimp Cocktail</u></font></a><br/><b>$44.99/ea</b></td>

					<td width="20%" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="9"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=hmr_app_minisprng&amp;catId=hmr_ent_happs&amp;trk=epicks10d" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/meals/appetizers/hmr_app_minisprng_c.jpg" width="90" height="71" border="0" alt="Mini Spring Rolls with Dipping Sauce" vspace="3"/><br/><font color="#336600"><u>Mini Spring Rolls with Dipping Sauce</u></font></a><br/><b>$13.99/ea</b></td>

					<td width="20%" class="promoProduct"><a href="http://www.freshdirect.com/category.jsp?catId=gro_beer_domes&amp;prodCatId=gro_beer_domes&amp;productId=beer_brooklyn_pilsner_sxbt&amp;trk=epicks10d" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/grocery_two/beer_brooklyn_pilsner_sxbt_c.jpg" width="80" height="80" border="0" alt="Brooklyn Brewery Pilsner" vspace="3"/><br/><font color="#336600"><u><b>Brooklyn Brewery</b><br/>Pilsner</u></font></a><br/><b>$7.49/ea</b></td>

					<td width="20%" class="promoProduct"><a href="http://www.freshdirect.com/category.jsp?catId=gro_bever&amp;groceryVirtual=gro_bever&amp;brandValue=bd_pepsi&amp;prodCatId=gro_bever_soda_cola&amp;productId=gro_pepsi_cola02&amp;trk=epicks10d" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/grocery_five/gro_pepsi_cola02_c.jpg" width="80" height="80" border="0" alt="Pepsi" vspace="3"/><br/><font color="#336600"><u><b>Pepsi</b></u></font></a><br/><b>$2.99/ea</b></td>
				</tr>
				
				<tr>
					<td colspan="5"><br/></td>
				</tr>
				
				<tr valign="top" align="center">
					<td class="promoProduct"><a href="http://www.freshdirect.com/category.jsp?catId=gro_snack_chips_potat&amp;prodCatId=gro_snack_chips_potat&amp;productId=spe_terra_blues_01&amp;trk=epicks10d" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/specialty/spe_terra_blues_01_c.jpg" width="80" height="80" border="0" alt="Terra Blues Potato Chips" vspace="3"/><br/><font color="#336600"><u><b>Terra</b><br/>Blues Potato Chips</u></font></a><br/><b>$2.79/ea</b></td>
					
					<td class="promoProduct"><a href="http://www.freshdirect.com/category.jsp?catId=gro_snack_chips_corn&amp;prodCatId=gro_snack_chips_corn&amp;productId=spe_tostitos_bluecrn&amp;trk=epicks10d" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/grocery_two/spe_tostitos_bluecrn_c.jpg" width="80" height="80" border="0" alt="Tostitos Natural Blue Corn Tortilla Chips" vspace="3"/><br/><font color="#336600"><u><b>Tostitos</b><br/>Natural Blue Corn Tortilla Chips</u></font></a><br/><b>$2.99/ea</b></td>

					<td class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="9"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=hmrap_dpchp_cater&amp;catId=hmr_apps&amp;trk=epicks10d" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/meals/appetizers/hmrap_dpchp_cater_c.jpg" width="90" height="71" border="0" alt="Guacamole, Salsa and Tortilla Chips" vspace="3"/><br/><font color="#336600"><u>Guacamole, Salsa and Tortilla Chips</u></font></a><br/><b>$18.99/ea</b></td>
					
					<td class="promoProduct"><a href="http://www.freshdirect.com/category.jsp?catId=gro_snack_salsa&amp;prodCatId=gro_snack_salsa&amp;productId=gro_muir_g_salsa_m_01&amp;trk=epicks10d" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/grocery/gro_muir_g_salsa_m_01_c.jpg" width="80" height="80" border="0" alt="Muir Glen Organic Salsa" vspace="3"/><br/><font color="#336600"><u><b>Muir Glen</b><br/>Organic Salsa - Medium</u></font></a><br/><b>$2.59/ea</b></td>

					<td class="promoProduct"><a href="http://www.freshdirect.com/product.jsp?productId=cake_cheese&amp;catId=bak_cake_fd&amp;trk=epicks10d" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/pastry/cakes_cupcakes/cake_cheese_c.jpg" width="80" height="80" border="0" alt="FreshDirect New York Cheesecake" vspace="3"/><br/><font color="#336600"><u>New York Cheesecake</u></font></a><br/><b>$10.99/ea</b></td>
				</tr>
				
				<tr>
					<td colspan="5" align="center"><br/>
					<a href="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks10d" class="mainLink"><b><u>Click here to see all of our Knicks Picks!</u></b></a>
					<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/><br/><img src="http://www.freshdirect.com/media/images/promotions/knicks_line.gif" width="522" height="16" vspace="6"/></td>
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
								<td align="center"><a href="http://www.freshdirect.com/department.jsp?deptId=hba&amp;trk=epicks10d" class="fdFooter_s"><img src="http://www.freshdirect.com/media/editorial/picks/now_avail_hba.gif" width="254" height="51" border="0" alt="Now Available - Health &amp; Beauty Products"/><br/><img src="http://www.freshdirect.com/media/editorial/picks/hba_4products.jpg" width="254" height="65" border="0" vspace="6" alt="Health &amp; Beauty Products"/></a></td>
								<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/></td>
								<td bgcolor="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/></td>
								<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/></td>
								<td><a href="http://www.freshdirect.com/newproducts.jsp?trk=epicks10d" class="fdFooter_s"><img src="http://www.freshdirect.com/media/editorial/picks/new_products_ad.gif" width="160" height="33" alt="We're always adding new products!" vspace="4" border="0"/><br/><font color="#336600"><u><b>Click here!</b></u></font></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/cccccc.gif" width="230" height="1" vspace="18"/><br/><a href="mailto:web_ideas@freshdirect.com?subject=Website%20Suggestions!&amp;body=Please%20send website%20comments%20only.%20Feedback%20regarding%20orders,%20credits,%20or%20delivery%20should%20be%20sent%20to%20service@freshdirect.com%20or%20call%20toll%20free%201-866-279-2451." class="fdFooter_s"><img src="http://www.freshdirect.com/media/editorial/picks/suggestions.gif" width="143" height="30" alt="Website suggestions?" vspace="4" border="0"/><br/><font color="#336600"><u><b>Email our Editor.</b></u></font></a></td>
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
<AREA SHAPE="RECT" COORDS="0,0,30,39" HREF="http://www.freshdirect.com/index.jsp?trk=epicks10d" ALT="Home"/>
<AREA SHAPE="RECT" COORDS="31,1,70,21" HREF="http://www.freshdirect.com/department.jsp?deptId=fru&amp;trk=epicks10d" ALT="Fruit"/>
<AREA SHAPE="RECT" COORDS="71,1,139,21" HREF="http://www.freshdirect.com/department.jsp?deptId=veg&amp;trk=epicks10d" ALT="Vegetables"/>
<AREA SHAPE="RECT" COORDS="140,1,176,21" HREF="http://www.freshdirect.com/department.jsp?deptId=mea&amp;trk=epicks10d" ALT="Meat"/>
<AREA SHAPE="RECT" COORDS="177,1,233,21" HREF="http://www.freshdirect.com/department.jsp?deptId=sea&amp;trk=epicks10d" ALT="Seafood"/>
<AREA SHAPE="RECT" COORDS="234,1,264,21" HREF="http://www.freshdirect.com/department.jsp?deptId=del&amp;trk=epicks10d" ALT="Deli"/>
<AREA SHAPE="RECT" COORDS="265,1,308,21" HREF="http://www.freshdirect.com/department.jsp?deptId=che&amp;trk=epicks10d" ALT="Cheese"/>
<AREA SHAPE="RECT" COORDS="309,1,350,21" HREF="http://www.freshdirect.com/department.jsp?deptId=dai&amp;trk=epicks10d" ALT="Dairy"/>
<AREA SHAPE="RECT" COORDS="351,1,389,21" HREF="http://www.freshdirect.com/department.jsp?deptId=pas&amp;trk=epicks10d" ALT="Pasta"/>
<AREA SHAPE="RECT" COORDS="389,1,434,21" HREF="http://www.freshdirect.com/department.jsp?deptId=cof&amp;trk=epicks10d" ALT="Coffee"/>
<AREA SHAPE="RECT" COORDS="435,1,462,21" HREF="http://www.freshdirect.com/department.jsp?deptId=tea&amp;trk=epicks10d" ALT="Tea"/>
<AREA SHAPE="RECT" COORDS="463,1,561,21" HREF="http://www.freshdirect.com/department.jsp?deptId=bak&amp;trk=epicks10d" ALT="Bakery"/>
<AREA SHAPE="RECT" COORDS="562,1,655,21" HREF="http://www.freshdirect.com/department.jsp?deptId=hmr&amp;trk=epicks10d" ALT="Meals"/>
<AREA SHAPE="RECT" COORDS="29,22,90,41" HREF="http://www.freshdirect.com/department.jsp?deptId=gro&amp;trk=epicks10d" ALT="Grocery"/>
<AREA SHAPE="RECT" COORDS="91,22,152,41" HREF="http://www.freshdirect.com/department.jsp?deptId=spe&amp;trk=epicks10d" ALT="Specialty"/>
<AREA SHAPE="RECT" COORDS="153,22,204,41" HREF="http://www.freshdirect.com/department.jsp?deptId=fro&amp;trk=epicks10d" ALT="Frozen"/>
<AREA SHAPE="RECT" COORDS="202,22,304,41" HREF="http://www.freshdirect.com/department.jsp?deptId=hba&amp;trk=epicks10d" ALT="Health and Beauty"/>
<AREA SHAPE="RECT" COORDS="305,22,343,41" HREF="http://www.freshdirect.com/department.jsp?deptId=win&amp;trk=epicks10d" ALT="Wine"/>
<AREA SHAPE="RECT" COORDS="610,22,659,41" HREF="http://www.freshdirect.com/department.jsp?deptId=kos&amp;trk=epicks10d" ALT="Kosher"/>
</MAP>

</xsl:template>

</xsl:stylesheet>
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
		<title>Kosher for Passover Picks</title>
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
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="20"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="5" height="20"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="328" height="20"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="328" height="20"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="5" height="20"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="20"/></td>
	</tr>
	<tr valign="bottom">
		<td colspan="3"><a href="http://www.freshdirect.com?trk=passover04"><img src="http://www.freshdirect.com/media_stat/images/logos/fd_logo_sm_gl_nv.gif" width="195" height="38" border="0" alt="FreshDirect"/></a></td>
		<td colspan="3" align="right">
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td><a href="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=passover04"><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/nav_spring_picks.gif" width="68" height="26" border="0" alt="Spring Picks"/></a></td>
					<td><img src="http://www.freshdirect.com/media_stat/images/layout/999966.gif" width="1" height="24" hspace="6"/></td>
					<td><a href="http://www.freshdirect.com/your_account/manage_account.jsp?trk=passover04"><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/nav_your_account.gif" width="70" height="24" border="0" alt="Your Account"/></a></td>
					<td><img src="http://www.freshdirect.com/media_stat/images/layout/999966.gif" width="1" height="24" hspace="6"/></td>
					<td><a href="http://www.freshdirect.com/help/index.jsp?trk=passover04"><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/nav_get_help.gif" width="47" height="25" border="0" alt="Get Help"/></a></td>
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
					<a href="http://www.freshdirect.com/category.jsp?catId=kosher_passover&amp;trk=passover04"><img src="http://www.freshdirect.com/media/images/promotions/email/passover04.gif" width="453" height="36" vspace="5" border="0" alt="Kosher for Passover Picks"/></a><br/>
					<img src="http://www.freshdirect.com/media_stat/images/template/email/kosher/picks_line.gif" width="522" height="20" vspace="8"/>
					</td>
				</tr>
				
				<tr valign="top" align="center">
					<td width="20%" class="promoProduct"><a href="http://www.freshdirect.com/category.jsp?catId=kosh_pass_bever&amp;prodCatId=kosh_pass_bever&amp;productId=gro_kedem_concj_1&amp;trk=passover04" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/grocery_five/gro_kedem_concj_1_c.jpg" width="80" height="80" border="0" alt="Kedem Concord Grape Juice" vspace="3"/><br/><font color="#336600"><u><b>Kedem</b><br/>Concord Grape Juice</u></font></a><br/><b>$4.99/ea</b></td>

					<td width="20%" class="promoProduct"><a href="http://www.freshdirect.com/category.jsp?catId=kosh_pass_cjfish&amp;prodCatId=kosh_pass_cjfish&amp;productId=gro_kedem_gefilte&amp;trk=passover04" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/grocery_five/gro_kedem_gefilte_c.jpg" width="80" height="80" border="0" alt="Kedem Gourmet Gefilte Fish" vspace="3"/><br/><font color="#336600"><u><b>Kedem</b><br/>Gourmet Gefilte Fish</u></font></a><br/><b>$4.99/ea</b></td>

					<td width="20%" class="promoProduct"><a href="http://www.freshdirect.com/category.jsp?catId=kosh_pess_matzo&amp;prodCatId=kosh_pess_matzo&amp;productId=gro_osem_matzeg&amp;trk=passover04" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/grocery_five/gro_osem_matzeg_c.jpg" width="80" height="80" border="0" alt="Osem Egg Matzah" vspace="3"/><br/><font color="#336600"><u><b>Osem</b><br/>Egg Matzah</u></font></a><br/><b>$3.29/ea</b></td>

					<td width="20%" class="promoProduct"><a href="http://www.freshdirect.com/category.jsp?catId=kosher_grocery_cereal&amp;prodCatId=kosher_grocery_cereal&amp;productId=gro_tas_frostos&amp;trk=passover04" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/grocery_five/gro_tas_frostos_c.jpg" width="80" height="80" border="0" alt="Ta's Frosted Crispy O's Cereal" vspace="3"/><br/><font color="#336600"><u><b>Ta's</b><br/>Frosted Crispy O's Cereal</u></font></a><br/><b>$3.29/ea</b></td>

					<td width="20%" class="promoProduct"><a href="http://www.freshdirect.com/category.jsp?catId=kosh_pass_cookie&amp;prodCatId=kosh_pass_cookie&amp;productId=gro_gefen_cocoroon&amp;trk=passover04" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/grocery_five/gro_gefen_cocoroon_c.jpg" width="80" height="80" border="0" alt="Gefen Coconut Flavored Macaroons" vspace="3"/><br/><font color="#336600"><u><b>Gefen</b><br/>Coconut Flavored Macaroons</u></font></a><br/><b>$2.99/ea</b></td>
				</tr>
				
				<tr>
					<td colspan="5"><br/></td>
				</tr>
				
				<tr valign="top" align="center">
					<td class="promoProduct"><a href="http://www.freshdirect.com/category.jsp?catId=kosh_pass_bever&amp;prodCatId=kosh_pass_bever&amp;productId=spe_ceres_apricot_01&amp;trk=passover04" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/specialty/spe_ceres_apricot_01_c.jpg" width="80" height="80" border="0" alt="Ceres Apricot Juice" vspace="3"/><br/><font color="#336600"><u><b>Ceres</b><br/>Apricot Juice</u></font></a><br/><b>$2.49/ea</b></td>
					
					<td class="promoProduct"><a href="http://www.freshdirect.com/category.jsp?catId=kosh_pass_condi&amp;prodCatId=kosh_pass_condi&amp;productId=gro_kedem_sfspred&amp;trk=passover04" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/grocery_five/gro_kedem_sfspred_c.jpg" width="80" height="80" border="0" alt="Kedem Sugar Free Grape Preserves" vspace="3"/><br/><font color="#336600"><u><b>Kedem</b><br/>Sugar Free Grape Preserves</u></font></a><br/><b>$3.99/ea</b></td>

					<td class="promoProduct"><a href="http://www.freshdirect.com/category.jsp?catId=kosh_pass_fruveg&amp;prodCatId=kosh_pass_fruveg&amp;productId=gro_gefen_orngseg&amp;trk=passover04" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/grocery_five/gro_gefen_orngseg_c.jpg" width="80" height="80" border="0" alt="Gefen Mandarin Orange Segments in Light Syrup" vspace="3"/><br/><font color="#336600"><u><b>Gefen</b><br/>Mandarin Orange Segments in Light Syrup</u></font></a><br/><b>$0.99/ea</b></td>
					
					<td class="promoProduct"><a href="http://www.freshdirect.com/category.jsp?catId=kosh_pass_bever&amp;prodCatId=kosh_pass_bever&amp;productId=gro_sweet_decaftea&amp;trk=passover04" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/grocery_five/gro_sweet_decaftea_c.jpg" width="80" height="80" border="0" alt="Swee-Touch-Nee No-Caffeine Tea Bags" vspace="3"/><br/><font color="#336600"><u><b>Swee-Touch-Nee</b><br/>No-Caffeine Tea Bags</u></font></a><br/><b>$1.49/ea</b></td>

					<td class="promoProduct"><a href="http://www.freshdirect.com/category.jsp?catId=kosh_pass_candy&amp;prodCatId=kosh_pass_candy&amp;productId=gro_alprose_bitchoc&amp;trk=passover04" class="promoProduct"><img src="http://www.freshdirect.com/media/images/product/grocery_five/gro_alprose_bitchoc_c.jpg" width="80" height="80" border="0" alt="Alprose Napolitains Miniature Bite-Size Assorted Milk Chocolates" vspace="3"/><br/><font color="#336600"><u><b>Alprose</b><br/>Napolitains Miniature Bite-Size Assorted Milk Chocolates</u></font></a><br/><b>$4.99/ea</b></td>
				</tr>
				
				<tr>
					<td colspan="5" align="center"><br/>
					<a href="http://www.freshdirect.com/category.jsp?catId=kosher_passover&amp;trk=passover04" class="mainLink"><b><u>Click here to see all of our Kosher for Passover items!</u></b></a>
					<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/><br/><img src="http://www.freshdirect.com/media_stat/images/template/email/kosher/picks_line.gif" width="522" height="20" vspace="6"/></td>
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
								<td align="center"><a href="http://www.freshdirect.com/category.jsp?catId=kosher_seafood&amp;trk=passover04" class="fdFooter_s"><img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/kosher_fish.gif" width="256" height="43" border="0" alt="Now available, Kosher Fish!"/><br/>
								<img src="http://www.freshdirect.com/media_stat/images/template/email/seasonal/kosher_fishes.jpg" width="266" height="34" border="0" vspace="4" alt="Kosher Fish"/></a></td>
								<td rowspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/></td>
								<td rowspan="2" bgcolor="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/></td>
								<td rowspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/></td>
								<td rowspan="2"><a href="http://www.freshdirect.com/newproducts.jsp?trk=passover04" class="fdFooter_s"><img src="http://www.freshdirect.com/media/editorial/picks/new_products_ad.gif" width="160" height="33" alt="We're always adding new products!" vspace="4" border="0"/><br/><font color="#336600"><u><b>Click here!</b></u></font></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/cccccc.gif" width="230" height="1" vspace="18"/><br/><a href="mailto:web_ideas@freshdirect.com?subject=Website%20Suggestions!&amp;body=Please%20send website%20comments%20only.%20Feedback%20regarding%20orders,%20credits,%20or%20delivery%20should%20be%20sent%20to%20service@freshdirect.com%20or%20call%20toll%20free%201-866-279-2451." class="fdFooter_s"><img src="http://www.freshdirect.com/media/editorial/picks/suggestions.gif" width="143" height="30" alt="Website suggestions?" vspace="4" border="0"/><br/><font color="#336600"><u><b>Email our Editor.</b></u></font></a></td>
							</tr>
							<tr>
								<td align="center" class="fdFooter_s">Check out our full line of your favorite Kosher fish &#8212; all hand-cut and wrapped to order under the watchful eye of OU and KAJ supervision.<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="6"/><br/><a href="http://www.freshdirect.com/category.jsp?catId=kosher_seafood&amp;trk=passover04" class="fdFooter_s"><font color="#336600"><u><b>Click here for Kosher Fish.</b></u></font></a></td>
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
<AREA SHAPE="RECT" COORDS="0,0,30,39" HREF="http://www.freshdirect.com/index.jsp?trk=passover04" ALT="Home"/>
<AREA SHAPE="RECT" COORDS="31,1,70,21" HREF="http://www.freshdirect.com/department.jsp?deptId=fru&amp;trk=passover04" ALT="Fruit"/>
<AREA SHAPE="RECT" COORDS="71,1,139,21" HREF="http://www.freshdirect.com/department.jsp?deptId=veg&amp;trk=passover04" ALT="Vegetables"/>
<AREA SHAPE="RECT" COORDS="140,1,176,21" HREF="http://www.freshdirect.com/department.jsp?deptId=mea&amp;trk=passover04" ALT="Meat"/>
<AREA SHAPE="RECT" COORDS="177,1,233,21" HREF="http://www.freshdirect.com/department.jsp?deptId=sea&amp;trk=passover04" ALT="Seafood"/>
<AREA SHAPE="RECT" COORDS="234,1,264,21" HREF="http://www.freshdirect.com/department.jsp?deptId=del&amp;trk=passover04" ALT="Deli"/>
<AREA SHAPE="RECT" COORDS="265,1,308,21" HREF="http://www.freshdirect.com/department.jsp?deptId=che&amp;trk=passover04" ALT="Cheese"/>
<AREA SHAPE="RECT" COORDS="309,1,350,21" HREF="http://www.freshdirect.com/department.jsp?deptId=dai&amp;trk=passover04" ALT="Dairy"/>
<AREA SHAPE="RECT" COORDS="351,1,389,21" HREF="http://www.freshdirect.com/department.jsp?deptId=pas&amp;trk=passover04" ALT="Pasta"/>
<AREA SHAPE="RECT" COORDS="389,1,434,21" HREF="http://www.freshdirect.com/department.jsp?deptId=cof&amp;trk=passover04" ALT="Coffee"/>
<AREA SHAPE="RECT" COORDS="435,1,462,21" HREF="http://www.freshdirect.com/department.jsp?deptId=tea&amp;trk=passover04" ALT="Tea"/>
<AREA SHAPE="RECT" COORDS="463,1,561,21" HREF="http://www.freshdirect.com/department.jsp?deptId=bak&amp;trk=passover04" ALT="Bakery"/>
<AREA SHAPE="RECT" COORDS="562,1,655,21" HREF="http://www.freshdirect.com/department.jsp?deptId=hmr&amp;trk=passover04" ALT="Meals"/>
<AREA SHAPE="RECT" COORDS="29,22,90,41" HREF="http://www.freshdirect.com/department.jsp?deptId=gro&amp;trk=passover04" ALT="Grocery"/>
<AREA SHAPE="RECT" COORDS="91,22,152,41" HREF="http://www.freshdirect.com/department.jsp?deptId=spe&amp;trk=passover04" ALT="Specialty"/>
<AREA SHAPE="RECT" COORDS="153,22,204,41" HREF="http://www.freshdirect.com/department.jsp?deptId=fro&amp;trk=passover04" ALT="Frozen"/>
<AREA SHAPE="RECT" COORDS="202,22,304,41" HREF="http://www.freshdirect.com/department.jsp?deptId=hba&amp;trk=passover04" ALT="Health and Beauty"/>
<AREA SHAPE="RECT" COORDS="305,22,343,41" HREF="http://www.freshdirect.com/department.jsp?deptId=win&amp;trk=passover04" ALT="Wine"/>
<AREA SHAPE="RECT" COORDS="610,22,659,41" HREF="http://www.freshdirect.com/department.jsp?deptId=kos&amp;trk=passover04" ALT="Kosher"/>
</MAP>

</xsl:template>

</xsl:stylesheet>
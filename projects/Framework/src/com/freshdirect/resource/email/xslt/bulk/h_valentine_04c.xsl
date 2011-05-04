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
		<title>Valentine's Day Ideas from FreshDirect!</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
		<style type="text/css">
		
		.picks    { font-size: 18px; color: #336600; font-weight: bold; font-family: Arial, Helvetica, sans-serif; }
		.picks.link {color: #336600; text-decoration: underline; }
		a.picks:link {color: #336600; text-decoration: underline; }
		a.picks:visited {color: #336600; text-decoration: underline; }
		a.picks:active {color: #FF9933; text-decoration: underline; }
		a.picks:hover {color: #336600; text-decoration: underline; }
		
		.vdayHeader    { font-size: 13px; color: #CC0000; font-weight: bold; font-family: Verdana, Arial, Helvetica, sans-serif; }
		a.vdayHeader:link {color: #CC0000; text-decoration: none; }
		a.vdayHeader:visited {color: #CC0000; text-decoration: none; }
		a.vdayHeader:active {color: #FF6699; text-decoration: none; }
		a.vdayHeader:hover {color: #CC0000; text-decoration: underline; }
		
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
<body bgcolor="#FFFFFF" text="#333333" marginheight="0" topmargin="0" marginwidth="0" leftmargin="0">
	<xsl:call-template name="mail_body" />
</body>
</html>

</xsl:template>

<xsl:template name="mail_body">
<table width="630" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" align="center" class="promoProduct">
	<tr>
		<td colspan="7" align="center"><br/><a href="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=vday_04c"><img src="http://www.freshdirect.com/media_stat/images/logos/valentine.gif" width="195" height="81" border="0" alt="FreshDirect"/><br/><img src="http://www.freshdirect.com/media/images/promotions/valentine_04c.gif" width="612" height="39" vspace="6" border="0" alt="Valentine's Day Ideas from FreshDirect."/></a><br/>
	<img src="http://www.freshdirect.com/media_stat/images/template/email/valentine/heart_line.gif" width="628" height="7" vspace="2"/><br/><br/>
		</td>
	</tr>	
	
	<tr valign="top">
		<td colspan="3"><a href="http://www.freshdirect.com/category.jsp?catId=hmr_val&amp;trk=vday_04c"><img src="http://www.freshdirect.com/media_stat/images/template/email/valentine/vday_subhead.gif" width="310" height="60" border="0" alt="What could be more sexy and fun than teasing your partner with long stem strawberries dipped in melted chocolate?"/></a></td>
		<td rowspan="6"></td>
		<td rowspan="6" bgcolor="#CCCCCC"></td>
		<td rowspan="6"></td>
		<td rowspan="6" class="promoProduct_s">
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td colspan="4" class="vdayHeader">
					<a href="http://www.freshdirect.com/product.jsp?productId=usa_cupidcam_hv&amp;catId=camem&amp;trk=vday_04c" class="vdayHeader"><font color="#CC0000"><b>HUDSON VALLEY CUPID'S<br/>CHOICE CAMEMBERT - 4oz - $5.99</b></font></a>
					<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="4"/>
					</td>
				</tr>
				<tr valign="top">
					<td class="promoProduct_s">
					A sweet and buttery blend of sheep's milk, cow's milk, and fresh cream. Very rich and mild, with a white rind that blooms naturally as the cheese ripens. Available for a limited time.
					<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8"/><br/>
					<a href="http://www.freshdirect.com/product.jsp?productId=usa_cupidcam_hv&amp;catId=camem&amp;trk=vday_04c" class="promoProduct"><font color="#336600"><u><b>Click here to learn more!</b></u></font></a>
					<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="18"/>
					</td>
					<td colspan="3"><a href="http://www.freshdirect.com/product.jsp?productId=usa_cupidcam_hv&amp;catId=camem&amp;trk=vday_04c"><img src="http://www.freshdirect.com/media_stat/images/template/email/valentine/hv_cupidcam.jpg" width="95" height="95" border="0" alt="Hudson Valley Cupid's Choice Camembert" /></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="18"/></td>
				</tr>
				<tr bgcolor="#CCCCCC">
					<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="182" height="1"/></td>
					<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="15" height="1"/></td>
					<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="15" height="1"/></td>
					<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="70" height="1"/></td>
				</tr>
				
				<tr>
					<td colspan="4" class="vdayHeader"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="14"/><br/>
					<a href="http://www.freshdirect.com/product.jsp?productId=wine_bc_veuvecliquot_1&amp;catId=win_fizzy&amp;trk=vday_04c" class="vdayHeader"><font color="#CC0000"><b>VEUVE CLICQUOT PONSARDIN VINTAGE<br/>RESERVE 1996 - $49.00/ea</b></font></a>
					<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="4"/>
					</td>
				</tr>
				
				<tr valign="top">
					<td colspan="3" class="promoProduct_s">
					Crafted from a blend of Premier and Grand Cru vineyards, VC's Pinot Noir-led Vintage Reserve presses all the right buttons. On the palate, citrus and stone fruits mingle with toasted nuts and fresh-baked brioche. What's not to like?
					<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8"/><br/>
					<a href="http://www.freshdirect.com/product.jsp?productId=wine_bc_veuvecliquot_1&amp;catId=win_fizzy&amp;trk=vday_04c" class="promoProduct"><font color="#336600"><u><b>Click here to learn more</b></u></font></a> 
					<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="12"/>
					</td>
					<td><a href="http://www.freshdirect.com/product.jsp?productId=wine_bc_veuvecliquot_1&amp;catId=win_fizzy&amp;trk=vday_04c"><img src="http://www.freshdirect.com/media_stat/images/template/email/valentine/bc_veuvecliquot.jpg" width="64" height="150" border="0" alt="Veuve Clicquot Ponsardin Vintage Reserve 1996" /></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="14"/></td>
				</tr>
				
				<tr>
					<td colspan="4" bgcolor="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1"/></td>
				</tr>
				
				<tr>
					<td colspan="4" class="vdayHeader"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="14"/><br/>
					<a href="http://www.freshdirect.com/product.jsp?productId=cake_val_cpcke&amp;catId=bak_cake_fd&amp;trk=vday_04c" class="vdayHeader"><font color="#CC0000"><b>VALENTINE'S DAY CUPCAKES - 4PK - $4.99</b></font></a>
					<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="4"/>
					</td>
				</tr>
				
				<tr valign="top">
					<td colspan="2" class="promoProduct_s">
					Our moist vanilla and chocolate cupcakes get all ready for Valentine's Day with thick vanilla frosting and romantically colored candy hearts.
					<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8"/><br/>
					<a href="http://www.freshdirect.com/product.jsp?productId=cake_val_cpcke&amp;catId=bak_cake_fd&amp;trk=vday_04c" class="promoProduct"><font color="#336600"><u><b>Click here to learn more</b></u></font></a>
					</td>
					<td colspan="2"><a href="http://www.freshdirect.com/product.jsp?productId=cake_val_cpcke&amp;catId=bak_cake_fd&amp;trk=vday_04c"><img src="http://www.freshdirect.com/media_stat/images/template/email/valentine/vday_cupcake.jpg" width="80" height="80" border="0" alt="Valentine's Day Cupcakes" /></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="6"/></td>
				
				</tr>
			</table>
		</td>
	</tr>
	
	<tr><td colspan="3"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8"/></td></tr>
	
	<tr valign="top">
		<td><a href="http://www.freshdirect.com/category.jsp?catId=hmr_val&amp;trk=vday_04c"><img src="http://www.freshdirect.com/media_stat/images/template/email/valentine/fondue_strawberry.jpg" width="97" height="116" border="0" alt="Chocolate Fondue with Strawberries" /></a></td>
		<td colspan="2" class="promoProduct_s">Forget about last-minute reservations, over-priced restaurants, or preparing dinner for Valentine's Day. This year stay at home and let our chef prepare a romantic fondue dinner for you and your Valentine. We'll bring everything to your door (even the next morning's breakfast). All you have to do is heat, dip and enjoy.<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="12"/>
</td>
	</tr>
	
	<tr><td colspan="3"><a href="http://www.freshdirect.com/product.jsp?productId=hmr_fonbox_che&amp;catId=hmr_fondue_cheese&amp;trk=vday_04c" class="promoProduct"><font color="#336600"><u><b>Classic Cheese Fondue for Two</b></u></font></a></td></tr>
	
	<tr valign="top">
		<td colspan="2" class="promoProduct_s">
		<b>$79.99 w/fondue pot</b><br/>
		$49.99 food only<br/>
		<br/>
		<a href="http://www.freshdirect.com/product.jsp?productId=hmr_fonbox_sea&amp;catId=hmr_fondue_seafood&amp;trk=vday_04c" class="promoProduct"><font color="#336600"><u><b>Seafood Fondue for Two</b></u></font></a><br/>
		<b>$99.99 w/fondue pot</b><br/>
		$69.99 food only<br/>
		<br/>
		<a href="http://www.freshdirect.com/product.jsp?productId=hmr_fonbox_meat&amp;catId=hmr_fondue_meat&amp;trk=vday_04c" class="promoProduct"><font color="#336600"><u><b>Meat Fondue for Two</b></u></font></a><br/>
		<b>$119.99 w/fondue pot</b><br/>
		$89.99 food only<br/>
		<br/>
		<a href="http://www.freshdirect.com/product.jsp?productId=hmr_fonbox_surf&amp;catId=hmr_fondue_surf&amp;trk=vday_04c" class="promoProduct"><font color="#336600"><u><b>Surf n' Turf Fondue for Two</b></u></font></a><br/>
		<b>$129.99 w/fondue pot</b><br/>
		$99.99 food only<br/>
		<img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="14"/>
		</td>
		<td><br/><a href="http://www.freshdirect.com/category.jsp?catId=hmr_val&amp;trk=vday_04c"><img src="http://www.freshdirect.com/media_stat/images/template/email/valentine/fondue_04.jpg" width="133" height="105" border="0" vspace="10" alt="FreshDirect's Valentine's Day Fondue"/></a></td>
	</tr>
	
	<tr>
		<td colspan="3" class="promoProduct_s">
		<span class="vdayHeader">ALL OUR FONDUE DINNERS ALSO INCLUDE:</span>
		<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="2"/><br/>
		Fondue pot, Caesar salad, chocolate fondue dessert, and even breakfast for the next morning (fresh orange juice, croissants, coffee, and fresh fruit). 
		<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="6"/><br/>
		<a href="http://www.freshdirect.com/category.jsp?catId=hmr_val&amp;trk=vday_04c" class="promoProduct"><font color="#336600"><u><b>Click here for more details about our Fondue Dinners!</b></u></font></a>
		<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="12"/><br/>
		<a href="http://www.freshdirect.com/category.jsp?catId=hmr_val&amp;trk=vday_04c"><img src="http://www.freshdirect.com/media_stat/images/template/email/valentine/fondue_seafood.jpg" width="300" height="54" border="0" alt="FreshDirect's Valentine's Day Fondue"/></a>
		</td>
	</tr>
	
	<tr><td colspan="7" align="center"><br/><img src="http://www.freshdirect.com/media_stat/images/template/email/valentine/heart_line.gif" width="628" height="7"/><br/><img src="http://www.freshdirect.com/media_stat/images/template/email/valentine/more_vday_picks.gif" width="282" height="26" vspace="15" alt="More Valentine's Day picks."/></td>
	</tr>

	<tr><td colspan="7" align="center">
	
		<table width="100%" celpadding="0" cellspacing="0" border="0">
			<tr valign="top" align="center">
				<td width="105" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=bstk_flet_dfat&amp;catId=bgril&amp;trk=vday_04c"><img src="http://www.freshdirect.com/media/images/product/meat/beef/steaks/bstk_flet_dfat_c.jpg" width="70" height="70" border="0" alt="Filet Mignon"/><br/><font color="#336600"><u>Filet Mignon</u></font></a><br/><b>$17.99/lb</b></td>
				
				<td width="105" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="20"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=slbsr_lbstr_live&amp;catId=slbsr&amp;trk=vday_04c"><img src="http://www.freshdirect.com/media/images/product/seafood/lobster/slbsr_lbstr_live_c.jpg" width="80" height="60" border="0" alt="Live Lobsters"/><br/><font color="#336600"><u>Live<br/>Lobsters</u></font></a><br/><b>$9.99/lb</b></td>
				
				<td width="105" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=chclt_strwbrry&amp;catId=bak_cookies_fd&amp;trk=vday_04c"><img src="http://www.freshdirect.com/media/images/product/pastry/chocolates/chclt_strwbrry_c.jpg" width="70" height="70" border="0" alt="FreshDirect Dark Chocolate-Dipped Strawberries"/><br/><font color="#336600"><u><b>FreshDirect</b><br/>Dark<br/>Chocolate-Dipped Strawberries</u></font></a><br/><b>$7.49/6pk</b></td>
				
				<td width="105" class="promoProduct"><a href="http://www.freshdirect.com/category.jsp?catId=fro_icecr_ice_candy&amp;prodCatId=fro_icecr_ice_candy&amp;productId=fro_godiva_pecan_ca_01&amp;trk=vday_04c"><img src="http://www.freshdirect.com/media/images/product/frozen/fro_godiva_pecan_ca_01_c.jpg" width="80" height="80" border="0" alt="Godiva Vanilla with Chocolate Caramel Hearts Ice Cream"/><br/><font color="#336600"><u><b>Godiva</b><br/>Vanilla with Chocolate Caramel Hearts Ice Cream</u></font></a><br/><b>$2.59/ea</b></td>
				
				<td width="105" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=ckibni_val&amp;catId=bak_cookies_fd&amp;trk=vday_04c"><img src="http://www.freshdirect.com/media/images/product/pastry/cookies_brownies/ckibni_val_c.jpg" width="70" height="70" border="0" alt="Valentine's Day Cookies"/><br/><font color="#336600"><u>Valentine's Day Cookies</u></font></a><br/><b>$3.99/6pk</b></td>
				
				<td width="105" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=pate_dar_msetrf&amp;catId=pate&amp;trk=vday_04c"><img src="http://www.freshdirect.com/media/images/product/deli/pate/pate_dar_msetrf_c.jpg" width="70" height="70" border="0" alt="D'Artagnan Mousse Truffee"/><br/><font color="#336600"><u><b>D'Artagnan</b><br/>Mousse Truffee</u></font></a><br/><b>$6.99/ea</b></td>
			</tr>
			
			<tr><td colspan="6"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8"/></td></tr>

			<tr valign="top" align="center">
				<td class="promoProduct"></td>
				<td class="promoProduct"><a href="http://www.freshdirect.com/product.jsp?productId=wine_bc_hopeestate_1&amp;catId=win_luscious&amp;trk=vday_04c"><img src="http://www.freshdirect.com/media/images/product/wine/wine_bc_hopeestate_1_a.jpg" width="43" height="100" border="0" alt="Great White Hope Hope Estate Chardonnay &#34;Broke&#34; Vineyard 2002"/><br/><font color="#336600"><u><b>Great White Hope</b><br/>Hope Estate Chardonnay "Broke" Vineyard 2002</u></font></a><br/><b>$13.00/ea</b></td>
				
				<td class="promoProduct"><a href="http://www.freshdirect.com/product.jsp?productId=wine_bc_chartogne_1&amp;catId=win_beyond&amp;trk=vday_04c"><img src="http://www.freshdirect.com/media/images/product/wine/wine_bc_chartogne_1_a.jpg" width="43" height="100" border="0" alt="Chartogne Taillet &#34;Cuv&#233;e Fiacre&#34; 1996"/><br/><font color="#336600"><u>Chartogne Taillet "Cuv&#233;e Fiacre" 1996</u></font></a><br/><b>$48.00/ea</b></td>
				
				<td class="promoProduct"><a href="http://www.freshdirect.com/product.jsp?productId=wine_bc_elcopero_1&amp;catId=win_juicy&amp;trk=vday_04c"><img src="http://www.freshdirect.com/media/images/product/wine/wine_bc_elcopero_1_a.jpg" width="43" height="100" border="0" alt="A Lotta Rosado El Copero Rosado 2002"/><br/><font color="#336600"><u><b>A Lotta Rosado</b><br/>El Copero Rosado 2002</u></font></a><br/><b>$8.00/ea</b></td>
				
				<td class="promoProduct"><a href="http://www.freshdirect.com/product.jsp?productId=wine_bc_chatvieux_1&amp;catId=win_beyond&amp;trk=vday_04c"><img src="http://www.freshdirect.com/media/images/product/wine/wine_bc_chatvieux_1_a.jpg" width="43" height="100" border="0" alt="Domaine de Vieux Lazaret Ch&#226;teauneuf du Pape 2001"/><br/><font color="#336600"><u>Domaine de Vieux Lazaret Ch&#226;teauneuf du Pape 2001</u></font></a><br/><b>$23.00/ea</b></td>
				<td class="promoProduct"></td>
			</tr>
		</table>
		
		</td>
	</tr>
	
	<tr><td colspan="7" align="center"><br/><br/><a href="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=vday_04c" class="picks"><font color="#336600"><u><b>Click here for all of our Valentine's Day Suggestions!</b></u></font></a><br/><br/><br/></td></tr>
	
	<tr><td colspan="7" align="center"><img src="http://www.freshdirect.com/media_stat/images/template/email/valentine/heart_line.gif" width="628" height="7"/></td></tr>
	
	<tr> 
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="108" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="92" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="133" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="6" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="8" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="282" height="10"/></td>
	</tr>
	
	<tr>
		<td colspan="7" align="center" class="promoProduct_s">
		Price and availability subject to change. Please see Web site for current 
	    prices and availability.<br/>
	    <a href="http://www.freshdirect.com?trk=vday_04c"><img src="http://www.freshdirect.com/media_stat/images/template/email/logo_url.gif" width="264" height="35" vspace="6" border="0" alt="www.FreshDirect.com"/></a><br/>
	    <img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8"/>
		</td>
	</tr>
	<tr><td colspan="7"><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/><br/><br/></td></tr>
</table>
</xsl:template>

</xsl:stylesheet>
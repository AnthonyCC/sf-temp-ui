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
		<title>Mother's Day ideas from FreshDirect</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
		<style type="text/css">
          .mdayHeader    { font-size: 16px; color: #FF99CC; font-weight: bold; font-family: Arial, Helvetica, sans-serif; }
		.mdaySubheader    { font-size: 15px; color: #9966CC; font-weight: bold; font-family:Arial, Helvetica, sans-serif; }
          .mdayBody    { font-size: 11px; color: #333333; font-family: Verdana, Arial, Helvetica, sans-serif; }
          .featureProduct  { font-size: 13px; color: #336600; font-family: Verdana, Arial, Helvetica, sans-serif; }
          .featureProduct.link {color: #336600; text-decoration: none; }
		a.featureProduct:link {color: #336600; text-decoration: none; }
		a.featureProduct:visited {color: #336600; text-decoration: none; }
		a.featureProduct:active {color: #FF99CC; text-decoration: none; }
		a.featureProduct:hover {color: #336600; text-decoration: none; }
		.promoProduct    { font-size: 11px; color: #336600; font-family: Verdana, Arial, Helvetica, sans-serif; }
		.promoProduct.link {color: #336600; text-decoration: none; }
		a.promoProduct:link {color: #336600; text-decoration: none; }
		a.promoProduct:visited {color: #336600; text-decoration: none; }
		a.promoProduct:active {color: #FF99CC; text-decoration: none; }
		a.promoProduct:hover {color: #336600; text-decoration: none; }
		</style>
	</head>
<body bgcolor="#FFFFFF" text="#333333" size="1" marginwidth="20" marginheight="18" leftmargin="20" topmargin="18">
	<xsl:call-template name="mail_body" />
</body>
</html>

</xsl:template>

<xsl:template name="mail_body">

<table cellpadding="0" cellspacing="0" border="0" width="100%">
     <tr>
          <td colspan="5" align="center"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/mothersday_hdr.gif" width="438" height="111" alt="Mother's Day ideas from FreshDirect." /></td>
     </tr>
     <tr><td colspan="5"><br/></td></tr>
     <tr><td colspan="5" background="http://www.freshdirect.com/media_stat/images/template/email/mother/little_flower.gif"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="100%" height="21" /></td>
     </tr>
     <tr><td colspan="5"><br/></td></tr>
     <tr valign="top">
			<td width="48%" valign="top" align="center">
				<table cellpadding="0" cellspacing="5" border="0" width="90%">
				<tr>
                    <td align="center" colspan="3" class="mdaySubheader"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/sunday_brunch.gif" width="261" height="33" /><br/>This year honor your Mother with a home-cooked breakfast.</td>
                    </tr>
				<tr valign="top" align="center">
				<td width="30%" class="mdayBody"><a href="http://www.freshdirect.com/product.jsp?productId=crsnt_bttr&amp;catId=roll&amp;cmpgn=mday&amp;trk=mday" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/p_crois_bttr.jpg" width="70" height="70" border="0" /><br/>Butter Croissants</a><br/><b>$3.49/4pk</b><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="14" /></td>
				<td width="30%" class="mdayBody"><a href="http://www.freshdirect.com/product.jsp?productId=crsnt_chc&amp;catId=roll&amp;cmpgn=mday&amp;trk=mday" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/p_crois_choc.jpg" width="70" height="70" border="0" /><br/>Chocolate Croissants</a><br/><b>$3.99/4pk</b><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="14" /></td>
                    <td width="30%" class="mdayBody"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8" /><br/><a href="http://www.freshdirect.com/product.jsp?productId=ctfru_seasonsld&amp;catId=cut_fruit&amp;cmpgn=mday&amp;trk=mday" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/p_cutfruit.jpg" width="62" height="62" border="0" /><br/>Seasonal Fruit Salad</a><br/><b>$3.49/ea</b><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="14" /></td>
				</tr>
                    <tr colspan="3"><td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="5" /></td></tr>
                    <tr valign="top" align="center">
				<td class="mdayBody"><a href="http://www.freshdirect.com/product.jsp?productId=juic_sir_ojhg&amp;catId=fresh_juice_sir&amp;cmpgn=mday&amp;trk=mday" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/p_org_oj.jpg" width="80" height="80" border="0" /><br/>Organic Orange Juice, 1/2 gal</a><br/><b>$3.49/ea</b><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="14" /></td>
				<td class="mdayBody"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10" /><br/><a href="http://www.freshdirect.com/product.jsp?productId=bl_french&amp;catId=cof_by_rst_fr_reg&amp;cmpgn=mday&amp;trk=mday" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/p_french_roast.jpg" width="70" height="70" border="0" /><br/>French Roast</a><br/><b>$3.99/lb</b><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="14" /></td>
                    <td class="mdayBody"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10" /><br/><a href="http://www.freshdirect.com/product.jsp?productId=blk_eng_bkst&amp;catId=blk_tea&amp;cmpgn=mday&amp;trk=mday" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/p_eng_brkfst.jpg" width="70" height="70" border="0" /><br/>English Breakfast Tea</a><br/><b>$7.99/lb</b><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="14" /></td>
				</tr>
                    <tr valign="top" align="center">
				<td class="mdayBody"><a href="http://www.freshdirect.com/category.jsp?catId=dai_eggs&amp;prodCatId=dai_eggs&amp;productId=dai_eggs_brn_01&amp;cmpgn=mday&amp;trk=mday" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/p_dai_eggs_brn.jpg" width="80" height="80" border="0" /><br/>Grade A Jumbo Brown Eggs - 1 dozen</a><br/><b>$1.99/ea</b><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="14" /></td>
				<td class="mdayBody"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10" /><br/><a href="http://www.freshdirect.com/product.jsp?catId=mea_bacon_pckgd&amp;productId=ccbn_dw_smkd_bacon&amp;cmpgn=mday&amp;trk=mday" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/p_smkd_bacon.jpg" width="70" height="70" border="0" /><br/><b>D &amp; W</b> Smoked Gourmet Bacon</a><br/><b>$3.49/ea</b><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="14" /></td>
                    <td class="mdayBody"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10" /><br/><a href="http://www.freshdirect.com/product.jsp?productId=usa_muttbutt&amp;catId=sheep&amp;cmpgn=mday&amp;trk=mday" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/p_mutt_button.jpg" width="70" height="70" border="0" /><br/>Mutton Button</a><br/><b>$2.99/ea</b><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="14" /></td>
				</tr>
                    <tr colspan="3"><td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="5" /></td></tr>
                    <tr valign="top" align="center">
				<td class="mdayBody"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="6" /><br/><a href="http://www.freshdirect.com/category.jsp?catId=gro_peanu_prese&amp;prodCatId=gro_peanu_prese&amp;productId=spe_sarabe_billys_b_01&amp;cmpgn=mday&amp;trk=mday" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/p_billys_blues.jpg" width="64" height="64" border="0" /><br/><b>Sarabeth's</b> Mixed Berry (Billy's Blues) Preserves</a><br/><b>$5.99/ea</b><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="14" /></td>
				<td class="mdayBody"><a href="http://www.freshdirect.com/product.jsp?productId=whit_rsnpcnlg&amp;catId=bfruitnutherb&amp;cmpgn=mday&amp;trk=mday" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/p_raisin_pecan.jpg" width="70" height="70" border="0" /><br/>Raisin Pecan</a><br/><b>$3.59/ea</b><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="14" /></td>
                    <td class="mdayBody"><a href="http://www.freshdirect.com/product.jsp?productId=bfruitnutherb_chocb&amp;catId=bfruitnutherb&amp;cmpgn=mday&amp;trk=mday" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/p_choc_boule.jpg" width="70" height="70" border="0" /><br/>Chocolate Boule</a><br/><b>$3.99/ea</b><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="14" /></td>
				</tr>
                    <tr valign="top" align="center">
				<td class="mdayBody"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="22" /><br/><a href="http://www.freshdirect.com/product.jsp?catId=bak_cookies_fd&amp;productId=chclt_strwbrry&amp;cmpgn=mday&amp;trk=mday" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/p_choc_strawberry.jpg" width="87" height="58" border="0" /><br/>Dark Chocolate-Dipped Strawberries</a><br/><b>$6.99/6pk</b><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="14" /></td>
				<td class="mdayBody"><a href="http://www.freshdirect.com/category.jsp?catId=gro_candy_candy&amp;prodCatId=gro_candy_candy&amp;productId=spe_nei_hrtsnlovs&amp;cmpgn=mday&amp;trk=mday" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/p_hrtsnlovs_box.jpg" width="80" height="80" border="0" /><br/><b>Niederegger</b> Chocolate &amp; Marzipan Hearts &amp; Loaves Gift Box</a><br/><b>$5.49/ea</b><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="14" /></td>
                    <td class="mdayBody"><a href="http://www.freshdirect.com/category.jsp?catId=gro_fine_choco&amp;prodCatId=gro_fine_choco&amp;productId=spe_clu_chcups&amp;cmpgn=mday&amp;trk=mday" class="promoProduct"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/p_choc_esp_cups.jpg" width="80" height="80" border="0" /><br/><b>Michel Cluizel</b> Chocolate Espresso Cups</a><br/><b>$6.99/5pc</b><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="14" /></td>
				</tr>
				</table>
			</td>
			<td width="2%"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="10" height="1" /></td>
			<td bgcolor="#CCCCCC" width="1"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1" /></td>
			<td width="2%"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="10" height="1" /></td>
			<td width="48%" valign="top" align="center">
				<table cellpadding="0" cellspacing="0" border="0" width="90%">
					<tr>
                         <td><font class="mdayHeader"><b>Our Mother's Day Cake with<br/>Cream Cheese Icing 6'' - $10.99</b></font><br/><a href="http://www.freshdirect.com/product.jsp?productId=cake_mother_carrot&amp;catId=bak_cake_fd&amp;cmpgn=mday&amp;trk=mday"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/f_momsdaycake.jpg" width="169" height="169" border="0" vspace="8" /></a><br/><font class="mdaySubheader"><b>Baked from scratch in our Bakery!</b></font><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8" /><br/>Richly textured with pecans and raisins, spiced with cinnamon and nutmeg, this brown-sugar-sweetened carrot cake is moist and delicious. Two layers of cake covered with our tangy, sweet cream cheese frosting. Our limited-edition Mother's Day version features buttercream flowers and a delectable, decorated chocolate medallion.
<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10" /><br/>
<a href="http://www.freshdirect.com/product.jsp?productId=cake_mother_carrot&amp;catId=bak_cake_fd&amp;cmpgn=mday&amp;trk=mday" class="featureProduct"><b>Click here for more details!</b></a></td>
					</tr>
                         <tr><td><img src="http://www.freshdirect.com/media_stat/images/layout/cccccc.gif" width="100%" height="1" vspace="14" /></td></tr>
                         <tr><td><font class="mdayHeader">Eric Girerd Chocolates</font><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8" /><br/><a href="http://www.freshdirect.com/category.jsp?catId=gro_candy&amp;groceryVirtual=gro_candy&amp;brandValue=bd_eric_girerd&amp;prodCatId=gro_candy_candy&amp;productId=spe_eg_choc_01&amp;cmpgn=mday&amp;trk=mday"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/f_spe_eg_choc.jpg" width="125" height="125" align="right" vspace="8" hspace="10" border="0" /></a>Eric Girerd is a third generation Pastry Chef formerly of Le Chantilly and Tavern on the Green. Chef Girerd blends his own beans to make his unique chocolate paste and fillings. His creations are currently offered by fine establishments like the Waldorf-Astoria, Ritz-Carlton, and Four Seasons in New York City.<br/><br/><b>Assorted Classically Flavored Chocolates</b><br/>
			9 piece - $5.99, 15 piece - $8.99<br/><br/>
			<b>Assorted Asian Spice Infused Chocolates</b><br/>
			9 piece - $5.99<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10" /><br/><a href="http://www.freshdirect.com/category.jsp?catId=gro_fine_choco&amp;cmpgn=mday&amp;trk=mday" class="featureProduct"><b>Click here to see all our fine chocolates!</b></a></td></tr>
					<tr><td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="12" /></td></tr>	
				</table>
               </td>
</tr>
<tr><td colspan="5"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="18" /></td></tr>
<tr><td colspan="5" background="http://www.freshdirect.com/media_stat/images/template/email/mother/little_flower.gif"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="100%" height="21" /></td></tr>
<tr><td colspan="5"><br/></td></tr>
<tr><td colspan="5" align="center">Price and availability subject to change. Please see Web site for current prices and availability.<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="18" /><br/><a href="http://www.freshdirect.com?cmpgn=mday&amp;trk=mday"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/logo_url.gif" width="264" height="35" border="0" alt="www.freshdirect.com" /></a><br/><br/></td></tr>
</table>
	
	<xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/>

</xsl:template>

</xsl:stylesheet>

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
		a.mdayHeader:link {color: #FF99CC; text-decoration: none; }
		a.mdayHeader:visited {color: #FF99CC; text-decoration: none; }
		a.mdayHeader:active {color: #FF99CC; text-decoration: none; }
		a.mdayHeader:hover {color: #FF99CC; text-decoration: underline; }
        .mdayBody    { font-size: 11px; color: #333333; font-family: Verdana, Arial, Helvetica, sans-serif; }
        .mdayProduct  { font-size: 13px; color: #669933; font-family: Verdana, Arial, Helvetica, sans-serif; }
        .mdayProduct.link {color: #669933; text-decoration: none; }
		a.mdayProduct:link {color: #669933; text-decoration: none; }
		a.mdayProduct:visited {color: #669933; text-decoration: none; }
		a.mdayProduct:active {color: #FF99CC; text-decoration: none; }
		a.mdayProduct:hover {color: #669933; text-decoration: underline; }
		</style>
	</head>
<body bgcolor="#FFFFFF" text="#333333" size="1" marginwidth="20" marginheight="18" leftmargin="20" topmargin="18">
	<xsl:call-template name="mail_body" />
</body>
</html>

</xsl:template>

<xsl:template name="mail_body">

<table width="627" cellpadding="0" cellspacing="0" align="center" class="mdayBody">
	<tr>
		<td colspan="7" align="center"><a href="http://www.freshdirect.com?trk=mday04b"><img src="http://www.freshdirect.com/media/images/promotions/email/mday04b.gif" width="438" height="112" vspace="6" border="0"/></a><br/><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/04_flower_line.gif" width="626" height="21" vspace="6"/></td>
	</tr>
	<tr valign="top">
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="250" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="53" height="10"/></td>
		<td rowspan="6"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="10" height="10"/></td>
		<td rowspan="6" bgcolor="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/ffffff.gif" width="1" height="10"/></td>
		<td rowspan="6"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="10" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="183" height="10"/></td>
		<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="120" height="10"/></td>
	</tr>
	
	<tr valign="top">
		<td colspan="2" class="mdayBody">
			<a href="http://www.freshdirect.com/category.jsp?catId=hmr_mom_breakfast&amp;trk=mday04b"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/04_sunday_brunch.gif" width="281" height="36" vspace="0" border="0"/><br/>
			<img src="http://www.freshdirect.com/media_stat/images/template/email/mother/04_mday_brunch.jpg" width="281" height="132" vspace="8" border="0"/>
			</a><br/>
			We think our own mothers would agree that a perfect Mother's Day starts with breakfast in bed. But what to serve? We've taken the guesswork out of it by assembling an assortment of tasty treats that are sure to meet with mom's approval. 
			<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8"/><br/><a href="http://www.freshdirect.com/category.jsp?catId=hmr_mom_breakfast&amp;trk=mday04b" class="mdayProduct"><font color="#669933"><b>Click here to buy</b></font></a>
		</td>
		
    <td colspan="2" class="mdayBody">
		<a href="http://www.freshdirect.com/product.jsp?productId=cake_motherstrwb&amp;catId=bak_cake_fd&amp;trk=mday04b" class="mdayHeader"><font color="#FF99CC"><b>Mother's Day Strawberry Mousse<br/>Cake with Butter Cream Icing - $9.99</b></font><br/>
		<img src="http://www.freshdirect.com/media_stat/images/template/email/mother/04_mday_cake.jpg" width="175" height="133" border="0"/></a><br/>
		Our delectable Mother's Day cake combines the light, moist, finely textured features of the classic French genoise cake with an inner layer of rich strawberry mousse. We top it off with a butter cream frosting sprinkled with pink flowers and a "To Mother," greeting, making this cake a festive finale to your celebrations.
		<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=cake_motherstrwb&amp;catId=bak_cake_fd&amp;trk=mday04b" class="mdayProduct"><font color="#669933"><b>Click here to buy</b></font></a>
	</td>
	</tr>
	
	<tr><td colspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/cccccc.gif" width="303" height="1" vspace="12"/></td><td colspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/cccccc.gif" width="303" height="1" vspace="12"/></td></tr>
	
	<tr valign="top">
		<td class="mdayBody">
			<a href="http://www.freshdirect.com/product.jsp?productId=wine_bc_saniger_1&amp;catId=win_fizzy&amp;trk=mday04b" class="mdayHeader"><font color="#FF99CC"><b>Make Mom a Mimosa<br/>with S&#225;niger Cava - $10.00</b></font></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="6" /><br/>
			S&#225;niger's toasty Brut is one of the most elegantly refined cavas we've ever uncorked. Just add fresh squeezed OJ for a the prefect mimosa.
			<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8"/><br/>
			<a href="http://www.freshdirect.com/product.jsp?productId=wine_bc_saniger_1&amp;catId=win_fizzy&amp;trk=mday04b" class="mdayProduct"><font color="#669933"><b>Click here to buy</b></font></a>
		</td>
		<td align="right"><a href="http://www.freshdirect.com/product.jsp?productId=wine_bc_saniger_1&amp;catId=win_fizzy&amp;trk=mday04b"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/04_bc_saniger.jpg" width="43" height="110" border="0"/></a></td>
		<td class="mdayBody">
			<a href="http://www.freshdirect.com/category.jsp?catId=gro_candy_candy&amp;prodCatId=gro_candy_candy&amp;productId=gro_gift_kidmom&amp;trk=mday04b" class="mdayHeader"><font color="#FF99CC"><b>Kid's Mother's Day<br/>
			Basket - $14.99</b></font></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="6" /><br/>
			Give mom a big kiss for Mother's Day!  Includes one bag of small Hershey chocolate kisses and one large Madelaine Big Kiss. 
			<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8"/><br/>
			<a href="http://www.freshdirect.com/category.jsp?catId=gro_candy_candy&amp;prodCatId=gro_candy_candy&amp;productId=gro_gift_kidmom&amp;trk=mday04b" class="mdayProduct"><font color="#669933"><b>Click here to buy</b></font></a>
		</td>
		<td align="right"><a href="http://www.freshdirect.com/category.jsp?catId=gro_candy_candy&amp;prodCatId=gro_candy_candy&amp;productId=gro_gift_kidmom&amp;trk=mday04b"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/04_mday_kids_basket.jpg" width="110" height="110" border="0"/></a></td>
	</tr>
	
	<tr><td colspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/cccccc.gif" width="303" height="1" vspace="12"/></td><td colspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/cccccc.gif" width="303" height="1" vspace="12"/></td></tr>
	
	<tr valign="top">
		<td class="mdayBody">
			<a href="http://www.freshdirect.com/product.jsp?productId=wine_bc_veuvecliquot_1&amp;catId=win_fizzy&amp;trk=mday04b" class="mdayHeader"><font color="#FF99CC"><b>Veuve Clicquot Ponsardin<br/>Vintage Reserve 1996 - $49.00</b></font></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="6" /><br/>
			Crafted from a blend of Premier and Grand Cru vineyards, VC's Pinot Noir-led Vintage Reserve presses all the right buttons.
			<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8"/><br/>
			<a href="http://www.freshdirect.com/product.jsp?productId=wine_bc_veuvecliquot_1&amp;catId=win_fizzy&amp;trk=mday04b" class="mdayProduct"><font color="#669933"><b>Click here to buy</b></font></a>
		</td>
		<td align="right"><a href="http://www.freshdirect.com/product.jsp?productId=wine_bc_veuvecliquot_1&amp;catId=win_fizzy&amp;trk=mday04b"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/04_bc_veuvecliquot.jpg" width="43" height="110" border="0"/></a></td>
		<td class="mdayBody">
			<a href="http://www.freshdirect.com/category.jsp?catId=gro_candy_candy&amp;prodCatId=gro_candy_candy&amp;productId=gro_gift_mom&amp;trk=mday04b" class="mdayHeader"><font color="#FF99CC"><b>Mother's Day<br/>
Basket - $24.99</b></font></a><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="6" /><br/>
			Delight your mom or grandma this Mother's Day with our pretty wicker basket filled with sweet treats. 
			<br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8"/><br/>
			<a href="http://www.freshdirect.com/category.jsp?catId=gro_candy_candy&amp;prodCatId=gro_candy_candy&amp;productId=gro_gift_mom&amp;trk=mday04b" class="mdayProduct"><font color="#669933"><b>Click here to buy</b></font></a>
		</td>
		<td align="right"><a href="http://www.freshdirect.com/category.jsp?catId=gro_candy_candy&amp;prodCatId=gro_candy_candy&amp;productId=gro_gift_mom&amp;trk=mday04b"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/04_mday_basket.jpg" width="110" height="110" border="0"/></a></td>
	</tr>
	
	<tr>
		<td colspan="7" align="center" class="mdayBody">
		<img src="http://www.freshdirect.com/media_stat/images/template/email/mother/04_flower_line.gif" width="626" height="21" vspace="12"/><br/>
		Price and availability subject to change. Please see Web site for current prices and availability.<br/><a href="http://www.freshdirect.com?cmpgn=mday&amp;trk=mday04b"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/logo_url.gif" width="264" height="35" border="0" alt="www.freshdirect.com" vspace="10" /></a>
	
	<xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/>
		</td>
	</tr>
</table>


</xsl:template>

</xsl:stylesheet>

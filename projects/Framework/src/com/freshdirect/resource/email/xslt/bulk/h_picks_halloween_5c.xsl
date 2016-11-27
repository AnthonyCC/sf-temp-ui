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
		<title>Our Picks for Halloween!</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
		<style type="text/css">
		.subText    { font-size: 9px; color: #000000; font-weight: bold; font-family: Verdana, Arial, Helvetica, sans-serif; }
		.picks    { font-size: 18px; color: #336600; font-weight: bold; font-family: Arial, Helvetica, sans-serif; }
		.picks.link {color: #336600; text-decoration: underline; }
		a.picks:link {color: #336600; text-decoration: underline; }
		a.picks:visited {color: #336600; text-decoration: underline; }
		a.picks:active {color: #FF9933; text-decoration: underline; }
		a.picks:hover {color: #336600; text-decoration: underline; }
		.promoProduct    { font-size: 12px; color: #000000; font-family: Verdana, Arial, Helvetica, sans-serif; }
		.promoProduct.link {color: #336600; text-decoration: underline; }
		a.promoProduct:link {color: #336600; text-decoration: underline; }
		a.promoProduct:visited {color: #336600; text-decoration: underline; }
		a.promoProduct:active {color: #FF9933; text-decoration: underline; }
		a.promoProduct:hover {color: #336600; text-decoration: underline; }
		</style>
	</head>
<body bgcolor="#000000" text="#000000" marginheight="0" topmargin="0" marginwidth="0" leftmargin="0">
	<xsl:call-template name="mail_body" />
</body>
</html>

</xsl:template>

<xsl:template name="mail_body">
<table width="100%" cellpadding="13" cellspacing="0" border="0" bgcolor="#000000"><tr><td>
<table width="666" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF" align="center" background="" class="promoProduct">
  <tr> 
    <td colspan="2"><img src="http://www.freshdirect.com/media_stat/images/template/email/halloween/crazy_eyes1.gif" width="44" height="25" hspace="8"/></td>
    <td colspan="9"></td>
  </tr>
  <tr> 
    <td colspan="11" align="center"><a href="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks05c" class="picks" style="color: #336600;"><img src="http://www.freshdirect.com/media/images/promotions/picks_halloween_5c.gif" width="634" height="57" border="0"/></a><br/>
      <img src="http://www.freshdirect.com/media_stat/images/template/email/halloween/skull_line.gif" width="626" height="17" vspace="3"/><br/>
      <img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="5"/></td>
  </tr>
  <tr> 
    <td colspan="2" align="right" valign="bottom"><img src="http://www.freshdirect.com/media_stat/images/template/email/halloween/crazy_eyes4.gif" width="44" height="25"/></td>
    <td rowspan="3" align="right" valign="top"><a href="http://www.freshdirect.com/product.jsp?productId=cake_hween_chccke&amp;catId=bak_cake_fd&amp;trk=epicks05c"><img src="http://www.freshdirect.com/media_stat/images/template/email/halloween/hween_choccake.jpg" width="202" height="168" border="0" vspace="8"/></a></td>
    <td colspan="2"></td>
    <td rowspan="7" bgcolor="#000000"></td>
    <td rowspan="7"></td>
    <td colspan="2"><a href="http://www.freshdirect.com/product.jsp?productId=cake_hween_cpcke&amp;catId=bak_cake_fd&amp;trk=epicks05c"><img src="http://www.freshdirect.com/media_stat/images/template/email/halloween/cupcakes.gif" width="181" height="32" border="0" alt="Halloween Cupakes - $4.99/ 4pk"/></a></td>
    <td><img src="http://www.freshdirect.com/media_stat/images/template/email/halloween/bat_top.gif" width="49" height="24"/></td>
    <td rowspan="7"></td>
  </tr>
  <tr> 
    <td colspan="2" rowspan="2"><img src="http://www.freshdirect.com/media_stat/images/template/email/halloween/witch.gif" width="65" height="89"/></td>
    <td align="right" colspan="2" rowspan="2"><img src="http://www.freshdirect.com/media_stat/images/template/email/halloween/frankie.gif" width="50" height="86"/></td>
    <td valign="top">Our moist vanilla and chocolate cupcakes get all dressed up for Halloween with thick vanilla frosting, multicolored sprinkles and candy corn. All treat, no trick.<br/><br/><a href="http://www.freshdirect.com/product.jsp?productId=cake_hween_cpcke&amp;catId=bak_cake_fd&amp;trk=epicks05c" class="promoProduct" style="color: #336600"><font color="#336600"><u>Click for cupcakes!</u></font></a></td>
    <td colspan="2" align="right"><a href="http://www.freshdirect.com/product.jsp?productId=cake_hween_cpcke&amp;catId=bak_cake_fd&amp;trk=epicks05c"><img src="http://www.freshdirect.com/media_stat/images/template/email/halloween/hween_cupcake.jpg" width="110" height="96" border="0"/></a></td>
  </tr>
  <tr> 
    <td colspan="3"><img src="http://www.freshdirect.com/media_stat/images/layout/333333.gif" width="258" height="1"/></td>
  </tr>
  <tr> 
    <td colspan="5" align="center" rowspan="2"><a href="http://www.freshdirect.com/product.jsp?productId=cake_hween_chccke&amp;catId=bak_cake_fd&amp;trk=epicks05c"><img src="http://www.freshdirect.com/media_stat/images/template/email/halloween/choc_cake.gif" width="277" height="63" border="0" alt="FreshDirect Halloween Chocolate Layer Cake - $9.99/ each"/></a></td>
    <td colspan="2"><a href="http://www.freshdirect.com/product.jsp?productId=cake_hween_ckie&amp;catId=bak_cookies_fd&amp;trk=epicks05c"><img src="http://www.freshdirect.com/media_stat/images/template/email/halloween/cookies.gif" width="223" height="34" border="0" alt="Halloween Sugar Cookies - $3.99/ 6pk" vspace="3"/></a></td>
    <td><img src="http://www.freshdirect.com/media_stat/images/template/email/halloween/crazy_eyes2.gif" width="44" height="25"/></td>
  </tr>
  <tr> 
    <td valign="top" rowspan="3">Crispy and sweet, you will mistake these buttery-tasting cookies for homemade.
	The edges are baked until crunchy, but the centers are still soft. Dunk into a glass of milk, serve alongside a dish of vanilla ice cream or pack into a lunch box.<br/><br/>
      <a href="http://www.freshdirect.com/product.jsp?productId=cake_hween_ckie&amp;catId=bak_cookies_fd&amp;trk=epicks05c" class="promoProduct" style="color: #336600;"><font color="#336600"><u>Click here for ghosts!</u></font></a></td>
    <td colspan="2" align="right" rowspan="3" valign="top"><br/><a href="http://www.freshdirect.com/product.jsp?productId=cake_hween_ckie&amp;catId=bak_cookies_fd&amp;trk=epicks05c"><img src="http://www.freshdirect.com/media_stat/images/template/email/halloween/hween_cookie.jpg" width="100" height="100" border="0"/></a></td>
  </tr>
  <tr> 
    <td rowspan="2"></td>
    <td colspan="3" valign="top"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8"/><br/>
      Here's a cake that will satisfy the most passionate of chocolate obsessions. 
      Three layers of rich, dense cake that tastes of milky chocolate, generously 
      frosted with our thick, fudgy icing. The cake layers have a mellow sweetness, 
      set off by the more intense flavor of dark chocolate in the frosting. Serve 
      with milk or a steaming mug of cappuccino.<br/>
      <br/>
    </td>
    <td rowspan="2"></td>
  </tr>
  <tr> 
    <td><img src="http://www.freshdirect.com/media_stat/images/template/email/halloween/crazy_eyes3.gif" width="44" height="25"/></td>
    <td colspan="2" height="24"><xsl:text disable-output-escaping="yes">&amp;nbsp;</xsl:text><a href="http://www.freshdirect.com/product.jsp?productId=cake_hween_chccke&amp;catId=bak_cake_fd&amp;trk=epicks05c" class="promoProduct" style="color: #336600;"><font color="#336600"><u>Don't be scared, click here to buy!</u></font></a></td>
  </tr>
  <tr> 
    <td colspan="11" align="center"><img src="http://www.freshdirect.com/media_stat/images/template/email/halloween/skull_line.gif" width="626" height="17" vspace="12"/><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="10"/></td>
  </tr>
  <tr> 
    <td colspan="10" align="center"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0" class="promoProduct">
        <tr align="center" valign="top"> 
          <td align="left" colspan="2"><img src="http://www.freshdirect.com/media_stat/images/template/email/halloween/vamp.gif" width="54" height="86"/></td>
          <td><a href="http://www.freshdirect.com/category.jsp?catId=gro_candy_chocb&amp;prodCatId=gro_candy_chocb&amp;productId=gro_hersheys_assorted_01&amp;trk=epicks05c" class="promoProduct" style="color: #336600;"><img src="http://www.freshdirect.com/media/images/product/grocery/gro_hersheys_assorted_01_c.jpg" width="80" height="80" border="0"/><br/><font color="#336600"><u><b>Hershey's</b> Assorted Miniatures Chocolate Bars</u></font></a><br/><b>$2.99/ea</b></td>
          <td><a href="http://www.freshdirect.com/category.jsp?catId=gro_peanu_marma&amp;prodCatId=gro_peanu_marma&amp;productId=spe_sarabe_blood_or_01&amp;trk=epicks05c" class="promoProduct" style="color: #336600;"><img src="http://www.freshdirect.com/media/images/product/specialty/spe_sarabe_blood_or_01_c.jpg" width="80" height="80" border="0"/><br/><font color="#336600"><u><b>Sarabeth's</b> Blood Orange Marmalade</u></font></a><br/><b>$6.99/ea</b></td>
          <td><a href="http://www.freshdirect.com/category.jsp?catId=gro_beer_domes&amp;prodCatId=gro_beer_domes&amp;productId=beer_brooklyn_pum_sxbt&amp;trk=epicks05c" class="promoProduct" style="color: #336600;"><img src="http://www.freshdirect.com/media/images/product/grocery_three/beer_brooklyn_pum_sxbt_c.jpg" width="80" height="80" border="0"/><br/><font color="#336600"><u><b>Brooklyn Brewery</b> Post Road Pumpkin Ale</u></font></a><br/><b>$7.49/6pk</b></td>
          <td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10"/><br/>
            <a href="http://www.freshdirect.com/product.jsp?productId=sq_hrd_pump&amp;catId=sqw&amp;trk=epicks05c" class="promoProduct" style="color: #336600;"><img src="http://www.freshdirect.com/media/images/product/vegetables/squash/sq_hrd_pump_cr.jpg" width="70" height="70" border="0"/><br/><font color="#336600"><u>Pumpkin</u></font></a><br/><b>$0.39/lb</b></td>
          <td><a href="http://www.freshdirect.com/category.jsp?catId=gro_candy_candy&amp;prodCatId=gro_candy_candy&amp;productId=gro_hersheys_kisses_m_01&amp;trk=epicks05c" class="promoProduct" style="color: #336600;"><img src="http://www.freshdirect.com/media/images/product/grocery/gro_hersheys_kisses_m_01_c.jpg" width="80" height="80" border="0"/><br/><font color="#336600"><u><b>Hershey's</b> Chocolate Kisses</u></font></a><br/><b>$2.99/ea</b></td>
        </tr>
		<tr><td colspan="7"><img src="http://www.freshdirect.com/media_stat/images/template/layout/clear.gif" width="1" height="10"/></td></tr>
        <tr valign="top" align="center"> 
          <td><img src="http://www.freshdirect.com/media_stat/images/template/layout/clear.gif" width="10" height="1"/></td>
          <td><img src="http://www.freshdirect.com/media_stat/images/template/layout/clear.gif" width="1" height="10"/><br/><a href="http://www.freshdirect.com/product.jsp?productId=cndyapl_cndyapple&amp;catId=candy_apples&amp;trk=epicks05c" class="promoProduct" style="color: #336600;"><img src="http://www.freshdirect.com/media/images/product/fruit/cndyapl_cndyapple_c.jpg" width="70" height="70" border="0"/><br/><font color="#336600"><u>Candy Apple</u></font></a><br/><b>$0.99/ea</b></td>
          <td><a href="http://www.freshdirect.com/product.jsp?productId=cfncndy_tootsiechc&amp;catId=gro_candy_bulk&amp;trk=epicks05c" class="promoProduct" style="color: #336600;"><img src="http://www.freshdirect.com/media/images/product/candy_fruit_nuts/candy/cfncndy_tootsiechc_c.jpg" width="80" height="80" border="0"/><br/><font color="#336600"><u><b>Tootsie Roll</b> Midgees</u></font></a><br/><b>$1.45/bag</b></td>
          <td><a href="http://www.freshdirect.com/product.jsp?productId=spe_lillyplum_van&amp;catId=bak_cake_lillyplum&amp;trk=epicks05c" class="promoProduct" style="color: #336600;"><img src="http://www.freshdirect.com/media_stat/images/template/email/halloween/hween_lillyplum.jpg" width="80" height="80" border="0"/><br/><font color="#336600"><u>Cream-Filled Vanilla Cupcakes (Halloween Edition)</u></font></a><br/><b>$5.99/4pk</b></td>
          <td><a href="http://www.freshdirect.com/product.jsp?productId=pietrt_pump&amp;catId=bak_pie_fd&amp;trk=epicks05c" class="promoProduct" style="color: #336600;"><img src="http://www.freshdirect.com/media/images/product/pastry/pies_tarts/pietrt_pump_c.jpg" width="80" height="80" border="0"/><br/><font color="#336600"><u>Pumpkin Pie</u></font></a><br/><b>$8.99/ea</b></td>
          <td><a href="http://www.freshdirect.com/category.jsp?catId=gro_candy_chocb&amp;prodCatId=gro_candy_chocb&amp;productId=gro_reeses_milk_cho_01&amp;trk=epicks05c" class="promoProduct" style="color: #336600;"><img src="http://www.freshdirect.com/media/images/product/grocery/gro_reeses_milk_cho_01_c.jpg" width="80" height="80" border="0"/><br/><font color="#336600"><u><b>Reese's</b> Peanut Butter Cup Miniatures</u></font></a><br/><b>$2.99/ea</b></td>
          <td><a href="http://www.freshdirect.com/category.jsp?catId=gro_candy_chocb&amp;prodCatId=gro_candy_chocb&amp;productId=gro_snickers_choc_car_01&amp;trk=epicks05c" class="promoProduct" style="color: #336600;"><img src="http://www.freshdirect.com/media/images/product/grocery/gro_snickers_choc_car_01_c.jpg" width="80" height="80" border="0"/><br/><font color="#336600"><u><b>Snickers</b> Fun Size Chocolate Bars</u></font></a><br/><b>$2.89/ea</b></td>
        </tr>
      </table>
    </td>
    <td></td>
  </tr>
  <tr> 
    <td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="20"/></td>
    <td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="45" height="20"/></td>
    <td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="202" height="20"/></td>
    <td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="50" height="20"/></td>
    <td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="15" height="20"/></td>
    <td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="20"/></td>
    <td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="15" height="20"/></td>
    <td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="148" height="20"/></td>
    <td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="60" height="20"/></td>
    <td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="50" height="20"/></td>
    <td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="20"/></td>
  </tr>
  <tr valign="top"> 
    <td colspan="2" align="right"><img src="http://www.freshdirect.com/media_stat/images/template/email/halloween/bat_bot.gif" width="43" height="35"/></td>
    <td colspan="7" align="center"><a href="http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks05c" class="picks" style="font-size: 18px; color: #336600; font-weight: bold; font-family: Arial, Helvetica, sans-serif;"><font color="#336600"><u>Click here for all of our Halloween Picks!</u></font></a></td>
    <td colspan="2"></td>
  </tr>
  <tr align="center"> 
    <td></td>
    <td colspan="9">
      <a href="http://www.freshdirect.com?trk=epicks05c"><img src="http://www.freshdirect.com/media_stat/images/template/email/mother/logo_url.gif" width="264" height="35" vspace="6" border="0" alt="www.FreshDirect.com"/></a><br/>
      Price and availability subject to change. Please see Web site for current 
      prices and availability.<br/>
      <img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8"/></td>
    <td></td>
  </tr>
  <tr> 
    <td></td>
    <td colspan="9"><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/><br/>
      <br/>
    </td>
    <td></td>
  </tr>
</table>
</td></tr>
</table>
</xsl:template>

</xsl:stylesheet>
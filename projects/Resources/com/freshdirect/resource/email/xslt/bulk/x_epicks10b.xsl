<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
OUR KNICKS PICKS 

ALL AMERICAN SUPER HERO - $49.99/ea 
http://www.freshdirect.com/product.jsp?productId=hmr_hero_amer&amp;catId=hmr_ent_entree&amp;trk=epicks10b 

PARTY SIZE SHRIMP COCKTAIL - $44.99/ea 
http://www.freshdirect.com/product.jsp?productId=hmr_cater_shrmctl&amp;catId=hmr_apps&amp;trk=epicks10b 

MINI SPRING ROLLS WITH DIPPING SAUCE - $13.99/ea 
http://www.freshdirect.com/product.jsp?productId=hmr_app_minisprng&amp;catId=hmr_ent_happs&amp;trk=epicks10b 

BROOKLYN BREWERY PILSNER - $7.49/ea 
http://www.freshdirect.com/category.jsp?catId=gro_beer_domes&amp;prodCatId=gro_beer_domes&amp;productId=beer_brooklyn_pilsner_sxbt&amp;trk=epicks10b 

PEPSI - $2.99/ea 
http://www.freshdirect.com/category.jsp?catId=gro_bever&amp;groceryVirtual=gro_bever&amp;brandValue=bd_pepsi&amp;prodCatId=gro_bever_soda_cola&amp;productId=gro_pepsi_cola02&amp;trk=epicks10b 

TERRA BLUES POTATO CHIPS - $2.79/ea 
http://www.freshdirect.com/category.jsp?catId=gro_snack_chips_potat&amp;prodCatId=gro_snack_chips_potat&amp;productId=spe_terra_blues_01&amp;trk=epicks10b 

TOSTITOS NATURAL BLUE CORN TORTILLA CHIPS - $2.99/ea 
http://www.freshdirect.com/category.jsp?catId=gro_snack_chips_corn&amp;prodCatId=gro_snack_chips_corn&amp;productId=spe_tostitos_bluecrn&amp;trk=epicks10b 

GUACAMOLE, SALSA AND TORTILLA CHIPS - $18.99/ea 
http://www.freshdirect.com/product.jsp?productId=hmrap_dpchp_cater&amp;catId=hmr_apps&amp;trk=epicks10b 

MUIR GLEN ORGANIC SALSA, MEDIUM - $2.59/ea 
http://www.freshdirect.com/category.jsp?catId=gro_snack_salsa&amp;prodCatId=gro_snack_salsa&amp;productId=gro_muir_g_salsa_m_01&amp;trk=epicks10b 

FRESHDIRECT NEW YORK CHEESECAKE - $10.99/ea 
http://www.freshdirect.com/product.jsp?productId=cake_cheese&amp;catId=bak_cake_fd&amp;trk=epicks10b 

SEE ALL OF OUR KNICKS PICKS: 
http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks10b 

Now Available 
HEALTH &amp; BEAUTY PRODUCTS: 
http://www.freshdirect.com/department.jsp?deptId=hba&amp;trk=epicks10b 
<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

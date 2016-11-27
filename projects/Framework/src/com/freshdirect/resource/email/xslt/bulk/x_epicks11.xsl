<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
MEMORIAL DAY PICKS 

RIB EYE STEAK - $9.99/lb 
http://www.freshdirect.com/product.jsp?productId=bstk_rbeye_bnls&amp;catId=bgril&amp;trk=epicks11 

FRESH ROUND HAMBURGERS - $2.99/lb 
http://www.freshdirect.com/product.jsp?productId=bgrnd_ptty_rnd&amp;catId=bptty&amp;trk=epicks11 

FRESH TURKEY BURGERS - $4.99/lb 
http://www.freshdirect.com/product.jsp?productId=ptrk_ptty&amp;catId=mt_trky&amp;trk=epicks11 

SHRIMP KABOBS - $11.99/3pk 
http://www.freshdirect.com/product.jsp?productId=ffss_shrmp_kbob&amp;catId=ready_to&amp;trk=epicks11 

VEGETABLE KABOBS - $9.99/3pk 
http://www.freshdirect.com/product.jsp?productId=hmr_kbob_veg3pk&amp;catId=cut_veg&amp;trk=epicks11 

SEEDLESS WATERMELON - $0.39/lb 
http://www.freshdirect.com/product.jsp?productId=mln_wtr_sdls&amp;catId=mln&amp;trk=epicks11 

SEASONAL FRUIT TART - $12.99/ea 
http://www.freshdirect.com/product.jsp?productId=pietrt_mxbrry&amp;catId=bak_pie_fd&amp;trk=epicks11 

STAR-SPANGLED CUPCAKES - $3.99/4pk 
http://www.freshdirect.com/product.jsp?productId=cake_rwbcup&amp;catId=bak_cake_fd&amp;trk=epicks11 

BROOKLYN BREWERY PILSNER - $7.99/6pk 
http://www.freshdirect.com/category.jsp?catId=gro_beer_domes&amp;prodCatId=gro_beer_domes&amp;productId=beer_brooklyn_pilsner_sxbt&amp;trk=epicks11 

DOMAINE DES LAURIERS PICPOUL DE PINET - $9.50/ea 
http://www.freshdirect.com/product.jsp?productId=wine_bc_domlau_pin&amp;catId=win_fresh&amp;trk=epicks11 

SEE ALL OF OUR MEMORIAL DAY PICKS: 
http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks11 

CATERING NOW OPEN! 
We've created the perfect platters for entertaining, parties, and business meetings. 
http://www.freshdirect.com/department.jsp?deptId=cat&amp;trk=epicks11 
<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

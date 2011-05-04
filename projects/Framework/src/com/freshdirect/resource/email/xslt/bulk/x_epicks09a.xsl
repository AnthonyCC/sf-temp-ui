<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
OUR SPRING PICKS 

STRAWBERRIES - $1.69/pkg 
http://www.freshdirect.com/product.jsp?productId=br_strwbrry&amp;catId=br&amp;trk=epicks09a 

BABY SPINACH, ORGANIC, PACKAGED - $4.29/ea 
http://www.freshdirect.com/product.jsp?productId=grns_org_bbyspnpk&amp;catId=grns&amp;trk=epicks09a 

BONED, ROLLED, &amp; TIED LEG OF LAMB - $6.69/lb 
http://www.freshdirect.com/product.jsp?catId=lrstbnlss&amp;productId=lrst_leg_bndrldtd&amp;trk=epicks09a 

LEG OF LAMB WITH SAUCE - $99.00/ea 
http://www.freshdirect.com/product.jsp?productId=hmr_hol_lglmb&amp;catId=hmr_ent_entree&amp;trk=epicks09a 

HALIBUT FILLET - $14.99/lb 
http://www.freshdirect.com/product.jsp?productId=fflt_hlbt_flt&amp;catId=fflt&amp;trk=epicks09a 

FOIL SOLID CHOCOLATE SITTING RABBIT - $4.79/ea 
http://www.freshdirect.com/category.jsp?catId=gro_candy_choco&amp;prodCatId=gro_candy_choco&amp;productId=gro_choc_foilrbtsolid&amp;trk=epicks09a 

CADBURY CREME EGGS - $0.99/ea 
http://www.freshdirect.com/category.jsp?catId=gro_candy_candy&amp;prodCatId=gro_candy_candy&amp;productId=gro_cadbury_crmegg&amp;trk=epicks09a 

EASTER CUPCAKES - $3.99/4pk 
http://www.freshdirect.com/product.jsp?productId=cake_easter_cpcke&amp;catId=bak_cake_fd&amp;trk=epicks09a 

EASTER COOKIES - $3.99/7pk 
http://www.freshdirect.com/product.jsp?productId=ckibni_easter&amp;catId=bak_cookies_fd&amp;trk=epicks09a 

LARGE TRADITIONAL JELLY BEANS - $0.79/ea 
http://www.freshdirect.com/category.jsp?catId=gro_candy_bulk&amp;prodCatId=gro_candy_bulk&amp;productId=gro_jelly_jbeans&amp;trk=epicks09a 

SEE ALL OF OUR SPRING PICKS: 
http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks09a 

SHOP FROM OUR KOSHER FOR PASSOVER SECTION: 
http://www.freshdirect.com/category.jsp?catId=kosher_passover&amp;trk=epicks09a 
<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

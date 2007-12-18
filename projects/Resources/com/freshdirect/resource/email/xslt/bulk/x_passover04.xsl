<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
KOSHER FOR PASSOVER PICKS 

KEDEM CONCORD GRAPE JUICE - $4.99/ea 
http://www.freshdirect.com/category.jsp?catId=kosh_pass_bever&amp;prodCatId=kosh_pass_bever&amp;productId=gro_kedem_concj_1&amp;trk=passover04 

KEDEM GOURMET GEFILTE FISH - $4.99/ea   
http://www.freshdirect.com/category.jsp?catId=kosh_pass_cjfish&amp;prodCatId=kosh_pass_cjfish&amp;productId=gro_kedem_gefilte&amp;trk=passover04 

OSEM EGG MATZAH - $3.29/ea 
http://www.freshdirect.com/category.jsp?catId=kosh_pess_matzo&amp;prodCatId=kosh_pess_matzo&amp;productId=gro_osem_matzeg&amp;trk=passover04 

TA'S FROSTED CRISPY O'S CEREAL - $3.29/ea 
http://www.freshdirect.com/category.jsp?catId=kosher_grocery_cereal&amp;prodCatId=kosher_grocery_cereal&amp;productId=gro_tas_frostos&amp;trk=passover04 

GEFEN COCONUT FLAVORED MACAROONS - $2.99/ea 
http://www.freshdirect.com/category.jsp?catId=kosh_pass_cookie&amp;prodCatId=kosh_pass_cookie&amp;productId=gro_gefen_cocoroon&amp;trk=passover04 

CERES APRICOT JUICE - $2.49/ea 
http://www.freshdirect.com/category.jsp?catId=kosh_pass_bever&amp;prodCatId=kosh_pass_bever&amp;productId=spe_ceres_apricot_01&amp;trk=passover04 

KEDEM SUGAR FREE GRAPE PRESERVES - $3.99/ea 
http://www.freshdirect.com/category.jsp?catId=kosh_pass_condi&amp;prodCatId=kosh_pass_condi&amp;productId=gro_kedem_sfspred&amp;trk=passover04 

GEFEN MANDARIN ORANGE SEGMENTS IN LIGHT SYRUP - $0.99/ea 
http://www.freshdirect.com/category.jsp?catId=kosh_pass_fruveg&amp;prodCatId=kosh_pass_fruveg&amp;productId=gro_gefen_orngseg&amp;trk=passover04 

SWEE-TOUCH-NEE NO-CAFFEINE TEA BAGS - $1.49/ea 
http://www.freshdirect.com/category.jsp?catId=kosh_pass_bever&amp;prodCatId=kosh_pass_bever&amp;productId=gro_sweet_decaftea&amp;trk=passover04 

ALPROSE NAPOLITAINS MINIATURE BITE-SIZE ASSORTED MILK CHOCOLATES - $4.99/ea 
http://www.freshdirect.com/category.jsp?catId=kosh_pass_candy&amp;prodCatId=kosh_pass_candy&amp;productId=gro_alprose_bitchoc&amp;trk=passover04 

SEE ALL OF OUR KOSHER FOR PASSOVER ITEMS: 
http://www.freshdirect.com/category.jsp?catId=kosher_passover&amp;trk=passover04 

NOW AVAILABLE, KOSHER FISH! 
http://www.freshdirect.com/category.jsp?catId=kosher_seafood&amp;trk=passover04 
<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

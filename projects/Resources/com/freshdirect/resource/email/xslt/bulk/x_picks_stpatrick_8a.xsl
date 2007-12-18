<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
OUR ST. PATRICK'S DAY PICKS 

LAMB STEW - $8.99/ea 
http://www.freshdirect.com/product.jsp?productId=hmrmeat_lambstew_new&amp;catId=hmr_entree_meat&amp;trk=epicks08a

GUINNESS PUB DRAUGHT - $7.99/ea 
http://www.freshdirect.com/category.jsp?catId=gro_beer_impor&amp;prodCatId=gro_beer_impor&amp;productId=beer_guinness_pub_fourcan&amp;trk=epicks08a

IRISH SODA BREAD - $4.99/ea 
http://www.freshdirect.com/product.jsp?productId=bfruitnutherb_soda&amp;catId=bfruitnutherb&amp;trk=epicks08a

ST. PATRICK'S DAY COOKIES - $4.99/6pk 
http://www.freshdirect.com/product.jsp?productId=ckibni_stpat&amp;catId=bak_cookies_fd&amp;trk=epicks08a

CLARE ISLAND IRISH ORGANIC SALMON FILLET - $9.99/lb 
http://www.freshdirect.com/product.jsp?productId=fflt_slmn_org&amp;catId=fflt&amp;trk=epicks08a

McCANN'S IRISH OATMEAL - $6.49/ea 
http://www.freshdirect.com/category.jsp?catId=gro_cerea&amp;groceryVirtual=gro_cerea&amp;brandValue=bd_mccanns&amp;prodCatId=gro_cerea_hot&amp;productId=spe_mccanns_oat&amp;trk=epicks08a

SEE ALL OF OUR ST. PATRICK'S DAY PICKS: 
http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks08a

NOW AVAILABLE, KOSHER SEAFOOD!  
http://www.freshdirect.com/category.jsp?catId=kosher_seafood&amp;trk=epicks08a
<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

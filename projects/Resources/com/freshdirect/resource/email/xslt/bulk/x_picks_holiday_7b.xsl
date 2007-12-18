<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
HAVE A SAFE &amp; HAPPY HOLIDAYS!
FROM YOUR FRESHDIRECT FAMILY

Just a reminder we'll be closed for Christmas and will resume normal delivery hours on Friday, 12/26.

CHRISTMAS SUGAR COOKIES - $3.99/6pk
http://www.freshdirect.com/product.jsp?productId=ckibni_xmas&amp;catId=bak_cookies_fd&amp;trk=epicks07b

CLARE ISLAND CERTIFIED ORGANIC SALMON FILLET - $9.99/lb
http://www.freshdirect.com/product.jsp?productId=fflt_slmn_org&amp;catId=fflt&amp;trk=epicks07b

FRESHDIRECT DARK CHOCOLATE-DIPPED STRAWBERRIES - $7.49/6pk
http://www.freshdirect.com/product.jsp?productId=chclt_strwbrry&amp;catId=bak_cookies_fd&amp;trk=epicks07b

ARTISANAL FOUR-CHEESE PLATE - $33.99/ea
http://www.freshdirect.com/product.jsp?productId=che_arti_course_six&amp;catId=che_arti&amp;trk=epicks07b

SEE ALL OF OUR HOLIDAY SUGGESTIONS!
http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks07b
<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

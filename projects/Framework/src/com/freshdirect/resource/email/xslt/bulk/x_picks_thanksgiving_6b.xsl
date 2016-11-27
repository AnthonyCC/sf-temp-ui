<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
Dear <xsl:value-of select="customer/firstName"/>,

OUR THANKSGIVING FAVORITES

FRESH GRADE A ALL NATURAL TURKEY - $1.19/lb
http://www.freshdirect.com/product.jsp?productId=trk_fresh&amp;catId=mt_trky&amp;trk=epicks06b

FROZEN GRADE A ALL NATURAL TURKEY - $0.99/lb
http://www.freshdirect.com/product.jsp?productId=ptrk_whl&amp;catId=mt_trky&amp;trk=epicks06b

AARON'S GLATT KOSHER FRESH TURKEY - $1.99/lb
http://www.freshdirect.com/product.jsp?productId=kos_turk_whole&amp;catId=kosher_meat_poultry&amp;trk=epicks06b

PUMPKIN CHEESECAKE WITH CRANBERRY CHUTNEY - $9.99/each
http://www.freshdirect.com/product.jsp?productId=cake_pmpkn_cheese&amp;catId=bak_cake_fd&amp;trk=epicks06b
Rich and creamy New York cheesecake takes an autumnal turn when it meets pumpkin and cranberries. The tangy flavor of fresh cranberry chutney is a perfect counterpoint to the warm, delicate flavors of pumpkin and spice.

AUTUMN SUGAR COOKIES - $3.99/ 6pk
http://www.freshdirect.com/product.jsp?productId=cake_hween_ckie&amp;catId=bak_cookies_fd&amp;trk=epicks06b
Crispy and sweet, you will mistake these buttery-tasting cookies for homemade. The edges are baked until crunchy, but the centers are still soft. Dunk into a glass of milk, serve alongside a dish of vanilla ice cream or pack into a lunch box.

DON'T FEEL LIKE COOKING? TRY OUR CHEF'S COMPLETE TURKEY DINNERS.
http://www.freshdirect.com/category.jsp?catId=hmr_thanks&amp;trk=epicks06b
Our chefs will be preparing every dish using top quality ingredients, family recipes and professional know-how. All you'll have to do is heat and serve.

CHECK OUT ALL OF OUR THANKSGIVING PICKS!
http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks06b


**OUR SCHEDULE FOR THE THANKSGIVING WEEK**

Due to high demand, we recommend placing your order early to guarantee delivery for Tuesday 11/25 or Wednesday 11/26.

Tuesday 11/25	Regular delivery schedule
Wednesday 11/26	Deliveries will have 3-hour windows from 9:00 am - 11:00 pm
Thursday 11/27	Thanksgiving Day - FreshDirect will be closed
Friday 11/28	FreshDirect will be closed but customer service will be open from 8:00 am - 10:00 pm
Saturday 11/29	Normal delivery schedules resume


Happy Thanksgiving from all of us at FreshDirect!
<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

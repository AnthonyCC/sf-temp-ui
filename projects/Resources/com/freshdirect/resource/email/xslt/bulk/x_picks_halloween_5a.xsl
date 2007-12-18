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

Has Halloween shopping got you terrified?  Fear not! Try our Halloween Picks.   

HALLOWEEN CHOCOLATE LAYER CAKE - $9.99/ ea
http://www.freshdirect.com/product.jsp?productId=cake_hween_chccke&amp;catId=bak_cake_fd&amp;trk=epicks05a
Here's a cake that will satisfy the most passionate of chocolate obsessions. Three layers of rich, dense cake that tastes of milky chocolate, generously frosted with our thick, fudgy icing. The cake layers have a mellow sweetness, set off by the more intense flavor of dark chocolate in the frosting. Serve with milk or a steaming mug of cappuccino.

HALLOWEEN CUPCAKES - $4.99/ 4pk
http://www.freshdirect.com/product.jsp?productId=cake_hween_cpcke&amp;catId=bak_cake_fd&amp;trk=epicks05a
Our moist vanilla and chocolate cupcakes get all dressed up for Halloween with thick vanilla frosting, multicolored sprinkles and candy corn. All treat, no trick.

HALLOWEEN SUGAR COOKIES - $3.99/ 6pk
http://www.freshdirect.com/product.jsp?productId=cake_hween_ckie&amp;catId=bak_cookies_fd&amp;trk=epicks05a
Crispy and sweet, you will mistake these buttery-tasting cookies for homemade. The edges are baked until crunchy, but the centers are still soft. Dunk into a glass of milk, serve alongside a dish of vanilla ice cream or pack into a lunch box.

CHECK OUT ALL OF OUR HALLOWEEN PICKS!
http://www.freshdirect.com/department.jsp?deptId=our_picks&amp;trk=epicks05a


Happy Halloween,
   
FreshDirect
<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
ORGANIC &amp; ALL-NATURAL! - CHECK OUT OUR NEW DEPARTMENT! 
We're proud to announce the launch of our Organic and All-Natural Department. 
We've added a wide selection of Organic fruit and vegetables as well as a hormone- and antibiotic-free meat and deli items. We've also gathered together hundreds of carefully selected All-Natural and Certified Organic grocery items &#151; from shade-grown coffee to phosphate-free laundry detergent.  

Visit our new Organic &amp; All-Natural Department: 
http://www.freshdirect.com/department.jsp?deptId=orgnat&amp;trk=organic04 

CHECK OUT OUR SUMMER GRILLING MENU! 
It just wouldn't be summer without the hiss and sizzle of the grill. To make your party planning less fuss and more fun, we've gathered a special collection of our summer grill favorites from burgers and brats to steaks and salads. Eat, drink, and grill on! 
http://www.freshdirect.com/category.jsp?catId=menu&amp;trk=organic04 

JUST FOR FATHER'S DAY 
SUNDAY, JUNE 20, IS JUST DAYS AWAY! 
FATHER'S DAY CHOCOLATE MOUSSE CAKE - $10.99/ea 
http://www.freshdirect.com/product.jsp?productId=cake_fathermousse&amp;catId=bak_cake_fd&amp;trk=organic04 

ALSO IN SEASON! 

LOCAL NEW YORK STATE STRAWBERRIES - $3.49/ea 
http://www.freshdirect.com/product.jsp?productId=br_nystrawbrry&amp;catId=br&amp;trk=organic04 

COPPER RIVER WILD KING SALMON STEAKS - $16.99/lb 
http://www.freshdirect.com/product.jsp?productId=fstk_slmn_cpprwk&amp;catId=fstk&amp;trk=organic04 
<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

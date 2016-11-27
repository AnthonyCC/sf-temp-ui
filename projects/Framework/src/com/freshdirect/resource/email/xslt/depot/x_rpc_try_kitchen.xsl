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
			
We've created a menu of restaurant-quality lunch and dinner classics at terrific prices. Chef David McInerney (formerly of Bouley and One If by Land, Two If by Sea) and his team make everything by hand using fresh, natural ingredients. All you have to do is heat and enjoy.
			
Whether you're tired of the neighborhood fare or want to supplement your own cooking, stock up on our five most popular dishes--or go to our Meals Department to check out the whole menu.
		
			
Kansas City Barbecued Ribs
Slow-cooked pork ribs rubbed with spices and brushed with sweet and spicy barbecue sauce.

Rotisserie Chicken
Lemon Herb, Barbeque, or Salt &amp; Pepper. You can't track down a tastier chicken.

Three Cheese Lasagna with Bolognese Sauce
Mozzarella, Ricotta, and Parmesan layered in sheets of fresh pasta with extra meaty Bolognese.

Grilled Thin-Crust Pizza
Fresh mozzarella and Parmesan, and fresh tomato sauce on a crisp, super-thin grilled crust.

Classic Caesar Salad
Crisp romaine lettuce, crunchy croutons, and heaps of shaved Parmesan with our homemade Caesar dressing.
			
			

We're adding new items all the time, so keep checking back for our new line of seasonal dishes, appetizers, and soups. And remember to save a little room for a homemade dessert. Simply log on to www.freshdirect.com/rpc
			
As a reminder your delivery days are - Tuesday &amp; Friday from 3:00pm to 6:00pm
			
You can also contact us via email at rpcservice@freshdirect.com
			
See you soon,
			
FreshDirect
			
<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='x_header.xsl'/>
	<xsl:include href='x_optout_footer.xsl'/>
	<xsl:include href='x_footer_v2.xsl'/>
	<xsl:output method="text"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>
OUR THANKSGIVING FEAST!

Your turn to cook this year? We have you covered - from gorgeous all-natural fresh roasted turkey to spiced pumpkin pie, with every glorious morsel in between.
Our chefs David McInerney (formerly of Bouley and One If by Land, Two If by Sea) and Michael Stark (formerly of Tribeca Grill) will be preparing every dish using top quality ingredients, family recipes and professional know-how so it's bound to be exceptionally delicious.
When we bring it to your door, all you'll have to do is heat and serve, so you can spend time with your family.

$119.99 - 12-14lb Turkey Dinner (Serves 4-6)
$199.99 - 16-20lb Turkey Dinner (Serves 8-12)
$259.99 - 22-24lb Turkey Dinner (Serves 14-18) 

Included Side Dishes:
Cranberry Sauce, Parbaked Dinner Rolls, Zucchini Bread

Includes your choice of Stuffing:
Classic Cornbread Stuffing, Sausage Herb Stuffing or Dried Fruit Stuffing

Includes your choice of Gravy:
Traditional Turkey Gravy or Au Jus

Includes your choice of four Side Dishes:
Candied Yams, Mashed Potatoes, Green Beans, Glazed Carrots or Creamed Corn

Includes your choice of Desserts:
Pumpkin Pie, Carolina Pecan Pie, New England Apple Pie or Dark Double Chocolate Layer Cake

Orders must be placed by Sun., Nov. 23 for delivery Tues., Nov. 25

See our entire Thanksgiving menu: http://www.freshdirect.com/category.jsp?catId=hmr_thanks&amp;trk=thanks_03dta

<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>
<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_common_functions_v1.xsl'/>
	<xsl:include href='h_optout_footer.xsl'/>
	<xsl:include href='h_footer_v1.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="erp-email">
<HTML>
<HEAD>
	<TITLE>Eat Out. In.</TITLE>
	<BASE HREF="http://www.freshdirect.com"/>
	<LINK REL="stylesheet" href="/assets/css/emails.css"/>
</HEAD>
<BODY BGCOLOR="#FFFFFF"><xsl:call-template name="h_header_v1"/>
<TABLE cellpadding="0" cellspacing="0" border="0"><TR>
<!-- =============== START LEFT SPACER =============== -->
<TD WIDTH="20"><img src="http://www.freshdirect.com/images/clear.gif" width="20" height="1" border="0" alt="" /></TD>
<!-- =============== END LEFT SPACER ================= -->
<!-- ~~~~~~~~~~~~~~~ START CONTENT ~~~~~~~~~~~~~~~ -->
<TD>
<TABLE cellpadding="0" cellspacing="0" width="90%">
<TR>
<TD WIDTH="100%"><TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0"><TR><TD WIDTH="100%" BGCOLOR="#CCCCCC"><img src="images/clear.gif" width="600" height="1" border="0" alt=""/></TD></TR></TABLE><P><b>Dear <xsl:value-of select="@first-name"/></b>,</P>
<P>We've created a menu of restaurant-quality lunch and dinner classics at terrific prices. Chef David McInerney (formerly of Bouley and One If by Land, Two If by Sea) and his team make everything by hand using fresh, natural ingredients. All you have to do is heat and enjoy.</P>
<P>Whether you're tired of the neighborhood fare or want to supplement your own cooking, stock up on our five most popular dishes--or go to our <a href="http://www.freshdirect.com/department.jsp?deptId=hmr&amp;cmpgn=trykitchen">Meals Department</a> to check out the whole menu.</P>
<!-- menu start -->
<TABLE WIDTH="75%" CELLPADDING="0" CELLSPACING="0" BORDER="0">
<tr><td rowspan="5" width="20"><img src="images/clear.gif" width="20" height="1" border="0" alt=""/></td>
<td height="85"><a href="product.jsp?productId=hmrtmeat_bbqrib&amp;catId=menu_entrees&amp;cmpgn=trykitchen"><img src="media/images/product/meals/hmr_temp/hmrtmeat_bbqrib_c.jpg" width="90" height="71" border="0" alt="Kansas City Barbecued Ribs" /></a></td>
<td rowspan="5" width="10"><img src="images/clear.gif" width="10" height="1" border="0"/></td>
<td><a href="product.jsp?productId=hmrtmeat_bbqrib&amp;catId=menu_entrees&amp;cmpgn=trykitchen"><b>Kansas City Barbecued Ribs</b></a><br/>
Slow-cooked pork ribs rubbed with spices and brushed with sweet and spicy barbecue sauce.</td>
</tr>
<tr>
<td height="85"><a href="product.jsp?productId=hmrtpltry_rotchkn&amp;catId=menu_entrees&amp;cmpgn=trykitchen"><img src="media/images/product/meals/hmr_temp/hmrtpltry_rotchkn_c.jpg" width="90" height="71" border="0" alt="Rotisserie Chicken" /></a></td>
<td><a href="product.jsp?productId=hmrtpltry_rotchkn&amp;catId=menu_entrees&amp;cmpgn=trykitchen"><b>Rotisserie Chicken</b></a><br/>
Lemon Herb, Barbeque, or Salt &amp; Pepper. You can't track down a tastier chicken.</td>
</tr>
<tr>
<td height="85"><a href="product.jsp?productId=hmrtpsta_lasagna&amp;catId=menu_entrees&amp;cmpgn=trykitchen"><img src="media/images/product/meals/hmr_temp/hmrtpsta_lasagna_c.jpg" width="90" height="71" border="0" alt="Three Cheese Lasagna with Bolognese Sauce" /></a></td>
<td><a href="product.jsp?productId=hmrtpsta_lasagna&amp;catId=menu_entrees&amp;cmpgn=trykitchen"><b>Three Cheese Lasagna with Bolognese Sauce</b></a><br/>
Mozzarella, Ricotta, and Parmesan layered in sheets of fresh pasta with extra meaty Bolognese.</td>
</tr>
<tr>
<td height="85"><a href="product.jsp?productId=hmrpizza_plain&amp;catId=menu_entrees&amp;cmpgn=trykitchen"><img src="media/images/product/meals/hmr_temp/hmrpizza_plain_c.jpg" width="90" height="71" border="0" alt="Grilled Thin-Crust Pizza" /></a></td>
<td><a href="product.jsp?productId=hmrpizza_plain&amp;catId=menu_entrees&amp;cmpgn=trykitchen"><b>Grilled Thin-Crust Pizza</b></a><br/>
Fresh mozzarella and Parmesan, and fresh tomato sauce on a crisp, super-thin grilled crust.</td>
</tr>
<tr>
<td height="85"><a href="product.jsp?productId=hmrsld_ceasar&amp;catId=menu_sides&amp;cmpgn=trykitchen"><img src="media/images/product/meals/salads/salad_entree/hmrsld_ceasar_c.jpg" width="90" height="71" border="0" alt="Classic Caesar Salad" /></a></td>
<td><a href="product.jsp?productId=hmrsld_ceasar&amp;catId=menu_sides&amp;cmpgn=trykitchen"><b>Classic Caesar Salad</b></a><br/>
Crisp romaine lettuce, crunchy croutons, and heaps of shaved Parmesan with our homemade Caesar dressing.</td>
</tr>
</TABLE><!-- menu end --><P>We're adding new items all the time, so keep checking back for our new line of seasonal dishes, appetizers, and soups. And remember to save a little room for a <a href="department.jsp?deptId=bak&amp;cmpgn=trykitchen">homemade dessert</a>.</P>
<P><b>See you soon</b>,<br/>
<br/>
FreshDirect</P>
<!-- end modify -->
<P><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v1"/></P></TD></TR></TABLE>
</TD>
<!-- ~~~~~~~~~~~~~~~ END CONTENT ~~~~~~~~~~~~~~~ -->
<!-- =============== BEGIN RIGHT SPACER =============== -->
<TD WIDTH="20"><img src="images/clear.gif" width="20" height="1" border="0" alt="" /></TD>
<!-- =============== END RIGHT SPACER ================= -->
</TR>
</TABLE>
</BODY>
</HTML>
</xsl:template>

</xsl:stylesheet>
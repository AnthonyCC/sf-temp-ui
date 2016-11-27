<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:doc="http://xsltsl.org/xsl/documentation/1.0"
	xmlns:str="http://xsltsl.org/string"
	extension-element-prefixes="doc str">
	<xsl:output method="html"/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_optout_footer.xsl'/>
	<xsl:include href='h_footer_v2.xsl'/>
	<xsl:include href='string.xsl' />
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">

<html>
	<head>
		<title>Your personal chef.</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
	</head>
<body bgcolor="#FFFFFF" text="#333333">
	<xsl:call-template name="mail_body" />
</body>
</html>

</xsl:template>

<xsl:template name="mail_body">

<table cellpadding="0" cellspacing="0">
<tr>
<!-- =============== START LEFT SPACER =============== -->
<td><img src="/images/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END LEFT SPACER ================= -->

<!-- ~~~~~~~~~~~~~~~ START CONTENT ~~~~~~~~~~~~~~~ -->
<td><xsl:call-template name="h_header_v1" />
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
			<td width="100%"><table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td BGCOLOR="#CCCCCC"><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></td></tr></table></td>
		</tr>
	</table><br/>
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
		<td>
			<p>Dear <xsl:value-of select="customer/firstName"/>,</p>
			
			<p>We've created a menu of restaurant-quality lunch and dinner classics at terrific prices. Chef David McInerney (formerly of Bouley and One If by Land, Two If by Sea) and his team make everything by hand using fresh, natural ingredients. All you have to do is heat and enjoy.</p>
			
            <p>Whether you're tired of the neighborhood fare or want to supplement your own cooking, stock up on our five most popular dishes--or go to our <a href="http://www.freshdirect.com/department.jsp?deptId=hmr">Meals Department</a> to check out the whole menu.</p>
			
			<!-- menu start -->
			<table width="75%" cellpadding="0" cellspacing="0" border="0">
			<tr><td rowspan="5" width="20"><img src="images/clear.gif" width="20" height="1" border="0" alt=""/></td>
			<td height="85"><a href="http://www.freshdirect.com/product.jsp?productId=hmrtmeat_bbqrib&amp;catId=hmr_entreesnew&amp;trk=email"><img src="http://www.freshdirect.com/media/images/product/meals/hmr_temp/hmrtmeat_bbqrib_c.jpg" width="90" height="71" border="0" alt="Kansas City Barbecued Ribs" /></a></td>
			<td rowspan="5" width="10"><img src="images/clear.gif" width="10" height="1" border="0"/></td>
			<td><a href="http://www.freshdirect.com/product.jsp?productId=hmrtmeat_bbqrib&amp;catId=hmr_entreesnew&amp;trk=email"><b>Kansas City Barbecued Ribs</b></a><br/>
			Slow-cooked pork ribs rubbed with spices and brushed with sweet and spicy barbecue sauce.</td>
			</tr>
			<tr>
			<td height="85"><a href="http://www.freshdirect.com/product.jsp?productId=hmrtpltry_rotchkn&amp;catId=hmr_entreesnew&amp;trk=email"><img src="http://www.freshdirect.com/media/images/product/meals/hmr_temp/hmrtpltry_rotchkn_c.jpg" width="90" height="71" border="0" alt="Rotisserie Chicken" /></a></td>
			<td><a href="http://www.freshdirect.com/product.jsp?productId=hmrtpltry_rotchkn&amp;catId=hmr_entreesnew&amp;trk=email"><b>Rotisserie Chicken</b></a><br/>
			Lemon Herb, Barbeque, or Salt &amp; Pepper. You can't track down a tastier chicken.</td>
			</tr>
			<tr>
			<td height="85"><a href="http://www.freshdirect.com/product.jsp?productId=hmrtpsta_lasagna&amp;catId=hmr_pasta&amp;trk=email"><img src="http://www.freshdirect.com/media/images/product/meals/hmr_temp/hmrtpsta_lasagna_c.jpg" width="90" height="71" border="0" alt="Three Cheese Lasagna with Bolognese Sauce" /></a></td>
			<td><a href="http://www.freshdirect.com/product.jsp?productId=hmrtpsta_lasagna&amp;catId=hmr_pasta&amp;trk=email"><b>Three Cheese Lasagna with Bolognese Sauce</b></a><br/>
			Mozzarella, Ricotta, and Parmesan layered in sheets of fresh pasta with extra meaty Bolognese.</td>
			</tr>
			<tr>
			<td height="85"><a href="http://www.freshdirect.com/product.jsp?productId=hmrpizza_plain&amp;catId=hmr_pizza&amp;trk=email"><img src="http://www.freshdirect.com/media/images/product/meals/hmr_temp/hmrpizza_plain_c.jpg" width="90" height="71" border="0" alt="Grilled Thin-Crust Pizza" /></a></td>
			<td><a href="http://www.freshdirect.com/product.jsp?productId=hmrpizza_plain&amp;catId=hmr_pizza&amp;trk=email"><b>Grilled Thin-Crust Pizza</b></a><br/>
			Fresh mozzarella and Parmesan, and fresh tomato sauce on a crisp, super-thin grilled crust.</td>
			</tr>
			<tr>
			<td height="85"><a href="http://www.freshdirect.com/product.jsp?productId=hmrsld_ceasar&amp;catId=hmr_sidesnew&amp;trk=email"><img src="http://www.freshdirect.com/media/images/product/meals/salads/salad_entree/hmrsld_ceasar_c.jpg" width="90" height="71" border="0" alt="Classic Caesar Salad" /></a></td>
			<td><a href="http://www.freshdirect.com/product.jsp?productId=hmrsld_ceasar&amp;catId=hmr_sidesnew&amp;trk=email"><b>Classic Caesar Salad</b></a><br/>
			Crisp romaine lettuce, crunchy croutons, and heaps of shaved Parmesan with our homemade Caesar dressing.</td>
			</tr>
			</table>
			<!-- menu end -->
			
			<p>We're adding new items all the time, so keep checking back for our new line of seasonal dishes, appetizers, and soups. And remember to save a little room for a <a href="http://www.freshdirect.com/department.jsp?deptId=bak">homemade dessert</a>. Simply log on to <a href="http://www.freshdirect.com/ca">www.freshdirect.com/ca</a><xsl:text disable-output-escaping="yes">&amp;nbsp;&amp;nbsp;</xsl:text>Access Code: CA321</p>
			
			<p>You can also contact us via email at <a href="mailto:caservice@freshdirect.com">caservice@freshdirect.com</a></p>
			
			<p><b>See you soon,</b></p>
			
            <p>FreshDirect</p>
			
			<p><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/></p>
</td></tr>
</table>
</td>
<!-- ~~~~~~~~~~~~~~~ END CONTENT ~~~~~~~~~~~~~~~ -->

<!-- =============== BEGIN RIGHT SPACER =============== -->
<td><img src="/images/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END RIGHT SPACER ================= -->
</tr>
</table>

</xsl:template>

</xsl:stylesheet>
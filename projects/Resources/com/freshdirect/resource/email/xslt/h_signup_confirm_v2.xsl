<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_footer_v1.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">

<html>
	<head>
		<title>Welcome to FreshDirect!</title>
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
<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END LEFT SPACER ================= -->

<!-- ~~~~~~~~~~~~~~~ START CONTENT ~~~~~~~~~~~~~~~ -->
<td><xsl:call-template name="h_header_v1" />
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
			<td WIDTH="100%"><table WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0"><tr><td BGCOLOR="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1" border="0" alt="" /></td></tr></table></td>
		</tr>
	</table><br/>
	<table cellpadding="0" cellspacing="0" width="90%">
     <tr>
          <td>
               <p><b>Hello <xsl:value-of select="customer/firstName"/></b>,</p>
				<p>Thank you for signing up with FreshDirect.</p>
				<p>We've hired New York's finest food experts, built the perfect environment for food, and found the shortest distance from farms, dairies, and fisheries to your table. We have all the irresistibly fresh foods you could want, plus popular grocery brands, all for up to 25% less than supermarket prices. And we bring it to your door.</p>
				
				<!--
				<xsl:if test="customer/eligibleForPromotion = 'true'">
				<p>We'd like to welcome you with $20 worth of our irresistibly fresh <a href="http://www.freshdirect.com/department.jsp?deptId=fru">fruit</a> 
				and <a href="http://www.freshdirect.com/department.jsp?deptId=veg">vegetables</a>, 
				<a href="http://www.freshdirect.com/department.jsp?deptId=mea">meat</a> and 
				<a href="http://www.freshdirect.com/department.jsp?deptId=sea">seafood</a> cut to order, 
				<a href="http://www.freshdirect.com/department.jsp?deptId=che">fine cheeses</a> and 
				<a href="http://www.freshdirect.com/department.jsp?deptId=del">deli goods</a>, or any of our other perishables - FREE!*</p>
				</xsl:if>
				-->
				
				<p>We also have a full selection of grocery and household brands, so you can do all your shopping in one stop. So come back soon, <a href="http://www.freshdirect.com">place your first order</a>, and get ready for the freshest, highest-quality food at the best prices in New York.</p>
				<p><b>Happy eating!</b><br/>
				<br/>
				FreshDirect</p>
				
				<!--
				<xsl:if test="customer/eligibleForPromotion = 'true'">
				<p>* Web orders and home delivery orders only. Limited time offer. One per household. Delivery and billing address must match. Certain fresh food items may be excluded. Perishable food is fresh food. $20 limit per item. Available in selected zones. See Web site for full details.</p>
				</xsl:if>
				-->
				
				<p><xsl:call-template name="h_footer_v1"/></p>
		</td>
	</tr>
</table>
</td>
<!-- ~~~~~~~~~~~~~~~ END CONTENT ~~~~~~~~~~~~~~~ -->

<!-- =============== BEGIN RIGHT SPACER =============== -->
<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END RIGHT SPACER ================= -->
</tr>
</table>

</xsl:template>

</xsl:stylesheet>
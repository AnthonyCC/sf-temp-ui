<?xml version="1.0" encoding="UTF-8"?>
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
	       
               <p>Welcome to FreshDirect At The Office!</p>
        	
               <p><b>Hello <xsl:value-of select="customer/firstName"/>,</b></p>
               
               <p>Thank you for signing up with FreshDirect At The Office.</p>
               
               <p>We've gathered a terrific selection of products at low prices to make shopping for the office a breeze. Our expanded product and service offerings specially tailored for corporate clients include:</p>
		
		<ul>
			<li>One-stop shopping for all your office needs (save time and streamline your work!) </li>
			<li>Everyday low prices on popular brands of snacks, beverages and pantry-stocking items (save money and look like a cost-cutting superstar!) </li>
			<li>The convenience of easy online ordering and next-day delivery </li>
			<li>Exceptional corporate customer service and our freshness guarantee </li>
	
		</ul>
               <p>We have everything from milk, coffee and fresh fruit to cleaning supplies and bulk beverages! Enjoy fresher food, convenient deliveries and fuss-free customer care, all for up to 35% less than most vendors.</p>
               
               <p>See you at the office!<br/>
			   <br/>
			    Sincerely,<br/>
			   <br/>
				FreshDirect At The Office</p>
               
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
<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_order_info_v1.xsl'/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_common_functions_v2.xsl'/>
	<xsl:include href='h_footer_v1.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<html lang="en-US" xml:lang="en-US">
<head>
	<title>Your order information has been updated</title>
	<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
</head>
<body bgcolor="#FFFFFF">

<table cellpadding="0" cellspacing="0" width="100%">
<tr>
<!-- =============== START LEFT SPACER =============== -->
<td><img src="/images/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END LEFT SPACER ================= -->

<!-- ~~~~~~~~~~~~~~~ START CONTENT ~~~~~~~~~~~~~~~ -->
<td><xsl:call-template name="h_header_v1" />
	<table cellpadding="0" cellspacing="0" width="90%">
			<tr>
			<td width="100%" bgcolor="#CCCCCC"><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></td>
			</tr>
			<tr><td><br/></td></tr>
			<tr>
				<td>
					<p><b>Hello <xsl:value-of select="customer/firstName"/></b>,</p>

					<p>We've updated your order information. You will receive your final invoice on the day of your delivery. Please look over the details below. If you'd like to make further changes, <xsl:element name = "a"><xsl:attribute name = "href">http://www.freshdirect.com/your_account/order_details.jsp?orderId=<xsl:value-of select="order/erpSalesId"/></xsl:attribute>click here</xsl:element>.</p>

					<p>Enjoy,<br/>
					<br/>
					FreshDirect<br/>
					Customer Service Group</p>
					
					<xsl:comment>fdcOrderCount is CURRENT (modify is called AFTER place, so it's either 0 or 1 during modify)</xsl:comment>
					<xsl:if test="customer/fdcOrderCount &lt;= 1 and order/deliveryInfo/deliveryPlantInfo/salesOrg = '1400'">
						<div style="margin: 20px 0; padding: 10px;"><a href="https://www.freshdirect.com/recycle" style="text-decoration: none; border: 0;"><img src="https://www.freshdirect.com/media/images/email/boxes_to_bags/banner.jpg" alt="We're switching from boxes to bags. A fresh new look! Learn more" style="border: 0;" /></a></div>
					</xsl:if>

					<p><xsl:call-template name="h_order_info_v1"/></p>

					<p><xsl:call-template name="h_footer_v1"/></p>
				</td>
			</tr>
	</table>
</td>
<!-- ~~~~~~~~~~~~~~~ END CONTENT ~~~~~~~~~~~~~~~ -->

<!-- =============== BEGIN RIGHT SPACER =============== -->
<td><img src="/images/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END RIGHT SPACER ================= -->
</tr>
</table>
</body>
</html>
</xsl:template>
</xsl:stylesheet>
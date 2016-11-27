<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_so_cart_info_v1.xsl'/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_common_functions_v2.xsl'/>
	<xsl:include href='h_footer_v1.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<html>
<head>
	<title>Reminder, your standing order </title>
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
		<tr><td width="100%" bgcolor="#CCCCCC"><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></td>
		</tr>
		<tr><td><br/></td></tr>
					<tr>
						<td>
							<p><b>Hi <xsl:value-of select="customer/firstName"/></b>,</p>
                            
                            <p> Thanks for creating your Standing Order. It’s the most convenient way to stay fully stocked with all the office’s favorite eats and drinks!</p>
							<p>Your first Standing Order is scheduled to arrive on <b><xsl:value-of select="standingOrder/deliveryDay" />, <xsl:value-of select="standingOrder/deliveryMonthDate" /> </b> between <b><xsl:value-of select="standingOrder/deliveryTime" /></b>. </p> 
                            
                            <p>We’ll send you an order confirmation with the details of your first Standing Order within the next 24 hours. We’ll also send you a weekly receipt every time your order is assembled and delivered.</p>
							
							<p>Below you will see the settings for your Standing Order, including its name, day, time, and delivery address. You’ll also see all the items in your order.</p> 

							<p>If you’d like to make changes to your Standing Order, <a href="http://www.freshdirect.com/quickshop/standing_orders.jsp">click here.</a> </p>
							
							<p>If you’d like to take a look at our tutorial on how to modify your Standing Order, <a href="http://www.freshdirect.com/quickshop/standing_orders.jsp">click here.</a></p>

							
							<p><b>Thanks for shopping with us!</b></p>
							<p>The FreshDirect At The Office Team </p><br/>

							<p><xsl:call-template name="h_so_cart_info_v1"/></p>
							
							<p>NOTE: If this email does not print out clearly, please go to <a href="http://www.freshdirect.com/quickshop/standing_orders.jsp">Standing Order</a> for a printer-friendly version of your order details.</p>

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

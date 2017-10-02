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
	<title>Multiple order instances for your standing order</title>
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
							<p><b>Hello <xsl:value-of select="customer/firstName"/></b>,</p>

							<p>Please note that you have <xsl:value-of select="orderCount"/> order instances for your standing order <b><xsl:value-of select="standingOrder/customerListName" /></b>. These are: </p>

<i>(Click on Order id for more detials)</i><br/>
<xsl:for-each select="orders/orders">
	Order id: <a href="http://www.freshdirect.com/your_account/order_details.jsp?orderId={erpSalesId}"><xsl:value-of select="erpSalesId"/></a> Delivery date: <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="requestedDate"/></xsl:call-template>

	<br/>
</xsl:for-each>

<p>If you want to make modifications on your standing order please click <a href="http://www.freshdirect.com/quickshop/so_details.jsp?ccListId={standingOrder/customerListId}">here</a>.</p>
							
							<p>Your Customer Service Team at <a href="http://www.freshdirect.com/">FreshDirect</a></p>
							
							<p>NOTE: If this email does not print out clearly, please go to <a href="https://www.freshdirect.com/your_account/order_history.jsp">https://www.freshdirect.com/your_account/order_history.jsp</a> for a printer-friendly version of your order details.</p>

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

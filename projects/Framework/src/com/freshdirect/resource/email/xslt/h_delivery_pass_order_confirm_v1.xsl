<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_delivery_pass_order_info_v1.xsl'/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_common_functions_v2.xsl'/>
	<xsl:include href='h_delivery_pass_footer_v1.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
	<xsl:template match="fdemail">
		<html lang="en-US" xml:lang="en-US">
			<head>
				<title>Thank You for Purchasing a DeliveryPass® <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime" /></xsl:call-template></title>
				<link rel="stylesheet" href="https://www.freshdirect.com/assets/css/emails.css"/>
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
										<!-- ORDER TEXT -->
										
										<p><b>Hello <xsl:value-of select="customer/firstName"/></b>,</p>
										
										<p>
											<xsl:if test="number(order/totalDiscountValue) &gt; 0">
												<p>We've taken $<xsl:value-of select='format-number(order/totalDiscountValue, "###,##0.00", "USD")'/> off your order.</p>
											</xsl:if>
										</p>
										
										<p>Congratulations on purchasing a <strong><xsl:value-of select="order/orderLines/orderLines/description"/></strong>!</p>
										
										<p style="margin: 10px 0 0 0;">Order as often as you like and never be charged a delivery fee*, enjoy loads of great giveaways, deals, and exclusive benefits.</p>
										
										<p style="margin: 10px 0 0 0;">To manage your DeliveryPass® membership, visit our <a href="https://www.freshdirect.com/your_account/delivery_pass.jsp"> DeliveryPass® account settings</a> page.</p>
										
										<p style="margin: 10px 0 0 0;">For more information, please visit our  <a href="https://www.freshdirect.com/media/editorial/picks/deliverypass/dp_terms_only.html"> terms and conditions</a>.</p>
										<br/>
										
										<p><b>Happy shopping and saving!</b><br/>
											
											
											Your FreshDirect DeliveryPass® Team</p>
										
										<p style="margin: 10px 0 0 0;">*Midweek DeliveryPass® subscribers pay zero delivery fees on orders delivered Tuesday-Thursday only.</p>
										<xsl:if test="order/deliveryType = 'H' and customer/numberOfOrders &gt; 1">
											<br/>
											<p><a target="_blank" href="https://refer.freshdirect.com/orderconfirmemail2525"><img src="https://www.freshdirect.com/media/images/promotions/raf/RAF_email_216x42.jpg" alt="Refer A Friend" /></a></p>
										</xsl:if>
										
										<p><xsl:call-template name="h_delivery_pass_order_info_v1.xsl"/></p>
										
										<p><xsl:call-template name="h_delivery_pass_footer_v1"/></p>
										
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
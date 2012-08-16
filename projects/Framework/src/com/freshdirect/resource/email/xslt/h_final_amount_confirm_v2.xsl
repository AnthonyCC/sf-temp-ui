<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_invoice_info_v1.xsl'/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_common_functions_v2.xsl'/>
	<xsl:include href='h_footer_v1.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<html>
<head>
	<title>Your order for <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template> is on its way</title>
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
	</table><br/>
	<table cellpadding="0" cellspacing="0" width="90%">
				<tr>
					<td>
					
						<xsl:choose>
						<xsl:when test="count(order/shortedItems/shortedItems) > 0">
							<p><b>Hello <xsl:value-of select="customer/firstName"/>,</b> and thank you for shopping with FreshDirect!</p>
							
							<p>Unfortunately one or more items that you ordered were not available. You will not be charged for these items. We apologize for any inconvenience this may cause you.<br/><br/>
							
							<table width = "100%" cellspacing="0" cellpadding="0" align="center">
							<tr><td width="50">&#160;</td>
							<td>&#160;</td>
							<td>&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;<b>ITEM(S) NOT IN YOUR ORDER:<br/><br/></b></td>
							</tr>
							<xsl:for-each select="order/shortedItems/shortedItems">
									<tr>
										<td width="50">&#160;</td>
										<td><xsl:value-of select="orderedQuantity - deliveredQuantity" />&#160;<xsl:value-of select="unitsOfMeasure" /></td>
										<td><b><xsl:value-of select="description" /></b><xsl:if test="configurationDesc != '' "><xsl:text> - </xsl:text>(<xsl:value-of select="configurationDesc"/>)</xsl:if></td>
									</tr>		
							</xsl:for-each>
							</table>
							</p>
							
							<xsl:choose>
							<xsl:when test="order/deliveryType = 'P'">
								<p>The rest of your order <b>(#<xsl:value-of select="order/erpSalesId"/>)</b>
								<xsl:choose>
									<xsl:when test="order/depotFacility = 'FreshDirect Pickup'">
								is ready for pickup. Stop by our facility anytime between
									</xsl:when>
									<xsl:otherwise>
								is ready for pickup. Stop by the designated pickup location anytime between
									</xsl:otherwise>
								</xsl:choose>
								<b><xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template> 
								and <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/endTime"/></xsl:call-template></b> 
								on <b><xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template></b>
								to pick up your order. You'll need to bring<xsl:if test="order/paymentMethod/paymentMethodType = 'EBT'"> your EBT card to complete purchase and a valid </xsl:if>photo ID to pick up your food. Just present it to the attendant when you arrive.
								<xsl:if test="order/paymentMethod/paymentMethodType = 'EBT'"> If you need more information on the EBT purchase process, <a href="http://www.freshdirect.com/category.jsp?catId=about_ebt">click here</a> to see our EBT Info Page. </xsl:if>We hope you find everything absolutely fresh and delicious.</p>
							</xsl:when>
							<xsl:otherwise>
							<br/><p>The rest of the order <b>(#<xsl:value-of select="order/erpSalesId"/>)</b> is on its way to you. It will be delivered between <b><xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template> 
									and <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/endTime"/></xsl:call-template></b> 
									on <b><xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template></b>. 
									<xsl:if test="order/paymentMethod/paymentMethodType = 'EBT'"> Please make sure you have your EBT card to complete purchase. </xsl:if>
							</p>
							</xsl:otherwise>
							</xsl:choose>
							
							<p>
							<xsl:choose>
									<xsl:when test="order/paymentMethod/paymentMethodType = 'EBT'">
								Your EBT Purchase Total is <b>$<xsl:value-of select='format-number(order/ebtPurchaseAmount, "###,##0.00", "USD")'/></b>.
									</xsl:when>
									<xsl:otherwise>
								Your final total is <b>$<xsl:value-of select='format-number(order/invoicedTotal, "###,##0.00", "USD")'/></b>.
									</xsl:otherwise>
								</xsl:choose> <xsl:if test="customer/goGreen = 'false'">We'll include a printed, itemized receipt with your goods. </xsl:if><xsl:element name = "a"><xsl:attribute name = "href">http://www.freshdirect.com/your_account/order_details.jsp?orderId=<xsl:value-of select="order/erpSalesId"/></xsl:attribute>Click here</xsl:element> to view order details online.</p>
							
							<p>We hope you find everything absolutely fresh and delicious. Please keep in mind that we have a 100% <u>Satisfaction Guarantee</u>. If you're not happy, please contact us right away so we can make it right.</p>
							
							<p>Shop again soon, and don't forget - you can use our <u>Quickshop</u> feature to shop from all or part of your last order in minutes. Or create a <u>Shopping List</u> to keep track of all the food you love. </p>
							
							<p><b>Thank you for your order and happy eating!</b><br/>
							<br/>
							FreshDirect<br/>
							<xsl:choose><xsl:when test="order/deliveryType != 'C'">Customer Service Group</xsl:when><xsl:otherwise>Corporate Services Group</xsl:otherwise></xsl:choose></p>
							
						</xsl:when>
						
						<xsl:otherwise>
					
						<p><b>Dear <xsl:value-of select="customer/firstName"/></b>,</p>
						
						<xsl:choose>
							<xsl:when test="order/deliveryType = 'P'">
								<p>Hello again! Your order <b>(#<xsl:value-of select="order/erpSalesId"/>)</b>
								<xsl:choose>
									<xsl:when test="order/depotFacility = 'FreshDirect Pickup'">
								is ready for pickup. Stop by our facility anytime between
									</xsl:when>
									<xsl:otherwise>
								is ready for pickup. Stop by the designated pickup location anytime between
									</xsl:otherwise>
								</xsl:choose>
								<b><xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template> 
								and <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/endTime"/></xsl:call-template></b> 
								on <b><xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template></b>
								to pick up your order. You'll need to bring<xsl:if test="order/paymentMethod/paymentMethodType = 'EBT'"> your EBT card to complete purchase and a valid </xsl:if>photo ID to pick up your food. Just present it to the attendant when you arrive.
								<xsl:if test="order/paymentMethod/paymentMethodType = 'EBT'"> If you need more information on the EBT purchase process, <a href="http://www.freshdirect.com/category.jsp?catId=about_ebt">click here</a> to see our EBT Info Page. </xsl:if>We hope you find everything absolutely fresh and delicious.</p>
							</xsl:when>
			   			
							<xsl:otherwise>
								<p>Hello again! Your order <b>(#<xsl:value-of select="order/erpSalesId"/>)</b> 
								is on its way to you. It will be delivered between 
								<b><xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template> 
								and <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/endTime"/></xsl:call-template></b> 
								on <b><xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template></b>. 
								<xsl:if test="order/paymentMethod/paymentMethodType = 'EBT'"> Please make sure you have your EBT card to complete purchase. If you need more information on the EBT purchase process, <a href="http://www.freshdirect.com/category.jsp?catId=about_ebt">click here</a> to see our EBT Info Page. </xsl:if>We hope you find everything absolutely fresh and delicious.</p>
							</xsl:otherwise>
						</xsl:choose>
				
				<xsl:if test="count(order/shortedItems/shortedItems) > 0">
					<p>Unfortunately the following items were not available.  You will not be charged for these items.<br/><br/>
					<table width = "100%" cellspacing="0" cellpadding="0">
							<xsl:for-each select="order/shortedItems/shortedItems">
									<tr>
										<td width="50">&#160;</td>
										<td><xsl:value-of select="orderedQuantity - deliveredQuantity" />&#160;<xsl:value-of select="unitsOfMeasure" /></td>
										<td><b><xsl:value-of select="description" /></b><xsl:if test="configurationDesc != '' "><xsl:text> - </xsl:text>(<xsl:value-of select="configurationDesc"/>)</xsl:if></td>
									</tr>		
							</xsl:for-each>
					</table>
					</p>
				</xsl:if>
				
				<p>
				<xsl:choose>
					<xsl:when test="order/paymentMethod/paymentType = 'M'"><b>Please note that you are not being charged for this order. The amount displayed below, as well as your account with FreshDirect, will reflect a zero balance within the next 48 hours.</b></xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="order/paymentMethod/paymentMethodType = 'EBT'">
						Your EBT Purchase Total is <b>$<xsl:value-of select='format-number(order/ebtPurchaseAmount, "###,##0.00", "USD")'/></b>.
							</xsl:when>
							<xsl:otherwise>
						Your final total is <b>$<xsl:value-of select='format-number(order/invoicedTotal, "###,##0.00", "USD")'/></b>.
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose> 
				<xsl:if test="customer/goGreen = 'false'">We'll include a printed, itemized receipt with your goods. </xsl:if><xsl:element name = "a"><xsl:attribute name = "href">http://www.freshdirect.com/your_account/order_details.jsp?orderId=<xsl:value-of select="order/erpSalesId"/></xsl:attribute>Click here</xsl:element> to view order details online.</p>

				<p>Come back again soon -- reordering is easy with <a href="http://www.freshdirect.com/quickshop/index.jsp">Quickshop</a>. Just choose a list and pick the items you want to buy. Then go to Checkout and start figuring out what to do with your spare time.</p>

				<p><b>Thank you for your order and happy eating!</b><br/>
				<br/>
				FreshDirect<br/>
				<xsl:choose><xsl:when test="order/deliveryType != 'C'">Customer Service Group</xsl:when><xsl:otherwise>Corporate Services Group</xsl:otherwise></xsl:choose></p>
				
				</xsl:otherwise>
				</xsl:choose>
				
				<p><xsl:call-template name="h_invoice_info_v1"/></p>
				
				<p>NOTE: If this email does not print out clearly, please go to <a href="https://www.freshdirect.com/your_account/order_history.jsp">https://www.freshdirect.com/your_account/order_history.jsp</a> for a printer-friendly version of your order details.</p>
			
				<p><xsl:call-template name="h_footer_v1"/></p>
				</td></tr>
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
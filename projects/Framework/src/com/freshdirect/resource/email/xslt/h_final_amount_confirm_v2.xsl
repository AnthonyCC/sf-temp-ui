<?xml version="1.0"?>
<!DOCTYPE xsl:stylesheet [
    <!ENTITY nbsp "&#160;">
    <!ENTITY dot  "&#183;">
    <!ENTITY mdash  "&#8212;">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_invoice_info_v1.xsl'/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_common_functions_v2.xsl'/>
	<xsl:include href='h_footer_v1.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<html lang="en-US" xml:lang="en-US">
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
				<xsl:if test="order/deliveryReservation/deliveryETA/emailETAenabled = 'true'">
					<p><font color="#00A4DE"><b>NEW! More time for you!</b> We’ve cut your wait time in half. Your order’s Estimated Time of Arrival (ETA) is between <b>
					<xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/deliveryETA/startTime"/></xsl:call-template> 
					to <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/deliveryETA/endTime"/></xsl:call-template></b>. We’ll meet you at the door!<br/><br/>							
					</font></p>
			    </xsl:if>
				
				<p><b>Dear <xsl:value-of select="customer/firstName"/></b>,</p>
				
				<p style="margin: 10px 0 0 0;">
					We're busy picking and packing your order (<b>#<xsl:value-of select="order/erpSalesId"/></b>), and it's looking fresh and delicious.
					
					<xsl:choose>
						<xsl:when test="order/deliveryReservation/deliveryETA/emailETAenabled = 'true'">
							Its ETA is between 
								<b><xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/deliveryETA/startTime"/></xsl:call-template> 
									and <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/deliveryETA/endTime"/></xsl:call-template></b> 
								on <b><xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template></b>. 
								<xsl:if test="order/paymentMethod/paymentMethodType = 'EBT'">When your order arrives, please make sure you have your EBT card ready to complete the purchase. If you need more information on the EBT purchase process, <a href="http://www.freshdirect.com/category.jsp?catId=about_ebt">click here</a> to see our EBT Info Page. </xsl:if>
						</xsl:when>
						<xsl:otherwise>
							It will be delivered between 
								<b><xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template> 
									and <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/endTime"/></xsl:call-template></b> 
								on <b><xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template></b>. 
								<xsl:if test="order/paymentMethod/paymentMethodType = 'EBT'">When your order arrives, please make sure you have your EBT card ready to complete the purchase. If you need more information on the EBT purchase process, <a href="http://www.freshdirect.com/category.jsp?catId=about_ebt">click here</a> to see our EBT Info Page. </xsl:if>
						</xsl:otherwise>
					</xsl:choose>
				</p>
				
				<xsl:variable name="bundleShortItems" select="order/bundleShortItems/bundleShortItems"/>
				<xsl:variable name="bundleCompleteShort" select="order/bundleCompleteShort/bundleCompleteShort"/>
				<xsl:variable name="hasSubstitutes" select="order/hasSubstitutes"/>
				<xsl:choose>
					<xsl:when test="count(order/shortedItems/shortedItems) > 0 or count($bundleShortItems) > 0 or count(bundleCompleteShort) > 0">
						<p style="margin: 10px 0 0 0;">Unfortunately, one or more items that you ordered are unavailable. Where possible, we have substituted the unavailable item for a similar item.</p>
						
						<xsl:choose>
							<xsl:when test="count(order/shortedItems/shortedItems) > 0 and count($bundleShortItems) = 0 and count($bundleCompleteShort) = 0">
								<p>
									<xsl:call-template name="shortedItems" />
								</p>
							</xsl:when>
							<xsl:when test="count(order/shortedItems/shortedItems) > 0 and count($bundleShortItems) > 0 and count($bundleCompleteShort) = 0">
								<p>
									<xsl:call-template name="shortedItems"><xsl:with-param name="hasSubstitutes" select="$hasSubstitutes"/></xsl:call-template>
								</p>
								<p>
									<xsl:call-template name="bundleShortItems" />
								</p>
							</xsl:when>
							<xsl:when test="count(order/shortedItems/shortedItems) = 0 and count($bundleShortItems) > 0 and count($bundleCompleteShort) = 0">
								<p>
									<xsl:call-template name="bundleShortItems" />
								</p>
							</xsl:when>
							<xsl:when test="count(order/shortedItems/shortedItems) = 0 and count($bundleShortItems) = 0 and count($bundleCompleteShort) > 0">
								<p>
									<xsl:call-template name="bundleCompleteShort" />
								</p>
							</xsl:when>
							<xsl:when test="count(order/shortedItems/shortedItems) > 0 and count($bundleShortItems) = 0 and count($bundleCompleteShort) > 0">
								<p>
									<xsl:call-template name="shortedItems" />
								</p>
								<p>
									<xsl:call-template name="bundleCompleteShort" />
								</p>
							</xsl:when>
							<xsl:when test="count(order/shortedItems/shortedItems) = 0 and count($bundleShortItems) > 0 and count($bundleCompleteShort) > 0">
								<p>
									<xsl:call-template name="bundleShortItems" />
								</p>
								<p>
									<xsl:call-template name="bundleCompleteShort" />
								</p>
							</xsl:when>
							<xsl:when test="count(order/shortedItems/shortedItems) > 0 and count($bundleShortItems) > 0 and count($bundleCompleteShort) > 0">
								<p>
									<xsl:call-template name="shortedItems" />
								</p>
								<p>
									<xsl:call-template name="bundleShortItems" />
								</p>
								<p>
									<xsl:call-template name="bundleCompleteShort" />
								</p>
							</xsl:when>
						</xsl:choose>
						
						<p style="margin: 10px 0 0 0;">
							<xsl:choose>
								<xsl:when test="order/hasSubstitutes = 'true'"><b>Heads up! We've upgraded your substituted items at no additional cost. You have not been charged for any item(s) we were unable to substitute.</b></xsl:when>
								<xsl:otherwise><b>Heads up! You have not been charged for these unavailable item(s).</b></xsl:otherwise>
							</xsl:choose>
						</p>
													
						<xsl:choose>
							<xsl:when test="order/deliveryType = 'P'">
								<p style="margin: 10px 0 0 0;">
									The rest of your order (<b>#<xsl:value-of select="order/erpSalesId"/></b>)
									<xsl:call-template name="pickupText"></xsl:call-template>
								</p>
							</xsl:when>
							<xsl:when test="order/deliveryReservation/deliveryETA/emailETAenabled = 'true'">
								<p>The rest of the order (<b>#<xsl:value-of select="order/erpSalesId"/></b>) is on its way to you. Its ETA is between <b><xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/deliveryETA/startTime"/></xsl:call-template> 
										and <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/deliveryETA/endTime"/></xsl:call-template></b> 
										on <b><xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template></b>. 
										<xsl:if test="order/paymentMethod/paymentMethodType = 'EBT'"> When your order arrives, please make sure you have your EBT card ready to complete the purchase. </xsl:if>
										<xsl:if test="order/paymentMethod/paymentMethodType = 'EBT'"> If you need more information on the EBT purchase process, <a href="http://www.freshdirect.com/category.jsp?catId=about_ebt">click here</a> to see our EBT Info Page. </xsl:if>
								</p>
							</xsl:when>
							<xsl:otherwise>
								<p style="margin: 10px 0 0 0;">
									The rest of the order (<b>#<xsl:value-of select="order/erpSalesId"/></b>) is on its way to you. It will be delivered between <b><xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template> 
										and <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/endTime"/></xsl:call-template></b> 
										on <b><xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template></b>. 
										<xsl:if test="order/paymentMethod/paymentMethodType = 'EBT'"> When your order arrives, please make sure you have your EBT card ready to complete the purchase. </xsl:if>
										<xsl:if test="order/paymentMethod/paymentMethodType = 'EBT'"> If you need more information on the EBT purchase process, <a href="http://www.freshdirect.com/category.jsp?catId=about_ebt">click here</a> to see our EBT Info Page. </xsl:if>
								</p>
							</xsl:otherwise>
						</xsl:choose>
						
					</xsl:when>
				
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="order/deliveryType = 'P'">
								<p style="margin: 10px 0 0 0;">
									Hello again! Your order (<b>#<xsl:value-of select="order/erpSalesId"/></b>)
									<xsl:call-template name="pickupText"></xsl:call-template>
								</p>
							</xsl:when>
							
						</xsl:choose>
				
						<xsl:choose>
							<xsl:when test="order/paymentMethod/paymentType = 'M'">
								<p style="margin: 10px 0 0 0;">
									<b>Please note that you are not being charged for this order. The amount displayed below, as well as your account with FreshDirect, will reflect a zero balance within the next 48 hours.</b>
								</p>
							</xsl:when>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>
				
				<p style="margin: 10px 0 0 0;">
					<xsl:choose>
						<xsl:when test="order/paymentMethod/paymentMethodType = 'EBT'">
							Your EBT purchase total amount is <b>$<xsl:value-of select='format-number(order/ebtPurchaseAmount, "###,##0.00", "USD")'/></b>.
						</xsl:when>
						<xsl:otherwise>
							Your final order amount is <b>$<xsl:value-of select='format-number(order/invoicedTotal, "###,##0.00", "USD")'/></b>.
						</xsl:otherwise>
					</xsl:choose>
					<xsl:element name = "a"><xsl:attribute name = "href"><xsl:text>https://www.freshdirect.com/your_account/order_details.jsp?orderId=</xsl:text><xsl:value-of select="order/erpSalesId"/></xsl:attribute>Click here</xsl:element> to view your order details.
				</p>
				
				<p style="margin: 10px 0 0 0;">Remember that shopping your favorites is fast and easy with <a href="http://www.freshdirect.com/quickshop/qs_past_orders.jsp">Reorder</a>. You can also create custom <a href="http://www.freshdirect.com/quickshop/qs_shop_from_list.jsp">shopping lists</a> to make getting your go-to groceries a piece of cake.</p>
				
				<xsl:if test="count(order/shortedItems/shortedItems) > 0 or count($bundleShortItems) > 0 or count(bundleCompleteShort) > 0">
					<p style="margin: 10px 0 0 0;">If you're not 100% happy with your order, please contact us and we'll make it right.</p>
				</xsl:if>
				
				<p style="margin: 10px 0 0 0;">
					<b>Thanks for your shopping. Come back again soon!</b>
				</p>
				<p style="margin: 10px 0 0 0;">
					FreshDirect <xsl:choose><xsl:when test="order/deliveryType != 'C'">Customer Service Group</xsl:when><xsl:otherwise>Corporate Services Group</xsl:otherwise></xsl:choose>
				</p>
				
				<xsl:if test="order/deliveryType = 'H'">
					<p style="margin: 10px 0 0 0;"><a target="_blank" href="https://refer.freshdirect.com/orderreceiptemail2525"><img src="https://www.freshdirect.com/media/images/promotions/raf/RAF_email_216x42.jpg" alt="Refer A Friend" border="0" /></a></p>
				</xsl:if>
				
				<p><xsl:call-template name="h_invoice_info_v1"/></p>
		
				<p>NOTE: If this email does not print out clearly, please go to <xsl:element name = "a"><xsl:attribute name = "href"><xsl:text>https://www.freshdirect.com/your_account/order_details.jsp?orderId=</xsl:text><xsl:value-of select="order/erpSalesId"/></xsl:attribute><xsl:text>https://www.freshdirect.com/your_account/order_details.jsp?orderId=</xsl:text><xsl:value-of select="order/erpSalesId"/></xsl:element> for a printer-friendly version of your order details.</p>
	
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
	
<xsl:template name="substituteHeader">
	<xsl:if test="order/hasSubstitutes = 'true'">&nbsp;<em>(Substituted items in italics)</em>:</xsl:if>
</xsl:template>	
<xsl:template name="shortedItems">
	<xsl:param name="hasSubstitutes"/>
	<table width = "100%" cellspacing="0" cellpadding="0" align="center">
		<tr>
			<td colspan="3" style="margin: 0 0 10px 0;"><b>ITEMS NOT IN YOUR ORDER</b><xsl:call-template name="substituteHeader" /></td>
		</tr>
		<xsl:for-each select="order/shortedItems/shortedItems">
			<tr>
				<td width="20">&nbsp;</td>
				<td width="50">
					<xsl:value-of select="orderedQuantity" />/<xsl:choose>
						<xsl:when test="substituteProductName != '' and substitutedQuantity > 0">0</xsl:when>
						<xsl:otherwise><xsl:value-of select="deliveredQuantity" /></xsl:otherwise>
					</xsl:choose>
				</td>
				<td><b><xsl:value-of select="description" /></b><xsl:if test="configurationDesc != '' "><xsl:text> - </xsl:text>(<xsl:value-of select="configurationDesc"/>)</xsl:if></td>
			</tr>
			<xsl:if test="substituteProductName != '' and substitutedQuantity > 0">
				<tr>
					<td width="20">&nbsp;</td>
					<td></td>
					<td style="padding-left: 20px;"><em><xsl:value-of select="substitutedQuantity" />&nbsp;<xsl:value-of select="substituteProductName" />&nbsp;sent as substitute</em></td>
				</tr>
			</xsl:if>
		</xsl:for-each>
	</table>
</xsl:template>

<xsl:template name="bundleShortItems">
	<table width = "100%" cellspacing="0" cellpadding="0" align="center">
		<tr>
			<td colspan="3" style="margin: 0 0 10px 0;"><b>SOME ITEMS IN YOUR BUNDLE(S) ARE NOT AVAILABLE</b><xsl:call-template name="substituteHeader" /></td>
		</tr>
		<xsl:for-each select="order/bundleShortItems/bundleShortItems">
			<tr>
				<td width="20">&nbsp;</td>
				<td width="50">
					<xsl:value-of select="orderedQuantity" />/<xsl:choose>
						<xsl:when test="substituteProductName != '' and substitutedQuantity > 0">0</xsl:when>
						<xsl:otherwise><xsl:value-of select="deliveredQuantity" /></xsl:otherwise>
					</xsl:choose>
				</td>
				<td><b><xsl:value-of select="description" /></b><xsl:if test="configurationDesc != '' "><xsl:text> - </xsl:text>(<xsl:value-of select="configurationDesc"/>)</xsl:if></td>
			</tr>
			<xsl:if test="substituteProductName != '' and substitutedQuantity > 0">
				<tr>
					<td width="20">&nbsp;</td>
					<td></td>
					<td style="padding-left: 20px;"><em><xsl:value-of select="substitutedQuantity" />&nbsp;<xsl:value-of select="substituteProductName" />&nbsp;sent as substitute</em></td>
				</tr>
			</xsl:if>
		</xsl:for-each>
	</table>
</xsl:template>
	
<xsl:template name="bundleCompleteShort">
	<table width = "100%" cellspacing="0" cellpadding="0" align="center">
		<tr>
			<td colspan="3" style="margin: 0 0 10px 0;"><b>YOUR BUNDLE(S) ARE NOT AVAILABLE</b><xsl:call-template name="substituteHeader" /></td>
		</tr>
		<xsl:for-each select="order/bundleCompleteShort/bundleCompleteShort">
			<tr>
				<td width="20">&nbsp;</td>
				<td width="50">
					<xsl:value-of select="orderedQuantity" />/<xsl:choose>
						<xsl:when test="substituteProductName != '' and substitutedQuantity > 0">0</xsl:when>
						<xsl:otherwise><xsl:value-of select="deliveredQuantity" /></xsl:otherwise>
					</xsl:choose>
				</td>
				<td><b><xsl:value-of select="description" /></b><xsl:if test="configurationDesc != '' "><xsl:text> - </xsl:text>(<xsl:value-of select="configurationDesc"/>)</xsl:if></td>
			</tr>
			<xsl:if test="substituteProductName != '' and substitutedQuantity > 0">
				<tr>
					<td width="20">&nbsp;</td>
					<td></td>
					<td style="padding-left: 20px;"><em><xsl:value-of select="substitutedQuantity" />&nbsp;<xsl:value-of select="substituteProductName" />&nbsp;sent as substitute</em></td>
				</tr>
			</xsl:if>
		</xsl:for-each>
	</table>
</xsl:template>

<xsl:template name="pickupText">
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
	to pick up your order. You'll need to bring<xsl:if test="order/paymentMethod/paymentMethodType = 'EBT'"> your EBT card ready to complete the purchase and a valid </xsl:if> photo ID to pick up your food. Just present it to the attendant when you arrive.
	<xsl:if test="order/paymentMethod/paymentMethodType = 'EBT'"> If you need more information on the EBT purchase process, <a href="http://www.freshdirect.com/category.jsp?catId=about_ebt">click here</a> to see our EBT Info Page. </xsl:if>We hope you find everything absolutely fresh and delicious.
</xsl:template>

</xsl:stylesheet>
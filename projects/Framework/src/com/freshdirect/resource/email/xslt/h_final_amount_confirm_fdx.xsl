<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_invoice_info_fdx.xsl'/>
	<xsl:include href='h_header_fdx.xsl'/>
	<xsl:include href='h_common_functions_v2.xsl'/>
	<xsl:include href='h_footer_fdx.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<html lang="en-US" xml:lang="en-US">
	<head>
		<xsl:choose>
			<xsl:when test="count(order/shortedItems/shortedItems) + count(order/bundleShortItems/bundleShortItems) + count(order/bundleCompleteShort/bundleCompleteShort) = 1">
				<title>Heads Up! Item Missing From Your FoodKick Order</title>
			</xsl:when>
			<xsl:when test="count(order/shortedItems/shortedItems) + count(order/bundleShortItems/bundleShortItems) + count(order/bundleCompleteShort/bundleCompleteShort) > 1">
				<title>Heads Up! <xsl:value-of select="count(order/shortedItems/shortedItems) + count(order/bundleShortItems/bundleShortItems) + count(order/bundleCompleteShort/bundleCompleteShort)" /> Items Missing From Your Order</title>
			</xsl:when>
			<xsl:when test="count(order/shortedItems/shortedItems) + count(order/bundleShortItems/bundleShortItems) + count(order/bundleCompleteShort/bundleCompleteShort) = 0">
				<title>Your FoodKick Order Is on Its Way</title>
			</xsl:when>
		</xsl:choose>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
	</head>
	<body bgcolor="#CCCCCC">
		<div style="display:none; font-size: 1px; color: #333333; line-height: 1px; max-height: 0px; max-width: 0px; opacity: 0; overflow: hidden;">
			<xsl:choose>
				<xsl:when test="count(order/shortedItems/shortedItems) + count(order/bundleShortItems/bundleShortItems) + count(order/bundleCompleteShort/bundleCompleteShort) = 1">The rest of your order is on the way.</xsl:when>
				<xsl:when test="count(order/shortedItems/shortedItems) + count(order/bundleShortItems/bundleShortItems) + count(order/bundleCompleteShort/bundleCompleteShort) > 1">The rest of your order is on the way.</xsl:when>
				<xsl:when test="count(order/shortedItems/shortedItems) + count(order/bundleShortItems/bundleShortItems) + count(order/bundleCompleteShort/bundleCompleteShort) = 0">See you soon!</xsl:when>
			</xsl:choose>
		</div>
		
		<div style="background-color: #ffffff; border: 1px solid #bbbbbb; margin: 5px; padding: 20px;">
			<div style="margin: 0 0 15px 0;"><xsl:call-template name="h_header_fdx" /></div>
			
			<xsl:choose>
				<xsl:when test="count(order/shortedItems/shortedItems) + count(order/bundleShortItems/bundleShortItems) + count(order/bundleCompleteShort/bundleCompleteShort) > 0">
					<div style="color: #732484; font-size: 36px; font-weight: bold; margin: 15px 0;">Hey <xsl:value-of select="customer/firstName"/>,</div>
					
					<p>We're sorry that one or more of your items are now unavailable. We know this is inconvenient–and wanted to let you know ASAP.<br /><br />
						<table width="100%" cellspacing="0" cellpadding="0" align="center">
							<xsl:choose>
								<xsl:when test="count(order/shortedItems/shortedItems) > 0">
									<tr>
										<td colspan="3"><b>ITEMS NOT IN YOUR ORDER<br /><br /></b></td>
									</tr>
									<xsl:for-each select="order/shortedItems/shortedItems">
										<tr>
											<td width="50">&#160;</td>
											<td><xsl:value-of select="orderedQuantity - deliveredQuantity" />&#160;<xsl:value-of select="unitsOfMeasure" /></td>
											<td><b><xsl:value-of select="description" /></b><xsl:if test="configurationDesc != '' "><xsl:text> - </xsl:text>(<xsl:value-of select="configurationDesc"/>)</xsl:if></td>
										</tr>
										<tr>
											<td colspan="3">&#160;</td>
										</tr>
									</xsl:for-each>
								</xsl:when>
							</xsl:choose>
							
							<xsl:choose>
								<xsl:when test="count(order/bundleCompleteShort/bundleCompleteShort) > 0">
									<tr>
										<td colspan="3"><b>YOUR BUNDLE(S) ARE NOT AVAILABLE<br /><br /></b></td>
									</tr>
									<xsl:for-each select="order/bundleCompleteShort/bundleCompleteShort">
										<tr>
											<td width="50">&#160;</td>
											<td><xsl:value-of select="orderedQuantity - deliveredQuantity" />&#160;<xsl:value-of select="unitsOfMeasure" /></td>
											<td><b><xsl:value-of select="description" /></b><xsl:if test="configurationDesc != '' "><xsl:text> - </xsl:text>(<xsl:value-of select="configurationDesc"/>)</xsl:if></td>
										</tr>
										<tr>
											<td colspan="3">&#160;</td>
										</tr>
									</xsl:for-each>
								</xsl:when>
							</xsl:choose>
							
							<xsl:choose>
								<xsl:when test="count(order/bundleShortItems/bundleShortItems) > 0">
									<tr>
										<td colspan="3"><b>SOME ITEMS IN YOUR BUNDLE(S) ARE NOT AVAILABLE<br /><br /></b></td>
									</tr>
									<xsl:for-each select="order/bundleShortItems/bundleShortItems">
										<tr>
											<td width="50">&#160;</td>
											<td>Partial</td>
											<td><b><xsl:value-of select="description" /></b><xsl:if test="configurationDesc != '' "><xsl:text> - </xsl:text>(<xsl:value-of select="configurationDesc"/>)</xsl:if></td>
										</tr>
										<tr>
											<td colspan="3">&#160;</td>
										</tr>
									</xsl:for-each>
								</xsl:when>
							</xsl:choose>
						</table>
						
						<div style="margin: 15px 0; font-size: 16px;">
							The rest of your order <xsl:value-of select="order/erpSalesId"/> is on the way, and the final total is <xsl:choose><xsl:when test="order/invoicedTotal &lt;= order/total">$<xsl:value-of select='format-number(order/invoicedTotal, "###,##0.00", "USD")'/></xsl:when><xsl:otherwise>$<xsl:value-of select='format-number(order/invoicedTotal, "###,##0.00", "USD")'/></xsl:otherwise></xsl:choose>. Expect our kickers to arrive between <xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template> - <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/endTime" /></xsl:call-template> on <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template>.
							<br /><br />
							<a href="https://www.foodkick.com/#!/help/faq/iphonegeneral">Chat with us! We always want to make things right.</a>
							<br /><br />
							See ya soon,<br />
							Your FoodKick SideKicks<br />
							<span style="font-weight: bold;">#FoodKick</span>
						</div>
					</p>
				</xsl:when>
				<xsl:when test="count(order/shortedItems/shortedItems) + count(order/bundleShortItems/bundleShortItems) + count(order/bundleCompleteShort/bundleCompleteShort) = 0">
					<div style="color: #732484; font-size: 36px; font-weight: bold; margin: 15px 0;">Your FoodKick Order Is on Its Way.</div>
					<div style="margin: 15px 0; font-size: 16px;">
						(Is there any better phrase in the English language?) Expect our Kickers to arrive between <xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template> - <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/endTime" /></xsl:call-template>.<br />
						<br />
						Your final total is <xsl:choose><xsl:when test="order/invoicedTotal &lt;= order/total">$<xsl:value-of select='format-number(order/invoicedTotal, "###,##0.00", "USD")'/></xsl:when><xsl:otherwise>$<xsl:value-of select='format-number(order/invoicedTotal, "###,##0.00", "USD")'/></xsl:otherwise></xsl:choose>. Hope everything is absolutely Instagram-worthy.
						
						<br /><br />
						See ya soon,<br />
						Your FoodKick SideKicks<br />
						<span style="font-weight: bold;">#FoodKick</span>
					</div>
				</xsl:when>
			</xsl:choose>
			
			
			
			
			<!-- <div class="email-footer-block" style="height: 30px; margin: 10px 0;"><a class="email-body-button" href="#">ADD MORE STUFF</a></div> -->
		</div>
		
		<div style="margin: 15px 0;">
			<xsl:call-template name="h_invoice_info_fdx"/>
		</div>

		<xsl:call-template name="h_footer_fdx"/>
	</body>
</html>
</xsl:template>

</xsl:stylesheet>
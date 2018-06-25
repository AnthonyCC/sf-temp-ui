<?xml version="1.0"?>
<!DOCTYPE xsl:stylesheet [
    <!ENTITY nbsp "&#160;">
    <!ENTITY dot  "&#183;">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:import href='h_common_functions_v2.xsl'/>
	<xsl:output method="html"/>
	<xsl:template name="h_order_info_fdx_dp">
		<div style="background-color: #ffffff; border: 1px solid #bbbbbb; margin: 5px; padding: 20px;">
			<div style="color: #732484; font-size: 36px; font-weight: bold;">Order Information</div>
			<b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:14px;">for ORDER NUMBER <xsl:value-of select="order/erpSalesId"/></b>
			<br />
			<br />
			<table cellpadding="0" cellspacing="0" width="100%"  style="padding:0;margin:0;border-collapse:collapse;border-spacing:0;border-style:none;">
				<tr>
					<td width="49%" valign="top" style="font-family: Verdana, Arial, sans-serif;font-size:12px;">
						<b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;">ORDER TOTAL</b>
						<br />
						$<xsl:value-of select='format-number(order/total, "###,##0.00", "USD")'/>*<br />
						<br />
	
						<xsl:if test="order/totalAppliedGCAmount >0">
							<b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;">GIFT CARD AMOUNT TO BE APPLIED:</b><br />
							$<xsl:value-of select='format-number(order/totalAppliedGCAmount, "###,##0.00", "USD")'/>*<br />
							<br />
		
							<b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;">REMAINING GIFT CARD BALANCE:</b><br />
							$<xsl:value-of select='format-number(customer/userGiftCardsBalance, "###,##0.00", "USD")'/>*<br />
							<br />
						</xsl:if>
		
						<xsl:if test="order/paymentMethod/paymentType = 'M'">
							<b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;">AMOUNT DUE:</b><br />
							$0.00<br /><br />
						</xsl:if>
					
						<xsl:if test="order/paymentMethod/paymentMethodType != 'Gift-Card'">			
							<xsl:if test="order/totalAppliedGCAmount >0">
								<b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;">Amount to Be Charged to Your Account:</b><br />		
								$<xsl:value-of select='format-number((order/total)-(order/totalAppliedGCAmount)+(order/bufferAmt), "###,##0.00", "USD")' />*<br />
							</xsl:if>
		
							<br />
							<xsl:if test="order/paymentMethod/paymentType != 'M'">
								<b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;"><xsl:value-of select="order/paymentMethod/paymentMethodType"/></b><br />
								<xsl:call-template name="format-payment-method"><xsl:with-param name="paymentMethod" select="order/paymentMethod" /></xsl:call-template><br />
								<br />
							</xsl:if>
						
						</xsl:if>
					
					</td>
					<td width="2%"></td>
					<td width="30%" valign="top"></td>
				</tr>
				<tr>
					<td colspan="5">
						<xsl:variable name="countedFDW">
							<xsl:value-of select="count(order/orderViews/orderViews/orderLines/orderLines[affiliate = 'FDW'])" />
						</xsl:variable>
						<xsl:if test="$countedFDW > 0">		
							<div></div>
						</xsl:if>
					</td>
				</tr>
			</table>
			
		</div>
		<div style="background-color: #ffffff; border: 1px solid #bbbbbb; margin: 5px; padding: 20px;">
			<div style="color: #732484; font-size: 36px; font-weight: bold;">Your Order</div>
			<div style="height: 2px; line-height: 2px; background-color: #eeeeee;"></div>
			
			<xsl:for-each select="order/orderViews/orderViews">
				<table cellpadding="0" cellspacing="0" width="100%"  style="padding:0;margin:0;border-collapse:collapse;border-spacing:0;border-style:none;">
				<xsl:if test="'' != description">
					<tr>
						<td colspan="11" style="background-color: #732484; color: #fff; font-weight:bold; text-align:left;">
								<div style="margin: 10px; padding: 10px; font-size: 16px; font-weight: bold;"><xsl:value-of select="description"/></div>
						</td>
					</tr>
				</xsl:if>
				
				<xsl:for-each select="orderLines/orderLines">					
					<!-- PRODUCT LINE -->
					<tr><td colspan="11">
						<table width="100%" cellspacing="0" cellpadding="0" border="0"  style="padding: 0; margin: 0; border-collapse: collapse; border-spacing: 0; border-style: none;">
							<tr>
								<xsl:variable name="idCartlineId" select="concat('id',string(cartlineId))"/>
								<xsl:variable name="prodImgUri" select="normalize-space(//prodInfos/*[name()=$idCartlineId]/imageUri)"/>
								<xsl:variable name="smallcase" select="'abcdefghijklmnopqrstuvwxyz'" />
								<xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'" />
								<td rowspan="3" width="150">
									<xsl:if test="$prodImgUri != ''"><img><xsl:attribute name="src">https://www.freshdirect.com<xsl:value-of select="$prodImgUri" /></xsl:attribute></img></xsl:if></td>
								<td rowspan="3" valign="middle">
									<div style="font-weight: bold; font-size: 11px; margin: 6px 0;"><xsl:value-of select="translate(//prodInfos/*[name()=$idCartlineId]/brandName, $smallcase, $uppercase)" /></div>
									<div style="font-weight: bold; font-size: 13px; margin: 6px 0;"><xsl:value-of select="//prodInfos/*[name()=$idCartlineId]/nameNoBrand" /></div>
									<xsl:if test="configurationDesc != ''">
										<div style="font-size: 11px; margin: 6px 10px 6px 0;">(<xsl:value-of select="configurationDesc"/>)</div>
									</xsl:if>
									
								</td>
								<td align="right" style="font-weight: bold; font-size: 11px;">QTY&nbsp;<xsl:value-of select="displayQuantity"/></td>
							</tr>
							<tr>
								<td align="right">
									<span>
										<xsl:if test="not(starts-with(departmentDesc, 'FREE'))">
											<span>
												<xsl:if test="discount != ''"><xsl:attribute name="style">color: #f00;</xsl:attribute></xsl:if>
												(<xsl:value-of select="unitPrice"/>)
											</span>
										</xsl:if>
										<xsl:if test="starts-with(departmentDesc, 'FREE')"><span style="font-weight: bold; color: #f00;">FREE</span></xsl:if>
									</span>
								</td>
							</tr>
							<tr>
								<td align="right" style="font-family: Verdana, Arial, sans-serif; font-size: 11px; font-weight: bold;">
									<xsl:if test="not(starts-with(departmentDesc, 'FREE'))">
										<xsl:choose>
											<xsl:when test="discount != ''">
												<span style="color: #f00;">
													<xsl:value-of select="discount/promotionDescription"/>&nbsp;(You Saved <xsl:value-of select="format-number(discountAmount, '$###,##0.00', 'USD')"/>)
												</span>
											</xsl:when>
											<xsl:when test="number(groupQuantity) &gt; 0">
												Group Discount&nbsp;<span style="color:#f00">(You Saved <xsl:value-of select="format-number(groupScaleSavings, '$###,##0.00', 'USD')"/>)</span>
											</xsl:when>
											<xsl:when test="couponDiscount != ''">
												<span style="color:purple;">Saved&nbsp;<xsl:value-of select="format-number(couponDiscount/discountAmt, '$###,##0.00', 'USD')"/> with coupon</span>
											</xsl:when>
											<xsl:otherwise>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:if>
								
									
									<span style="font-weight: bold;">&nbsp;Price:&nbsp;</span>
									
									<span>
										<xsl:if test="not(starts-with(departmentDesc, 'FREE'))">
											<span>
												<xsl:if test="discount != ''"><xsl:attribute name="style">color: #f00;</xsl:attribute></xsl:if>
												<xsl:value-of select="format-number(price, '$###,##0.00', 'USD')"/>
											</span>
																					
											<xsl:if test="estimatedPrice = 'true'">*</xsl:if>
											<xsl:if test="tax = 'true'">&nbsp;T</xsl:if>
											<xsl:if test="scaledPricing = 'true'">&nbsp;S</xsl:if>
											<xsl:if test="depositValue = 'true'">&nbsp;D</xsl:if>
										</xsl:if>
										<xsl:if test="starts-with(departmentDesc, 'FREE')"><span style="font-weight: bold; color: #f00;">FREE</span></xsl:if>
									</span>
									
								</td>
							</tr>
						</table>
						
						<div style="height: 2px; line-height: 2px; background-color: #eeeeee; margin: 15px 0;"></div>
						
					</td></tr>
					<!-- END PRODUCT LINE -->
					
					
				</xsl:for-each>
					
				</table>
				
				<table cellpadding="0" cellspacing="0" width="100%"  style="padding:0;margin:0;border-collapse:collapse;border-spacing:0;border-style:none;">
					<tr><td colspan="3" style="height: 8px;">&nbsp;</td></tr>
					
					<tr class="orderViewSummary">
						<td colspan="1" align="right" style="font-family: Verdana, Arial, sans-serif;font-size:12px;"><xsl:value-of select="description"/><xsl:if test="estimatedPrice = 'true'"> Estimated</xsl:if> Subtotal:</td>
						<td colspan="1" align="right" width="75" style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;"><xsl:value-of select="format-number(subtotal, '$###,##0.00', 'USD')"/></td>
						<td colspan="1" width="10" style="font-family: Verdana, Arial, sans-serif;font-size:12px;"><xsl:if test="estimatedPrice = 'true'">*</xsl:if></td>
					</tr>
					<tr class="orderViewSummary">
						<td colspan="1" align="right" style="font-family: Verdana, Arial, sans-serif;font-size:12px;">Tax:</td>
						<td colspan="1" align="right" style="font-family: Verdana, Arial, sans-serif;font-size:12px;"><xsl:value-of select="format-number(tax, '$###,##0.00', 'USD')"/></td>
						<td colspan="1">&nbsp;</td>
					</tr>
				
					<tr class="orderViewSummary">
						<td colspan="1" align="right" style="font-family: Verdana, Arial, sans-serif;font-size:12px;">State Bottle Deposit:</td>
						<td colspan="1" align="right" style="font-family: Verdana, Arial, sans-serif;font-size:12px;"><xsl:value-of select="format-number(depositValue, '$###,##0.00', 'USD')"/></td>
						<td colspan="1">&nbsp;</td>
					</tr>
				</table>
			</xsl:for-each>
			
			
			<table cellpadding="0" cellspacing="0" width="100%" style="padding:0;margin:0;border-collapse:collapse;border-spacing:0;border-style:none;">
				<tr><td colspan="3" style="height: 8px;">&nbsp;</td></tr>
				<tr valign="top" class="orderSummary">
					<td colspan="1" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">Order Subtotal:</td>
					<td colspan="1" align="right" width="75"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"><xsl:value-of select="format-number(order/subTotal, '$###,##0.00', 'USD')"/></td>
					<td colspan="1" style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"><xsl:if test="order/estimatedPrice = 'true'">*</xsl:if></td>
				</tr>
				<xsl:if test="number(order/tip) &gt; 0">
					<tr valign="top" class="orderSummary">
						<td colspan="1" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">Tip :</td>
						<td colspan="1" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">
							<xsl:value-of select="format-number(order/tip, '$###,##0.00', 'USD')"/>							
						</td>
						<td colspan="1" style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"></td>
					</tr>
				</xsl:if>

				<xsl:if test="number(order/deliverySurcharge) &gt; 0">
					<tr valign="top" class="orderSummary">
						<td colspan="1" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">Delivery Fee<xsl:if test="order/deliveryChargeWaived = 'true'"> (waived)</xsl:if>:</td>
						<td colspan="1" align="right" width="60"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">
							<xsl:choose>
								<xsl:when test="order/deliveryChargeWaived = 'true'">$0.00</xsl:when>
								<xsl:otherwise><xsl:value-of select="format-number(order/deliveryCharge, '$###,##0.00', 'USD')"/></xsl:otherwise>
							</xsl:choose>
						</td>
						<td colspan="1" style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"><xsl:if test="order/deliveryChargeWaived = 'false'"><xsl:if test="order/deliveryChargeTaxable = 'true'"><b>T</b></xsl:if></xsl:if></td>
					</tr>
				</xsl:if>
			
				<xsl:if test="number(order/miscellaneousCharge) &gt; 0">
					<tr valign="top" class="orderSummary">
						<td colspan="1" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">Fuel Surcharge<xsl:if test="order/miscellaneousChargeWaived = 'true'"> (waived)</xsl:if>:</td>
						<td colspan="1" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">
							<xsl:choose>
								<xsl:when test="order/miscellaneousChargeWaived = 'true'">$0.00</xsl:when>
								<xsl:otherwise><xsl:value-of select="format-number(order/miscellaneousCharge, '$###,##0.00', 'USD')"/></xsl:otherwise>
							</xsl:choose>
						</td>
						<td colspan="1" style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"><xsl:if test="order/miscellaneousChargeWaived = 'false'"><xsl:if test="order/miscellaneousChargeTaxable = 'true'"><b>T</b></xsl:if></xsl:if></td>
					</tr>
				</xsl:if>
		
				<xsl:if test="number(order/taxValue) &gt; 0">
					<tr valign="top" class="orderSummary">
						<td colspan="1" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">Total Tax:</td>
						<td colspan="1" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"><xsl:value-of select="format-number(order/taxValue, '$###,##0.00', 'USD')"/></td>
						<td colspan="1"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"></td>
					</tr>
				</xsl:if>
		
				<xsl:if test="number(order/depositValue) &gt; 0">
					<tr valign="top" class="orderSummary">
						<td colspan="1" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">State Bottle Deposit:</td>
							<td colspan="1" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"><xsl:value-of select="format-number(order/depositValue, '$###,##0.00', 'USD')"/></td>
							<td colspan="1"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"></td>
					</tr>
				</xsl:if>
		
				<xsl:if test="order/phoneChargeWaived = 'false' and number(order/phoneCharge) &gt; 0">
					<tr valign="top" class="orderSummary">
						<td colspan="1" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">Phone Handling Fee:</td>
							<td colspan="1"  align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"><xsl:value-of select="format-number(order/phoneCharge, '$###,##0.00', 'USD')"/></td>
							<td colspan="1" style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"><xsl:if test="order/phoneChargeWaived = 'false'"><xsl:if test="order/miscellaneousChargeTaxable = 'true'"><b>T</b></xsl:if></xsl:if></td>
					</tr>
				</xsl:if>
			
				<xsl:if test="number(order/totalDiscountValue) &gt; 0">
					<xsl:for-each select="order/headerDiscounts/headerDiscounts">
						<tr valign="top" class="orderSummary">
							<td colspan="1" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"><xsl:value-of select='description'/>:</td>
							<td colspan="1" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">-<xsl:value-of select="format-number(model/discount/amount, '$###,##0.00', 'USD')"/></td>
							<td colspan="1"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"></td>
						</tr>
					</xsl:for-each>
				</xsl:if>
				<xsl:if test="order/RedeemedSampleDescription != 'NONE'">
					<tr valign="top" class="orderSummary">
						<td colspan="1" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"><xsl:value-of select='order/redeemedSampleDescription'/>:</td>
							<td colspan="1" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">FREE!</td>
							<td colspan="1"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"></td>
					</tr>
				</xsl:if>
			
				<xsl:if test="number(order/customerCreditsValue) &gt; 0">
					<tr valign="top" class="orderSummary">
						<td colspan="1" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">Credit Applied:</td>
							<td colspan="1" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">-<xsl:value-of select="format-number(order/customerCreditsValue, '$###,##0.00', 'USD')"/></td>
							<td colspan="1"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"></td>
					</tr>
				</xsl:if>

				<tr valign="top" style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #732484;color: white;">
					<td colspan="1" align="right" style="font-weight:bold;font-family: Verdana, Arial, sans-serif; font-size:12px; background-color: #732484; color: white; padding: 5px 0;">
						<xsl:choose>
							<xsl:when test="order/estimatedPrice = 'true'">ESTIMATED TOTAL</xsl:when>
							<xsl:otherwise>ORDER TOTAL</xsl:otherwise>
						</xsl:choose>:
					</td>
					<td colspan="1" width="60" align="right" style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #732484;color: white; padding: 5px 0;"><xsl:value-of select="format-number(order/total, '$###,##0.00', 'USD')"/></td>
					<td colspan="1" width="10" style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #732484;color: white; padding: 5px 0;"><xsl:if test="order/estimatedPrice = 'true'">*</xsl:if></td>
				</tr>
			
				<xsl:if test="number(order/chargeInvoiceTotal) &gt; 0">
					<tr valign="top">
						<td colspan="3"></td>
					</tr>
					<tr valign="top" class="orderViewSummary">
						<td colspan="1" align="right" style="font-family: Verdana, Arial, sans-serif;font-size:12px;">Returned Check Fee:</td>
						<td colspan="1"  align="right" style="font-family: Verdana, Arial, sans-serif;font-size:12px;"><xsl:value-of select="format-number(order/chargeInvoiceTotal, '$###,##0.00', 'USD')"/></td>
						<td colspan="1"></td>
					</tr>
				</xsl:if>
		
				<xsl:if test="order/paymentMethod/paymentType = 'M'">
					<tr valign="top" style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #732484;color: white;">
						<td colspan="1" align="right" style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #732484;color: white;">
							AMOUNT DUE:
						</td>
						<td colspan="1" width="60" align="right" style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #732484;color: white;">$0.00</td>
						<td colspan="1" width="10" style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #732484;color: white;"></td>
					</tr>
				</xsl:if>
			
			</table>
	
			<xsl:if test="order/estimatedPrice = 'true'">
				<div style="text-align: right; margin: 10px 0;">* Find out more about estimated prices.</div>
			</xsl:if>
			
			<xsl:if test="order/paymentMethod/paymentType = 'M'">
				<br />
				<b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;">Please note that you are not being charged for this order. The amount displayed above, as well as your account with FreshDirect, will reflect a zero balance within the next 48 hours.</b>
			</xsl:if>
		</div>
</xsl:template>

</xsl:stylesheet>

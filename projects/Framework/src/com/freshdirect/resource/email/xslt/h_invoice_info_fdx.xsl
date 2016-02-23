<?xml version="1.0"?>
<!DOCTYPE xsl:stylesheet [
    <!ENTITY nbsp "&#160;">
    <!ENTITY dot  "&#183;">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:import href='h_common_functions_v2.xsl'/>
	<xsl:output method="html"/>
	<xsl:template name="h_invoice_info_fdx">
		
		<div style="background-color: #ffffff; border: 1px solid #bbbbbb; margin: 5px; padding: 20px;">
			<div style="color: #732484; font-size: 36px; font-weight: bold;">Order Information</div>
			<b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:14px;">for ORDER NUMBER <xsl:value-of select="order/erpSalesId"/></b>
			<br />
			<br />
			<table cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td width="33%" valign="top"><font face="verdana, arial, sans-serif" size="1" color="black">
						<b>TIME</b><br/>
						<xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template><br/>
						<xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template>
						- <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/endTime" /></xsl:call-template><br/>
						<br/>
						<b>ADDRESS</b><br/>
						<xsl:value-of select="order/deliveryAddress/firstName"/><font> </font><xsl:value-of select="order/deliveryAddress/lastName"/><br/>
						<xsl:call-template name="format-delivery-address"><xsl:with-param name="dlvAddress" select="order/deliveryAddress" /></xsl:call-template>
						   
						<xsl:if test="order/deliveryType = 'P'">
						<br/>
						 <xsl:choose>
						       <xsl:when test="order/depotFacility = 'FreshDirect Pickup'">
						           <a href="http://www.freshdirect.com/help/delivery_lic_pickup.jsp" target="_blank">Click here for directions and details</a>
							</xsl:when>
							<xsl:otherwise>
						           <a href="http://www.freshdirect.com/help/delivery_hamptons.jsp" target="_blank">Click here for directions and details</a>
							</xsl:otherwise>
						</xsl:choose>
						</xsl:if>
			               
						<br/>
						<br/>
			
						<xsl:if test="order/deliveryAddress/altDelivery != 'none'">  
							<b>ALTERNATE DELIVERY</b><br/>
							<xsl:value-of select="order/deliveryAddress/altDelivery"/><br/>
							<xsl:if test="order/delivery-info/address/alt-delivery/@delivery-code = 'neighbor'"> 
								<xsl:value-of select="order/delivery-info/address/@alt-first-name"/><font> </font><xsl:value-of select="order/delivery-info/address/@alt-last-name"/><br/>
								Apt. #: <xsl:value-of select="order/delivery-info/address/@alt-apt"/><br/>
								Phone: <xsl:value-of select="order/delivery-info/address/alt-phone/@phone"/> Ext. <xsl:value-of select="order/delivery-info/address/alt-phone/@ext"/><br/>
							</xsl:if>		
							<br/>		
						</xsl:if>
			               
						<xsl:if test="order/deliveryAddress/instructions != 'none'">  
							<b>DELIVERY INSTRUCTIONS</b><br/>
							<xsl:value-of select="order/deliveryAddress/instructions"/><br/>
							<br/>		
						</xsl:if>
						</font>
					</td>
					
					<td width="2%">&nbsp;</td>
					<td width="33%" valign="top" style="color: #ffffff; font-weight:bold;"><font face="verdana, arial, sans-serif" size="1" color="black">
						<b>ORDER TOTAL</b><br/>
						$<xsl:value-of select='format-number(order/invoicedTotal, "###,##0.00", "USD")'/><br/>
						<br/>
						
						<xsl:if test="order/totalAppliedGCAmount >0">
						<b>GIFT CARD AMOUNT APPLIED:</b><br/>
						$<xsl:value-of select='format-number(order/totalAppliedGCAmount, "###,##0.00", "USD")'/>*<br/>
						<br/>			
						</xsl:if>
			
						<xsl:if test="order/paymentMethod/paymentType = 'M'">
						<b>AMOUNT DUE:</b><br/>
						$0.00<br/><br/>
						</xsl:if>
						
						<xsl:if test="order/paymentMethod/paymentMethodType != 'Gift-Card'">
							<xsl:if test="order/totalAppliedGCAmount >0">
							<b>Amount Charged to Your Account:</b><br/>		
							$<xsl:value-of select='format-number((order/invoicedTotal)-(order/totalAppliedGCAmount), "###,##0.00", "USD")' />*<br/>
							</xsl:if>
			
							<br/>
							<xsl:if test="order/paymentMethod/paymentType != 'M'">
							<b><xsl:value-of select="order/paymentMethod/paymentMethodType"/></b><br/>
							<xsl:call-template name="format-payment-method"><xsl:with-param name="paymentMethod" select="order/paymentMethod" /></xsl:call-template><br/>
							<br/>
							</xsl:if>
							
						</xsl:if>	
						
						</font>
					</td>
					<td width="2%">&nbsp;</td>
					<td width="30%" valign="top">&nbsp;</td>
				</tr>
				<tr>
					<td colspan="5">
						<xsl:variable name="countedFDW">
							<xsl:value-of select="count(order/invoicedOrderViews/invoicedOrderViews/orderLines/orderLines[affiliate = 'FDW'])" />
						</xsl:variable>
						<xsl:if test="$countedFDW > 0">		
							<div>I acknowledge that I have purchased alcohol from FreshDirect Wines &amp; Spirits, and that my credit card or checking account will be charged separately by "FreshDirect Wines".<br /><br /></div>
						</xsl:if>
					</td>
				</tr>
			</table>
			
			<table border="0" cellpadding="0" cellspacing="0" width="100%">
			     <tr>
				<td colspan="9" bgcolor="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1" /></td>
				</tr>
			     <tr>
				<td colspan="9"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8" /></td>
			     </tr>
			</table>
			
			<xsl:for-each select="order/invoicedOrderViews/invoicedOrderViews">
				<xsl:call-template name="orderlinesView">
					<xsl:with-param name="view" select="." />
				</xsl:call-template>
			</xsl:for-each>
			
			<table cellpadding="0" cellspacing="0" width="100%" border="0" style="padding:0;margin:0;border-collapse:collapse;border-spacing:0;border-style:none;">
				<tr>
					<td colspan="6"><div class="orderSeparator" /></td>
				</tr>
				
				<tr>				
					<td></td>
					<td width="75"></td>
					<td width="30"></td>
				</tr>
			
				<xsl:if test="number(order/totalDiscountValue) &gt; 0">
					<xsl:for-each select="order/headerDiscounts/headerDiscounts">
						<tr valign="top" class="orderSummary">
							<td colspan="1" align="right"><xsl:value-of select='description'/>:</td>
							<td colspan="1" align="right">-<xsl:value-of select="format-number(model/discount/amount, '$###,##0.00', 'USD')"/></td>
							<td colspan="1"></td>
						</tr>
					</xsl:for-each>
				</xsl:if>
				
				<xsl:if test="order/invoicedTaxValue > 0">
					<tr valign="top" class="orderSummary">
						<td colspan="1" align="right">Total Tax:</td>
						<td align="right">$<xsl:value-of select='format-number(order/invoicedTaxValue, "###,##0.00", "USD")'/></td>
						<td colspan="1">&nbsp;</td>
					</tr>
				</xsl:if>
			
				<xsl:if test="order/depositValue > 0">
					<tr valign="top" class="orderSummary">
						<td colspan="1" align="right">State Bottle Deposit:</td>
						<td align="right">$<xsl:value-of select='format-number(order/depositValue, "###,##0.00", "USD")'/></td>
						<td colspan="1">&nbsp;</td>
					</tr>
				</xsl:if>
				
				<xsl:if test="number(order/tip) &gt; 0">
					<tr valign="top" class="orderSummary">
						<td colspan="1" align="right">Tip :</td>
						<td align="right"><xsl:value-of select="format-number(order/tip, '$###,##0.00', 'USD')"/>							
						</td>
						<td colspan="1">&nbsp;</td>
					</tr>
				</xsl:if>
				
				<xsl:if test="number(order/deliverySurcharge) &gt; 0">
					<tr valign="top" class="orderSummary">
						<td colspan="1" align="right">Delivery Fee<xsl:if test="order/deliveryChargeWaived = 'true'"> (waived)</xsl:if>:</td>
						<td align="right">
							<xsl:choose>
								<xsl:when test="order/deliveryChargeWaived = 'true'">$0.00</xsl:when>
								<xsl:otherwise><xsl:value-of select="format-number(order/deliveryCharge, '$###,##0.00', 'USD')"/></xsl:otherwise>
							</xsl:choose>
						</td>
						<td colspan="1"><xsl:if test="order/deliveryChargeWaived = 'false'"><xsl:if test="order/deliveryChargeTaxable = 'true'"><b>T</b></xsl:if></xsl:if></td>
					</tr>
				</xsl:if>
				
				<xsl:if test="number(order/miscellaneousCharge) &gt; 0">
					<tr valign="top" class="orderSummary">
						<td colspan="1" align="right">Fuel Surcharge<xsl:if test="order/miscellaneousChargeWaived = 'true'"> (waived)</xsl:if>:</td>
						<td align="right">
							<xsl:choose>
								<xsl:when test="order/miscellaneousChargeWaived = 'true'">$0.00</xsl:when>
								<xsl:otherwise><xsl:value-of select="format-number(order/miscellaneousCharge, '$###,##0.00', 'USD')"/></xsl:otherwise>
							</xsl:choose>
						</td>
						<td colspan="1"><xsl:if test="order/miscellaneousChargeWaived = 'false' and order/miscellaneousChargeTaxable = 'true'"><b>T</b></xsl:if></td>
					</tr>
				</xsl:if>
				
				<xsl:if test="order/phoneChargeWaived = 'false' and order/phoneCharge > 0">
					<tr valign="top" class="orderSummary">
						<td colspan="1" align="right">Phone Handling Fee:</td>
						<td align="right">$<xsl:value-of select='format-number(order/phoneCharge, "###,##0.00", "USD")'/></td>
						<td colspan="1"><xsl:if test="order/miscellaneousChargeTaxable = 'true'"><b>T</b></xsl:if></td>
					</tr>
				</xsl:if>
				
				<xsl:if test="order/appliedCredits/appliedCredits/amount != '' and order/appliedCredits/appliedCredits/amount > 0">
					<tr valign="top" class="orderSummary">
						<td colspan="1" align="right">Credits:</td>
						<td align="right">
							($<xsl:value-of select='format-number(sum(order/appliedCredits/appliedCredits/amount), "###,##0.00", "USD")'/>)
						</td>
						<td colspan="1">&nbsp;</td>
					</tr>
				</xsl:if>
			        
				<tr valign="top" class="orderTotal" style="background-color: #732484; color: #ffffff; font-weight:bold;">
					<td colspan="1" align="right" style="background-color: #732484; color: #ffffff; font-weight:bold;"><b>ORDER TOTAL:</b></td>
					<td align="right" style="background-color: #732484; color: #ffffff; font-weight:bold;">
						<b>$<xsl:value-of select='format-number(order/invoicedTotal, "###,##0.00", "USD")'/></b>
					</td>
					<td colspan="1" style="background-color: #732484; color: #ffffff; font-weight:bold;">&nbsp;</td>
				</tr>
				<xsl:if test="order/paymentMethod/paymentMethodType = 'EBT'">
					<tr valign="top" class="orderTotal">
						<td colspan="1" align="right"><b>EBT PURCHASE TOTAL:</b></td>
						<td align="right">
							<xsl:choose>
								<xsl:when test="order/invoicedTotal &lt;= order/total">
									<b>$<xsl:value-of select='format-number(order/invoicedTotal, "###,##0.00", "USD")'/></b>
								</xsl:when>
								<xsl:otherwise>
									<b>$<xsl:value-of select='format-number(order/total, "###,##0.00", "USD")'/></b>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td colspan="1">&nbsp;</td>
					</tr>
				</xsl:if>
				
				<xsl:if test="order/paymentMethod/paymentType = 'M'">
					<tr valign="top" class="orderTotal">
						<td colspan="1" align="right"><b>AMOUNT DUE:</b></td>
						<td align="right">
							<b>$0.00</b>
						</td>
						<td colspan="1">&nbsp;</td>
					</tr>
				</xsl:if>
				
				<tr>
					<td colspan="3"><br/><br/></td>
				</tr>
				<tr valign="top">
					<td colspan="3" align="right"><b>T = Taxable Item</b></td>
				</tr>
				<tr valign="top">
					<td colspan="3" align="right"><b>S = Special Price</b></td>
				</tr>
				<tr valign="top">
					<td colspan="3" align="right"><b>D = State Bottle Deposit</b></td>
				</tr>
			</table>
		</div>
	</xsl:template>
				
	<xsl:template name="orderlinesView">
		<xsl:param name="view"/>
		<table cellpadding="0" cellspacing="0" width="100%" border="0" style="padding:0;margin:0;border-collapse:collapse;border-spacing:0;border-style:none;">	
			<xsl:if test="'' != description">
				<tr>
					<td colspan="11" style="background-color: #732484; color: #ffffff; font-weight:bold; text-align:left;">
						<div style="margin: 10px; padding: 10px; font-size: 16px; font-weight: bold; color: #ffffff;"><xsl:value-of select="description"/></div>
					</td>
				</tr>
			</xsl:if>
			<xsl:for-each select="$view/orderLines/orderLines">					
				<!-- PRODUCT LINE -->
					<tr><td colspan="9">
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
												<xsl:if test="pricedByLb='true'">
													<span><xsl:value-of select='concat(format-number(invoiceLine/weight, "###,##0.00"), " lb")' /> </span>
												</xsl:if>
												<xsl:if test="invoice/customizationPrice > 0">
													$<xsl:value-of select='format-number(invoice/customizationPrice, "###,##0.00", "USD")' /> 
												</xsl:if>
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
											<xsl:when test="number(invoiceLine/couponDiscountAmount) > 0">
												<span style="color:purple;">Saved&nbsp;<xsl:value-of select="format-number(invoiceLine/couponDiscountAmount, '$###,##0.00', 'USD')"/> with coupon</span>
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
												<xsl:value-of select="format-number(invoiceLine/price, '###,##0.00', 'USD')"/>
											</span>
											
											<xsl:if test="estimatedPrice = 'true'">*</xsl:if>
											
											<xsl:if test="invoiceLine/taxValue > 0"><b>&nbsp;T</b></xsl:if>
											<xsl:if test="scaledPricing = 'true'"><b>&nbsp;S</b></xsl:if>
											<xsl:if test="invoiceLine/depositValue  > 0"><b>&nbsp;D</b></xsl:if>
										</xsl:if>
										<xsl:if test="starts-with(departmentDesc, 'FREE')"><span style="font-weight: bold; color: #f00;">FREE</span></xsl:if>
									</span>
									
								</td>
							</tr>
						</table>
						
						<div style="height: 2px; line-height: 2px; background-color: #eeeeee; margin: 15px 0;"></div>
						
					</td></tr>
				<!-- PRODUCT LINE -->
			</xsl:for-each>
			
		</table>
		
		<table cellpadding="0" cellspacing="0" width="100%"  style="padding:0;margin:0;border-collapse:collapse;border-spacing:0;border-style:none;">
			<tr><td colspan="2" style="height: 8px;">&nbsp;</td></tr>
		
			<tr class="orderViewSummary">
				<td colspan="1" align="right">Subtotal:</td>
				<td colspan="1" align="right" width="75"><b><xsl:value-of select="format-number($view/subtotal, '$###,##0.00', 'USD')"/></b></td>
			</tr>
			
			<tr class="orderViewSummary">
				<td colspan="1" align="right">Tax:</td>
				<td colspan="1" align="right"><xsl:value-of select="format-number($view/tax, '$###,##0.00', 'USD')"/></td>
			</tr>
		
			<tr class="orderViewSummary">
				<td colspan="1" align="right">State Bottle Deposit:</td>
				<td colspan="1" align="right"><xsl:value-of select="format-number($view/depositValue, '$###,##0.00', 'USD')"/></td>
			</tr>
		</table>
	</xsl:template>


</xsl:stylesheet>
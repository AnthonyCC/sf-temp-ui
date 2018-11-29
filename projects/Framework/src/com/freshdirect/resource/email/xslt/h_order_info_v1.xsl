<?xml version="1.0"?>
<!DOCTYPE xsl:stylesheet [
    <!ENTITY nbsp "&#160;">
    <!ENTITY dot  "&#183;">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:output method="html"/>
	<xsl:template name="h_order_info_v1">
		<br/>
		<b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;"><span style="color:#FF9933">ORDER INFORMATION</span> for ORDER NUMBER <xsl:value-of select="order/erpSalesId"/></b><br/>
		<br/>
<table cellpadding="0" cellspacing="0" width="100%"  style="padding:0;margin:0;border-collapse:collapse;border-spacing:0;border-style:none;">
	<tr>
		<td width="100%" bgcolor="#CCCCCC"><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></td>
	</tr>
</table><br/>
<table cellpadding="0" cellspacing="0" width="100%"  style="padding:0;margin:0;border-collapse:collapse;border-spacing:0;border-style:none;">
	<tr>
		<td width="33%" valign="top" style="font-family: Verdana, Arial, sans-serif;font-size:12px;">
			<b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;">TIME</b><br/>
			<xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime" /></xsl:call-template><br/>
			<xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template> - <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/endTime" /></xsl:call-template><br/>
			<br/>
			<b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;">ADDRESS</b><br/>
			<xsl:value-of select="order/deliveryAddress/firstName"/><font><xsl:text> </xsl:text></font><xsl:value-of select="order/deliveryAddress/lastName"/><br/>
			<xsl:call-template name="format-delivery-address"><xsl:with-param name="dlvAddress" select="order/deliveryAddress" /></xsl:call-template><br/>
			<br/>
	
			<xsl:if test="order/deliveryAddress/instructions != 'none'">  
				<b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;">DELIVERY INSTRUCTIONS</b><br/>
				<xsl:value-of select="order/deliveryAddress/instructions"/><br/>
				<br/>		
			</xsl:if>

			<xsl:if  test="order/deliveryAddress/unattendedDeliveryFlag = 'OPT_IN'">
				<b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;">UNATTENDED DELIVERY</b><br/>

				<xsl:choose>
					<xsl:when test="order/deliveryAddress/unattendedDeliveryInstructions">
						<xsl:value-of select="order/deliveryAddress/unattendedDeliveryInstructions"/><br/>
					</xsl:when>
					<xsl:otherwise>
						OK<br/>
					</xsl:otherwise>
				</xsl:choose>
				<br/>
			</xsl:if>

			<xsl:if test="order/deliveryAddress/altDelivery != 'none'">
				<b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;">ALTERNATE DELIVERY</b><br/>
	
				<xsl:value-of select="order/deliveryAddress/altDelivery"/><br/>
	
					<xsl:if test="order/deliveryAddress/altDelivery = 'Deliver order to neighbor'">
						<xsl:value-of select="order/deliveryAddress/altFirstName"/> <xsl:value-of select="order/deliveryAddress/altLastName"/><br/>
						Apt. #: <xsl:value-of select="order/deliveryAddress/altApartment"/><br/>
						Phone: <xsl:value-of select="order/deliveryAddress/altPhone"/><br/>
					</xsl:if>
				<br/>
			</xsl:if>
	
		</td>
		<td width="2%"></td>
		<td width="33%" valign="top" style="font-family: Verdana, Arial, sans-serif;font-size:12px;">
			<b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;">ORDER TOTAL</b><br/>
			$<xsl:value-of select='format-number(order/total, "###,##0.00", "USD")'/>*<br/>
			<br/>

			<xsl:if test="order/totalAppliedGCAmount >0">
			<b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;">GIFT CARD AMOUNT TO BE APPLIED:</b><br/>
			$<xsl:value-of select='format-number(order/totalAppliedGCAmount, "###,##0.00", "USD")'/>*<br/>
			<br/>

			<b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;">REMAINING GIFT CARD BALANCE:</b><br/>
			$<xsl:value-of select='format-number(customer/userGiftCardsBalance, "###,##0.00", "USD")'/>*<br/>
			<br/>
			</xsl:if>

			<xsl:if test="order/paymentMethod/paymentType = 'M'">
			<b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;">AMOUNT DUE:</b><br/>
			$0.00<br/><br/>
			</xsl:if>
			
			<xsl:if test="order/paymentMethod/paymentMethodType != 'Gift-Card'">			
				<xsl:if test="order/totalAppliedGCAmount >0">
				<b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;">Amount to Be Charged to Your Account:</b><br/>		
				$<xsl:value-of select='format-number((order/total)-(order/totalAppliedGCAmount)+(order/bufferAmt), "###,##0.00", "USD")' />*<br/>
				</xsl:if>

				<br/>
				<xsl:if test="order/paymentMethod/paymentType != 'M'">
				<b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;"><xsl:value-of select="order/paymentMethod/paymentMethodType"/></b><br/>
				<xsl:call-template name="format-payment-method"><xsl:with-param name="paymentMethod" select="order/paymentMethod" /></xsl:call-template><br/>
				<br/>
				</xsl:if>
				
			</xsl:if>
			
			<xsl:variable name="countedUSQ">
				<xsl:value-of select="count(order/orderViews/orderViews/orderLines/orderLines[affiliate = 'USQ'])" />
			</xsl:variable>
			
			<xsl:if test="$countedUSQ > 0">			
			I acknowledge that I have purchased alcohol from Union Square Wines &amp; Spirits and that my credit card or checking account will be charged separately by UNION SQUARE WINE.<br/>
			<br/>
			</xsl:if>
			
			<xsl:variable name="countedFDW">
				<xsl:value-of select="count(order/orderViews/orderViews/orderLines/orderLines[affiliate = 'FDW'])" />
			</xsl:variable>
			
			<xsl:if test="$countedFDW > 0">			
			I acknowledge that I have purchased alcohol from FreshDirect Wines &amp; Spirits and that my credit card or checking account will be charged separately by "Fresh Direct Wines".<br/>
			<br/>
			</xsl:if>
		</td>
		<td width="2%"></td>
		<td width="30%" valign="top"></td>
	</tr>
</table>
<table cellpadding="0" cellspacing="0" width="100%"  style="padding:0;margin:0;border-collapse:collapse;border-spacing:0;border-style:none;">
	<tr>
		<td width="100%" bgcolor="#CCCCCC"><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></td>
	</tr>
</table><br/>

	<table width="100%" cellspacing="0" cellpadding="0" border="0"  style="padding:0;margin:0;border-collapse:collapse;border-spacing:0;border-style:none;">
<xsl:for-each select="order/orderViews/orderViews">
		<tr>
			<td colspan="11" style="background-color:#e0e3d0;font-weight:bold;text-align:left;">
				<xsl:if test="'' != description">
					<br/>&nbsp;&nbsp;<xsl:value-of select="description"/><br />
					<br />
				</xsl:if>
			</td>
		</tr>
		<xsl:if test="displayDepartment = 'false'">
		<tr><td colspan="11"><br /></td></tr>
		</xsl:if>
		
		<xsl:for-each select="orderLines/orderLines">
			<xsl:variable name="prevPos" select="position()-1"/>
			<xsl:if test="(../../displayDepartment = 'true') and ($prevPos = 0 or ../orderLines[$prevPos]/departmentDesc != departmentDesc)">
				<xsl:choose>
					<xsl:when test="starts-with(departmentDesc, 'Recipe: ')">
						<xsl:if test="not(starts-with(../orderLines[$prevPos]/departmentDesc, 'Recipe: '))">
							<tr>
								<td colspan="3"></td>
								<td colspan="8" style="color: #f93; font-weight: bold;font-family: Verdana, Arial, sans-serif;font-size:12px;"><br/>Recipes
								</td>
							</tr>
						</xsl:if>
						<tr>
							<td colspan="3"></td>
							<td colspan="8" style="font-weight: bold;font-family: Verdana, Arial, sans-serif;font-size:12px;"><xsl:value-of select="substring(departmentDesc, string-length('Recipe: ')+1)"/></td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td colspan="3"></td>
							<td colspan="8" style="color: #f93; font-weight: bold;font-family: Verdana, Arial, sans-serif;font-size:12px;"><br/><xsl:value-of select="departmentDesc"/></td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
			
			<tr valign="middle">
				<td align="right" width="40" style="font-family: Verdana, Arial, sans-serif;font-size:12px;">
					<xsl:value-of select="displayQuantity"/>
				</td>
				<td width="16"></td>
				<td width="22">&nbsp;<xsl:value-of select="label"/></td>
				<td colspan="2"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;">
					<b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;"><xsl:value-of select="description"/></b>
					<xsl:if test="configurationDesc != ''">
						&nbsp;(<xsl:value-of select="configurationDesc"/>)
					</xsl:if>
				</td>
  				<xsl:choose>
					<xsl:when test="discount != ''">
					<xsl:if test="not(starts-with(departmentDesc, 'FREE'))">
						<td width="70" align="right" style="font-family: Verdana, Arial, sans-serif;font-size:12px;color:red;">(<xsl:value-of select="format-number(basePrice, '$###,##0.00', 'USD')"/>)</td>                
                        <td width="60" align="right" style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;color:red;"><xsl:value-of select="format-number(price, '$###,##0.00', 'USD')"/></td>
                        <td width="10" style="font-family: Verdana, Arial, sans-serif;font-size:12px;"><xsl:if test="estimatedPrice = 'true'">*</xsl:if></td>
                        <td colspan="3" width="70" style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;"><xsl:if test="tax = 'true'">&nbsp;T</xsl:if><xsl:if test="scaledPricing = 'true'">&nbsp;S</xsl:if><xsl:if test="depositValue = 'true'">&nbsp;D</xsl:if></td>                    
                   </xsl:if>
                   <xsl:if test="starts-with(departmentDesc, 'FREE')">
                    <td width="70" align="right" style="font-family: Verdana, Arial, sans-serif;font-size:12px;color:red;"><xsl:text>  </xsl:text></td>                
                    <td width="60" align="right" style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;color:red;"><xsl:text> FREE </xsl:text></td>
                   </xsl:if>
                    </xsl:when>	
		   	
					<xsl:otherwise>
					<xsl:if test="not(starts-with(departmentDesc, 'FREE'))">
						<td width="70" align="right" style="font-family: Verdana, Arial, sans-serif;font-size:12px;">(<xsl:value-of select="format-number(basePrice, '$###,##0.00', 'USD')"/>)</td>                
                        <td width="60" align="right" style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;"><xsl:value-of select="format-number(price, '$###,##0.00', 'USD')"/></td>
                        <td width="10" style="font-family: Verdana, Arial, sans-serif;font-size:12px;"><xsl:if test="estimatedPrice = 'true'">*</xsl:if></td>
                        <td colspan="3" width="70" style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;"><xsl:if test="tax = 'true'">&nbsp;T</xsl:if><xsl:if test="scaledPricing = 'true' or number(groupQuantity) &gt; 0">&nbsp;S</xsl:if><xsl:if test="depositValue = 'true'">&nbsp;D</xsl:if></td>                    
                   </xsl:if>
                   <xsl:if test="starts-with(departmentDesc, 'FREE')">
                     <td width="70" align="right" style="font-family: Verdana, Arial, sans-serif;font-size:12px;color:red;"><xsl:text>  </xsl:text></td>                
                    <td width="60" align="right" style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;color:red;"><xsl:text> FREE </xsl:text></td>
                   </xsl:if>
                    </xsl:otherwise>
				</xsl:choose>
			</tr>
			<xsl:choose>
			<xsl:when test="discount != ''">
			<tr valign="middle">
				<td align="right" width="40"></td>
				<td width="16"></td>
				<td width="22"></td>
				<td colspan="2" style="font-family: Verdana, Arial, sans-serif;font-size:12px;color:red;">
				<xsl:if test="not(starts-with(departmentDesc, 'FREE'))">
				<xsl:value-of select="discount/promotionDescription"/>&nbsp;(You Saved <xsl:value-of select="format-number(discountAmount, '$###,##0.00', 'USD')"/>)
				</xsl:if>
				</td>
				<td width="70" align="right"></td>
				<td width="60" align="right"></td>
				<td width="10"></td>
				<td width="3" colspan="3"></td>
			</tr>
			</xsl:when>
			<xsl:when test="number(groupQuantity) &gt; 0">
			<tr valign="middle">
				<td align="right" width="40"></td>
				<td width="16"></td>
				<td width="22"></td>
				<td colspan="2" style="font-family: Verdana, Arial, sans-serif;font-size:12px;">
				<xsl:if test="not(starts-with(departmentDesc, 'FREE'))">
				Group Discount&nbsp;<span style="color:red">(You Saved <xsl:value-of select="format-number(groupScaleSavings, '$###,##0.00', 'USD')"/>)</span>
				</xsl:if>
				</td>
				<td width="70" align="right"></td>
				<td width="60" align="right"></td>
				<td width="10"></td>
				<td width="3" colspan="3"></td>
			</tr>
			</xsl:when>
			<xsl:when test="couponDiscount != ''">
			<tr valign="middle">
				<td align="right" width="40"></td>
				<td width="16"></td>
				<td width="22"></td>
				<td colspan="2" style="font-family: Verdana, Arial, sans-serif;font-size:12px;color:purple;">
				<xsl:if test="not(starts-with(departmentDesc, 'FREE'))">
				Saved&nbsp;<xsl:value-of select="format-number(couponDiscount/discountAmt, '$###,##0.00', 'USD')"/> with coupon
				</xsl:if>
				</td>
				<td width="70" align="right"></td>
				<td width="60" align="right"></td>
				<td width="10"></td>
				<td width="3" colspan="3"></td>
			</tr>
			</xsl:when>
			<xsl:otherwise>
			</xsl:otherwise>
				</xsl:choose>
		</xsl:for-each>	
		
		<tr><td colspan="11" style="height: 8px;">&nbsp;
		</td></tr>
	
		<tr class="orderViewSummary">
			<td colspan="6" align="right" style="font-family: Verdana, Arial, sans-serif;font-size:12px;"><xsl:value-of select="description"/><xsl:if test="estimatedPrice = 'true'"> Estimated</xsl:if> Subtotal:</td>
			<td colspan="1" align="right" style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;"><xsl:value-of select="format-number(subtotal, '$###,##0.00', 'USD')"/></td>
			<td colspan="1" width="10" style="font-family: Verdana, Arial, sans-serif;font-size:12px;"><xsl:if test="estimatedPrice = 'true'">*</xsl:if></td>
			<td colspan="3">&nbsp;</td>
		</tr>
		
		<tr class="orderViewSummary">
			<td colspan="6" align="right" style="font-family: Verdana, Arial, sans-serif;font-size:12px;">Tax:</td>
			<td colspan="1" align="right" style="font-family: Verdana, Arial, sans-serif;font-size:12px;"><xsl:value-of select="format-number(tax, '$###,##0.00', 'USD')"/></td>
			<td colspan="4">&nbsp;</td>
		</tr>

		<tr class="orderViewSummary">
			<td colspan="6" align="right" style="font-family: Verdana, Arial, sans-serif;font-size:12px;">State Bottle Deposit:</td>
			<td colspan="1" align="right" style="font-family: Verdana, Arial, sans-serif;font-size:12px;"><xsl:value-of select="format-number(depositValue, '$###,##0.00', 'USD')"/></td>
			<td colspan="4">&nbsp;</td>
		</tr>

</xsl:for-each>
		<tr><td colspan="11" style="height: 8px;">&nbsp;
		</td></tr>
	
	
	<tr valign="top" class="orderSummary">
		<td colspan="4">&nbsp;</td>
		<td colspan="2" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">Order Subtotal:</td>
		<td colspan="1" align="right" width="60"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"><xsl:value-of select="format-number(order/subTotal, '$###,##0.00', 'USD')"/></td>
		<td colspan="4" style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"><xsl:if test="order/estimatedPrice = 'true'">*</xsl:if></td>
	</tr>
	
	<xsl:if test="number(order/deliverySurcharge) &gt; 0">
		<tr valign="top" class="orderSummary">
		<td colspan="4">&nbsp;</td>
		<td colspan="2" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">Delivery Fee<xsl:if test="order/deliveryChargeWaived = 'true'"> (waived)</xsl:if>:</td>
			<td colspan="1" align="right" width="60"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">
				<xsl:choose>
					<xsl:when test="order/deliveryChargeWaived = 'true'">$0.00</xsl:when>
					<xsl:otherwise><xsl:value-of select="format-number(order/deliveryCharge, '$###,##0.00', 'USD')"/></xsl:otherwise>
				</xsl:choose>
			</td>
			<td  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"></td>
			<td colspan="3"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"><xsl:if test="order/deliveryChargeWaived = 'false'"><xsl:if test="order/deliveryChargeTaxable = 'true'"><b>T</b></xsl:if></xsl:if></td>
		</tr>
	</xsl:if>
	
	<xsl:if test="number(order/tip) &gt; 0">
		<tr valign="top" class="orderSummary">
		    <td colspan="4">&nbsp;</td>
			<td colspan="2" align="right" style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">Tip :</td>
			<td colspan="1" align="right" style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">
				<xsl:value-of select="format-number(order/tip, '$###,##0.00', 'USD')" />
			</td>
			<td colspan="3" style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"></td>
		</tr>
	</xsl:if>

	
	<xsl:if test="number(order/miscellaneousCharge) &gt; 0">
		<tr valign="top" class="orderSummary">
		<td colspan="4">&nbsp;</td>
		<td colspan="2" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">Fuel Surcharge<xsl:if test="order/miscellaneousChargeWaived = 'true'"> (waived)</xsl:if>:</td>
			<td colspan="1" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">
				<xsl:choose>
					<xsl:when test="order/miscellaneousChargeWaived = 'true'">$0.00</xsl:when>
					<xsl:otherwise><xsl:value-of select="format-number(order/miscellaneousCharge, '$###,##0.00', 'USD')"/></xsl:otherwise>
				</xsl:choose>
			</td>
			<td  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"></td>
			<td colspan="3"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"><xsl:if test="order/miscellaneousChargeWaived = 'false'"><xsl:if test="order/miscellaneousChargeTaxable = 'true'"><b>T</b></xsl:if></xsl:if></td>
		</tr>
	</xsl:if>

	<xsl:if test="number(order/taxValue) &gt; 0">
		<tr valign="top" class="orderSummary">
		<td colspan="4">&nbsp;</td>
		<td colspan="2"  align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">Total Tax:</td>
			<td colspan="1" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"><xsl:value-of select="format-number(order/taxValue, '$###,##0.00', 'USD')"/></td>
			<td colspan="4"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"></td>
		</tr>
	</xsl:if>

	<xsl:if test="number(order/depositValue) &gt; 0">
		<tr valign="top" class="orderSummary">
		<td colspan="4">&nbsp;</td>
		<td colspan="2"  align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">State Bottle Deposit:</td>
			<td colspan="1" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"><xsl:value-of select="format-number(order/depositValue, '$###,##0.00', 'USD')"/></td>
			<td colspan="4"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"></td>
		</tr>
	</xsl:if>

	<xsl:if test="order/phoneChargeWaived = 'false' and number(order/phoneCharge) &gt; 0">
		<tr valign="top" class="orderSummary">
		<td colspan="4">&nbsp;</td>
		<td colspan="2" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">Phone Handling Fee:</td>
			<td colspan="1"  align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"><xsl:value-of select="format-number(order/phoneCharge, '$###,##0.00', 'USD')"/></td>
			<td  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"></td>
			<td colspan="3"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"><xsl:if test="order/phoneChargeWaived = 'false'"><xsl:if test="order/miscellaneousChargeTaxable = 'true'"><b>T</b></xsl:if></xsl:if></td>
		</tr>
	</xsl:if>
	
	<xsl:if test="number(order/totalDiscountValue) &gt; 0">
		<xsl:for-each select="order/headerDiscounts/headerDiscounts">
			<tr valign="top" class="orderSummary">
		<td colspan="4">&nbsp;</td>
		<td colspan="2" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"><xsl:value-of select='description'/>:</td>
				<td colspan="1" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">-<xsl:value-of select="format-number(model/discount/amount, '$###,##0.00', 'USD')"/></td>
				<td colspan="4"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"></td>
			</tr>
		</xsl:for-each>
	</xsl:if>
	<xsl:if test="order/RedeemedSampleDescription != 'NONE'">
		<tr valign="top" class="orderSummary">
		<td colspan="4">&nbsp;</td>
		<td colspan="2" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"><xsl:value-of select='order/redeemedSampleDescription'/>:</td>
			<td colspan="1" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">FREE!</td>
			<td colspan="4"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"></td>
		</tr>
	</xsl:if>
	
	<xsl:if test="number(order/customerCreditsValue) &gt; 0">
		<tr valign="top" class="orderSummary">
		<td colspan="4">&nbsp;</td>
		<td colspan="2" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">Credit Applied:</td>
			<td colspan="1" align="right"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;">-<xsl:value-of select="format-number(order/customerCreditsValue, '$###,##0.00', 'USD')"/></td>
			<td colspan="4"  style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #e0e3d0;"></td>
		</tr>
	</xsl:if>

	<tr valign="top" class="orderTotal">
		<td colspan="4">&nbsp;</td>
		<td colspan="2" align="right" style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #969;color: white;">
			<xsl:choose>
				<xsl:when test="order/estimatedPrice = 'true'">ESTIMATED TOTAL</xsl:when>
				<xsl:otherwise>ORDER TOTAL</xsl:otherwise>
			</xsl:choose>:
		</td>
		<td colspan="1" width="60" align="right" style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #969;color: white;"><xsl:value-of select="format-number(order/total, '$###,##0.00', 'USD')"/></td>
		<td colspan="1" width="10" style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #969;color: white;"><xsl:if test="order/estimatedPrice = 'true'">*</xsl:if></td>
		<td colspan="3" width="70" style="font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #969;color: white;"></td>
	</tr>
	
	<xsl:if test="number(order/chargeInvoiceTotal) &gt; 0">
		<tr valign="top">
			<td colspan="11"></td>
		</tr>
		<tr valign="top" class="orderViewSummary">
		<td colspan="4">&nbsp;</td>
		<td colspan="2" align="right" style="font-family: Verdana, Arial, sans-serif;font-size:12px;">Returned Check Fee:</td>
			<td colspan="1"  align="right" style="font-family: Verdana, Arial, sans-serif;font-size:12px;"><xsl:value-of select="format-number(order/chargeInvoiceTotal, '$###,##0.00', 'USD')"/></td>
			<td colspan="4"></td>
		</tr>
	</xsl:if>

	<xsl:if test="order/paymentMethod/paymentType = 'M'">
	<tr valign="top" class="orderTotal">
		<td colspan="4">&nbsp;</td>
		<td colspan="2" align="right" style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #969;color: white;">
			AMOUNT DUE:
		</td>
		<td colspan="1" width="60" align="right" style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #969;color: white;">$0.00</td>
		<td colspan="1" width="10" style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #969;color: white;"></td>
		<td colspan="3" width="70" style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;background-color: #969;color: white;"></td>
	</tr>
	</xsl:if>
	
	<xsl:if test="order/estimatedPrice = 'true'">
		<tr valign="top">
			<td colspan="11" align="right" style="font-family: Verdana, Arial, sans-serif;font-size:12px;">
				<br/>
				* Find out more about <a href="http://www.freshdirect.com/help/faq_home.jsp?page=shopping#question4"><span style="color:#360;text-decoration:underline;font-family: Verdana, Arial, sans-serif;font-size:12px;">estimated</span></a> prices.
			</td>
		</tr>
	</xsl:if>
</table>

<xsl:if test="order/paymentMethod/paymentType = 'M'">
				<br/>
				<b style="font-weight:bold;font-family: Verdana, Arial, sans-serif;font-size:12px;">Please note that you are not being charged for this order. The amount displayed above, as well as your account with FreshDirect, will reflect a zero balance within the next 48 hours.</b>
</xsl:if>

</xsl:template>

</xsl:stylesheet>

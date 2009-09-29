<?xml version="1.0"?>
<!DOCTYPE xsl:stylesheet [
    <!ENTITY nbsp "&#160;">
    <!ENTITY dot  "&#183;">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:output method="html"/>
	<xsl:template name="h_order_info_v1">
		<br/>
		<b><font color="#FF9933">ORDER INFORMATION</font> for ORDER NUMBER <xsl:value-of select="order/erpSalesId"/></b><br/>
<table cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td width="100%" bgcolor="#CCCCCC"><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></td>
	</tr>
</table><br/>
<table cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td width="33%" valign="top"><font face="verdana, arial, sans-serif" size="1" color="black">
			<b>TIME</b><br/>
			<xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime" /></xsl:call-template><br/>
			<xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template> - <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/endTime" /></xsl:call-template><br/>
			<br/>
			<b>ADDRESS</b><br/>
			<xsl:value-of select="order/deliveryAddress/firstName"/><font><xsl:text> </xsl:text></font><xsl:value-of select="order/deliveryAddress/lastName"/><br/>
			<xsl:call-template name="format-delivery-address"><xsl:with-param name="dlvAddress" select="order/deliveryAddress" /></xsl:call-template><br/>
			<br/>
	
			<xsl:if test="order/deliveryAddress/instructions != 'none'">  
				<b>DELIVERY INSTRUCTIONS</b><br/>
				<xsl:value-of select="order/deliveryAddress/instructions"/><br/>
				<br/>		
			</xsl:if>

			<xsl:if  test="order/deliveryAddress/unattendedDeliveryFlag = 'OPT_IN'">
				<b>UNATTENDED DELIVERY</b><br/>

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
				<b>ALTERNATE DELIVERY</b><br/>
	
				<xsl:value-of select="order/deliveryAddress/altDelivery"/><br/>
	
					<xsl:if test="order/deliveryAddress/altDelivery = 'Deliver order to neighbor'">
						<xsl:value-of select="order/deliveryAddress/altFirstName"/><font> </font><xsl:value-of select="order/deliveryAddress/altLastName"/><br/>
						Apt. #: <xsl:value-of select="order/deliveryAddress/altApartment"/><br/>
						Phone: <xsl:value-of select="order/deliveryAddress/altPhone"/><br/>
					</xsl:if>
				<br/>
			</xsl:if>
	
			</font>
		</td>
		<td width="2%"></td>
		<td width="33%" valign="top"><font face="verdana, arial, sans-serif" size="1" color="black">
			<b>ORDER TOTAL</b><br/>
			$<xsl:value-of select='format-number(order/total, "###,##0.00", "USD")'/>*<br/>
			<br/>

			<xsl:if test="order/totalAppliedGCAmount >0">
			<b>GIFT CARD AMOUNT TO BE APPLIED:</b><br/>
			$<xsl:value-of select='format-number(order/totalAppliedGCAmount, "###,##0.00", "USD")'/>*<br/>
			<br/>

			<b>REMAINING GIFT CARD BALANCE:</b><br/>
			$<xsl:value-of select='format-number(customer/userGiftCardsBalance, "###,##0.00", "USD")'/>*<br/>
			<br/>
			</xsl:if>

			<xsl:if test="order/paymentMethod/paymentType = 'M'">
			<b>AMOUNT DUE:</b><br/>
			$0.00<br/><br/>
			</xsl:if>
			
			<xsl:if test="order/paymentMethod/paymentMethodType != 'Gift-Card'">			
				<xsl:if test="order/totalAppliedGCAmount >0">
				<b>Amount to Be Charged to Your Account:</b><br/>		
				$<xsl:value-of select='format-number((order/total)-(order/totalAppliedGCAmount)+(order/bufferAmt), "###,##0.00", "USD")' />*<br/>
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
		<td width="2%"></td>
		<td width="30%" valign="top"></td>
	</tr>
</table>
<table cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td width="100%" bgcolor="#CCCCCC"><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></td>
	</tr>
</table><br/>

<xsl:for-each select="order/orderViews/orderViews">
	<table width="100%" cellspacing="0" cellpadding="0" border="0">
		<tr>
			<td colspan="11">
				<xsl:if test="'' != description">
					<br/><div class="orderViewHeader"><xsl:value-of select="description"/></div>
					<xsl:if test="displayDepartment = 'false'"><br/></xsl:if>
				</xsl:if>
			</td>
		</tr>
		
		<xsl:for-each select="orderLines/orderLines">
			<xsl:variable name="prevPos" select="position()-1"/>
			<xsl:if test="(../../displayDepartment = 'true') and ($prevPos = 0 or ../orderLines[$prevPos]/departmentDesc != departmentDesc)">
				<xsl:choose>
					<xsl:when test="starts-with(departmentDesc, 'Recipe: ')">
						<xsl:if test="not(starts-with(../orderLines[$prevPos]/departmentDesc, 'Recipe: '))">
							<tr>
								<td colspan="3"></td>
								<td colspan="8" style="color: #f93; font-weight: bold;"><br/>&nbsp;&nbsp;Recipes
								</td>
							</tr>
						</xsl:if>
						<tr>
							<td colspan="3"></td>
							<td colspan="8" style="font-weight: bold;">&nbsp;&nbsp;<xsl:value-of select="substring(departmentDesc, string-length('Recipe: ')+1)"/></td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td colspan="3"></td>
							<td colspan="8" style="color: #f93; font-weight: bold;"><br/>&nbsp;&nbsp;<xsl:value-of select="departmentDesc"/></td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
			
			<tr valign="middle">
				<td align="right" width="40">
					<xsl:value-of select="displayQuantity"/>
				</td>
				<td width="16"></td>
				<td width="22">&nbsp;<xsl:value-of select="label"/></td>
				<td colspan="2">
					<div style="margin-left:16px; text-indent:-8px;">
						<span class="text10bold"><xsl:value-of select="description"/></span>
						<xsl:if test="configurationDesc != ''">
							&nbsp;(<xsl:value-of select="configurationDesc"/>)
						</xsl:if>
					</div>
				</td>
  				<xsl:choose>
					<xsl:when test="discount != ''">
                        <td width="70" align="right"><font color="red">(<xsl:value-of select="unitPrice"/>)</font></td>                
                        <td width="60" align="right"><span class="text10bold"><font color="red"><xsl:value-of select="format-number(price, '$###,##0.00', 'USD')"/></font></span></td>
                        <td width="10"><xsl:if test="estimatedPrice = 'true'">*</xsl:if></td>
                        <td colspan="3" width="70"><b><xsl:if test="tax = 'true'">&nbsp;T</xsl:if><xsl:if test="scaledPricing = 'true'">&nbsp;S</xsl:if><xsl:if test="depositValue = '
                        true'">&nbsp;D</xsl:if></b></td>                    
                    </xsl:when>
					<xsl:otherwise>
                        <td width="70" align="right">(<xsl:value-of select="unitPrice"/>)</td>                
                        <td width="60" align="right"><span class="text10bold"><xsl:value-of select="format-number(price, '$###,##0.00', 'USD')"/></span></td>
                        <td width="10"><xsl:if test="estimatedPrice = 'true'">*</xsl:if></td>
                        <td colspan="3" width="70"><b><xsl:if test="tax = 'true'">&nbsp;T</xsl:if><xsl:if test="scaledPricing = 'true'">&nbsp;S</xsl:if><xsl:if test="depositValue = '
                        true'">&nbsp;D</xsl:if></b></td>                    
                    </xsl:otherwise>
				</xsl:choose>
			</tr>
			
		</xsl:for-each>	
		
		<tr><td colspan="11">
			<div class="orderViewSeparator" />
		</td></tr>
	
		<tr class="orderViewSummary">
			<td colspan="6" align="right"><xsl:value-of select="description"/><xsl:if test="estimatedPrice = 'true'"> Estimated</xsl:if> Subtotal:</td>
			<td colspan="1" align="right"><b><xsl:value-of select="format-number(subtotal, '$###,##0.00', 'USD')"/></b></td>
			<td colspan="1" width="10"><xsl:if test="estimatedPrice = 'true'">*</xsl:if></td>
			<td colspan="3">&nbsp;</td>
		</tr>
		
		<tr class="orderViewSummary">
			<td colspan="6" align="right">Tax:</td>
			<td colspan="1" align="right"><xsl:value-of select="format-number(tax, '$###,##0.00', 'USD')"/></td>
			<td colspan="4">&nbsp;</td>
		</tr>

		<tr class="orderViewSummary">
			<td colspan="6" align="right">Bottle Deposit:</td>
			<td colspan="1" align="right"><xsl:value-of select="format-number(depositValue, '$###,##0.00', 'USD')"/></td>
			<td colspan="4">&nbsp;</td>
		</tr>

	</table>
</xsl:for-each>

<table width="100%" cellspacing="0" cellpadding="0">
	<tr><td colspan="10">
		<div class="orderSeparator" />
	</td></tr>
	
	<tr valign="top">
		<td colspan="3" rowspan="12" width="260"></td>
		<td colspan="7"></td>
	</tr>
	
	<tr valign="top" class="orderSummary">
		<td colspan="3" align="right">Order Subtotal:</td>
		<td colspan="1" align="right" width="60"><xsl:value-of select="format-number(order/subTotal, '$###,##0.00', 'USD')"/></td>
		<td width="10"><xsl:if test="order/estimatedPrice = 'true'">*</xsl:if></td>
		<td colspan="2" width="70">&nbsp;</td>
	</tr>
	
	<xsl:if test="number(order/deliverySurcharge) &gt; 0">
		<tr valign="top" class="orderSummary">
			<td colspan="3" align="right">Delivery Charge<xsl:if test="order/deliveryChargeWaived = 'true'"> (waived)</xsl:if>:</td>
			<td colspan="1" align="right" width="60">
				<xsl:choose>
					<xsl:when test="order/deliveryChargeWaived = 'true'">$0.00</xsl:when>
					<xsl:otherwise><xsl:value-of select="format-number(order/deliverySurcharge, '$###,##0.00', 'USD')"/></xsl:otherwise>
				</xsl:choose>
			</td>
			<td></td>
			<td colspan="2"><xsl:if test="order/deliveryChargeWaived = 'false'"><xsl:if test="order/deliveryChargeTaxable = 'true'"><b>T</b></xsl:if></xsl:if></td>
		</tr>
	</xsl:if>
	
	<xsl:if test="number(order/miscellaneousCharge) &gt; 0">
		<tr valign="top" class="orderSummary">
			<td colspan="3" align="right">Fuel Surcharge<xsl:if test="order/miscellaneousChargeWaived = 'true'"> (waived)</xsl:if>:</td>
			<td colspan="1" align="right">
				<xsl:choose>
					<xsl:when test="order/miscellaneousChargeWaived = 'true'">$0.00</xsl:when>
					<xsl:otherwise><xsl:value-of select="format-number(order/miscellaneousCharge, '$###,##0.00', 'USD')"/></xsl:otherwise>
				</xsl:choose>
			</td>
			<td></td>
			<td colspan="2"><xsl:if test="order/miscellaneousChargeWaived = 'false'"><xsl:if test="order/miscellaneousChargeTaxable = 'true'"><b>T</b></xsl:if></xsl:if></td>
		</tr>
	</xsl:if>

	<xsl:if test="number(order/taxValue) &gt; 0">
		<tr valign="top" class="orderSummary">
			<td colspan="3" align="right">Total Tax:</td>
			<td colspan="1" align="right"><xsl:value-of select="format-number(order/taxValue, '$###,##0.00', 'USD')"/></td>
			<td colspan="3"></td>
		</tr>
	</xsl:if>

	<xsl:if test="number(order/depositValue) &gt; 0">
		<tr valign="top" class="orderSummary">
			<td colspan="3" align="right">Bottle Deposit:</td>
			<td colspan="1" align="right"><xsl:value-of select="format-number(order/depositValue, '$###,##0.00', 'USD')"/></td>
			<td colspan="3"></td>
		</tr>
	</xsl:if>

	<xsl:if test="order/phoneChargeWaived = 'false' and number(order/phoneCharge) &gt; 0">
		<tr valign="top" class="orderSummary">
			<td colspan="3" align="right">Phone Handling Fee:</td>
			<td colspan="1"  align="right"><xsl:value-of select="format-number(order/phoneCharge, '$###,##0.00', 'USD')"/></td>
			<td colspan="3"></td>
		</tr>
	</xsl:if>

	<xsl:if test="number(order/totalDiscountValue) &gt; 0">
		<tr valign="top" class="orderSummary">
			<td colspan="3" align="right"><xsl:value-of select='order/discountDescription'/>:</td>
			<td colspan="1" align="right">-<xsl:value-of select="format-number(order/totalDiscountValue, '$###,##0.00', 'USD')"/></td>
			<td colspan="3"></td>
		</tr>
	</xsl:if>
	
	<xsl:if test="order/RedeemedSampleDescription != 'NONE'">
		<tr valign="top" class="orderSummary">
			<td colspan="3" align="right"><xsl:value-of select='order/redeemedSampleDescription'/>:</td>
			<td colspan="1" align="right">FREE!</td>
			<td colspan="3"></td>
		</tr>
	</xsl:if>
	
	<xsl:if test="number(order/customerCreditsValue) &gt; 0">
		<tr valign="top" class="orderSummary">
			<td colspan="3" align="right">Credit Applied:</td>
			<td colspan="1" align="right">-<xsl:value-of select="format-number(order/customerCreditsValue, '$###,##0.00', 'USD')"/></td>
			<td colspan="3"></td>
		</tr>
	</xsl:if>

	<tr valign="top" class="orderTotal">
		<td colspan="3" align="right">
			<b><xsl:choose>
				<xsl:when test="order/estimatedPrice = 'true'">ESTIMATED TOTAL</xsl:when>
				<xsl:otherwise>ORDER TOTAL</xsl:otherwise>
			</xsl:choose></b>:
		</td>
		<td colspan="1" width="60" align="right"><b><xsl:value-of select="format-number(order/total, '$###,##0.00', 'USD')"/></b></td>
		<td colspan="1" width="10"><xsl:if test="order/estimatedPrice = 'true'">*</xsl:if></td>
		<td colspan="2" width="70"></td>
	</tr>
	
	<xsl:if test="number(order/chargeInvoiceTotal) &gt; 0">
		<tr valign="top">
			<td colspan="7"></td>
		</tr>
		<tr valign="top" class="orderViewSummary">
			<td colspan="3" align="right">Returned Check Fee:</td>
			<td colspan="1"  align="right"><xsl:value-of select="format-number(order/chargeInvoiceTotal, '$###,##0.00', 'USD')"/></td>
			<td colspan="3"></td>
		</tr>
	</xsl:if>

	<xsl:if test="order/paymentMethod/paymentType = 'M'">
	<tr valign="top" class="orderTotal">
		<td colspan="3" align="right">
			<b>AMOUNT DUE:</b>
		</td>
		<td colspan="1" width="60" align="right"><b>$0.00</b></td>
		<td colspan="1" width="10"></td>
		<td colspan="2" width="70"></td>
	</tr>
	</xsl:if>
	
	<xsl:if test="order/estimatedPrice = 'true'">
		<tr valign="top">
			<td colspan="7" align="right">
				<br/>
				* Find out more about <a href="http://www.freshdirect.com/help/faq_home.jsp?page=shopping#question4">estimated</a> prices.
			</td>
		</tr>
	</xsl:if>
</table>

<xsl:if test="order/paymentMethod/paymentType = 'M'">
				<br/>
				<b>Please note that you are not being charged for this order. The amount displayed above, as well as your account with FreshDirect, will reflect a zero balance within the next 48 hours.</b>
</xsl:if>

</xsl:template>

</xsl:stylesheet>

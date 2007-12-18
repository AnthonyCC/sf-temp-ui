<?xml version="1.0"?>
<!DOCTYPE xsl:stylesheet [
    <!ENTITY nbsp "&#160;">
    <!ENTITY dot  "&#183;">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:output method="html"/>
	<xsl:template name="h_invoice_info_v1">
<br/>
<table cellpadding="0" cellspacing="0" width="100%">
     <tr>
		<td colspan="5"><b><font color="#FF9933">ORDER INFORMATION</font> for ORDER NUMBER <xsl:value-of select="order/erpSalesId"/></b><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="4" /></td>
	</tr>
	<tr>
		<td colspan="5" bgcolor="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1" border="0" alt="" /></td>
	</tr>
     <tr>
		<td colspan="5"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8" /></td>
	</tr>
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
		<td width="33%" valign="top"><font face="verdana, arial, sans-serif" size="1" color="black">
			<b>ORDER TOTAL</b><br/>
			$<xsl:value-of select='format-number(order/invoicedTotal, "###,##0.00", "USD")'/><br/>
			<br/>
			
			<xsl:if test="order/paymentMethod/paymentType = 'M'">
			<b>AMOUNT DUE:</b><br/>
			$0.00<br/><br/>
			</xsl:if>
			
			<xsl:if test="order/paymentMethod/paymentType != 'M'">
			<b><xsl:value-of select="order/paymentMethod/paymentMethodType"/></b><br/>
			<xsl:call-template name="format-payment-method"><xsl:with-param name="paymentMethod" select="order/paymentMethod"/></xsl:call-template><br/>
			</xsl:if>
			
			</font>
		</td>
		<td width="2%">&nbsp;</td>
		<td width="30%" valign="top">&nbsp;</td>
	</tr>
     <tr><td colspan="5" class="bodyCopy"><br/><b>FRESHDIRECT TIPPING POLICY<br/><img src="http://www.freshdirect.com/media_stat/images/layout/cccccc.gif" width="100%" height="1" border="0" vspace="4"/><br/>You are under no obligation to tip but have the option of providing a nominal tip if you feel that you've received exceptional service. FreshDirect delivery personnel are not permitted to solicit tips under any circumstances.</b><br/><br/><br/></td></tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
     <tr>
	<td colspan="9" bgcolor="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1" /></td>
	</tr>
     <tr>
	<td colspan="9"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8" /></td>
	</tr>
	<xsl:for-each select="order/invoicedOrderViews/invoicedOrderViews">
		<xsl:call-template name="orderlinesView">
			<xsl:with-param name="view" select="." />
		</xsl:call-template>
	</xsl:for-each>
</table>

<table border="0" cellspacing="0" cellpadding="0" width="100%">
<tr>
	<td colspan="9"><div class="orderSeparator" /></td>
</tr>

<tr>
	<td colspan="3" rowspan="12"></td>
	<td width="180"></td>
	<td width="60"></td>
	<td width="70"></td>
	<td width="10"></td>
	<td width="10"></td>
	<td width="10"></td>
</tr>

	<xsl:if test="number(order/totalDiscountValue) &gt; 0">
		<tr valign="top" class="orderSummary">
			<td colspan="2" align="right">Free Food:</td>
			<td align="right">
				$<xsl:value-of select='format-number(order/totalDiscountValue, "###,##0.00", "USD")'/>
			</td>
			<td colspan="3">&nbsp;</td>
		</tr>
	</xsl:if>

	<xsl:if test="order/invoicedTaxValue > 0">
		<tr valign="top" class="orderSummary">
			<td colspan="2" align="right">Total Tax:</td>
			<td align="right">$<xsl:value-of select='format-number(order/invoicedTaxValue, "###,##0.00", "USD")'/></td>
			<td colspan="3">&nbsp;</td>
		</tr>
	</xsl:if>

	<xsl:if test="order/depositValue > 0">
		<tr valign="top" class="orderSummary">
			<td colspan="2" align="right">Bottle Deposit:</td>
			<td align="right">$<xsl:value-of select='format-number(order/depositValue, "###,##0.00", "USD")'/></td>
			<td colspan="3">&nbsp;</td>
		</tr>
	</xsl:if>
	
	<xsl:if test="number(order/deliverySurcharge) &gt; 0">
		<tr valign="top" class="orderSummary">
			<td colspan="2" align="right">Delivery Charge<xsl:if test="order/deliveryChargeWaived = 'true'"> (waived)</xsl:if>:</td>
			<td align="right">
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
			<td colspan="2" align="right">Fuel Surcharge<xsl:if test="order/miscellaneousChargeWaived = 'true'"> (waived)</xsl:if>:</td>
			<td align="right">
				<xsl:choose>
					<xsl:when test="order/miscellaneousChargeWaived = 'true'">$0.00</xsl:when>
					<xsl:otherwise><xsl:value-of select="format-number(order/miscellaneousCharge, '$###,##0.00', 'USD')"/></xsl:otherwise>
				</xsl:choose>
			</td>
			<td></td>
			<td colspan="2"><xsl:if test="order/miscellaneousChargeWaived = 'false'"><xsl:if test="order/miscellaneousChargeTaxable = 'true'"><b>T</b></xsl:if></xsl:if></td>
		</tr>
	</xsl:if>
	
	<xsl:if test="order/phoneChargeWaived = 'false' and order/phoneCharge > 0">
		<tr valign="top" class="orderSummary">
			<td colspan="2" align="right">Phone Handling Fee:</td>
			<td align="right">$<xsl:value-of select='format-number(order/phoneCharge, "###,##0.00", "USD")'/></td>
			<td colspan="3">&nbsp;</td>
		</tr>
	</xsl:if>
	
	<xsl:if test="order/appliedCredits/appliedCredits/amount != '' and order/appliedCredits/appliedCredits/amount > 0">
		<tr valign="top" class="orderSummary">
			<td colspan="2" align="right">Credits:</td>
			<td align="right">
				($<xsl:value-of select='format-number(sum(order/appliedCredits/appliedCredits/amount), "###,##0.00", "USD")'/>)
			</td>
			<td colspan="3">&nbsp;</td>
		</tr>
	</xsl:if>
        
	<tr valign="top" class="orderTotal">
		<td colspan="2" align="right"><b>ORDER TOTAL:</b></td>
		<td align="right">
			<b>$<xsl:value-of select='format-number(order/invoicedTotal, "###,##0.00", "USD")'/></b>
		</td>
		<td colspan="3">&nbsp;</td>
	</tr>
	
	<xsl:if test="order/paymentMethod/paymentType = 'M'">
	<tr valign="top" class="orderTotal">
		<td colspan="2" align="right"><b>AMOUNT DUE:</b></td>
		<td align="right">
			<b>$0.00</b>
		</td>
		<td colspan="3">&nbsp;</td>
	</tr>
	</xsl:if>
	
	<tr>
		<td colspan="6"><br/><br/></td>
	</tr>
	<tr valign="top">
		<td colspan="6" align="right"><b>T = Taxable Item</b></td>
	</tr>
	<tr valign="top">
		<td colspan="6" align="right"><b>S = Special Price</b></td>
	</tr>
	<tr valign="top">
		<td colspan="6" align="right"><b>D = Bottle Deposit</b></td>
	</tr>
</table>
</xsl:template>
	
<xsl:template name="orderlinesView">
	<xsl:param name="view"/>
 	<xsl:if test="$view/displayDepartment = 'true'">
		<tr>
			<td align="center"><b>Quantity<br/>Ordered/Delivered</b></td>
			<td>&nbsp;</td>
			<td align="center"><b>Final<br/>Weight</b></td>
			<td align="center"><b>Unit<br/>Price</b></td>
			<td align="center"><b>Options<br/>Price</b></td>
			<td align="right"><b>Final<br/>Price</b></td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
               <td>&nbsp;</td>
		</tr>
	</xsl:if>
	<tr>
		<td colspan="10">
			<xsl:if test="'' != $view/description">
				<br/><div class="orderViewHeader"><xsl:value-of select="$view/description"/></div>
				<xsl:if test="$view/displayDepartment = 'false'"><br/></xsl:if>
			</xsl:if>
		</td>
	</tr>	
	<xsl:for-each select="$view/orderLines/orderLines">
		<xsl:variable name="prevPos" select="position()-1"/>
		<xsl:if test="($view/displayDepartment = 'true') and ($prevPos = 0 or ../orderLines[$prevPos]/departmentDesc != departmentDesc)">
			<xsl:choose>
				<xsl:when test="starts-with(departmentDesc, 'Recipe: ')">
					<xsl:if test="not(starts-with(../orderLines[$prevPos]/departmentDesc, 'Recipe: '))">
						<tr>
							<td>&nbsp;</td>
							<td colspan="8">
								<br/>
								<div style="color: #f93; font-weight: bold;">Recipes</div>
							</td>
						</tr>
					</xsl:if>
					<tr>
						<td>&nbsp;</td>
						<td colspan="8">
							<br/>
							<div style="font-weight: bold;"><xsl:value-of select="substring(departmentDesc, string-length('Recipe: ')+1)"/></div>
						</td>
					</tr>
				</xsl:when>
				<xsl:otherwise>
					<tr>
						<td>&nbsp;</td>
						<td colspan="8">
							<br/>
							<div style="color: #f93; font-weight: bold;"><xsl:value-of select="departmentDesc"/></div>
						</td>
					</tr>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>		

		<tr valign="top">
			<td align="center"><xsl:value-of select="displayQuantity"/> <xsl:if test="salesUnit = 'LB' and pricedByLb = 'true'">&nbsp;lb&nbsp;</xsl:if></td>
				
			<td>
				<b><xsl:value-of select="description"/></b> 
				<xsl:if test="configurationDesc!=''">
					&nbsp;-&nbsp;(<xsl:value-of select="configurationDesc"/>)
				</xsl:if>
			</td>
               
			<td align="center">
				<xsl:if test="pricedByLb='true'">
					<xsl:value-of select='concat(format-number(invoiceLine/weight, "###,##0.00"), " lb")' />
				</xsl:if> 
			</td>
               
			<td align="center">
				<xsl:if test="unitPrice!=''">
					(<xsl:value-of select="unitPrice"/>)
				</xsl:if>
			</td>
               
			<td align="center">
				<xsl:if test="invoice/customizationPrice > 0">
					$<xsl:value-of select='format-number(invoice/customizationPrice, "###,##0.00", "USD")' />
				</xsl:if>
			</td>
               
			<td align="right">
				<b>$<xsl:value-of select='format-number(invoiceLine/price, "###,##0.00", "USD")'/></b>
			</td>
               
			<td>
				<xsl:if test="invoiceLine/taxValue > 0">
					<b>&nbsp;T</b>
				</xsl:if>
			</td>
               
			<td>
				<xsl:if test="scaledPricing = 'true'">
					<b>&nbsp;S</b>
				</xsl:if>
			</td>
               
			<td>
				<xsl:if test="invoiceLine/depositValue > 0">
					<b>&nbsp;D</b>
				</xsl:if>
			</td>
		</tr>
	</xsl:for-each>

	<tr><td colspan="10">
		<div class="orderViewSeparator" />
	</td></tr>

	<tr class="orderViewSummary">
		<td colspan="5" align="right">Subtotal:</td>
		<td colspan="1" align="right"><b><xsl:value-of select="format-number($view/subtotal, '$###,##0.00', 'USD')"/></b></td>
		<td colspan="3">&nbsp;</td>
	</tr>
	
	<tr class="orderViewSummary">
		<td colspan="5" align="right">Tax:</td>
		<td colspan="1" align="right"><xsl:value-of select="format-number($view/tax, '$###,##0.00', 'USD')"/></td>
		<td colspan="3">&nbsp;</td>
	</tr>

	<tr class="orderViewSummary">
		<td colspan="5" align="right">Bottle Deposit:</td>
		<td colspan="1" align="right"><xsl:value-of select="format-number($view/depositValue, '$###,##0.00', 'USD')"/></td>
		<td colspan="3">&nbsp;</td>
	</tr>

</xsl:template>

</xsl:stylesheet>
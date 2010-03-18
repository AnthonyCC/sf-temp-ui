<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:output method="text"/>
	<xsl:template name="x_order_info_v1">

=========== ORDER INFORMATION ===========

ORDER NUMBER
<xsl:value-of select="order/erpSalesId"/>

TIME
<xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime"/></xsl:call-template><xsl:text>
</xsl:text><xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime" /></xsl:call-template> - <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/endTime"/></xsl:call-template>

ADDRESS
<xsl:value-of select="order/deliveryAddress/firstName"/> <xsl:value-of select="order/deliveryAddress/lastName"/><xsl:text>
</xsl:text>
<xsl:call-template name="format-delivery-address"><xsl:with-param name="dlvAddress" select="order/deliveryAddress" /></xsl:call-template>

DELIVERY INSTRUCTIONS
<xsl:value-of select="order/deliveryAddress/instructions"/> 
<xsl:if test="order/deliveryAddress/unattendedDeliveryFlag = 'OPT_IN'">

UNATTENDED DELIVERY
<xsl:choose><xsl:when 
	test="order/deliveryAddress/unattendedDeliveryInstructions"><xsl:value-of 
	select="order/deliveryAddress/unattendedDeliveryInstructions"/></xsl:when><xsl:otherwise>OK</xsl:otherwise></xsl:choose>

<xsl:value-of select="order/deliveryAddress/unattendedDeliveryInstructions"/>
</xsl:if>
<xsl:if test="order/deliveryAddress/altDelivery != 'none'">

ALTERNATE DELIVERY<xsl:text>
</xsl:text>
<xsl:value-of select="order/deliveryAddress/altDelivery"/><xsl:text>
</xsl:text>
<xsl:if test="order/deliveryAddress/altDelivery = 'Deliver order to neighbor'">
<xsl:value-of select="order/deliveryAddress/altFirstName"/>  <xsl:value-of select="order/deliveryAddress/altLastName"/><xsl:text>
</xsl:text>Apt. #: <xsl:value-of select="order/deliveryAddress/altApartment"/><xsl:text>
</xsl:text>Phone: <xsl:value-of select="order/deliveryAddress/altPhone"/> <xsl:if test="order/deliveryAddress/altPhone/extension != ''"> Ext. <xsl:value-of select="order/deliveryAddress/altPhone/extension"/></xsl:if><xsl:text>
</xsl:text>
</xsl:if>
</xsl:if>

ORDER TOTAL 
$<xsl:value-of select='format-number(order/total, "###,##0.00", "USD")'/>* 

<xsl:if test="order/totalAppliedGCAmount >0">
GIFT CARD AMOUNT TO BE APPLIED:
$<xsl:value-of select='format-number(order/totalAppliedGCAmount, "###,##0.00", "USD")'/>*

Remaining Gift Card Balance:
$<xsl:value-of select='format-number(customer/userGiftCardsBalance, "###,##0.00", "USD")'/>*
</xsl:if>

<xsl:if test="order/paymentMethod/paymentType = 'M'">
AMOUNT DUE: 
$0.00 
</xsl:if>

<xsl:if test="order/paymentMethod/paymentMethodType != 'Gift-Card'">
<xsl:if test="order/totalAppliedGCAmount >0">
Amount to Be Charged to Your Account:		
$<xsl:value-of select='format-number((order/total)-(order/totalAppliedGCAmount)+(order/bufferAmt), "###,##0.00", "USD")' />*
</xsl:if>
<xsl:if test="order/paymentMethod/paymentType != 'M'">
<xsl:value-of select="order/paymentMethod/paymentMethodType"/>
<xsl:call-template name="format-payment-method"><xsl:with-param name="paymentMethod" select="order/paymentMethod" /></xsl:call-template>
</xsl:if>
</xsl:if>
----------------------------------------
CART DETAILS
----------------------------------------
<xsl:for-each select="order/orderLines/orderLines">
<xsl:variable name="prevPos" select="position()-1"/>
<xsl:if test="$prevPos = 0 or ../orderLines[$prevPos]/departmentDesc != departmentDesc">
<xsl:text>
</xsl:text>
<xsl:choose>
<xsl:when test="starts-with(departmentDesc, 'Recipe: ')">
<xsl:if test="not(starts-with(../orderLines[$prevPos]/departmentDesc, 'Recipe: '))">
Recipes
</xsl:if>
<xsl:value-of select="substring(departmentDesc, string-length('Recipe: ')+1)"/>
</xsl:when>
<xsl:otherwise>
<xsl:value-of select="departmentDesc"/>
</xsl:otherwise>
</xsl:choose>
<xsl:text>
</xsl:text>
		</xsl:if>
			<xsl:choose>
					<xsl:when test="salesUnitDescription and pricedByLb = 'true'">
						<xsl:choose>
							<xsl:when test="soldBySalesUnits = 'true' and salesUnit != 'EA'">
								<xsl:value-of select="salesUnitDescription" />
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select='format-number(quantity, "###,##0.##")'/><xsl:if test="soldByLb = 'true'"><xsl:text> lb</xsl:text></xsl:if>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select='format-number(quantity, "###,##0.##")'/>
					</xsl:otherwise>
				</xsl:choose><xsl:text>  </xsl:text>
			<xsl:value-of select="description"/> 
			<xsl:if test="configurationDesc!=''">
				<xsl:text> - (</xsl:text><xsl:value-of select="configurationDesc"/><xsl:text>) </xsl:text>
			</xsl:if> 
			<xsl:if test="unitPrice!=''">
				<xsl:text>(</xsl:text><xsl:value-of select="unitPrice"/><xsl:text>) </xsl:text>
			</xsl:if>
			<xsl:text>$</xsl:text><xsl:value-of select='format-number(price, "###,##0.00", "USD")'/>
			<xsl:if test="estimatedPrice = 'true'">*</xsl:if> 
			<xsl:if test="tax = 'true'"><xsl:text>  </xsl:text>T</xsl:if> 
			<xsl:if test="scaledPricing = 'true'"><xsl:text>  </xsl:text>S</xsl:if> 
			<xsl:if test="depositValue = 'true'"><xsl:text>  </xsl:text>D</xsl:if>
			<xsl:text>
</xsl:text>		
	</xsl:for-each>		  	
			<xsl:text>

</xsl:text>
----------------------------------------
----------------------------------------
Estimated Subtotal: $<xsl:value-of select='format-number(order/subTotal, "###,##0.00", "USD")'/>*
<xsl:if test="order/appliedCredits != '' and order/appliedCredits > 0">
Credits: ($<xsl:value-of select='format-number(sum(order/appliedCredits/appliedCredits/amount), "###,##0.00", "USD")'/>)
</xsl:if>

<xsl:if test="number(order/deliverySurcharge) &gt; 0">
Delivery Charge<xsl:if test="order/deliveryChargeWaived = 'true'"> (waived)</xsl:if>: <xsl:choose><xsl:when test="order/deliveryChargeWaived = 'true'">$0.00</xsl:when><xsl:otherwise><xsl:value-of select="format-number(order/deliverySurcharge, '$###,##0.00', 'USD')"/></xsl:otherwise></xsl:choose> <xsl:if test="order/deliveryChargeWaived = 'false'"><xsl:if test="order/deliveryChargeTaxable = 'true'"> T</xsl:if></xsl:if>
</xsl:if>

<xsl:if test="number(order/miscellaneousCharge) &gt; 0">
Fuel Surcharge<xsl:if test="order/miscellaneousChargeWaived = 'true'"> (waived)</xsl:if>: <xsl:choose><xsl:when test="order/miscellaneousChargeWaived = 'true'">$0.00</xsl:when><xsl:otherwise><xsl:value-of select="format-number(order/miscellaneousCharge, '$###,##0.00', 'USD')"/></xsl:otherwise></xsl:choose> <xsl:if test="order/miscellaneousChargeWaived = 'false'"><xsl:if test="order/miscellaneousChargeTaxable = 'true'"> T</xsl:if></xsl:if>
</xsl:if>

<xsl:if test="order/phoneChargeWaived = 'false' and number(order/phoneCharge) &gt; 0">
Phone Handling Fee: <xsl:value-of select="format-number(order/phoneCharge, '$###,##0.00', 'USD')"/>
</xsl:if>

<xsl:if test="number(order/totalDiscountValue) &gt; 0">
<xsl:text>
</xsl:text>
<xsl:for-each select="order/headerDiscounts/headerDiscounts"><xsl:value-of select='description' />: ($<xsl:value-of select='format-number(model/discount/amount, "###,##0.00", "USD")'/>)
</xsl:for-each>
</xsl:if>
Total Tax: <xsl:value-of select='format-number(order/taxValue, "$###,##0.00", "USD")'/> 
State Bottle Deposit and Handling Fee: <xsl:value-of select='format-number(order/depositValue, "$###,##0.00", "USD")'/> 
Estimated Order Total: <xsl:value-of select='format-number(order/total, "$###,##0.00", "USD")'/>*
<xsl:if test="order/paymentMethod/paymentType = 'M'">
Amount due: $0.00 

Please note that you are not being charged for this order. The amount displayed above, as well as your account with FreshDirect, will reflect a zero balance within the next 48 hours.
</xsl:if>

<xsl:if test="number(order/chargeInvoiceTotal) &gt; 0">
Returned Check Fee: <xsl:value-of select="format-number(order/chargeInvoiceTotal, '$###,##0.00', 'USD')"/>
</xsl:if>

* Estimated Weight &amp; Our Honest Pricing Policy
Every apple, every steak, every cheese varies a little in size and weight. While you shop we show an estimated weight and price for everything priced by the pound. On the day of delivery we assemble your order and weigh each item to determine its final price.

We guarantee that you'll always pay the true price for the actual weight of your products.
</xsl:template>


</xsl:stylesheet>

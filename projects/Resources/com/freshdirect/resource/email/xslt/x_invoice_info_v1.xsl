<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt="http://xsltsl.org/date-time" version="1.0">
<xsl:template name="x_invoice_info_v1">

=========== ORDER INFORMATION ===========

ORDER NUMBER
<xsl:value-of select="order/erpSalesId"/>

TIME
<xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime" /></xsl:call-template><xsl:text> </xsl:text>
<xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="order/deliveryReservation/startTime" /></xsl:call-template> - <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="order/deliveryReservation/endTime" /></xsl:call-template>

ADDRESS
<xsl:value-of select="order/deliveryAddress/firstName"/><xsl:text> </xsl:text><xsl:value-of select="order/deliverAddress/lastName"/>
<xsl:call-template name="format-delivery-address"><xsl:with-param name="dlvAddress" select="order/deliveryAddress"/></xsl:call-template>
<xsl:text>

</xsl:text>
<xsl:if test="order/deliveryType = 'P'">
	<xsl:choose>
		<xsl:when test="order/depotFacility = 'FreshDirect Pickup'">Directions and details: http://www.freshdirect.com/help/delivery_lic_pickup.jsp	</xsl:when>
		<xsl:otherwise>Directions and details: http://www.freshdirect.com/help/delivery_hamptons.jsp</xsl:otherwise>
	</xsl:choose>
</xsl:if>

DELIVERY INSTRUCTIONS
<xsl:value-of select="order/deliveryAddress/instructions"/>

ORDER TOTAL 
$<xsl:value-of select='format-number(order/invoicedTotal, "###,##0.00", "USD")'/>  

<xsl:if test="order/paymentMethod/paymentType = 'M'">
<xsl:text>
</xsl:text>
AMOUNT DUE: 
$0.00
</xsl:if>

<xsl:if test="order/paymentMethod/paymentType != 'M'">
<xsl:value-of select="order/paymentMethod/paymentMethodType"/>
<xsl:call-template name="format-payment-method"><xsl:with-param name="paymentMethod" select="order/paymentMethod" /></xsl:call-template>
</xsl:if>
---
FRESHDIRECT TIPPING POLICY
You are under no obligation to tip but have the option of providing a nominal tip if you feel that you've received exceptional service. FreshDirect delivery personnel are not permitted to solicit tips under any circumstances.

----------------------------------------
CART DETAILS
----------------------------------------
<xsl:for-each select="order/invoicedOrderViews/invoicedOrderViews">
	<xsl:call-template name="orderlinesView">
		<xsl:with-param name="view" select="." />
	</xsl:call-template>
</xsl:for-each>

=========================================
<xsl:if test="number(order/totalDiscountValue) > 0">
Free Food: $<xsl:value-of select='format-number(order/totalDiscountValue, "###,##0.00", "USD")'/>
<xsl:text>
</xsl:text>
</xsl:if>
<xsl:if test="order/invoicedTaxValue > 0">
Total Tax: $<xsl:value-of select='format-number(order/invoicedTaxValue, "###,##0.00", "USD")'/>
<xsl:text>
</xsl:text>
</xsl:if>

<xsl:if test="number(order/deliverySurcharge) &gt; 0">
Delivery Charge<xsl:if test="order/deliveryChargeWaived = 'true'"> (waived)</xsl:if>: <xsl:choose><xsl:when test="order/deliveryChargeWaived = 'true'">$0.00</xsl:when><xsl:otherwise><xsl:value-of select="format-number(order/deliverySurcharge, '$###,##0.00', 'USD')"/></xsl:otherwise></xsl:choose> <xsl:if test="order/deliveryChargeWaived = 'false'"><xsl:if test="order/deliveryChargeTaxable = 'true'"> T</xsl:if></xsl:if>
<xsl:text>
</xsl:text>
</xsl:if>

<xsl:if test="number(order/miscellaneousCharge) &gt; 0">
Fuel Surcharge<xsl:if test="order/miscellaneousChargeWaived = 'true'"> (waived)</xsl:if>: <xsl:choose><xsl:when test="order/miscellaneousChargeWaived = 'true'">$0.00</xsl:when><xsl:otherwise><xsl:value-of select="format-number(order/miscellaneousCharge, '$###,##0.00', 'USD')"/></xsl:otherwise></xsl:choose> <xsl:if test="order/miscellaneousChargeWaived = 'false'"><xsl:if test="order/miscellaneousChargeTaxable = 'true'"> T</xsl:if></xsl:if>
<xsl:text>
</xsl:text>
</xsl:if>

<xsl:if test="order/appliedCredits/appliedCredits/amount != '' and order/appliedCredits/appliedCredits/amount > 0">
Credits: ($<xsl:value-of select='format-number(sum(order/appliedCredits/appliedCredits/amount), "###,##0.00", "USD")'/>)
<xsl:text>
</xsl:text>
</xsl:if>
Order Total: $<xsl:value-of select='format-number(order/invoicedTotal, "###,##0.00", "USD")'/>
<xsl:text>
</xsl:text>
<xsl:if test="order/paymentMethod/paymentType = 'M'">
Amount Due: $0.00
<xsl:text>
</xsl:text>
</xsl:if>
T = Taxable Item
S = Special Price
D = Bottle Deposit

</xsl:template>

<xsl:template name="orderlinesView">
<xsl:param name="view"/>

<xsl:for-each select="$view/orderLines/orderLines">
<xsl:variable name="prevPos" select="position()-1"/>
<xsl:if test="$prevPos = 0 or ../orderLines[$prevPos]/departmentDesc != departmentDesc">
<xsl:text>
</xsl:text>

<xsl:choose>
<xsl:when test="$view/displayDepartment = 'true'">
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
</xsl:when>
<xsl:otherwise>----------------------------------------
<xsl:choose>
<xsl:when test="$view/description = 'Best Cellars'">Best Cellars</xsl:when>
<xsl:otherwise><xsl:value-of select="$view/description"/> Charges</xsl:otherwise>
</xsl:choose>
</xsl:otherwise>
</xsl:choose>

<xsl:text>
</xsl:text>
</xsl:if>
<xsl:value-of select="displayQuantity" /><xsl:text>  </xsl:text>
<xsl:if test="salesUnit = 'LB' and pricedByLb = 'true'">
<xsl:text> lb </xsl:text>
</xsl:if>
<xsl:value-of select="description"/> 
<xsl:if test="configurationDesc!=''">
<xsl:text> - (</xsl:text><xsl:value-of select="configurationDesc"/><xsl:text>)</xsl:text>
</xsl:if>
<xsl:if test="pricedByLb='true'">
<xsl:text>  WEIGHT: </xsl:text><xsl:value-of select='concat(format-number(invoiceLine/weight, "###,##0.00"), " lb")' />
</xsl:if> 
<xsl:if test="unitPrice!=''">
<xsl:text>  UNIT PRICE: (</xsl:text><xsl:value-of select="unitPrice"/><xsl:text>) </xsl:text>
</xsl:if>
<xsl:if test="customizationPrice > 0">
<xsl:text>  OPTIONS PRICE: $</xsl:text><xsl:value-of select='format-number(customizationPrice, "###,##0.00", "USD")' />
</xsl:if>
<xsl:text> FINAL PRICE: $</xsl:text><xsl:value-of select='format-number(invoiceLine/price, "###,##0.00", "USD")'/>
<xsl:if test="invoiceLine/taxValue > 0">
<xsl:text>  T</xsl:text>
</xsl:if>
<xsl:if test="scaledPricing = 'true'">
<xsl:text>  S</xsl:text>
</xsl:if>
<xsl:if test="invoiceLine/depositValue > 0">
<xsl:text>  D</xsl:text>
</xsl:if>
<xsl:text>
</xsl:text>

</xsl:for-each>
----------------------------------------
Subtotal (<xsl:value-of select="$view/description" />): $<xsl:value-of select='format-number($view/subtotal, "###,##0.00", "USD")'/>
<xsl:text>
</xsl:text>
Tax (<xsl:value-of select="$view/description" />): $<xsl:value-of select='format-number($view/tax, "###,##0.00", "USD")'/>
<xsl:text>
</xsl:text>
<xsl:if test="number($view/depositValue) > 0">
Bottle Deposit (<xsl:value-of select="$view/description" />): $<xsl:value-of select='format-number($view/depositValue, "###,##0.00", "USD")'/>
</xsl:if>
<xsl:text>
</xsl:text>
</xsl:template>

</xsl:stylesheet>
<?xml version="1.0"?>
<!DOCTYPE xsl:stylesheet [
    <!ENTITY nbsp "&#160;">
    <!ENTITY dot  "&#183;">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:output method="html"/>
	<xsl:template name="h_gc_order_info_v1">
		<br/>
		<b><font color="#FF9933">PAYMENT INFORMATION FOR</font> ORDER NUMBER #<xsl:value-of select="order/erpSalesId"/></b><br/>
<table cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td width="100%" bgcolor="#CCCCCC"><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></td>
	</tr>
</table><br/>
<table cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td width="33%" valign="top"><font face="verdana, arial, sans-serif" size="1" color="black">
			<b>FreshDirect Store Credit:</b><br/>
			$<xsl:value-of select='format-number(order/customerCreditsValue, "###,##0.00", "USD")'/>*<br/>
			<br/>

			<b>Order Total:</b><br/>
			$<xsl:value-of select='format-number(order/total, "###,##0.00", "USD")'/>*<br/>
			<br/>
			
	
			<xsl:if test="order/paymentMethod/paymentType != 'M'">
			<b><xsl:value-of select="order/paymentMethod/paymentMethodType"/></b><br/>
			<xsl:call-template name="format-payment-method"><xsl:with-param name="paymentMethod" select="order/paymentMethod" /></xsl:call-template><br/>
			<br/>
			</xsl:if>
			<b>BILLING ADDRESS</b><br/>
			<xsl:value-of select="order/paymentMethod/name"/><font><xsl:text> </xsl:text></font><br/>
			<xsl:call-template name="format-billing-address"><xsl:with-param name="billingAddress" select="order/paymentMethod" /></xsl:call-template><br/>
			<br/>
	
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
<!--<p><xsl:call-template name="h_gc_recipient_info_v1"/></p>

<xsl:if test="order/paymentMethod/paymentType = 'M'">
				<br/>
				<b>Please note that you are not being charged for this order. The amount displayed above, as well as your account with FreshDirect, will reflect a zero balance within the next 48 hours.</b>
</xsl:if>-->

</xsl:template>

</xsl:stylesheet>

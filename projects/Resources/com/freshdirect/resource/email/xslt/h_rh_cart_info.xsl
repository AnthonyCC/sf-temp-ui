<?xml version="1.0"?>
<!DOCTYPE xsl:stylesheet [
    <!ENTITY nbsp "&#160;">
    <!ENTITY dot  "&#183;">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:output method="html"/>
	<xsl:template name="h_rh_cart_info">
		<br/>

<table width="680" cellspacing="0" cellpadding="0" border="0">

	<tr>
			<td class="text11bold" align="center">Quantity<br/> Order/Delivered</td>
			<td></td>
			<td class="text11bold" align="center">Final<br/>Weight</td>
			<td class="text11bold" align="center">Unit<br/>Price</td>
			<td class="text11bold" align="center">Options<br/>Price</td>
			<td class="text11bold" align="center">Final<br/>Price</td>
	</tr>
		<tr>
			<td><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></td>
		</tr>
		
	<tr>
			<td align="center" class="text11bold">
			<img src="/media_stat/images/donation/robinhood/landing/color_swatch_F0F0E7.gif" width="1" height="8" border="0" /> FRESHDIRECT
			</td>
	</tr>
		<tr>
			<td><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></td>
		</tr>
		<xsl:for-each select="order/orderViews/orderViews">
		<xsl:for-each select="orderLines/orderLines">
	<tr>
		
			<td class="text11bold" align="center"><xsl:value-of select="orderedQuantity"/>/<xsl:value-of select="deliveredQuantity"/></td>
			<td style="padding-left:30px;" class="text11bold" align="center">Robin Hood Holiday Meal for Eight	</td>
			<td></td>
			<td align="center">$75.00/ea</td>
			<td></td>
			<td  align="center"  style="padding-left:4px;" class="text11bold">$<xsl:value-of select='format-number(price, "###,##0.00", "USD")'/></td>
		
	</tr>
	</xsl:for-each>
		</xsl:for-each>
	<tr>
				<td align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
	</tr>
	

</table> 

<table width="680" cellspacing="0" cellpadding="0" border="0" valign="middle" >
		<tr>
			<td ><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></td>
			<td></td>			<td></td>			<td></td>			<td></td>			<td></td>

		</tr>
		
        <tr>
			<td colspan="4"></td>
		<td align="right" class="text11bold" >
		ORDER TOTAL:
		</td>
        <td  align="right"  style="padding-left:4px;" class="text11bold">$<xsl:value-of select='format-number(order/total, "###,##0.00", "USD")'/></td>
        </tr>
</table>
</xsl:template>

</xsl:stylesheet>
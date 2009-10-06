<?xml version="1.0"?>
<!DOCTYPE xsl:stylesheet [
    <!ENTITY nbsp "&#160;">
    <!ENTITY dot  "&#183;">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:output method="html"/>
	<xsl:template name="h_rh_cart_info">
		<br/>

<img src="/media_stat/images/layout/cccccc.gif" width="693" height="1"/><br/>

<table width="100%" cellspacing="0" cellpadding="0" border="0">

	<tr>
			<td class="text11bold" align="center"><b>&nbsp;Quantity</b><br/></td>
			<td></td>
			<td></td>
			<td class="text11bold" align="center"><b>Unit<br/>Price</b></td>
			<td></td>
			<td class="text11bold" align="center"><b>Final<br/>Price</b></td>
	</tr>
		<tr>
			<td><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></td>
		</tr>
		
	<tr align="left">
			<td class="text11bold" bgcolor="#dddddd" colspan="6">
			<img src="/media_stat/images/donation/robinhood/landing/color_swatch_F0F0E7.gif" width="1" height="8" border="0" /> <b> &nbsp;&nbsp;&nbsp;FRESHDIRECT</b>
			</td>
	</tr>
		<tr>
			<td><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></td>
		</tr>
		<xsl:variable name="defaultPrice1" select="defaultPrice" /> 
		<xsl:variable name="defaultPriceUnit1" select="defaultPriceUnit" /> 
		<xsl:for-each select="order/orderViews/orderViews">
		<xsl:for-each select="orderLines/orderLines">
	<tr>
		
			<td class="text11bold" align="center"><xsl:value-of select="orderedQuantity"/></td>
			<td style="padding-left:30px;" class="text11bold" align="center">Robin Hood Holiday Meal for Eight	</td>
			<td></td>
			<td align="center">$<xsl:value-of select='format-number($defaultPrice1, "###,##0.00", "USD")' />/<xsl:value-of select="$defaultPriceUnit1"/></td>
			<td></td>
			<td  align="center"  style="padding-left:4px;" class="text11bold">$<xsl:value-of select='format-number(price, "###,##0.00", "USD")'/></td>
		
	</tr>
	</xsl:for-each>
		</xsl:for-each>
	<tr>
				<td align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
	</tr>
	 <tr>
		<td align="center" colspan="4" ></td>
		<td align="center"><b>ORDER TOTAL:&nbsp;&nbsp;</b></td>
		<td align="center" >
		<b>$<xsl:value-of select='format-number(order/total, "###,##0.00", "USD")'/> </b>
		</td>
         </tr>

</table> 


</xsl:template>

</xsl:stylesheet>
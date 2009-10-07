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
			<td class="text11bold" align="center"><font face="verdana, arial, sans-serif" size="1" color="black"><b>Quantity</b><br/></font></td>
			<td></td>
			<td></td>
			<td></td>
			<td></td>
			<td class="text11bold" align="center"><font face="verdana, arial, sans-serif" size="1" color="black"><b>Unit<br/>Price</b></font></td>
			<td></td>
			<td></td>
			<td class="text11bold" align="right"><font face="verdana, arial, sans-serif" size="1" color="black"><b>Final<br/>Price</b></font></td>
	 </tr>
	 <tr>
			<td><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></td>
	 </tr>
		
	<tr align="left"> 
			<td class="text11bold" bgcolor="#dddddd" colspan="10"><font face="verdana, arial, sans-serif" size="1" color="black">
			<img src="/media_stat/images/donation/robinhood/landing/color_swatch_F0F0E7.gif" width="1" height="8" border="0" /> <b> &nbsp;&nbsp;&nbsp;FRESHDIRECT</b>
			</font></td>
	</tr>
	<tr>
			<td><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></td>

	</tr>
		<xsl:variable name="defaultPrice1" select="defaultPrice" /> 
		<xsl:variable name="defaultPriceUnit1" select="defaultPriceUnit" /> 
		<xsl:variable name="productFullName" select="productFullName" /> 
		<xsl:for-each select="order/orderViews/orderViews">
		<xsl:for-each select="orderLines/orderLines">
	<tr> <font face="verdana, arial, sans-serif" size="1" color="black">
		
			<td class="text11bold" align="center"><font face="verdana, arial, sans-serif" size="1" color="black"><xsl:value-of select="orderedQuantity"/></font></td>
			<td style="padding-left:30px;" class="text11bold" align="center"><font face="verdana, arial, sans-serif" size="1" color="black"> <b><xsl:value-of select="$productFullName" /></b> </font></td>
			<td></td>
			<td></td>
			<td></td>
			<td align="center"><font face="verdana, arial, sans-serif" size="1" color="black">$<xsl:value-of select='format-number($defaultPrice1, "###,##0.00", "USD")' />/<xsl:value-of select="$defaultPriceUnit1"/> </font></td>
			<td></td><td></td>
			<td  align="right" style="padding-left:4px;" class="text11bold"> <font face="verdana, arial, sans-serif" size="1" color="black">$<xsl:value-of select='format-number(price, "###,##0.00", "USD")'/> </font></td>
		
	</font> </tr>
	</xsl:for-each>
		</xsl:for-each>
	<tr>
				<td align="center"><img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" /></td>
	</tr>
	 <tr>
		<td></td>
		<td colspan="4"><img src="/media_stat/images/layout/clear.gif" width="170" height="1" /></td>
		<td  style="background-color: #969;color: white;line-height: 20px;" colspan="4" width="" align="right"><font face="verdana, arial, sans-serif" size="1" color="black">
			<b>ORDER TOTAL:&nbsp;&nbsp;		
			 $<xsl:value-of select='format-number(order/total, "###,##0.00", "USD")'/> </b>
		</font></td>
		<td></td>
         </tr>

</table> 


</xsl:template>

</xsl:stylesheet>
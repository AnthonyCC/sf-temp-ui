<?xml version="1.0"?>
<!DOCTYPE xsl:stylesheet [
    <!ENTITY nbsp "&#160;">
    <!ENTITY dot  "&#183;">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:output method="html"/>
	<xsl:template name="h_gc_bulk_recipient_info_v1">
		<br/>
		<b><font color="#FF9933">DETAILS FOR</font> ORDER NUMBER #<xsl:value-of select="order/erpSalesId"/></b><br/>

	<table cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td width="100%" bgcolor="#CCCCCC"><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></td>
	</tr>
	</table><br/>
	<u><b><a href="http://www.freshdirect.com/your_account/order_history.jsp"><font face="verdana, arial, sans-serif" size="2" color="black">View this order (and get printable versions of your gifts) in Your Account.</font></a></b></u><br/><br/>
	<table width="100%" cellspacing="0" cellpadding="0" border="0"><font face="verdana, arial, sans-serif" size="1" color="black">
	<b><tr>
			<td colspan="45%" align="left" ><b><font face="verdana, arial, sans-serif" size="1" color="black">Card Type</font></b></td>
			<td colspan="15%" align="left" ><b><font face="verdana, arial, sans-serif" size="1" color="black">Amount</font></b></td>
			<td colspan="15%" align="left" ><b><font face="verdana, arial, sans-serif" size="1" color="black">Qty</font></b></td>
			<td colspan="15%" align="right" ><b><font face="verdana, arial, sans-serif" size="1" color="black">SubTotal</font></b></td>
			<td colspan="10%" >&nbsp;</td>
	</tr></b></font>
	
	<xsl:for-each select="order/giftCardBulkRecipients/recipents/recipents">
	
		

		<tr>
			<td colspan="45%" align="left" ><b><font face="verdana, arial, sans-serif" size="1" color="black"><xsl:value-of select="templateId"/></font></b></td>
			<td colspan="15%" align="left" ><b><font face="verdana, arial, sans-serif" size="1" color="black">$<xsl:value-of select='format-number(amount, "###,##0.00", "USD")'/></font></b></td>
			<td colspan="15%" align="left" ><b><font face="verdana, arial, sans-serif" size="1" color="black"><xsl:value-of select="quantity"/></font></b></td>
			<td colspan="15%" align="right" ><b><font face="verdana, arial, sans-serif" size="1" color="black">$<xsl:value-of select='format-number(subTotal, "###,##0.00", "USD")'/></font></b></td>
			<td colspan="10%" >&nbsp;</td>
		       </tr>
	</xsl:for-each>

	</table>
	 <br/>
	<table width="100%" cellspacing="0" cellpadding="0" border="0">
		<tr>
			<td colspan="100%" align="right"><b>Total: $<xsl:value-of select='format-number(order/total, "###,##0.00", "USD")'/>*</b></td>
		</tr>
	</table>
	</xsl:template>

</xsl:stylesheet>
<?xml version="1.0"?>
<!DOCTYPE xsl:stylesheet [
    <!ENTITY nbsp "&#160;">
    <!ENTITY dot  "&#183;">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:output method="html"/>
	<xsl:template name="h_gc_recipient_info_v1">
		<br/>
		<b><font color="#FF9933">RECIPIENT LIST FOR</font> ORDER NUMBER #<xsl:value-of select="order/erpSalesId"/></b><br/>

	<table cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td width="100%" bgcolor="#CCCCCC"><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></td>
	</tr>
	</table><br/>
	<u><b><a href="http://www.freshdirect.com/your_account/order_history.jsp"><font face="verdana, arial, sans-serif" size="2" color="black">View this order (and get printable versions of your gifts) in Your Account.</font></a></b></u><br/>
	<xsl:for-each select="order/giftCardRecipients/recipents/recipents">
	<table width="100%" cellspacing="0" cellpadding="0" border="0"><font face="verdana, arial, sans-serif" size="1" color="black">
		
		<b>
		<tr >
			<td colspan="5%" align="left" ><b><font face="verdana, arial, sans-serif" size="1" color="black"><xsl:value-of select="position()"/>.</font></b></td>
			<td colspan="25%" align="left" ><b><font face="verdana, arial, sans-serif" size="1" color="black"><xsl:value-of select="recipientName"/></font></b></td>
			<td colspan="45%" ><b><font face="verdana, arial, sans-serif" size="1" color="black">...........................................................................................................</font></b></td>
			<td colspan="24%" align="right" ><b><font face="verdana, arial, sans-serif" size="1" color="black">$<xsl:value-of select='format-number(amount, "###,##0.00", "USD")'/></font></b></td>
			<td colspan="1%" >&nbsp;</td>
		</tr>	
		<tr class="orderViewSummary">
			<br/>
		</tr></b>
		</font>
	</table>	
	</xsl:for-each>
        <br/>
	<table width="100%" cellspacing="0" cellpadding="0" border="0">
		<tr>
			<td colspan="100%" align="right"><b>Total: $<xsl:value-of select='format-number(order/total, "###,##0.00", "USD")'/>*</b></td>
		</tr>
	</table>
	</xsl:template>

</xsl:stylesheet>

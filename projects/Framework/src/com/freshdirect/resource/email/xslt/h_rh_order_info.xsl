<?xml version="1.0"?>
<!DOCTYPE xsl:stylesheet [
    <!ENTITY nbsp "&#160;">
    <!ENTITY dot  "&#183;">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:output method="html"/>
	<xsl:template name="h_rh_order_info">
		<br/>


<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">		

<TR valign="top">
<TD width="320">
		<table cellpadding="0" cellspacing="0" width="320">
			<tr>
			<td colspan="3" style="padding: 2px;" align="left">
				<img src="http://www.freshdirect.com/media_stat/images/donation/robinhood/robin_hood_logo_sm.gif" height="23" width="130" alt="Robin Hood" /><br />
				</td>
			</tr>
			<tr>
				<td colspan="3" style="padding: 2px;">
					<img src="/media_stat/images/layout/clear.gif" width="1" height="8" border="0" alt="" /><br />
				</td>
			</tr>



			<tr>
				<td colspan="2" style="padding: 2px;" align="left" valign="bottom" ><font face="verdana, arial, sans-serif" size="1" color="black">  <b><xsl:value-of select="productFullName" /></b> Subtotal&nbsp; ( <xsl:value-of select="qty"/> Meals):
					 </font> </td>
				<td style="padding: 2px;" width="70" align="left" valign="bottom"><font face="verdana, arial, sans-serif" size="1" color="black"> &nbsp; $<xsl:value-of select='format-number(order/total, "###,##0.00", "USD")'/> </font> </td>
			</tr>
			
			<tr>
				<td colspan="2" style="padding9/29/2009: 2px;" align="left"><font face="verdana, arial, sans-serif" size="1" color="black">
					<b>&nbsp;TOTAL: &nbsp; $<xsl:value-of select='format-number(order/total, "###,##0.00", "USD")'/> </b> 
				</font></td>
				
			</tr>
			<tr>
				<td colspan="3" style="padding: 2px;">
					<img src="/media_stat/images/layout/clear.gif" width="1" height="16" border="0" alt="" /><br />
				</td>
			</tr>



			<tr>
			<td>
			<table cellpadding="0" cellspacing="0" border="0" width="320">
			<tr VALIGN="TOP">
				<td WIDTH="320" COLSPAN="2"><font face="verdana, arial, sans-serif" size="1" color="black">
			        Thank you for your donation to Robin Hood. Your gift will help feed New York City's hungry families during the holiday season.<br /><br/>
				You may view this donation in <a href="http://freshdirect.com/your_account/order_history.jsp"> Your Orders.</a><br/><br/>
				</font></td>
			</tr>
			</table>
			</td>
			</tr>
		 </table>
</TD>

<td valign="top" align="CENTER" width="25">
	<img height="280" width="1" alt="" src="/images/clear.gif"/>
<br/>
</td>
<TD align="CENTER" bgcolor="#CCCCCC" width="1" valign="top">
    	 	<img src="/images/cccccc.gif" alt="" width="1" height="280" /><br />
</TD>

<td valign="top" align="CENTER" width="25">
	<img height="280" width="1" alt="" src="/images/clear.gif"/>
<br/>
</td>

<TD><font face="verdana, arial, sans-serif" size="1" color="black"> 

			<b>PAYMENT INFO</b><br/>
			<table width="100%">
			<tr>
			<td><table cellpadding="0" cellspacing="0" width="100%">
		<tr>
		<td width="100%" bgcolor="#CCCCCC"><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></td>
		</tr>
		</table>
	</td>
			</tr>
			</table><br/>
			<b>Order Total:</b><br/>
			$<xsl:value-of select='format-number(order/total, "###,##0.00", "USD")'/><br/>
			<br/>
			
	
			<xsl:if test="order/paymentMethod/paymentType != 'M'">
			<b><xsl:value-of select="order/paymentMethod/paymentMethodType"/></b><br/>
			<xsl:call-template name="format-payment-method"><xsl:with-param name="paymentMethod" select="order/paymentMethod" /></xsl:call-template><br/>
			<br/>
			</xsl:if>
			<b>Billing Address:</b><br/>
			<xsl:value-of select="order/paymentMethod/name"/><font><xsl:text> </xsl:text></font><br/>
			<xsl:call-template name="format-billing-address"><xsl:with-param name="billingAddress" select="order/paymentMethod" /></xsl:call-template><br/>
			<br/>
	
			</font>
</TD>		
<td width="2%"></td>

</TR>
</TABLE>

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

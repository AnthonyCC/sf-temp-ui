<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_gc_order_info_v1.xsl'/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_common_functions_v2.xsl'/>
	<xsl:include href='h_footer_v1.xsl'/>
	<xsl:include href='h_gc_bulk_recipient_info_v1.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<html>
<head>
</head>
<body bgcolor="#FFFFFF">
<table cellpadding="0" cellspacing="0" width="100%">
<tr>
<!-- =============== START LEFT SPACER =============== -->
<td><img src="/images/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END LEFT SPACER ================= -->

<!-- ~~~~~~~~~~~~~~~ START CONTENT ~~~~~~~~~~~~~~~ -->
<td><xsl:call-template name="h_header_v1" />
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr><td width="100%" bgcolor="#CCCCCC"><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></td>
		</tr>
		<tr><td><br/></td></tr>
					<tr>
						<td>
							<!-- FIRST ORDER TEXT -->
<font face="verdana, arial, sans-serif" size="1" color="black">
									<p><b>Dear <xsl:value-of select="customer/firstName"/></b>,</p>

									<p>Thank you for your Gift Card order (<b>#<xsl:value-of select="order/erpSalesId"/></b>). Order and payment details are below. The final total is<b> $<xsl:value-of select='format-number(order/subTotal, "###,##0.00", "USD")'/>.</b></p>
									<p> PLEASE NOTE: It may take up to <b> two hours or more</b> to activate your Gift Cards. Until the cards are active your recipients will not be able to use their gift card numbers. We will send confirmation to you via email once your newly purchased Gift Cards are active.
									   Thank you for your patience.</p>

                                                                        <p><b> To view this order (and get printable versions of your gifts) in Your Account, <a href="http://www.freshdirect.com/your_account/manage_account.jsp">click here</a>.</b></p>

									<p> We hope your recipients enjoy their gifts.</p>

									<p>FreshDirect<br/>									
									<xsl:choose><xsl:when test="order/deliveryType != 'C'">Customer Service Group</xsl:when><xsl:otherwise>Corporate Services Group</xsl:otherwise></xsl:choose></p>
</font>
									<br/><br/>							
								<p><xsl:call-template name="h_gc_order_info_v1"/></p>
							<p><font face="verdana, arial, sans-serif" size="1" color="black">NOTE: If this email does not print out clearly, please go to <a href="https://www.freshdirect.com/your_account/order_history.jsp">https://www.freshdirect.com/your_account/order_history.jsp</a> for a printer-friendly version of your order details.</font></p>
							<p><xsl:call-template name="h_gc_bulk_recipient_info_v1"/></p>

								<xsl:if test="order/paymentMethod/paymentType = 'M'">
								<br/>
								<b>Please note that you are not being charged for this order. The amount displayed above, as well as your account with FreshDirect, will reflect a zero balance within the next 48 hours.</b>
								</xsl:if>
							<p><xsl:call-template name="h_footer_v1"/></p>

						</td>
					</tr>
				</table>
			</td>
			<!-- ~~~~~~~~~~~~~~~ END CONTENT ~~~~~~~~~~~~~~~ -->

<!-- =============== BEGIN RIGHT SPACER =============== -->
<td><img src="/images/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END RIGHT SPACER ================= -->
</tr>
</table>
</body>
</html>
</xsl:template>

</xsl:stylesheet>

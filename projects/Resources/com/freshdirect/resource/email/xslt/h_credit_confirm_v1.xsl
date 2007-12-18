<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_footer_v1.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<html>
<head>
	<title>We've issued your credits</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
</head>
<body bgcolor="#ffffff">

<xsl:call-template name="h_header_v1"/>

<table cellpadding="0" cellspacing="0">
	<tr>
		<td width="20"><img src="/images/clear.gif" width="20" height="1" border="0" alt="" /></td>
		<td>
			<table cellpadding="0" cellspacing="0" width="90%">
				<tr>
					<td width="100%">
						<table width="100%" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<td width="100%" bgcolor="#cccccc"><img src="/images/clear.gif" width="600" height="1" border="0" alt="" /></td>
							</tr>
						</table>
					</td>
				</tr>
			</table><br/>
			<table cellpadding="0" cellspacing="0" width="90%">
				<tr>
					<td>
						<p><b>Dear <xsl:value-of select="customer/firstName"/></b>,</p>

						<p>Thank you for helping us to serve you better. We have issued you the following credits for <b>order #<xsl:value-of select="saleId"/></b>:<br/>
						<br/>

						<xsl:if test="complaint/storeCreditAmount &gt; 0">
							<xsl:for-each select="complaint/complaintLines/complaintLines">
                                <xsl:if test="not(number(amount)=0)">
                                    <xsl:if test="method = 'FDC'">
                                        $<xsl:value-of select='format-number(amount, "###,##0.00", "USD")'/><xsl:text>  </xsl:text> from the 
										<xsl:if test="departmentCode = 'GDW'">
											<xsl:text>Customer Service</xsl:text> 
										</xsl:if>
										<xsl:if test="not(departmentCode='GDW')">
											<xsl:value-of select="departmentName"/>
										</xsl:if>
										 Department in store credit<br/>
                                    </xsl:if>
                                </xsl:if>
							</xsl:for-each>
							$<xsl:value-of select='format-number(complaint/storeCreditAmount, "###,##0.00", "USD")'/> total store credit<br/>
							<br/>
							<xsl:if test="complaint/cashBackAmount &gt; 0">
								plus...<br/>
								<br/>
							</xsl:if>
						</xsl:if>
						<xsl:if test="complaint/cashBackAmount &gt; 0">
							<xsl:for-each select="complaint/complaintLines/complaintLines">
                                <xsl:if test="not(number(amount)=0)">
                                    <xsl:if test="method = 'CSH'">
                                        $<xsl:value-of select='format-number(amount, "###,##0.00", "USD")'/><xsl:text>  </xsl:text> from the 
										<xsl:if test="departmentCode = 'GDW'">
											<xsl:text>Customer Service</xsl:text> 
										</xsl:if>
										<xsl:if test="not(departmentCode='GDW')">
											<xsl:value-of select="departmentName"/>
										</xsl:if>
                                         Department charged back to your payment account<br/>
                                    </xsl:if>
                                </xsl:if>
							</xsl:for-each>
							$<xsl:value-of select='format-number(complaint/cashBackAmount, "###,##0.00", "USD")'/> total charged back to your payment account<br/>
							<br/>
						</xsl:if>

						$<xsl:value-of select='format-number(complaint/amount, "###,##0.00", "USD")'/> TOTAL CREDIT<br/>
						<br/>
						<xsl:if test="complaint/storeCreditAmount &gt; 0">We'll automatically subtract these store credits from your next order total.</xsl:if>
						<br/>
						<br/>
						We invite you to let us know how we're doing. <a href="http://www.freshdirect.com/help/contact_fd.jsp">Click here</a> to contact us.<br/>
						<br/>
						Thank you for shopping with us. We greatly appreciate your business and hope you'll come back soon.<br/>
						<br/>
						Sincerely,<br/>
						<br/>
						FreshDirect<br/>
						Customer Service Group</p>

						<p><xsl:call-template name="h_footer_v1"/></p>

					</td>
				</tr>
			</table>
		</td>
		<td width="20"><img src="/images/clear.gif" width="20" height="1" border="0" alt="" /></td>
	</tr>
</table>
</body>
</html>
</xsl:template>

</xsl:stylesheet>
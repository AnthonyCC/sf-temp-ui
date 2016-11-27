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
<body bgcolor="#FFFFFF">

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

                        <xsl:if test="string-length(complaint/customerEmail/customMessage) &gt; 0">
                        <div>
                           <xsl:value-of disable-output-escaping="yes" select='complaint/customerEmail/customMessageHTML'/>
                        </div>
                        </xsl:if><br/>
			Thank you for allowing us to serve you better. We have issued you the following credits for <b>order #<xsl:value-of select="saleId"/></b>:
						<br/><br/>
	$<xsl:value-of select='format-number(complaint/cashBackAmount, "###,##0.00", "USD")'/> from the FreshDirect 
                                        <xsl:if test="departmentCode = 'GDW'">
                                            <xsl:text>Customer Service</xsl:text> 
                                        </xsl:if>
                                        <xsl:if test="not(departmentCode='GDW')">
                                            <xsl:value-of select="departmentName"/>
                                        </xsl:if>
                                         store credited back to your credit card.<br/><br/>


						<xsl:if test="complaint/storeCreditAmount &gt; 0">
							<xsl:for-each select="complaint/complaintLinesAggregated/complaintLinesAggregated">
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

						<table width="100%" cellspacing="0" cellpadding="0" border="0"><font face="verdana, arial, sans-serif" size="1" color="black">
						<b><tr>
							<td colspan="45%" ><b><font face="verdana, arial, sans-serif" size="1" color="black">Certificate #</font></b></td>
							<td colspan="5%" align="left" ><b><font face="verdana, arial, sans-serif" size="1" color="black">Card Type.</font></b></td>
							<td colspan="25%" align="left" ><b><font face="verdana, arial, sans-serif" size="1" color="black">Amount</font></b></td>
								
								
						</tr></b></font>
						
						<xsl:for-each select="complaint/complaintLines/complaintLines">
						
							<b>
							<tr >
								<td colspan="45%" ><font face="verdana, arial, sans-serif" size="1" color="black"><xsl:value-of select="certificateNumber"/></font></td>
								<td colspan="5%" align="left" ><font face="verdana, arial, sans-serif" size="1" color="black"><xsl:value-of select="templateId"/></font></td>
								<td colspan="25%" align="left" ><font face="verdana, arial, sans-serif" size="1" color="black">$<xsl:value-of select='format-number(amount, "###,##0.00", "USD")'/></font></td>
								
								
							</tr>	
							<tr class="orderViewSummary">
								<br/>
							</tr></b>
						</xsl:for-each>	
						</table>	

						<!--<xsl:if test="not(number(amount)=0)">
						    <xsl:if test="method = 'CSH'">
							$<xsl:value-of select='format-number(amount, "###,##0.00", "USD")'/><xsl:text>  </xsl:text> 
							 store credited back to your credit card.<br/>
						    </xsl:if>
						</xsl:if>-->
						
							<!--$<xsl:value-of select='format-number(complaint/cashBackAmount, "###,##0.00", "USD")'/> total charged back to your credit card<br/>-->
							<br/>
						</xsl:if>

						<b>$<xsl:value-of select='format-number(complaint/amount, "###,##0.00", "USD")'/> TOTAL CREDIT<br/></b>
						<br/>
						<xsl:if test="complaint/storeCreditAmount &gt; 0">Please note it takes approximately two business days for your store credit to become available. Once it is available we'll automatically subtract these store credits from your next order total at the last stage of checkout.</xsl:if>
						<xsl:if test="complaint/cashBackAmount &gt; 0">Please note that this credit should reflect on your credit card within five business days.</xsl:if>
						<br/>
						<br/>
						I'd like to thank you for letting us know what occurred and giving us the opportunity to help solve your problem. If, for any reason you're dissatisfied with how we resolved your problem, or need further assistance, please feel free to e-mail us directly by clicking on the contact us link on the website or call us at 212-796-8002.<br/>
						<br/>
						Your satisfaction is our number one priority!<br/>
						<br/>
						Sincerely,<br/>
						<br/>
                          <xsl:if test="string-length(complaint/customerEmail/signature) &gt; 0">
                            <xsl:value-of select="complaint/customerEmail/signature"/><br/>
                          </xsl:if>
						<p>FreshDirect<br/>									
						<xsl:choose><xsl:when test="order/deliveryType != 'C'">Customer Service Group</xsl:when><xsl:otherwise>Corporate Services Group</xsl:otherwise></xsl:choose></p>
                                                <p><b> To view the FreshDirect Gift Cards Terms and Conditions, <a href="http://www.freshdirect.com/media/editorial/giftcards/media_includes/terms_and_conditions.html">Please click here</a>.</b></p>
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
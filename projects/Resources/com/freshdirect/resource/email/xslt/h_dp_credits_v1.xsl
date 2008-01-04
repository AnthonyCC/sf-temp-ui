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
						<p>We apologize for any inconvenience you may have experienced with your order <b>#<xsl:value-of select="saleId"/></b>.
						   We have issued you the following credit:<br/><br/>
					<xsl:if test="dpName='UNLIMITED'">
						<xsl:if test="number(creditCount)=1">
                                        	Your DeliveryPass has been extended by one additional week. 
                              	</xsl:if>
						<xsl:if test="number(creditCount)=2">
                                        	Your DeliveryPass has been extended by two additional weeks. 
                              	</xsl:if>
						<xsl:if test="creditCount=3">
                                        	Your DeliveryPass has been extended by three additional weeks. 
                              	</xsl:if>
					</xsl:if>
					<xsl:if test="dpName='BSGS'">
						<xsl:if test="number(creditCount)=1">
                                        	One free delivery has been added to your DeliveryPass.
                              	</xsl:if>
						<xsl:if test="number(creditCount)=2">
                                        	Two free deliveries have been added to your DeliveryPass.
                              	</xsl:if>
						<xsl:if test="creditCount=3">
                                        	Three free deliveries have been added to your DeliveryPass.
                              	</xsl:if>
					</xsl:if>


						<br/>
						<br/>
						We invite you to let us know how we're doing. <a href="http://www.freshdirect.com/help/contact_fd.jsp">Click here</a> to contact us.<br/>
						<br/>
						Thank you for shopping with us. We greatly appreciate your business and hope you'll come back soon.<br/>
						<br/>
						Sincerely,<br/>
						<br/>
						Your Customer Service Team at FreshDirect.</p>

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
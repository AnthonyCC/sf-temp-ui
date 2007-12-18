<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_common_functions_v2.xsl'/>
	<xsl:include href='h_footer_v1.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<html>
<head>
	<title>Cancellation receipt</title>
	<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
</head>
<body bgcolor="#ffffff">

<xsl:call-template name="h_header_v1"/>

<table cellpadding="0" cellspacing="0">
	<tr>
		<td WIDTH="20"><img src="/images/clear.gif" width="20" height="1" border="0" alt="" /></td>
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

							<p><b>Hello <xsl:value-of select="customer/firstName"/></b>,</p>

							<p>We received your cancellation request for your order <b>(#<xsl:value-of select="orderNumber"/>)</b> 
							scheduled for between <b><xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="deliveryStartTime" /></xsl:call-template> 
							and <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="deliveryEndTime" /></xsl:call-template></b> 
							on <b><xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="deliveryStartTime" /></xsl:call-template></b>. 
							The order will not be delivered and you won't be charged. All of the items from this order will be stored as <a href="http://www.freshdirect.com/quickshop/index.jsp">"Your Last Order"</a> in Quickshop.</p>

							<p>We're always trying to improve and your feedback is important to us. If you have a moment, please <a href="http://www.freshdirect.com/help/contact_fd.jsp">tell us</a> your reasons for canceling. (You can also call us at <xsl:choose><xsl:when test="customer/chefsTable = 'true'">1-866-511-1240</xsl:when><xsl:otherwise>1-212-796-8002</xsl:otherwise></xsl:choose>.)</p>

							<p>We hope you <a href="http://www.freshdirect.com/">come back</a> soon for more of the freshest, highest-quality food at the best prices in New York.</p>

							<p>Best Regards,<br/>
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
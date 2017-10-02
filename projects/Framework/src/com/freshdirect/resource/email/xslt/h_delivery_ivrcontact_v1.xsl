<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_common_functions_v2.xsl'/>
	<xsl:include href='h_footer_v1.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<html lang="en-US" xml:lang="en-US">
<head>
	<title>FreshDirect is trying to reach you</title>
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
							<p>This is FreshDirect's automated notification system.</p>
							<p>This e-mail was sent to you because our delivery team would like to speak with you about your order.</p>
							<p>Please call us at <xsl:value-of select="customer/customerServiceContact"/> at your earliest convenience</p>
							<p>Press option 1.</p>
							<p>Your order number is <b>(#<xsl:value-of select="orderNumber"/>)</b></p> 
							
							<br/><p>Thank you,</p>		
							Your FreshDirect Customer Service Team<br/>
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
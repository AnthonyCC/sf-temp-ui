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

							<p><b>Dear <xsl:value-of select="gcDlvInfo/recepientModel/recipientName"/></b>,</p>

							<p>There was an error with the Gift Card you recently used <b>(Certificate #<xsl:value-of select="gcDlvInfo/certificationNumber"/>)</b> and the corresponding order has been placed on hold. Please contact the Gift Card purchaser for more information.</p>
							<br/>
							<p> We apologize for any inconvenience this may cause you.</p>
							<br/>
							<p>Sincerly,<br/>
							FreshDirect Customer Service
							</p>

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
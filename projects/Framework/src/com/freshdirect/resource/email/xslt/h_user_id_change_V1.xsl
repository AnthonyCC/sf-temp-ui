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

							<p>Hi <xsl:value-of select="customer/firstName"/>,</p>

							<p>We just wanted to confirm that you’ve successfully made a change to the email address associated with your account. If you didn't make this change or have any related questions, please call us at 1-866-283-7374.</p>

							<p>Happy Shopping,<br/>
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
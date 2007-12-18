<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:doc="http://xsltsl.org/xsl/documentation/1.0"
	xmlns:str="http://xsltsl.org/string"
	extension-element-prefixes="doc str">
	<xsl:output method="html"/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_optout_footer.xsl'/>
	<xsl:include href='h_footer_v2.xsl'/>
	<xsl:include href='string.xsl' />
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">

<html>
	<head>
		<title>Get $10 off your second order!</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
	</head>
<body bgcolor="#FFFFFF" text="#333333">
	<xsl:call-template name="mail_body" />
</body>
</html>

</xsl:template>

<xsl:template name="mail_body">

<table cellpadding="0" cellspacing="0">
<tr>
<!-- =============== START LEFT SPACER =============== -->
<td><img src="/images/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END LEFT SPACER ================= -->

<!-- ~~~~~~~~~~~~~~~ START CONTENT ~~~~~~~~~~~~~~~ -->
<td><xsl:call-template name="h_header_v1" />
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
			<td WIDTH="100%"><table WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0"><tr><td BGCOLOR="#CCCCCC"><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></td></tr></table></td>
		</tr>
	</table><br/>
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
		<td>
			<p>Dear <xsl:value-of select="customer/firstName"/>,</p>
			
			<p>We hope you've had a great summer and are gearing up for a restful Labor Day.  We wanted to let you know about some changes in our delivery / pick-up schedule for the week of Labor Day. Our revised Labor Day delivery / pick-up schedule is as follows:</p>

			<table width="40%" cellpadding="0" cellspacing="4" border="0">
				<tr>
					<td width="34%">Day</td>
					<td width="33%">Date</td>
					<td width="33%">Status</td>
				</tr>
				<tr>
					<td>Tuesday</td>
					<td>8/26/03</td>
					<td>Open</td>
				</tr>
				<tr>
					<td>Wednesday</td>
					<td>8/27/03</td>
					<td>Open</td>
				</tr>
				<tr>
					<td>Thursday</td>
					<td>8/28/03</td>
					<td>Open</td>
				</tr>
				<tr>
					<td>Friday</td>
					<td>8/29/03</td>
					<td>Closed</td>
				</tr>
				<tr>
					<td>Saturday</td>
					<td>8/30/03</td>
					<td>Open</td>
				</tr>
				<tr>
					<td>Sunday</td>
					<td>8/31/03</td>
					<td>Open</td>
				</tr>
				<tr>
					<td>Monday</td>
					<td>9/1/02</td>
					<td>Closed</td>
				</tr>
				<tr>
					<td>Tuesday</td>
					<td>9/2/03</td>
					<td>Open</td>
				</tr>
				<tr>
					<td>Wednesday</td>
					<td>9/3/03</td>
					<td>Open</td>
				</tr>
				<tr>
					<td>Thursday</td>
					<td>9/4/04</td>
					<td>Open</td>
				</tr>
			</table>

			<p>Our normal delivery schedule will resume on Thursday 9/4/03. We hope our revised schedule does not inconvenience you.  Have a wonderful Labor Day.</p>


			<p>Happy eating,
			<br/><br/>
			FreshDirect</p>
			
			<p><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/></p>
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

</xsl:template>

</xsl:stylesheet>
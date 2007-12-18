<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_footer_v1.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<html>
<head>
	<title>Important message from FreshDirect</title>
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
					<td width="100%" bgcolor="#cccccc"><img src="/images/clear.gif" width="600" height="1" border="0" alt="" /></td>
				</tr>
			</table><br/>
			<table cellpadding="0" cellspacing="0" width="90%">
				<tr>
					<td>
						<P><b>Hello <xsl:value-of select="customer/firstName"/></b>,</P>

						<P>Please click on the following link to change your password:</P>
						<P><a href="{passwordLink}"><xsl:value-of select="passwordLink"/></a></P>

						<P>To protect your account, this link will expire at <b><xsl:call-template name="format-expiration-time"><xsl:with-param name="dateTime" select="expirationTime"/></xsl:call-template></b>, so be sure to use it before then.</P>

						<P>If you have any questions, call us at 1-866-283-7374.</P>

						<P>Cheers,<br/>
						<br/>
						FreshDirect<br/>
						Customer Service Group</P>

						<P><xsl:call-template name="h_footer_v1"/></P>
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

<xsl:template name="format-expiration-time">
	<xsl:param name="dateTime" />
	<xsl:param name="format" select="'%I:%M %p, %B %d'"/>
	<xsl:call-template name="dt:format-date-time">
		<xsl:with-param name="year" select="substring($dateTime, 1,4)"/>
		<xsl:with-param name="month" select="substring($dateTime, 6,2)"/>
		<xsl:with-param name="day" select="substring($dateTime, 9,2)"/>
		<xsl:with-param name="hour" select="substring($dateTime, 12, 2)"/>
		<xsl:with-param name="minute" select="substring($dateTime, 15, 2)"/>
		<xsl:with-param name="format" select="$format"/>
	</xsl:call-template>
</xsl:template>

</xsl:stylesheet>
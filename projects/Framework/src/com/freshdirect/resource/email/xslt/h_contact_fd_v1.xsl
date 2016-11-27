<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_order_info_v1.xsl'/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_footer_v1.xsl'/>
	<xsl:output method="html"/>
<xsl:template match="contact-fDEmail">
<HTML>
<HEAD>
	<TITLE>Thank you for ordering from FreshDirect!</TITLE>
	<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
</HEAD>
<BODY BGCOLOR="#FFFFFF">

<xsl:call-template name="h_header_v1"/>

<TABLE cellpadding="0" cellspacing="0"><TR>
	<!-- =============== START LEFT SPACER =============== -->
	<TD WIDTH="20"><img src="/images/clear.gif" width="20" height="1" border="0" alt="" /></TD>
	<!-- =============== END LEFT SPACER ================= -->

	<!-- ~~~~~~~~~~~~~~~ START CONTENT ~~~~~~~~~~~~~~~ -->
	<TD>
<table cellpadding="0" cellspacing="0" width="90%">
	<TR>
		<TD WIDTH="100%"><TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0"><TR><TD WIDTH="100%" BGCOLOR="#CCCCCC"><img src="/images/clear.gif" width="600" height="1" border="0" alt="" /></TD></TR></TABLE></TD>
	</TR>
</table><br/>
<table cellpadding="0" cellspacing="0" width="90%">
	<TR><TD>

<P>Hey, this is an email!</P>

<P>Customer Name: <xsl:value-of select="first-name"/><xsl:text> </xsl:text><xsl:value-of select="last-name"/></P>

<P>Home Phone: <xsl:value-of select="home-phone"/></P>

<P>Alt. Phone: <xsl:value-of select="alt-phone"/></P>

<P>Area of Concern: <xsl:value-of select="area"/></P>

<P>Comments: <xsl:value-of select="body"/></P>

	</TD>
	</TR>
</table>
</TD>
<!-- ~~~~~~~~~~~~~~~ END CONTENT ~~~~~~~~~~~~~~~ -->

<!-- =============== BEGIN RIGHT SPACER =============== -->
<TD WIDTH="20"><img src="/images/clear.gif" width="20" height="1" border="0" alt="" /></TD>
<!-- =============== END RIGHT SPACER ================= -->
</TR>
</TABLE>
</BODY>
</HTML>
</xsl:template>


<!-- DELIVERY START DATE/TIME FORMATTER -->
<xsl:template name="format-delivery-start">
	<xsl:param name="str"><xsl:value-of select="@date-start-time"/></xsl:param>
	<xsl:param name="format" select="'%I:%M %p'"/>
	<xsl:call-template name="dt:format-date-time">
		<xsl:with-param name='hour' select='substring($str, 12, 2)'/>
		<xsl:with-param name='minute' select='substring($str, 15, 2)'/>
		<xsl:with-param name="format" select="$format"/>
	</xsl:call-template>
</xsl:template>

<!-- DELIVERY END DATE/TIME FORMATTER -->
<xsl:template name="format-delivery-end">
	<xsl:param name="str"><xsl:value-of select="@date-end-time"/></xsl:param>
	<xsl:param name="format" select="'%I:%M %p'"/>
	<xsl:call-template name="dt:format-date-time">
		<xsl:with-param name='hour' select='substring($str, 12, 2)'/>
		<xsl:with-param name='minute' select='substring($str, 15, 2)'/>
		<xsl:with-param name="format" select="$format"/>
	</xsl:call-template>
</xsl:template>

<!-- ORDER DATE FORMATTER
<xsl:template name="format-order-date">
	<xsl:param name="year"><xsl:value-of select="substring(@date-placed,1,4)"/></xsl:param>
	<xsl:param name="month"><xsl:value-of select="substring(@date-placed,6,2)"/></xsl:param>
	<xsl:param name="day"><xsl:value-of select="substring(@date-placed,9,2)"/></xsl:param>
	<xsl:param name="format" select="'%A, %B %d'"/>
	<xsl:call-template name="dt:format-date-time">
		<xsl:with-param name="year" select="$year"/>
		<xsl:with-param name="month" select="$month"/>
		<xsl:with-param name="day" select="$day"/>
		<xsl:with-param name="format" select="$format"/>
	</xsl:call-template>
</xsl:template>-->

<xsl:template match="delivery-address">
	<xsl:value-of select="address1"/><xsl:text> </xsl:text><xsl:if test="element-available('apartment')"><xsl:value-of select="apartment"/></xsl:if><br/>
	<xsl:if test="element-available('address2')"><xsl:value-of select="address2"/><br/></xsl:if>
	<xsl:value-of select="city"/>,<xsl:text> </xsl:text><xsl:value-of select="state"/><xsl:text> </xsl:text><xsl:value-of select="zip-code"/><br/>
</xsl:template>

</xsl:stylesheet>
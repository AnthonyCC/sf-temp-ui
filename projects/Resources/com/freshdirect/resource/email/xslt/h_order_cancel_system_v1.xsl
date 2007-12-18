<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_order_info_v1.xsl'/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_common_functions_v1.xsl'/>
	<xsl:include href='h_footer_v1.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="erp-email">
<HTML>
<HEAD>
	<TITLE>Cancellation receipt</TITLE>
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

<P><b>Hello <xsl:value-of select="@first-name"/></b>,</P>

<P>Your order <b>(#<xsl:value-of select="@order-id"/>)</b> scheduled for between <b><xsl:value-of select="order/delivery-info/@formatted-delivery-start-time"/> and <xsl:value-of select="order/delivery-info/@formatted-delivery-end-time"/></b> on <b><xsl:call-template name="format-delivery-date"/></b> has been cancelled. The order will not be delivered and you won't be charged. All of the items from this order will be stored as <a href="http://www.freshdirect.com/quickshop/index.jsp">"Your Last Order"</a> in Quickshop.</P>

<P>For more details please contact us at 1-866-283-7374. We're here Monday through Thursday from 6.30 a.m. to 1 a.m., Friday from 6.30 a.m. to 10 p.m., Saturday from 7:30 a.m. to 10 p.m., and Sunday from 7:30 a.m. to 1 a.m.</P>

<P>Sincerely,<br/>
<br/>
FreshDirect<br/>
Customer Service Group</P>

<P><xsl:call-template name="h_footer_v1"/></P>

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

<!-- DELIVERY DATE FORMATTER -->
<!--<xsl:template name="format-delivery-date">
	<xsl:param name="year"><xsl:value-of select="substring(order/delivery-info/@delivery-start-time,1,4)"/></xsl:param>
	<xsl:param name="month"><xsl:value-of select="substring(order/delivery-info/@delivery-start-time,6,2)"/></xsl:param>
	<xsl:param name="day"><xsl:value-of select="substring(order/delivery-info/@delivery-start-time,9,2)"/></xsl:param>
	<xsl:param name="format" select="'%A, %B %d'"/>
	<xsl:call-template name="dt:format-date-time">
		<xsl:with-param name="year" select="$year"/>
		<xsl:with-param name="month" select="$month"/>
		<xsl:with-param name="day" select="$day"/>
		<xsl:with-param name="format" select="$format"/>
	</xsl:call-template>
</xsl:template>-->

<!-- DELIVERY ADDRESS FORMATTER -->
<!--<xsl:template name="format-delivery-address">
	<xsl:value-of select="order/delivery-info/address/@address1"/><xsl:text> </xsl:text><xsl:if test="element-available('order/delivery-info/address/@apartment')"><xsl:value-of select="order/delivery-info/address/@apartment"/></xsl:if><br/>
	<xsl:if test="element-available('order/delivery-info/address/@address2')"><xsl:value-of select="order/delivery-info/address/@address2"/><br/></xsl:if>
	<xsl:value-of select="order/delivery-info/address/@city"/>,<xsl:text> </xsl:text><xsl:value-of select="order/delivery-info/address/@state"/><xsl:text> </xsl:text><xsl:value-of select="order/delivery-info/address/@zip-code"/><br/>
</xsl:template>-->

</xsl:stylesheet>
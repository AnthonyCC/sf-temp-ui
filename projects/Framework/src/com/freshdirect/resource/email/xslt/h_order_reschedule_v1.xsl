<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='h_order_info_v1.xsl'/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_footer_v1.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="erp-email">
<HTML>
<HEAD>
	<TITLE>Your order has been rescheduled</TITLE>
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

<P>Thanks for rescheduling your order. We apologize for any inconvenience this has caused you. [print next sentence only if credit issued for order]To make up for your inconvenience, we are giving you $[total credit amount] off your next order.[/end of optional sentence]</P>

<P>Your order (#<xsl:value-of select="@order-id"/>) will be delivered between <b><xsl:call-template name="format-delivery-start"/> and <xsl:call-template name="format-delivery-end"/></b> on <xsl:call-template name="format-delivery-date"/> to the address below:
[Delivery address with line breaks]</P>

<P>If you have last-minute updates or additions to your order, go to <a href="http://www.freshdirect.com/your_account/manage_account.jsp">your account</a> and make the changes before <xsl:call-template name="format-cutoff-date-time"/>.</P>

<P>We hope you enjoy everything in your order. Please <a href="http://www.freshdirect.com/">come back</a> soon for more of the freshest, highest-quality food at the best prices in New York.</P>

<P><b>Happy eating!</b><br/>
<br/>
FreshDirect<br/>
Customer Service Group</P>

<P><b>Order Information</b>:
<xsl:call-template name="h_order_info_v1"/></P>

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

<!-- DELIVERY CUTOFF DATE/TIME FORMATTER -->
<xsl:template name="format-cutoff-date-time">
	<xsl:param name="year"><xsl:value-of select="substring(order/delivery-info/@delivery-cutoff-time,1,4)"/></xsl:param>
	<xsl:param name="month"><xsl:value-of select="substring(order/delivery-info/@delivery-cutoff-time,6,2)"/></xsl:param>
	<xsl:param name="day"><xsl:value-of select="substring(order/delivery-info/@delivery-cutoff-time,9,2)"/></xsl:param>
	<xsl:param name="hour"><xsl:value-of select="substring(order/delivery-info/@delivery-cutoff-time, 12, 2)"/></xsl:param>
	<xsl:param name="minute"><xsl:value-of select="substring(order/delivery-info/@delivery-cutoff-time, 15, 2)"/></xsl:param>
	<xsl:param name="format" select="'%I:%M %p, %B %d'"/>
	<xsl:call-template name="dt:format-date-time">
		<xsl:with-param name="year" select="$year"/>
		<xsl:with-param name="month" select="$month"/>
		<xsl:with-param name="day" select="$day"/>
		<xsl:with-param name="hour" select="$hour"/>
		<xsl:with-param name="minute" select="$minute"/>
		<xsl:with-param name="format" select="$format"/>
	</xsl:call-template>
</xsl:template>

	<!-- DELIVERY DATE FORMATTER -->
	<xsl:template name="format-delivery-date">
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
	</xsl:template>

	<!-- DELIVERY START TIME FORMATTER -->
	<xsl:template name="format-delivery-start">
		<xsl:param name="str"><xsl:value-of select="order/delivery-info/@delivery-start-time"/></xsl:param>
		<xsl:param name="format" select="'%I:%M %p'"/>
		<xsl:call-template name="dt:format-date-time">
			<xsl:with-param name='hour' select='substring($str, 12, 2)'/>
			<xsl:with-param name='minute' select='substring($str, 15, 2)'/>
			<xsl:with-param name="format" select="$format"/>
		</xsl:call-template>
	</xsl:template>

	<!-- DELIVERY END TIME FORMATTER -->
	<xsl:template name="format-delivery-end">
		<xsl:param name="str"><xsl:value-of select="order/delivery-info/@delivery-end-time"/></xsl:param>
		<xsl:param name="format" select="'%I:%M %p'"/>
		<xsl:call-template name="dt:format-date-time">
			<xsl:with-param name='hour' select='substring($str, 12, 2)'/>
			<xsl:with-param name='minute' select='substring($str, 15, 2)'/>
			<xsl:with-param name="format" select="$format"/>
		</xsl:call-template>
	</xsl:template>

</xsl:stylesheet>
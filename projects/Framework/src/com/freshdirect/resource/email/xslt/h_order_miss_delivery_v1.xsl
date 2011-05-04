<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_common_functions_v1.xsl'/>
	<xsl:include href='h_footer_v1.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="erp-email">
<HTML>
<HEAD>
	<TITLE>Unable to deliver your order</TITLE>
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
<xsl:choose>
	<!-- 	*******************************************************
		(CUSTOMER AT FAULT == TRUE) TEXT
		*******************************************************-->
	<xsl:when test="@customer-at-fault='true'">
<P><b>Dear <xsl:value-of select="@first-name"/></b>,</P>

<P>Unfortunately, since no one was available to receive our delivery, we were unable to complete your order <b>(#<xsl:value-of select="@order-id"/>)</b> scheduled for between <b><xsl:call-template name="format-delivery-start"/> and <xsl:call-template name="format-delivery-end"/></b> on <b><xsl:call-template name="format-delivery-date"/></b> to:<br/>
<br/>
<b><xsl:apply-templates select="order/delivery-info/address"/></b><br/>

We have attempted to reach you but were unable to do so. Please contact us as soon as possible at 1-866-283-7374 to discuss alternate delivery options.</P>

<P>Sincerely,<br/>
<br/>
FreshDirect<br/>
Customer Service Group</P>
	</xsl:when>
	<!-- 	*******************************************************
		(CUSTOMER AT FAULT == FALSE) TEXT
		*******************************************************-->
	<xsl:otherwise>
<P><b>Dear <xsl:value-of select="@first-name"/></b>,</P>

<P>We were unable to deliver your order <b>(#<xsl:value-of select="@order-id"/>)</b> scheduled for between <b><xsl:call-template name="format-delivery-start"/> and <xsl:call-template name="format-delivery-end"/></b> on <b><xsl:call-template name="format-delivery-date"/></b> to:<br/>
<br/>
<b><xsl:apply-templates select="order/delivery-info/address"/></b><br/>

We are sorry for any inconvenience this has caused you. <xsl:value-of select="notes"/><br/>
<br/>
Please contact us as soon as possible at 1-866-283-7374 to reschedule delivery.</P>

<P>Our apologies,<br/>
<br/>
FreshDirect<br/>
Customer Service Group</P>
	</xsl:otherwise>
</xsl:choose>

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

</xsl:stylesheet>
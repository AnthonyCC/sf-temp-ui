<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_footer_v1.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<HTML>
<HEAD>
	<TITLE>Subscription cancelled</TITLE>
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
<P>Dear <xsl:value-of select="customer/firstName"/>,</P>

<P>Unfortunately, we were unable to process your auto-renewal of FreshDirect DeliveryPass because the credit card in your account has expired.</P>
<br/>
<P>To ensure that you will continue receiving the fee-free deliveries you receive with FreshDirect DeliveryPass membership, please follow the steps below:</P>
<br/>
<p>1) Go to Your Account at FreshDirect to log in.</p>
<p>2) Click into "Payment Options" to update your expiration date or add a new payment type.</p>
<p>3) Place an order with a new DeliveryPass membership in your shopping cart.</p>

<P>If you have questions or need assistance, please contact our Customer Service Team via email: customerservice@freshdirect.com</P>
<br/>
<P>Sincerely,<br/>
<br/>
Your Customer Service Team at <a href="http://www.freshdirect.com/">FreshDirect</a>
</P>

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
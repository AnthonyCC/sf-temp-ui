<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_common_functions_v1.xsl'/>
	<xsl:include href='h_footer_v2.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="erp-email">
<HTML>
<HEAD>
	<TITLE>Now available in your neighborhood!</TITLE>
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
	<TR>
		<TD>
			Good things come to those who wait.<br/>
			<br/>
			FreshDirect is now delivering to Roosevelt Island! Place your first Web order and get $50 worth of free fresh food* just for trying us.<br/>
			<br/><br/>
			Choose from over 3,000 irresistibly fresh items, plus a full selection of organic foods and popular grocery and household brands. All delivered to your door, exactly the way you want, with 100% satisfaction guaranteed.<br/>
			<br/><br/>
			To top it off, delivery is free for the first three orders, and our drivers won't accept tips.<br/>
			<br/><br/>
			You've waited long enough. Log on to <a href="http://www.freshdirect.com/index.jsp">www.freshdirect.com</a> today.<br/>
			<br/>
			<br/>
			<b>Enjoy,</b><br/>
			<br/>
			Joe Fedele<br/>
			CEO and Creator, FreshDirect<br/>
			Co-founder of Fairway Uptown<br/>
			<br/>
			* Web orders only. Limited time offer. One per household. Certain perishables may be excluded, $15 limit per item. Available in selected zones.<br/>

			<P><xsl:call-template name="h_footer_v2"/></P>
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
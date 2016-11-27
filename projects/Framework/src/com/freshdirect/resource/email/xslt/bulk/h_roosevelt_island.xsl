<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_common_functions_v1.xsl'/>
	<xsl:include href='h_optout_footer.xsl'/>
	<xsl:include href='h_footer_v1.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="erp-email">
<HTML>
<HEAD>
	<TITLE>As part of our family</TITLE>
	<link rel="stylesheet" href="http://www.freshdirect.com/common/css/emails.css"/>
</HEAD>
<BODY BGCOLOR="#FFFFFF">

<xsl:call-template name="h_header_v1"/>

<TABLE cellpadding="0" cellspacing="0"><TR>
	<!-- =============== START LEFT SPACER =============== -->
	<TD WIDTH="20"><img src="/images/clear.gif" width="20" height="1" border="0" alt="" /></TD>
	<!-- =============== END LEFT SPACER ================= -->

	<!-- ~~~~~~~~~~~~~~~ START CONTENT ~~~~~~~~~~~~~~~ -->
	<TD>
<TABLE cellpadding="0" cellspacing="0" width="90%">
	<TR><TD WIDTH="100%"><TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0"><TR><TD WIDTH="100%" BGCOLOR="#CCCCCC"><img src="/images/clear.gif" width="600" height="1" border="0" alt="" /></TD></TR></TABLE>
<br/>
<table cellpadding="0" cellspacing="0">
	<TR><TD>
<P><b>Dear <xsl:value-of select="@first-name"/></b>,</P>
<P><a href="http://www.freshdirect.com">FreshDirect</a> is proud to be servicing the Roosevelt Island community and I'd like to thank you for being one of our first customers.  We noticed that you have not placed an order with us for quite some time and we would like to ask you for your feedback.</P>

<P>I created FreshDirect to give New Yorker's the best food at the best possible price. I also believe in treating customers like my own family, so I want to know the good and the bad. Are you pleased with the food quality? Is our selection satisfactory? How were our prices?  Is there anything we can do better? <a href="mailto:joe@freshdirect.com">Drop me an e-mail</a> and let me know your honest thoughts.</P>

<P>Direct feedback is the most valuable thing I can think of to ensure that your FreshDirect food shopping experience is the best you've ever had.  We strive for excellence in all aspects of our business and we would love for you to continue to use FreshDirect as your source for fresh food and groceries at savings up to 35% less than your local store.</P>
<P>I look forward to hearing from you.</P>
            <P><b>Joe Fedele</b><br/>
            Creator and CEO<br/>
            <a href="http://www.freshdirect.com">FreshDirect</a><br/>
            Fresher Food, Lower Prices, Delivered</P>
		</TD>
        <TD width="20"><img src="/images/clear.gif" width="20" height="1" border="0" alt="" /></TD>
        <TD>
            <img src="http://www.freshdirect.com/media_stat/images/template/about/joe_photo.jpg" width="136" height="250" border="0"/>
            <br/>
            <img src="http://www.freshdirect.com/media_stat/images/template/about/joe_title.gif" width="130" height="30" border="0"/>
        </TD>
	</TR>
</table>
<P><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v1"/></P>
</TD></TR></TABLE></TD>
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
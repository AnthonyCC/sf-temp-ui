<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:output method="html"/>
	<xsl:template name="h_footer_v2">
		<xsl:param name="preview">false</xsl:param>
<table cellpadding="0" cellspacing="0" width="100%" class="fdFooter">
	<TR>
		<TD WIDTH="100%"><TABLE WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0"><TR><TD WIDTH="100%" BGCOLOR="#CCCCCC"><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></TD></TR></TABLE></TD>
	</TR>
</table><br/>
<center>
	<xsl:choose>
		<xsl:when test="$preview = 'true'">
			<font color="336600"><b>Go to FreshDirect</b></font> | <font color="336600"><b>Contact Us</b></font> | <font color="336600"><b>Frequently Asked Questions</b></font>
		</xsl:when>
		<xsl:otherwise>
			<b><a href="http://www.freshdirect.com/index.jsp" style="color:#336600;" class="fdFooter"><font color="#336600"><u>Go to FreshDirect</u></font></a></b> | <b><a href="http://www.freshdirect.com/help/contact_fd.jsp" style="color:#336600;" class="fdFooter"><font color="#336600"><u>Contact Us</u></font></a></b> | <b><a href="http://www.freshdirect.com/help/faq_home.jsp?page=faqHome" style="color:#336600;" class="fdFooter"><font color="#336600"><u>Frequently Asked Questions</u></font></a></b>
		</xsl:otherwise>
	</xsl:choose>
	<br/><br/><img src="http://www.freshdirect.com/media_stat/images/template/freshness/fresh_guar_photos.jpg" alt=""/><br/>
	<!-- &#169; --> &#169; 2002 - 2007 FRESHDIRECT. All Rights Reserved.
</center>
	</xsl:template>
</xsl:stylesheet>
<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:output method="html"/>
	<xsl:template name="h_footer_v1">
<table cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td width="100%" height="1" bgcolor="#CCCCCC"><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></td>
	</tr>
</table><br/>
<center>
	<b><a href="http://www.freshdirect.com/" style="color:#336600;font-size:12px;text-decoration:underline;">Go to FreshDirect</a></b> | <b><a href="http://www.freshdirect.com/help/contact_fd.jsp" style="color:#336600;font-size:12px;text-decoration:underline;">Contact Us</a></b> | <b><a href="http://www.freshdirect.com/your_account/order_history.jsp" style="color:#336600;font-size:12px;text-decoration:underline;">Check or Change an Order</a></b> | <b><a href="http://www.freshdirect.com/help/faq_home.jsp?page=faqHome" style="color:#336600;font-size:12px;text-decoration:underline;">Frequently Asked Questions</a></b><br>
		<span class="fdFooter_s">FreshDirect, LLC. 2 St Ann's Ave Bronx, NY 10454</span></br>
	<br/><br/><img src="http://www.freshdirect.com/media_stat/images/template/freshness/fresh_guar_photos.jpg" alt=""/><br/>
	<span class="fdFooter_s">&#169; 2002 - <xsl:value-of select="substring(//curYear,1,4)" /> Fresh Direct, LLC. All Rights Reserved.</span>

</center>
	</xsl:template>
</xsl:stylesheet>
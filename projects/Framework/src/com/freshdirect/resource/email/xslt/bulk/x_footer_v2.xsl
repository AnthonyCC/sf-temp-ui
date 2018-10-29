<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:output method="text"/>
	<xsl:template name="x_footer_v2">
-----

QUICK LINKS:

Go to FreshDirect
http://www.freshdirect.com

Contact Us
http://www.freshdirect.com/help/contact_fd.jsp

Frequently Asked Questions
http://www.freshdirect.com/help/faq_home.jsp

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
FreshDirect, LLC. 2 St Ann's Ave Bronx, NY 10454 
(c) 2002 - <xsl:value-of select="substring(//curYear,1,4)" /> FRESHDIRECT. All Rights Reserved. 
	</xsl:template>
</xsl:stylesheet>
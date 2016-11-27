<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:doc="http://xsltsl.org/xsl/documentation/1.0"
	xmlns:str="http://xsltsl.org/string"
	extension-element-prefixes="doc str">
	<xsl:output method="html"/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_footer_v2.xsl'/>
	<xsl:include href='string.xsl' />
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">

<html>
	<head>
        <BASE href="http://www.freshdirect.com" /> 
		<title>Delivery Delay Notice</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
	</head>
<body bgcolor="#FFFFFF" text="#333333">
	<xsl:call-template name="mail_body" />
</body>
</html>

</xsl:template>

<xsl:template name="mail_body">

<xsl:comment>

www.FRESHDIRECT.com
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Dear FreshDirect Customer,

As you know, we at FreshDirect strive to deliver each and every delivery on time every day.  However, occasionally we are subject to unforeseen circumstances causing a delay in deliveries.  This morning we encountered such a delay, which was caused by a system outage resulting in unfortunate delays in some deliveries. 

We are adding extra delivery staff to assist in minimizing delays with today's deliveries.  However, your delivery is expected to be delayed up to one hour.  We are hopeful that your delivery delay does not cause too much inconvenience for you.

If for any reason you are not able to accept your delivery today, please call us at 1-866-283-7374 (no e-mails, please) so we may arrange an alternate delivery time.

We at FreshDirect would like to apologize for the inconvenience and thank you for your cooperation and understanding.

Your FreshDirect Customer Service Team.
1-866-283-7374

======

QUICK LINKS:

Go to FreshDirect
http://www.freshdirect.com

Contact Us
http://www.freshdirect.com/help/contact_fd.jsp

Frequently Asked Questions
http://www.freshdirect.com/help/faq_home.jsp

~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
(c) 2002, 2003 FRESHDIRECT. All Rights Reserved.

</xsl:comment>

<table cellpadding="0" cellspacing="0">
<tr>
<!-- =============== START LEFT SPACER =============== -->
<td><img src="/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END LEFT SPACER ================= -->

<!-- ~~~~~~~~~~~~~~~ START CONTENT ~~~~~~~~~~~~~~~ -->
<td><xsl:call-template name="h_header_v1" />
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
		<td width="100%"><table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td bgcolor="#CCCCCC"><img src="/media_stat/images/layout/clear.gif" width="1" height="1" border="0" alt="" /></td></tr></table></td>
		</tr>
	</table><br/>
	<table cellpadding="0" cellspacing="0" width="90%">
                <tr>
                <td>
                <p>Dear FreshDirect Customer,</p>

<p>As you know, we at FreshDirect strive to deliver each and every delivery on time every day.  However, occasionally we are subject to unforeseen circumstances causing a delay in deliveries.  This morning we encountered such a delay, which was caused by a system outage resulting in unfortunate delays in some deliveries.</p> 

<p>We are adding extra delivery staff to assist in minimizing delays with today's deliveries.  However, your delivery is expected to be delayed up to one hour.  We are hopeful that your delivery delay does not cause too much inconvenience for you.</p>

<p>If for any reason you are not able to accept your delivery today, please call us at 1-866-283-7374 (no e-mails, please) so we may arrange an alternate delivery time.</p>

<p>We at FreshDirect would like to apologize for the inconvenience and thank you for your cooperation and understanding.</p>

<p>Your FreshDirect Customer Service Team.<br/>
1-866-283-7374</p>

<p><xsl:call-template name="h_footer_v2"/></p>
                </td>
                </tr>
</table>
</td>
<!-- ~~~~~~~~~~~~~~~ END CONTENT ~~~~~~~~~~~~~~~ -->

<!-- =============== BEGIN RIGHT SPACER =============== -->
<td><img src="/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END RIGHT SPACER ================= -->
</tr>
</table>

</xsl:template>

</xsl:stylesheet>
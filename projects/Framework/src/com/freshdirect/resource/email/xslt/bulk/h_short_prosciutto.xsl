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
		<title>Fiorucci Prosciutto di Parma</title>
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

Dear FreshDirect Customer:

Thank you for your order with FreshDirect.

At FreshDirect we are very conscious of the quality of the products we receive in our facility from our partners around the world.  We find that today's shipment of Fiorucci Prosciutto di Parma was unacceptable and did not meet our fresh standards.  We are not comfortable selling the Prosciutto di Parma which we would not give to our own families.  We apologize for this inconvenience but we will only sell products we feel to be of the freshest quality.  You may, however, order our new shipment arriving this week.

Our records indicate that the Quantity Delivered of this item is zero (0) and the Final Price charged to you is $0. You can confirm this by logging into your account ( https://www.freshdirect.com/your_account/manage_account.jsp ) on our website and reviewing the details of this order.

If you have any other questions, please call us toll-free at 1-866 2UFRESH (1-866 283-7374). We're here Sunday-Friday 9am-midnight and Saturdays 9am-9pm. Or, you may email us 24 hours a day at service@freshdirect.com.
 

Sincerely,
 
FreshDirect Customer Service
www.freshdirect.com

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
                <p>Dear FreshDirect Customer:</p>

                <p>Thank you for your order with FreshDirect.</p>
                
                <p>At FreshDirect we are very conscious of the quality of the products we receive in our facility from our partners around the world.  We find that today's shipment of Fiorucci Prosciutto di Parma was unacceptable and did not meet our fresh standards.  We are not comfortable selling the Prosciutto di Parma which we would not give to our own families.  We apologize for this inconvenience but we will only sell products we feel to be of the freshest quality.  You may, however, order our new shipment arriving this week.</p>
                
                <p>Our records indicate that the Quantity Delivered of this item is zero (0) and the Final Price charged to you is $0. You can confirm this by logging into <a href="https://www.freshdirect.com/your_account/manage_account.jsp">your account</a> on our website and reviewing the details of this order.</p>
                
                <p>If you have any other questions, please call us toll-free at 1-866 2UFRESH (1-866 283-7374). We're here Sunday-Friday 9am-midnight and Saturdays 9am-9pm. Or, you may email us 24 hours a day at <a href="mailto:service@freshdirect.com">service@freshdirect.com</a>.</p>  

                <p>Sincerely,<br/>
                FreshDirect Customer Service<br/>
                <a href="http://www.freshdirect.com">www.freshdirect.com</a></p> 
                </td>
                </tr>
		<tr>
		<td>
			<br/><p><xsl:call-template name="h_footer_v2"/></p>
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
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

<html lang="en-US" xml:lang="en-US">
	<head>
		<title>Your March 24th Order</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
	</head>
<body bgcolor="#FFFFFF" text="#333333">
	<xsl:call-template name="mail_body" />
</body>
</html>

</xsl:template>

<xsl:template name="mail_body">

<table cellpadding="0" cellspacing="0">
<tr>
<!-- =============== START LEFT SPACER =============== -->
<td><img src="http://www.freshdirect.com/images/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END LEFT SPACER ================= -->

<!-- ~~~~~~~~~~~~~~~ START CONTENT ~~~~~~~~~~~~~~~ -->
<td><xsl:call-template name="h_header_v1" />
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
		<td width="100%"><table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td bgcolor="#CCCCCC"><img src="http://www.freshdirect.com/images/clear.gif" width="1" height="1" border="0" alt="" /></td></tr></table></td>
		</tr>
	</table><br/>
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
		<td>
                <p>Dear Valued Customer:</p>
        
                <p>Thank you for your recent order with FreshDirect. </p>
                
                <p>We observed you placed an order scheduled for delivery on Monday, March 24th.  Earlier, we experienced some minor technical problems, which resulted in the unavailability of certain time periods for Thursday, March 20th and Friday, March 21st.  This has been resolved.  If you would like to attempt to reschedule delivery for an earlier date you may follow the instructions below.  Please note this is subject to time slot availability.</p>
                
                <p>Log into <a href="https://www.freshdirect.com/your_account/manage_account.jsp">your account</a> from the main screen, select the link, "Your Account", then select "Your Orders".  On the next screen, you will see an orange button that reads, "Change this order".  You will be asked to click this button a second time on the next screen.  Proceed through the checkout process and select the delivery time slot desired.</p>
                
                <p>We appreciate your patience and understanding.  If you have any other questions, please call us toll free at 1-866-2UFRESH (1-866-283-7374).  We're here Sunday-Friday, 9am to midnight and Saturdays from 9am to 9pm.  Or, you may email us 24 hours a day at <a href="mailto:service@freshdirect.com">service@freshdirect.com</a></p>

                <p>
                Sincerely,<br/>
                FreshDirect<br/>
                Customer Service Group</p>
			
			<p><xsl:call-template name="h_footer_v2"/></p>
		</td>
	</tr>
</table>
</td>
<!-- ~~~~~~~~~~~~~~~ END CONTENT ~~~~~~~~~~~~~~~ -->

<!-- =============== BEGIN RIGHT SPACER =============== -->
<td><img src="http://www.freshdirect.com/images/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END RIGHT SPACER ================= -->
</tr>
</table>

</xsl:template>

</xsl:stylesheet>
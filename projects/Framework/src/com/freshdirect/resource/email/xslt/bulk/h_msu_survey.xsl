<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:doc="http://xsltsl.org/xsl/documentation/1.0"
	xmlns:str="http://xsltsl.org/string"
	extension-element-prefixes="doc str">
	<xsl:output method="html"/>
	<xsl:include href='h_header_v1.xsl'/>
    <xsl:include href='h_optout_footer.xsl'/>
	<xsl:include href='h_footer_v2.xsl'/>
	<xsl:include href='string.xsl' />
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">

<html>
	<head>
        <BASE href="http://www.freshdirect.com" /> 
		<title>How are we doing?</title>
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

Dear <xsl:value-of select="customer/firstName"/>,

Now that you've used FreshDirect for a while, I'd like your help in finding out how we're doing. Researchers from Michigan State University have developed a survey to gather your opinions about FreshDirect's products and service as well as online grocery shopping in general. The survey should take about 15 to 20 minutes.

In return for your participation, we're giving away fashionable, FREE, limited-edition FreshDirect baseball caps to every customer who submits the survey (one size fits all).* In order to be eligible to receive the hat, you must type the following ID number in the box provided on the survey:

    Your ID: <xsl:value-of select="customer/msu_id"/>
    
    Take the survey now!  ( http://globaledge.msu.edu/surveys/Freshdirect )

New York's response to FreshDirect has been tremendous and your feedback will help us ensure that as we grow we only improve. Whether you choose to fill out this survey or not, I'd like to thank you for being such a great customer.

Sincerely, 
Joe Fedele
CEO and Creator, FreshDirect

P.S. If you have comments but would rather not take the survey, send me an email at service@freshdirect.com (sorry, no hat for that...)


Privacy Note: Any information that you provide is strictly confidential. Your privacy will be protected to the maximum extent allowable by law. Michigan State University researchers will not have access to any of your personal data.  FreshDirect will receive an aggregate summary of the survey results but no individual customers will be identified by name or ID number. 

* Hat will be given to all participants who enter a valid ID and complete the survey. Approximate retail value of hat is $12.99. Survey participants must be registered customers. One hat per household. Hat will be delivered within 6-10 weeks. Offer good for limited time only.

======
When you signed up with FreshDirect, you indicated an interest in receiving newsletters and updates. You may change this preference online in Your Account or REPLY via email and write UNSUBSCRIBE in the subject line.
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
                <p>Dear <xsl:value-of select="customer/firstName"/>,</p>

                <p>Now that you've used FreshDirect for a while, I'd like your help in finding out how we're doing. Researchers from Michigan State University have developed a survey to gather your opinions about FreshDirect's products and service as well as online grocery shopping in general. The survey should take about 15 to 20 minutes.</p>
                
                <p>In return for your participation, we're giving away fashionable, FREE, limited-edition FreshDirect baseball caps to every customer who submits the survey (one size fits all).* In order to be eligible to receive the hat, you must type the following ID number in the box provided on the survey:</p>
                
                <blockquote><b>Your ID: <xsl:value-of select="customer/msu_id"/></b><br/><br/>
                <a href="http://globaledge.msu.edu/surveys/Freshdirect"><b>Take the survey now!</b></a>  ( <a href="http://globaledge.msu.edu/surveys/Freshdirect">http://globaledge.msu.edu/surveys/Freshdirect</a> )
</blockquote>  
                
                <p>New York's response to FreshDirect has been tremendous and your feedback will help us ensure that as we grow we only improve. Whether you choose to fill out this survey or not, I'd like to thank you for being such a great customer.</p>
                
                <p>Sincerely,<br/>
                Joe Fedele<br/>
                CEO and Creator, FreshDirect</p>
                
                <p>P.S. If you have comments but would rather not take the survey, send me an email at <a href="mailto:service@freshdirect.com">service@freshdirect.com</a> (sorry, no hat for that...)</p>

                <p class="bodyCopySmall"><br/><b>Privacy Note:</b> Any information that you provide is strictly confidential. Your privacy will be protected to the maximum extent allowable by law. Michigan State University researchers will not have access to any of your personal data.  FreshDirect will receive an aggregate summary of the survey results but no individual customers will be identified by name or ID number.<br/><br/>
                * Hat will be given to all participants who enter a valid ID and complete the survey. Approximate retail value of hat is $12.99. Survey participants must be registered customers. One hat per household. Hat will be delivered within 6-10 weeks. Offer good for limited time only.</p>
                </td>
                </tr>
		<tr>
		<td>
			<br/><p><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/></p>
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
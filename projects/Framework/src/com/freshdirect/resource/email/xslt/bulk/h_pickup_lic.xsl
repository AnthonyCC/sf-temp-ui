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
		<title>Now available! Order pickup at FreshDirect.</title>
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

PICK UP YOUR ORDER!
Now everyone in the Tri-State area can get FreshDirect.

I am pleased to announce that now you don't have to live in an area where FreshDirect offers home delivery to get the best food at the best prices in New York!

Anyone in the Tri-State area (including existing home delivery customers) can place an order online for pickup at our facility, located just outside the Midtown Tunnel in Long Island City, Queens. Pickup service is available six days a week, there's no service charge, and your order will be loaded directly into your car curbside. For more information, click here ( http://www.freshdirect.com/pickup/index.jsp?trk=bqemail ).

Sincerely, 

Joe Fedele
President &amp; CEO

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

<table cellpadding="0" cellspacing="0" align="center">
<tr>
<!-- =============== START LEFT SPACER =============== -->
<td><img src="/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END LEFT SPACER ================= -->

<!-- ~~~~~~~~~~~~~~~ START CONTENT ~~~~~~~~~~~~~~~ -->
<td>
	<table cellpadding="0" cellspacing="0" width="612">
        <tr><td colspan="3" class="header"><img src="/media_stat/images/template/email/pickup/lic_hdr.gif" width="612" height="135" alt="PICK UP YOUR ORDER! Now everyone in the Tri-State area can get FreshDirect."/><br/><img src="/media_stat/images/layout/clear.gif" width="1" height="18" /></td></tr>
        <tr valign="top">
            <td width="397">
            <p>I am pleased to announce that now you don't have to live in an area where FreshDirect offers home delivery to <b>get the best food at the best prices in New York!</b></p>

            <p>Anyone in the Tri-State area (including existing home delivery customers) can place an order online for pickup at our facility, located just outside the Midtown Tunnel in Long Island City, Queens. Pickup service is available six days a week, there's no service charge, and your order will be loaded directly into your car curbside. <a href="http://www.freshdirect.com/pickup/index.jsp?trk=bqemail"><b>For more information, click here</b></a>.</p>

            <p>Sincerely,</p> 

            <p>Joe Fedele<br/>
            President &amp; CEO</p>
            </td>
            <td width="18"><img src="/media_stat/images/layout/clear.gif" width="18" height="1" /></td>
            <td width="197" align="center"><a href="http://www.freshdirect.com/pickup/index.jsp?trk=bqemail"><img src="/media_stat/images/template/email/pickup/lic_img.jpg" width="187" height="200" border="0" alt="Our Facility"/><br/>
            <img src="/media_stat/images/template/email/pickup/lic_button.gif" width="187" height="22" vspace="1" border="0" alt="Click here to get started"/></a>
            </td>
            </tr>
    		<tr>
    		<td colspan="3"><br/>
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
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
		<title>FreshDirect Delivery Suspension</title>
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
				<p>July 23, 2003</p>

				<p>Dear North Shore - LIJ Health System Employee,</p>
				
				<p>It is with great regret that FreshDirect announces the indefinite suspension of our fresh food delivery service to North Shore - LIJ.  As of Wednesday, July 23rd, 2003 all service is indefinitely discontinued and we will not be accepting new orders moving forward, with the exception of any orders already in our system for delivery Friday, July 25th, 2003.</p>
				
				<p>The suspension of the service is due to low participation rates amongst North Shore - LIJ employees.  While our regular North Shore - LIJ customers rave about FreshDirect's fresh food, quality and convenience, we can no longer justify our high delivery costs to your facilities.</p>
				
				<p>Currently, FreshDirect and North Shore - LIJ's Human Resources Department are assessing the overall program.  To help us determine how we could have serviced you better we would greatly appreciate any feedback you would like to send to us.  It is your invaluable feedback that will allow us to hopefully service you again in the future.  Please send all of your comments, positive or negative to us at: <a href="mailto:nslijservice@freshdirect.com">nslijservice@freshdirect.com</a>.</p>
				
				<p>For those of you who cannot live without FreshDirect or who still want to try us, we have opened a FreshDirect pick up window at our facility.  Anyone in the Tri-State area can now place an order online for pickup at FreshDirect located just outside the Midtown Tunnel in Long Island City, Queens.  For all details please go to: <a href="http://www.freshdirect.com/help/delivery_lic_pickup.jsp">http://www.freshdirect.com/help/delivery_lic_pickup.jsp</a>.</p>
				
				<p>We thoroughly enjoyed working with North Shore - LIJ and hope to do so again in the future.</p>
				
				<p>Sincerely,</p>
				
				<p>FreshDirect</p>
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
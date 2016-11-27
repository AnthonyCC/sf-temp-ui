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
		<title>FreshDirect Depot Delivery</title>
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
<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>

<td><xsl:call-template name="h_header_v1" />
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
			<td width="100%"><table width="100%" cellpadding="0" cellspacingG="0" border="0"><tr><td bgcolor="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1" border="0" alt="" /></td></tr></table></td>
		</tr>
	</table><br/>
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
		<td>
			<p>Dear Symbol Technologies Corporate Depot Customer,</p>
			
			<p>For some time now, Freshdirect has been delivering fresh food and groceries to Long Island corporate locations. Early feedback was that everyone loved shopping online and having their groceries delivered to the trunk of their car. Not only was it a time-saver, it was a great new way to receive better quality foods, at better prices, to bring home to your family.</p>

			<p>Recent customer satisfaction surveys show that everyone still loves the quality of food, everyday low prices, convenience and overall lifestyle benefit provided by having extra time to do other things. Unfortunately, the survey results also address the overriding concern about the limited delivery days and time slots. We understand your view and have come to agree that providing a limited service is no substitute for a full service. It makes it difficult for you, as well as us, to plan accordingly.</p>
			
			<p>In the near future, we do plan to expand our service to include home deliveries to Long Island. We will provide delivery schedules that address the daily needs and lifestyles of the consumer market at large.</p>
			
			<p>So now, after much discussion and thought, we regret to inform you, effective week ending <b>January 16, 2004</b> we will no longer be delivering to your location. We truly want to thank you for allowing us to serve you in your place of work. And to those of you who utilized Freshdirect on a regular basis we sincerely apologize. We would appreciate the opportunity of serving you again.</p>
			
			<p>Customers who have accounts with FreshDirect can still log in to view their <a href="https://www.freshdirect.com/your_account/manage_account.jsp"><b>account details</b></a>.</p>
			
			<p>Anyone in the Tri-State area (including existing depot and home delivery customers) can place an order online for pickup at our facility, located just outside the Midtown Tunnel in Long Island City, Queens.</p>
			
			<p><a href="http://www.freshdirect.com"><b>Click here to start shopping.</b></a></p>
			
			<p>FreshDirect</p>
			<p><xsl:call-template name="h_footer_v2"/></p>
		</td>
	</tr>
</table>
</td>

<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
</tr>
</table>

</xsl:template>

</xsl:stylesheet>
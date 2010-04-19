<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' xmlns:markup='http://xsltsl.org/markup'>
	<xsl:output method="html"/>
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_common_functions_v2.xsl'/>
	<xsl:include href='h_optout_footer.xsl'/>
	<xsl:include href='h_footer_v2.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">

<html>
	<head>
		<title>A problem with your standing order for <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="standingOrder/nextDeliveryDate" /></xsl:call-template></title>
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
<td><img src="/images/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END LEFT SPACER ================= -->

<!-- ~~~~~~~~~~~~~~~ START CONTENT ~~~~~~~~~~~~~~~ -->
<td><xsl:call-template name="h_header_v1" />
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
			<td WIDTH="100%"><table WIDTH="100%" CELLPADDING="0" CELLSPACING="0" BORDER="0"><tr><td BGCOLOR="#CCCCCC"><img src="/images/clear.gif" width="1" height="1" border="0" alt="" /></td></tr></table></td>
		</tr>
	</table><br/>
	<table cellpadding="0" cellspacing="0" width="90%">
     <tr>
          <td>
               <p>Dear <xsl:value-of select="customer/firstName"/>,</p>
               
               <p>We were unable to process your <a href="http://www.freshdirect.com/quickshop/so_details.jsp?ccListId={standingOrder/customerListId}">standing order</a> when we tried to schedule a delivery between
				<xsl:call-template name="format-delivery-start"><xsl:with-param name="dateTime" select="standingOrder/startTime"/></xsl:call-template>
				and <xsl:call-template name="format-delivery-end"><xsl:with-param name="dateTime" select="standingOrder/endTime"/></xsl:call-template>
				on <xsl:call-template name="format-delivery-date"><xsl:with-param name="dateTime" select="standingOrder/nextDeliveryDate" /></xsl:call-template>.
               </p>
               
               <p><b style="color: #990000;"><xsl:value-of select="standingOrder/errorHeader"/></b><markup:cdata-section text=" "/><xsl:value-of select="standingOrder/errorDetail"/></p>
               
               <p><a href="http://www.freshdirect.com/quickshop/so_details.jsp?ccListId={standingOrder/customerListId}">Click here to change the schedule or options for all future deliveries.</a></p>
               
               
               <p>Sincerely<br/>
               <br/>
               Your Customer Service Team at <a href="http://www.freshdirect.com/">FreshDirect</a></p>
               
               <p><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/></p>
		</td>
	</tr>
</table>
</td>
<!-- ~~~~~~~~~~~~~~~ END CONTENT ~~~~~~~~~~~~~~~ -->

<!-- =============== BEGIN RIGHT SPACER =============== -->
<td><img src="/images/clear.gif" width="20" height="1" border="0" /></td>
<!-- =============== END RIGHT SPACER ================= -->
</tr>
</table>

</xsl:template>

</xsl:stylesheet>

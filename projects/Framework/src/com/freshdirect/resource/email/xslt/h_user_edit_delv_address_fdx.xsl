<?xml version="1.0"?>
<!DOCTYPE xsl:stylesheet [
    <!ENTITY nbsp "&#160;">
    <!ENTITY dot  "&#183;">
    <!ENTITY mdash  "&#8212;">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_common_functions_v2.xsl'/>
	<xsl:include href='h_header_fdx.xsl'/>
	<xsl:include href='h_footer_fdx.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<html>
<head>
	<title>Delivery Address Change/Update</title>
	<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
</head>
<body bgcolor="#eeeeee" text="#333333">
	<xsl:call-template name="mail_body" />
</body>
</html>
</xsl:template>
	
<xsl:template name="mail_body">
	
	<div style="background-color: #ffffff; border: 1px solid #bbbbbb; margin: 5px; padding: 20px;">
		<div style="margin: 0 0 15px 0;"><xsl:call-template name="h_header_fdx" /></div>
		
		<div style="color: #732484; font-size: 36px; font-weight: bold; margin: 15px 0;">Hey <xsl:value-of select="customer/firstName"/>,</div>
		
		<div style="margin: 15px 0; font-size: 16px;">
			<span style="font-weight: bold;">#Success!</span> You just updated a delivery address for your account:<br />
			<br />
			<div style="margin-left: 20px;">
				<xsl:value-of select="erpAddressModel/firstName"/><xsl:text> </xsl:text>
				<xsl:value-of select="erpAddressModel/lastName"/>
				<xsl:if test="erpAddressModel/companyName != ''">
					<br/><xsl:value-of select="erpAddressModel/companyName"/><br/>
				</xsl:if>
				<xsl:if test="erpAddressModel/companyName = ''">
					<br/>
				</xsl:if>
				<xsl:value-of select="erpAddressModel/address1"/>,
				<xsl:if test="erpAddressModel/serviceType/name = 'CORPORATE'">
					Floor/Suite
				</xsl:if>
				<xsl:if test="erpAddressModel/serviceType/name != 'CORPORATE'">
					Apt.
				</xsl:if>
				<xsl:value-of select="erpAddressModel/apartment"/> 
				
				<xsl:if test="erpAddressModel/address2 != ''">
					<br/><xsl:value-of select="erpAddressModel/address2"/><br/>
				</xsl:if>
				<xsl:if test="erpAddressModel/address2 = ''">
					<br/>
				</xsl:if>   							  
				<xsl:value-of select="erpAddressModel/city"/>,
				<xsl:value-of select="erpAddressModel/state"/><xsl:text> </xsl:text>
				<xsl:value-of select="erpAddressModel/zipCode"/><br/><br/>
				
				<xsl:value-of select="phone"/><br/>
				<xsl:value-of select="alternatePhone"/>
			</div>
			<br />
			If you didn't make this change or have any questions, open up FoodKick and hit 'Me' to check your settings. Tap 'Help -> Contact Us' to chat with one of our SideKicks so we can clear things up. <br />
			<br />
			Happy Shopping,<br />
			Your FoodKick SideKicks
		</div>
	</div>
	
	<xsl:call-template name="h_footer_fdx"/>
</xsl:template>
</xsl:stylesheet>
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
<xsl:if test="preview = 'false'"> 
<html>
	<head>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
	</head>
<body bgcolor="#ffffff">
	<xsl:call-template name="mail_body" />
</body>
</html>
</xsl:if>

<xsl:if test="preview = 'true'">
	<xsl:call-template name="mail_body" />
</xsl:if>

</xsl:template>

<xsl:template name="mail_body">

<xsl:call-template name="h_header_v1"><xsl:with-param name="preview" select="preview" /></xsl:call-template>
	<table cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr>
			<td bgcolor="#cccccc"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1" /></td>
		</tr>
	</table>
	<table cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr>
			<td width="75%" class="text12" valign="top">
				Dear <xsl:value-of select="mailInfo/friendName" />,
				<br /><br/>
				<xsl:value-of select="mailInfo/emailText" />
				<br /><br/>
				- <xsl:value-of select="mailInfo/customerFirstName" />
				<br /><br/>
				<br /><br/>
				<b>FreshDirect is the better way to shop for food. Order online and save both time and money. We deliver right to your home, right when you want it. Our food is fresher. It&apos;s less expensive than local grocery stores. And it&apos;s made to order just for you.</b>
				<br /><br/>
				Your friend <xsl:value-of select="mailInfo/customerFirstName" /> has referred you as a friend who might like FreshDirect.  For 
				a limited time, we&apos;ll give you 10% off your first FreshDirect order. To redeem this offer, use the link below to visit our site and
				enter promotion code <b>FRIEND</b> at checkout. *Please note that other offers may take precedence in new delivery areas.
				<br></br>When you sign up as a new customer and place three orders within 6 months of signing up, your friend will get $20 off. 
				<b>This offer is valid for referrals of new customers only.</b> **So, if you don&apos;t already have a FreshDirect account, 
				click on the link below to find out if we&apos;re in your area. 
				Please note that you must use the sign up link below in order for <xsl:value-of select="mailInfo/customerFirstName" /> to be eligible.
				<br /><br/>
					<a target="freshdirect">
						<xsl:attribute name="href">
					<xsl:value-of select="concat('http://', mailInfo/server, '/index.jsp?ref_prog_id=', mailInfo/referralProgramId, '&amp;ref_trk_dtls=', mailInfo/referralId)"/>
						</xsl:attribute>
					<b>Click here to visit FreshDirect as <xsl:value-of select="mailInfo/customerFirstName" />'s friend</b>
					</a>
				<br /><br/>
			</td>
			<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="6" height="1" /></td>
			<td valign="center" align="right" width="25%"><img border="0" alt="10 Percent off" name="10percent_img" src="http://www.freshdirect.com/media_stat/images/template/tell_a_friend/email_friend_invite_img.jpg"/></td>
		</tr>
		<tr></tr>
		<tr>
			<td colspan="2"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8" /></td>
		</tr>
	</table>	
	<table cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr></tr>
		<tr>
			<td bgcolor="#cccccc"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1" /></td>
		</tr>
		<tr>
			<td width="75%" class="noteFooter" valign="top">
			* Web orders only.  May not be combined with any other promotional offer.  One per household.  Offer available for a limited time.  Other restrictions may apply.
			<br></br><br></br>** This offer is valid for the referral of new FreshDirect customers only. When a FreshDirect customer refers a friend, FreshDirect automatically sends an email on their behalf. You will need to sign up as a new customer using the link in this email to activate the Refer-A-Friend offer. Only customers referred in this manner will activate our referral promotion. After you place three orders (within 6 months of signing up) the friend who referred you will be given a promotion code for $20 off any single order. This offer may not be combined with any other offer and is non-transferable. 
				<br /><br/>
			This program is intended solely for use by FreshDirect customers to refer family and friends. FreshDirect does not support spamming, and misuse of this program will be considered unacceptable use according to our Customer Agreement. If you believe this program is being abused, please contact us immediately at service@freshdirect.com.
			</td>
		</tr>
	</table>
	
	<xsl:call-template name="h_footer_v2"><xsl:with-param name="preview" select="preview" /></xsl:call-template>

</xsl:template>

</xsl:stylesheet>

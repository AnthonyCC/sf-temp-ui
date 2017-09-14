<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' xmlns:v="urn:schemas-microsoft-com:vml" version="1.0">
	<xsl:import href='stdlib.xsl'/>
	<xsl:include href='h_invoice_info_v1.xsl'/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_common_functions_v2.xsl'/>
	<xsl:include href='h_footer_v1.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<html xmlns:v="urn:schemas-microsoft-com:vml">
<head>
	<title>Refer A Friend Invitation</title>
	<link rel="stylesheet" href="https://www.freshdirect.com/media/images/profile/raf_email/emails.css"/>
	<style type="text/css">
		v\:* { behavior: url(#default#VML); display:inline-block}
		table{border-collapse:collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;}
	</style>
</head>
<body style="padding-top: 0px; margin-top: 0px;">
	<table align="center" border="0" width="640" cellpadding="0" cellspacing="0">
		<tr>
			<td colspan="3" valign="middle" align="center" style="background-image: url('https://www.freshdirect.com/media/images/profile/raf_email/content_header.png'); background-color: #2f6606;">
				<img style="display: block" alt="" src="https://www.freshdirect.com/media/images/profile/raf_email/transparent_10x10.png" border="0" height="10" width="8" /><br />
				<span style="color: #fff; text-align: center; font-size: 18px; font-family: 'Helvetica Neue', 'Segoe UI', Helvetica, Arial, 'Lucida Grande', sans-serif; display: block;"><xsl:value-of select="name"/> has invited you to try FreshDirect!</span>
			</td>
		</tr>
		<tr>
			<td colspan="3">
				<img src="https://www.freshdirect.com/media/images/profile/raf_email/content_header_2.png" width="640" height="170" alt="" style="display: block;" />
			</td>
		</tr>
		<tr>
			<td>
				<img src="https://www.freshdirect.com/media/images/profile/raf_email/content_middle_1.png" width="24" height="482" alt="" style="display: block;" />
			</td>
			<td background="https://www.freshdirect.com/media/images/profile/raf_email/content_middle_2.png" width="327" valign="top">
			<!--[if gte mso 9]>
			  <v:rect xmlns:v="urn:schemas-microsoft-com:vml" fill="true" stroke="false" style="width:327px;">
			    <v:fill type="tile" src="https://www.freshdirect.com/media/images/profile/raf_email/content_middle_2.png" color="" />
			    <v:textbox style="mso-fit-shape-to-text:true" inset="0,0,0,0">
			  <![endif]-->
			  <div>
				<xsl:comment>[if gte vml 1]<![CDATA[>]]>
					&lt;v:shape stroked="f" style= "float: left; position: absolute; z-index:-2; visibility: visible; width: 327px; height: 482px; top: 0; left: 0px; border: 0; align: center;"&gt;
						&lt;v:imagedata src="https://www.freshdirect.com/media/images/profile/raf_email/content_middle_2.png"/&gt;&lt;/v:imagedata&gt;
					&lt;/v:shape&gt;
				<![CDATA[<![endif]]]></xsl:comment>
				
				<table cellpadding="0" cellspacing="0">
					<tr><td><img src="https://www.freshdirect.com/media/images/profile/raf_email/transparent_10x10.png" width="300" height="10" alt="" style="display: block;" /></td></tr>
					<tr><td height="22px"></td></tr>					
					<tr><td style="padding: 0 10px 0 0; font-family: Verdana, Arial, sans-serif;" ><xsl:value-of select="offerText" disable-output-escaping="yes"/></td></tr>
					<tr><td style="padding: 20px 10px 10px 10px; font-family: Verdana, Arial, sans-serif; font-size: 12px; word-break: break-word; word-wrap: break-word;"><img src="https://www.freshdirect.com/media/images/profile/raf_email/quote.png" alt="“" /> <xsl:value-of select="userMessage" disable-output-escaping="yes"/><span style="font-weight:bold;color: #E58816;font-family: Cooper Black, Times New Roman, Sans-serif;"> ”</span></td></tr>
					<tr><td style="padding: 20px 10px 10px 10px; font-family: Verdana, Arial, sans-serif; font-size: 12px; word-break: break-word; word-wrap: break-word;"><xsl:value-of select="systemMessage" disable-output-escaping="yes"/></td></tr>
					<tr><td style="padding: 20px 10px 10px 10px;"><a href="{refLink}"><img src="https://www.freshdirect.com/media/images/profile/raf_email/get_started.png" alt="" /></a></td></tr>
				</table>
			  </div>
			  <!--[if gte mso 9]>
			    </v:textbox>
			  </v:rect>
			  <![endif]-->
			</td>
			<td>
				<img src="https://www.freshdirect.com/media/images/profile/raf_email/content_middle_3.png" width="289" height="482" alt="" style="display: block;" />
			</td>
		</tr>
		<tr>
			<td colspan="3"><img src="https://www.freshdirect.com/media/images/profile/raf_email/content_footer.jpg" width="640" height="67" alt="FreshDirect" style="display: block;" /></td>
		</tr>
	</table>

	
	<table align="center" bgcolor="#ffffff" border="0" width="640" cellpadding="0" cellspacing="0">
	<tbody>
	<tr>
	<td><img style="DISPLAY: block" alt="" src="https://www.freshdirect.com/media/images/profile/raf_email/transparent_10x10.png" border="0" height="19" width="8" /></td></tr></tbody></table>

	<table align="center" border="0" width="640" cellpadding="0" cellspacing="0">
	<tbody>
	<tr>
	<td><a href="http://www.freshdirect.com/index.jsp?serviceType=HOME"><img style="DISPLAY: block" alt="Go to FreshDirect" src="https://www.freshdirect.com/media/images/profile/raf_email/ftr_nav_go_to_freshdirect.gif" usemap="#Map" border="0" height="34" width="150" /></a></td>
	<td><a href="http://www.freshdirect.com/category.jsp?catId=picks_love"><img style="DISPLAY: block" alt="President's Picks" src="https://www.freshdirect.com/media/images/profile/raf_email/ftr_nav_presidents_picks.gif" usemap="#Map" border="0" height="34" width="130" /></a></td>
	<td><a href="https://www.freshdirect.com/newproducts.jsp"><img style="DISPLAY: block" alt="New Products" src="https://www.freshdirect.com/media/images/profile/raf_email/ftr_nav_new_products.gif" usemap="#Map" border="0" height="34" width="116" /></a></td>
	<td><a href="https://www.freshdirect.com/your_account/manage_account.jsp"><img style="DISPLAY: block" alt="Your Account" src="https://www.freshdirect.com/media/images/profile/raf_email/ftr_nav_your-account.gif" usemap="#Map" border="0" height="34" width="116" /></a></td>
	<td><a href="https://www.freshdirect.com/your_account/brownie_points.jsp"><img style="DISPLAY: block" alt="Refer a Friend" src="https://www.freshdirect.com/media/images/profile/raf_email/ftr_nav_refer_a_friend.gif" usemap="#Map" border="0" height="34" width="128" /></a></td></tr></tbody></table>

	<table align="center" bgcolor="#ffffff" border="0" width="640" cellpadding="0" cellspacing="0">
	<tbody>
	<tr>
	<td><img style="DISPLAY: block" alt="" src="https://www.freshdirect.com/media/images/profile/raf_email/transparent_10x10.png" border="0" height="19" width="8" /></td></tr></tbody></table>
	<table align="center" border="0" width="640" cellpadding="0" cellspacing="0">
	<tbody>
	<tr>
	<td style="FONT-FAMILY: 'Century Gothic', Verdana, Arial, Helvetica, sans-serif; COLOR: #999; FONT-SIZE: 14px" align="center" valign="middle">Connect with FreshDirect</td>
	<td style="FONT-FAMILY: 'Century Gothic', Verdana, Arial, Helvetica, sans-serif; COLOR: #999; FONT-SIZE: 14px" align="center" valign="middle">Get the App for Your Appetite</td></tr>
	<tr>
	<td align="center" valign="middle"><img style="DISPLAY: block" alt="" src="https://www.freshdirect.com/media/images/profile/raf_email/transparent_10x10.png" border="0" height="15" width="310" /></td>
	<td align="center" valign="middle"><img style="DISPLAY: block" alt="" src="https://www.freshdirect.com/media/images/profile/raf_email/transparent_10x10.png" border="0" height="15" width="330" /></td></tr></tbody></table>
	<table align="center" border="0" width="640" cellpadding="0" cellspacing="0">
	<tbody>
	<tr>
	<td width="88"><img style="DISPLAY: block" src="https://www.freshdirect.com/media/images/profile/raf_email/ftr_left_before_fb.gif" border="0" height="27" width="88" /></td>
	<td width="27"><a href="http://www.facebook.com/freshdirect"><img style="DISPLAY: block" alt="Facebook" src="https://www.freshdirect.com/media/images/profile/raf_email/ftr_facebook.gif" border="0" height="27" width="27" /></a></td>
	<td width="11"><img style="DISPLAY: block" src="https://www.freshdirect.com/media/images/profile/raf_email/ftr_right_after_facebook.gif" border="0" height="27" width="11" /></td>
	<td width="27"><a href="http://www.twitter.com/freshdirect"><img style="DISPLAY: block" alt="Twitter" src="https://www.freshdirect.com/media/images/profile/raf_email/ftr_twitter.gif" border="0" height="27" width="27" /></a></td>
	<td width="10"><img style="DISPLAY: block" src="https://www.freshdirect.com/media/images/profile/raf_email/ftr_right_after_twitter.gif" border="0" height="27" width="10" /></td>
	<td width="27"><a href="http://www.pinterest.com/freshdirect/"><img style="DISPLAY: block" alt="Pinterest" src="https://www.freshdirect.com/media/images/profile/raf_email/ftr_pinterest.gif" border="0" height="27" width="27" /></a></td>
	<td width="12"><img style="DISPLAY: block" src="https://www.freshdirect.com/media/images/profile/raf_email/ftr_right_after_pinterest.gif" border="0" height="27" width="12" /></td>
	<td width="26"><a href="http://instagram.com/freshdirect"><img style="DISPLAY: block" alt="Intstagram" src="https://www.freshdirect.com/media/images/profile/raf_email/ftr_instagram.gif" border="0" height="27" width="26" /></a></td>
	<td width="167"><img style="DISPLAY: block" src="https://www.freshdirect.com/media/images/profile/raf_email/ftr_right_after_instagram.gif" border="0" height="27" width="167" /></td>
	<td width="75"><a href="http://itunes.apple.com/us/app/freshdirect/id346631494"><img style="DISPLAY: block" alt="Available on the App Store" src="https://www.freshdirect.com/media/images/profile/raf_email/ftr_apple_app.gif" border="0" height="27" width="75" /></a></td>
	<td width="12"><img style="DISPLAY: block" src="https://www.freshdirect.com/media/images/profile/raf_email/ftr_right_after_instagram.gif" border="0" height="27" width="12" /></td>
	<td width="76"><a href="https://play.google.com/store/apps/details?id=com.freshdirect.android"><img style="DISPLAY: block" alt="Android app on Google Play Store" src="https://www.freshdirect.com/media/images/profile/raf_email/ftr_google_play.gif" border="0" height="27" width="76" /></a></td>
	<td width="82"><img style="DISPLAY: block" src="https://www.freshdirect.com/media/images/profile/raf_email/ftr_right_after_google_play.gif" border="0" height="27" width="82" /></td></tr></tbody></table>
	<table align="center" bgcolor="#ffffff" border="0" width="640" cellpadding="0" cellspacing="0">
	  <tbody>
	<tr>
	<td height="11"><img style="DISPLAY: block" alt="" src="https://www.freshdirect.com/media/images/profile/raf_email/transparent_10x10.png" border="0" height="30" width="8" /></td></tr></tbody></table>
	<table align="center" bgcolor="#ffffff" border="0" width="640" cellpadding="0" cellspacing="0">
	<tbody>
	<tr>
	<td style="LINE-HEIGHT: 14px; FONT-FAMILY: Verdana,Arial,Helvetica,sans-serif; COLOR: #999; FONT-SIZE: 10px" align="left">Please add announcements@.freshdirect.com to your address book to ensure our emails reach your inbox.<br /><br />For FreshDirect online Help or to contact us, please <a style="COLOR: rgb(51,102,0)" href="http://www.freshdirect.com/help/contact_fd.jsp"><span style="COLOR: rgb(51,102,0); FONT-WEIGHT: normal; TEXT-DECORATION: underline">click here</span></a>.<br /><br />23-30 Borden Ave. Long Island City, NY 11101<br /><br /><a style="COLOR: #360" href="https://www.freshdirect.com/help/terms_of_service.jsp"><span style="COLOR: rgb(51,102,0); FONT-WEIGHT: normal; TEXT-DECORATION: underline">Customer Agreement</span></a><br /><xsl:value-of select="substring(//curYear,1,4)" /> Fresh Direct, LLC. All Rights Reserved.</td></tr></tbody></table>

	<table align="center" bgcolor="#ffffff" border="0" width="640" cellpadding="0" cellspacing="0">
	<tbody>
	<tr>
	<td><img style="DISPLAY: block" alt="" src="https://www.freshdirect.com/media/images/profile/raf_email/transparent_10x10.png" border="0" height="19" width="8" /></td></tr></tbody></table>

	<table align="center" bgcolor="#ccc" border="0" width="640" cellpadding="0" cellspacing="0">
		<tbody>
			<tr><td><img alt="" src="https://www.freshdirect.com/media/images/profile/raf_email/line_640x1.png" border="0" height="1" width="640" /></td></tr>
			<tr><td><img alt="" src="https://www.freshdirect.com/media/images/profile/raf_email/transparent_10x10.png" border="0" height="10" /></td></tr>
		</tbody>
	</table>
	<table align="center" border="0" width="640" cellpadding="0" cellspacing="0">
		<tbody>
			<tr><td style="LINE-HEIGHT: 14px; FONT-FAMILY: Verdana,Arial,Helvetica,sans-serif; COLOR: #999; FONT-SIZE: 10px" align="left"><xsl:value-of select="legal"/></td></tr>
		</tbody>
	</table>

</body>
</html>
</xsl:template>

<xsl:template name="conditionalComment">
    <xsl:param name="qualifier" select="'gte vml 1'"/>
    <xsl:param name="contentRTF" select="''" />
    <xsl:comment>[if <xsl:value-of select="$qualifier"/>]<![CDATA[>]]>
        <xsl:copy-of select="$contentRTF" />
        <![CDATA[<![endif]]]></xsl:comment>
</xsl:template>

</xsl:stylesheet>
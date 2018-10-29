<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_footer_v1.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">

<html lang="en-US" xml:lang="en-US">
	<head>
	    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
	    <title>Welcome to FreshDirect</title>
	</head>
	<body>
		<xsl:call-template name="mail_body" />
	</body>
</html>

</xsl:template>

<xsl:template name="mail_body">
 <table style="font-family:Verdana, Arial, Helvetica, sans-serif; font-size:12px; color:#333333; line-height:normal" align="center" bgcolor="#FFFFFF" border="0" cellpadding="0" cellspacing="0" width="640">
        <tbody>
            <tr>
                <td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" alt="" height="15" border="0" width="1"/>
                </td>
            </tr>
            <tr>
                <td>
                    <table align="center" bgcolor="#ffffff" border="0" cellpadding="0" cellspacing="0" width="640">
                        <tbody>
                            <tr>
                                <td style="LINE-HEIGHT: 13px; FONT-FAMILY: Verdana, Arial, Helvetica, sans-serif; COLOR: rgb(153,153,153); FONT-SIZE: 10px; FONT-WEIGHT: normal" align="left" height="20" valign="top"><a style="color:#360; text-decoration:underline" href="https://www.freshdirect.com/index.jsp?serviceType=HOME">Start Exploring</a>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <table align="center" bgcolor="#ffffff" border="0" cellpadding="0" cellspacing="0" width="638">
                        <tbody>
                            <tr>
                                <td colspan="6" height="15"><img style="DISPLAY: block" alt="" src="http://www.freshdirect.com/media/images/email/confirm_signup/transparent_10x10.png" height="15" border="0" width="8"/>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <table align="center" bgcolor="#ffffff" border="0" cellpadding="0" cellspacing="0" width="655">
                        <tbody>
                            <tr>
                                <td align="left" valign="top" width="569">
                                    <a href="https://www.freshdirect.com/index.jsp?serviceType=HOME"><img style="DISPLAY: block" alt="FreshDirect" src="http://www.freshdirect.com/media/images/email/confirm_signup/freshdirect_logoLTG.gif" height="45" border="0" width="225"/>
                                    </a>
                                </td>
                                <td align="right" valign="bottom" width="77">
                                    <a href="https://www.freshdirect.com/index.jsp?serviceType=HOME"><img style="DISPLAY: block" alt="Shop Now" src="http://www.freshdirect.com/media/images/email/confirm_signup/shop_nowLTG.gif" height="32" border="0" width="77"/>
                                    </a>
                                </td>
                                <td align="right" valign="bottom" width="9"><img style="DISPLAY: block" alt="" src="http://www.freshdirect.com/media/images/email/confirm_signup/transparent_10x10.png" height="1" border="0" width="9"/>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <table align="center" bgcolor="#ffffff" border="0" cellpadding="0" cellspacing="0" width="638">
                        <tbody>
                            <tr>
                                <td align="right" valign="top"><img style="DISPLAY: block" alt="" src="http://www.freshdirect.com/media/images/email/confirm_signup/transparent_10x10.png" height="10" border="0" width="8"/>
                                </td>
                            </tr>
                        </tbody>
                    </table>

                    <table align="center" border="0" cellpadding="0" cellspacing="0" width="638">
                        <tbody>
                            <tr>
                                <td style="font-family:Verdana, Arial,
Helvetica, sans-serif; font-size:12px; color:#76973E; font-weight:bold;
letter-spacing:0px;" align="left" height="17"><a style="color:#76973E; text-decoration:none;" href="https://www.freshdirect.com/index.jsp?serviceType=HOME">&#160;&#160;SHOP
NOW</a>
                                </td>
                                <td style="font-family:Verdana, Arial,
Helvetica, sans-serif; font-size:11px; color:#76973E; font-weight:bold;
letter-spacing:0px" align="left">|</td>
                                <td style="font-family:Verdana, Arial, Helvetica,
sans-serif; font-size:12px; color:#76973E; font-weight:bold;
letter-spacing:0px;" align="center"><a style="color:#76973E; text-decoration:none;" href="https://www.freshdirect.com/browse.jsp?pageType=browse&amp;id=top_rated">&#160;&#160;TOP
RATED&#160;&#160;</a>
                                </td>
                                <td style="font-family:Verdana, Arial,
Helvetica, sans-serif; font-size:11px; color:#76973E; font-weight:bold;
letter-spacing:0px" align="left">|</td>
                                <td style="font-family:Verdana, Arial, Helvetica,
sans-serif; font-size:12px; color:#76973E; font-weight:bold;
letter-spacing:0px;" align="center"><a style="color:#76973E; text-decoration:none;" href="https://www.freshdirect.com/srch.jsp?pageType=pres_picks&amp;id=picks_love">FRESH DEALS</a>
                                </td>
                                <td style="font-family:Verdana, Arial,
Helvetica, sans-serif; font-size:11px; color:#76973E; font-weight:bold;
letter-spacing:0px" align="left">|</td>
                                <td style="font-family:Verdana, Arial, Helvetica,
sans-serif; font-size:12px; color:#76973E; font-weight:bold;
letter-spacing:0px;" align="center"><a style="color:#76973E; text-decoration:none;" href="https://www.freshdirect.com/browse.jsp?pageType=browse&amp;id=local">&#160;&#160;LOCAL&#160;&#160;</a>
                                </td>

                                <td style="font-family:Verdana, Arial, Helvetica,
sans-serif; font-size:11px; color:#76973E; font-weight:bold;
letter-spacing:0px" align="left">|</td>
                                <td style="font-family:Verdana, Arial, Helvetica,
sans-serif; font-size:12px; color:#76973E; font-weight:bold;
letter-spacing:0px;" align="right"><a style="color:#76973E; text-decoration:none;" href="https://www.freshdirect.com/browse.jsp?pageType=browse&amp;id=deals">DEALS&#160;&#160;</a>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                    <table align="center" bgcolor="#ffffff" border="0" cellpadding="0" cellspacing="0" width="638">
                        <tbody>
                            <tr>
                                <td align="right" valign="top"><img style="DISPLAY: block" alt="" src="http://www.freshdirect.com/media/images/email/confirm_signup/transparent_10x10.png" height="10" border="0" width="8"/>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
        </tbody>
    </table>

    <table align="center" border="0" cellpadding="0" cellspacing="0" width="640">
        <tbody>
            <tr>
                <td align="left" bgcolor="#d9d9d9" valign="top" width="1"><img style="DISPLAY: block" alt="" src="http://www.freshdirect.com/media/images/email/confirm_signup/transparent_10x10.png" height="1" border="0" width="1"/>
                </td>
                <td align="center" bgcolor="#ffffff" valign="top" width="638">
                    <a href="https://www.freshdirect.com/index.jsp?serviceType=HOME"><img src="http://www.freshdirect.com/media/images/email/confirm_signup/slices_01_Welcome_confirm.jpg" alt="Welcome! So Great to Meet You!" style="border: 0; display: block;" height="103" border="0" width="638"/>
                    </a>
                </td>
                <td align="right" bgcolor="#d9d9d9" valign="top" width="1"><img style="DISPLAY: block" alt="" src="http://www.freshdirect.com/media/images/email/confirm_signup/transparent_10x10.png" height="1" border="0" width="1"/>
                </td>
            </tr>
        </tbody>
    </table>
    <table align="center" background="http://www.freshdirect.com/media/images/email/confirm_signup/slices_02_Welcome_confirm.jpg" border="0" cellpadding="0" cellspacing="0" width="640">
        <tbody>
            <tr>
                <td align="left" bgcolor="#d9d9d9" valign="top" width="1"><img style="DISPLAY: block" alt="" src="http://www.freshdirect.com/media/images/email/confirm_signup/transparent_10x10.png" height="1" border="0" width="1"/>
                </td>
                <td align="center" bgcolor="#f5f5f5" valign="top" width="80"><img style="DISPLAY: block" alt="" src="http://www.freshdirect.com/media/images/email/confirm_signup/transparent_10x10.png" height="65" border="0" width="20"/>
                </td>
                <td style="font-family: Verdana, Arial, Lucida Grande, sans-serif; font-size:18px; font-weight:normal; color:#222" align="left" bgcolor="#f5f5f5" valign="middle" width="558">Hi <xsl:value-of select="customer/firstName"/>,</td>
                <td align="right" bgcolor="#d9d9d9" valign="top" width="1"><img style="DISPLAY: block" alt="" src="http://www.freshdirect.com/media/images/email/confirm_signup/transparent_10x10.png" height="1" border="0" width="1"/>
                </td>
            </tr>
        </tbody>
    </table>

    <table align="center" border="0" cellpadding="0" cellspacing="0" width="640">
        <tbody>
            <tr>
                <td align="left" bgcolor="#d9d9d9" valign="top" width="1"><img style="DISPLAY: block" alt="" src="http://www.freshdirect.com/media/images/email/confirm_signup/transparent_10x10.png" height="1" border="0" width="1"/>
                </td>
                <td align="center" bgcolor="#ffffff" valign="top" width="638">
                    <a href="https://www.freshdirect.com/index.jsp?serviceType=HOME"><img src="http://www.freshdirect.com/media/images/email/confirm_signup/slices_03_Welcome_confirm.jpg" alt="We've been hoping to catch your eye, and we can't wait to share our finest and freshest with you. Here are some of the great things you can expect from us: -From favorites to new flavors, food that simply tastes great- -Great ideas and quick fixes make it easy to plan ahead- -Stress-free shopping so you can just sit back and enjoy- So come and check us out! We have the goods from our good (and responsible) friends at local farms, dairies and fisheries, and all the best-loved brands. Order today and get ready for the freshest, highest-quality food at the best prices delivered straight to your door, and (we hope) straight into your heart. START EXPLORING We're all so happy you're here" style="border: 0; display: block;" height="503" border="0" width="638"/>
                    </a>
                </td>
                <td align="right" bgcolor="#d9d9d9" valign="top" width="1"><img style="DISPLAY: block" alt="" src="http://www.freshdirect.com/media/images/email/confirm_signup/transparent_10x10.png" height="1" border="0" width="1"/>
                </td>
            </tr>
        </tbody>
    </table>


    <table align="center" bgcolor="#ffffff" border="0" cellpadding="0" cellspacing="0" width="638">
        <tbody>
            <tr>
                <td height="11"><img style="DISPLAY: block" alt="" src="http://www.freshdirect.com/media/images/email/confirm_signup/transparent_10x10.png" height="30" border="0" width="8"/>
                </td>
            </tr>
        </tbody>
    </table>
    <table align="center" border="0" cellpadding="0" cellspacing="0" width="609">
        <tbody>
            <tr>
                <td align="right" width="120">
                    <a href="http://blog.freshdirect.com/"><img style="DISPLAY: block" alt="IDEAS" src="http://www.freshdirect.com/media/images/email/confirm_signup/New_bottom-nav_08_ideas.jpg" height="34" border="0" width="120"/>
                    </a>
                </td>
                <td align="right" width="156">
                    <a href="https://www.freshdirect.com/login/login.jsp?successPage=/your_account/brownie_points.jsp"><img style="DISPLAY: block" alt="REFER A FRIEND" src="http://www.freshdirect.com/media/images/email/confirm_signup/New_bottom-nav_09_raf.jpg" height="34" border="0" width="156"/>
                    </a>
                </td>
                <td align="right" width="165">
                    <a href="https://www.freshdirect.com/newproducts.jsp"><img style="DISPLAY: block" alt="NEW PRODUCTS" src="http://www.freshdirect.com/media/images/email/confirm_signup/New_bottom-nav_10_np.jpg" height="34" border="0" width="165"/>
                    </a>
                </td>
                <td align="right" width="168">
                    <a href="https://www.freshdirect.com/whatsgood.jsp?deptId=wgd"><img style="DISPLAY: block" alt="WHATS GOOD" src="http://www.freshdirect.com/media/images/email/confirm_signup/New_bottom-nav_11_wg.jpg" height="34" border="0" width="168"/>
                    </a>
                </td>
            </tr>
        </tbody>
    </table>
    <table align="center" bgcolor="#ffffff" border="0" cellpadding="0" cellspacing="0" width="638">
        <tbody>
            <tr>
                <td><img style="DISPLAY: block" alt="" src="http://www.freshdirect.com/media/images/email/confirm_signup/transparent_10x10.png" height="19" border="0" width="8"/>
                </td>
            </tr>
        </tbody>
    </table>
    <table align="center" border="0" cellpadding="0" cellspacing="0" width="638">
        <tbody>
            <tr>
                <td style="FONT-FAMILY: 'Century Gothic', Verdana, Arial, Helvetica, 
sans-serif; COLOR: #999; FONT-SIZE: 14px" align="center" valign="middle">Connect with FreshDirect</td>
                <td style="FONT-FAMILY: 'Century Gothic', Verdana, Arial, Helvetica, 
sans-serif; COLOR: #999; FONT-SIZE: 14px" align="center" valign="middle">Get the App for Your Appetite</td>
            </tr>
            <tr>
                <td align="center" valign="middle"><img style="DISPLAY: block" alt="" src="http://www.freshdirect.com/media/images/email/confirm_signup/transparent_10x10.png" height="15" border="0" width="310"/>
                </td>
                <td align="center" valign="middle"><img style="DISPLAY: block" alt="" src="http://www.freshdirect.com/media/images/email/confirm_signup/transparent_10x10.png" height="15" border="0" width="328"/>
                </td>
            </tr>
        </tbody>
    </table>
    <table align="center" border="0" cellpadding="0" cellspacing="0" width="638">
        <tbody>
            <tr>
                <td width="88"><img style="DISPLAY: block" src="http://www.freshdirect.com/media/images/email/confirm_signup/ftr_left_before_fb.gif" height="27" border="0" width="88"/>
                </td>
                <td width="27">
                    <a href="https://www.facebook.com/FreshDirect"><img style="DISPLAY: block" alt="Facebook" src="http://www.freshdirect.com/media/images/email/confirm_signup/ftr_facebook.gif" height="27" border="0" width="27"/>
                    </a>
                </td>
                <td width="11"><img style="DISPLAY: block" src="http://www.freshdirect.com/media/images/email/confirm_signup/ftr_right_after_facebook.gif" height="27" border="0" width="11"/>
                </td>
                <td width="27">
                    <a href="https://twitter.com/FreshDirect"><img style="DISPLAY: block" alt="Twitter" src="http://www.freshdirect.com/media/images/email/confirm_signup/ftr_twitter.gif" height="27" border="0" width="27"/>
                    </a>
                </td>
                <td width="10"><img style="DISPLAY: block" src="http://www.freshdirect.com/media/images/email/confirm_signup/ftr_right_after_twitter.gif" height="27" border="0" width="10"/>
                </td>
                <td width="27">
                    <a href="https://www.pinterest.com/freshdirect/"><img style="DISPLAY: block" alt="Pinterest" src="http://www.freshdirect.com/media/images/email/confirm_signup/ftr_pinterest.gif" height="27" border="0" width="27"/>
                    </a>
                </td>

                <td width="12"><img style="DISPLAY: block" src="http://www.freshdirect.com/media/images/email/confirm_signup/ftr_right_after_pinterest.gif" height="27" border="0" width="12"/>
                </td>
                <td width="26">
                    <a href="http://instagram.com/freshdirect"><img style="DISPLAY: block" alt="Intstagram" src="http://www.freshdirect.com/media/images/email/confirm_signup/ftr_instagram.gif" height="27" border="0" width="26"/>
                    </a>
                </td>
                <td width="167"><img style="DISPLAY: block" src="http://www.freshdirect.com/media/images/email/confirm_signup/ftr_right_after_instagram.gif" height="27" border="0" width="167"/>
                </td>
                <td width="75">
                    <a href="http://itunes.apple.com/us/app/freshdirect/id346631494"><img style="DISPLAY: block" alt="Available on the App Store" src="http://www.freshdirect.com/media/images/email/confirm_signup/ftr_apple_app.gif" height="27" border="0" width="75"/>
                    </a>
                </td>
                <td width="12"><img style="DISPLAY: block" src="http://www.freshdirect.com/media/images/email/confirm_signup/ftr_right_after_instagram.gif" height="27" border="0" width="12"/>
                </td>
                <td width="76">
                    <a href="https://play.google.com/store/apps/details?id=com.freshdirect.android"><img style="DISPLAY: block" alt="Android app on Google Play Store" src="http://www.freshdirect.com/media/images/email/confirm_signup/ftr_google_play.gif" height="27" border="0" width="76"/>
                    </a>
                </td>
                <td width="82"><img style="DISPLAY: block" src="http://www.freshdirect.com/media/images/email/confirm_signup/ftr_right_after_google_play.gif" height="27" border="0" width="82"/>
                </td>
            </tr>
        </tbody>
    </table>
    <table align="center" bgcolor="#ffffff" border="0" cellpadding="0" cellspacing="0" width="638">
        <tbody>
            <tr>
                <td height="11"><img style="DISPLAY: block" alt="" src="http://www.freshdirect.com/media/images/email/confirm_signup/transparent_10x10.png" height="30" border="0" width="8"/>
                </td>
            </tr>
        </tbody>
    </table>
    <table align="center" bgcolor="#ffffff" border="0" cellpadding="0" cellspacing="0" width="638">
        <tbody>
            <tr>
                <td style="LINE-HEIGHT: 14px; FONT-FAMILY: 
Verdana,Arial,Helvetica,sans-serif; COLOR: #999; FONT-SIZE: 10px" align="left">
                    <blockquote>
                        <p>If you prefer not to receive promotional email messages from us, please <a style="COLOR: rgb(51,102,0)" href="https://www.freshdirect.com/your_account/signin_information.jsp
"><span style="COLOR: rgb(51,102,0); FONT-WEIGHT: normal; TEXT-DECORATION: 
underline">click here</span></a>.
                            <br/>
                            <br/>For FreshDirect online Help or to contact us, please <a style="COLOR: rgb(51,102,0)" href="http://www.freshdirect.com/help/contact_fd.jsp"><span style="COLOR: rgb(51,102,0); FONT-WEIGHT: normal; TEXT-DECORATION: 
underline">click here</span></a>.
                            <br/>
                            <br/>2 St Ann's Ave Bronx, NY 10454
                            <br/>
                            <br/><a style="COLOR: #360" href="https://www.freshdirect.com/help/terms_of_service.jsp"><span style="COLOR: rgb(51,102,0); FONT-WEIGHT: normal; TEXT-DECORATION: 
underline">Customer Agreement</span></a>
                            <br/> ©2015 Fresh Direct, LLC. All Rights Reserved.</p>
                    </blockquote>
                </td>
            </tr>
        </tbody>
    </table>


    <img src="http://www.freshdirect.com/media/images/email/confirm_signup/spacer.gif"/>

</xsl:template>

</xsl:stylesheet>
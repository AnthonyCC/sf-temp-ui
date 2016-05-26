<?xml version="1.0"?>
<!DOCTYPE xsl:stylesheet [
    <!ENTITY nbsp "&#160;">
    <!ENTITY dot  "&#183;">
    <!ENTITY mdash  "&#8212;">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:include href='h_footer_fdx.2.xsl'/>
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>

	<!--<xsl:variable name="site" select="'https://www.freshdirect.com'" />-->
	<xsl:variable name="site" select="'https://foodkick.freshdirect.com'" />
	<xsl:variable name="editorial_dir" select="concat($site, '/media/editorial/foodkick')" />
	<xsl:variable name="img_dir" select="concat($site, '/media/images/email/foodkick')" />
	<xsl:variable name="purple" select="'#732484'" />
	<xsl:variable name="litegrey" select="'#f4f4f2'" />
	<xsl:variable name="white" select="'#FFFFFF'" />
	<xsl:variable name="border_color" select="'#D8D8D8'" />
	<xsl:variable name="fontsize_p" select="'1em'" />
	<xsl:variable name="layout_w" select="'452'" />
	<xsl:variable name="fontfamily" select="'font-family:Arial, Helvetica, sans-serif;'" />

	<xsl:variable name="list_img_tag"><img src="{$img_dir}/check.jpg" style="position:relative;top:5px; margin-right:3px;" /></xsl:variable>
	<xsl:variable name="list_style" select="concat( $fontfamily, 'margin-bottom:0px; padding-top: 3px; padding-left:0px; min-height:20px; background-position: 0px 1px; text-align:left;')" />
	<xsl:variable name="cell_style" select="concat( $fontfamily, 'text-align:left; white-space:nowrap;')" />
	
	<xsl:template match="fdemail">
	<html>
		<head>
			<title>Congrats! You're In!</title>
			<link rel="stylesheet" href="{$site}/assets/css/emails.css" />
		</head>
		<body bgcolor="{$litegrey}" text="" margin="0" style="background-color:{$litegrey}; margin:0px; color: #333333; {$fontfamily}">
			<xsl:call-template name="mail_body" />
		</body>
	</html>
	</xsl:template>

	<xsl:template name="mail_body">
		<table style="text-align:center; background-color: {$purple}; white-space:nowrap; min-height:24px; width:100%;">
			<tr>
				<td style="padding-right: 23px;">
					<table width="{$layout_w}" style="width:{$layout_w}px; background-color: {$purple}; {$fontfamily} color:{$white}; font-size:0.9em; margin:auto">
						<tr>
							<td align="left" style="background-color: {$purple}; text-align:left; {$fontfamily} white-space:nowrap; width:300px">Welcome to FoodKick! + 30 Days of Free Delivery</td>
							<td align="right" style="text-align:right; {$fontfamily} white-space:nowrap; width:152px">&nbsp;&nbsp;&nbsp;&nbsp;View <a href="http://www.foodkick.com" style="color: {$white};">Web</a></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		
		<div align="center" style="{$fontfamily} margin: 5px auto; padding: 0px 25px 0px 5px; text-align:center; box-sizing: border-box;">
			<div style="margin: 10px auto; font-size: {$fontsize_p}; text-align: center;">
				<table width="{$layout_w}" bgcolor="{$white}" align="center" style="border: 1px solid {$border_color}; background-color:{$white}; padding: 0px 0px 10px 0px;">
					<tr>
						<td width="33%">&nbsp;</td>
						<td width="452"><img src="{$img_dir}/hero.png" /></td>
						<td width="33%">&nbsp;</td>
					</tr>
					<tr>
						<td width="33%">&nbsp;</td>
						<td width="{$layout_w}" align="left" style="text-align:left;">
							<div style="color: {$purple}; font-size: 26px; {$fontfamily} font-weight: bold; margin: 27px 0 1px;">
								Welcome to Life on FoodKick!
							</div>
							
							<p style="margin-bottom:7px; {$fontfamily}">
								Get the food and booze that makes the moment-we'll bring it to your door in as little as one hour. Plus, your first 30 days of delivery are FREE!*
							</p>
							<p style="margin-bottom:7px; {$fontfamily}">
								We curate FoodKick daily to bring you the best of what's fresh right now, so you can discover new ideas and favorites like:
							</p>
							
							<p style="{$list_style}">
								<xsl:copy-of select="$list_img_tag"/> Customizable meal hacks &amp; ready-to-eat solutions
							</p>

							<p style="{$list_style}">
								<xsl:copy-of select="$list_img_tag"/> Farm-fresh produce, meat, fish and dairy
							</p>
							<p style="{$list_style}">
								<xsl:copy-of select="$list_img_tag"/> Pantry staples and home essentials
							</p>
							<p style="{$list_style}">
								<xsl:copy-of select="$list_img_tag"/> Wines, spirits, and beers to cheers
							</p>
							
							<p style="margin-bottom:20px; {$fontfamily}">
								<b>No need to plan or run errands ever again.</b> <br/>
								Seriously, even for TP.  Go ahead and place an order now, so you can get back to doing you.
							</p>
							
							<table width="276" align="center" style="width: 276px; height:52px; color: {$white}; font-size: 19px; border:none;" cellpadding="0" cellspacing="0" border="0">
								<tr>
									<td style="margin:0px;padding:0px;border:none">
										<img src="/images/clear.gif" width="1" height="32" border="0" alt="" style="display:none;border:none" />
									</td>
									<!--
									<td style=" font-size: 19px; vertical-align: middle; text-align: center; {$fontfamily} font-weight: bold; background-color: {$purple};">
										<a href="{$site}/media/editorial/foodkick/ua_router.html" style=" color: {$white}; text-decoration:none;">LET&#8217;S KICK IT</a>
									</td>
									//-->
									<td style=" font-size: 19px; vertical-align: middle; text-align: center; {$fontfamily} font-weight: bold;">
										<a href="{$editorial_dir}/ua_router.html" style=" color: {$white}; text-decoration:none;">
											<!--<img src="{$img_dir}/button2.png" width="1" height="32" border="0" alt="" />-->
											
											<img src="http://i.imgur.com/Y6lnAVB.png" width="277" height="49" border="0" alt="" />
										</a>
									</td>
									<td style="margin:0px;padding:0px;border:none;">
										<img src="/images/clear.gif" width="1" height="32" border="0" alt="" style="display:none;border:none" />
									</td>
								</tr>
							</table>
						</td>
						<td width="33%">&nbsp;</td>
					</tr>
					<tr>
						<td width="33%">&nbsp;</td>
						<td width="{$layout_w}">
							<h2 style="color:{$purple};text-align:center;font-size:1.3em; margin-top: 28px; margin-bottom: 45px; {$fontfamily}">Get your first 30 days of delivery FREE!</h2>
							<img src="{$img_dir}/insta.jpg" />
							<p style="font-size: 18px; line-height: 25px; {$fontfamily}">Follow us on Instagram and tap the Like2Buy link in our bio to shop our featured products and get daily foodspiration!</p>
						</td>
						<td width="33%">&nbsp;</td>
					</tr>
				</table>
				
			</div>
			<!-- <div style="text-align: center; padding: 3px; height: 30px; margin: 10px 0;"><a style="height: 100%; width: 100%; display: inline-block; padding: 0; margin: 0; font-size: 14px; color: {$purple}; font-weight: bold; line-height: 28px; text-decoration: none;" href="#">START HERE</a></div> -->
		</div>
		<xsl:call-template name="h_footer_fdx2"/>
	</xsl:template>
</xsl:stylesheet>
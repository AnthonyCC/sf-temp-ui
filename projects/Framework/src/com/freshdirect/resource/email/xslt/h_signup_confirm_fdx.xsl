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
	
	<xsl:variable name="site" select="'https://www.freshdirect.com'" />
	<xsl:variable name="img_dir" select="concat($site, '/media/images/email/foodkick')" />
	<xsl:variable name="purple" select="'#732484'" />
	<xsl:variable name="border_color" select="'#D8D8D8'" />
	<xsl:variable name="fontsize_p" select="'16px'" />
	<xsl:variable name="fontfamily" select="'Arial, Helvetica, sans-serif'" />
	<xsl:variable name="list_style" select="concat('background-image:url(', $img_dir, '/check.jpg);background-repeat:no-repeat; margin-bottom:0px; padding-top: 3px; padding-left:25px; min-height:20px; background-position: 0px 1px;')" />
	
	<xsl:template match="fdemail">
	<html>
		<head>
			<title>Congrats! You're In!</title>
			<link rel="stylesheet" href="{$site}/assets/css/emails.css" />
		</head>
		<body bgcolor="#f4f4f2" text="#333333" margin="0" style="margin:0px; font-family:{$fontfamily}">
			<xsl:call-template name="mail_body" />
		</body>
	</html>
	</xsl:template>

	<xsl:template name="mail_body">
		<div style="text-align:center; color:white; font-size:9pt; padding:15px 10px 5px;background-color: {$purple};">
			<span>Welcome to FoodKick! + 30 Days of Free Delivery</span>
			<span style="padding-left: 92px;">&nbsp;&nbsp;&nbsp;&nbsp;View <a href="http://www.foodkick.com" style="color: white;">Web</a></span>
		</div>
		
		<div style="background-color: #ffffff; border: 1px solid {$border_color}; margin: 5px; padding: 0px 20px 20px; text-align:center;">
			<div style="text-align:center;"><img src="{$site}/media/images/email/foodkick/hero.png" /></div>
			
			<div style="margin: 10px auto; font-size: {$fontsize_p}; text-align: center;">

				
				<table width="100%" bgcolor="#FFFFFF" align="center">
					<tr>
						<td width="33%">&nbsp;</td>
						<td width="452">

							<div style="color: {$purple}; font-size: 26px; font-family:{$fontfamily}; font-weight: bold; margin: 27px 0 1px;">
								Welcome to Life on FoodKick!
							</div>
							
							<p style="margin-bottom:7px;">
								Get the food and booze that makes the moment-we'll bring it to your door in as little as one hour. Plus, your first 30 days of delivery are FREE!*
							</p>
							<p style="margin-bottom:7px;">
								We're always in the palm of your hand giving you the inside scoon on:
							</p>
							
							<p style="{$list_style}">
								Customizable meal hacks from the freshest ingredients
							</p>
							<p style="{$list_style}">
								The perfect food and booze pairing recommendations from our squad
							</p>
							<p style="{$list_style}">
								Pantry staples and home essentials
							</p>
							<p style="{$list_style}">
								Wines, spirits, and beers to cheers
							</p>
							
							<p style="margin-bottom:20px;">
								<b>No need to plan or run errands ever again.</b> <br/>
								Even for TP.  Now, place an order and get back to doing you.
							</p>
							
							<table width="276" align="center" style="width: 276px; height:64px;">
								<tr>
									<td>
										<div style="text-align:center;
											background-color: {$purple};
											color: white;
											width: 276px;
											height:64px;
											margin: auto;
											font-size: 19px;
											vertical-align: middle;
											padding-top: 14px;
											padding-bottom: 12px;
											letter-spacing: 1.5px;
											font-family: {$fontfamily};
											font-weight: bold;
											text-decoration: none;
											display: block;">
											<a href="{$site}/media/editorial/foodkick/ua_router.html" style="color:white;text-decoration:none">LET&#8217;S KICK IT</a>
											
											<!--<img src="{$site}/media/images/email/foodkick/button.jpg" style="text-align:center;margin:auto" />-->
										</div>
									</td>
								</tr>
							</table>
						</td>
						<td width="33%">&nbsp;</td>
					</tr>
				</table>
				
				<h2 style="color:{$purple};text-align:center;font-size:1.3em; margin-top: 28px; margin-bottom: 45px;">Get your first 30 days of delivery FREE!</h2>
				
				<img src="{$img_dir}/insta.jpg" />
				
				<p style="font-size: 18px; line-height: 25px;">Follow us on Instagram and tap the Like2Buy link in our bio to shop our featured products and get daily foodspiration!</p>
			</div>
			<!-- <div style="text-align: center; padding: 3px; height: 30px; margin: 10px 0;"><a style="height: 100%; width: 100%; display: inline-block; padding: 0; margin: 0; font-size: 14px; color: {$purple}; font-weight: bold; line-height: 28px; text-decoration: none;" href="#">START HERE</a></div> -->
		</div>
		<xsl:call-template name="h_footer_fdx2"/>
	</xsl:template>
</xsl:stylesheet>
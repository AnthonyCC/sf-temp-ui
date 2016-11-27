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
	<xsl:variable name="layout_w_str" select="concat('width:', $layout_w, 'px;')" />

	<xsl:variable name="fontfamily" select="'font-family:Arial, Helvetica, sans-serif;'" />

	<xsl:variable name="list_img_tag"><img src="{$img_dir}/check_60.jpg" style="position:relative;top:5px; margin-right:3px;" /></xsl:variable>
	<xsl:variable name="list_style" select="concat( $fontfamily, 'margin-bottom:0px; padding-top: 3px; padding-left:0px; background-position: 0px 1px; text-align:left;')" />
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
					<!--<table width="{$layout_w}" style="width:{$layout_w}px; background-color: {$purple}; {$fontfamily} color:{$white}; font-size:0.9em; margin:auto">-->
					<table width="" style="width:{$layout_w}px; background-color: {$purple}; {$fontfamily} color:{$white}; font-size:0.9em; margin:auto">
						<tr>
							<td align="left" style="background-color: {$purple}; text-align:left; {$fontfamily} white-space:nowrap; width:300px">
								Welcome to FoodKick! + 60 Days of Free Delivery
							</td>
							<td align="right" style="text-align:right; {$fontfamily} white-space:nowrap; width:152px">
								&nbsp;&nbsp;&nbsp;&nbsp;View <a href="http://www.foodkick.com" style="color: {$white};">Web</a>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		
		<div align="center" style="{$fontfamily} margin: 5px auto; padding: 0px 25px 0px 5px; text-align:center; box-sizing: border-box;">
			<div style="margin: 10px auto; font-size: {$fontsize_p}; text-align: center;">
				<table width="" bgcolor="{$white}" align="center" style="border: 1px solid {$border_color}; background-color:{$white}; padding: 0px 0px 10px 0px; {$layout_w_str}">
					<tr>
						<td width="33%">&nbsp;</td>
						<td width=""><img src="{$img_dir}/hero_60.gif" /></td>
						<td width="33%">&nbsp;</td>
					</tr>
					<tr>
						<td width="33%">&nbsp;</td>
						<!--<td width="{$layout_w}" align="left" style="text-align:left;">-->
						<td width="" align="left" style="text-align:left;">
														
							<p style="margin-bottom:7px; {$fontfamily}">
								Hey <xsl:value-of select="customer/firstName"/>,
							</p>
							<p style="margin-bottom:7px; {$fontfamily}">
								Welcome to life on FoodKick! We curate our selection daily to bring you the food + booze you need right now–and we'll deliver to your door<b> in as little as an hour.</b>
							</p>
							<p style="color: {$purple}; font-size: 16px; {$fontfamily} font-weight: bold; margin: 20px 0 1px;">								
								No need to plan or run to the store ever again.
							</p>						
							
							<p style="{$list_style}">
								<xsl:copy-of select="$list_img_tag"/> Meal hacks–from grab-and-go breakfasts to quick weeknight dinners
							</p>

							<p style="{$list_style}">
								<xsl:copy-of select="$list_img_tag"/> Cold beers and party-perfect snacks
							</p>
							<p style="{$list_style}">
								<xsl:copy-of select="$list_img_tag"/> Top selection of wines and spirits
							</p>
							<p style="{$list_style}">
								<xsl:copy-of select="$list_img_tag"/> Locally-sourced produce, meat, fish, and dairy
							</p>
							<p style="{$list_style}">
								<xsl:copy-of select="$list_img_tag"/> Pantry staples and home essentials (like TP + Advil)
							</p>
																					
							<!--<table width="276" align="center" style="width: 276px; height:52px; color: {$white}; font-size: 19px; border:none;" cellpadding="0" cellspacing="0" border="0">-->
							<table width="" align="center" style="width: 276px; height:52px; color: {$white}; font-size: 19px; border:none;" cellpadding="0" cellspacing="0" border="0">
								<tr>
																		
									<td style="font-size: 19px; vertical-align: middle; text-align: center; {$fontfamily} font-weight: bold;">
										<a href="{$editorial_dir}/ua_router.html" style=" color: {$white}; text-decoration:none;">
											
											<img src="{$img_dir}/button_60.jpg" width="277" height="69" border="0" alt="Shop Now" />
										</a>
									</td>
									
								</tr>
							</table>
						</td>
						<td width="33%">&nbsp;</td>
					</tr>
					<tr>
						<td width="33%">&nbsp;</td>
						<!--<td width="{$layout_w}">-->
						<td width="">
							<a href="https://www.instagram.com/foodkick/"> <img src="{$img_dir}/insta_60.gif" /></a>							  
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
<?xml version="1.0"?>
<!DOCTYPE xsl:stylesheet [
    <!ENTITY nbsp "&#160;">
    <!ENTITY dot  "&#183;">
    <!ENTITY mdash  "&#8212;">
]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:output method="html"/>
	<!--<xsl:variable name="border_color" select="'#D8D8D8'" />-->

	<xsl:variable name="sociallink_styles" select="'text-decoration: none; border: none;padding-right:20px;display:inline-block'" />
	<xsl:variable name="whitebutton_cell_styles" select="concat('text-align: center; padding: 10px 5px 5px 10px; width: 33%; background-color:', $white, '; border: ', $border_color, ' 1px solid; white; white-space: nowrap;')" />
	<xsl:variable name="whitebutton_styles" select="concat('padding: 8px 5px 6px; margin: 10px;font-size: ', $fontsize_p, ';color: #732484;font-weight: bold;height: 36px;text-decoration: none; ', $fontfamily, 'width:158px;text-align:center; box-sizing: border-box;  white-space: nowrap;')" />
	
	<xsl:template name="h_footer_fdx2">
		<div style="margin: 0px auto; text-align: center;">
			<table cellpadding="0" cellspacing="7" width="490" style="border-collapse: separate; padding: 0; margin: auto; border-spacing: 7px; border-style: none; box-sizing: border-box;">
				<!--<tr>
					<td width="50%" style="text-align: center; padding: 0 5px 5px 0;"><a href="#" style="border: 1px solid {$border_color}; height: 100%; width: auto; display: inline-block; padding: 10px 30px; margin: 0; font-size: 18px; background-color: #732484; color: #fff; font-weight: bold; line-height: 28px; text-decoration: none;">Check/Change an Order</a></td>
					<td style="text-align: center; padding: 0 0 5px 5px;"><a href="#" style="border: 1px solid {$border_color}; height: 100%; width: auto; display: inline-block; padding: 10px 30px; margin: 0; font-size: 18px; background-color: #732484; color: #fff; font-weight: bold; line-height: 28px; text-decoration: none;">Contact Us</a></td>
				</tr>-->
				<!--<tr>
					<td colspan="3" style="height: 14px;"><img src="/images/clear.gif" width="1" height="10" border="0" alt="" style="display:none" /></td>
				</tr>-->
				<tr>
					<td style="{$whitebutton_cell_styles}">
						<a href="mailto:sidekicks@foodkick.com" style="{$whitebutton_styles}">Contact Us</a>
					</td>
					<td style="padding:0px"> &nbsp; </td>
					<td style="{$whitebutton_cell_styles}">
						<a href="{$site}" style="{$whitebutton_styles}">Go to FoodKick</a>
					</td>
					<td style="padding:0px"> &nbsp; </td>
					<td style="{$whitebutton_cell_styles}">
						<a href="{$site}/foodkick/faq.jsp" style="{$whitebutton_styles}">FAQs</a>
					</td>
				</tr>

				<tr> <td colspan="5" style="padding:0px"> &nbsp; </td> </tr>

				<tr>
					<td colspan="5" style="background-color: #ffffff; border: 1px solid {$border_color}; margin: 5px; padding: 20px;">
						<table cellpadding="0" cellspacing="0" width="100%" style="padding: 0; margin: 0; border-collapse: collapse; border-spacing: 0; border-style: none;" class="section email-footer-block">
							<tr>
								<td width="50%" style="font-family:Georgia, Times New Roman, Times, serif;font-size: 22px; font-weight: bold; padding-right: 10px;" align="center" valign="middle">
									Spread the
									<img src="{$img_dir}/heart.png" alt="love" style="margin-bottom: -5px;" />
									tell a friend
								</td>
							</tr>
							<tr>
								<td align="center" style="padding-top:20px">
									<a href="https://www.facebook.com/FoodKick/" style="{$sociallink_styles}"><img src="{$img_dir}/logo_facebook.png" alt="Facebook" style="text-decoration: none; border: none;padding-right:30px;" border="0" /></a>
									<a href="https://twitter.com/foodkick" style="{$sociallink_styles}"><img src="{$img_dir}/logo_twitter.png" alt="Twitter"  style="text-decoration: none; border: none;margin-right:30px;" border="0" /></a>
									<!-- <img src="{$img_dir}/logo_pinterest.png" alt="Pinterest" /> -->
									<a href="https://instagram.com/foodkick/" style="{$sociallink_styles}"><img src="{$img_dir}/logo_instagram.png" alt="Instagram" style="text-decoration: none; border: none;" border="0" /></a>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="5" >
						<div style="color: #888; padding: 20px;  {$fontfamily}">
							<p>This Offer is for free delivery on qualifying order for a thirty (30) day period.
								Offer applies to first-time customers only.  Free delivery means <u>no delivery or service fees</u>.
								<u>Eligible orders</u> must (a) exceed minimum purchase requirements before taxes &amp; fees,
								(b) be within eligible <u>delivery areas</u>, and (c) have a delivery window greater than one (1) hour.
								Delivery is subject to availability.  No promotional code necessary and the Offer will automatically apply starting with your first purchase and will continue for 30 days.
								This is a limited time Offer.  All standard customer terms and conditions apply.
								FoodKick reserves the right to cancel or modify this Offer at any time.  Offer is nontransferable.
								Void where prohibited.  All rights reserved, Fresh Direct, LLC.
							</p>
							
							<p>
								ALCOHOL CANNOT BE DELIVERED OUTSIDE OF NEW YORK STATE.
								Beer, wine and spirits will be removed from your cart during checkout if an out-of-state address is selected for delivery.
								IT IS A VIOLATION PUNISHABLE UNDER LAW FOR ANY PERSON UNDER THE AGE OF TWENTY-ONE TO PRESENT ANY WRITTEN EVIDENCE OF AGE WHICH IS FALSE,
								FRAUDULENT OR NOT ACTUALLY HIS/HER OWN FOR THE PURPOSE OF ATTEMPTING TO PURCHASE ANY ALCOHOLIC BEVERAGE.
								Wines &amp; Spirits are sold by FreshDirect Wines &amp; Spirits, an independently owned store with NY State #1277181 | 620 5th Ave,
								Brooklyn, NY 11215, Tel: (718) 768-1521, M-W 1-10pm, Th-Sa  11am-10pm, Su  Noon-8pm  2016 FreshDirect, LLC dba FreshDirect Wines &amp; Spirits.
							</p>	
							
							<xsl:if test="//fromEmail != ''">
								<br /><br />Please add <xsl:value-of select="//fromEmail"/> to your address book to ensure our emails reach your inbox.<br /><br />
							</xsl:if>
							<!-- For FoodKick online Help or to contact us, please <a href="#">click here.</a><br /><br /> -->
							For FoodKick online Help or to contact us, please <a href="mailto:sidekicks@foodkick.com" style="color: #888;">click here.</a><br /><br />
							23-30 Borden Ave. Long Island City, NY 11101<br /><br />
							<!-- <a href="#">Customer Agreement</a><br /> -->
							
							<a href="#" style="color: #888; text-decoration:none;">Customer Agreement</a><br />
							&#169;2002 - <xsl:value-of select="substring(//curYear,1,4)" /> Fresh Direct, LLC. All Rights Reserved.
						</div>
						
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>
</xsl:stylesheet>
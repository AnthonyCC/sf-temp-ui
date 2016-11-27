<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
	<xsl:output method="html"/>
	<xsl:template name="h_footer_fdx">
		
		<div style="margin: 5px;">
			<table cellpadding="0" cellspacing="0" width="100%" style="padding: 0; margin: 0; border-collapse: collapse; border-spacing: 0; border-style: none;">
				<!--<tr>
					<td width="50%" style="text-align: center; padding: 0 5px 5px 0;"><a href="#" style="border: 1px solid #bbbbbb; height: 100%; width: auto; display: inline-block; padding: 10px 30px; margin: 0; font-size: 18px; background-color: #732484; color: #fff; font-weight: bold; line-height: 28px; text-decoration: none;">Check/Change an Order</a></td>
					<td style="text-align: center; padding: 0 0 5px 5px;"><a href="#" style="border: 1px solid #bbbbbb; height: 100%; width: auto; display: inline-block; padding: 10px 30px; margin: 0; font-size: 18px; background-color: #732484; color: #fff; font-weight: bold; line-height: 28px; text-decoration: none;">Go to FoodKick</a></td>
				</tr>
				<tr>
					<td colspan="2"><img src="/images/clear.gif" width="1" height="10" border="0" alt="" /></td>
				</tr>
				<tr>
					<td style="text-align: center; padding: 0 5px 5px 0;"><a href="#" style="border: 1px solid #bbbbbb; height: 100%; width: auto; display: inline-block; padding: 10px 30px; margin: 0; font-size: 18px; background-color: #732484; color: #fff; font-weight: bold; line-height: 28px; text-decoration: none;">Contact Us</a></td>
					<td style="text-align: center; padding: 0 0 5px 5px;"><a href="#" style="border: 1px solid #bbbbbb; height: 100%; width: auto; display: inline-block; padding: 10px 30px; margin: 0; font-size: 18px; background-color: #732484; color: #fff; font-weight: bold; line-height: 28px; text-decoration: none;">FAQs</a></td>
				</tr>
				<tr>
					<td colspan="2"><img src="/images/clear.gif" width="1" height="10" border="0" alt="" /></td>
				</tr>-->
				<tr>
					<td colspan="2" style="background-color: #ffffff; border: 1px solid #bbbbbb; margin: 5px; padding: 20px;">
						<table cellpadding="0" cellspacing="0" width="100%" style="padding: 0; margin: 0; border-collapse: collapse; border-spacing: 0; border-style: none;" class="section email-footer-block">
							<tr>
								<td width="50%" style="font-size: 22px; font-weight: bold; padding-right: 10px;" align="right" valign="middle">
									Spread the <img src="http://www.freshdirect.com/media/images/email/foodkick/heart.png" alt="love" style="margin-bottom: -5px;" /> tell a friend</td>
								<td align="left" style="padding-left: 10px;">
									<a href="https://www.facebook.com/FoodKick/" style="text-decoration: none; border: none;"><img src="http://www.freshdirect.com/media/images/email/foodkick/logo_facebook.png" alt="Facebook" style="text-decoration: none; border: none;" border="0" /></a>
									<a href="https://twitter.com/foodkick" style="text-decoration: none; border: none;"><img src="http://www.freshdirect.com/media/images/email/foodkick/logo_twitter.png" alt="Twitter"  style="text-decoration: none; border: none;" border="0" /></a>
									<!-- <img src="http://www.freshdirect.com/media/images/email/foodkick/logo_pinterest.png" alt="Pinterest" /> -->
									<a href="https://instagram.com/foodkick/" style="text-decoration: none; border: none;"><img src="http://www.freshdirect.com/media/images/email/foodkick/logo_instagram.png" alt="Instagram" style="text-decoration: none; border: none;" border="0" /></a>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			
			<div style="color: #888; padding: 20px;">
				<xsl:if test="//fromEmail != ''">Please add <xsl:value-of select="//fromEmail"/> to your address book to ensure our emails reach your inbox.<br /><br /></xsl:if>
				<!-- For FoodKick online Help or to contact us, please <a href="#">click here.</a><br /><br /> -->
				23-30 Borden Ave. Long Island City, NY 11101<br /><br />
				<!-- <a href="#">Customer Agreement</a><br /> -->
				&#169;2002 - <xsl:value-of select="substring(//curYear,1,4)" /> Fresh Direct, LLC. All Rights Reserved.
			</div>
			
		</div>
			
		
	</xsl:template>
</xsl:stylesheet>
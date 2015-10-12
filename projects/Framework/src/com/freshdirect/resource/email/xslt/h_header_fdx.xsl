<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html"/>
	<xsl:template name="h_header_fdx">
		<xsl:param name="preview">false</xsl:param>
		<xsl:choose>
			<xsl:when test="$preview = 'true'">
				<img src="http://www.freshdirect.com/media/images/email/foodkick/header_logo.png" alt="FoodKick" border="0" /><br/><img src="http://www.freshdirect.com/media/images/email/foodkick/line.png" alt="" border="0" style="margin: 0 8px;" />
			</xsl:when>
			<xsl:otherwise>
				<table cellpadding="0" cellspacing="0" border="0" width="100%">
					<tr>
						<td align="center" style="padding: 0 0 10px 0;"><img src="http://www.freshdirect.com/media/images/email/foodkick/header_logo.png" alt="FoodKick" border="0" /></td>
					</tr>
					<tr><td align="center" style="background-image: url('http://www.freshdirect.com/media/images/email/foodkick/line.png');"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="10" alt="" border="0" style="margin: 0 8px;" /></td></tr>
				</table>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
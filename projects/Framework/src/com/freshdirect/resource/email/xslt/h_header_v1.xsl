<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="html"/>
	<xsl:template name="h_header_v1">
		<xsl:param name="preview">false</xsl:param>
		<xsl:choose>
			<xsl:when test="$preview = 'true'">
				<img src="http://www.freshdirect.com/media_stat/images/logos/fd_logo_md.gif" width="216" height="42" alt="FreshDirect" border="0"/><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8" alt="" border="0"/>
			</xsl:when>
			<xsl:otherwise>
				<table cellpadding="0" cellspacing="0" border="0" width="90%">
					<tr>
						<td><a href="http://www.freshdirect.com"><img src="http://www.freshdirect.com/media_stat/images/logos/fd_logo_md.gif" width="216" height="42" alt="FreshDirect" border="0"/></a></td>
					</tr>
					<tr><td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8" border="0"/></td></tr>
				</table>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>
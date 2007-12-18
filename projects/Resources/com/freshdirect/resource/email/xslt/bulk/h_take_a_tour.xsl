<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:doc="http://xsltsl.org/xsl/documentation/1.0"
	xmlns:str="http://xsltsl.org/string"
	extension-element-prefixes="doc str">
	<xsl:output method="html"/>
	<xsl:include href='h_header_v1.xsl'/>
	<xsl:include href='h_optout_footer.xsl'/>
	<xsl:include href='h_footer_v2.xsl'/>
	<xsl:include href='string.xsl' />
	<xsl:output method="html"/>
	<xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">

<html>
	<head>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
	</head>
<body bgcolor="#FFFFFF" text="#333333">
	<xsl:call-template name="mail_body" />
</body>
</html>

</xsl:template>

<xsl:template name="mail_body">

<xsl:call-template name="h_header_v1" />
	<table cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr>
			<td bgcolor="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1" /></td>
		</tr>
		<tr>
			<td><a href="http://www.freshdirect.com/about/plant_tour/index.jsp?catId=about_tour"><img src="http://www.freshdirect.com/media_stat/images/template/about/plant_tour/taketour_hdr.gif" width="611" height="41" vspace="8" alt="Take a tour of FreshDirect!" border="0" /></a></td>
		</tr>
		<tr>
			<td bgcolor="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1" /></td>
		</tr>
		<tr>
			<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="14" /></td>
		</tr>
	</table>
	<table cellpadding="0" cellspacing="0" border="0" width="100%">
		<tr valign="top">
			<td width="75%" class="bodyCopy" valign="top">
				Dear <xsl:value-of select="customer/firstName"/>, 
				<br /><br />
				Now that you've tried FreshDirect, we thought you might want to <b>learn about where your food comes from</b>.
				<br /><br />
				We've recently added photographs of our fully refrigerated, 300,000 square foot facility to our web site. Learn more about FreshDirect, one of the cleanest, most technologically advanced food facilities in the country.
				<br /><br /><ul>
				<li>12 climate zones, ranging from 62 to -40 degrees, providing optimal conditions for fresh food</li>
				<li>We follow USDA guidelines and the HACCP food safety system</li>
				<li>In-house bakery &amp; state-of-the-art USDA kitchen</li>
				<li>A full-time microbiologist and onsite laboratory</li></ul>
				<a href="http://www.freshdirect.com/about/plant_tour/index.jsp?catId=about_tour"><b>Click here to take a photographic tour and learn more about our food facility.</b></a>
				<br /><br />
				Happy Holidays,
				<br /><br />
				<b>Joe Fedele</b>, CEO and Co-Founder
				<br /><br />
				<b>FreshDirect</b><br />
				It's All About the Food<br />
			</td>
			<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="8" height="1" /></td>
			<td width="25%" align="right"><a href="http://www.freshdirect.com/about/plant_tour/index.jsp?catId=about_tour"><img src="http://www.freshdirect.com/media_stat/images/template/about/plant_tour/taketour_img.jpg" width="180" height="330" border="0" alt="Our Facility" /></a></td>
		</tr>
		<tr>
			<td colspan="3"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="8" /></td>
		</tr>
	</table>
	
	<!-- disclaimer include -->
	<xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/>

</xsl:template>

</xsl:stylesheet>

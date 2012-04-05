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
	<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
	<style type="text/css">
		v\:* { behavior: url(#default#VML); display:inline-block}
	</style>
</head>
<body>
<table width="800"> <tr><td>
<div id="parentdiv" style="float:left;position:relative !important;top:0;left:0;">
<table width="800" height="760" background="http://www.freshdirect.com/media_stat/images/profile/email_background1.jpg" style="background-image:url('http://www.freshdirect.com/media_stat/images/profile/email_background1.jpg');background-repeat: no-repeat;background-position: top;border:0;">
		<xsl:comment>[if gte vml 1]<![CDATA[>]]>
        &lt;v:shape stroked="f" style= "float:left;position:absolute;z-index:-2;visibility:visible;width:800px; height:760px;top:0;left:0px;border:0;align:center;"&gt;
					&lt;v:imagedata src="http://www.freshdirect.com/media_stat/images/profile/email_background1.jpg"/&gt;
        <![CDATA[<![endif]]]></xsl:comment>
		
		<tr valign="top"><td>
		<table width="550" border='0' style="float:left;position:absolute !important;width:550px;align:center;top:70px;left:70px;padding-left:50px;padding-top:107px;margin-left:70px;">
			<xsl:comment>[if gte vml 1]<![CDATA[>]]>
			&lt;tr&gt;&lt;td colspan="2"&gt;&lt;br/&gt;&lt;br/&gt;&lt;/td&gt;&lt;/tr&gt;
			<![CDATA[<![endif]]]></xsl:comment>
				<tr>
					<td colspan="2" align="center">
						<img src="http://www.freshdirect.com/media_stat/images/profile/unlock_greate_taste.jpg" border="0" />
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<p><img src="http://www.freshdirect.com/media_stat/images/profile/left_curly_bracket.jpg" border="0" /><b><xsl:value-of select="name"/></b> has invited you to try Freshdirect.com<img src="http://www.freshdirect.com/media_stat/images/profile/right_curly_bracket.jpg" border="0" /></p><br />
					</td>
				</tr>
				<tr>
					<td valign="top" colspan="2" align="center" style="line-height:20px;">
						<p>&quot;<xsl:value-of select="userMessage"/>&quot;</p>
					</td>					
				</tr>
				<tr><td colspan="2"><br/><br/></td></tr>
				<tr>
					<td valign="top" colspan="2" align="center" style="line-height:20px;">
						<p><xsl:value-of select="systemMessage"/></p>
					</td>					
				</tr>				
				<tr>
					<td valign="top" colspan="2" align="center">
						<p><br/><a href="{refLink}"><img src="http://www.freshdirect.com/media_stat/images/profile/sign_up_and_start_shopping.jpg" border="0" /></a></p>
					</td>					
				</tr>											
			</table>			
</td></tr>
<xsl:comment>[if gte vml 1]<![CDATA[>]]>
        &lt;/v:imagedata&gt;
		&lt;/v:shape&gt;
        <![CDATA[<![endif]]]></xsl:comment>
</table>
</div>
</td></tr>
<tr><td>
<xsl:comment>[if gte vml 1]<![CDATA[>]]>
	&lt;v:shape style="position:absolute;top:375px;left:0;margin-top:375px;width:800px;"&gt;		
 <![CDATA[<![endif]]]></xsl:comment>
<table width="100%">
	<tr><td align="center" style="font-size:9px;">
	Legal: <xsl:value-of select="legal"/>
	</td></tr>
</table>
<xsl:comment>[if gte vml 1]<![CDATA[>]]>
	&lt;/v:shape&gt;
 <![CDATA[<![endif]]]></xsl:comment>
</td></tr>
<tr><td>
<xsl:comment>[if gte vml 1]<![CDATA[>]]>
	&lt;v:shape style="position:absolute;top:415px;left:0;margin-top:415px;width:800px;height:100px;"&gt;		
 <![CDATA[<![endif]]]></xsl:comment>
<table width="100%">
	<tr><td align="center">
	<xsl:call-template name="h_footer_v1"/>
	</td></tr>
</table>
<xsl:comment>[if gte vml 1]<![CDATA[>]]>
	&lt;/v:shape&gt;
 <![CDATA[<![endif]]]></xsl:comment>
</td></tr>
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
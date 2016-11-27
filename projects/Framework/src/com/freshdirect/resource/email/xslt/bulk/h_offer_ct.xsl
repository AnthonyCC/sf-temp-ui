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
        <BASE href="http://www.freshdirect.com" />
		<title>A Special Offer for FreshDirect Customers</title>
		<link rel="stylesheet" href="http://www.freshdirect.com/assets/css/emails.css"/>
	</head>
<body bgcolor="#FFFFFF" text="#333333">
	<xsl:call-template name="mail_body" />
</body>
</html>

</xsl:template>

<xsl:template name="mail_body">

<xsl:comment>

</xsl:comment>

<table cellpadding="0" cellspacing="0">
<tr>
<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
<td><xsl:call-template name="h_header_v1" />
	<table cellpadding="0" cellspacing="0" width="90%">
		<tr>
		<td width="100%"><table width="100%" cellpadding="0" cellspacing="0" border="0"><tr><td bgcolor="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1" border="0" alt="" /></td></tr></table></td>
		</tr>
	</table><br/>
	<table cellpadding="0" cellspacing="0" width="90%">
                <tr><td colspan="3"><img src="http://www.freshdirect.com/media/images/promotions/email/ct.gif" width="527" height="17"/><br/><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="14"/></td></tr>
                <tr valign="top">
				<td>
				<p>In our effort to bring our customers the best culinary experiences, FreshDirect has partnered with Chef's Theater, a delicious new kind of entertainment experience.
				Chef's Theater, staged at the elegant Supper Club, combines the best of fine dining with the best of Broadway... for a one-of-a-kind night on the town.
				Some of the culinary stars of the show include:</p> 
				<p>
				<b>Todd English</b>, <i>Olives</i>  March 30 - April 4<br/>
				<img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="6" /><br/>
				<b>Tom Valenti</b>, <i>'Cesca</i>, Ouest  April 6 - April 11<br/>
				<img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="6" /><br/>
				Food Network's <b>Tyler Florence</b> April 13 - April 18<br/>
				<img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="6" /><br/>
				<b>Mary Sue Milliken</b> and <b>Susan Feniger</b> (formerly <i>Too Hot Tamales</i>), <i>Border Grill</i> April 20 - April 25<br/>
				<img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="6" /><br/>
				<b>Michael Lomonaco</b>, <i>Noche</i>  April 27 - May 2<br/>
				<img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="6" /><br/>
				<b>Michael Romano</b>, <i>Union Square Caf&#233;</i>  May 4 - May 9<br/>
				</p>
				<p>
				FreshDirect and Chef's Theater are proud to provide an exclusive delicious deal for FreshDirect customers:
				<div align="center">
				<br/><b>Buy two dinner tickets, save $30*<br/>
				<img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="4" /><br/>
				Buy two brunch tickets, save $20*<br/>
				<img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="4" /><br/>
				Buy two dessert tickets, save $10*</b>
				</div><br/>
				Orders must be placed by April 13, 2004.  Offer good for all performances through June 27, 2004.
				</p>
				<p>
				To take advantage of this special offer, visit <a href="http://www.broadwayoffers.com">www.broadwayoffers.com</a> and enter code <b>CFFD459</b>, or call Telecharge.com at <b>212-947-8844</b> and mention code <b>CFFD459</b>.</p>
                </td>
				<td width="25"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="25" height="1" /></td>
				<td width="268"><img src="http://www.freshdirect.com/media_stat/images/template/email/offer/ct_logo.gif" width="268" height="287" alt="Chef's Theatre, a musical feast" border="0"/></td>
                </tr>
        		<tr>
        		<td colspan="3">
					<img src="http://www.freshdirect.com/media_stat/images/template/email/offer/ct_bottom.gif" width="630" height="133" vspace="10" alt="Chef's Theatre is a delicious new kind of entertainment experience."/><br/>
					*Offer subject to availability.  Offer may be revoked at any time.  Tickets must be ordered by 4/13/04.  Offer valid through 6/27/04.  Ticket prices do not include gratuity.
        			<p><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/></p>
        		</td>
        	    </tr>
	</table>
</td>
<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
</tr>
</table>

</xsl:template>

</xsl:stylesheet>
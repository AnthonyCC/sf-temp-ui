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
        <title>Lobster Friday.</title>
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
    <p>
    <table cellpadding="0" cellspacing="0" width="90%">
            <tr><td>
              <table width="295"><tr><td valign="top">
             <a href="http://www.freshdirect.com/product.jsp?productId=hmrsea_splitlobstr&amp;catId=hmr_entree_sea&amp;trk=lob01"><img src="http://www.freshdirect.com/media_stat/images/template/email/lobster_friday/steamed_lob_hdr.gif" width="295" height="135" border="0" /></a>
             <br/>
             Whether it's date night, family night, or you're just happy to have made it through another week, every Friday is cause for celebration! Every week, Montauk lobstermen deliver their catch to Gosman's, who rush the live lobsters to us on Thursday. We steam them for you, bringing a lobster dinner to your door on Friday evening. Your lobster dinner is delicious cold, or warm it for a moment on the grill. We'll include our tangy cocktail sauce and creamy, spicy wasabi mayonnaise for dipping.
             <br/><br/><a href="http://www.freshdirect.com/product.jsp?productId=hmrsea_splitlobstr&amp;catId=hmr_entree_sea&amp;trk=lob01">Click here to Learn More!</a>
             </td>
            <td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
            <td valign="top"><a href="http://www.freshdirect.com/product.jsp?productId=hmrsea_splitlobstr&amp;catId=hmr_entree_sea&amp;trk=lob01"><img src="http://www.freshdirect.com/media_stat/images/template/email/lobster_friday/lobster.jpg" width="285" height="226" border="0" /></a>
            <br/>
            <a href="http://www.freshdirect.com/product.jsp?productId=hmrsea_splitlobstr&amp;catId=hmr_entree_sea&amp;trk=lob01"><img src="http://www.freshdirect.com/media_stat/images/template/email/lobster_friday/lob_price.gif" width="285" height="22" border="0" /></a></td>
            </tr></table></td></tr>
            <tr><td><br/><table cellpadding="0" cellspacing="0">
                <tr><td colspan="7" bgcolor="#CCCCCC"><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="1" height="1" border="0" alt="" /></td></tr>
                <tr>
                 <td colspan="7"><br/>
                    <img src="http://www.freshdirect.com/media_stat/images/template/email/lobster_friday/wines_hdr.gif" width="606" height="12" border="0" />
                    <br/><br/>
                 </td>
                </tr>
                <tr>
                <td valign="top">
                <!-- Wine img1 -->
                    <a href="http://www.freshdirect.com/product.jsp?productId=wine_bc_hopeestate_1&amp;catId=win_luscious&amp;trk=lob01"><img src="http://www.freshdirect.com/media_stat/images/template/email/lobster_friday/bc_hopeestate.jpg" width="45" height="121" border="0" /></a>
                </td>
                <td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="5" height="1" border="0" /></td>
                <td valign="top" class="bodyCopySmall">
                  <!--wine text1-->
                    <b><a href="http://www.freshdirect.com/product.jsp?productId=wine_bc_hopeestate_1&amp;catId=win_luscious&amp;trk=lob01">Hope Estate Chardonnay<br/>
                    "Broke" Vineyard 2002</a> - $13.00</b><br/>
                    Fans of rich, toasty, full-throttle Chardonnays will be thrilled at both the style and price of this big, toasty, buttery, down-under wonder, which tastes more like a Meursault than it has a right to.
                    <br/><br/><a href="http://www.freshdirect.com/product.jsp?productId=wine_bc_hopeestate_1&amp;catId=win_luscious&amp;trk=lob01">Click here to Learn More!</a>
                </td>
                <td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
                <td valign="top">
                <!-- Wine img2 -->
                    <a href="http://www.freshdirect.com/product.jsp?productId=wine_bc_terrapin_1&amp;catId=win_soft&amp;trk=lob01"><img src="http://www.freshdirect.com/media_stat/images/template/email/lobster_friday/bc_terrapin.jpg" width="45" height="121" border="0" /></a>
                </td>
                <td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="5" height="1" border="0" /></td>
                <td valign="top" class="bodyCopySmall">
                  <!--wine text2-->
                    <b><a href="http://www.freshdirect.com/product.jsp?productId=wine_bc_terrapin_1&amp;catId=win_soft&amp;trk=lob01">Terrapin Bay Chardonnay<br/>
                    -Viognier 2002</a> - $10.00</b><br/>
                    What happens when you combine two hot white grapes in the same cool, bang-for-the-buck bottle? You get Terrapin Bay  one of our tastiest new wines for $10! In the glass, notes of white flowers and peach skin unite with the flavors of ripe pears and grapefruit zest, giving TB a mouthwatering quality that is practically irresistible.
                    <br/><br/><a href="http://www.freshdirect.com/product.jsp?productId=wine_bc_terrapin_1&amp;catId=win_soft&amp;trk=lob01">Click here to Learn More!</a>
                </td>
            </tr></table>
            <br/><br/>
            </td></tr>
            <tr><td><xsl:call-template name="h_optout_footer"/><xsl:call-template name="h_footer_v2"/></td></tr>
    </table>
   </p>
</td>
<td><img src="http://www.freshdirect.com/media_stat/images/layout/clear.gif" width="20" height="1" border="0" /></td>
</tr>
</table>

</xsl:template>

</xsl:stylesheet>
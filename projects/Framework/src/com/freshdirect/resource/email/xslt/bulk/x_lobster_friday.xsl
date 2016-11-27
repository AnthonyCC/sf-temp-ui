<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:dt='http://xsltsl.org/date-time' version="1.0">
    <xsl:include href='x_header.xsl'/>
    <xsl:include href='x_optout_footer.xsl'/>
    <xsl:include href='x_footer_v2.xsl'/>
    <xsl:output method="text"/>
    <xsl:decimal-format name="USD" decimal-separator="." grouping-separator=","/>
<xsl:template match="fdemail">
<xsl:call-template name="x_header"/>

Whether it's date night, family night, or you're just happy to have made it through another week, every Friday is cause for celebration! Every week, Montauk lobstermen deliver their catch to Gosman's, who rush the live lobsters to us on Thursday. We steam them for you, bringing a lobster dinner to your door on Friday evening. Your lobster dinner is delicious cold, or warm it for a moment on the grill. We'll include our tangy cocktail sauce and creamy, spicy wasabi mayonnaise for dipping.
http://www.freshdirect.com/product.jsp?productId=hmrsea_splitlobstr&amp;catId=hmr_entree_sea&amp;trk=lob01

Hope Estate Chardonnay
"Broke" Vineyard 2002 - $13.00
Fans of rich, toasty, full-throttle Chardonnays will be thrilled at both the style and price of this big, toasty, buttery, down-under wonder, which tastes more like a Meursault than it has a right to. 
http://www.freshdirect.com/product.jsp?productId=wine_bc_hopeestate_1&amp;catId=win_luscious&amp;trk=lob01

Terrapin Bay Chardonnay
-Viognier 2002 - $10.00
What happens when you combine two hot white grapes in the same cool, bang-for-the-buck bottle? You get Terrapin Bay--one of our tastiest new wines for $10! In the glass, notes of white flowers and peach skin unite with the flavors of ripe pears and grapefruit zest, giving TB a mouthwatering quality that is practically irresistible.
http://www.freshdirect.com/product.jsp?productId=wine_bc_terrapin_1&amp;catId=win_soft&amp;trk=lob01

<xsl:call-template name="x_optout_footer"/>
<xsl:call-template name="x_footer_v2"/>
</xsl:template>
</xsl:stylesheet>

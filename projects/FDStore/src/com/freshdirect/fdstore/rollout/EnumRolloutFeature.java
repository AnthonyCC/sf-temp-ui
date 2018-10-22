package com.freshdirect.fdstore.rollout;

public enum EnumRolloutFeature {
    pdplayout2014("pdplayout", "2014"),
    pplayout2014("pplayout", "2014"),
    leftnav2014("leftnav", "2014"),
    searchredesign2014("searchredesign", "2014"),
    leftnavtut2014("leftnavtut", "2014"),
    browseflyoutrecommenders("browseflyout", "recommenders"), // fly-out recommended products on "browse" pages (transactionalPopup)
    quickshop2_2("quickshop", "2_2"),
    quickshop2_0(quickshop2_2, "quickshop", "2_0"),
    akamaiimageconvertor("akamaiimageconvertor", "2015"),
    checkout2_0("checkout", "2_0"),
    checkout1_0(checkout2_0, "checkout", "1_0"),
    gridlayoutcolumn4_0("gridlayoutcolumn", "4_0"),
    gridlayoutcolumn5_0(gridlayoutcolumn4_0, "gridlayoutcolumn", "5_0"),
    standingorder3_0("standingorder", "3_0"),
    browseaggregatedcategories1_0("browseaggregatedcategories", "1_0"), // browse category page with aggregated sub-categories
    hooklogic2016("hooklogic", "2016"),
    priceconfigdisplay2016("priceconfigdisplay", "2016"),
    unbxdintegrationblackhole2016("unbxdintegrationblackhole", "2016"),
    unbxdanalytics2016("unbxdanalytics", "2016"),
    mobweb("mobweb", "2016"),
    mobwebindexopt("mobwebindexopt", "2017"), /* optimized index template */
    printinvoice("printinvoice", "2016"), // has print invoice button
    carttabcars("carttabcars", "2017"),
    debitCardSwitch("debitCardSwitch", "2017"),
    cosRedesign2017("cosRedesign2017", "2017"),
    modOrderConfirmPageRedesign("modOrderConfirmPageRedesign", "2018"),
    aggregatedfilterimprovement2018("aggregatedfilterimprovment", "2018"),
    backOfficeSelfCredit("backOfficeSelfCredit", "2018"),
    productCard2018("productCard", "2018");

    private final EnumRolloutFeature child;
    private final String cookieName;
    private final String cookieVersion;

    private EnumRolloutFeature(String cookieName, String cookieVersion) {
        this(null, cookieName, cookieVersion);
    }

    private EnumRolloutFeature(EnumRolloutFeature child, String cookieName, String cookieVersion) {
        this.child = child;
        this.cookieName = cookieName;
        this.cookieVersion = cookieVersion;
    }

    /**
     * @return the parent
     */
    public EnumRolloutFeature getChild() {
        return child;
    }

    /**
     * @return the cookieName
     */
    public String getCookieName() {
        return cookieName;
    }

    /**
     * @return the cookieVersion
     */
    public String getCookieVersion() {
        return cookieVersion;
    }

    public boolean featureIsChild(EnumRolloutFeature childCandidate) {
        boolean result = false;
        EnumRolloutFeature feature = this.getChild();
        while (result == false && feature != null) {
            result = feature == childCandidate;
            feature = feature.getChild();
        }
        return result;
    }
}

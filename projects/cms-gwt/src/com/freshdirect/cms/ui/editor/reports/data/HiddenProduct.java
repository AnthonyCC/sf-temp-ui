package com.freshdirect.cms.ui.editor.reports.data;

import com.freshdirect.cms.core.domain.ContentKey;

public class HiddenProduct {

    private final ContentKey productKey;

    private final ContentKey storeKey;

    private final ContentKey primaryHomeKey;

    private final HiddenProductReason reason;

    public HiddenProduct(ContentKey storeKey, ContentKey productKey, ContentKey primaryHomeKey, HiddenProductReason reason) {
        this.storeKey = storeKey;
        this.productKey = productKey;
        this.primaryHomeKey = primaryHomeKey;
        this.reason = reason;
    }


    public ContentKey getProductKey() {
        return productKey;
    }


    public ContentKey getStoreKey() {
        return storeKey;
    }


    public ContentKey getPrimaryHomeKey() {
        return primaryHomeKey;
    }


    public HiddenProductReason getReason() {
        return this.reason;
    }

    @Override
    public String toString() {
        return storeKey + ";" + productKey + "->" + primaryHomeKey;
    }
}

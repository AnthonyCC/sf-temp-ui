package com.freshdirect.storeapi.multistore;

/**
 * This type represents the CMS context
 * of a runtime in regard of how many store
 * node is available.
 *
 * Storefront including preview and mobile service
 * require CMS content with only one store in it.
 *
 * @author segabor
 *
 */
public enum MultiStoreContext {
    /**
     * Store content has just one and only one store top node.
     *
     * Typically, production cluster nodes fed by Store.xml
     * run in this context.
     */
    SINGLESTORE,

    /**
     * Store model may contain multiple store nodes
     * and none of them is even set.
     *
     * CMS Editor node (cms-gwt app) and CMS Admin
     * uses this context.
     *
     * Only ContentNode level CMS features are available!
     */
    MULTISTORE,

    /**
     * Store model may contain multiple store nodes,
     * one of them is promoted as the actual store key.
     *
     * CMS Preview nodes run this way
     */
    SINGLESTORE_PREVIEW,

    /**
     * This context is held for nodes requiring
     * no CMS at all.
     *
     * CRM application is one of them.
     */
    NO_STORE;

    /**
     * Use this method to check if CMS has one and only one
     * store key.
     *
     * see CmsManager#getSingleStoreKey()
     * see CmsManager#getEStoreEnum()
     *
     * @return
     */
    public boolean isSingleStore() {
        return this == SINGLESTORE || this == SINGLESTORE_PREVIEW;
    }

    /**
     * Returns true when CMS subsystem may contain multiple store keys.
     *
     *
     * @return CMS can have multiple store keys.
     */
    public boolean isMultiStore() {
        return this == MULTISTORE;
    }

    /**
     * CMS is either unavailable or there's no store key in it
     *
     * @return
     */
    public boolean isNoStore() {
        return this == NO_STORE;
    }

    /**
     * Returns true if node runs a preview node.
     * That is, runs on DB but there's a store key promoted for store content provision
     *
     * @return
     */
    public boolean isPreviewNode() {
        return this == SINGLESTORE_PREVIEW;
    }

}

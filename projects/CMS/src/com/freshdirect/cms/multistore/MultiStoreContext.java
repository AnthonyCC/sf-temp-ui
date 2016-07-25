package com.freshdirect.cms.multistore;

import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentServiceI;

/**
 * This type represents the CMS context
 * of a runtime in regard of how many store
 * node is available.
 *
 * @author segabor
 *
 */
public enum MultiStoreContext {
    /**
     * Store model has one and only one store node.
     * 
     * Typically, production cluster nodes fed by Store.xml
     * run in this context.
     */
    SINGLESTORE,
    
    /**
     * Store model may contain multiple store nodes
     * and none of them is even set.
     * 
     * CMS Editor node (cms-gwt app) uses this context.
     * 
     * Only ContentNode level CMS features are available!
     * 
     * @see ContentServiceI
     */
    MULTISTORE,

    /**
     * Store model may contain multiple store nodes, 
     * one of them is promoted as the actual store key.
     * 
     * CMS Preview nodes run this way
     * 
     * @see CmsManager#getSingleStoreKey()
     */
    MULTISTORE_WITH_KEY,

    /**
     * This context is held for nodes requiring
     * no CMS at all.
     * 
     * CRM application is one of them.
     */
    NO_STORE
}

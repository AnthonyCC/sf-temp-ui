/*
 * Created on Dec 28, 2004
 *
 */
package com.freshdirect.cms.application;

import java.util.Collection;

import com.freshdirect.cms.ContentNodeI;


/**
 * Represents a transactional content modification request.
 * A request is a collection of {@link com.freshdirect.cms.ContentNodeI}
 * objects that need to be updated. It also has contextual information, such as
 * the {@link com.freshdirect.cms.application.UserI} executing the transaction.
 */
public interface CmsRequestI {
    
    /**
     * Type designated to denote origin of change request.
     */
    enum Source {
        NODE_EDITOR,    // gwt: save node
        BULKLOADER,     // change request comes from bulkloader
        RECIPE_LOADER,  // used by RecipeLoaderService, outdated
        AUTO_ASSOCIATE, // media handler facility
        OVERVIEW,       // draft changes overview page, verify intent 
        MERGE,          // merge draft to main
        STORE_IMPORT,   // store import tool
        ELSE            // unknown or unspecified
    }

    /**
     * Type designated to run mode of change request.
     */
    enum RunMode {
        NORMAL,         // changes are validated and saved
        DRY             // changes are validated only
    }

	/**
	 * Get the user executing the transaction.
	 *  
	 * @return user principal, never null
	 */
    UserI getUser();

    /**
     * Add a {@link ContentNodeI} modification to the request.
     * 
     * @param node content node to update
     */
    void addNode(ContentNodeI node);

    /**
     * Get content nodes that need to be updated with this request.
     * 
     * @return List of {@link ContentNodeI} instances (never null).
     */
    Collection<ContentNodeI> getNodes();

    /**
     * Specifies the source of request
     * 
     * @return source of request or ELSE (should not be null!)
     */
    Source getSource();

    /**
     * Return draft context of the request
     * @see {@link DraftContext}
     * 
     * @return actual draft or {@link DraftContext#MAIN}
     */
    DraftContext getDraftContext();

    /**
     * Specifies the run mode of request
     *
     * @return run mode
     */
    RunMode getRunMode();

    /**
     * Specifies run mode is dry
     *
     * @return run mode is dry
     */
    boolean isDryMode();
}

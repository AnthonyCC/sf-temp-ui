/*
 * Created on Dec 28, 2004
 *
 */
package com.freshdirect.cms.application;

import java.util.List;

import com.freshdirect.cms.ContentNodeI;


/**
 * Represents a transactional content modification request.
 * A request is a collection of {@link com.freshdirect.cms.ContentNodeI}
 * objects that need to be updated. It also has contextual information, such as
 * the {@link com.freshdirect.cms.application.UserI} executing the transaction.
 */
public interface CmsRequestI {
    
	/**
	 * Get the user executing the transaction.
	 *  
	 * @return user principal, never null
	 */
    public UserI getUser();

    /**
     * Add a {@link ContentNodeI} modification to the request.
     * 
     * @param node content node to update
     */
    public void addNode(ContentNodeI node);

    /**
     * Get content nodes that need to be updated with this request.
     * 
     * @return List of {@link ContentNodeI} instances (never null).
     */
    public List getNodes();

}

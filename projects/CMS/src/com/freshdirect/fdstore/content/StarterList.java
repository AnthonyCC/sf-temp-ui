package com.freshdirect.fdstore.content;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;


public class StarterList extends ContentNodeModelImpl implements ContentStatusI {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8629600147619071601L;
	
	private final ContentStatusHelper helper;

	/**
	 *  Constructor based on the content key.
	 *
	 *  @param cKey the content key of the node.
	 */
	public StarterList(ContentKey cKey) {
		super(cKey);
		helper = new ContentStatusHelper(this);
	}
	
	/**
	 *  Get the start date for the starter list.
	 *  
	 *  @return the start date of the starter list.
	 */
	public Date getStartDate() {
		return helper.getStartDate();
	}

	/**
	 *  Get the end date for the starter list.
	 *  
	 *  @return the end date of the starter list.
	 */
	public Date getEndDate() {
		return helper.getEndDate();
	}
	
	/**
	 *  Return the production status of the starter list.
	 *  
	 *  @return the production status as a string, a constant from
	 *          EnumProductionStatus
	 *  @see EnumProductionwStatus  
	 */
	public String getWorkflowStatus() {
		return getAttribute("productionStatus", EnumProductionStatus.PENDING);
	}
	
	/** Return if list is current and active.
	 * 
	 * @return whether list is available (current and active)
	 */
	public boolean isAvailable() {
	   return helper.isAvailable();
	}
	
	/**
	 * Return notes associated with starter list.
	 * 
	 * @return notes
	 */
	public String getNotes() {
		return getAttribute("notes","");
	}
	
	/** 
	 * Return full name of starter list
	 * @return full name of starter list
	 */
	public String getFullName() {
		return getAttribute("FULL_NAME","");
	}
	
	/**
	 * Return blurb.
	 * @return blurb
	 */
	public String getBlurb() {
		return getAttribute("BLURB","");
	}
	
	/**
	 * Return starter lists.
	 * 
	 * Starter lists are expected to reside under the "starterLists" folder.
	 * @param active whether only active lists are to be returned
	 * @return list of starter lists (List<StarterList>)
	 */
	public static List getStarterLists(boolean active) {
		
        List starterLists = new LinkedList();
        
		FDFolder folder = (FDFolder)ContentFactory.getInstance().getContentNode("starterLists");
		if (folder == null) return starterLists;
		getStarterLists(folder.getContentKey(),starterLists,active,true);
		
		return starterLists;
	}
	
	
	private List products = new LinkedList();
	/** Get products on starter list
	 * 
	 * @return list of products (List<ProductModel>)
	 */
	public List getListContents() {
		
		ContentNodeModelUtil.refreshModels(this, "listContents", products, false);
		
		return Collections.unmodifiableList(products);
	}
	
	
	/**
	 * Find all starter lists under a content node.
	 * 
	 * @param key parent's content key
	 * @param starterLists resulting list
	 * @param active whether only active
	 * @param recursive whether to recurse into child branches
	 */
	private static void getStarterLists(ContentKey key, List starterLists, boolean active, boolean recursive) {
		ContentNodeI node = CmsManager.getInstance().getContentNode(key);
		List children = (List) node.getAttribute("children").getValue();
		if (children == null) return;
	    for(Iterator i = children.iterator(); i.hasNext(); ) {
	    	ContentKey childKey = (ContentKey)i.next();
	    	if (childKey.getType().equals(FDContentTypes.STARTER_LIST)) {  
	    		StarterList slist = (StarterList)ContentFactory.getInstance().getContentNodeByKey(childKey);
	    		if (!active || slist.isAvailable()) starterLists.add(slist);
	    	} else if (recursive) {
	    		if (childKey.getType().equals(FDContentTypes.FDFOLDER)) {
		    		getStarterLists(childKey,starterLists,active,true);	    			
	    		}
	    	}
	    }
	}

}

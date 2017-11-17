package com.freshdirect.storeapi.content;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.storeapi.ContentNodeI;
import com.freshdirect.storeapi.application.CmsManager;


public class StarterList extends ContentNodeModelImpl implements ContentStatusI {

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
	@Override
    public Date getStartDate() {
		return helper.getStartDate();
	}

	/**
	 *  Get the end date for the starter list.
	 *
	 *  @return the end date of the starter list.
	 */
	@Override
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
	@Override
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
	@Override
    public String getFullName() {
		return getAttribute("FULL_NAME","");
	}

	/**
	 * Return blurb.
	 * @return blurb
	 */
	@Override
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
	public static List<StarterList> getStarterLists(boolean active) {

        List<StarterList> starterLists = new LinkedList<StarterList>();

		FDFolder folder = (FDFolder)ContentFactory.getInstance().getContentNode("starterLists");
		if (folder == null) return starterLists;
        getStarterLists(folder.getContentKey(), starterLists, active, true);

		return starterLists;
	}


	private List<ProductModel> products = new LinkedList<ProductModel>();

	/** Get products on starter list
	 *
	 * @return list of products (List<ProductModel>)
	 */
	public List<ProductModel> getListContents() {

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
    private static void getStarterLists(ContentKey key, List<StarterList> starterLists, boolean active, boolean recursive) {
        ContentNodeI node = CmsManager.getInstance().getContentNode(key);
		List<ContentKey> children = (List<ContentKey>) node.getAttributeValue("children");

		if (children == null) return;
		for (ContentKey childKey : children) {
	    	if (childKey.type == ContentType.StarterList) {
	    		StarterList slist = (StarterList)ContentFactory.getInstance().getContentNodeByKey(childKey);
	    		if (!active || slist.isAvailable()) starterLists.add(slist);
	    	} else if (recursive) {
	    		if (childKey.type == ContentType.FDFolder) {
                    getStarterLists(childKey, starterLists, active, true);
	    		}
	    	}
	    }
	}

}

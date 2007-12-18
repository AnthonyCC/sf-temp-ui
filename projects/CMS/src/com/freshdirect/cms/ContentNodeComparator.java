/**
 * @author ekracoff
 * Created on Mar 23, 2005*/

package com.freshdirect.cms;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.freshdirect.cms.fdstore.FDContentTypes;

/**
 * Comparator for ordering {@link com.freshdirect.cms.ContentNodeI} instances
 * for display. Orders by a priority assigned to the content type then
 * alphabetically based on the label.
 * 
 * @TODO refactor as a service where ContentTypes can register themselves
 */
public class ContentNodeComparator implements Comparator {
	
	private final static Map DEFAULT_ORDER = new HashMap();
	static{
		DEFAULT_ORDER.put(FDContentTypes.STORE, new Integer(8));
		DEFAULT_ORDER.put(FDContentTypes.DEPARTMENT, new Integer(7));
		DEFAULT_ORDER.put(FDContentTypes.FDFOLDER, new Integer(6));
		DEFAULT_ORDER.put(FDContentTypes.CATEGORY, new Integer(5));
		DEFAULT_ORDER.put(FDContentTypes.PRODUCT, new Integer(4));
		DEFAULT_ORDER.put(FDContentTypes.SKU, new Integer(3));
		DEFAULT_ORDER.put(FDContentTypes.MEDIAFOLDER, new Integer(2));
		DEFAULT_ORDER.put(FDContentTypes.IMAGE, new Integer(1));
		DEFAULT_ORDER.put(FDContentTypes.HTML, new Integer(1));
	}
	
	
	public final static ContentNodeComparator DEFAULT = new ContentNodeComparator(DEFAULT_ORDER);
	
	private final Map order;
	
	/**
	 * @param order Map of ContentType -> Integer
	 */
	public ContentNodeComparator(Map order){
		super();
		this.order = order;
	}

	public int compare(Object o1, Object o2) {
		ContentNodeI node1 = (ContentNodeI) o1;
		ContentNodeI node2 = (ContentNodeI) o2;
		
		ContentKey key1 = node1.getKey();
		ContentKey key2 = node2.getKey();
		
		int node1Priority = order.get(key1.getType()) == null ? 0 : ((Integer)order.get(key1.getType())).intValue();
		int node2Priority = order.get(key2.getType()) == null ? 0 : ((Integer)order.get(key2.getType())).intValue();
		
		int i =  node2Priority - node1Priority;
		
		if(i == 0){
			i = node1.getLabel().toLowerCase().compareTo(node2.getLabel().toLowerCase());
		}

		return i; 
	}
	
	
}

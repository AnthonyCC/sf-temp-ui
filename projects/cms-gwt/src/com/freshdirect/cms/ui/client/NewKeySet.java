package com.freshdirect.cms.ui.client;

import java.util.Collection;
import java.util.HashSet;

/**
 * NewKeySet temporarily stores content keys just created in New Content ID Window.
 * 
 * Thus subsequent node creation operations can simply ensure entered content keys
 * are already allocated or assigned to new content nodes waiting for save
 * in {@link WorkingSet} collection.
 *  
 * Keys will be dropped when NewContentNode popup is closed no matter node is created
 * or disposed.
 *
 * @author segabor
 *
 */
public class NewKeySet {
	private static Collection<String> keySet = new HashSet<String>();

	public static boolean add(String contentKey) {
		CmsGwt.debug("NewKeySet: Adding key " + contentKey);
		boolean flag = keySet.add(contentKey);
		CmsGwt.debug("NewKeySet("+keySet.size()+"): " + keySet);
		return flag;
	}
	
	public static boolean remove(String contentKey) {
		CmsGwt.debug("NewKeySet: Removing key " + contentKey);
		boolean flag = keySet.remove(contentKey);
		CmsGwt.debug("NewKeySet("+keySet.size()+"): " + keySet);
		return flag;
	}
	
	public static boolean contains(String contentKey) {
		final boolean flag = keySet.contains(contentKey);
		CmsGwt.debug("NewKeySet: contains key " + contentKey + " = "  + flag);
		CmsGwt.debug("NewKeySet("+keySet.size()+"): " + keySet);
		return flag;
	}

	public static void clear() {
		CmsGwt.debug("NewKeySet: clear set");
		keySet.clear();
	}
}

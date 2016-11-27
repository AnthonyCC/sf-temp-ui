package com.freshdirect.cms.ui.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ui.model.GwtContentNode;

/**
 *	Static singleton class for working sets.
 *  Changed nodes are collected in an internal collection.
 *	Ensures that the internal collection contains no duplicates of nodes. (with equal content keys) 
 * 
 * @author treer
 */

public class WorkingSet {
	
	private static Collection<GwtContentNode> workingset = new ArrayList<GwtContentNode>();
	
    public static Collection<GwtContentNode> getWorkingSet() {
        return workingset;
    }
    
    public static void add ( GwtContentNode node ) {
    	if ( node != null ) {
	    	remove( node.getKey() );
	    	workingset.add( node );
    	}
    }
    
    public static void addAll ( Collection<GwtContentNode> nodes ) {
		if ( nodes != null ) {
			for ( GwtContentNode node : nodes ) {
				add( node );
			}
		}
	}
    
    public static void remove ( String contentKey ) {
    	if ( contentKey != null && !contentKey.trim().equals( "" ) ) {
    		Iterator<GwtContentNode> it = workingset.iterator();
	    	while ( it.hasNext() ) {
	    		GwtContentNode node = it.next();
	    		if ( node != null && node.getKey().equals( contentKey ) ) {
	    			it.remove();
	    		}
	    	}
    	}
    }

    public static void remove ( GwtContentNode node ) {
    	if ( node != null ) {
    		remove( node.getKey() );
    	}
    }
    
    public static GwtContentNode get ( String contentKey ) {
    	if ( contentKey != null && !contentKey.trim().equals( "" ) ) {
    		for ( GwtContentNode node : workingset ) {
	    		if ( node != null && node.getKey().equals( contentKey ) ) {
	    			return node;
	    		}
	    	}
    	}    	
    	return null;
    }
    
    public static boolean isEmpty() {
    	return workingset.isEmpty();
    }
    
    public static void clear() {
    	workingset.clear();
    }


    /**
     * Ensures if node with the given content key exists in the working set
     * @param contentKey String representation of content key (ie. "type:id")
     * @return
     * 
     * @see ContentKey
     */
    public static boolean containsNodeWithKey(String contentKey) {
    	if (contentKey == null) {
    		return false;
    	}

    	for (GwtContentNode node : workingset) {
    		if (node.getKey().equals(contentKey)) {
    			return true;
    		}
    	}
    	
    	return false;
    }


    public static Set<String> getKeys() {
    	Set<String> keySet = new HashSet<String>(workingset.size());
    	for (GwtContentNode node : workingset) {
    		keySet.add(node.getKey());
    	}
    	return keySet;
    }
}

package com.freshdirect.cms.ui.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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
    
    public static void clear() {
    	workingset.clear();
    }
    
}

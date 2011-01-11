package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class YmalSetSourceUtil {
	
	private static final Random rnd = new Random();
	private synchronized static final int nextInt(int n) {
		return rnd.nextInt(n);
	}

	
	public static YmalSet findActiveYmalSet(YmalSetSource src) {
		
		List<YmalSet> ymalSets;		
		List<YmalSet> activeSets = new ArrayList<YmalSet>();

		while ( src != null ) {
			
			ymalSets = src.getYmalSets();		

			// collect active ymal sets
			for ( YmalSet ymalSet : ymalSets ) {
				if ( ymalSet.isActive() ) {
					activeSets.add( ymalSet );
				}
			}

			// we have found at least one active ymal set
			if ( activeSets.size() != 0 ) {
				break;
			}
			
			// no active sets found in source object -> find subsequent source and repeat
			src = src.getParentYmalSetSource();
		}

		// return one random item from the active sets
		return activeSets.size() > 0 ? activeSets.get( nextInt( activeSets.size() ) ) : null;
	}

	
	
	
	// ==================================================================
	// Default implementations of interface methods :
	// 
	// They are here to provide a common implementation in one place.
	// ==================================================================
	
	 
	protected static List<YmalSet> getYmalSets( ContentNodeModel thisPointer, List<YmalSet> ymalSets ) {
		ContentNodeModelUtil.refreshModels((ContentNodeModelImpl)thisPointer, "ymalSets", ymalSets, false, true);
		return Collections.unmodifiableList(ymalSets);
	}
	
	protected static boolean hasActiveYmalSets( ContentNodeModel thisPointer, List<YmalSet> ymalSets ) {
		for (YmalSet ymal : getYmalSets( thisPointer, ymalSets )) {
			if (ymal.isActive())
				return true;
		}
		return false;
	}

	protected static YmalSetSource getParentYmalSetSource( ContentNodeModel thisPointer ) {
		// DEFAULT BEHAVIOUR
		ContentNodeModel p = thisPointer.getParentNode();
		while (p instanceof YmalSetSource) {
			YmalSetSource ymSS = (YmalSetSource) p;
			
			if (ymSS.hasActiveYmalSets())
				return ymSS;

			// climb up
			p = p.getParentNode();
		}

		return null;
	}

}

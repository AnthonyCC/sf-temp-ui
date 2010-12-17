package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class YmalSetSourceUtil {
	private static final Random rnd = new Random();
	private synchronized static final int nextInt(int n) {
		return rnd.nextInt(n);
	}
	
	public static YmalSet findActiveYmalSet(YmalSetSource src) {
		List<YmalSet> ymalSets = src.getYmalSets();
		if (ymalSets == null)
			return null;
		
		List<YmalSet> activeSets = new ArrayList<YmalSet>(ymalSets.size());

		while (activeSets.size() == 0 && src != null) {
			// collect active ymal sets
			for (YmalSet ymalSet : ymalSets) {
				if (ymalSet.isActive()) {
					activeSets.add(ymalSet);
				}
			}

			// no active sets found in source object -> find subsequent source
			if (activeSets.size() == 0) {
				src = src.getParentYmalSetSource();
				if (src != null) {
					activeSets = new ArrayList<YmalSet>(ymalSets.size());
				}
			}
		}

		return activeSets.size() > 0 ?
				activeSets.get(nextInt(activeSets.size()))
				:
				null;
	}
}

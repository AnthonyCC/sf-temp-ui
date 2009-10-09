package com.freshdirect.webapp.util;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.Recommendations;

public class RecommendationsCache {
	/**
	 * Simple queue to keep at most 'size' amount of page impression IDs.
	 * At new arrivals it drops the oldest entry and puts the new one to the top of queue.
	 */
	ArrayDeque<String> keys = new ArrayDeque<String>();
	
	/**
	 * Number of maximum elements to keep in cache
	 */
	int	size = 3;
	
	/** page impression->map of variant ID -> recommendation */
	Map<String,Map<String,Recommendations>> imp2rec = new HashMap<String,Map<String,Recommendations>>();
	
	
	protected RecommendationsCache() {
	}


	public static RecommendationsCache getCache(HttpSession session) {
		RecommendationsCache sharedInstance = null;
		
		synchronized(session) {
			sharedInstance = (RecommendationsCache) session.getAttribute("RCache");
			if (sharedInstance == null) {
				sharedInstance = new RecommendationsCache();
				session.setAttribute("RCache", sharedInstance);
			}
		}

		return sharedInstance;
	}
	
	/**
	 * Returns cached recommendation or null if not cached yet
	 * 
	 * @param pageImpressionId Page impression ID
	 * @param v Variant
	 * 
	 * @return
	 */
	public Recommendations get(String pageImpressionId, Variant v) {
		if (imp2rec.keySet().contains(pageImpressionId)) {
			return imp2rec.get(pageImpressionId).get(v.getId());
		}
		
		return null;
	}

	public void store(String pageImpressionId, Variant v, Recommendations r) {
		if (imp2rec.keySet().contains(pageImpressionId)) {
			// simply store recommendation by overwriting an older if any
			imp2rec.get(pageImpressionId).put(v.getId(), r);
		} else {
			// key not exists yet
			
			// remove eldest entries until less than 3 remain in the queue
			while (keys.size() >= 3) {
				String pId = keys.removeFirst();
				imp2rec.remove(pId);
			}

			keys.push(pageImpressionId);
			
			// push in the new entry
			Map<String,Recommendations> m = new HashMap<String,Recommendations>();
			m.put(v.getId(), r);
			
			imp2rec.put(pageImpressionId, m);
		}
	}
}

package com.freshdirect.cms.context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;

/**
 * PHSWalker helps {@link com.freshdirect.cms.ui.tapestry.component.PrimaryHomeSelection} component
 * by filtering off invisible parents from candidate primary homes.
 * 
 * @author segabor
 */
public class PHSWalker extends ContextWalker {
	public PHSWalker(ContextualContentNodeI node) {
		super(node);
	}

	/**
	 * Walk down the path and guess it is visible as primary home.
	 * (NOT invisible)
	 */
	public boolean walkAndTest() {
		super.walk(); return !getTestResult();
	}

	/**
	 * Walker stops at once when test succeeded (found invisible node).
	 */
	public boolean shouldStop(ContentNodeI n) {
		return testResult; // stop if test was positive
	}

	/**
	 * Test actual node against hidden state.
	 * Test is TRUE <=> node IS HIDDEN
	 */
	public boolean test(ContentNodeI n) {
		Map attrs = n.getAttributes();
		AttributeI a_hu = (AttributeI) attrs.get("HIDE_URL");

		return ( (a_hu != null && a_hu.getValue() != null));
	}
	
	public ContentKey getLeafKey() {
		return leaf.getKey();
	}
	
	// returns the parent (category) key of leaf node (product)
	public ContentKey getHomeKey() {
		ContextualContentNodeI parentNode = leaf.getParentNode();
		return parentNode != null ? parentNode.getKey(): null;
	}



	/**
	 * Utility method used by {@link com.freshdirect.cms.ui.tapestry.component.PrimaryHomeSelection}
     * Returns the list of content keys of valid parents.
     *
	 * @param it	Iterator that iterates on set of Contexts 
	 * @param svc	Instance of ContextService
	 * @return List<ContentKey> of content keys
	 */
	public static List getVisibleParents(Iterator it, ContextService svc) {
		List parentKeys = new ArrayList();
		while (it.hasNext()) {
			Context ctx = (Context) it.next();

			if (ctx.isRoot()) // [MNT-380] avoid crash with NPE
				continue;
			
			PHSWalker walker = new PHSWalker(svc.getContextualizedContentNode(ctx));
			if (walker.walkAndTest()) {
				ContentKey homeKey = walker.getHomeKey();
				if (homeKey != null)
					parentKeys.add(homeKey);
			}
		}
		return parentKeys;
	}
}

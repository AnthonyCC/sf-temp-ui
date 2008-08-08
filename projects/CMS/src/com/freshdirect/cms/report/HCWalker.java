package com.freshdirect.cms.report;

import java.util.List;
import java.util.Map;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.context.ContextWalker;
import com.freshdirect.cms.context.ContextualContentNodeI;
import com.freshdirect.cms.fdstore.FDContentTypes;

public class HCWalker extends ContextWalker {
	private List cache;
	
	public HCWalker(ContextualContentNodeI node, List dirtyCache) {
		super(node);
		this.cache = dirtyCache;
	}
	
	public HCWalker walkWithMe() {
		super.walk(); return this;
	}
	
	public boolean shouldStop(ContentNodeI n) {
		// stop if found a hidden / not searchable category or reached the end of context chain.
		return testResult ||  FDContentTypes.DEPARTMENT.equals(n.getDefinition().getType());
	}

	// returns true if node is hidden OR NOT searchable
	public boolean test(ContentNodeI n) {
		// lookup in cache
		if (cache.contains(n)) {
			return true;
		}

		Map attrs = n.getAttributes();
		AttributeI a_hu = (AttributeI) attrs.get("HIDE_URL");
		AttributeI a_ns = (AttributeI) attrs.get("NOT_SEARCHABLE");

		boolean flag = ( (a_hu != null && a_hu.getValue() != null) ||
			(a_ns != null && Boolean.TRUE.equals(a_ns.getValue()) )
		);
		
		// store in dirty cache on positive test case
		if (flag) {
			cache.add(n);
		}
		
		return flag;
	}

	public boolean isValid() {
		return !testResult;
	}
}

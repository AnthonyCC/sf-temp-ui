/*
 * Created on Apr 6, 2005
 */
package com.freshdirect.cms.ui.tapestry.component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.context.Context;
import com.freshdirect.cms.context.ContextService;
import com.freshdirect.cms.context.ContextWalker;
import com.freshdirect.cms.context.ContextualContentNodeI;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.ui.tapestry.ContentKeySelectionModel;

/**
 * @author vszathmary
 */
public abstract class PrimaryHomeSelection extends BaseComponent {

	public IPropertySelectionModel getSelectionModel() {
		ContentNodeI node = getAttribute().getContentNode();

		Set parentKeys = new HashSet();

		List ctxs = new ArrayList(ContextService.getInstance().getAllContextsOf(node.getKey()));
		ContextService svc = ContextService.getInstance();
		for (Iterator it=ctxs.iterator(); it.hasNext(); ) {
			Context ctx = (Context) it.next();
			PHSWalker walker = new PHSWalker(svc.getContextualizedContentNode(ctx));
			if (walker.walkAndTest()) {
				parentKeys.add(walker.getHomeKey());
			}
		}
		
		ContentNodeI ctxNode = getContextNode();
		if (ctxNode != null) {
			parentKeys.add(ctxNode.getKey());
		}

		ContentKey[] keys = (ContentKey[]) parentKeys.toArray(new ContentKey[parentKeys.size()]);
		return new ContentKeySelectionModel(keys, true, true);
	}

	public abstract ContextualContentNodeI getContextNode();

	public abstract AttributeI getAttribute();

}




class PHSWalker extends ContextWalker {
	public PHSWalker(ContextualContentNodeI node) {
		super(node);
	}

	public boolean walkAndTest() {
		super.walk(); return !getTestResult();
	}

	public boolean shouldStop(ContentNodeI n) {
		return testResult; // stop if test was positive
	}

	public boolean test(ContentNodeI n) {
		Map attrs = n.getAttributes();
		AttributeI a_hu = (AttributeI) attrs.get("HIDE_URL");
		// AttributeI a_ns = (AttributeI) attrs.get("NOT_SEARCHABLE");

		return ( (a_hu != null && a_hu.getValue() != null)
				/* || (a_ns != null && Boolean.TRUE.equals(a_ns.getValue())  ) */
		);
	}
	
	public ContentKey getLeafKey() {
		return leaf.getKey();
	}
	
	// returns the parent (category) key of leaf node (product)
	public ContentKey getHomeKey() {
		return leaf.getParentNode().getKey();
	}
}

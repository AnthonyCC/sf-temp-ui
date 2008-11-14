/*
 * Created on Apr 6, 2005
 */
package com.freshdirect.cms.ui.tapestry.component;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.context.ContextService;
import com.freshdirect.cms.context.ContextualContentNodeI;
import com.freshdirect.cms.context.PHSWalker;
import com.freshdirect.cms.ui.tapestry.ContentKeySelectionModel;

/**
 * @author vszathmary
 */
public abstract class PrimaryHomeSelection extends BaseComponent {

	public IPropertySelectionModel getSelectionModel() {
		ContentNodeI node = getAttribute().getContentNode();

		List ctxs = new ArrayList(ContextService.getInstance().getAllContextsOf(node.getKey()));
		List parentKeys = PHSWalker.getVisibleParents(ctxs.iterator(), ContextService.getInstance());

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

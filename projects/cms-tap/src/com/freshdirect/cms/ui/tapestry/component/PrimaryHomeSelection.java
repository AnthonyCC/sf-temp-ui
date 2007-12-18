/*
 * Created on Apr 6, 2005
 */
package com.freshdirect.cms.ui.tapestry.component;

import java.util.HashSet;
import java.util.Set;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.form.IPropertySelectionModel;

import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.context.ContextualContentNodeI;
import com.freshdirect.cms.ui.tapestry.ContentKeySelectionModel;

/**
 * @author vszathmary
 */
public abstract class PrimaryHomeSelection extends BaseComponent {

	public IPropertySelectionModel getSelectionModel() {
		ContentNodeI node = getAttribute().getContentNode();

		Set parentKeys = new HashSet();
		//parentKeys.add(null);
		parentKeys.addAll(CmsManager.getInstance().getParentKeys(node.getKey()));
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

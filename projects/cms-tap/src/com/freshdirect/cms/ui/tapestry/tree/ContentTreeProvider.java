/*
 * Created on Mar 8, 2005
 */
package com.freshdirect.cms.ui.tapestry.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.tacos.model.ITreeContentProvider;

import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.service.query.CmsQueryTypes;

/**
 * @author vszathmary
 */
public class ContentTreeProvider implements ITreeContentProvider {

	private final Set rootKeys;

	public ContentTreeProvider(Set rootKeys) {
		this.rootKeys = rootKeys;
	}

	public List getElements() {
		return new ArrayList(getNodes(rootKeys));
	}

	public Collection getChildren(Object parentElement) {
		ContentNodeI parentNode = (ContentNodeI) parentElement;
		return getNodes(parentNode.getChildKeys());
	}

	private Collection getNodes(Set childKeys) {
		Map childNodes = CmsManager.getInstance().getContentNodes(childKeys);
		return childNodes.values();
	}

	public Object getParent(Object childElement) {
		ContentNodeI childNode = (ContentNodeI) childElement;
		Set parentKeys = CmsManager.getInstance().getParentKeys(childNode.getKey());
		Map parentNodes = CmsManager.getInstance().getContentNodes(parentKeys);
		return parentNodes.isEmpty() ? null : parentNodes.values().iterator().next();
	}

	public boolean hasChildren(Object parentElement) {
		ContentNodeI parentNode = (ContentNodeI) parentElement;
		ContentType type = parentNode.getKey().getType();
		if (type.equals(CmsQueryTypes.QUERY)) {
			return true;
		}
		return !parentNode.getChildKeys().isEmpty();
	}

}
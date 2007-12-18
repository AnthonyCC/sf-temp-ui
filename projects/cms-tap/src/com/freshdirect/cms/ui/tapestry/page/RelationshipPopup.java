package com.freshdirect.cms.ui.tapestry.page;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.RelationshipI;
import com.freshdirect.cms.node.ContentNodeUtil;
import com.freshdirect.cms.ui.tapestry.CmsVisit;

public abstract class RelationshipPopup extends BasePopupPage implements IExternalPage, PageDetachListener {

	public void activateExternalPage(Object[] parameters, IRequestCycle cycle) {
		setOpenerPageName((String) parameters[0]);
		setParentKey((ContentKey) parameters[1]);
		setAttributeName((String) parameters[2]);
		refreshParent = false;
	}

	public ContentNodeI getParentNode() {
		CmsVisit visit = (CmsVisit) getVisit();
		return visit.getWorkingSet(getParentKey()).getContentNode(getParentKey());
	}

	public RelationshipDefI getRelationshipDef() {
		return (RelationshipDefI) getParentNode().getAttribute(getAttributeName()).getDefinition();
	}

	private boolean refreshParent = false;

	public boolean isRefreshParent() {
		return refreshParent;
	}

	public void selectKey(IRequestCycle cycle) {
		ContentKey selectedKey = (ContentKey) cycle.getServiceParameters()[0];
		if (selectedKey != null) {

			RelationshipI rel = (RelationshipI) getParentNode().getAttribute(getAttributeName());
			boolean added = ContentNodeUtil.addRelationshipKey(rel, selectedKey);

			refreshParent = added;
		}
	}

	public void pageDetached(PageEvent event) {
		refreshParent = false;
	}

	public abstract String getAttributeName();

	public abstract void setAttributeName(String attributeName);

	public abstract ContentKey getParentKey();

	public abstract void setParentKey(ContentKey key);

}
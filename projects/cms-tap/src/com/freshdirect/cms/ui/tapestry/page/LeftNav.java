package com.freshdirect.cms.ui.tapestry.page;

import net.sf.tacos.ajax.components.tree.ITreeManager;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ui.tapestry.component.ContentTree;

public class LeftNav extends BasePage implements IExternalPage {

	public void activateExternalPage(Object[] params, IRequestCycle cycle) {
		ContentKey key = (ContentKey) params[0];
		ContentNodeI node = key.lookupContentNode();

		ContentTree tree = (ContentTree) getComponent("contentTree");
		ITreeManager mgr = tree.getTreeManager();

		tree.setSearchQuery(null);
		mgr.collapseAll();
		mgr.reveal(node);
		mgr.setExpanded(node, true);
	}

}

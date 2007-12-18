package com.freshdirect.cms.ui.tapestry.page;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.changecontrol.ChangeLogServiceI;
import com.freshdirect.cms.changecontrol.ChangeSet;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.framework.conf.FDRegistry;

public abstract class ContentHistory extends BasePage implements IExternalPage {
	private ChangeLogServiceI service = (ChangeLogServiceI) FDRegistry.getInstance().getService(ChangeLogServiceI.class);

	public void activateExternalPage(Object[] params, IRequestCycle cycle) {
		ContentKey key = (ContentKey) params[0];
		setContentKey(key);
	}

	public boolean isShowVersions() {
		ContentType t = getContentKey().getType();
		return t.equals(FDContentTypes.IMAGE) || t.equals(FDContentTypes.HTML);
	}

	public List getChanges() {
		List history = service.getChangeHistory(getContentKey());
		List changes = new ArrayList();
		for (Iterator i = history.iterator(); i.hasNext();) {
			ChangeSet cn = (ChangeSet) i.next();
			changes.addAll(cn.getNodeChanges());
		}
		return changes;
	}

	public abstract ContentKey getContentKey();

	public abstract void setContentKey(ContentKey contentKey);

}

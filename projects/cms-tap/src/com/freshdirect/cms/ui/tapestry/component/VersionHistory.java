package com.freshdirect.cms.ui.tapestry.component;

import java.util.Collections;
import java.util.List;

import org.apache.tapestry.BaseComponent;

import com.freshdirect.cms.CmsRuntimeException;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.service.WebDavServiceI;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.framework.conf.FDRegistry;

public abstract class VersionHistory extends BaseComponent {

	public List getVersionHistory() {
		try {
			if (!FDRegistry.getInstance().containsService(WebDavServiceI.class)) {
				return Collections.EMPTY_LIST;
			}
			WebDavServiceI service = (WebDavServiceI) FDRegistry.getInstance().getService(WebDavServiceI.class);
			return service.getVersionHistory(getContentKey().lookupContentNode());
		} catch (CmsRuntimeException e) {
			e.printStackTrace();
			return Collections.EMPTY_LIST;
		}
	}

	public String getPopupFeatures() {
		ContentNodeI node = getContentKey().lookupContentNode();
		String features = "toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=1";
		if (node.getKey().getType().equals(FDContentTypes.IMAGE)) {
			features = features
				+ ",height="
				+ node.getAttribute("height").getValue()
				+ ",width="
				+ node.getAttribute("width").getValue();
		}
		return features;
	}

	public abstract ContentKey getContentKey();

}

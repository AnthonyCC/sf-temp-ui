package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.ContentKey;

public class FDFolder extends ContentNodeModelImpl {
	List<ContentNodeModel> children = new ArrayList<ContentNodeModel>();

	public FDFolder(ContentKey cKey) {
		super(cKey);
	}

	public List<ContentNodeModel> getChildren() {
		ContentNodeModelUtil.refreshModels(this, "children", children, true);
		return new ArrayList<ContentNodeModel>(children);
	}
}

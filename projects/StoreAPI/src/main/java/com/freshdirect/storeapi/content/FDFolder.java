package com.freshdirect.storeapi.content;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.core.domain.ContentKey;

public class FDFolder extends ContentNodeModelImpl {
	List<ContentNodeModel> children = new ArrayList<ContentNodeModel>();

    public FDFolder(ContentKey key) {
        super(key);
    }

	public List<ContentNodeModel> getChildren() {
		ContentNodeModelUtil.refreshModels(this, "children", children, true);
		return new ArrayList<ContentNodeModel>(children);
	}
}

package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;

public class GlobalNavigationModel extends ContentNodeModelImpl {

    private final List<ContentNodeModel> items = new ArrayList<ContentNodeModel>(); //may contain SuperDepartmentModel and DepartmentModel

    public GlobalNavigationModel(ContentKey key) {
		super(key);
	}

	public Html getMedia() {
		return FDAttributeFactory.constructHtml(this, "media");
	}

    public List<ContentNodeModel> getItems() {
        ContentNodeModelUtil.refreshModels(this, "items", items, false, false);
        return new ArrayList<ContentNodeModel>(items);
    }

}

package com.freshdirect.storeapi.content;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.storeapi.attributes.FDAttributeFactory;

public class GlobalNavigationModel extends ContentNodeModelImpl {

    private final List<ContentNodeModel> items = new ArrayList<ContentNodeModel>(); //may contain SuperDepartmentModel and DepartmentModel

    public GlobalNavigationModel(ContentKey key) {
        super(key);
    }

	public Html getMedia() {
		return FDAttributeFactory.constructHtml(this, "media");
	}

    public List<ContentNodeModel> getItems() {
        ContentNodeModelUtil.refreshModels(this, "items", items, false);
        return new ArrayList<ContentNodeModel>(items);
    }

}

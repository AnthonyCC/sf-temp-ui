package com.freshdirect.storeapi.content;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.core.domain.ContentKey;

public class CategorySectionModel extends ContentNodeModelImpl {

	private final List<CategoryModel> selectedCategories = new ArrayList<CategoryModel>();

    public CategorySectionModel(ContentKey key) {
        super(key);
    }

	public String getHeadline() {
		return (String) getCmsAttributeValue("headline");
	}

	public Boolean isLastSectionPerColumn() {
		return (Boolean) getCmsAttributeValue("insertColumnBreak");
	}

	public List<CategoryModel> getSelectedCategories() {
		ContentNodeModelUtil.refreshModels(this, "selectedCategories", selectedCategories, false);
		return new ArrayList<CategoryModel>(selectedCategories);
	}
}

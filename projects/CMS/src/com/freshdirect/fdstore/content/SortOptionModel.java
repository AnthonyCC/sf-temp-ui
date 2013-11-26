package com.freshdirect.fdstore.content;

import com.freshdirect.cms.ContentKey;

public class SortOptionModel extends ContentNodeModelImpl {

	
	public SortOptionModel(ContentKey key) {
		super(key);
	}

	public String getLabel() {
		return (String) getCmsAttributeValue("label");
	}

	public String getSelectedLabel1() {
		return (String) getCmsAttributeValue("selectedLabel1");
	}

	public String getSelectedLabel2() {
		return (String) getCmsAttributeValue("getSelectedLabel1");
	}

	public String getStrategy() {
		return (String) getCmsAttributeValue("strategy");
	}

}

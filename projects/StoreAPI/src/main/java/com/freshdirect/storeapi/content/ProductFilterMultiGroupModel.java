package com.freshdirect.storeapi.content;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.storeapi.attributes.FDAttributeFactory;

public class ProductFilterMultiGroupModel extends ContentNodeModelImpl {

    public ProductFilterMultiGroupModel(ContentKey key) {
        super(key);
    }

	public TagModel getRootTag() {
		return FDAttributeFactory.lookup(this, "rootTag", null);
	}

	public String getLevel1Name() {
		return (String) getCmsAttributeValue("level1Name");
	}

	public String getLevel1Type() {
		return (String) getCmsAttributeValue("level1Type");
	}

	public String getLevel1AllSelectedLabel() {
		return (String) getCmsAttributeValue("level1AllSelectedLabel");
	}

	public String getLevel2Name() {
		return (String) getCmsAttributeValue("level2Name");
	}

	public String getLevel2Type() {
		return (String) getCmsAttributeValue("level2Type");
	}

	public String getLevel2AllSelectedLabel() {
		return (String) getCmsAttributeValue("level2AllSelectedLabel");
	}
}

package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.ContentKey;

public class TagModel extends ContentNodeModelImpl {

	private List<TagModel> children = new ArrayList<TagModel>();
	
	public TagModel(ContentKey key) {
		super(key);
	}

	public String getName() {
		return (String) getCmsAttributeValue("name");
	}
	
	public List<TagModel> getChildren() {
		ContentNodeModelUtil.refreshModels(this, "children", children, true);
		return new ArrayList<TagModel>(children);
	}
}

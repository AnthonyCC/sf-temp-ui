package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.ContentKey;

public class FavoriteList extends ContentNodeModelImpl {
	private static final long serialVersionUID = 1205090955009726825L;

	private final List items = new ArrayList();
	
	public FavoriteList(ContentKey key) {
		super(key);
	}

	public String getFullName() {
		return getAttribute("full_name", getContentName());
	}
		
	public List getItems() {
		ContentNodeModelUtil.refreshModels(this, "items", items, false);

		return new ArrayList(items);
	}
	
}

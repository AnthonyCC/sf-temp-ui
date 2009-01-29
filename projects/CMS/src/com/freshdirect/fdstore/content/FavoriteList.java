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

	public List getFavoriteItems() {
		ContentNodeModelUtil.refreshModels(this, "favoriteItems", items, false);

		return new ArrayList(items);
	}

	/**
	 * The items relationship has been renamed to favoriteItems due to
	 * unresolvable DB query inconsistencies (see CMS.ALL_NODES materialized
	 * view)
	 * 
	 * @deprecated
	 * @return
	 */
	public List getItems() {
		return getFavoriteItems();
	}
}

package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;

public class Tile extends ContentNodeModelImpl {
	List<ContentNodeModel> quickbuy = new ArrayList<ContentNodeModel>();

	public Tile(ContentKey key) {
		super(key);
	}

	public boolean isGoesGreatWith() {
		return getAttribute("GGF_TYPE", false);
	}

	public MediaModel getMedia() {
		return (MediaModel) FDAttributeFactory.constructWrapperValue(this, "media");
	}

	public List<ContentNodeModel> getQuickbuyItems() {
		ContentNodeModelUtil.refreshModels(this, "quick_buy", quickbuy, false);
		return new ArrayList<ContentNodeModel>(quickbuy);
	}

	public boolean isQuickbuyTile() {
		return getQuickbuyItems().size() > 0;
	}
}

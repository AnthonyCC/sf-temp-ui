package com.freshdirect.storeapi.content;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.storeapi.attributes.FDAttributeFactory;

public class Tile extends ContentNodeModelImpl {
	List<ContentNodeModel> quickbuy = new ArrayList<ContentNodeModel>();

    public Tile(ContentKey cKey) {
        super(cKey);
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

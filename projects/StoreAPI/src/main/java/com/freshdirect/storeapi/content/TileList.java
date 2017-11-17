package com.freshdirect.storeapi.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.freshdirect.cms.core.domain.ContentKey;

public class TileList extends ContentNodeModelImpl {
    private List<Tile> tiles = new ArrayList<Tile>();
    private List<ContentNodeModel> filter = new ArrayList<ContentNodeModel>();

    public TileList(ContentKey cKey) {
        super(cKey);
    }

    public List<ContentNodeModel> getFilter() {
        ContentNodeModelUtil.refreshModels(this, "filter", filter, false);
        return Collections.unmodifiableList(filter);
    }

    @SuppressWarnings("unchecked")
	public List<ContentKey> getFilterKeys() {
        return (List<ContentKey>) getCmsAttributeValue("filter");
    }

    public List<Tile> getTiles() {
        ContentNodeModelUtil.refreshModels(this, "tiles", tiles, false);
        return new ArrayList<Tile>(tiles);
    }
}

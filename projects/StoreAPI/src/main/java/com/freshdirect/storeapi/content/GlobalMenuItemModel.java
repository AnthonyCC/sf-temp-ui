package com.freshdirect.storeapi.content;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.storeapi.attributes.FDAttributeFactory;

public class GlobalMenuItemModel extends ContentNodeModelImpl {
	public final static String DEFAULT_MENU_FOLDER = "FDFolder:globalMenu";
	public final static String TITLE_LABEL_NEW_LINE = "<br\\s*/*>";

	private List<GlobalMenuSectionModel> subSections = new ArrayList<GlobalMenuSectionModel>();

    public GlobalMenuItemModel(ContentKey key) {
        super(key);
    }

	public List<GlobalMenuSectionModel> getSubSections() {
		ContentNodeModelUtil.refreshModels(this, "subSections", subSections, true);
		return new ArrayList<GlobalMenuSectionModel>(subSections);
	}

	@Override
    public Html getEditorial() {
		return FDAttributeFactory.constructHtml(this, "editorial");
	}

	public int getLayout() {
		return getAttribute("LAYOUT", 0);
	}

	public String getTitleLabel() {
		return getAttribute("TITLE_LABEL", "Unknown title");
	}

	public String[] getTitleLabelRows(){
		 return getTitleLabel().split(TITLE_LABEL_NEW_LINE);
	}
}

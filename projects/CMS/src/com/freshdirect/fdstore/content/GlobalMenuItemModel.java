package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;

public class GlobalMenuItemModel extends ContentNodeModelImpl {
	public final static String DEFAULT_MENU_FOLDER = "FDFolder:globalMenu";
	public final static String TITLE_LABEL_NEW_LINE = "<br\\s*/*>";

	private List<GlobalMenuSectionModel> subSections = new ArrayList<GlobalMenuSectionModel>();

	public GlobalMenuItemModel(ContentKey key) {
		super(key);
	}

	public List<GlobalMenuSectionModel> getSubSections() {
		ContentNodeModelUtil.refreshModels(this, "subSections", subSections,
				true, true);
		return new ArrayList<GlobalMenuSectionModel>(subSections);
	}

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

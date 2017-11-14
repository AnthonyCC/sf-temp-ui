package com.freshdirect.webapp.taglib.menu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ContentNodeModel;
import com.freshdirect.storeapi.content.FDFolder;
import com.freshdirect.storeapi.content.GlobalMenuItemModel;

/**
* @author tgelesz
*
**/
public class GlobalMenuTag extends SimpleTagSupport {

	private String attrName;

	@Override
	public void doTag() throws JspException, IOException {

		ContentNodeModel model = ContentFactory.getInstance()
				.getContentNodeByKey(ContentKeyFactory
						.get(GlobalMenuItemModel.DEFAULT_MENU_FOLDER));

		if (model instanceof FDFolder) {
			FDFolder folder = (FDFolder) model;
			List<GlobalMenuItemModel> globalMenuItemModels = new ArrayList<GlobalMenuItemModel>();

			for (ContentNodeModel cm : folder.getChildren()) {
				if (cm instanceof GlobalMenuItemModel) {
					globalMenuItemModels.add((GlobalMenuItemModel) cm);
				}
			}

			getJspContext().setAttribute(attrName, globalMenuItemModels);

			getJspBody().invoke(null);
		}
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}


}

package com.freshdirect.webapp.taglib.menu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.FDFolder;
import com.freshdirect.fdstore.content.GlobalMenuItemModel;

/**
* @author tgelesz
* 
**/
public class GlobalMenuTag extends SimpleTagSupport {

	private String attrName;
	
	@Override
	public void doTag() throws JspException, IOException {

		ContentNodeModel model = ContentFactory.getInstance()
				.getContentNodeByKey(ContentKey
						.decode(GlobalMenuItemModel.DEFAULT_MENU_FOLDER));

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

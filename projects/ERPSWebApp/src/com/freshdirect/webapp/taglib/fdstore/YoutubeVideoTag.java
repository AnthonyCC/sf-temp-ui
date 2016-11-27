package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.FDFolder;
import com.freshdirect.fdstore.content.YoutubeVideoModel;
import com.freshdirect.framework.util.log.LoggerFactory;

public class YoutubeVideoTag extends SimpleTagSupport {

	final static Logger LOGGER = LoggerFactory.getInstance(YoutubeVideoTag.class);

	private String attrName;
	private String title;

	@SuppressWarnings("unchecked")
	@Override
	public void doTag() throws JspException, IOException {

		PageContext ctx = (PageContext) getJspContext();

		YoutubeVideoModel ytvm = null;

		ContentNodeModel model = ContentFactory.getInstance().getContentNodeByKey(
				ContentKey.decode(YoutubeVideoModel.DEFAULT_YOUTUBE_FOLDER));

		if (model instanceof FDFolder) {
			FDFolder folder = (FDFolder) model;

			for (ContentNodeModel cm : folder.getChildren()) {
				if (title.equals(cm.getCmsAttributeValue("TITLE"))) {
					if (cm instanceof YoutubeVideoModel) {
						ytvm = (YoutubeVideoModel) cm;
						break;
					}
				}
			}
		}

		if(ytvm==null){
			ctx.setAttribute("error", "Video not found!");
		}else{
			ctx.setAttribute(attrName, ytvm);
		}
		

	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}

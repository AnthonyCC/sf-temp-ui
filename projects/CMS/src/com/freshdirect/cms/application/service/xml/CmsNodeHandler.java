package com.freshdirect.cms.application.service.xml;

import java.util.Map;

import org.xml.sax.helpers.DefaultHandler;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.application.service.ResourceInfoServiceI;

/**
 * Supporting base class for SAX event handlers parsing content data.
 * 
 * @see com.freshdirect.cms.application.service.xml.XmlContentService
 */
public abstract class CmsNodeHandler extends DefaultHandler {

	private ContentServiceI contentService;
	private ResourceInfoServiceI resourceInfoService;

	protected ContentServiceI getContentService() {
		return contentService;
	}

	void setContentService(ContentServiceI contentService) {
		this.contentService = contentService;
	}

	public ResourceInfoServiceI getResourceInfoService() {
		return resourceInfoService;
	}

	public void setResourceInfoService(ResourceInfoServiceI resourceInfoService) {
		this.resourceInfoService = resourceInfoService;
	}

	protected ContentNodeI createNode(ContentKey key) {
		ContentNodeI node = getContentService().getContentNode(key);
		if (node == null) {
			node = getContentService().createPrototypeContentNode(key);
		}
		//if (node == null) {
		//	throw new IllegalArgumentException("No prototype for " + key);
		//}
		return node;
	}

	public abstract Map<ContentKey, ContentNodeI> getContentNodes();

}
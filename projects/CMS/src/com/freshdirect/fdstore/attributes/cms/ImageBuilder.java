/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.fdstore.attributes.cms;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.TitledMedia;

/**
 * @author mrose
 *
 */
public class ImageBuilder extends AbstractAttributeBuilder {

    @Override
	public Object buildValue(AttributeDefI aDef, Object value) {
        ContentNodeI cNode = CmsManager.getInstance().getContentNode((ContentKey) value);

        if (cNode == null) { return null; }
        
        String lastModify = (String) cNode.getAttributeValue("lastmodified");
        
		String path = (String) cNode.getAttributeValue("path");
		
		// FIXME this is due to invalid data
		if (path == null) {
			System.err.println("ImageBuilder.buildValue(): image without path " + cNode);
			path = cNode.getKey().getId();
		}
		// append last modify param
        path = path+"?lastModify="+lastModify;
		
		Object widthObj = cNode.getAttributeValue("width"); 
		if (widthObj == null) {
			System.err.println("ImageBuilder.buildValue(): Image without width " + cNode);
			return new Image(path, 1, 1);
		}

		int width = ((Integer) widthObj).intValue();
		int height = ((Integer) cNode.getAttributeValue("height")).intValue();
		if (cNode.getAttributeValue("title") == null) {
			Image i = new Image(path, width, height);
			return i;
		}
		String title = (String) cNode.getAttributeValue("title");
		return new TitledMedia(new Image(path, width, height), title, null);
	}

}
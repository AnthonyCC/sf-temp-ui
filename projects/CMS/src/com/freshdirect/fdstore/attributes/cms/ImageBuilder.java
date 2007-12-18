/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.fdstore.attributes.cms;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.AttributeI;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.fdstore.attributes.EnumAttributeType;
import com.freshdirect.fdstore.content.Image;
import com.freshdirect.fdstore.content.TitledMedia;

/**
 * @author mrose
 *
 */
public class ImageBuilder extends AbstractAttributeBuilder {

	public EnumAttributeType getFDAttributeType() {
		return EnumAttributeType.MEDIA;
	}

	public Object buildValue(AttributeDefI aDef, Object value) {
		ContentNodeI cNode = (ContentNodeI) value;
		AttributeI patr = cNode.getAttribute("path");

		String path;
		
		// FIXME this is due to invalid data
		if (patr == null || patr.getValue() == null) {
			System.err.println("ImageBuilder.buildValue(): image without path " + cNode);
			path = cNode.getKey().getId();
		} else {
			path = (String)patr.getValue();
		}
		if (cNode.getAttribute("width") == null || cNode.getAttribute("width").getValue() == null) {
			System.err.println("ImageBuilder.buildValue(): Image without width " + cNode);
			return new Image(path, 1, 1);
		}

		int width = ((Integer) cNode.getAttribute("width").getValue()).intValue();
		int height = ((Integer) cNode.getAttribute("height").getValue()).intValue();
		if (cNode.getAttribute("title") == null) {
			Image i = new Image(path, width, height);
			return i;
		} else {
			String title = (String) cNode.getAttribute("title").getValue();
			return new TitledMedia(new Image(path, width, height), title, null);
		}
	}

}
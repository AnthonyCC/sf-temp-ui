/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.fdstore.attributes.cms;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.fdstore.attributes.EnumAttributeType;
import com.freshdirect.fdstore.content.Html;
import com.freshdirect.fdstore.content.TitledMedia;

/**
 * @author mrose
 *
 */
public class HtmlBuilder extends AbstractAttributeBuilder {
    
    public EnumAttributeType getFDAttributeType() {
        return EnumAttributeType.MEDIA;
    }
    
    public Object buildValue(AttributeDefI aDef, Object value) {
        ContentNodeI cNode = (ContentNodeI) value;
        String path = (String) cNode.getAttribute("path").getValue();

		if (cNode.getAttribute("title").getValue()==null  && cNode.getAttribute("popupSize").getValue()==null) {
			Html h = new Html(path);
			return new TitledMedia(h, "", "");
		} else {
			String title = (String) cNode.getAttribute("title").getValue();
			String sizeName = (String)cNode.getAttribute("popupSize").getValue();
			return new TitledMedia(new Html(path), title, sizeName);
		}
    }

}

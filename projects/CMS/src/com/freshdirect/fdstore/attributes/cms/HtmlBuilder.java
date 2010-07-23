/*
 * Created on Dec 13, 2004
 *
 */
package com.freshdirect.fdstore.attributes.cms;

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
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
        ContentNodeI cNode = ((ContentKey) value).lookupContentNode();
        String path = (String) cNode.getAttributeValue("path");

		if ( cNode.getAttributeValue( "title" ) == null && cNode.getAttributeValue( "popupSize" ) == null ) {
			Html h = new Html( path );
			return new TitledMedia( h, "", "" );
		}
		String title = (String) cNode.getAttributeValue("title");
		String sizeName = (String)cNode.getAttributeValue("popupSize");
		return new TitledMedia(new Html(path), title, sizeName);
    }
}

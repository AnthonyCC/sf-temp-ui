package com.freshdirect.cms.ui.translator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.ui.model.ContentNodeModel;
import com.freshdirect.cms.ui.model.EnumModel;
import com.freshdirect.cms.ui.model.GwtContentNode;

/**
 * Static class for converting client-side Gwt data types to server-side CMS data types.
 * 
 * @author treer
 */

public class TranslatorFromGwt {

	public static ContentNodeI getContentNodeI( GwtContentNode clientNode ) {
		ContentKey key = ContentKey.decode(clientNode.getKey());
        ContentNodeI node = CmsManager.getInstance().getContentNode(key);
        if (node == null) {
            // new node creation
            node = CmsManager.getInstance().createPrototypeContentNode(key);
        }
        // calling 'copy' is essential !
        return node.copy();
	}

	@SuppressWarnings("unchecked")
	public static Object getServerValue( Serializable value ) {
		if ( value instanceof ContentNodeModel ) {
			return ContentKey.decode( ( (ContentNodeModel)value ).getKey() );
		}
		if ( value instanceof EnumModel ) {
			return ( (EnumModel)value ).getKey();
		}
		if ( value instanceof List ) {
			List<Object> result = new ArrayList<Object>();
			for ( Object item : (List<Object>)value ) {
				result.add( getServerValue( (Serializable)item ) );
			}
			return result;
		}
		return value;
	}

}

package com.freshdirect.webapp.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;

public class FDFaqUtil {

	public static List<ContentNodeI> getFaqsByCategory(String categoryId){
		CmsManager          manager     = CmsManager.getInstance();				
		ContentKey 			key 		= new ContentKey(FDContentTypes.FDFOLDER, categoryId);
		ContentNodeI 		contentNode = manager.getContentNode(key);
		
		List<ContentNodeI> faqSubFolders = new LinkedList<ContentNodeI>();
		if ( null != contentNode ) {
			Set<ContentKey> subNodes = contentNode.getChildKeys();
			for ( Object object : subNodes ) {
				ContentKey subContentKey = (ContentKey)object;
				if ( null != subContentKey ) {
					ContentType contentType = subContentKey.getType();
					ContentNodeI subContentNode = manager.getContentNode( subContentKey );
					if ( FDContentTypes.FAQ.equals( contentType ) ) {
						faqSubFolders.add( subContentNode );
					}
				}
			}

		}
		return faqSubFolders;
	}
}

package com.freshdirect.webapp.util;

import java.util.LinkedList;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.EnumEStoreId;

public class FDFaqUtil {

	public static final String FAQ_HOME_FD = "faqHome";
	public static final String FAQ_HOME_FDX = "faqHome_fdx";
	
	public static List<ContentNodeI> getFaqsByCategory(String categoryId){
		CmsManager          manager     = CmsManager.getInstance();				
		ContentKey 			key 		= new ContentKey(FDContentTypes.FDFOLDER, categoryId);
		ContentNodeI 		contentNode = manager.getContentNode(key);
		
		List<ContentNodeI> faqSubFolders = new LinkedList<ContentNodeI>();
		if ( null != contentNode ) {
			List<ContentKey> subNodes = (List<ContentKey>) contentNode.getAttributeValue("children");
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

	public static String getFaqHomeId() {
		ContentKey storeKey = CmsManager.getInstance().getSingleStoreKey();

		return storeKey != null && EnumEStoreId.FDX.getContentId().equalsIgnoreCase( storeKey.getId() )
				? FAQ_HOME_FDX
				: FAQ_HOME_FD;
	}
}

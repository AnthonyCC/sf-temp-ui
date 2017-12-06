package com.freshdirect.webapp.util;

import java.util.LinkedList;
import java.util.List;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.storeapi.ContentNodeI;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.fdstore.FDContentTypes;

public class FDFaqUtil {

	public static final String FAQ_HOME_FD = "faqHome";
	public static final String FAQ_HOME_FDX = "faqHome_fdx";

	public static List<ContentNodeI> getFaqsByCategory(String categoryId){
		CmsManager          manager     = CmsManager.getInstance();
		ContentKey 			key 		= ContentKeyFactory.get(FDContentTypes.FDFOLDER, categoryId);
		ContentNodeI 		contentNode = manager.getContentNode(key);

		List<ContentNodeI> faqSubFolders = new LinkedList<ContentNodeI>();
        if ( null != contentNode ) {
            List<ContentKey> subNodes = (List<ContentKey>) contentNode.getAttributeValue("children");
            if (subNodes != null) {
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

package com.freshdirect.webapp.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;

public class FDFaqUtil {

	public static List getFaqsByCategory(String categoryId){
		CmsManager          manager     = CmsManager.getInstance();				
		ContentKey key = new ContentKey(FDContentTypes.FDFOLDER, categoryId);
		ContentNodeI contentNode = manager.getContentNode(key);
		
		List faqSubFolders = new LinkedList();
		if(null != contentNode){			
			Set subNodes = contentNode.getChildKeys();
//			Map subNodesMap = new LinkedHashMap();
			for (Object object : subNodes) {
				ContentKey subContentKey= (ContentKey)object;
				if(null!=subContentKey){
					ContentType contentType=subContentKey.getType(); 
					ContentNodeI subContentNode = manager.getContentNode(subContentKey);
					if(FDContentTypes.FAQ.equals(contentType)){
						faqSubFolders.add(subContentNode);
					}						
				}					
			}
//			faqSubFolders.add(subNodesMap);
			
		}
		return faqSubFolders;
	}
}

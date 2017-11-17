package com.freshdirect.cms.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.content.BrandModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ContentSearch;
import com.freshdirect.framework.util.log.LoggerFactory;

public class BrandNameWordList implements WordListI{
    private final static Logger        LOGGER   = LoggerFactory.getInstance(ContentSearch.class);
    private List<String> words = null;
    
	private void generateWords() {
        LOGGER.info("createAutocompleteService");
        Set<ContentKey> contentKeysByType = CmsManager.getInstance().getContentKeysByType(FDContentTypes.BRAND);
        LOGGER.info("contentKeysByType loaded :"+contentKeysByType.size());
        
        words = new ArrayList<String>(contentKeysByType.size());
        
        ContentFactory contentFactory = ContentFactory.getInstance();
        for (Iterator<ContentKey> keyIterator = contentKeysByType.iterator();keyIterator.hasNext();) {
            ContentKey key = keyIterator.next();
            ContentNodeModel nodeModel = contentFactory.getContentNodeByKey(key);
            if (nodeModel instanceof BrandModel) {
                BrandModel bm = (BrandModel) nodeModel;
                    words.add(bm.getFullName());
            }
        }
        LOGGER.info("product names extracted:"+words.size());				
	}
	
	public List<String> getWords() {
		if(words == null) {
			generateWords();
		}
		return words;
	}
}

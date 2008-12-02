package com.freshdirect.cms.fdstore;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.labels.ILabelProvider;

public class SearchRelevancyLabelProvider implements ILabelProvider {

    public String getLabel(ContentNodeI node) {
        ContentType type = node.getKey().getType();
        
        if (FDContentTypes.SEARCH_RELEVANCY_LIST.equals(type)) {
            return "Search for:"+node.getAttribute("Keywords").getValue();
        }
        if (FDContentTypes.SEARCH_RELEVANCY_HINT.equals(type)) {
            ContentKey key = (ContentKey) node.getAttribute("category").getValue();
            return "Category "+(key !=null ? key.getId() : "<not specified>")+" score : "+node.getAttribute("score").getValue();
        }
        return null;
    }

}

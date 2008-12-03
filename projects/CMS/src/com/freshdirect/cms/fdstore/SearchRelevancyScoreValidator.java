package com.freshdirect.cms.fdstore;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsRequestI;
import com.freshdirect.cms.application.ContentServiceI;
import com.freshdirect.cms.search.SearchRelevancyList;
import com.freshdirect.cms.validation.ContentValidationDelegate;
import com.freshdirect.cms.validation.ContentValidatorI;
import com.freshdirect.fdstore.content.ContentSearch;

public class SearchRelevancyScoreValidator implements ContentValidatorI {

    public SearchRelevancyScoreValidator() {
    }

    public void validate(ContentValidationDelegate delegate, ContentServiceI service, ContentNodeI node, CmsRequestI request) {
        ContentType t = node.getKey().getType();
        if (FDContentTypes.SEARCH_RELEVANCY_LIST.equals(t) || FDContentTypes.SEARCH_RELEVANCY_HINT.equals(t)) {
            ContentSearch.getInstance().invalidateRelevancyScores();
        }
        if (FDContentTypes.FDFOLDER.equals(t)) {
            if (ContentKey.decode(SearchRelevancyList.SEARCH_RELEVANCY_KEY).equals(node.getKey())) {
                ContentSearch.getInstance().invalidateRelevancyScores();
            }
        }

    }

}

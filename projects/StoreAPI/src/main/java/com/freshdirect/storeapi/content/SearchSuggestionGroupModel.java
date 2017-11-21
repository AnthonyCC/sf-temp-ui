package com.freshdirect.storeapi.content;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.storeapi.attributes.FDAttributeFactory;

public class SearchSuggestionGroupModel extends ContentNodeModelImpl {

    public SearchSuggestionGroupModel(ContentKey cKey) {
        super(cKey);
    }

	public Image getTabletImage() {
		return FDAttributeFactory.constructImage(this, "tabletImage");
	}

	public List<String> getSearchTerms() {
		List<String> searchTerms = new ArrayList<String>();
		String searchTermsCsv = (String) getCmsAttributeValue("searchTerms");

		if (searchTermsCsv != null){
			for (String token : searchTermsCsv.trim().split("\\s*;\\s*")){
				if (!"".equals(token)){
					searchTerms.add(token);
				}
			}
		}

		return searchTerms;
	}

}

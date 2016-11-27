package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.attributes.FDAttributeFactory;

public class SearchSuggestionGroupModel extends ContentNodeModelImpl {

	public SearchSuggestionGroupModel(ContentKey key) {
		super(key);
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

package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.cms.ContentKey;

public class RecipeSearchPage extends ContentNodeModelImpl {

	private final List<RecipeSearchCriteria> criteria = new ArrayList<RecipeSearchCriteria>();

	private final List<Domain> filterByDomains = new ArrayList<Domain>();

	public RecipeSearchPage(ContentKey cKey) {
		super(cKey);
	}

	public static RecipeSearchPage getDefault() {
		return (RecipeSearchPage) ContentFactory.getInstance().getContentNode("rec_search");
	}
	
	public List<RecipeSearchCriteria> getCriteria() {
		ContentNodeModelUtil.refreshModels(this, "criteria", criteria, true);
		return Collections.unmodifiableList(criteria);
	}
	
	public List<Domain> getFilterByDomains() {
		ContentNodeModelUtil.refreshModels(this, "filterByDomains", filterByDomains, false);
		return Collections.unmodifiableList(filterByDomains);
	}

	public List<RecipeSearchCriteria> getCriteriaBySelectionType(String selectionType) {
		List<RecipeSearchCriteria> l = new ArrayList<RecipeSearchCriteria>(getCriteria());
		for (Iterator<RecipeSearchCriteria> i = l.iterator(); i.hasNext();) {
			RecipeSearchCriteria c = i.next();
			if (!selectionType.equals(c.getSelectionType())) {
				i.remove();
			}
		}
		return l;
	}

}

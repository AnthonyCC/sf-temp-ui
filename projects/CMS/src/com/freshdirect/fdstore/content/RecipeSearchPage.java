package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.freshdirect.cms.ContentKey;

public class RecipeSearchPage extends ContentNodeModelImpl {

	private final List criteria = new ArrayList();

	private final List filterByDomains = new ArrayList();

	public RecipeSearchPage(ContentKey cKey) {
		super(cKey);
	}

	public static RecipeSearchPage getDefault() {
		return (RecipeSearchPage) ContentFactory.getInstance().getContentNode(
				"rec_search");
	}
	
	public List getCriteria() {
		ContentNodeModelUtil.refreshModels(this, "criteria", criteria, true);
		return Collections.unmodifiableList(criteria);
	}
	
	public List getFilterByDomains() {
		ContentNodeModelUtil.refreshModels(this, "filterByDomains", filterByDomains, false);
		return Collections.unmodifiableList(filterByDomains);
	}

	public List getCriteriaBySelectionType(String selectionType) {
		List l = new ArrayList(getCriteria());
		for (Iterator i = l.iterator(); i.hasNext();) {
			RecipeSearchCriteria c = (RecipeSearchCriteria) i.next();
			if (!selectionType.equals(c.getSelectionType())) {
				i.remove();
			}
		}
		return l;
	}

}

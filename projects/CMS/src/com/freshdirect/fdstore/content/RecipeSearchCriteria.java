package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.freshdirect.cms.ContentKey;

public class RecipeSearchCriteria extends ContentNodeModelImpl {

	public final static String TYPE_ONE = "ONE";

	public final static String TYPE_MANY = "MANY";

	public final static String OPERATION_AND = "AND";

	public final static String OPERATION_OR = "OR";

	private final List domainValues = new ArrayList();

	public RecipeSearchCriteria(ContentKey cKey) {
		super(cKey);
	}

	public String getName() {
		return getAttribute("name", "");
	}
	
	public String getSelectionType() {
		return getAttribute("selectionType", TYPE_ONE);
	}

	public String getLogicalOperation() {
		return getAttribute("logicalOperation", OPERATION_AND);
	}

	public List getCriteriaDomainValues() {
		ContentNodeModelUtil.refreshModels(this, "criteriaDomainValues", domainValues, false);
		return Collections.unmodifiableList(domainValues);
	}

}

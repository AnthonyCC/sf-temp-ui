package com.freshdirect.fdstore.content;

import com.freshdirect.cms.ContentKey;

public class SortOptionModel extends ContentNodeModelImpl {

	
	public SortOptionModel(ContentKey key) {
		super(key);
	}

	public String getLabel() {
		return (String) getCmsAttributeValue("label");
	}

	public String getSelectedLabel() {
		return (String) getCmsAttributeValue("selectedLabel");
	}

	public String getSelectedLabelReverseOrder() {
		return (String) getCmsAttributeValue("selectedLabelReverseOrder");
	}

	public String getStrategy() {
		return (String) getCmsAttributeValue("strategy");
	}

	/**
	 * Guess exact sort type upon value obtained from {@link #getStrategy()} attribute.
	 * It can be null if type could not be matched
	 * 
	 * @return
	 */
	public SortStrategyType getSortStrategyType() {
		return SortStrategyType.toEnum( getStrategy() );
	}
}

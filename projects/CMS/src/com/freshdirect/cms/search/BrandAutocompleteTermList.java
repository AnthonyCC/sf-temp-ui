package com.freshdirect.cms.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.content.BrandModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.framework.util.log.LoggerFactory;

public class BrandAutocompleteTermList extends AutocompleteTermList {
	private final static Logger LOGGER = LoggerFactory.getInstance(BrandAutocompleteTermList.class);

	protected List<AutocompleteTerm> doGetTerms() {
		LOGGER.info("generating brand name world list");
		Set<ContentKey> keys = CmsManager.getInstance().getContentKeysByType(FDContentTypes.BRAND);
		LOGGER.info("loaded " + keys.size() + " brand keys");

		List<AutocompleteTerm> terms = new ArrayList<AutocompleteTerm>(keys.size());

		ContentFactory contentFactory = ContentFactory.getInstance();
		for (ContentKey key : keys) {
			ContentNodeModel node = contentFactory.getContentNodeByKey(key);
			if (node instanceof BrandModel) {
				BrandModel bm = (BrandModel) node;
				String name = bm.getFullName();
				if (name != null) {
					addTerm(terms, key, name);
				}
			}
		}
		LOGGER.info("extracted " + terms.size() + " brand names");
		return terms;
	}
}

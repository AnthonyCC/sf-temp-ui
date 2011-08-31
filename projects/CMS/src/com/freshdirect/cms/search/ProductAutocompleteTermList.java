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
import com.freshdirect.fdstore.content.ContentSearchUtil;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ProductAutocompleteTermList extends AutocompleteTermList {
	private final static Logger LOGGER = LoggerFactory.getInstance(ProductAutocompleteTermList.class);

	protected List<AutocompleteTerm> doGetTerms() {
		LOGGER.info("generating product name world list");
		Set<ContentKey> keys = CmsManager.getInstance().getContentKeysByType(FDContentTypes.PRODUCT);
		LOGGER.info("loaded " + keys.size() + " product keys");

		List<AutocompleteTerm> terms = new ArrayList<AutocompleteTerm>(keys.size() + 1000);

		ContentFactory contentFactory = ContentFactory.getInstance();
		for (ContentKey key : keys) {
			ContentNodeModel nodeModel = contentFactory.getContentNodeByKey(key);
			if (nodeModel instanceof ProductModel) {
				ProductModel product = (ProductModel) nodeModel;
				if (ContentSearchUtil.isDisplayable(product)) {
					String name = product.getFullName();
					if (name != null) {
						addTerm(terms, key, name);
					}
					name = product.getGlanceName();
					if (name != null) {
						addTerm(terms, key, name);
					}
					List<BrandModel> brands = product.getBrands();
					for (BrandModel brand : brands) {
						name = brand.getFullName();
						if (name != null) {
							addTerm(terms, key, name);
						}
					}
				}
			}
		}

		keys = CmsManager.getInstance().getContentKeysByType(FDContentTypes.RECIPE);
		LOGGER.info("loaded " + keys.size() + " recipe keys");

		for (ContentKey key : keys) {
			ContentNodeModel nodeModel = contentFactory.getContentNodeByKey(key);
			if (nodeModel instanceof Recipe) {
				Recipe recipe = (Recipe) nodeModel;
				if (ContentSearchUtil.isDisplayable(recipe)) {
					String name = recipe.getFullName();
					if (name != null) {
						addTerm(terms, key, name);
					}
				}
			}
		}
		LOGGER.info("extracted " + terms.size() + " product/recipe names");
		return terms;
	}
}

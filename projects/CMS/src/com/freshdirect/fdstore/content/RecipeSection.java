package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;

public class RecipeSection extends ContentNodeModelImpl {

	private final List ingredients = new ArrayList();

	public RecipeSection(ContentKey cKey) {
		super(cKey);
	}

	public String getName() {
		return getAttribute("name", "");
	}
	
	public boolean isShowQuantity() {
		return getAttribute("SHOW_QUANTITY", true);
	}

	/**
	 *  Return the list of ingredients for the section.
	 *  
	 *  @return a list of ConfiguredProduct objects.
	 */
	public List getIngredients() {
		ContentNodeModelUtil.refreshModels(this, "ingredients", ingredients, false);
		return Collections.unmodifiableList(ingredients);
	}

	/** @return Set of SkuModel */
	public Set getDistinctSkus() {
		Set skus = new HashSet();
		for (Iterator si = getIngredients().iterator(); si.hasNext();) {
			ConfiguredProduct cp = (ConfiguredProduct) si.next();
			if (cp.isUnavailable()) {
				continue;
			}
			String skuCode = cp.getSkuCode();
			SkuModel sku = (SkuModel) ContentFactory.getInstance().getContentNode(skuCode);
			if (sku != null) {
				skus.add(sku);
			}
		}
		return skus;
	}

	/**
	 *  Tell if the recipe is available for sale.
	 *  
	 *  @return true if all required ingredients are available,
	 *          false otherwise
	 */
	public boolean isAvailable() {
		for (Iterator i = getIngredients().iterator(); i.hasNext();) {
			ConfiguredProduct prod = (ConfiguredProduct) i.next();
			if (prod.isUnavailable() && prod.isRequired()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 *  Tell if there are any ingredients that are not available,
	 *  be that required or not required ingredients.
	 *  
	 *  @return true if all the ingredients in the recipe section
	 *          are available, false otherwise.
	 */
	public boolean isAllAvailable() {
		for (Iterator i = getIngredients().iterator(); i.hasNext();) {
			ConfiguredProduct prod = (ConfiguredProduct) i.next();
			if (prod.isUnavailable()) {
				return false;
			}
		}
		return true;
	}
}

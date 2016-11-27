package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;

public class RecipeVariant extends ContentNodeModelImpl {

	private final List<RecipeSection> sections = new ArrayList<RecipeSection>();

	public RecipeVariant(ContentKey cKey) {
		super(cKey);
	}

	public String getName() {
		return getAttribute("name", "");
	}

	public List<RecipeSection> getSections() {
		ContentNodeModelUtil.refreshModels(this, "sections", sections, true);
		return Collections.unmodifiableList(sections);
	}

	/** @return Set of SkuModel */
	public Set<SkuModel> getDistinctSkus() {
		Set<SkuModel> skus = new HashSet<SkuModel>();
		for ( RecipeSection s : getSections() ) {
			skus.addAll(s.getDistinctSkus());
		}
		return skus;
	}

	/**
	 *  Return the list of all ingredients for this variant.
	 *  
	 *  @return a list of ConfiguredProduct objects.
	 */
	public List<ConfiguredProduct> getAllIngredients() {
		List<ConfiguredProduct> l = new ArrayList<ConfiguredProduct>();
		for ( RecipeSection s : getSections() ) {
			l.addAll(s.getIngredients());
		}
		return l;
	}

	
	/**
	 *  Tell if the recipe is available for sale.
	 *  
	 *  @return true if all sections of the variant are available,
	 *          false otherwise
	 */
	public boolean isAvailable() {
		for ( RecipeSection section : getSections() ) {
			if (!section.isAvailable()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 *  Tell if all items in the recipe variant are available,
	 *  be that required or not required ingredients.
	 *  
	 *  @return true if all the ingredients in the recipe variant
	 *          are available, false otherwise.
	 */
	public boolean isAllAvailable() {
		for ( RecipeSection section : getSections() ) {
			if (!section.isAllAvailable()) {
				return false;
			}
		}
		return true;
	}

}

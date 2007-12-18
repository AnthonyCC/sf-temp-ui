package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;

public class RecipeVariant extends ContentNodeModelImpl {

	private final List sections = new ArrayList();

	public RecipeVariant(ContentKey cKey) {
		super(cKey);
	}

	public String getName() {
		return getAttribute("name", "");
	}

	public List getSections() {
		ContentNodeModelUtil.refreshModels(this, "sections", sections, true);
		return Collections.unmodifiableList(sections);
	}

	/** @return Set of SkuModel */
	public Set getDistinctSkus() {
		Set skus = new HashSet();
		for (Iterator i = getSections().iterator(); i.hasNext();) {
			RecipeSection s = (RecipeSection) i.next();
			skus.addAll(s.getDistinctSkus());
		}
		return skus;
	}

	/**
	 *  Return the list of all ingredients for this variant.
	 *  
	 *  @return a list of ConfiguredProduct objects.
	 */
	public List getAllIngredients() {
		List l = new ArrayList();
		for (Iterator i = getSections().iterator(); i.hasNext();) {
			RecipeSection s = (RecipeSection) i.next();
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
		for (Iterator i = getSections().iterator(); i.hasNext(); ) {
			RecipeSection section = (RecipeSection) i.next();
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
		for (Iterator i = getSections().iterator(); i.hasNext(); ) {
			RecipeSection section = (RecipeSection) i.next();
			if (!section.isAllAvailable()) {
				return false;
			}
		}
		return true;
	}

}

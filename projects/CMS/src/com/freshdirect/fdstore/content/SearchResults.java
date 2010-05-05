/*
 * $Workfile$
 *
 * $Date$
 * 
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */
package com.freshdirect.fdstore.content;

import java.io.Serializable;
import java.util.List;

import com.freshdirect.cms.search.SearchHit;

/**
 *
 *
 * @version $Revision$
 * @author $Author$
 */
public class SearchResults implements Serializable {

	
	private static final long serialVersionUID = 4489822494244167394L;

	/**
	 * 
	 * Stores statistics of two possible intersecting sets.
	 * 
	 * The sets O and S represent the sets returned to the original query (O) and 
	 * the suggested query (S).
	 * 
	 * 
	 * @author istvan
	 *
	 */
	public static class SpellingResultsDifferences implements Serializable {

		private static final long serialVersionUID = 4046636016313032574L;
		private final int OMinusS;
		private final int SMinusO;
		private final int OAndS;
		
		public SpellingResultsDifferences(int OMinusS, int SMinusO, int OAndS) {
			this.OMinusS = OMinusS;
			this.SMinusO = SMinusO;
			this.OAndS = OAndS;
		}
		
		/** 
		 * Get the number of elements unique to O only.
		 * @return number of elements unique to O
		 */
		public int getOriginal() {
			return OMinusS;
		}
		
		/**
		 * Get the number of elements unique to S only.
		 * @return number of elements unique to S
		 */
		public int getSuggested() {
			return SMinusO;
		}
		
		/**
		 * Get number of elements common to O and S
		 * @return number of elements common to O and S
		 */
		public int getIntersection() {
			return OAndS;
		}
		
		/**
		 * Get the union of O and S
		 * @return union of O and S
		 */
		public int getUnion() {
			return OMinusS + SMinusO + OAndS;
		}
		
	};
	
	private List<ProductModel> products;
	private final List<Recipe> recipes;
	
	private final boolean productsRelevant;
	
	private String spellingSuggestion = null;
	private boolean suggestionMoreRelevant = false;
	
	protected String searchTerm;

	private SpellingResultsDifferences spellingResultsDifferences = null;

        private final List<SearchHit> productSearchHit;
        private final List<SearchHit> recipeSearchHit;
	
        public SearchResults(List<ProductModel> products, List<Recipe> recipes, boolean productsRelevant, String searchTerm) {
            this(products, recipes, productsRelevant, searchTerm, null, null);
        }
    
        public SearchResults(List<ProductModel> products, List<Recipe> recipes, boolean productsRelevant, String searchTerm, List<SearchHit> productSearchHit,
                List<SearchHit> recipeSearchHit) {
            this.products = products;
            this.recipes = recipes;
            this.productsRelevant = productsRelevant;
            this.searchTerm = searchTerm;
            this.productSearchHit = productSearchHit;
            this.recipeSearchHit = recipeSearchHit;
        }

	/**
	 * 
	 * @param spellingSuggestion
	 * @param suggestionMoreRelevant
	 */
	public void setSpellingSuggestion(String spellingSuggestion, boolean suggestionMoreRelevant) {
		this.spellingSuggestion = spellingSuggestion;
		this.suggestionMoreRelevant = suggestionMoreRelevant;
	}
	
	/**
	 * 
	 * @param diffs
	 */
	public void setSpellingResultsDifferences(SpellingResultsDifferences diffs) {
		this.spellingResultsDifferences = diffs;
	}
	
	public List<ProductModel> getProducts() {
		return this.products;
	}
	
	void setProducts(List<ProductModel> products) {
		this.products = products;
	}
	
	public List<Recipe> getRecipes() {
		return recipes;
	}

	public int numberOfResults() {
		return products.size() + recipes.size();
	}
	
	public String getSpellingSuggestion() {
		return spellingSuggestion;
	}
	
	public boolean isSuggestionMoreRelevant() {
		return suggestionMoreRelevant;
	}
	
	public boolean isProductsRelevant() {
		return productsRelevant;
	}
	
	public SpellingResultsDifferences getSpellingResultsDifferences() {
		return spellingResultsDifferences;
	}

	public String getSearchTerm() {
		return searchTerm;
	}
	
	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}
	
	public List<SearchHit> getProductSearchHit() {
            return productSearchHit;
        }
	
	public List<SearchHit> getRecipeSearchHit() {
            return recipeSearchHit;
        }
}
package com.freshdirect.fdstore.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.search.term.Term;

public class SearchResults implements Serializable, Cloneable {
	private static final long serialVersionUID = 4489822494244167394L;
	
	public static final SearchResults EMPTY_SEARCH_RESULTS = new SearchResults();
	
	public static void replaceResults(SearchResults oldResults, SearchResults newResults) {
		oldResults.products.clear();
		oldResults.products.addAll(newResults.products);
		oldResults.recipes.clear();
		oldResults.recipes.addAll(newResults.recipes);
		oldResults.categories.clear();
		oldResults.categories.addAll(newResults.categories);
		oldResults.suggestedTerm = newResults.searchTerm;
	}

	public static void replaceResults(SearchResults oldResults, List<SearchResults> newResultsList) {
		oldResults.products.clear();
		oldResults.recipes.clear();
		oldResults.categories.clear();
		Set<FilteringSortingItem<ProductModel>> products = new HashSet<FilteringSortingItem<ProductModel>>();
		Set<FilteringSortingItem<Recipe>> recipes = new HashSet<FilteringSortingItem<Recipe>>();
		Set<FilteringSortingItem<CategoryModel>> categories = new HashSet<FilteringSortingItem<CategoryModel>>();
		Set<String> suggestedTerms = new LinkedHashSet<String>();
		for (SearchResults newResults : newResultsList) {
			products.addAll(newResults.products);
			recipes.addAll(newResults.recipes);
			categories.addAll(newResults.categories);
			List<String> tokens = Term.tokenize(newResults.searchTerm, Term.DEFAULT_TOKENIZERS);
			suggestedTerms.addAll(tokens);
		}
		oldResults.products.addAll(products);
		oldResults.recipes.addAll(recipes);
		oldResults.categories.addAll(categories);
		oldResults.suggestedTerm = Term.join(new ArrayList<String>(suggestedTerms), Term.DEFAULT_SEPARATOR);
	}
	
	private final List<FilteringSortingItem<ProductModel>> products;
	private List<ProductModel> ddppProducts = new ArrayList<ProductModel>(); //ddpp products on 'search like' pages
	private Map<String, List<ProductModel>> assortProducts = new HashMap<String, List<ProductModel>>(); //staff picks

	private List<FilteringSortingItem<ProductModel>> adProducts = new ArrayList<FilteringSortingItem<ProductModel>>();
	private String pageBeacon;
	private int hlProductsCount;

	private final List<FilteringSortingItem<Recipe>> recipes;
	private final List<FilteringSortingItem<CategoryModel>> categories;
	private boolean phrase; // tells whether the original search was quoted or not

	private String searchTerm;
	private String suggestedTerm; // term used when displaying products for the spelling suggestion (there's only one DYM case)
	private Collection<String> spellingSuggestions = Collections.emptyList();

	public SearchResults() {
		this(Collections.<FilteringSortingItem<ProductModel>> emptyList(), Collections.<FilteringSortingItem<Recipe>> emptyList(),
				Collections.<FilteringSortingItem<CategoryModel>> emptyList(), "", true);
	}

	public SearchResults(List<FilteringSortingItem<ProductModel>> products, List<FilteringSortingItem<Recipe>> recipes,
			List<FilteringSortingItem<CategoryModel>> categories, String searchTerm, boolean phrase) {
		this.products = products;
		this.recipes = recipes;
		this.categories = categories;
		this.searchTerm = searchTerm;
		this.phrase = phrase;
	}

	public List<FilteringSortingItem<ProductModel>> getProducts() {
		return this.products;
	}
	
	public void emptyProducts() {
		if (!products.isEmpty()) {
			products.clear();
		}
	}

	public List<FilteringSortingItem<Recipe>> getRecipes() {
		return recipes;
	}

	public void emptyRecipes() {
		if (!recipes.isEmpty()) {
			recipes.clear();
		}
	}

	public List<FilteringSortingItem<CategoryModel>> getCategories() {
		return categories;
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public String getSuggestedTerm() {
		return suggestedTerm;
	}

	public void setSuggestedTerm(String suggestedTerm) {
		this.suggestedTerm = suggestedTerm;
	}

	public Collection<String> getSpellingSuggestions() {
		return spellingSuggestions;
	}

	public void setSpellingSuggestions(Collection<String> spellingSuggestions) {
		this.spellingSuggestions = spellingSuggestions;
	}

	public boolean isEmpty() {
		return products.isEmpty() && recipes.isEmpty();
	}

	public int size() {
		return products.size() + recipes.size();
	}

	public boolean isNoProducts() {
		return products.isEmpty();
	}

	public boolean isPhrase() {
		return phrase;
	}

	public void setPhrase(boolean phrase) {
		this.phrase = phrase;
	}

	@Override
	public SearchResults clone()  {
		List<FilteringSortingItem<ProductModel>> products = new ArrayList<FilteringSortingItem<ProductModel>>(this.products);
		List<FilteringSortingItem<Recipe>> recipes = new ArrayList<FilteringSortingItem<Recipe>>(this.recipes);
		List<FilteringSortingItem<CategoryModel>> categories = new ArrayList<FilteringSortingItem<CategoryModel>>(this.categories);
		SearchResults _new = new SearchResults(products, recipes, categories, searchTerm, phrase);
		_new.spellingSuggestions = spellingSuggestions;
		_new.suggestedTerm = suggestedTerm;
		return _new;
	}

	public boolean resultsEquals(SearchResults o) {
		if (products.size() != o.products.size())
			return false;
		if (recipes.size() != o.recipes.size())
			return false;
		if (categories.size() != o.categories.size())
			return false;

		Set<ContentKey> otherKeys = new HashSet<ContentKey>(products.size() + recipes.size() + categories.size() + 100);
		for (FilteringSortingItem<ProductModel> product : o.products)
			otherKeys.add(product.getModel().getContentKey());
		for (FilteringSortingItem<Recipe> recipe : o.recipes)
			otherKeys.add(recipe.getModel().getContentKey());
		for (FilteringSortingItem<CategoryModel> category : o.categories)
			otherKeys.add(category.getModel().getContentKey());

		for (FilteringSortingItem<ProductModel> item : products)
			if (!otherKeys.contains(item.getModel().getContentKey()))
				return false;
		for (FilteringSortingItem<Recipe> item : recipes)
			if (!otherKeys.contains(item.getModel().getContentKey()))
				return false;
		for (FilteringSortingItem<CategoryModel> item : categories)
			if (!otherKeys.contains(item.getModel().getContentKey()))
				return false;

		return true;
	}

	public List<ProductModel> getDDPPProducts() {
		return ddppProducts;
	}

	public void setDDPPProducts(List<ProductModel> ddppProducts) {
		this.ddppProducts = ddppProducts;
	}
	
	public Map<String, List<ProductModel>> getAssortProducts() {
		return assortProducts;
	}
	public void setAssortProducts(Map<String, List<ProductModel>> assortProducts) {
		this.assortProducts = assortProducts;
	}
	public List<FilteringSortingItem<ProductModel>> getAdProducts() {
		return adProducts;
	}

	public void setAdProducts(List<FilteringSortingItem<ProductModel>> adProducts) {
		this.adProducts = adProducts;
	}
	public String getPageBeacon() {
		return pageBeacon;
	}

	public void setPageBeacon(String pageBeacon) {
		this.pageBeacon = pageBeacon;
	}
	public int getHlProductsCount() {
		return hlProductsCount;
	}

	public void setHlProductsCount(int hlProductsCount) {
		this.hlProductsCount = hlProductsCount;
	}
	
}
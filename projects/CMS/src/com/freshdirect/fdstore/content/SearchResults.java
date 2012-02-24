package com.freshdirect.fdstore.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
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
		Set<SearchResultItem<ProductModel>> products = new HashSet<SearchResultItem<ProductModel>>();
		Set<SearchResultItem<Recipe>> recipes = new HashSet<SearchResultItem<Recipe>>();
		Set<SearchResultItem<CategoryModel>> categories = new HashSet<SearchResultItem<CategoryModel>>();
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
	
	private final List<SearchResultItem<ProductModel>> products;
	private final List<SearchResultItem<Recipe>> recipes;
	private final List<SearchResultItem<CategoryModel>> categories;
	private boolean phrase; // tells whether the original search was quoted or not

	private String searchTerm;
	private String suggestedTerm; // term used when displaying products for the spelling suggestion (there's only one DYM case)
	private Collection<String> spellingSuggestions = Collections.emptyList();

	public SearchResults() {
		this(Collections.<SearchResultItem<ProductModel>> emptyList(), Collections.<SearchResultItem<Recipe>> emptyList(),
				Collections.<SearchResultItem<CategoryModel>> emptyList(), "", true);
	}

	public SearchResults(List<SearchResultItem<ProductModel>> products, List<SearchResultItem<Recipe>> recipes,
			List<SearchResultItem<CategoryModel>> categories, String searchTerm, boolean phrase) {
		this.products = products;
		this.recipes = recipes;
		this.categories = categories;
		this.searchTerm = searchTerm;
		this.phrase = phrase;
	}

	public List<SearchResultItem<ProductModel>> getProducts() {
		return this.products;
	}
	
	public void emptyProducts() {
		if (!products.isEmpty()) {
			products.clear();
		}
	}

	public List<SearchResultItem<Recipe>> getRecipes() {
		return recipes;
	}

	public void emptyRecipes() {
		if (!recipes.isEmpty()) {
			recipes.clear();
		}
	}

	public List<SearchResultItem<CategoryModel>> getCategories() {
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
		List<SearchResultItem<ProductModel>> products = new ArrayList<SearchResultItem<ProductModel>>(this.products);
		List<SearchResultItem<Recipe>> recipes = new ArrayList<SearchResultItem<Recipe>>(this.recipes);
		List<SearchResultItem<CategoryModel>> categories = new ArrayList<SearchResultItem<CategoryModel>>(this.categories);
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
		for (SearchResultItem<ProductModel> product : o.products)
			otherKeys.add(product.getModel().getContentKey());
		for (SearchResultItem<Recipe> recipe : o.recipes)
			otherKeys.add(recipe.getModel().getContentKey());
		for (SearchResultItem<CategoryModel> category : o.categories)
			otherKeys.add(category.getModel().getContentKey());

		for (SearchResultItem<ProductModel> item : products)
			if (!otherKeys.contains(item.getModel().getContentKey()))
				return false;
		for (SearchResultItem<Recipe> item : recipes)
			if (!otherKeys.contains(item.getModel().getContentKey()))
				return false;
		for (SearchResultItem<CategoryModel> item : categories)
			if (!otherKeys.contains(item.getModel().getContentKey()))
				return false;

		return true;
	}
}
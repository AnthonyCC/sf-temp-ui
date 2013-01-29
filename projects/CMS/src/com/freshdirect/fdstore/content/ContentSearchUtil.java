package com.freshdirect.fdstore.content;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.search.SearchHit;

public class ContentSearchUtil {
	public static boolean isDisplayable(ProductModel product) {
		return !product.isOrphan() && !product.isHidden() && product.getPrimaryHome() != null && product.isSearchable()
				&& !product.isDiscontinued();
	}

	public static boolean isDisplayable(Recipe recipe) {
		return !recipe.isOrphan() && !recipe.isHidden() && recipe.isSearchable() && recipe.isAvailable() && recipe.isActive();
	}

	public static boolean isDisplayable(CategoryModel category) {
		return !category.isOrphan() && !category.isHidden() && category.isSearchable();
	}

	public static List<ProductModel> filterProductsByDepartment(List<ProductModel> products, String departmentKey) {
		if (departmentKey == null || products == null) {
			return products;
		}
		List<ProductModel> result = new ArrayList<ProductModel>(products.size());
		for (ProductModel prod : products) {
			if (prod.getDepartment().getContentKey().getId().equals(departmentKey)) {
				result.add(prod);
			}
		}
		return result;
	}

	public static List<SearchHit> filterRecipesByAvailability(List<SearchHit> recipes) {
		for (Iterator<SearchHit> i = recipes.iterator(); i.hasNext();) {
			Recipe recipe = (Recipe) i.next().getNode();
			if (!recipe.isAvailable() || !recipe.isActive()) {
				i.remove();
			}
		}

		return recipes;
	}

	public static List<SearchHit> filterProductHits(Collection<SearchHit> hits) {
		List<SearchHit> ret = new ArrayList<SearchHit>();
		for (SearchHit hit : hits) {
			if (FDContentTypes.PRODUCT.equals(hit.getContentKey().getType())) {
				ProductModel product = (ProductModel) hit.getNode();
				if ( product != null && isDisplayable(product))
					ret.add(hit);
			}
		}
		return ret;
	}

	public static List<SearchHit> filterRecipeHits(Collection<SearchHit> hits) {
		List<SearchHit> ret = new ArrayList<SearchHit>();
		for (SearchHit hit : hits) {
			if (FDContentTypes.RECIPE.equals(hit.getContentKey().getType())) {
				Recipe recipe = (Recipe) hit.getNode();
				if (isDisplayable(recipe))
					ret.add(hit);
			}
		}
		return ret;
	}

	public static List<SearchHit> filterCategoryHits(Collection<SearchHit> hits) {
		List<SearchHit> ret = new ArrayList<SearchHit>();
		for (SearchHit hit : hits) {
			if (FDContentTypes.CATEGORY.equals(hit.getContentKey().getType())) {
				CategoryModel category = (CategoryModel) hit.getNode();
				if (isDisplayable(category))
					ret.add(hit);
			}
		}
		return ret;
	}

	public static List<ProductModel> filterProducts(List<SearchHit> hits) {
		List<ProductModel> products = new ArrayList<ProductModel>();
		for (SearchHit hit : hits)
			products.add((ProductModel) hit.getNode());
		return products;
	}

	public static List<Recipe> filterRecipes(List<SearchHit> hits) {
		List<Recipe> recipes = new ArrayList<Recipe>();
		for (SearchHit hit : hits)
			recipes.add((Recipe) hit.getNode());
		return recipes;
	}

	public static List<CategoryModel> filterCategories(List<SearchHit> hits) {
		List<CategoryModel> categories = new ArrayList<CategoryModel>();
		for (SearchHit hit : hits)
			categories.add((CategoryModel) hit.getNode());
		return categories;
	}

	public static List<SearchHit> filterFaqHits(Collection<SearchHit> hits) {
		List<SearchHit> ret = new ArrayList<SearchHit>();
		for (SearchHit hit : hits) {
			if (FDContentTypes.FAQ.equals(hit.getContentKey().getType())) {
				Faq faq = (Faq) hit.getNode();
				if (faq!=null && !faq.isOrphan() && !faq.isHidden() && faq.isSearchable())
					ret.add(hit);
			}
		}
		return ret;
	}

	public static List<Faq> filterFaqs(List<SearchHit> faqHits) {
		List<Faq> faqs = new ArrayList<Faq>();
		for (SearchHit hit : faqHits)
			faqs.add((Faq) hit.getNode());
		return faqs;
	}

	public static boolean isQuoted(String term) {
		return term.length() > 1 
			&& ((term.charAt(0) == '"' && term.charAt(term.length() - 1) == '"')
			|| (term.charAt(0) == '\'' && term.charAt(term.length() - 1) == '\''));
	}

	public static String removeQuotes(String term) {
		return term.substring(1, term.length() - 1);
	}

	public static Set<ContentKey> extractContentKeys(Collection<SearchHit> hits) {
		Set<ContentKey> keys = new HashSet<ContentKey>();
		for (SearchHit hit : hits)
			keys.add(hit.getContentKey());
		return keys;
	}

	public static String quote(boolean quoted, String phrase) {
		if (quoted)
			return "\"" + phrase + "\"";
		else
			return phrase;
	}
}

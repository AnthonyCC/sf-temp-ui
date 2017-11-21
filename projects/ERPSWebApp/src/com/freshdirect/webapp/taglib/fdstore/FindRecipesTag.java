package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.AllPredicate;
import org.apache.commons.collections.functors.OrPredicate;
import org.apache.commons.collections.functors.TransformedPredicate;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentTypes;
import com.freshdirect.cms.core.domain.Relationship;
import com.freshdirect.fdstore.util.RecipesUtil;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.storeapi.ContentNodeI;
import com.freshdirect.storeapi.StoreServiceLocator;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.Domain;
import com.freshdirect.storeapi.content.DomainValue;
import com.freshdirect.storeapi.content.Recipe;
import com.freshdirect.storeapi.content.RecipeSearchCriteria;
import com.freshdirect.storeapi.content.RecipeSearchPage;
import com.freshdirect.storeapi.fdstore.FDContentTypes;
import com.freshdirect.storeapi.query.AttributeEqualsPredicate;
import com.freshdirect.storeapi.query.ContentKeysPredicate;
import com.freshdirect.storeapi.query.RelationshipContainsAllPredicate;
import com.freshdirect.storeapi.query.RelationshipContainsAnyPredicate;
import com.freshdirect.storeapi.query.RelationshipLookupTransformer;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class FindRecipesTag extends AbstractGetterTag<List<Recipe>> {

	private static final long	serialVersionUID	= -5525892453864868480L;

	@Override
    protected List<Recipe> getResult() throws Exception {

		ServletRequest    request = pageContext.getRequest();

		if (request.getParameter("edit") != null) {
			return null;
		}

		ContentFactory	    cf          = ContentFactory.getInstance();
		CmsManager          manager     = CmsManager.getInstance();

		RecipeSearchPage searchPage = RecipeSearchPage.getDefault();

		/** Set of Domain */
		Set<Domain> allDomains = new HashSet<Domain>();

		/** Set of ContentKeys (of DomainValues) */
		Set<ContentKey> andKeys = new HashSet<ContentKey>();

		/** List of Predicate */
		List<Predicate> predicates = new ArrayList<Predicate>();

		//
		// handle dropDown (single-selection) criteria
		//

		List<RecipeSearchCriteria> dropDownCriteria = searchPage.getCriteriaBySelectionType(RecipeSearchCriteria.TYPE_ONE);

		for ( RecipeSearchCriteria criteria : dropDownCriteria ) {
			String               id       = criteria.getContentName();
			String               attr     = request.getParameter(id);

			if (attr != null && attr.length() > 0) {
				ContentKey dvKey = ContentKeyFactory.get(FDContentTypes.DOMAINVALUE, attr);
				DomainValue dv = cf.getDomainValueById(attr);
				if (dv != null) {
					andKeys.add(dvKey);
					allDomains.add(dv.getDomain());
				}
			}
		}

		//
		// handle checkBox (multiple-selection) criteria
		//

		List<RecipeSearchCriteria>  checkBoxCriteria = searchPage.getCriteriaBySelectionType(RecipeSearchCriteria.TYPE_MANY);

		for ( RecipeSearchCriteria criteria : checkBoxCriteria ) {
			String               id       = criteria.getContentName();
			String               attr[]   = request.getParameterValues(id);

			if (attr != null) {
				boolean andOp = RecipeSearchCriteria.OPERATION_AND.equals(criteria.getLogicalOperation());
				Set<ContentKey> orKeys = andOp ? null : new HashSet<ContentKey>();
				for (int i = 0; i < attr.length; ++i) {
					if (attr[i].length() > 0) {
						ContentKey domainValue = ContentKeyFactory.get(FDContentTypes.DOMAINVALUE, attr[i]);
						DomainValue dv = cf.getDomainValueById(attr[i]);
						if (dv != null) {
							if (andOp) {
								andKeys.add(domainValue);
							} else {
								orKeys.add(domainValue);
							}
							allDomains.add(dv.getDomain());
						}
					}
				}
				if (orKeys != null) {
					predicates.add(new RelationshipContainsAnyPredicate((Relationship) ContentTypes.Recipe.classifications, orKeys));
				}
			}
		}

		if (!andKeys.isEmpty()) {
			predicates.add(new RelationshipContainsAllPredicate((Relationship) ContentTypes.Recipe.classifications, andKeys));
		}


		//
		// filter by RecipeSource
		//
		String recipeSource = NVL.apply(request.getParameter("recipeSource"), "");
		if (!"".equals(recipeSource)) {
			ContentKey key = ContentKeyFactory.get(FDContentTypes.RECIPE_SOURCE, recipeSource);
			// AttributeDefI atrDef = recipeDef.getAttributeDef("source");
			Predicate p = new AttributeEqualsPredicate(ContentTypes.Recipe.source, key);
			predicates.add(p);
		}

		//
		// filter by RecipeAuthor
		//
		String recipeAuthor = NVL.apply(request.getParameter("recipeAuthor"), "");
		if (!"".equals(recipeAuthor)) {
			ContentKey key = ContentKeyFactory.get(FDContentTypes.RECIPE_AUTHOR, recipeAuthor);
			Set<ContentKey> s = Collections.singleton(key);
			// predicate for recipe.author
			Predicate p1 = new RelationshipContainsAllPredicate((Relationship)ContentTypes.Recipe.authors, s);

			// predicate for recipe.source.author
			Transformer sourceLookup = new RelationshipLookupTransformer((Relationship)ContentTypes.Recipe.source);
			Predicate p2 = new TransformedPredicate(sourceLookup, new RelationshipContainsAllPredicate((Relationship)ContentTypes.RecipeSource.authors, s));

			Predicate p = new OrPredicate(p1, p2);
			predicates.add(p);
		}


		//
		// get the keyword predicate
		//

		String keyword = NVL.apply(request.getParameter("keyword"), "");
		keyword = keyword.trim();
		if (!"".equals(keyword)) {
			Set<ContentKey> keys = searchRecipes(keyword);
			Predicate p = new ContentKeysPredicate(keys);
			predicates.add(p);
		}

		//
		// perform search
		//

		if (predicates.isEmpty()) {
			return null;
		}

		Predicate[] preds = predicates.toArray(new Predicate[predicates.size()]);
		Predicate searchPredicate = new AllPredicate(preds);
        Map<ContentKey, ContentNodeI> result = manager.queryContentNodes(FDContentTypes.RECIPE, searchPredicate);

		List<Recipe> recipes = new ArrayList<Recipe>();

		for ( ContentKey key : result.keySet() ) {
			Recipe recipe = (Recipe) cf.getContentNode(key.getId());
			if (recipe.isAvailable() && recipe.isActive()) {
				recipes.add(recipe);
			}
		}

		Collections.sort(recipes, new Comparator<Recipe>() {
			@Override
            public int compare(Recipe r1, Recipe r2) {
				return r1.getName().compareTo(r2.getName());
			}
		});

		List<DomainValue> classifications = RecipesUtil.collectClassifications(allDomains, new HashSet<Domain>(searchPage.getFilterByDomains()), recipes);
		pageContext.setAttribute("classifications", classifications);

		// filter the result set by the filter if supplied
		String filter = request.getParameter("filter");
		List<Recipe> recs = new ArrayList<Recipe>();
		if (filter != null && filter.length() > 0) {
			for ( Recipe recipe : recipes ) {
				List<DomainValue> cls = recipe.getClassifications();
				for ( DomainValue cl : cls ) {
					if (filter.equals(cl.getContentKey().getId())) {
						recs.add(recipe);
						break;
					}
				}
			}
		} else {
			recs = recipes;
		}

		return recs;
	}

	/**
	 *
	 * @param keyword
	 * @return Set<ContentKey>
	 */
	private Set<ContentKey> searchRecipes(String keyword) {
        return new HashSet<ContentKey>(StoreServiceLocator.contentSearch().searchRecipes(keyword));
	}


	public static class TagEI extends AbstractGetterTag.TagEI {

		@Override
        protected String getResultType() {
			return List.class.getName();
		}
	}


}

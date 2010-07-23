package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Collection;
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

import com.freshdirect.cms.AttributeDefI;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.ContentTypeDefI;
import com.freshdirect.cms.RelationshipDefI;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.application.ContentTypeServiceI;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.cms.query.AttributeEqualsPredicate;
import com.freshdirect.cms.query.ContentKeysPredicate;
import com.freshdirect.cms.query.RelationshipContainsAllPredicate;
import com.freshdirect.cms.query.RelationshipContainsAnyPredicate;
import com.freshdirect.cms.query.RelationshipLookupTransformer;
import com.freshdirect.cms.search.SearchHit;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentSearchUtil;
import com.freshdirect.fdstore.content.Domain;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.RecipeSearchCriteria;
import com.freshdirect.fdstore.content.RecipeSearchPage;
import com.freshdirect.fdstore.content.SearchQueryStemmer;
import com.freshdirect.fdstore.util.RecipesUtil;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class FindRecipesTag extends AbstractGetterTag<List<Recipe>> {

	private static final long	serialVersionUID	= -5525892453864868480L;

	protected List<Recipe> getResult() throws Exception {

		ServletRequest    request = pageContext.getRequest();
		
		if (request.getParameter("edit") != null) {
			return null;
		}

		ContentFactory	    cf          = ContentFactory.getInstance();
		CmsManager          manager     = CmsManager.getInstance();
		ContentTypeServiceI typeService = manager.getTypeService();
		ContentTypeDefI		recipeDef     = typeService.getContentTypeDefinition(FDContentTypes.RECIPE);
		RelationshipDefI classificationDef = (RelationshipDefI) recipeDef.getAttributeDef("classifications");
		
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
				ContentKey dvKey = new ContentKey(FDContentTypes.DOMAINVALUE, attr);
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
						ContentKey domainValue = new ContentKey(FDContentTypes.DOMAINVALUE, attr[i]);
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
					predicates.add(new RelationshipContainsAnyPredicate(classificationDef, orKeys));
				}
			}
		}
		
		if (!andKeys.isEmpty()) {
			predicates.add(new RelationshipContainsAllPredicate(classificationDef, andKeys));
		}
	
		
		//
		// filter by RecipeSource
		//
		String recipeSource = NVL.apply(request.getParameter("recipeSource"), "");
		if (!"".equals(recipeSource)) {
			ContentKey key = new ContentKey(FDContentTypes.RECIPE_SOURCE, recipeSource);
			AttributeDefI atrDef = recipeDef.getAttributeDef("source");
			Predicate p = new AttributeEqualsPredicate(atrDef, key);
			predicates.add(p);
		}
		
		//
		// filter by RecipeAuthor
		//
		String recipeAuthor = NVL.apply(request.getParameter("recipeAuthor"), "");
		if (!"".equals(recipeAuthor)) {
			ContentKey key = new ContentKey(FDContentTypes.RECIPE_AUTHOR, recipeAuthor);
			Set<ContentKey> s = Collections.singleton(key);
			// predicate for recipe.author
			RelationshipDefI relDef = (RelationshipDefI) recipeDef.getAttributeDef("authors");
			Predicate p1 = new RelationshipContainsAllPredicate(relDef, s);
			
			// predicate for recipe.source.author
			ContentTypeDefI recipeSourceDef = typeService.getContentTypeDefinition(FDContentTypes.RECIPE_SOURCE);
			relDef = (RelationshipDefI) recipeSourceDef.getAttributeDef("authors");
			Transformer sourceLookup = new RelationshipLookupTransformer((RelationshipDefI) recipeDef.getAttributeDef("source"));
			Predicate p2 = new TransformedPredicate(sourceLookup, new RelationshipContainsAllPredicate(relDef, s));
			
			Predicate p = new OrPredicate(p1, p2);
			predicates.add(p);
		}		
		
		
		//
		// get the keyword predicate
		//
		
		String keyword = NVL.apply(request.getParameter("keyword"), "");
		keyword = ContentSearchUtil.normalizeTerm(keyword);
		if (!"".equals(keyword)) {
			// TODO refactor lucene-based predicate search
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
		Map<ContentKey,ContentNodeI> result = manager.queryContentNodes(FDContentTypes.RECIPE, searchPredicate);
				
		List<Recipe> recipes = new ArrayList<Recipe>();
		
		for ( ContentKey key : result.keySet() ) {
			Recipe recipe = (Recipe) cf.getContentNode(key.getId());
			if (recipe.isAvailable() && recipe.isActive()) {
				recipes.add(recipe);
			}
		}
		
		Collections.sort(recipes, new Comparator<Recipe>() {
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
		Collection<SearchHit> hits = CmsManager.getInstance().search(keyword, 2000);

		Map<ContentType,List<SearchHit>> hitsByType = ContentSearchUtil.mapHitsByType(hits);

		String[] tokens = ContentSearchUtil.tokenizeTerm(keyword);

		// TODO : refactor, this way, we load ContentNodes twice, here, and in the upper method.
		List<SearchHit> recipes = ContentSearchUtil.filterRelevantNodes(ContentSearchUtil.resolveHits(hitsByType.get(FDContentTypes.RECIPE)), tokens, SearchQueryStemmer.LowerCase);
		
		Set<ContentKey> keys = new HashSet<ContentKey>(recipes.size());
		for ( SearchHit r : recipes ) {
			keys.add(r.getContentKey());
		}
		
		return keys;
	}


	public static class TagEI extends AbstractGetterTag.TagEI {

		protected String getResultType() {
			return List.class.getName();
		}
	}


}

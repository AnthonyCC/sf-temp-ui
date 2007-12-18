/**
 * 
 */
package com.freshdirect.webapp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.RecipeSubcategory;

/**
 * Utilities for recepies filtering. (Refactoring)
 * 
 * @author kocka
 *
 */
public class RecipesUtil {

	private static final String REQUEST_ATTR_FILTERBYKEYS = "com.freshdirect.recipe.filterbykeys";
	private static final String REQUEST_ATTR_RECIPES = "com.freshdirect.recipe.recipes";
	
	/**
	 * Cached 
	 * @param recipeSubCat
	 * @param filter
	 * @param filterByKeys
	 * @param request
	 * @return
	 */
	public static Map findRecipes(RecipeSubcategory recipeSubCat, DomainValue filter, List filterByKeys, ServletRequest request) {
		if(request.getAttribute(REQUEST_ATTR_FILTERBYKEYS) == null || request.getAttribute(REQUEST_ATTR_RECIPES) == null) {
			Map ret = findRecipes(recipeSubCat, filter, filterByKeys);
			request.setAttribute(REQUEST_ATTR_FILTERBYKEYS, filterByKeys);
			request.setAttribute(REQUEST_ATTR_RECIPES, ret);
			return ret;
		} else {
			filterByKeys.clear();
			List cachedFilterBykeys = (List) request.getAttribute(REQUEST_ATTR_FILTERBYKEYS);
			filterByKeys.addAll(cachedFilterBykeys);
			return (Map) request.getAttribute(REQUEST_ATTR_RECIPES);
		}
	}

	
	/**
	 * Find all {@link Recipe}s in the given {@link RecipeSubcategory} matching the specified (optional) filter criteria.
	 * 
	 * @param recipeSubCat recipe subcategory (never null)
	 * @param filter recipe classification domain value (optional)
	 * @param filterByKeys mutable List of DomainValue (never null). This list gets narrowed to the filters applicable to the recipes found
	 * 
	 * @return Map of DomainValue (classification) -> List of {@link Recipe} (recipes with this classification).
	 * 
	 * @FIXME returned map contains "noGroupBy" string key. use null-key
	 *  instead.
	 */
	public static Map findRecipes(RecipeSubcategory recipeSubCat, DomainValue filter, List filterByKeys) {
		Map groupByMap = new HashMap();

		// go get all the recipes
		ContentType cType = ContentType.get("Recipe");
		Set allRecipeKeys = CmsManager.getInstance().getContentKeysByType(cType);

		List groupBy = recipeSubCat.getGroupBy();

		// List of DomainValue - all classifications (before filtering applied)
		Set classifications = new HashSet();

		boolean isFilterInGroup = filter==null ? false : groupBy.contains(filter);
		for (Iterator setItr = allRecipeKeys.iterator();setItr.hasNext();) {
			ContentKey cKey = (ContentKey)setItr.next();
			Recipe recipe =   (Recipe)ContentFactory.getInstance().getContentNode(cKey.getId());
			List recipeClassifications = recipe.getClassifications();
			// skip it if recipe not in Subcategories classification?
	        if (!recipe.isActive() || !recipeClassifications.contains(recipeSubCat.getClassification())) {
				continue;
			}
			
			//ok, is it available...
	        if (!recipe.isAvailable()) {
				continue;
			}

			classifications.addAll( recipeClassifications );
			
			// skip it if filter specified and recipe not in filter classification?
			if (filter!=null && !recipeClassifications.contains(filter)) continue;
			

			// place recipe in participating groups... if no groupbys, then make a dummy group entry in the map
			if (groupBy==null || groupBy.size() < 1) {
				List recipeList = (List)groupByMap.get("noGroupBy");
				if (recipeList==null) {
					recipeList=new ArrayList();
					groupByMap.put("noGroupBy",recipeList);
				}
				recipeList.add(recipe);
			} else {
				List recipeList = null;
				boolean inAGroup=false;
				//creative says to let recipe appear in multipe group by's.  if they reconsider, then add inAGroup to loop condition
				for(Iterator lItr = groupBy.iterator(); lItr.hasNext();) {
					DomainValue dvGroupBy = (DomainValue)lItr.next();
					if (isFilterInGroup && !dvGroupBy.equals(filter)){
						if (recipeClassifications.contains(dvGroupBy))	{
							inAGroup=true;
						}
						continue;
					}
					
					if (recipeClassifications.contains(dvGroupBy)) {
					   recipeList = (List)groupByMap.get(dvGroupBy);
					   inAGroup=true;
					   if (recipeList==null) {
						recipeList=new ArrayList();
						groupByMap.put(dvGroupBy,recipeList);
					   }
					   recipeList.add(recipe);
					}

				}
				if (!inAGroup) {
					recipeList = (List)groupByMap.get("Other");
					if (recipeList==null) {
					   recipeList=new ArrayList();
					   groupByMap.put("Other",recipeList);
					}
					
					recipeList.add(recipe);
				}
				
			}
		}
		
		// retain only matching domainvalues
		filterByKeys.retainAll( classifications );
		 
		return groupByMap;
	}

	public static String getFilterParam(ServletRequest request) {
		return request.getParameter("filter")!=null ?  request.getParameter("filter") : "";
	}
	
	public static DomainValue getFilter(List filterByKeys, ServletRequest request){
		DomainValue filter = null;
		for (Iterator i = filterByKeys.iterator(); i.hasNext(); ) {
			DomainValue dv = (DomainValue) i.next();
			if (dv.getContentName().equals(getFilterParam(request)) ) { 
				filter = dv;
				break;
			}
		}
		return filter;
	}
	
}

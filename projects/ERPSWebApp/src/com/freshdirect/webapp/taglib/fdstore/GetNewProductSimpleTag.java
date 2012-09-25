package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.EnumFilteringValue;
import com.freshdirect.fdstore.content.EnumSortingValue;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.framework.util.DateUtil;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

public class GetNewProductSimpleTag extends AbstractGetterTag<SearchResults> {

	private static final long serialVersionUID = 2165436398381451042L;
	
	public static class TagEI extends AbstractGetterTag.TagEI {
		
		@Override
		public VariableInfo[] getVariableInfo(TagData data) {

			return new VariableInfo[] {
				new VariableInfo(
					data.getAttributeString("id"),
					this.getResultType(),
					true,
					getScope() ),
				new VariableInfo(
					data.getAttributeString("featuredCategory"),
					CategoryModel.class.getName(),
					true,
					getScope() )	
			};

		}
		
		@Override
		protected String getResultType() {
			return SearchResults.class.getName();
		}
		
		@Override
		protected int getScope() {
			return VariableInfo.AT_END;
		}
	}
	
	protected FilteringNavigator nav;
	private String featuredCategory;

	@Override
	protected SearchResults getResult() throws Exception {
		Date now = new Date();
		List<FilteringSortingItem<ProductModel>> items = new ArrayList<FilteringSortingItem<ProductModel>>(1000);
		Map<ProductModel, Date> newProducts = ContentFactory.getInstance().getNewProducts();
		for (Entry<ProductModel, Date> entry : newProducts.entrySet())
			items.add(new FilteringSortingItem<ProductModel>(entry.getKey()).putSortingValue(EnumSortingValue.NEWNESS, DateUtil.diffInDays(now, entry.getValue())));
		newProducts = ContentFactory.getInstance().getBackInStockProducts();
		for (Entry<ProductModel, Date> entry : newProducts.entrySet())
			items.add(new FilteringSortingItem<ProductModel>(entry.getKey()).putSortingValue(EnumSortingValue.NEWNESS, DateUtil.diffInDays(now, entry.getValue())));
		SearchResults results = new SearchResults(items, FilteringSortingItem.<Recipe> emptyList(),
				FilteringSortingItem.<CategoryModel> emptyList(), null, false);
		
		extractFeaturedCat();
		
		return results;
	}
	
	private void extractFeaturedCat(){
		
		CategoryModel featuredCategory = (CategoryModel) ContentFactory.getInstance().getContentNode("Category", FDStoreProperties.getNewProductsCatId());
		if (nav.getFilterValues().get(EnumFilteringValue.CAT) != null) {
			CategoryModel category = (CategoryModel) ContentFactory.getInstance().getContentNode("Category", nav.getFilterValues().get(EnumFilteringValue.CAT).get(0).toString());
			if (category.getCategoryLabel() != null)
				featuredCategory = category;
		}
		if (featuredCategory.getCategoryLabel() == null)
			featuredCategory = null;
		
		pageContext.setAttribute(this.featuredCategory, featuredCategory);
	}
	
	public FilteringNavigator getNav() {
		return nav;
	}

	public void setNav(FilteringNavigator nav) {
		this.nav = nav;
	}

	public String getFeaturedCategory() {
		return featuredCategory;
	}

	public void setFeaturedCategory(String featuredCategory) {
		this.featuredCategory = featuredCategory;
	}

}

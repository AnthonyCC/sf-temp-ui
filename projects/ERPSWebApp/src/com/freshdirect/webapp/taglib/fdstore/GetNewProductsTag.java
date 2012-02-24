package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.CategoryNodeTree;
import com.freshdirect.fdstore.content.ComparatorChain;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.EnumSortingValue;
import com.freshdirect.fdstore.content.ProductContainer;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.SearchResultItem;
import com.freshdirect.fdstore.content.SearchResults;
import com.freshdirect.fdstore.content.SearchSortType;
import com.freshdirect.fdstore.content.SortValueComparator;
import com.freshdirect.fdstore.content.ContentNodeTree.TreeElement;
import com.freshdirect.fdstore.util.NewProductsGrouping;
import com.freshdirect.fdstore.util.ProductPagerNavigator;
import com.freshdirect.fdstore.util.TimeRange;
import com.freshdirect.framework.util.DateUtil;

public class GetNewProductsTag extends AbstractProductPagerTag {
	private static final long serialVersionUID = 4285691979496555198L;

	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] { new VariableInfo(data.getAttributeString("id"), GetNewProductsTag.class.getName(), true,
					VariableInfo.NESTED) };
		}
	}

	private boolean simpleView = false;
	private SortedMap<TimeRange, List<ProductModel>> groupings;
	private NewProductsGrouping groupingTool;
	private CategoryModel featuredCategory;
	private boolean showFeatured;

	@Override
	protected SearchResults getResults() {
		Date now = new Date();
		List<SearchResultItem<ProductModel>> items = new ArrayList<SearchResultItem<ProductModel>>(1000);
		Map<ProductModel, Date> newProducts = ContentFactory.getInstance().getNewProducts();
		for (Entry<ProductModel, Date> entry : newProducts.entrySet())
			items.add(new SearchResultItem<ProductModel>(entry.getKey()).putSortingValue(EnumSortingValue.NEWNESS, DateUtil.diffInDays(now, entry.getValue())));
		newProducts = ContentFactory.getInstance().getBackInStockProducts();
		for (Entry<ProductModel, Date> entry : newProducts.entrySet())
			items.add(new SearchResultItem<ProductModel>(entry.getKey()).putSortingValue(EnumSortingValue.NEWNESS, DateUtil.diffInDays(now, entry.getValue())));
		SearchResults results = new SearchResults(items, SearchResultItem.<Recipe> emptyList(),
				SearchResultItem.<CategoryModel> emptyList(), null, false);
		return results;
	}

	@Override
	protected Comparator<SearchResultItem<ProductModel>> getProductSorter(List<SearchResultItem<ProductModel>> products,
			SearchSortType sortBy, boolean ascending) {
		Comparator<SearchResultItem<ProductModel>> comparator;
		switch (sortBy) {
			case BY_NAME:
				comparator = SearchResultItem.wrap(ProductModel.FULL_NAME_PRODUCT_COMPARATOR);
				if (!ascending)
					comparator = Collections.reverseOrder(comparator);
				break;
			case BY_PRICE:
				Comparator<ProductModel> priceComparator = ProductModel.GENERIC_PRICE_COMPARATOR;
				if (!ascending)
					priceComparator = Collections.reverseOrder(priceComparator);
				comparator = SearchResultItem.wrap(ComparatorChain.create(priceComparator).chain(ProductModel.FULL_NAME_PRODUCT_COMPARATOR));
				break;
			case BY_RECENCY:
			default:
				if (isShowGrouped())
					comparator = ComparatorChain.create(getGroupingTool().getTimeRangeComparator()).chain(SearchResultItem.wrap(ProductModel.DEPTFULL_COMPARATOR))
							.chain(SearchResultItem.wrap(ProductModel.FULL_NAME_PRODUCT_COMPARATOR));
				else
					comparator = new SortValueComparator<ProductModel>(EnumSortingValue.NEWNESS);
				if (!ascending)
					comparator = Collections.reverseOrder(comparator);
		}
		
		return comparator;
	}
	
	@Override
	protected ProductContainer filterProductsByCategory(ProductPagerNavigator nav, CategoryNodeTree categoryTree,
			SearchResults results) {
		// stupid hack to use the special filtering code on DFGS screen only. sorry
		// two things are mixed already and difficult to separate
		// see AbstractProductPagerTag
		if (!simpleView)
			return super.filterProductsByCategory(nav, categoryTree, results);
		String parentId = nav.getDepartment();
		ProductContainer parentContainer = null;
		TreeElement parentElement = null;
		if (parentId != null) {
			parentElement = categoryTree.getTreeElement(parentId);
			
			if (parentElement == null) {
				results.emptyProducts();	
				return null;
			}
		}
		if (parentElement != null)
			parentContainer = (ProductContainer) parentElement.getModel();

		if (results.getProducts().isEmpty())
			return parentContainer; // nothing to do
	
		if (nav.isRecipesFiltered())
			results.emptyProducts();
		
		if (parentElement != null) {
			Set<ContentKey> filtered = parentElement.getAllChildren();
			Iterator<SearchResultItem<ProductModel>> it = results.getProducts().iterator();
			while (it.hasNext()) {
				SearchResultItem<ProductModel> item = it.next();
				if (!filtered.contains(item.getModel().getContentKey()))
					it.remove();
			}
		}
		
		return parentContainer;
	}

	@Override
	protected void postProcess(SearchResults results) {
		// create groups if needed
		if (isShowGrouped())
			groupings = getGroupingTool().groupBy(getPageProductsInternal());
		else {
			groupings = new TreeMap<TimeRange, List<ProductModel>>();
			groupings.put(new TimeRange(1, 0, 6, TimeRange.MONTH, TimeRange.NULL), getPageProducts());
		}
		
		if (nav.isRefined())
			showFeatured = false;
		else
			showFeatured = true;
		
		featuredCategory = (CategoryModel) ContentFactory.getInstance().getContentNode("Category", FDStoreProperties.getNewProductsCatId());
		if (nav.getCategory() != null) {
			CategoryModel category = (CategoryModel) ContentFactory.getInstance().getContentNode("Category", nav.getCategory());
			if (category.getCategoryLabel() != null)
				featuredCategory = category;
		}
		if (featuredCategory.getCategoryLabel() == null)
			featuredCategory = null;
	}

	public boolean isShowGrouped() {
		return !simpleView && nav.getSortBy().equals(SearchSortType.BY_RECENCY)
				&& nav.getBrand() == null && nav.getDepartment() == null
				&& (nav.getCategory() == null || nav.getCategory().equals(FDStoreProperties.getNewProductsCatId()));
	}

	public void setSimpleView(boolean simpleView) {
		this.simpleView = simpleView;
	}

	public boolean isSimpleView() {
		return simpleView;
	}

	public CategoryModel getFeaturedCategory() {
		return featuredCategory;
	}

	public SortedMap<TimeRange, List<ProductModel>> getGroupings() {
		return groupings;
	}
	
	public boolean isShowFeatured() {
		return showFeatured;
	}
	
	public NewProductsGrouping getGroupingTool() {
		if (groupingTool == null)
			groupingTool = new NewProductsGrouping(!nav.isSortOrderingAscending());
		return groupingTool;
	}
}

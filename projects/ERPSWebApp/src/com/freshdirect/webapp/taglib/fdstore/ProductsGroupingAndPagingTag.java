package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.SearchSortType;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.fdstore.util.FilteringNavigator;
import com.freshdirect.fdstore.util.NewProductsGrouping;
import com.freshdirect.fdstore.util.TimeRange;
import com.freshdirect.framework.webapp.BodyTagSupportEx;
import com.freshdirect.storeapi.content.EnumSearchFilteringValue;
import com.freshdirect.storeapi.content.FilteringSortingItem;
import com.freshdirect.storeapi.content.ProductModel;

public class ProductsGroupingAndPagingTag extends BodyTagSupportEx {

	private static final long serialVersionUID = -677797725563207080L;

	private String itemsId;
	private String groupingsId;
	private List<FilteringSortingItem<ProductModel>> items;
	private FilteringNavigator nav;

	private NewProductsGrouping groupingTool;
	private SortedMap<TimeRange, List<ProductModel>> groupings;
	private boolean simpleView = false;
	private List<FilteringSortingItem<ProductModel>> pageProducts = Collections.emptyList();
	private List<ProductModel> pageProductsUnwrap = null;

	@Override
	public int doStartTag() throws JspException {

		createProductPageWindow(); 

		if (groupingsId!=null) {

			if (isShowGrouped())
				groupings = getGroupingTool().groupBy(getPageProductsInternal());
			else {
				groupings = new TreeMap<TimeRange, List<ProductModel>>();
				groupings.put(new TimeRange(1, 0, 6, TimeRange.MONTH, TimeRange.NULL), getPageProducts());
			}
			pageContext.setAttribute(groupingsId, groupings);			
		}

		pageContext.setAttribute(itemsId, pageProducts);

		return EVAL_BODY_INCLUDE;

	}

	private void createProductPageWindow() {
		int pageSize = nav.getPageSize();
		int pageOffset = nav.getPageOffset();

		pageProducts = new ArrayList<FilteringSortingItem<ProductModel>>(pageSize <= 0 ? items.size() : pageSize);

		int noOfPagedProducts = items.size();
		int pageCount = pageSize == 0 ? 1 : noOfPagedProducts / pageSize;
		if (pageSize != 0 && noOfPagedProducts % pageSize > 0) {
			pageCount++;

		}
		int max = pageSize == 0 ? pageOffset + noOfPagedProducts : pageOffset + pageSize;
		for (int i = pageOffset; i < max; i++) {
			if (i >= items.size()) {
				break;
			}
			pageProducts.add(items.get(i));
		}
	}

	private List<FilteringSortingItem<ProductModel>> getPageProductsInternal() {
		pageProductsUnwrap = null; // invalidate
		return pageProducts;
	}

	public List<ProductModel> getPageProducts() {
		if (pageProductsUnwrap == null)
			pageProductsUnwrap = ProductPricingFactory.getInstance().getPricingAdapter(
                    FilteringSortingItem.unwrap(pageProducts));
		return pageProductsUnwrap;
	}

	private NewProductsGrouping getGroupingTool() {
		if (groupingTool == null)
			groupingTool = new NewProductsGrouping(!nav.isSortOrderingAscending());
		return groupingTool;
	}

	// FIXME comparing a List<Object> to a String with equals() is kind of pointless... I'm not sure about the original intention here.
	private boolean isShowGrouped() {
		return 
				!simpleView && 
				nav.getSortBy().equals(SearchSortType.BY_RECENCY) && 
				nav.getFilterValues().get(EnumSearchFilteringValue.BRAND) == null && 
				nav.getFilterValues().get(EnumSearchFilteringValue.DEPT) == null && 
				(nav.getFilterValues().get(EnumSearchFilteringValue.CAT) == null || nav.getFilterValues().get(EnumSearchFilteringValue.CAT).equals(FDStoreProperties.getNewProductsCatId())
			);
	}

	public void setItems(List<FilteringSortingItem<ProductModel>> items) {
		this.items = items;
	}

	public void setItemsId(String itemsId) {
		this.itemsId = itemsId;
	}

	public void setNav(FilteringNavigator nav) {
		this.nav = nav;
	}

	public void setSimpleView(boolean simpleView) {
		this.simpleView = simpleView;
	}
	

	public void setGroupingsId(String groupingsId) {
		this.groupingsId = groupingsId;
	}

	public static class TagEI extends TagExtraInfo {
		@Override
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] { 
					new VariableInfo(
							data.getAttributeString("itemsId"),
							List.class.getName(),
							true,
							VariableInfo.NESTED),
					new VariableInfo(
							data.getAttributeString("groupingsId")!=null?data.getAttributeString("groupingsId"):"dummyMap",
							SortedMap.class.getName(),
							true,
							VariableInfo.NESTED)};
		}
	}

}

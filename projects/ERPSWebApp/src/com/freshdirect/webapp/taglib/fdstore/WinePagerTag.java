package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProductRatingGroup;
import com.freshdirect.fdstore.content.util.EnumWinePageSize;
import com.freshdirect.fdstore.content.util.QueryParameter;
import com.freshdirect.fdstore.content.util.QueryParameterCollection;
import com.freshdirect.fdstore.content.util.WineSorter;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.BodyTagSupportEx;
import com.freshdirect.webapp.taglib.QueryParserTag;

public class WinePagerTag extends BodyTagSupportEx {
	private static final long serialVersionUID = -1405148318861923116L;
	
	private static final String DEFAULT_PRODUCT_COUNT_ID = "wineProductCount";
	private static final String DEFAULT_PAGE_COUNT_ID = "winePageCount";
	private static final String DEFAULT_PAGE_SIZE_ID = "winePageSize";
	private static final String DEFAULT_PAGE_NO_ID = "winePageNo";
	private static final String DEFAULT_HAS_OTHER_RATINGS_ID = "wineHasOtherRatings";

	private String pagedProductsId;

	private String productCountId;

	private String pageCountId;
	
	private String pageSizeId;
	
	private String pageNoId;
	
	private String hasOtherRatingsId;

	public WinePagerTag() {
		productCountId = DEFAULT_PRODUCT_COUNT_ID;
		pageCountId = DEFAULT_PAGE_COUNT_ID;
		pageSizeId = DEFAULT_PAGE_SIZE_ID;
		pageNoId = DEFAULT_PAGE_NO_ID;
		hasOtherRatingsId = DEFAULT_HAS_OTHER_RATINGS_ID;
	}

	@Override
	public int doStartTag() throws JspException {
		QueryParserTag parent = (QueryParserTag) findAncestorWithClass(this, QueryParserTag.class);
		if (parent == null)
			throw new JspException("WineViewLink tag must be nested in a QueryParserTag tag");
		QueryParameterCollection qpc = (QueryParameterCollection) pageContext.getAttribute(parent.getQueryId());
		if (qpc == null)
			throw new JspException("there is something wrong with the parent QueryParserTag tag (query)");

		WineSorterTag parent2 = (WineSorterTag) findAncestorWithClass(this, WineSorterTag.class);
		if (parent2 == null)
			throw new JspException("WineViewLink tag must be nested in a WineSorter tag");
		WineSorter sorter = (WineSorter) pageContext.getAttribute(parent2.getSorterId());
		if (sorter == null)
			throw new JspException("there is something wrong with the parent WineSorter tag (sorter)");

		List<ProductRatingGroup> groups = sorter.getResults(); 
		if (groups == null)
			throw new JspException("wineProducts is not defined");

		EnumWinePageSize defaultPageSize = EnumWinePageSize.ALL;
		EnumWinePageSize currentPageSize;
		try {
			currentPageSize = EnumWinePageSize.valueOf(qpc.getParameterValue(QueryParameter.WINE_PAGE_SIZE));
		} catch (Exception e) {
			currentPageSize = defaultPageSize;
		}

		int productCount = ProductRatingGroup.productCount(groups);
		int pageCount = ProductRatingGroup.pageCount(groups, currentPageSize);

		long pageSize = currentPageSize.getSize();
		int defaultPage = 1;
		int currentPage;
		try {
			currentPage = Integer.valueOf(qpc.getParameterValue(QueryParameter.WINE_PAGE_NO));
		} catch (Exception e) {
			currentPage = defaultPage;
		}
		long productIndex = pageSize * (currentPage - 1);
		if (productIndex > productCount) {
			productIndex = 0;
			currentPage = 1;
		}
		List<ProductRatingGroup> page = new ArrayList<ProductRatingGroup>();
		
		// looking for the index of first group and product
		int i, j = 0;
		int pos = 0;
		for (i = 0; i < groups.size(); i++) {
			int size = groups.get(i).getProducts().size();
			if (pos + size > productIndex) {
				j = (int) productIndex - pos;
				break;
			}
			pos += size;
		}
		
		if (i == groups.size())
			page.addAll(groups);
		else {
			int remaining = (int) pageSize;
			for (; i < groups.size(); i++) {
				ProductRatingGroup current = groups.get(i);
				int size = current.getProducts().size();
				List<ProductModel> prods = current.getProducts().subList(j, Math.min(size, j + remaining));
				ProductRatingGroup group = new ProductRatingGroup(current.getRating(), prods.size());
				group.getProducts().addAll(prods);
				page.add(group);
				remaining -= prods.size();
				if (remaining <= 0) {
					break;
				}
				
				pos += size;
				j = 0;
			}
		}
		
		boolean hasOtherRatings = false;
		OUTER: for (ProductRatingGroup group : page)
			for (ProductModel p : group.getProducts())
				if (p.hasWineOtherRatings()) {
					hasOtherRatings = true;
					break OUTER;
				}

		pageContext.setAttribute(pagedProductsId, page);
		pageContext.setAttribute(productCountId, productCount);
		pageContext.setAttribute(pageCountId, pageCount);
		pageContext.setAttribute(pageSizeId, currentPageSize);
		pageContext.setAttribute(pageNoId, currentPage);
		pageContext.setAttribute(hasOtherRatingsId, hasOtherRatings);

		return EVAL_BODY_INCLUDE;
	}

	public String getProductCountId() {
		return productCountId;
	}

	public void setProductCountId(String productCountId) {
		this.productCountId = productCountId;
	}

	public String getPageCountId() {
		return pageCountId;
	}

	public void setPageCountId(String pageCountId) {
		this.pageCountId = pageCountId;
	}
	
	public String getPagedProductsId() {
		return pagedProductsId;
	}

	public void setPagedProductsId(String pagedProductsId) {
		this.pagedProductsId = pagedProductsId;
	}

	public String getPageSizeId() {
		return pageSizeId;
	}

	public void setPageSizeId(String pageSizeId) {
		this.pageSizeId = pageSizeId;
	}

	public String getPageNoId() {
		return pageNoId;
	}

	public void setPageNoId(String pageNoId) {
		this.pageNoId = pageNoId;
	}

	public String getHasOtherRatingsId() {
		return hasOtherRatingsId;
	}

	public void setHasOtherRatingsId(String hasOtherRatingsId) {
		this.hasOtherRatingsId = hasOtherRatingsId;
	}

	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
				new VariableInfo(data.getAttributeString("pagedProductsId"), 
						List.class.getName() + "<" + ProductRatingGroup.class.getName() + ">", true, VariableInfo.AT_BEGIN),
				declareVariableInfo(NVL.apply(data.getAttributeString("productCountId"), DEFAULT_PRODUCT_COUNT_ID),
						Integer.class, VariableInfo.NESTED),
				declareVariableInfo(NVL.apply(data.getAttributeString("pageCountId"), DEFAULT_PAGE_COUNT_ID),
						Integer.class, VariableInfo.NESTED),
				declareVariableInfo(NVL.apply(data.getAttributeString("pageSizeId"), DEFAULT_PAGE_SIZE_ID), 
						EnumWinePageSize.class, VariableInfo.NESTED),
				declareVariableInfo(NVL.apply(data.getAttributeString("pageNoId"), DEFAULT_PAGE_NO_ID),
						Integer.class, VariableInfo.NESTED),
				declareVariableInfo(NVL.apply(data.getAttributeString("hasOtherRatingsId"), DEFAULT_HAS_OTHER_RATINGS_ID),
						Boolean.class, VariableInfo.AT_BEGIN)
			};

		}
	}
}

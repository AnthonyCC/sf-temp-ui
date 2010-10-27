package com.freshdirect.webapp.taglib.fdstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.EnumWineFilterValueType;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.WineFilter;
import com.freshdirect.fdstore.content.WineFilterValue;
import com.freshdirect.fdstore.content.util.EnumWineViewType;
import com.freshdirect.fdstore.content.util.QueryParameter;
import com.freshdirect.fdstore.content.util.QueryParameterCollection;
import com.freshdirect.fdstore.content.util.WineSorter;
import com.freshdirect.fdstore.content.util.WineSorter.Type;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.BodyTagSupportEx;
import com.freshdirect.webapp.taglib.content.WineFilterTag;

public class WineSorterTag extends BodyTagSupportEx {
	private static final long serialVersionUID = 7126386560105151795L;
	
	private static final String DEFAULT_VIEW_ID = "wineView";
	private static final String DEFAULT_SORTY_BY_ID = "wineSortBy";
	private static final String DEFAULT_DEFAULT_SORT_BY_ID = "wineDefaultSortBy";

	private String sorterId;

	private String viewId;

	private String defaultSortById;

	private String sortById;

	public WineSorterTag() {
		viewId = DEFAULT_VIEW_ID;
		sortById = DEFAULT_SORTY_BY_ID;
		defaultSortById = DEFAULT_DEFAULT_SORT_BY_ID;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int doStartTag() throws JspException {
		WineFilterTag parent = (WineFilterTag) findAncestorWithClass(this, WineFilterTag.class);
		if (parent == null)
			throw new JspException("WineSorter tag must be nested in a WineFilter tag");
		WineFilter filter = (WineFilter) pageContext.getAttribute(parent.getFilterId());
		if (!parent.isUseItemGrabber() && filter == null)
			throw new JspException("there is something wrong with the parent WineFilter tag (filter)");
		QueryParameterCollection qpc = (QueryParameterCollection) pageContext.getAttribute(parent.getQueryId());
		if (qpc == null)
			throw new JspException("there is something wrong with the parent WineFilter tag (query)");

		String sortByParameter = request.getParameter(QueryParameter.WINE_SORT_BY);
		if (sortByParameter != null)
			qpc.addParameter(new QueryParameter(QueryParameter.WINE_SORT_BY, sortByParameter));

		Type defaultSortBy;
		if (parent.isUseItemGrabber()) {
			defaultSortBy = Type.ABC;
		} else {
			Set<WineFilterValue> filterValues = filter.getFilterValues();
			boolean sortByPrice = !filterValues.isEmpty();
			for (WineFilterValue value : filterValues)
				if (value.getWineFilterValueType() != EnumWineFilterValueType.RATING) {
					sortByPrice = false;
					break;
				}
			defaultSortBy = sortByPrice ? Type.PRICE : Type.EXPERT_RATING;
		}
		Type sortBy;
		try {
			sortBy = Type.valueOf(sortByParameter);
		} catch (Exception e) {
			sortBy = defaultSortBy;
		}

		String viewParameter = request.getParameter(QueryParameter.WINE_VIEW);
		String pageSizeParameter = request.getParameter(QueryParameter.WINE_PAGE_SIZE);
		String pageNoParameter = request.getParameter(QueryParameter.WINE_PAGE_NO);

		EnumWineViewType view = parent.isUseItemGrabber() ? EnumWineViewType.DETAILS : EnumWineViewType.COMPACT;
		if (viewParameter != null) {
			qpc.addParameter(new QueryParameter(QueryParameter.WINE_VIEW, viewParameter));
			try {
				view = EnumWineViewType.valueOf(qpc.getParameterValue(QueryParameter.WINE_VIEW));
			} catch (Exception e) {
			}
		}

		if (pageSizeParameter != null)
			qpc.addParameter(new QueryParameter(QueryParameter.WINE_PAGE_SIZE, pageSizeParameter));

		if (pageNoParameter != null)
			qpc.addParameter(new QueryParameter(QueryParameter.WINE_PAGE_NO, pageNoParameter));

		FDUserI user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
		WineSorter sorter;
		if (parent.isUseItemGrabber() && request.getAttribute("itemGrabberResult") != null) {
			Collection<ContentNodeModel> nodes = (Collection<ContentNodeModel>) request.getAttribute("itemGrabberResult");
			nodes = new ArrayList<ContentNodeModel>(nodes);
			Iterator<ContentNodeModel> it = nodes.iterator();
			while (it.hasNext())
				if (!FDContentTypes.PRODUCT.equals(it.next().getContentKey().getType()))
					it.remove();
			sorter = new WineSorter(user.getPricingContext(), sortBy, view, (Collection<ProductModel>) ((Collection<?>) nodes));
		} else
			sorter = new WineSorter(user.getPricingContext(), sortBy, view, filter);

		pageContext.setAttribute(sorterId, sorter);
		pageContext.setAttribute(viewId, view);
		pageContext.setAttribute(sortById, sortBy);
		pageContext.setAttribute(defaultSortById, defaultSortBy);

		return EVAL_BODY_INCLUDE;
	}

	public String getSorterId() {
		return sorterId;
	}

	public void setSorterId(String sorterId) {
		this.sorterId = sorterId;
	}

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	public String getDefaultSortById() {
		return defaultSortById;
	}

	public void setDefaultSortById(String defaultSortById) {
		this.defaultSortById = defaultSortById;
	}

	public String getSortById() {
		return sortById;
	}

	public void setSortById(String sortById) {
		this.sortById = sortById;
	}

	public static class TagEI extends TagExtraInfo {
		@Override
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
				declareVariableInfo(data.getAttributeString("sorterId"), WineSorter.class, VariableInfo.AT_BEGIN),
				declareVariableInfo(NVL.apply(data.getAttributeString("viewId"), DEFAULT_VIEW_ID),
						EnumWineViewType.class, VariableInfo.NESTED),
				declareVariableInfo(NVL.apply(data.getAttributeString("sortById"), DEFAULT_SORTY_BY_ID),
						WineSorter.Type.class, VariableInfo.NESTED),
				declareVariableInfo(NVL.apply(data.getAttributeString("defaultSortById"), DEFAULT_DEFAULT_SORT_BY_ID), 
						WineSorter.Type.class, VariableInfo.NESTED)
			};
		}
	}
}

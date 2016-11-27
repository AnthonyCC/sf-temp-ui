package com.freshdirect.webapp.taglib.content;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.WineFilter;
import com.freshdirect.fdstore.content.WineFilterValue;
import com.freshdirect.fdstore.content.util.QueryParameter;
import com.freshdirect.fdstore.content.util.QueryParameterCollection;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.webapp.BodyTagSupportEx;
import com.freshdirect.webapp.taglib.ParametersTag;
import com.freshdirect.webapp.taglib.QueryParserTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class WineFilterTag extends BodyTagSupportEx implements QueryParserTag {
	private static final long serialVersionUID = 3421165079629313555L;

	private String filterId;

	private String lastClickedId;

	private String queryId;
	
	private boolean useItemGrabber;

	@Override
	public int doStartTag() throws JspException {
		ParametersTag parent = (ParametersTag) findAncestorWithClass(this, ParametersTag.class);
		if (parent == null)
			throw new JspException("WineFilter tag must be nested in a Parameters tag");
		QueryParameterCollection qpc = parent.getQueryParameterCollection();
		if (qpc == null)
			throw new JspException("there is something wrong with the parent Parameters tag");
		ContentNodeModel contentNode = parent.getContentNode();
		WineFilter wineFilter = null;
		WineFilterValue lastClicked = null;

		String wineFilterParameter = request.getParameter(QueryParameter.WINE_FILTER);
		String wineFilterClickedParameter = request.getParameter(QueryParameter.WINE_FILTER_CLICKED);
		if (!useItemGrabber && wineFilterParameter != null) {
			FDUserI user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
			PricingContext pricingContext = user.getPricingContext();
			qpc.addParameter(new QueryParameter(QueryParameter.WINE_FILTER, wineFilterParameter));
			wineFilter = WineFilter.decode(pricingContext, wineFilterParameter);

			if (wineFilterClickedParameter != null) {
				qpc.addParameter(new QueryParameter(QueryParameter.WINE_FILTER_CLICKED, wineFilterClickedParameter));
				lastClicked = WineFilter.decodeFilterValue(wineFilterClickedParameter);
			}
		}

		pageContext.setAttribute(queryId, qpc);

		pageContext.setAttribute(lastClickedId, lastClicked);

		FDUserI user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
		PricingContext pricingContext = user.getPricingContext();
		WineFilter filter;
		if (wineFilter != null)
			filter = wineFilter;
		else {
			filter = new WineFilter(pricingContext);
			if (contentNode != null) {
				DomainValue dv = null;
				if (FDContentTypes.CATEGORY.equals(contentNode.getContentKey().getType())) {
					dv = (DomainValue) ContentFactory.getInstance().getDomainValueForWineCategory((CategoryModel) contentNode);
				} else if (FDContentTypes.PRODUCT.equals(contentNode.getContentKey().getType())) {
					ProductModel product = (ProductModel) contentNode;
					CategoryModel parentCategory;
					if (FDContentTypes.CATEGORY.equals(product.getParentNode().getContentKey().getType()))
						parentCategory = (CategoryModel) product.getParentNode();
					else
						parentCategory = product.getPrimaryHome();
					dv = (DomainValue) ContentFactory.getInstance().getDomainValueForWineCategory(parentCategory);
				}
				if (dv != null)
					filter.addFilterValue(dv);
			}
		}

		pageContext.setAttribute(filterId, filter);
		return EVAL_BODY_INCLUDE;
	}

	public String getFilterId() {
		return filterId;
	}

	public void setFilterId(String filterId) {
		this.filterId = filterId;
	}

	public String getLastClickedId() {
		return lastClickedId;
	}

	public void setLastClickedId(String lastClickedId) {
		this.lastClickedId = lastClickedId;
	}

	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public boolean isUseItemGrabber() {
		return useItemGrabber;
	}

	public void setUseItemGrabber(boolean useItemGrabber) {
		this.useItemGrabber = useItemGrabber;
	}

	public static class TagEI extends TagExtraInfo {
		@Override
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
					declareVariableInfo(data.getAttributeString("filterId"), WineFilter.class, VariableInfo.AT_BEGIN),
					declareVariableInfo(data.getAttributeString("lastClickedId"), WineFilterValue.class, VariableInfo.AT_BEGIN),
					declareVariableInfo(data.getAttributeString("queryId"), QueryParameterCollection.class, VariableInfo.AT_BEGIN) };
		}
	}
}

package com.freshdirect.webapp.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.util.QueryParameter;
import com.freshdirect.fdstore.content.util.QueryParameterCollection;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.webapp.BodyTagSupportEx;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class ParametersTag extends BodyTagSupportEx {
	private static final long serialVersionUID = -6790923380305613070L;

	private ContentNodeModel contentNode;
	private DepartmentModel department;
	private QueryParameterCollection qpc;
	private String trk;
	private String trkd;
	private FDUserI user;
	private PricingContext pricingContext;

	@Override
	public int doStartTag() throws JspException {
		qpc = new QueryParameterCollection();
		String productIdParameter = request.getParameter(QueryParameter.PRODUCT_ID);
		String catIdParameter = request.getParameter(QueryParameter.CAT_ID);
		String deptIdParameter = request.getParameter(QueryParameter.DEPT_ID);
		trk = request.getParameter(QueryParameter.TRK);
		trkd = request.getParameter(QueryParameter.TRKD);
		if (deptIdParameter != null) {
			qpc.addParameter(new QueryParameter(QueryParameter.DEPT_ID, deptIdParameter));
			contentNode = ContentFactory.getInstance().getContentNode(FDContentTypes.DEPARTMENT, deptIdParameter);
		} else if (catIdParameter != null) {
			qpc.addParameter(new QueryParameter(QueryParameter.CAT_ID, catIdParameter));
			if (productIdParameter != null) {
				qpc.addParameter(new QueryParameter(QueryParameter.PRODUCT_ID, productIdParameter));
				contentNode = ContentFactory.getInstance().getProductByName(catIdParameter, productIdParameter);
			} else
				contentNode = ContentFactory.getInstance().getContentNode(FDContentTypes.CATEGORY, catIdParameter);
		}
		if (contentNode != null) {
			ContentNodeModel node = contentNode;
			while (node != null && !node.getContentKey().getType().equals(FDContentTypes.DEPARTMENT))
				node = node.getParentNode();
			if (node != null && node.getContentKey().getType().equals(FDContentTypes.DEPARTMENT))
				department = (DepartmentModel) node;
		}
		user = (FDUserI) pageContext.getSession().getAttribute(SessionName.USER);
		if (user != null)
			pricingContext = user.getPricingContext();
		pageContext.setAttribute(id, this);
		return EVAL_BODY_INCLUDE;
	}

	public ContentNodeModel getContentNode() {
		return contentNode;
	}

	public DepartmentModel getDepartment() {
		return department;
	}

	public QueryParameterCollection getQueryParameterCollection() {
		return qpc;
	}

	public void setQueryParameterCollection(QueryParameterCollection qpc) {
		this.qpc = qpc;
	}

	public String getTrk() {
		return trk;
	}

	public String getTrkd() {
		return trkd;
	}

	public FDUserI getUser() {
		return user;
	}

	public PricingContext getPricingContext() {
		return pricingContext;
	}

	public static class TagEI extends TagExtraInfo {
		@Override
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] { declareVariableInfo(data.getAttributeString("id"), ParametersTag.class, VariableInfo.AT_BEGIN) };
		}
	}
}

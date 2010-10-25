package com.freshdirect.webapp.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.fdstore.content.EnumTrackingSource;
import com.freshdirect.fdstore.content.util.QueryParameter;
import com.freshdirect.fdstore.content.util.QueryParameterCollection;
import com.freshdirect.framework.webapp.BodyTagSupportEx;

public class TrackingQueryParamsTag extends BodyTagSupportEx {
	private static final long serialVersionUID = 5003178805427332981L;

	private String id;

	private EnumTrackingSource source;

	@Override
	public int doStartTag() throws JspException {
		QueryParserTag parent = (QueryParserTag) findAncestorWithClass(this, QueryParserTag.class);
		if (parent == null)
			throw new JspException("TrackingQueryParams tag must have a QueryParser tag as its parent");
		
		QueryParameterCollection qpc = (QueryParameterCollection) pageContext.getAttribute(parent.getQueryId());

		if (source == null || source == EnumTrackingSource.AUTO) {
			EnumTrackingSource tmp = (EnumTrackingSource) request.getAttribute("trkSource");
			if (tmp != null)
				source = tmp;
			else
				source = detectSource();
		}

		if (source != EnumTrackingSource.UNDEFINED)
			qpc.addParameterValue(QueryParameter.TRK, source.getValue());

		pageContext.setAttribute(id, this);
		return super.doStartTag();
	}

	private EnumTrackingSource detectSource() {
		if (request.getParameter(QueryParameter.DEPT_ID) != null)
			return EnumTrackingSource.DEPARTMENT;
		else if (request.getParameter(QueryParameter.CAT_ID) != null && request.getParameter(QueryParameter.PRODUCT_ID) == null)
			return EnumTrackingSource.CATEGORY;

		return EnumTrackingSource.UNDEFINED;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public EnumTrackingSource getSource() {
		return source;
	}

	public void setSource(EnumTrackingSource source) {
		this.source = source;
	}

	public static class TagEI extends TagExtraInfo {
		@Override
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] { 
					declareVariableInfo(data.getAttributeString("id"), TrackingQueryParamsTag.class, VariableInfo.AT_BEGIN),
			};
		}
	}
}

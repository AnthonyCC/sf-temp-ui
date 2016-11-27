package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.commons.lang.StringEscapeUtils;

import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.util.QueryParameter;
import com.freshdirect.fdstore.content.util.QueryParameterCollection;
import com.freshdirect.fdstore.content.util.WineSorter;
import com.freshdirect.fdstore.content.util.WineSorter.Type;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.webapp.BodyTagSupportEx;
import com.freshdirect.webapp.taglib.ParametersTag;
import com.freshdirect.webapp.taglib.QueryParserTag;

public class WineSortByLinkTag extends BodyTagSupportEx {
	private static final long serialVersionUID = -3595641069510067251L;
	
	private static final String DEFAULT_SELECTED_ID = "selected";

	private Type sortBy;

	private String className;

	private String style;

	private int renderState;
	
	private String selectedId = DEFAULT_SELECTED_ID;

	@Override
	public int doStartTag() throws JspException {
		QueryParserTag parent = (QueryParserTag) findAncestorWithClass(this, QueryParserTag.class);
		if (parent == null)
			throw new JspException("WineSortByLink tag must be nested in a QueryParserTag tag");
		QueryParameterCollection qpc = (QueryParameterCollection) pageContext.getAttribute(parent.getQueryId());
		if (qpc == null)
			throw new JspException("there is something wrong with the parent QueryParserTag tag (query)");

		WineSorterTag parent2 = (WineSorterTag) findAncestorWithClass(this, WineSorterTag.class);
		if (parent == null)
			throw new JspException("WineSortByLink tag must be nested in a WineSorter tag");
		WineSorter.Type currentSortBy = (WineSorter.Type) pageContext.getAttribute(parent2.getSortById());
		if (currentSortBy == null)
			throw new JspException("there is something wrong with the parent WineSorter tag (sortBy)");
		WineSorter.Type defaultSortBy = (WineSorter.Type) pageContext.getAttribute(parent2.getDefaultSortById());
		if (defaultSortBy == null)
			throw new JspException("there is something wrong with the parent WineSorter tag (defaultSortBy)");
		
		if (sortBy == Type.EXPERT_RATING) {
			ParametersTag parameters = (ParametersTag) findAncestorWithClass(this, ParametersTag.class);
			if (parameters != null) {
				ContentNodeModel currentNode = parameters.getContentNode();
				if (currentNode != null) {
					if (FDContentTypes.CATEGORY.equals(currentNode.getContentKey().getType())) {
						CategoryModel currentCategory = (CategoryModel) currentNode;
						if (currentCategory.isHideWineRatingPricing()) {
							renderState = 0;
							pageContext.setAttribute(selectedId, Boolean.FALSE);
							return SKIP_BODY;
						}
					}
				}
			}
		}

		if ((sortBy == Type.PRICE_REVERSE && currentSortBy != Type.PRICE) ||
				(sortBy == Type.PRICE && currentSortBy == Type.PRICE)) {
			renderState = 0;
			pageContext.setAttribute(selectedId, Boolean.TRUE);
			return SKIP_BODY;
		}

		StringBuilder link = null;
		if (sortBy == currentSortBy) {
			renderState = 1;
			pageContext.setAttribute(selectedId, Boolean.TRUE);
		} else {
			renderState = 2;
			pageContext.setAttribute(selectedId, Boolean.FALSE);
			qpc = qpc.clone();
			qpc.removeParameter(QueryParameter.WINE_SORT_BY);
			if (sortBy != defaultSortBy)
				qpc.addParameterValue(QueryParameter.WINE_SORT_BY, sortBy.name());
			qpc.removeParameter(QueryParameter.WINE_PAGE_NO);

			link = new StringBuilder();
			link.append(request.getRequestURI());
			link.append("?");
			link.append(qpc.getEncoded());
		}
		
		StringBuilder buf = new StringBuilder();

		if (renderState == 1) {
			buf.append("<span");
		} else {
			buf.append("<a href=\"");
			buf.append(StringEscapeUtils.escapeHtml(link.toString()));
			buf.append("\"");
		}

		if (className != null) {
			buf.append(" class=\"");
			buf.append(StringEscapeUtils.escapeHtml(className));
			buf.append("\"");
		}

		if (style != null) {
			buf.append(" style=\"");
			buf.append(StringEscapeUtils.escapeHtml(style));
			buf.append("\"");
		}

		buf.append(">");

		try {
			JspWriter out = pageContext.getOut();
			out.print(buf.toString());
		} catch (IOException e) {
			throw new JspException(e);
		}
		
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		if (renderState > 0)
			try {
				JspWriter out = pageContext.getOut();
				if (renderState == 1)
					out.print("</span>");
				else
					out.print("</a>");
			} catch (IOException e) {
				throw new JspException(e);
			}

		return super.doEndTag();
	}

	@Override
	public void release() {
		request = null;
		super.release();
	}

	public Type getSortBy() {
		return sortBy;
	}

	public void setSortBy(Type view) {
		this.sortBy = view;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public void setSelectedId(String selectedId) {
		this.selectedId = selectedId;
	}

	public String getSelectedId() {
		return selectedId;
	}
	
	public static class TagEI extends TagExtraInfo {
		@Override
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {
				declareVariableInfo(NVL.apply(data.getAttributeString("selectedId"), DEFAULT_SELECTED_ID), Boolean.class, VariableInfo.NESTED)
			};
		}
	}
}

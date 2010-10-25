package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang.StringEscapeUtils;

import com.freshdirect.fdstore.content.util.EnumWinePageSize;
import com.freshdirect.fdstore.content.util.QueryParameter;
import com.freshdirect.fdstore.content.util.QueryParameterCollection;
import com.freshdirect.webapp.taglib.QueryParserTag;

public class WinePageNoLinkTag extends BodyTagSupport {
	private static final long serialVersionUID = -6148927347347401959L;

	private int pageNo;

	private String className;

	private String style;

	private HttpServletRequest request;

	@Override
	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);
		request = (HttpServletRequest) pageContext.getRequest();
	}

	@Override
	public int doStartTag() throws JspException {
		QueryParserTag parent = (QueryParserTag) findAncestorWithClass(this, QueryParserTag.class);
		if (parent == null)
			throw new JspException("WinePageNoLink tag must be nested in a QueryParserTag tag");
		QueryParameterCollection qpc = (QueryParameterCollection) pageContext.getAttribute(parent.getQueryId());
		if (qpc == null)
			throw new JspException("there is something wrong with the parent QueryParserTag tag (query)");

		WinePagerTag parent2 = (WinePagerTag) findAncestorWithClass(this, WinePagerTag.class);
		if (parent == null)
			throw new JspException("WinePageNoLink tag must be nested in a WinePager tag");
		EnumWinePageSize currentPageSize = (EnumWinePageSize) pageContext.getAttribute(parent2.getPageSizeId());
		if (currentPageSize == null)
			throw new JspException("there is something wrong with the parent WinePager tag (pageSize)");
		Integer productCount = (Integer) pageContext.getAttribute(parent2.getProductCountId());
		if (productCount == null)
			throw new JspException("there is something wrong with the parent WinePager tag (productCount)");
		Integer currentPage = (Integer) pageContext.getAttribute(parent2.getPageNoId());
		if (currentPage == null)
			throw new JspException("there is something wrong with the parent WinePager tag (pageNo)");
		long pageSize = currentPageSize.getSize();
		long productIndex = pageSize * (currentPage - 1);
		if (productIndex > productCount) {
			productIndex = 0;
			currentPage = 1;
		}
		boolean omitAnchorTag = pageNo == currentPage;

		StringBuilder buf = new StringBuilder();
		
		buf.append("<span style=\"white-space: nowrap;\">");

		if (pageNo != 1)
			buf.append(" . ");
		
		int defaultPage = 1;
		if (!omitAnchorTag) {
			qpc = qpc.clone();
			qpc.removeParameter(QueryParameter.WINE_PAGE_NO);
			if (pageNo != defaultPage)
				qpc.addParameterValue(QueryParameter.WINE_PAGE_NO, Integer.toString(pageNo));

			StringBuilder link = new StringBuilder();
			link.append(request.getRequestURI());
			link.append("?");
			link.append(qpc.getEncoded());

			buf.append("<a href=\"");

			buf.append(StringEscapeUtils.escapeHtml(link.toString()));

			buf.append("\"");

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
		} else
			buf.append("<b>");
		
		buf.append(Integer.toString(pageNo));

		if (!omitAnchorTag)
			buf.append("</a>");
		else
			buf.append("</b>");
		
		buf.append("</span>");

		try {
			JspWriter out = pageContext.getOut();
			out.println(buf.toString());
		} catch (IOException e) {
		}

		return SKIP_BODY;
	}

	@Override
	public void release() {
		request = null;
		super.release();
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
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
}

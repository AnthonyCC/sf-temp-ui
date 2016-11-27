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

public class WinePageSizeLinkTag extends BodyTagSupport {
	private static final long serialVersionUID = 2249297951241983905L;

	private EnumWinePageSize pageSize;

	private String className;

	private String style;

	private HttpServletRequest request;

	private boolean omitAnchorTag = false;

	@Override
	public void setPageContext(PageContext pageContext) {
		super.setPageContext(pageContext);
		request = (HttpServletRequest) pageContext.getRequest();
	}

	@Override
	public int doStartTag() throws JspException {
		QueryParserTag parent = (QueryParserTag) findAncestorWithClass(this, QueryParserTag.class);
		if (parent == null)
			throw new JspException("WinePageSizeLink tag must be nested in a QueryParserTag tag");
		QueryParameterCollection qpc = (QueryParameterCollection) pageContext.getAttribute(parent.getQueryId());
		if (qpc == null)
			throw new JspException("there is something wrong with the parent QueryParserTag tag (query)");

		WinePagerTag parent2 = (WinePagerTag) findAncestorWithClass(this, WinePagerTag.class);
		if (parent2 == null)
			throw new JspException("WinePageSizeLink tag must be nested in a WinePager tag");
		EnumWinePageSize currentPageSize = (EnumWinePageSize) pageContext.getAttribute(parent2.getPageSizeId());
		if (currentPageSize == null)
			throw new JspException("there is something wrong with the parent WinePager tag (pageSize)");

		EnumWinePageSize defaultPageSize = EnumWinePageSize.ALL;
		if (omitAnchorTag = pageSize == currentPageSize)
			return EVAL_BODY_INCLUDE;

		qpc = qpc.clone();
		qpc.removeParameter(QueryParameter.WINE_PAGE_SIZE);
		if (pageSize != defaultPageSize)
			qpc.addParameterValue(QueryParameter.WINE_PAGE_SIZE, pageSize.name());
		qpc.removeParameter(QueryParameter.WINE_PAGE_NO);

		StringBuilder link = new StringBuilder();
		link.append(request.getRequestURI());
		link.append("?");
		link.append(qpc.getEncoded());

		StringBuilder buf = new StringBuilder();
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

		try {
			JspWriter out = pageContext.getOut();
			out.println(buf.toString());
		} catch (IOException e) {
		}

		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		if (!omitAnchorTag)
			try {
				JspWriter out = pageContext.getOut();
				out.println("</a>");
			} catch (IOException e) {
			}

		return super.doEndTag();
	}

	@Override
	public void release() {
		request = null;
		super.release();
	}

	public EnumWinePageSize getPageSize() {
		return pageSize;
	}

	public void setPageSize(EnumWinePageSize pageSize) {
		this.pageSize = pageSize;
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

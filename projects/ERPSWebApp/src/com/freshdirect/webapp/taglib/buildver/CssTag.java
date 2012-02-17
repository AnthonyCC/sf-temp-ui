package com.freshdirect.webapp.taglib.buildver;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang.StringEscapeUtils;

public class CssTag extends AbstractBuildverTag {
	private static final long serialVersionUID = -4300042483926875835L;

	private String href;

	@Override
	protected String getUri() {
		return href;
	}

	@Override
	protected void renderTag(String uri, JspWriter out) throws IOException {
		StringBuilder buf = new StringBuilder();
		String id = getId();
		buf.append("<link ");
		if (id != null) {
			buf.append("id=\"");
			buf.append(StringEscapeUtils.escapeHtml(id));
			buf.append("\" ");
		}
		buf.append("rel=\"stylesheet\" type=\"text/css\" href=\"");
		buf.append(StringEscapeUtils.escapeHtml(uri));
		buf.append("\" />");
		out.print(buf);
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
}

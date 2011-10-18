package com.freshdirect.webapp.taglib.buildver;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang.StringEscapeUtils;

public class JavaScriptTag extends AbstractBuildverTag {
	private static final long serialVersionUID = -4731657866645341370L;

	private String src;

	@Override
	protected String getUri() {
		return src;
	}

	@Override
	protected void renderTag(String uri, JspWriter out) throws IOException {
		StringBuilder buf = new StringBuilder();
		buf.append("<script type=\"text/javascript\" src=\"");
		buf.append(StringEscapeUtils.escapeHtml(uri));
		buf.append("\"></script>");
		out.print(buf);
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}
}

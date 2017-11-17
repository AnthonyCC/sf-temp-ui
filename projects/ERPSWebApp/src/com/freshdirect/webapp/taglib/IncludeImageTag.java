package com.freshdirect.webapp.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.commons.lang.StringEscapeUtils;

import com.freshdirect.framework.webapp.BodyTagSupportEx;
import com.freshdirect.storeapi.content.Image;

public class IncludeImageTag extends BodyTagSupportEx {
	private static final long serialVersionUID = -2169082168781818907L;

	private Image image;

	private String alt;

	private String className;

	private String style;
	
	@Override
    public int doStartTag() throws JspException {
		if (image == null)
			return SKIP_BODY;
		StringBuilder buf = new StringBuilder();
		buf.append("<img src=\"");
		buf.append(StringEscapeUtils.escapeHtml(image.getPathWithPublishId()));
		buf.append("\"");
		buf.append(" width=\"");
		buf.append(image.getWidth());
		buf.append("\" height=\"");
		buf.append(image.getHeight());
		buf.append("\"");

		if (alt != null) {
			buf.append(" alt=\"");
			buf.append(StringEscapeUtils.escapeHtml(alt));
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

		JspWriter out = pageContext.getOut();
		try {
			out.append(buf);
		} catch (IOException e) {
			throw new JspException();
		}
		return SKIP_BODY;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
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

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}
}

package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.framework.webapp.BodyTagSupport;

/**
 * Renders an opaque div around HTML content
 * 
 * @author segabor
 *
 */
public class OpaqueBoxTag extends BodyTagSupport {

	/**
	 * Opacity (0-100)
	 * IN, OPTIONAL
	 */
	int opacity = 50;
	
	/**
	 * Don't render opaque layer
	 * IN, OPTIONAL
	 */
	boolean disabled = false;
	
	/**
	 * DOM ID of frame element
	 * IN, OPTIONAL
	 */
	String boxId;
	
	/**
	 * Additional style parameters
	 * IN, OPTIONAL
	 */
	String style;
	



	
	public void setOpacity(int opacity) {
		if (opacity < 0)
			this.opacity = 0;
		else if (opacity > 100)
			this.opacity = 100;
		else
			this.opacity = opacity;
	}





	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}





	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}





	public void setStyle(String style) {
		this.style = style;
	}



	public void doInitBody() throws JspException {
		if (!this.disabled) {
			BrowserInfo bi = new BrowserInfo( (HttpServletRequest) pageContext.getRequest() );

			String obStyle = "";
			
			if (bi.isIE6()) {
				obStyle = "display: inline-block; filter:alpha(opacity="+opacity+"); width: 100%; height: 100%;";
			} else {
				float f_op = ((float)opacity)/100.0f;
				
				if (bi.isFirefox()) {
					obStyle = "-moz-opacity: "+f_op+";";
				} else if (bi.isSafari()) {
					obStyle = "-khtml-opacity: "+f_op+";";
				} else {
					obStyle = "opacity: "+f_op+";";
				}
			}
			
			if (this.style != null)
				obStyle += " " + style;

			StringBuffer buf = new StringBuffer();
			buf.append("<div");
			
			// append ID
			if (boxId != null)
				buf.append(" id=\""+boxId+"\"");

			// append style
			buf.append(" style=\""+obStyle+"\"");

			buf.append(">");

			try {
				JspWriter out = pageContext.getOut();
				out.write(buf.toString());
			} catch (IOException e) {}
		}
	}

	public int doAfterBody() throws JspException {
		if (!this.disabled) {
			StringBuffer buf = new StringBuffer();
			buf.append("</div>");

			try {
				JspWriter out = pageContext.getOut();
				out.write(buf.toString());
			} catch (IOException e) {}
		}
		return SKIP_BODY;
	}





	
	

	
	
	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {};
		}
	}
}

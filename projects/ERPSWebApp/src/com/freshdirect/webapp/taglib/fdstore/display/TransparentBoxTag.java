package com.freshdirect.webapp.taglib.fdstore.display;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.taglib.fdstore.BrowserInfo;

/**
 * Renders an opaque div around HTML content
 * 
 * @author segabor
 *
 */
public class TransparentBoxTag extends BodyTagSupport {
	private static final long serialVersionUID = 1L;

	/**
	 * Opacity (0-1)
	 * IN, OPTIONAL
	 */
	double opacity = 0.5;
	
	/**
	 * Don't render opaque layer
	 * IN, OPTIONAL
	 */
	boolean disabled = false;
	
	/**
	 * Additional style parameters
	 * IN, OPTIONAL
	 */
	String style;
	



	
	public void setOpacity(double opacity) {
		if (opacity < 0)
			this.opacity = 0;
		else if (opacity > 1)
			this.opacity = 1;
		else
			this.opacity = opacity;
	}





	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}





	public void setStyle(String style) {
		this.style = style;
	}



	public void doInitBody() throws JspException {
		if (!this.disabled) {
			BrowserInfo bi = new BrowserInfo( (HttpServletRequest) pageContext.getRequest() );

			String obStyle = getOpacityStyle(bi, 0.5);


			// append additional CSS style
			if (this.style != null)
				obStyle += " " + style;

			StringBuffer buf = new StringBuffer();
			buf.append("<div");
			
			// append ID
//			if (boxId != null)
//				buf.append(" id=\""+boxId+"\"");

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


	/**
	 * Helper to create opacity style commands specific to browser
	 * 
	 * @param bi
	 * @param opacity
	 * @return
	 */
	public static String getOpacityStyle(BrowserInfo bi, double opacity) {
		String obStyle = "";

		if (bi == null)
			return "opacity: "+opacity+";";


		if (bi.isInternetExplorer()) {
			int i_op = (int) Math.round(opacity*100);
			
			if (bi.getVersionNumber() >= 8.0) {
				obStyle = "-ms-filter: 'progid:DXImageTransform.Microsoft.Alpha(Opacity="+i_op+")';";
			} else {
				obStyle = "display: inline-block; filter: progid:DXImageTransform.Microsoft.Alpha(opacity="+i_op+") ! important;";
			}
		} else {
			if (bi.isFirefox()) {
				obStyle = "-moz-opacity: "+opacity+";";
			} else if (bi.isWebKit()) {
				obStyle = "-khtml-opacity: "+opacity+";";
			} else {
				obStyle = "opacity: "+opacity+";";
			}
		}
		return obStyle;
	}


	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[] {};
		}
	}
}

package com.freshdirect.webapp.taglib.smartstore;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.commons.lang.StringEscapeUtils;

import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.smartstore.fdstore.Recommendations;


/**
 * Store various recommendation parameters to in products form.
 * Then these parameters will be processed in {@link RecommendationsTag} and {@link GenericRecommendationsTag}
 * 
 * @author segabor
 */
public class StoreRecommendationTag extends BodyTagSupport {
	private static final long serialVersionUID = -3085305170488915085L;

	Recommendations recommendations;




	public void setRecommendations(Recommendations recommendations) {
		this.recommendations = recommendations;
	}


	public int doStartTag() throws JspException {
		JspWriter out = pageContext.getOut();
		
		try {
			// variant ID
			out.append("<input type=\"hidden\" name=\"variant\" value=\"");
			out.append(StringEscapeUtils.escapeHtml(recommendations.getVariant().getId()));
			out.append("\">\n");


			// site feature
			out.append("<input type=\"hidden\" name=\"siteFeature\" value=\"");
			out.append(StringEscapeUtils.escapeHtml(recommendations.getVariant().getSiteFeature().getName()));
			out.append("\">\n");


			// trk code
			out.append("<input type=\"hidden\" name=\"trk\" value=\"");
			out.append(StringEscapeUtils.escapeHtml(recommendations.getVariant().getSiteFeature().getName().toLowerCase()));
			out.append("\">\n");
		} catch (IOException e) {
		}


		return SKIP_BODY;
	}

	public static class TagEI extends TagExtraInfo {
        public VariableInfo[] getVariableInfo(TagData data) {
			return new VariableInfo[0];
        }
	}
}

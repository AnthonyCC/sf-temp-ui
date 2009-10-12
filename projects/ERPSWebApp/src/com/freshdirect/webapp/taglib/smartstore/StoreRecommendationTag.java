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


			// serialize recommendation
			out.append("<input type=\"hidden\" name=\"rec_product_ids\" value=\"");
			out.append(StringEscapeUtils.escapeHtml(recommendations.serializeRecommendation()));
			out.append("\">\n");


			// current product ID
			out.append("<input type=\"hidden\" name=\"rec_current_node\" value=\"");
			if (recommendations.getSessionInput() != null && recommendations.getSessionInput().getCurrentNode() != null) {
				out.append(recommendations.getSessionInput().getCurrentNode().getContentKey().getId());
			} else {
				out.append("(null)");
			}
			out.append("\">\n");


			// YMAL source ID
			out.append("<input type=\"hidden\" name=\"rec_ymal_source\" value=\"");
			if (recommendations.getSessionInput() != null && recommendations.getSessionInput().getCurrentNode() != null) {
				out.append(recommendations.getSessionInput().getCurrentNode().getContentKey().getId());
			} else {
				out.append("(null)");
			}
			out.append("\">\n");


			// refreshable flag
			out.append("<input type=\"hidden\" name=\"rec_refreshable\" value=\"");
			out.append(Boolean.toString(recommendations.isRefreshable()));
			out.append("\">\n");


			// rec_smart_savings flag
			out.append("<input type=\"hidden\" name=\"rec_smart_savings\" value=\"");
			out.append(Boolean.toString(recommendations.isSmartSavings()));
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

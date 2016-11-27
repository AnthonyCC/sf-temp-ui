package com.freshdirect.webapp.taglib.smartstore;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.CartTabRecommender;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.TabRecommendation;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

/**
 * Product Impression Presentation
 * Tab Layout Tag
 * 
 * @author treer
 * 
 */
public class PIPTabTag extends TabHelperTag {
	private static final long serialVersionUID = 1654943557811529504L;

	private static Logger LOGGER = LoggerFactory.getInstance( PIPTabTag.class );
	
	private int maxTabs = 3;
	private int maxRecommendations = 5;
	private String facility;
	
	public int getMaxTabs() {
		return maxTabs;
	}	
	public void setMaxTabs( int numTabs ) {
		this.maxTabs = numTabs;
	}
	
	public int getMaxRecommendations() {
		return maxRecommendations;
	}	
	public void setMaxRecommendations( int maxRecommendations ) {
		this.maxRecommendations = maxRecommendations;
	}
	
	public void setFacility(String facility) {
		this.facility = facility;
	}

	
	public int doStartTag() throws JspException {
		LOGGER.debug( "doStartTag()" );
		
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		SessionInput input = createSessionInput(session, user, maxRecommendations);
		
		tabs = CartTabRecommender.recommendTabs(user, input);
		
		// it's very similar to RecommendationsTag.persistToSession()
		if (input.getPreviousRecommendations() != null) {
		    pageContext.getSession().setAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS, input.getPreviousRecommendations());
		}
		/* savings variant id is the variant attached to the pre-selected smart saving site feature tab by CartTabRecommender.*/
		if (input.getSavingsVariantId() != null) {
			user.setSavingsVariantId(input.getSavingsVariantId());
		}
		if (tabs.size() == 0) {
		    return SKIP_BODY;
		}
		
		// old db logging..
		if (user instanceof FDSessionUser) {
			FDSessionUser sessionUser = (FDSessionUser) user;
			sessionUser.logTabImpression(tabs.getTabVariant().getId(), tabs.size());
		}
		
		if ( tabs.size() < maxTabs ) {
		    LOGGER.warn( "not enough variants ("+tabs.size()+") for "+maxTabs+" tabs." );
		}

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
                Impression imp = Impression.get(user, request, facility);
		String impressionId = imp.logFeatureImpression(null, null, tabs.getTabVariant(), 
		        input.getCategory(), input.getCurrentNode(), input.getYmalSource());
		tabs.setParentImpressionId(impressionId);
		
		for (int i = 0; i < tabs.size(); i++) {
		    String tabImp = imp.logTab(impressionId, i, tabs.get(i).getSiteFeature().getName());
                    tabs.setFeatureImpressionId(i, tabImp);
                }

		tabs.setSelected(getSelectedTab(session, request));
                pageContext.setAttribute(this.id, tabs);
                request.setAttribute("parentImpressionId", impressionId);
                
		return EVAL_BODY_INCLUDE; 
	}
	
	public static class TagEI extends TagExtraInfo {
        public VariableInfo[] getVariableInfo(TagData data) {
            return new VariableInfo[] { 
                    new VariableInfo(
                            "selectedTabIndex", 
                            "java.lang.Integer", 
                            true, 
                            VariableInfo.AT_BEGIN),
                    new VariableInfo(data.getAttributeString("id"), 
                            "com.freshdirect.smartstore.TabRecommendation", 
                            true, 
                            VariableInfo.AT_BEGIN)};
        }
    }
}

package com.freshdirect.webapp.taglib.smartstore;

import java.util.Map;
import java.util.Set;

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
import com.freshdirect.smartstore.ymal.YmalUtil;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

/**
 * Product Impression Presentation
 * Tab Layout Tag
 * 
 * @author treer
 * 
 */

public class PIPTabTag extends javax.servlet.jsp.tagext.BodyTagSupport {
	
	private static Logger LOGGER = LoggerFactory.getInstance( PIPTabTag.class );

	private static final String overriddenVariantId = null; //SmartStoreUtil.SKIP_OVERRIDDEN_VARIANT;	
	
	//=============================================================
	//						Attributes
	//=============================================================
	
	private int maxTabs = 3;
	private int maxRecommendations = 5;
	
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

	private TabRecommendation tabs;

	// @return number of tabs recommended
	public int getRecommendedTabs() {
		return this.tabs.size()/*  nTabs */;
	}
	
	public TabRecommendation getTabs() {
            return tabs;
        }
	
	//=============================================================
	//						Tag support methods
	//=============================================================
	
	
	public int doStartTag() throws JspException {

		LOGGER.debug( "doStartTag()" );	
		
		pageContext.setAttribute(this.id, this);
		
		// ----------- CartTabRecommender ...  -------------
		
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		SessionInput input = new SessionInput(user);
		Set cart = FDStoreRecommender.getShoppingCartContents( user );
		input.setCartContents(cart);
		input.setPreviousRecommendations((Map) session.getAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS));
		input.setYmalSource( YmalUtil.resolveYmalSource( FDStoreRecommender.getShoppingCartProductList( user ) ) );
		input.setCurrentNode( input.getYmalSource() );
		input.setMaxRecommendations(maxRecommendations);
		
		tabs = CartTabRecommender.recommendTabs( user, input, overriddenVariantId);
		
		if (tabs.size() == 0)
			return SKIP_BODY;
		
		if ( tabs.size() < maxTabs ) {
		    LOGGER.warn( "not enough variants ("+tabs.size()+") for "+maxTabs+" tabs." );
		}

		Impression imp = Impression.get(user, (HttpServletRequest) pageContext.getRequest());
		String impressionId = imp.logFeatureImpression(null, tabs.getTabRecommender().getVariant().getId(), input.getCurrentNode(), input.getYmalSource());
		for (int i = 0; i < tabs.size(); i++) {
		    String tabImp = imp.logTab(impressionId, i, tabs.get(i).getId());
                    tabs.setFeatureImpressionId(i, tabImp);
                }

		return EVAL_BODY_INCLUDE; 
	}	
	

	//=============================================================
	//						Tag extra info
	//=============================================================	
	
	public static class TagEI extends TagExtraInfo {
		public VariableInfo[] getVariableInfo(TagData data) {
	
			return new VariableInfo[] {
					new VariableInfo(
						data.getAttributeString("id"),
						"com.freshdirect.webapp.taglib.smartstore.PIPTabTag",
						true,
						VariableInfo.NESTED )
			};
		}
	}
}

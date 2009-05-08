package com.freshdirect.webapp.taglib.smartstore;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.promotion.PromoVariantModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.CartTabRecommender;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.TabRecommendation;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.smartstore.ymal.YmalUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
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
		
		
		// ----------- CartTabRecommender ...  -------------
		
		HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		SessionInput input = new SessionInput(user);
		input.setPreviousRecommendations((Map) session.getAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS));

		FDStoreRecommender.initYmalSource(input, user);
		input.setCurrentNode( input.getYmalSource() );
		input.setMaxRecommendations(maxRecommendations);
		
		if(input.getPreviousRecommendations() == null){
			//when no smart savings variant was previously set.
			input.setCheckForEnoughSavingsMode(true);
			validateForEnoughSavings(user, input, overriddenVariantId);
			input.setCheckForEnoughSavingsMode(false);
		}
		tabs = CartTabRecommender.recommendTabs( user, input, overriddenVariantId);
		
		// it's very similar to RecommendationsTag.persistToSession()
		if (input.getPreviousRecommendations() != null) {
		    pageContext.getSession().setAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS, input.getPreviousRecommendations());
		}
		
		if (tabs.size() == 0) {
		    return SKIP_BODY;
		}
		
		// old db logging..
                if (user instanceof FDSessionUser) {
                    FDSessionUser sessionUser = (FDSessionUser) user;
                    sessionUser.logTabImpression(tabs.getTabRecommender().getVariant().getId(), tabs.size());
                }
		
		if ( tabs.size() < maxTabs ) {
		    LOGGER.warn( "not enough variants ("+tabs.size()+") for "+maxTabs+" tabs." );
		}

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
                Impression imp = Impression.get(user, request);
		String impressionId = imp.logFeatureImpression(null, tabs.getTabRecommender().getVariant(), 
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
	
	protected int getSelectedTab(HttpSession session, ServletRequest req) {
            final int numTabs = tabs.size();
            
            int selectedTab = 0; // default value
            Object selectedTabAttribute = session.getAttribute(SessionName.SS_SELECTED_TAB);
            
            boolean shouldStoreTabPos = selectedTabAttribute == null; // true == not stored yet
            if (selectedTabAttribute != null) {
                // get the stored one if exist
                selectedTab = ((Integer) selectedTabAttribute).intValue();
            }

            // tab explicitly set
            String value = req.getParameter("tab");
            if (value != null && !"".equals(value)) {
                selectedTab = Integer.parseInt(value);
                shouldStoreTabPos = true;
            }

            if (selectedTab >= numTabs || (session.getAttribute(SessionName.SS_SELECTED_VARIANT) != null &&
            		!tabs.get(selectedTab).getId().equals( session.getAttribute(SessionName.SS_SELECTED_VARIANT) ) )) {
                // reset if selection is out of tab range or the variant of selected tab has changed
                selectedTab = 0;
                shouldStoreTabPos = true;
            }

            Integer iSelectedTab = new Integer(selectedTab);
            if (shouldStoreTabPos) {
                // store changed tab position in session
                session.setAttribute(SessionName.SS_SELECTED_TAB, iSelectedTab);
                session.setAttribute(SessionName.SS_SELECTED_VARIANT, tabs.get(selectedTab).getId());
            }
            pageContext.setAttribute("selectedTabIndex", iSelectedTab);

            return selectedTab;
	}
	
	private void validateForEnoughSavings(FDUserI user, SessionInput input, String overriddenVariantId){
		
		Map pvMap = input.getPromoVariantMap();
		if(pvMap != null && pvMap.size() > 0) {
			FDStoreRecommender recommender = FDStoreRecommender.getInstance();
			for(Iterator it = pvMap.keySet().iterator(); it.hasNext();){
				//String variantId = (String) it.next();
				PromoVariantModel pv = (PromoVariantModel)pvMap.get(it.next());
				try {
				Recommendations rec = recommender.getRecommendations(pv.getSiteFeature(), user, input, overriddenVariantId);
				  if (rec.getProducts().size() == 0) {
					  //no recommendations found for this smart saving feature. remove it from the promo variant map.
					 it.remove();
				  }
				}catch (FDResourceException e) {
					//Do nothing.
                }
			}
		}
	}
	//=============================================================
	//						Tag extra info
	//=============================================================	
	
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

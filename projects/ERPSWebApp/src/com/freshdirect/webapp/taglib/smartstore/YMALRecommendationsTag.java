package com.freshdirect.webapp.taglib.smartstore;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.YmalSource;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.smartstore.fdstore.SmartStoreServiceConfiguration;
import com.freshdirect.smartstore.ymal.YmalUtil;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

/**
 * SmartStore YMAL Recommendations Tag
 * 
 * @author csongor
 * 
 */
public class YMALRecommendationsTag extends RecommendationsTag implements SessionName {
	private static final long serialVersionUID = 5976696010559642821L;
	
	private YmalSource source = null;
	
	public YMALRecommendationsTag() {
		super();
		itemCount = 6;
	}

    public void setSource(YmalSource source) {
		this.source = source;
	}

	protected Recommendations getRecommendations() throws FDResourceException, InvalidContentKeyException {
        HttpSession session = pageContext.getSession();

        Recommendations results = null;

        if (errorOccurred) {
        	ServletRequest request = pageContext.getRequest();
    		// reconstruct recommendations
    		String variantId = request.getParameter("variant");
    		String siteFeatureName = request.getParameter("siteFeature");

    		if (variantId != null && siteFeatureName != null) {
    			final EnumSiteFeature sf = EnumSiteFeature.getEnum(siteFeatureName);
    			Map svcMap = SmartStoreServiceConfiguration.getInstance().getServices(sf);
    			RecommendationService svc = (RecommendationService) svcMap.get(variantId);
    			
    			if (request.getParameter("rec_product_ids") != null)
    				results = new Recommendations( svc.getVariant(),
    						request.getParameter("rec_product_ids"),
    						request.getParameter("rec_current_node"),
    						request.getParameter("rec_ymal_source"),
    						false, false); 
    		}
        }

        // get recommendations by recommender
        if (results == null) {
			FDStoreRecommender recommender = FDStoreRecommender.getInstance();
			
			HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
			
			String overriddenVariantID = request.getParameter("SmartStore.VariantID");
			if (overriddenVariantID != null)
			    session.setAttribute("SmartStore.VariantID", overriddenVariantID);
			
			// setup an input
			FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
			SessionInput inp = new SessionInput(user);
			initFromSession(inp);
			if (source != null) {
				inp.setYmalSource(source);
				if (source instanceof ProductModel)
					inp.setCurrentNode(source);
			} else
				inp.setYmalSource(YmalUtil.resolveYmalSource(user, null));
			
			if (inp.getCurrentNode() == null)
				inp.setCurrentNode(YmalUtil.getSelectedCartLine(user).lookupProduct());

			inp.setMaxRecommendations(itemCount);


			results = recommender.getRecommendations(EnumSiteFeature.YMAL, user, inp, overriddenVariantID);
			persistToSession(results);
        }

        return results;
    }

	public static class TagEI extends AbstractGetterTag.TagEI {
        protected String getResultType() {
            return "com.freshdirect.smartstore.fdstore.Recommendations";
        }
    }

}

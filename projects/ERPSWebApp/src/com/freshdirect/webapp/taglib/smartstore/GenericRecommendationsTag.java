package com.freshdirect.webapp.taglib.smartstore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.smartstore.fdstore.SmartStoreServiceConfiguration;
import com.freshdirect.smartstore.fdstore.SmartStoreUtil;
import com.freshdirect.smartstore.service.VariantRegistry;
import com.freshdirect.smartstore.ymal.YmalUtil;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.ConfigurationContext;
import com.freshdirect.webapp.util.ConfigurationStrategy;
import com.freshdirect.webapp.util.ProductImpression;
import com.freshdirect.webapp.util.prodconf.SmartStoreConfigurationStrategy;

/**
 * INPUT request attributes :
 * 		Variant genericRecommendationsVariant
 * 
 * @author treer
 *
 */

public class GenericRecommendationsTag extends RecommendationsTag implements SessionName {

    private static final long serialVersionUID = 1L;
    
    private static final Logger LOGGER = LoggerFactory.getInstance( GenericRecommendationsTag.class );

    // ID that makes cache name unique
    String cacheId;
    
    String overriddenVariantId;

    transient boolean shouldLogImpressions = false;


    public void setCacheId(String cacheId) {
    	this.cacheId = cacheId;
    }
    
    
    /**
     * Prepare recommendation cache
     */
    public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		overriddenVariantId = getOverriddenVariantID();
		
        // get selected tab (variant) on UI
        Variant variant = (Variant)request.getAttribute( "genericRecommendationsVariant" );
        
        if ( variant != null ) {
        	prepareCache(variant, false);
        }

    	
    	return super.doStartTag();
    }

    
    /**
     * 
     * @param v Original variant
     * @param override ID of overridden variant
     * @return
     * @throws FDResourceException
     */
    protected Variant getOverriddenVariant(Variant v, String override) throws FDResourceException {
        HttpSession session = pageContext.getSession();
        Variant v2 = SmartStoreUtil.getOveriddenVariant((FDUserI) session.getAttribute(SessionName.USER), v.getSiteFeature(), override);
		return v2 != null ? v2 : v;
    }

    /**
     * Prepare recommendation cache
     * 
     * @param variant
     * @param force Force (re)loading recommendation
     */
    protected void prepareCache(Variant variant, boolean force) {
        HttpSession session = pageContext.getSession();

		try {
			variant = getOverriddenVariant(variant, overriddenVariantId);
				
			final String CACHE_KEY = "RC-"+cacheId;
			
			Map<String,Recommendations> cache = (Map<String,Recommendations>) session.getAttribute(CACHE_KEY);
			if (force || cache == null) {
				cache = new HashMap<String,Recommendations>();
				session.setAttribute(CACHE_KEY, cache);
			}

			if (cache != null && cache.get(variant.getId()) == null ) {
				Recommendations recache = extractRecommendations(session, variant.getSiteFeature());
				cache.put(variant.getId(), recache);
			}
		} catch (FDResourceException e) {
		}
    }
    

    public Recommendations getCachedRecommendations(Variant variant) throws FDResourceException {
        HttpSession session = pageContext.getSession();
    	Map<String,Recommendations> cache = (Map<String,Recommendations>) session.getAttribute("RC-"+cacheId);
    	
    	if (cache == null)
    		return null;
    	
    	return cache.get(variant.getId());
    }


    /**
     * Get overridden variant ID from request and store in session
     * 
     * @return
     */
    protected String getOverriddenVariantID() {
        HttpSession session = pageContext.getSession();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		String overriddenVariantID = request.getParameter("SmartStore.VariantID");
		if (overriddenVariantID != null)
		    session.setAttribute("SmartStore.VariantID", overriddenVariantID);
		return overriddenVariantID;
    }
    
    protected String getCacheKey(Variant variant) throws FDResourceException {
		return "RC-"+cacheId;
    }


    protected Recommendations getRecommendations() throws FDResourceException, InvalidContentKeyException {
    	
        HttpSession session = pageContext.getSession();
    	HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
    	
        // get selected tab (variant) on UI
        Variant variant = (Variant)request.getAttribute( "genericRecommendationsVariant" );
        
        if ( variant == null ) {
        	return null;
        }


        // invalidate cache after a successful add2cart operation
        final boolean add2cartOccured = user.getShoppingCart().getRecentOrderLines().size() > 0 && "1".equalsIgnoreCase(request.getParameter("confirm"));
    	if (add2cartOccured) {
    		// invalidate cache
    		LOGGER.debug("Invalidate recommendations cache");
    		prepareCache(variant, true);
    	}

        
        Recommendations recommendations = getCachedRecommendations(getOverriddenVariant(variant, overriddenVariantId));
		if (recommendations != null && !add2cartOccured) {
			if ("next".equalsIgnoreCase(request.getParameter("page")) ) {
				recommendations.pageForward();
			} else if ("prev".equalsIgnoreCase(request.getParameter("page"))) {
				recommendations.pageBackward();
			}
		}
		
		if (recommendations == null){
			throw new FDResourceException("Recommendation cache not found!");
		}

		// Test-And-Set logged status of current product subset
		shouldLogImpressions = recommendations != null && !recommendations.isLogged();
		if (recommendations != null)
			persistToSession(recommendations);
			
        return recommendations;
    }

	private Recommendations extractRecommendations( HttpSession session, EnumSiteFeature sf ) throws FDResourceException {
		
		Recommendations recommendations;
		FDStoreRecommender recommender = FDStoreRecommender.getInstance();

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		SessionInput input = new SessionInput(user);
		initFromSession(input);
		FDStoreRecommender.initYmalSource(input, user, request);
		input.setCurrentNode( input.getYmalSource() );
		input.setMaxRecommendations(itemCount);

		recommendations = recommender.getRecommendations(sf, user, input, overriddenVariantId);
		
		return recommendations;
	}



	@Override
	protected boolean shouldLogImpressions() {
		LOGGER.debug("  Will log prod impressions? ==> " + shouldLogImpressions);
		return shouldLogImpressions; 
	}



	/** getImpressions()
	 * @param Recommendations recommendations
	 * @return List &lt ProductImpression &gt
	 */
	public List<ProductImpression> getImpressions ( Recommendations recommendations ) {
		
		ConfigurationContext confContext = new ConfigurationContext();
        HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		confContext.setFDUser(user);
		
		// List<ProductImpression> impressions : configured products enlisted in 'Your Favourites' table
		List<ProductImpression> impressions = new ArrayList<ProductImpression>();

		if ( recommendations != null && recommendations.getProducts().size() > 0 ) {
			
			ConfigurationStrategy cUtil = SmartStoreConfigurationStrategy.getInstance();

			List<ProductModel> products = recommendations.getProducts();
			
			// 'configure' products.
			for (ProductModel prd : products) {
				ProductImpression pi = cUtil.configure(prd, confContext);

				impressions.add(pi);
			}
		}		
		
		return impressions;
	
	}	
}

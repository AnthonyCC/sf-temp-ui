package com.freshdirect.webapp.taglib.smartstore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.freshdirect.cms.ContentKey.InvalidContentKeyException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.FDStoreRecommender;
import com.freshdirect.smartstore.fdstore.Recommendations;
import com.freshdirect.smartstore.fdstore.SmartStoreServiceConfiguration;
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

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    protected Recommendations getRecommendations() throws FDResourceException, InvalidContentKeyException {
    	
        HttpSession session = pageContext.getSession();
    	ServletRequest request = pageContext.getRequest();
    	
        FDUserI user = (FDUserI) session.getAttribute("fd.user");        
        
        // get selected tab (variant) on UI
        Variant variant = (Variant)request.getAttribute( "genericRecommendationsVariant" );
        
        if ( variant == null ) {
        	return null;
        }

        Recommendations recommendations = null;

        if ( errorOccurred ) {        	
        	
    		// reconstruct recommendations
			Map svcMap = SmartStoreServiceConfiguration.getInstance().getServices( variant.getSiteFeature() );
			RecommendationService svc = (RecommendationService)svcMap.get( variant.getId() );
			recommendations = new Recommendations( svc.getVariant(), request.getParameter( "rec_product_ids" ) );	//TODO request.getParameter( "rec_product_ids" ) helyett ... 
        }

        // get recommendations by recommender
        if ( recommendations == null ) {
        	
       		recommendations = extractRecommendations( session, variant.getSiteFeature() );
       		
        }
        return recommendations;
    }

	private Recommendations extractRecommendations( HttpSession session, EnumSiteFeature sf ) throws FDResourceException {
		
		Recommendations recommendations;
		FDStoreRecommender recommender = FDStoreRecommender.getInstance();

		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

		String overriddenVariantID = request.getParameter("SmartStore.VariantID");
		if (overriddenVariantID != null)
		    session.setAttribute("SmartStore.VariantID", overriddenVariantID);

		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		SessionInput input = new SessionInput(user);
		Set cart = FDStoreRecommender.getShoppingCartContents( user );
		input.setCartContents( cart );
		initFromSession(input);
		input.setYmalSource( YmalUtil.resolveYmalSource( FDStoreRecommender.getShoppingCartProductList( user ) ) );
		input.setCurrentNode( input.getYmalSource() );
		input.setMaxRecommendations(itemCount);

		recommendations = recommender.getRecommendations(sf, user, input, overriddenVariantID);
		
		persistToSession(recommendations);
		return recommendations;
	}

	/** getImpressions()
	 * @param Recommendations recommendations
	 * @return List &lt ProductImpression &gt
	 */
	public List getImpressions ( Recommendations recommendations ) {
		
		ConfigurationContext confContext = new ConfigurationContext();
        HttpSession session = pageContext.getSession();
		FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
		confContext.setFDUser(user);
		boolean hasAnyItemAddable = false;
		
		// List<ProductImpression> impressions : configured products enlisted in 'Your Favourites' table
		List impressions = new ArrayList();

		// List<SkuModel> skus : SKUs required by the pricing engine
		List skus = new ArrayList();
		
		if ( recommendations != null && recommendations.getProducts().size() > 0 ) {
			
	        //request.setAttribute("recommendationsRendered","true");		//TODO kell ez?
			ConfigurationStrategy cUtil = SmartStoreConfigurationStrategy.getInstance();

			List products = recommendations.getProducts();
			

			// 'configure' products.
			Iterator it = products.iterator();
			while ( it.hasNext() ) {
				ProductModel prd = (ProductModel) it.next();
				ProductImpression pi = cUtil.configure(prd, confContext);
				if (pi.isTransactional())
					hasAnyItemAddable = true;

				impressions.add(pi);
				skus.add(pi.getSku());
			}
		}		
		
		return impressions;
	
	}
	
	
	
	
//    public static class TagEI extends RecommendationsTag.TagEI {
//    }

}

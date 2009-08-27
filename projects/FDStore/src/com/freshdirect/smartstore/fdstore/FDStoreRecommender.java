package com.freshdirect.smartstore.fdstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.YmalSource;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.filter.FilterFactory;
import com.freshdirect.smartstore.filter.ProductFilter;
import com.freshdirect.smartstore.ymal.YmalUtil;

public class FDStoreRecommender {
    private final static Logger LOGGER = Logger.getLogger(FDStoreRecommender.class);

	public List filterProducts(List models, final Collection cartItems, boolean includeCartItems) {
		List newModels = new ArrayList(models.size());
		
		ProductFilter filter = null;
        if (!includeCartItems)
        	filter = FilterFactory.createStandardFilter(cartItems);
        else
        	filter = FilterFactory.createStandardFilter(); 
		
		Iterator it = models.iterator();
		while (it.hasNext()) {
			ProductModel model = filter.filter((ProductModel) it.next());
	        if (model != null)
	        	newModels.add(model);
		}

        return newModels;
	}

        /**
         * 
         * @param user
         * @return Set<ProductModel>
         */
        public static Set getShoppingCartContents(FDUserI user) {
            return getShoppingCartContents(user.getShoppingCart());
        }
	
        /**
         * helper to turn a shopping cart into a set of products
         * 
         * @return Set<ProductModel>
         */
	protected static Set getShoppingCartContents(FDCartModel cart) {
		List orderlines = cart.getOrderLines();
		Set products = new HashSet();
		for(Iterator i = orderlines.iterator(); i.hasNext();) {
			FDCartLineI cartLine = (FDCartLineI)i.next();
			products.add(ContentFactory.getInstance().getProduct(cartLine.getProductRef()));
		}
		return products;
	}

	/**
	 * @return recommendations. In case of failure it returns
	 *   a special EMPTY_RECOMMENDATION object having empty collection.
	 * @throws FDResourceException 
	 */
	public Recommendations getRecommendations(EnumSiteFeature siteFeature, FDUserI user, 
			SessionInput input, String overriddenVariantId) throws FDResourceException {
		Set cartItems = SmartStoreUtil.toContentKeySetFromModels(getShoppingCartContents(user.getShoppingCart()));
		return getRecommendations(siteFeature, user, input, overriddenVariantId, cartItems);
	}


        /**
         * Selects the 'best' fitting product from list.
         * This is currently the most expensive.
         * 
         * @param products List of ProductModel instances
         * 
         * @return The most expensive product as YmalSource
         */
        public static YmalSource resolveYmalSource(Collection products, ServletRequest request) {
            if (products == null || products.isEmpty()) {
                return null;
            } else  {
                YmalSource source = (YmalSource) Collections.max(products, ProductModel.PRODUCT_MODEL_PRICE_COMPARATOR_INVERSE);
                YmalUtil.resetActiveYmalSetSession(source, request);
                return source;
            }
        }

        /**
         * This method selects a good ymal source product, with it's parent category,
         * and assign to the given SessionInput.
         *  
         * @param input sessionInput
         * @param products List<ProductModel>
         */
        public static void initYmalSource(SessionInput input, FDUserI user, ServletRequest request) {
            Set cartContents = FDStoreRecommender.getShoppingCartContents( user ) ;
            input.setCartContents(SmartStoreUtil.toContentKeySetFromModels(cartContents));
            YmalSource ymal = resolveYmalSource(cartContents, request);
            if (ymal!=null) {
                input.setYmalSource(ymal);
                if (ymal instanceof ProductModel) {
                    input.setCategory((CategoryModel) ((ProductModel)ymal).getParentNode());
                }
            }
        }
	
	/**
	 * 
	 * @param trigger
	 * @param user the user
	 * @param input
	 * @param overriddenVariantId
	 * @param cartItems Set<ContentKey> of product keys
	 * @return
	 * @throws FDResourceException
	 */
	public Recommendations getRecommendations(EnumSiteFeature siteFeature, FDUserI user,
			SessionInput input, String overriddenVariantId, Set cartItems) throws FDResourceException
	{
		if (cartItems != null) {
		    input.setCartContents(cartItems);
		}

		// select service		
		RecommendationService service = 
			SmartStoreUtil.getRecommendationService(user, siteFeature, overriddenVariantId);
		
		
		List contentModels = doRecommend(input, service);

		LOGGER.debug("Items before filter: " + contentModels);


		// boolean includeCartItems = Boolean.valueOf(service.getVariant().getServiceConfig().get(SmartStoreServiceConfiguration.CKEY_INCLUDE_CART_ITEMS)).booleanValue();
		// filter unnecessary models
		List renderableProducts = filterProducts(contentModels, cartItems, service.isIncludeCartItems());
		
		// shave off the extra ones
		if (renderableProducts.size() > input.getMaxRecommendations()) 
			renderableProducts = renderableProducts.subList(0, input.getMaxRecommendations());
		
		LOGGER.debug("Recommended products by " + service.getVariant().getId() + ": " + renderableProducts);

		return new Recommendations(service.getVariant(), renderableProducts, input,
				service.isRefreshable(), service.isSmartSavings());
	}

	// mock point
	protected List doRecommend(SessionInput input, RecommendationService service) {
		return service.recommendNodes(input);
	}

	private static FDStoreRecommender instance = null;

	public static synchronized FDStoreRecommender getInstance() { 
		if (instance == null) {
			instance = new FDStoreRecommender();
		}
		return instance; 
	}
}

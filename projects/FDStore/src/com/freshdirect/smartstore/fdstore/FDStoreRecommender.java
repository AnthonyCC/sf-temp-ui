package com.freshdirect.smartstore.fdstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletRequest;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.ProductRef;
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

    public List<ContentNodeModel> filterProducts(List<ContentNodeModel> models, final Collection<ContentKey> cartItems, boolean includeCartItems, boolean useAlternatives) {
        List<ContentNodeModel> newModels = new ArrayList<ContentNodeModel>(models.size());

        ProductFilter filter = null;
        if (!includeCartItems) {
            filter = FilterFactory.createStandardFilter(cartItems, useAlternatives);
        } else {
            filter = FilterFactory.createStandardFilter(useAlternatives);
        }

        Iterator<ContentNodeModel> it = models.iterator();
        while (it.hasNext()) {
            ProductModel model = filter.filter((ProductModel) it.next());
            if (model != null) {
                newModels.add(model);
            }
        }

        return newModels;
    }

        /**
         * 
         * @param user
         * @return Set<ProductModel>
         */
        public static Set<ContentNodeModel> getShoppingCartContents(FDUserI user) {
            return getShoppingCartContents(user.getShoppingCart());
        }

        /**
         * 
         * @param user
         * @return
         */
        public static Set<ContentKey> getShoppingCartContentKeys(FDUserI user) {
            return getShoppingCartContentKeys(user.getShoppingCart());
        }
        
        /**
         * helper to turn a shopping cart into a set of products
         * 
         * @return Set<ProductModel>
         */
        protected static Set<ContentNodeModel> getShoppingCartContents(FDCartModel cart) {
            List<FDCartLineI> orderlines = cart.getOrderLines();
            Set<ContentNodeModel> products = new HashSet<ContentNodeModel>();
            for (FDCartLineI cartLine : orderlines) {
                products.add(ContentFactory.getInstance().getProduct(cartLine.getProductRef()));
            }
            return products;
        }
	
	protected static Set<ContentKey> getShoppingCartContentKeys(FDCartModel cart) {
            List<FDCartLineI> orderlines = cart.getOrderLines();
            Set<ContentKey> products = new HashSet<ContentKey>();
            for (FDCartLineI cartLine : orderlines) {
                ProductRef productRef = cartLine.getProductRef();
                products.add(productRef.getProductContentKey());
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
		Set<ContentKey> cartItems = getShoppingCartContentKeys(user.getShoppingCart());
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
        public static YmalSource resolveYmalSource(Collection<ContentNodeModel> products, ServletRequest request) {
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
            Set<ContentNodeModel> cartContents = FDStoreRecommender.getShoppingCartContents( user ) ;
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
			SessionInput input, String overriddenVariantId, Set<ContentKey> cartItems) throws FDResourceException
	{
		if (cartItems != null) {
		    input.setCartContents(cartItems);
		}

		// select service		
		RecommendationService service = 
			SmartStoreUtil.getRecommendationService(user, siteFeature, overriddenVariantId);
		
		
		List<ContentNodeModel> contentModels = doRecommend(input, service);

		LOGGER.debug("Items before filter: " + contentModels);


		// boolean includeCartItems = Boolean.valueOf(service.getVariant().getServiceConfig().get(SmartStoreServiceConfiguration.CKEY_INCLUDE_CART_ITEMS)).booleanValue();
		// filter unnecessary models
		List<ContentNodeModel> renderableProducts = filterProducts(contentModels, cartItems, service.isIncludeCartItems(), service.getVariant().isUseAlternatives());
		
		// shave off the extra ones
		// NOTE: Recommender no longer trim products to size.
		/** if (renderableProducts.size() > input.getMaxRecommendations()) { 
			renderableProducts = renderableProducts.subList(0, input.getMaxRecommendations());
		} **/
		
		LOGGER.debug("Recommended products by " + service.getVariant().getId() + ": " + renderableProducts);

		return new Recommendations(service.getVariant(), renderableProducts, input,
				service.isRefreshable(), service.isSmartSavings());
	}

	// mock point
	protected List<ContentNodeModel> doRecommend(SessionInput input, RecommendationService service) {
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

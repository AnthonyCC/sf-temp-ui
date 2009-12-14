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
import com.freshdirect.smartstore.Variant;
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
			SessionInput input, boolean ignoreOverriddenVariants) throws FDResourceException {
		Set<ContentKey> cartItems = getShoppingCartContentKeys(user.getShoppingCart());
		return getRecommendations(siteFeature, user, input, ignoreOverriddenVariants, cartItems);
	}

	public Recommendations getRecommendations(EnumSiteFeature siteFeature, FDUserI user, 
			SessionInput input) throws FDResourceException {
		Set<ContentKey> cartItems = getShoppingCartContentKeys(user.getShoppingCart());
		return getRecommendations(siteFeature, user, input, false, cartItems);
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
                YmalSource source = (YmalSource) Collections.min(products, ProductModel.PRODUCT_MODEL_PRICE_COMPARATOR_INVERSE);
                YmalUtil.resetActiveYmalSetSession(source, request);
                return source;
            }
        }

    /**
	 * This method selects a good ymal source product, with it's parent
	 * category, and assign to the given SessionInput.
	 * 
	 * @param input
	 *            sessionInput
	 * @param products
	 *            List<ProductModel>
	 */
	public static void initYmalSource(SessionInput input, FDUserI user,
			ServletRequest request) {
		Set<ContentNodeModel> cartContents = FDStoreRecommender.getShoppingCartContents(user);
		input.setCartContents(SmartStoreUtil.toContentKeySetFromModels(cartContents));
		YmalSource ymal = resolveYmalSource(cartContents, request);
		if (ymal != null) {
			input.setYmalSource(ymal);
			if (ymal instanceof ProductModel) {
				input.setCategory((CategoryModel) ((ProductModel) ymal).getParentNode());
			}
		}
	}

	public Recommendations getRecommendations(EnumSiteFeature siteFeature,
			FDUserI user, SessionInput input, Set<ContentKey> cartItems)
			throws FDResourceException {
		return getRecommendations(siteFeature, user, input, false, cartItems);
	}
        
	public Recommendations getRecommendations(EnumSiteFeature siteFeature,
			FDUserI user, SessionInput input, boolean ignoreOverriddenVariants,
			Set<ContentKey> cartItems) throws FDResourceException {
		Variant variant = VariantSelectorFactory.getSelector(siteFeature).select(user, ignoreOverriddenVariants);
		if (variant == null)
			throw new FDResourceException("error in configuration, no variant for site feature"
					+ siteFeature.getName() + " has been found");
		
		return getRecommendations(variant, user, input, cartItems);
	}
	
	public Recommendations getRecommendations(Variant variant,
			FDUserI user, SessionInput input, Set<ContentKey> cartItems) throws FDResourceException {
		if (cartItems != null) {
			input.setCartContents(cartItems);
		}

		RecommendationService service = variant.getRecommender();
		if (service == null)
			throw new FDResourceException("error in configuration, recommended not configured for variant "
					+ variant.getId() + " (site feature " + variant.getSiteFeature().getName() + ")");

		List<ContentNodeModel> contentModels = doRecommend(input, service);

		LOGGER.debug("Items before filter: " + contentModels);

		List<ContentNodeModel> renderableProducts = filterProducts(
				contentModels, cartItems, service.isIncludeCartItems(), variant.isUseAlternatives());

		LOGGER.debug("Recommended products by "
				+ variant.getId() + ": " + renderableProducts);

		return new Recommendations(variant, renderableProducts,
				input, service.isRefreshable(), service.isSmartSavings());
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

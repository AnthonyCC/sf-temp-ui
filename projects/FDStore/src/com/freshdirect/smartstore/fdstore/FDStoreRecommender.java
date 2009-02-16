package com.freshdirect.smartstore.fdstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Trigger;
import com.freshdirect.smartstore.impl.AbstractRecommendationService;

public class FDStoreRecommender {
    private final static Logger LOGGER = Logger.getLogger(FDStoreRecommender.class);

    

    /**
     * Filtering predicate for product models
     * 
     * @author zsombor
     */
    abstract static class FilterTemplate {
        protected abstract ProductModel evaluate(ProductModel model);

        protected FilterTemplate() {}


        /**
         *  Filters a list of products
         *  
         *  @param models List<ProductModel>
         */
        public List filter(List models) {
            List filteredModels = new ArrayList(models.size());
            for (Iterator i = models.iterator(); i.hasNext();) {
                ProductModel model = (ProductModel) i.next();
                ProductModel replaced = evaluate(model);
                if (replaced != null) {
                    filteredModels.add(replaced);
                }
            }
            return filteredModels;
        }
    }

	
	
	
    /**
     * check availabilities
     * 
     * @author zsombor
     *
     */
    static final class FilterAvailability extends FilterTemplate {
        
        protected ProductModel evaluate(ProductModel model) {
            if (available(model)) {
                return model;
            }

            for (Iterator i = model.getRecommendedAlternatives().iterator(); i.hasNext();) {
                ContentNodeModel alternativeModel = (ContentNodeModel) i.next();
                if (available(alternativeModel)) {
                    if (alternativeModel instanceof ProductModel) {
                        return (ProductModel) alternativeModel;
                    } else if (alternativeModel instanceof SkuModel) {
                        return evaluate((ProductModel) alternativeModel.getParentNode());
                    }
                }
            }
            return null;
        }

        private boolean available(ContentNodeModel model) {
            if (model instanceof SkuModel) {
                return !((SkuModel) model).getProductModel().isUnavailable();
            } else if (model instanceof ProductModel) {
                ProductModel p = (ProductModel) model;
                return p.isDisplayable();
            } else {
                return true;
            }
        }
    }

    /**
     * Filter out items which are on the cart already.
     * 
     * @author zsombor
     *
     */
    final static class FilterCartItems extends FilterTemplate {
        private final Collection cartItems;

        FilterCartItems(Collection cartItems) {
            this.cartItems = cartItems;
        }

        protected ProductModel evaluate(ProductModel model) {
        	return cartItems.contains(model.getContentKey()) ? null : model;
        }
    }


    final static class FilterExcludedItems extends FilterTemplate {
        protected ProductModel evaluate(ProductModel model) {
        	return model.isExcludedRecommendation() ? null : model;
        }
    }

    /**
     * This filter removes item duplicates producing a list of unique items
     * 
     * @author segabor
     *
     */
    final static class UnicityFilter extends FilterTemplate {
    	public List filter(List models) {
    		List ret = new ArrayList();
    		
    		HashSet keys = new HashSet();
    		for (Iterator it=models.iterator(); it.hasNext();) {
    			ProductModel prd = (ProductModel) it.next();
    			String prdId = prd.getContentName();
    			if (!keys.contains(prdId)) {
    				ret.add(prd);
    				keys.add(prdId);
    			}
    		}

    		return ret;
    	}

    	// not used
		protected ProductModel evaluate(ProductModel model) {
			return null;
		}
    }


    final static FilterTemplate UNIQUE_ITEMS = new UnicityFilter();
    final static FilterTemplate EXCLUDED_ITEMS = new FilterExcludedItems();
    final static FilterTemplate AVAILABLE_ITEMS = new FilterAvailability();

	
    /**
     * 
     * 
     */
    private List filter(List models, final Collection cartItems, boolean includeCartItems) {
    	models = UNIQUE_ITEMS.filter(models);

        models = EXCLUDED_ITEMS.filter(models);

        if (!includeCartItems) {
        	models = new FilterCartItems(cartItems).filter(models);
        }

        models = AVAILABLE_ITEMS.filter(models);

        return models;
	}

        
    public static Set getShoppingCartContents(FDUserI user) {
        return getShoppingCartContents(user.getShoppingCart());
    }
	
	// helper to turn a shopping cart into a set of products
	protected static Set getShoppingCartContents(FDCartModel cart) {
		List orderlines = cart.getOrderLines();
		Set products = new HashSet();
		for(Iterator i = orderlines.iterator(); i.hasNext();) {
			FDCartLineI cartLine = (FDCartLineI)i.next();
			products.add(SmartStoreUtil.getProductContentKey(cartLine.getSkuCode()));
		}
		return products;
	}


	/**
	 * @return recommendations. In case of failure it returns
	 *   a special EMPTY_RECOMMENDATION object having empty collection.
	 * @throws FDResourceException 
	 */
	public  Recommendations getRecommendations(Trigger trigger, HttpSession session) throws FDResourceException {
		// Get shopping cart and user id
		// FIXME: replace 'fd.user' constant to SessionName.USER
		FDUserI user = (FDUserI) session.getAttribute("fd.user");

		String overriddenVariantId = (String)session.getAttribute("SmartStore.VariantID");
		
		SessionInput input = new SessionInput(user);
		return getRecommendations(trigger, user, input, overriddenVariantId);
	}


	/**
	 * @return recommendations. In case of failure it returns
	 *   a special EMPTY_RECOMMENDATION object having empty collection.
	 * @throws FDResourceException 
	 */
	public  Recommendations getRecommendations(Trigger trigger, FDUserI user, SessionInput input, String overriddenVariantId) throws FDResourceException {
		Set cartItems = getShoppingCartContents(user.getShoppingCart());
		return getRecommendations(trigger, user, input, overriddenVariantId, cartItems);
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
	public  Recommendations getRecommendations(Trigger trigger, FDUserI user, SessionInput input, String overriddenVariantId, Set cartItems) throws FDResourceException {
		input.setCartContents(cartItems);

		// select service		
		RecommendationService service = 
			SmartStoreUtil.getRecommendationService(user, trigger.getSiteFeature(),overriddenVariantId);
		
		
		List contentModels = doRecommend(trigger, input, service);
		
		LOGGER.debug("Items before filter: " + contentModels);
		

		boolean includeCartItems = Boolean.valueOf(service.getVariant().getServiceConfig().get(AbstractRecommendationService.CKEY_INCLUDE_CART_ITEMS)).booleanValue();
		// filter unnecessary models
		List renderableModels = filter(contentModels,cartItems, includeCartItems);


		// shave off the extra ones
		while(renderableModels.size() > trigger.getMaxRecommendations()) { 
			renderableModels.remove(renderableModels.size()-1);
		}
		
		LOGGER.debug("Recommended items by " + service.getVariant().getId() + ": " + renderableModels);

		return new Recommendations(service.getVariant(),renderableModels);
		
	}


	// mock point
	protected List doRecommend(Trigger trigger, SessionInput input, RecommendationService service) {
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

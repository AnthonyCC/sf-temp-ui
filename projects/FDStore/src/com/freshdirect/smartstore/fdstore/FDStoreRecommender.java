package com.freshdirect.smartstore.fdstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.Recipe;
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
     * @author csongor
     */
    abstract static class FilterTemplate {
    	public abstract List filter(List models);
    }
    
    /**
     * Filtering predicate for product models
     * 
     * @author zsombor
     */
    abstract static class ProductFilterTemplate extends FilterTemplate {
        protected abstract ProductModel evaluate(ProductModel model);

        protected ProductFilterTemplate() {}


        /**
         *  Filters a list of products
         *  
         *  @param models List<ProductModel>
         */
        public List filter(List models) {
            List filteredModels = new ArrayList(models.size());
            for (Iterator i = models.iterator(); i.hasNext();) {
            	Object next = i.next();
            	if (next instanceof ProductModel) {
	                ProductModel model = (ProductModel) next;
	                ProductModel replaced = evaluate(model);
	                if (replaced != null) {
	                    filteredModels.add(replaced);
	                }
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
    static final class FilterProductAvailability extends ProductFilterTemplate {
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
    final static class FilterCartItems extends ProductFilterTemplate {
        private final Collection cartItems;

        FilterCartItems(Collection cartItems) {
            this.cartItems = cartItems;
        }

        protected ProductModel evaluate(ProductModel model) {
        	return cartItems.contains(model.getContentKey()) ? null : model;
        }
    }


    final static class FilterExcludedItems extends ProductFilterTemplate {
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
    final static class UnicityFilter extends ProductFilterTemplate {
    	public List filter(List models) {
    		List ret = new ArrayList();
    		
    		HashSet keys = new HashSet();
    		for (Iterator it=models.iterator(); it.hasNext();) {
    			ContentNodeModel item = (ContentNodeModel) it.next();
    			String prdId = item.getContentName();
    			if (!keys.contains(prdId)) {
    				ret.add(item);
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

    /**
     * @author csongor
     */
    final static class CategoryFilter extends FilterTemplate {
		public List filter(List models) {
            List filteredModels = new ArrayList(models.size());
            for (Iterator i = models.iterator(); i.hasNext();) {
            	Object next = i.next();
            	if (next instanceof CategoryModel) {
                    filteredModels.add(next);
            	}
            }
            return filteredModels;
		}
    }

    /**
     * added both type and availability filter
     * 
     * @author csongor
     */
    final static class RecipeFilter extends FilterTemplate {
		public List filter(List models) {
            List filteredModels = new ArrayList(models.size());
            for (Iterator i = models.iterator(); i.hasNext();) {
            	Object next = i.next();
            	if (next instanceof Recipe) {
            		Recipe r = (Recipe) next;
            		if (r.isAvailable())
            			filteredModels.add(r);
            	}
            }
            return filteredModels;
		}
    }
    
    final static ProductFilterTemplate UNIQUE_ITEMS = new UnicityFilter();

    final static ProductFilterTemplate EXCLUDED_ITEMS = new FilterExcludedItems();

    final static ProductFilterTemplate AVAILABLE_ITEMS = new FilterProductAvailability();
    
    final static FilterTemplate CATEGORY_FILTER = new CategoryFilter();

    final static FilterTemplate RECIPE_FILTER = new RecipeFilter();

	private List filterRecipes(List models) {
		return UNIQUE_ITEMS.filter(RECIPE_FILTER.filter(models));
	}

	private List filterCategories(List models) {
		return UNIQUE_ITEMS.filter(CATEGORY_FILTER.filter(models));
	}

	private List filterProducts(List models, final Collection cartItems, boolean includeCartItems) {
        models = AVAILABLE_ITEMS.filter(models);

        models = UNIQUE_ITEMS.filter(models);

        models = EXCLUDED_ITEMS.filter(models);

        if (!includeCartItems) {
        	models = new FilterCartItems(cartItems).filter(models);
        }


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
	public Recommendations getRecommendations(Trigger trigger, 
			HttpSession session) throws FDResourceException {
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
	public Recommendations getRecommendations(Trigger trigger, FDUserI user, 
			SessionInput input, String overriddenVariantId) throws FDResourceException {
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
	public Recommendations getRecommendations(Trigger trigger, FDUserI user,
			SessionInput input, String overriddenVariantId, Set cartItems) throws FDResourceException
	{
		if (cartItems != null)
			input.setCartContents(cartItems);

		// select service		
		RecommendationService service = 
			SmartStoreUtil.getRecommendationService(user, trigger.getSiteFeature(), overriddenVariantId);
		
		
		List contentModels = doRecommend(trigger, input, service);

		LOGGER.debug("Items before filter: " + contentModels);


		boolean includeCartItems = Boolean.valueOf(service.getVariant().getServiceConfig().get(AbstractRecommendationService.CKEY_INCLUDE_CART_ITEMS)).booleanValue();
		// filter unnecessary models
		List renderableProducts = filterProducts(contentModels, cartItems, includeCartItems);
		
		List renderableCategories = filterCategories(contentModels);
		
		List renderableRecipes = filterRecipes(contentModels);


		// shave off the extra ones
		while(renderableProducts.size() > trigger.getMaxRecommendations()) { 
			renderableProducts.remove(renderableProducts.size()-1);
		}
		
		LOGGER.debug("Recommended products by " + service.getVariant().getId() + ": " + renderableProducts);

		return new Recommendations(service.getVariant(), renderableProducts,
				renderableCategories, renderableRecipes, input);
		
	}

	// mock point
	protected List doRecommend(Trigger trigger, SessionInput input, RecommendationService service) {
		return service.recommendNodes(trigger, input);
	}

	private static FDStoreRecommender instance = null;

	public static synchronized FDStoreRecommender getInstance() { 
		if (instance == null) {
			instance = new FDStoreRecommender();
		}
		return instance; 
	}
}

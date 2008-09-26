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

import com.freshdirect.cms.ContentKey;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.smartstore.RecommendationService;
import com.freshdirect.smartstore.SessionInput;
import com.freshdirect.smartstore.Trigger;
import com.freshdirect.smartstore.Variant;

public class FDStoreRecommender {
	private final static Logger LOGGER = Logger.getLogger(FDStoreRecommender.class);
	
	public static final Recommendations EMPTY_RECOMMENDATION = new Recommendations(Variant.BAD_VARIANT, Collections.EMPTY_LIST);

	
	// getInstance
	// + initialization of configs (variant, strats, etc)
	
	
	private static boolean available(ContentNodeModel model) {
		
		if (model instanceof SkuModel) return !((SkuModel)model).getProductModel().isUnavailable();
		else if (model instanceof ProductModel) {
			ProductModel p = (ProductModel)model;
			return !(p.isHidden() || p.isDiscontinued() || p.isUnavailable() || p.isOrphan());
		}
		else return true;
	}
	
	
	// Filtering predicate for product models
	private abstract class FilterTemplate {
		
		protected abstract ProductModel evaluate(ProductModel model);
		
		private List models;
		
		protected FilterTemplate(List models) {
			this.models = models;
		}
		
		public List filter() {
			List filteredModels = new ArrayList(models.size());
			for(Iterator i = models.iterator(); i.hasNext(); ) {
				ProductModel model = (ProductModel)i.next();
				ProductModel replaced = evaluate(model);
				if (replaced != null) filteredModels.add(replaced);
//				else LOGGER.debug(model + " sorted out");
			}
			return filteredModels;
		}
		
	}
	
	/**
	 * 
	 * TODO: refactor filtering ... some specific to DYF, some not
	 */
	private List filter(List models, final Collection cartItems) {
		
		// check for explicit exclusion
		models = new FilterTemplate(models) {
			protected ProductModel evaluate(ProductModel model) {
				return model.isExcludedRecommendation() ? null : model;
			}
		}.filter();
		
		// check for duplicates
		models = new FilterTemplate(models) {
			protected ProductModel evaluate(ProductModel model) {
				return cartItems.contains(model.getContentKey()) ? null : model;
			}
		}.filter();
		
		// check availabilities
		models = new FilterTemplate(models) {
			protected ProductModel evaluate(ProductModel model) {
				if (available(model)) return model;
				
				for(Iterator i = model.getRecommendedAlternatives().iterator(); i.hasNext();) {
					ContentNodeModel alternativeModel = (ContentNodeModel)i.next();
					if (available(alternativeModel)) {
						if (alternativeModel instanceof ProductModel) return (ProductModel)alternativeModel;
						else if (alternativeModel instanceof SkuModel) return evaluate((ProductModel)alternativeModel.getParentNode());
						
					}
				}
				return null;
			}
		}.filter();

		// sort out inconsistent product, prevent crash at display level.
		// this is a rough check.
//		models = new FilterTemplate(models) {
//			protected ProductModel evaluate(ProductModel model) {
//				boolean isValid = true;
//				
//				try {
//					isValid = !(
//						model.getFullName() == null ||
//						model.getPrimaryBrandName() == null ||
//						model.getSizeDescription() == null ||
//						model.getDefaultPrice() == null
//					);
//				} catch (Exception exc) {
//					// invalidate product
//					isValid = false;
//				}
//				
//				if (!isValid) {
//					LOGGER.error("Bad product " + model);
//				}
//				
//				return isValid ? model : null;
//			}
//		}.filter();


		return models;
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
		if (user == null)
			return EMPTY_RECOMMENDATION;
		
		SessionInput input = new SessionInput(user.getIdentity().getErpCustomerPK());

		Set cartItems = getShoppingCartContents(user.getShoppingCart());
		input.setCartContents(cartItems);

		String overriddenVariantId = (String)session.getAttribute(trigger.getSiteFeature().getName() + ".VariantID");
		
		// select service		
		RecommendationService service = 
			SmartStoreUtil.getRecommendationService(user, trigger.getSiteFeature(),overriddenVariantId);
		
		
		List contentKeys = doRecommend(trigger, input, service);
		
		// remove duplicates and turn content keys into content nodes
		Set seen = new HashSet();

		List contentModels = new ArrayList(contentKeys.size());
		for(Iterator i = contentKeys.iterator(); i.hasNext();) {
			ContentKey contentKey = (ContentKey)i.next();

			// get product
			ProductModel prdModel = (ProductModel) ContentFactory.getInstance().getContentNodeByKey(contentKey);
			
			if (seen.contains(prdModel.getContentKey())) continue;
			seen.add(prdModel.getContentKey());
			
			contentModels.add(prdModel);
		}
	
		LOGGER.debug("Items before filter: " + contentModels);
		
		// filter duplicates
		List renderableModels = filter(contentModels,cartItems);
		
		
		// shave off the extra ones
		while(renderableModels.size() > trigger.getMaxRecommendations()) { 
			renderableModels.remove(renderableModels.size()-1);
		}
		
		LOGGER.debug("Recommended items by " + service.getVariant().getId() + ": " + contentModels);

		return new Recommendations(service.getVariant(),renderableModels);
		
	}


	// mock point
	protected List doRecommend(Trigger trigger, SessionInput input, RecommendationService service) {
		return service.recommend(trigger.getMaxRecommendations() * 2, input);
	}


	private static FDStoreRecommender instance = null;


	public static synchronized FDStoreRecommender getInstance() { 
		if (instance == null) {
			instance = new FDStoreRecommender();
		}
		return instance; 
	}
}

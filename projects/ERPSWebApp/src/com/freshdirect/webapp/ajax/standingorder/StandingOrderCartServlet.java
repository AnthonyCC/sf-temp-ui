package com.freshdirect.webapp.ajax.standingorder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.common.pricing.PricingException;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.RecipeVariant;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerListItem;
import com.freshdirect.fdstore.lists.FDCustomerProductListLineItem;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.cart.CartOperations;
import com.freshdirect.webapp.ajax.cart.data.AddToCartItem;
import com.freshdirect.webapp.ajax.shoppinglist.AddToListRequestData;
import com.freshdirect.webapp.ajax.shoppinglist.AddToListResponseItem;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.util.StandingOrderHelper;

public class StandingOrderCartServlet extends BaseJsonServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3987286560828009595L;

	private static final Logger LOG = LoggerFactory.getInstance(StandingOrderCartServlet.class);
	
	private static final String ACTION_TURN_OFF_REMINDER_OVERLAY="turnOffReminderOverlay";

	@Override
	protected boolean synchronizeOnUser() {
		return false; // no need to synchronize
	}

	@Override
	protected int getRequiredUserLevel() {
		return FDUserI.GUEST;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		final FDStandingOrdersManager m = FDStandingOrdersManager.getInstance();

		try {

			Collection<FDStandingOrder> sos = m.getValidStandingOrder(user.getIdentity());
			Collection<Map<String, Object>> so3Details=StandingOrderHelper.convertStandingOrderToSoy(sos,false,false);
			writeResponseData(response,so3Details);
		} catch (FDResourceException e) {
			LOG.error("EORRO WHILE GETTING THE STANDING ORDER DETAILS", e);
		} catch (FDInvalidConfigurationException e) {
			LOG.error("EORRO WHILE GETTING THE STANDING ORDER DETAILS", e);
		} catch (PricingException e) {
			LOG.error("EORRO WHILE GETTING THE STANDING ORDER DETAILS", e);
		} 
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		StandingOrderResponseData orderResponseData=new StandingOrderResponseData();


		if (user.getLevel() > FDUserI.RECOGNIZED) {
			AddToListRequestData reqData = parseRequestData(request, AddToListRequestData.class);

			try {
			
				if (null != reqData.getActiontype() && "AddProductToSO".equalsIgnoreCase(reqData.getActiontype())) {

				   if(validateSO3AlcoholResrtiction(reqData,user)){
					 orderResponseData.setAlcohol(true);
				   } else {
					storeCustomerList(reqData, user);
					storeDefualtStandingOrder(reqData, user);
				
					FDStandingOrder so = FDStandingOrdersManager.getInstance().load(new PrimaryKey(reqData.getStandingOrderId()));
					 if(null!=so){
						 FDActionInfo info = AccountActivityUtil.getActionInfo(request.getSession());
						 if(!so.getStandingOrderCart().getOrderLines().isEmpty()){
							 so.getStandingOrderCart().refreshAll(true);
						 }
						 if(so.getLastErrorCode()!=null && StandingOrderHelper.getTotalAmountForSoSettings(so)>=ErpServicesProperties.getStandingOrderSoftLimit()){    
							 StandingOrderHelper.clearSO3ErrorDetails(so, new String[]{"MINORDER","TIMESLOT_MINORDER"}) ;
							 FDStandingOrdersManager.getInstance().manageStandingOrder(info, so.getStandingOrderCart(), so, null) ;
						 } if("Y".equalsIgnoreCase(reqData.getAlcoholVerified())){
							 so.setAlcoholAgreement(true);
							 FDStandingOrdersManager.getInstance().manageStandingOrder(info, so.getStandingOrderCart(), so, null) ;

						 }
						orderResponseData=StandingOrderHelper.populateResponseData(so,true);
	
					 } else {
						 orderResponseData.setMessage("Standing Order has been deleted. " +
						 		" Please choose another Standing Order") ;
					 }
				}
				// Save user
				saveUser(user);

			  }
			} catch (FDResourceException e) {
			 orderResponseData.setMessage("ERROR WHILE RETRIEVING THE STANDING ORDER DATA") ;
			 orderResponseData.setSuccess(false);
			 LOG.error(" ERROR WHILE RETRIEVING THE STANDING ORDER DATA"+ reqData.getStandingOrderId());
			} catch (FDInvalidConfigurationException e) {
			 orderResponseData.setMessage("ERROR WHILE RETRIEVING THE STANDING ORDER DATA") ;
			 orderResponseData.setSuccess(false);
			 LOG.error(" ERROR WHILE RETRIEVING THE STANDING ORDER DATA"+ reqData.getStandingOrderId());
			}
		
			if (null != reqData.getActiontype()	&& ACTION_TURN_OFF_REMINDER_OVERLAY.equalsIgnoreCase(reqData.getActiontype())
					&& user.getCurrentStandingOrder() != null) {
				try {
					if (user.getCurrentStandingOrder().getId() != null)
						FDStandingOrdersManager.getInstance().turnOffReminderOverLayNewSo(user.getCurrentStandingOrder().getId());
				} catch (FDResourceException e) {
					LOG.error("Got the exeption while updating the RemiderOverlay flag for New Standing order"+e);
				}
			}
		}else{
			// User level not sufficient.
			 orderResponseData.setMessage("User Session is expired please try login to add the product to Standing order") ;
			 orderResponseData.setError("Session Expired");
			 orderResponseData.setSuccess(false);
		}
		writeResponseData(response,orderResponseData);


	}

	protected boolean validateSO3AlcoholResrtiction(AddToListRequestData reqData, FDUserI user) {

		String soId=reqData.getStandingOrderId();
		FDStandingOrder so = null;
		boolean isAlcoholPopupDisplay=false;
		try {
			
			    FDProduct product = FDCachedFactory.getProduct(FDCachedFactory.getProductInfo(reqData.getItems().get(0).getSkuCode()));

				if(product.isAlcohol() && !"Y".equalsIgnoreCase(reqData.getAlcoholVerified())){
					so = FDStandingOrdersManager.getInstance().load(new PrimaryKey(soId));
					if(!so.isAlcoholAgreement()){
						isAlcoholPopupDisplay=true;
					}
				}
			
		} catch (FDResourceException e) {
            LOG.error("ERROR WHILE GETTING STANDING ORDER : ID "+ soId);
		} catch (FDSkuNotFoundException e) {
			LOG.error("ERROR WHILE validating STANDING ORDER : ID "+ soId);
		}
		return isAlcoholPopupDisplay;
	}
	
	
	protected void storeDefualtStandingOrder(AddToListRequestData reqData,
			FDUserI user) throws HttpErrorResponse{
		try {
			FDStandingOrdersManager.getInstance().updateDefaultStandingOrder(reqData.getListId(),user.getIdentity()); 
		} catch (FDResourceException e) {
			returnHttpError(500, "System error (FDResourceException) - couldn't persist Standing Order list", e); // 500
																											// Internal// Server																									// Error
		}
	}

	/**
	 * @param reqData
	 * @param user
	 * @return
	 * @throws HttpErrorResponse
	 */
	private List<AddToListResponseItem> storeCustomerList(AddToListRequestData reqData, FDUserI user) throws HttpErrorResponse {
		// Parse request data

		String listId = reqData.getListId();
		String recipeId = reqData.getRecipeId();
		String recipeName = reqData.getRecipeName();
		if (listId == null) {
			// Creating new list - just validate at this point
				returnHttpError(400, "No list id provided for standing order"); // 400
																			// Bad
																			// Request

		} else if (recipeId != null) {
			returnHttpError(400, "Creating list from recipe only works when creating new list!"); // 400
																									// Bad
																									// Request
		}

		List<AddToCartItem> items = null;

		if (recipeId != null) {
			// Collect items from recipe ingredients
			items = new ArrayList<AddToCartItem>();
			recipeName = collectRecipeIngredients(items, recipeId, reqData.getRecipeVariantId());
		} else {
			// Use item list from request
			items = reqData.getItems();
		}

		if (items == null) {
			returnHttpError(400, "Bad JSON - items is missing"); // 400 Bad  Request
		}

		boolean listCreated = false;

		FDCustomerList list = null;
		try {
			list = FDListManager.getCustomerListById(user.getIdentity(), EnumCustomerListType.SO, listId);
		} catch (FDResourceException e) {
			returnHttpError(500, "System error (FDResourceException)", e); // 500 Internal Server
		}

		if (list == null) {
			returnHttpError(500, "Failed to get Standing order list : " + listId); // 500 Internal Server Error
		}

		// prepare response items holder
		List<AddToListResponseItem> responseItems = new ArrayList<AddToListResponseItem>();
		// Add items to list
		addItemsToList(list, items, recipeId, recipeName, responseItems, listCreated);

		// Save list
		try {
			list = FDListManager.storeCustomerList(list);
			if(StandingOrderHelper.isEligibleForSo3_0(user) && reqData.getStandingOrderId()!=null){
				List<FDCustomerListItem> cartLine=list.getLineItems();
				for (Iterator<FDCustomerListItem> iterator = cartLine.iterator(); iterator.hasNext();) {
						FDCustomerProductListLineItem fDCustomerProductListLineItem = (FDCustomerProductListLineItem) iterator.next();
							if(fDCustomerProductListLineItem.isSojustAddedItemToCart() && null != fDCustomerProductListLineItem.getPK())
									user.getSoCartLineMessagesMap().put(fDCustomerProductListLineItem.getPK().getId(), "NewItem");
				}
			}
		} catch (FDResourceException e) {
			returnHttpError(500, "System error (FDResourceException) - couldn't persist Standing order  list", e); // 500 Internal Server Error
		}
		return responseItems;
	}

	private static void addItemsToList(FDCustomerList list, List<AddToCartItem> items, String recipeId, String recipeName,
			List<AddToListResponseItem> responseItems, boolean listCreated) {

		for (AddToCartItem item : items) {

			AddToListResponseItem responseItem = new AddToListResponseItem();
			try {
				FDCustomerProductListLineItem listItem = createListLineItem(item, recipeId, false);
				list.addLineItem(listItem);
				listItem.setSojustAddedItemToCart(true);
				// create response item
				responseItem.setItemName(listItem.getFullName());
				responseItem.setMessage("Added to List");
			} catch (FDResourceException e) {
				responseItem.setMessage("Item not added to list: " + item.getLineId());
			}

			responseItem.setLineId(item.getLineId());
			responseItem.setListId(list.getId());
			responseItem.setListName(list.getName());
			responseItem.setListCreated(listCreated);
			responseItems.add(responseItem);
		}

		// Set recipe id for list
		list.setRecipeId(recipeId);
		list.setRecipeName(recipeName);
	}

	public static FDCustomerProductListLineItem createListLineItem(AddToCartItem item, String recipeId, boolean editItem)
			throws FDResourceException {
		// extra fail-safe sales-unit/quantity handling
		double quantity = CartOperations.extractQuantity(item);

		if ((item.getSalesUnit() == null || item.getSalesUnit().trim().isEmpty()) && quantity == 0.0) {
			// has no sales-unit, nor quantity set => skip item
			LOG.warn("Warning: skipped item " + item.getSkuCode() + ", because of missing quantity and sales-unit.");
			throw new FDResourceException();
		}
		if (item.getSalesUnit() != null && !item.getSalesUnit().trim().isEmpty() && quantity == 0.0) {
			// has a sales-unit, but no quantity has been set => set quantity to
			// one
			item.setQuantity("1.0");
		}
		if ((item.getSalesUnit() == null || item.getSalesUnit().trim().isEmpty()) && quantity != 0.0) {
			// has no sales-unit set, only quantity => set sales-unit to default
			try {
				item.setSalesUnit(FDCachedFactory.getProduct(FDCachedFactory.getProductInfo(item.getSkuCode())).getSalesUnits()[0]
						.getName());
			} catch (FDResourceException e) {
				LOG.warn("Warning: skipped item " + item.getSkuCode() + ", because of invalid quantity and sales-unit.");
				throw new FDResourceException(e);
			} catch (FDSkuNotFoundException e) {
				LOG.warn("Warning: skipped item " + item.getSkuCode() + ", because of invalid quantity and sales-unit.");
				throw new FDResourceException(e);
			} catch (NullPointerException e) {
				LOG.warn("Warning: skipped item " + item.getSkuCode() + ", because of invalid quantity and sales-unit.");
				throw new FDResourceException(e);
			}
		}

		// Paranoid check
		if (item.getSalesUnit() == null || item.getSalesUnit().trim().isEmpty()) {
			LOG.warn("Missing salesunit!");
			throw new FDResourceException();
		}

		// create and add list item
		FDConfiguration config = new FDConfiguration(quantity, item.getSalesUnit(), item.getConfiguration());
		FDCustomerProductListLineItem listItem = new FDCustomerProductListLineItem(item.getSkuCode(), config, recipeId);

		if (editItem && item.getLineId() != null) {
			listItem.setId(item.getLineId());
		}

		return listItem;
	}

	private static String collectRecipeIngredients(List<AddToCartItem> items, String recipeId, String recipeVariantId) {
		// Collect recipe ingredients
		Recipe recipe = (Recipe) ContentFactory.getInstance().getContentNodeByKey(
		        ContentKey.getContentKey(ContentType.get("Recipe"), recipeId));
		RecipeVariant recipeVariant = null;

		if (recipeVariantId != null) {
			List<RecipeVariant> variants = recipe.getVariants();
			for (RecipeVariant var : variants) {
				if (recipeVariantId.equals(var.getContentName())) {
					recipeVariant = var;
					break;
				}
			}
		}
		if (recipeVariant == null) {
			recipeVariant = recipe.getDefaultVariant();
		}

		List<ConfiguredProduct> ingredients = recipeVariant.getSections().get(0).getIngredients();

		for (ConfiguredProduct product : ingredients) {
			if (product.getProduct() != null) {
				AddToCartItem item = new AddToCartItem();
				item.setProductId(product.getProduct().getContentName());
				item.setCategoryId(product.getCategory().getContentName());
				item.setSkuCode(product.getSkuCode());
				item.setQuantity(Double.toString(product.getQuantity()));
				item.setSalesUnit(product.getSalesUnit());
				item.setConfiguration(product.getConfiguration().getOptions());
				item.setRecipeId(recipeId);
				items.add(item);
			}
		}
		return recipe.getName();
	}

	
}

package com.freshdirect.webapp.ajax.standingorder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.RecipeVariant;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerListExistsException;
import com.freshdirect.fdstore.lists.FDCustomerListInfo;
import com.freshdirect.fdstore.lists.FDCustomerProductListLineItem;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.cart.CartOperations;
import com.freshdirect.webapp.ajax.cart.data.AddToCartItem;
import com.freshdirect.webapp.ajax.expresscheckout.service.StandingOrderHelperService;
import com.freshdirect.webapp.ajax.shoppinglist.AddToListRequestData;
import com.freshdirect.webapp.ajax.shoppinglist.AddToListResponseItem;
import com.freshdirect.webapp.ajax.shoppinglist.ShoppingListChange;
import com.freshdirect.webapp.ajax.shoppinglist.ShoppingListInfo;
import com.freshdirect.webapp.ajax.shoppinglist.ShoppingListRequestData;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.util.StandingOrderHelper;

public class StandingOrderCartServlet extends BaseJsonServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3987286560828009595L;

	private static final Logger LOG = LoggerFactory.getInstance(StandingOrderCartServlet.class);

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
		// TODO need to get Standing order details
		final FDStandingOrdersManager m = FDStandingOrdersManager.getInstance();

		try {

			Collection<FDStandingOrder> validSO = m.getValidStandingOrder(user.getIdentity());
            
		} catch (FDResourceException e) {
			// TODO Auto-generated catch block
			LOG.error("EORRO WHILE GETTING THE STANDING ORDER DETAILS", e);
		} catch (FDInvalidConfigurationException e) {
			LOG.error("EORRO WHILE GETTING THE STANDING ORDER DETAILS", e);
		} 
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {

		if (user.getLevel() < FDUserI.SIGNED_IN) {
			// User did not login. Login required for delete/modify.
			returnHttpError(401, "User not logged in!"); // 401 Unauthorized
		}

		// Parse request data
		ShoppingListRequestData reqData = parseRequestData(request, ShoppingListRequestData.class);

		List<ShoppingListChange> newListInfos = reqData.getListInfos();
		if (newListInfos == null) {
			returnHttpError(400, "Bad JSON - listInfos is missing"); // 400 Bad
																		// Request
		}

		List<ShoppingListInfo> oldListInfos = collectListInfos(user);
		Set<String> oldListIds = new HashSet<String>();
		for (ShoppingListInfo inf : oldListInfos) {
			oldListIds.add(inf.getListId());
		}

		for (ShoppingListChange newInfo : newListInfos) {
			String listId = newInfo.getListId();
			// ====== VALIDATION ======
			if (listId == null) {
				// Invalid, no ID
				LOG.warn("Missing ID for shopping list change request, skipping.");
				continue;
			}

			if (!oldListIds.contains(listId)) {
				// It's not your list!
				LOG.warn("Invalid ID for shopping list change request : " + listId
						+ " - list belongs to another customer or does not exist, skipping.");
				continue;
			}

			// ====== DELETE ======
			if (newInfo.isDelete()) {
				deleteList(user, listId);
				continue;
			}

			// ====== SET DEFAULT LIST ======
			if (newInfo.isDefault()) {
				user.setDefaultListId(newInfo.getListId());
			}

			// ====== RENAME ======
			String newName = newInfo.getName();
			if (newName != null && !newName.trim().equals("")) {
				renameList(newInfo, listId, newName);
				continue;
			}

			if (newInfo.isEmpty()) {
				deleteListItems(user, listId);
				continue;
			}
		}

		// Save user
		saveUser(user);

		// Query and send back the new state
		// getInternal(request, response, user, true, null);

	}

	private void deleteListItems(FDUserI user, String listId) {
		LOG.info("Deleting list items " + listId);
		try {
			FDCustomerCreatedList customerCreatedList = FDListManager.getCustomerCreatedList(user.getIdentity(), listId);
			customerCreatedList.removeAllLineItems();
			FDListManager.storeCustomerList(customerCreatedList);
			LOG.info("Deleted list items " + listId);
		} catch (FDResourceException e) {
			LOG.error("Failed to delete list items: " + listId, e);
		}
	}

	private void renameList(ShoppingListChange newInfo, String listId, String newName) {
		LOG.info("renaming list " + newInfo.getListId() + " to " + newInfo.getName());
		try {
			FDListManager.renameShoppingList(listId, newName);
			LOG.info("Renamed list " + listId + " to " + newName);
		} catch (FDResourceException e) {
			LOG.error("Failed to rename list: " + listId, e);
		}
	}

	private void deleteList(FDUserI user, String listId) {
		LOG.info("Deleting list " + listId);
		try {
			FDListManager.deleteShoppingList(listId);
			if (listId.equals(user.getDefaultListId())) {
				user.setDefaultListId(null);
			}
			LOG.info("Deleted list " + listId);
		} catch (FDResourceException e) {
			LOG.error("Failed to delete list: " + listId, e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
		StandingOrderResponseData orderResponseData=new StandingOrderResponseData();


		if (user.getLevel() >= FDUserI.RECOGNIZED) {

			AddToListRequestData reqData = parseRequestData(request, AddToListRequestData.class);
			if (null != reqData.getActiontype() && "SaveStandingOrder".equalsIgnoreCase(reqData.getActiontype())) {
				StandingOrderHelperService helperService = new StandingOrderHelperService();
				try {
					helperService.saveStandingOrder(user);
				} catch (FDResourceException e) {
					LOG.error("ERROR WHILE SAVING STANDING ORDER TEMPLATE", e);
				}
	
			} else if (null != reqData.getActiontype() && "AddProductToSO".equalsIgnoreCase(reqData.getActiontype())) {
				// To store product details for given customer list id and list type
				// should be SO
	
				storeCustomerList(reqData, user);
				storeDefualtStandingOrder(reqData, user);
				
				FDStandingOrder so=null;
				try {
					so = FDStandingOrdersManager.getInstance().load(new PrimaryKey(reqData.getStandingOrderId()));
					 if(null!=so){
						 if(!so.getStandingOrderCart().getOrderLines().isEmpty()){
							 so.getStandingOrderCart().refreshAll(true);
						 }
						 if(so.getLastErrorCode()!=null && StandingOrderHelper.getTotalAmountForSoSettings(so)>=FDStoreProperties.getStandingOrderHardLimit()){    
							 StandingOrderHelper.clearSO3ErrorDetails(so, new String[]{"MINORDER","TIMESLOT_MINORDER"}) ;
							 FDActionInfo info = AccountActivityUtil.getActionInfo(request.getSession());
							 FDStandingOrdersManager.getInstance().manageStandingOrder(info, so.getStandingOrderCart(), so, null) ;
						 }
						orderResponseData=StandingOrderHelper.populateResponseData(so,true);
	
					 }else{
						 orderResponseData.setMessage("Standing Order has been deleted. " +
						 		" Please choose another Standing Order") ;
					 }
				} catch (FDResourceException e) {
					// TODO Auto-generated catch block
					 orderResponseData.setMessage("ERROR WHILE RETRIEVING THE STANDING ORDER DATA") ;
					 orderResponseData.setSuccess(false);
					LOG.error(" ERROR WHILE RETRIEVING THE STANDING ORDER DATA"+ reqData.getStandingOrderId());
				} catch (FDInvalidConfigurationException e) {
					// TODO Auto-generated catch block
					 orderResponseData.setMessage("ERROR WHILE RETRIEVING THE STANDING ORDER DATA") ;
					 orderResponseData.setSuccess(false);
					LOG.error(" ERROR WHILE RETRIEVING THE STANDING ORDER DATA"+ reqData.getStandingOrderId());
				}
				// Save user
				saveUser(user);

			 }
		}else{
			// User level not sufficient.
			 orderResponseData.setMessage("User Session is expired please try login to add the product to Standing order") ;
			 orderResponseData.setError("Session Expired");
			 orderResponseData.setSuccess(false);
			 //returnHttpError(401, "User auth level not sufficient!"); // 401 Unauthorized
		}
		writeResponseData(response,orderResponseData);


	}

	protected void storeDefualtStandingOrder(AddToListRequestData reqData,
			FDUserI user) throws HttpErrorResponse{
		try {
			FDStandingOrdersManager.getInstance().updateDefaultStandingOrder(reqData.getListId(),user.getIdentity()); 
		} catch (FDResourceException e) {
			returnHttpError(500, "System error (FDResourceException) - couldn't persist shopping list", e); // 500
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
			returnHttpError(500, "Failed to get shopping list : " + listId); // 500 Internal Server Error
		}

		// prepare response items holder
		List<AddToListResponseItem> responseItems = new ArrayList<AddToListResponseItem>();
		// Add items to list
		addItemsToList(list, items, recipeId, recipeName, responseItems, listCreated);

		// Save list
		try {
			FDListManager.storeCustomerList(list);
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
				// FIXME : this is quite bizarre for simply getting a default(?)
				// sales-unit ...
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
				new ContentKey(ContentType.get("Recipe"), recipeId));
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

	private static List<ShoppingListInfo> collectListInfos(FDUserI user) {
		String defaultListId = user.getDefaultListId();
		List<FDCustomerListInfo> lists = user.getCustomerCreatedListInfos();
		if (lists == null) {
			return new ArrayList<ShoppingListInfo>();
		}

		List<ShoppingListInfo> listInfos = new ArrayList<ShoppingListInfo>(lists.size());
		for (FDCustomerListInfo list : lists) {
			ShoppingListInfo info = new ShoppingListInfo();
			info.setListId(list.getId());
			info.setName(list.getName());
			info.setCount(list.getCount());
			info.setRecipeId(list.getRecipeId());
			info.setDefault(list.getId().equals(defaultListId));
			listInfos.add(info);
		}

		Collections.sort(listInfos, new Comparator<ShoppingListInfo>() {
			@Override
			public int compare(ShoppingListInfo o1, ShoppingListInfo o2) {
				// Default list is always the first
				if (o1.isDefault())
					return -1;
				if (o2.isDefault())
					return 1;

				// Sort the rest alphabetically
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});

		return listInfos;
	}

	private static String createList(FDUserI user, String name) throws FDResourceException, FDCustomerListExistsException {
		String newId = FDListManager.createCustomerCreatedList(user, name);
		user.invalidateCache();
		return newId;
	}

	@SuppressWarnings("unused")
	private static void removeLineItem(FDUserI user, String lineId) throws FDResourceException {
		FDListManager.removeCustomerListItem(user, new PrimaryKey(lineId));
		user.invalidateCache();
	}


}

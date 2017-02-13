package com.freshdirect.webapp.ajax.shoppinglist;

import java.util.ArrayList;
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
import com.freshdirect.fdstore.cache.EhCacheUtil;
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.RecipeVariant;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerListExistsException;
import com.freshdirect.fdstore.lists.FDCustomerListInfo;
import com.freshdirect.fdstore.lists.FDCustomerProductListLineItem;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.cart.CartOperations;
import com.freshdirect.webapp.ajax.cart.data.AddToCartItem;

public class ShoppingListServlet extends BaseJsonServlet {

	private static final long	serialVersionUID	= 4376343201345823580L;
	
	private static final Logger LOG = LoggerFactory.getInstance( ShoppingListServlet.class );
	
	
	@Override
	protected boolean synchronizeOnUser() {
		return false; //no need to synchronize
	}
	
	@Override
	protected int getRequiredUserLevel() {
		return FDUserI.GUEST;
	}	

	@Override
	protected void doGet( HttpServletRequest request, HttpServletResponse response, FDUserI user ) throws HttpErrorResponse {        
        getInternal( request, response, user, false, null );
	}
	
	private static void getInternal( HttpServletRequest request, HttpServletResponse response, FDUserI user, boolean shoppingListPageRefreshNeeded, List<AddToListResponseItem> responseItems ) throws HttpErrorResponse {
		try {			
			// Collect data
			List<ShoppingListInfo> listInfos = collectListInfos( user );
			
			// Create response data object
			ShoppingListResponseData responseData = new ShoppingListResponseData();
			responseData.setListInfos( listInfos );
			responseData.setResponseItems(responseItems);
			responseData.setShoppingListPageRefreshNeeded(shoppingListPageRefreshNeeded);
			if( user.getLevel() == FDUserI.GUEST ) {
				responseData.setListInfos( null );
			};
			writeResponseData( response, responseData );
			
		} catch ( Exception e ) {
			returnHttpError( 500, "Error while getting shopping list infos for user " + user.getUserId(), e );	// 500 Internal Server Error
		}
	}
	
	@Override
	protected void doPut( HttpServletRequest request, HttpServletResponse response, FDUserI user ) throws HttpErrorResponse {
		
		if ( user.getLevel() < FDUserI.SIGNED_IN ) {
        	// User did not login. Login required for delete/modify. 
        	returnHttpError( 401, "User not logged in!" );	// 401 Unauthorized
		}
                
		// Parse request data
		ShoppingListRequestData reqData = parseRequestData( request, ShoppingListRequestData.class );
		 
		List<ShoppingListChange> newListInfos = reqData.getListInfos(); 
		if ( newListInfos == null ) {
			returnHttpError( 400, "Bad JSON - listInfos is missing" );	// 400 Bad Request
		}
		
		List<ShoppingListInfo> oldListInfos = collectListInfos( user );
		Set<String> oldListIds = new HashSet<String>();
		for ( ShoppingListInfo inf : oldListInfos ) {
			oldListIds.add( inf.getListId() );
		}

		for ( ShoppingListChange newInfo : newListInfos ) {
			String listId = newInfo.getListId();
			// ====== VALIDATION ======
			if ( listId == null ) {
				// Invalid, no ID
				LOG.warn( "Missing ID for shopping list change request, skipping." );
				continue;
			}
			
			if ( !oldListIds.contains( listId ) ) {
				// It's not your list!
				LOG.warn( "Invalid ID for shopping list change request : "+listId+" - list belongs to another customer or does not exist, skipping." );
				continue;
			}
			
			// ====== DELETE ======
			if ( newInfo.isDelete() ) {
				deleteList(user, listId);
				continue;
			}

			
			// ====== SET DEFAULT LIST ======
			if ( newInfo.isDefault() ) {
				user.setDefaultListId( newInfo.getListId() );
			}

			
			// ====== RENAME ======
			String newName = newInfo.getName(); 
			if ( newName != null && !newName.trim().equals( "" ) ) {
				renameList(newInfo, listId, newName);
				continue;
			}
			
			if (newInfo.isEmpty()) {
				deleteListItems(user, listId);
				continue;
			}
		}
		
		//invalidate cache entry TODO: partial invalidation?
		EhCacheUtil.removeFromCache(EhCacheUtil.QS_SHOP_FROM_LISTS_CACHE_NAME, user.getIdentity().getErpCustomerPK());
		
		// Save user
		saveUser( user );
		
		// Query and send back the new state
        getInternal( request, response, user, true, null );
        
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
		LOG.info( "renaming list " + newInfo.getListId() + " to " + newInfo.getName() );
		try {
			FDListManager.renameShoppingList( listId, newName );
			LOG.info( "Renamed list " + listId + " to " + newName );
		} catch (FDResourceException e) {
			LOG.error( "Failed to rename list: "+listId, e );
		}
	}

	private void deleteList(FDUserI user, String listId) {
		LOG.info( "Deleting list " + listId );
		try {
			FDListManager.deleteShoppingList( listId );
			if(listId.equals(user.getDefaultListId())){
				user.setDefaultListId(null);
			}
			LOG.info( "Deleted list " + listId );
		} catch (FDResourceException e) {
			LOG.error( "Failed to delete list: "+listId, e );
		}
	}
	
	
	@Override
	protected void doPost( HttpServletRequest request, HttpServletResponse response, FDUserI user ) throws HttpErrorResponse {
		
		// POST requires at least RECOGNIZED
		if ( user.getLevel() < FDUserI.RECOGNIZED ) {
        	// User level not sufficient. 
        	returnHttpError( 401, "User auth level not sufficient!" ); // 401 Unauthorized
		}
        
		// Parse request data
		AddToListRequestData reqData = parseRequestData( request, AddToListRequestData.class );
		
		String listId = reqData.getListId();		
		String recipeId = reqData.getRecipeId();
		String newListName = reqData.getListName();
		String recipeName = null;
		
		if ( listId == null ) {
			// Creating new list - just validate at this point			
			if ( newListName == null || newListName.trim().equals( "" ) ) {
				returnHttpError( 400, "No list name provided for new list" );	// 400 Bad Request
			}
			
		} else if ( recipeId != null ) {
			returnHttpError( 400, "Creating list from recipe only works when creating new list!" );	// 400 Bad Request
		}
		
		List<AddToCartItem> items = null;
		
		if ( recipeId != null ) {
			// Collect items from recipe ingredients
			items = new ArrayList<AddToCartItem>();
			recipeName = collectRecipeIngredients(items, recipeId, reqData.getRecipeVariantId() );
		} else {
			// Use item list from request
			items = reqData.getItems();
		}
		
		if ( items == null ) {
			returnHttpError( 400, "Bad JSON - items is missing" );	// 400 Bad Request
		}
		
		boolean listCreated = false;
		if ( listId == null ) {
			// Creating new list - now create the new list for real
			try {				
				
				listId = createList( user, newListName );
				listCreated = true;
				
				//set the new list as default (if there is no default already)
				if(user.getDefaultListId()==null || user.getDefaultListId().isEmpty()){
					user.setDefaultListId( listId );					
				}
				
			} catch ( FDResourceException e ) {
				returnHttpError( 500, "System error (FDResourceException)", e );	// 500 Internal Server Error
			} catch ( FDCustomerListExistsException e ) {
				returnHttpError( 400, "List with same name already exists!", e );	// 400 Bad Request
			}
			
		}
		
		// Get the list
		FDCustomerList list = null;
		try {
			list = FDListManager.getCustomerListById( user.getIdentity(), EnumCustomerListType.CC_LIST, listId );
		} catch ( FDResourceException e ) {
			returnHttpError( 500, "System error (FDResourceException)", e );	// 500 Internal Server Error
		}
		
		if ( list == null ) {
			returnHttpError( 500, "Failed to get shopping list : " + listId );	// 500 Internal Server Error
		}
		
		//prepare response items holder
		List<AddToListResponseItem> responseItems = new ArrayList<AddToListResponseItem>();
		// Add items to list
		addItemsToList( list, items, recipeId, recipeName, responseItems, listCreated );

		
		// Save list
		try {
			FDListManager.storeCustomerList(list);
		} catch ( FDResourceException e ) {
			returnHttpError( 500, "System error (FDResourceException) - couldn't persist shopping list", e );	// 500 Internal Server Error
		}			
		
		//invalidate cache entry TODO: partial invalidation?
		EhCacheUtil.removeFromCache(EhCacheUtil.QS_SHOP_FROM_LISTS_CACHE_NAME, user.getIdentity().getErpCustomerPK());
	
		// Save user
		saveUser( user );
					
		getInternal(request, response, user, true, responseItems);
		
	}


	private static void addItemsToList( FDCustomerList list, List<AddToCartItem> items, String recipeId, String recipeName, List<AddToListResponseItem> responseItems, boolean listCreated ) {
		
		for ( AddToCartItem item : items ) {
			
			AddToListResponseItem responseItem = new AddToListResponseItem();
			try {
				FDCustomerProductListLineItem listItem = createListLineItem(item, recipeId, false);
				list.addLineItem( listItem );
				
				//create response item
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
		list.setRecipeId( recipeId );
		list.setRecipeName(recipeName);
		list.seteStoreType(ContentFactory.getInstance().getStore().getContentName());
	}
	
	public static FDCustomerProductListLineItem createListLineItem(AddToCartItem item, String recipeId, boolean editItem) throws FDResourceException{
		// extra fail-safe sales-unit/quantity handling
		double quantity = CartOperations.extractQuantity( item );
		
		if ((item.getSalesUnit() == null || item.getSalesUnit().trim().isEmpty()) &&  quantity == 0.0) {
			// has no sales-unit, nor quantity set => skip item
			LOG.warn("Warning: skipped item " + item.getSkuCode() + ", because of missing quantity and sales-unit.");
			throw new FDResourceException();
		}
		if (item.getSalesUnit() != null && !item.getSalesUnit().trim().isEmpty() && quantity == 0.0) {
			// has a sales-unit, but no quantity has been set => set quantity to one
			item.setQuantity("1.0");
		}
		if ((item.getSalesUnit() == null || item.getSalesUnit().trim().isEmpty()) && quantity != 0.0) {
			// has no sales-unit set, only quantity => set sales-unit to default
			try {
				// FIXME : this is quite bizarre for simply getting a default(?) sales-unit ...
				item.setSalesUnit(FDCachedFactory.getProduct(FDCachedFactory.getProductInfo(item.getSkuCode())).getSalesUnits()[0].getName());
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
		if ( item.getSalesUnit() == null || item.getSalesUnit().trim().isEmpty() ) {
			LOG.warn( "Missing salesunit!" );
			throw new FDResourceException();
		}

		// create and add list item
		FDConfiguration config = new FDConfiguration(quantity, item.getSalesUnit(), item.getConfiguration());
		FDCustomerProductListLineItem listItem = new FDCustomerProductListLineItem(item.getSkuCode(), config, recipeId);
		
		if(editItem && item.getLineId()!=null){
			listItem.setId(item.getLineId());			
		}
		
		return listItem;
	}


	private static String collectRecipeIngredients(List<AddToCartItem> items, String recipeId, String recipeVariantId ) {
		//Collect recipe ingredients
		Recipe recipe = (Recipe)ContentFactory.getInstance().getContentNodeByKey( ContentKey.getContentKey( ContentType.get( "Recipe" ), recipeId ) );
		RecipeVariant recipeVariant = null;
		
		if( recipeVariantId != null ) {
			List<RecipeVariant> variants = recipe.getVariants();
			for ( RecipeVariant var : variants ) {
				if ( recipeVariantId.equals( var.getContentName() ) ) {
					recipeVariant = var;
					break;
				}
			}
		}		
		if ( recipeVariant == null ) {
			recipeVariant = recipe.getDefaultVariant();
		}
		
		List<ConfiguredProduct> ingredients = recipeVariant.getSections().get( 0 ).getIngredients();
		
		for ( ConfiguredProduct product : ingredients ) {
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
	
	private static List<ShoppingListInfo> collectListInfos( FDUserI user ) {
		String defaultListId = user.getDefaultListId();
		List<FDCustomerListInfo> lists = user.getCustomerCreatedListInfos();
		if ( lists == null ) {
			return new ArrayList<ShoppingListInfo>();
		}
		
		List<ShoppingListInfo> listInfos = new ArrayList<ShoppingListInfo>( lists.size() );
		for ( FDCustomerListInfo list : lists ) {
			if(list.geteStoreType()!=null&&list.geteStoreType().equalsIgnoreCase(ContentFactory.getInstance().getStore().getContentName())){
			ShoppingListInfo info = new ShoppingListInfo();
			info.setListId( list.getId() );
			info.setName( list.getName() );
			info.setCount( list.getCount() );
			info.setRecipeId( list.getRecipeId() );
			info.setDefault( list.getId().equals( defaultListId ) );
			listInfos.add( info );
			}
		}
		
		Collections.sort( listInfos, new Comparator<ShoppingListInfo>() {
			@Override
			public int compare( ShoppingListInfo o1, ShoppingListInfo o2 ) {
				// Default list is always the first
				if ( o1.isDefault() )
					return -1;				
				if ( o2.isDefault() )
					return 1;
				
				// Sort the rest alphabetically
				return o1.getName().compareToIgnoreCase( o2.getName() );
			}
		} );
		
		return listInfos;
	}
		
	private static String createList( FDUserI user, String name ) throws FDResourceException, FDCustomerListExistsException {
		String newId = FDListManager.createCustomerCreatedList( user, name );
		user.invalidateCache();
		return newId;
	}
	
	
	@SuppressWarnings( "unused" )
	private static void removeLineItem(FDUserI user, String lineId) throws FDResourceException {
		FDListManager.removeCustomerListItem(user, new PrimaryKey(lineId));
		user.invalidateCache();
	}

}
package com.freshdirect.webapp.ajax.quickshop;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.WineUtil;
import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentNodeI;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.cms.fdstore.FDContentTypes;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.ZonePriceInfoModel;
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ConfiguredProductGroup;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.FDFolder;
import com.freshdirect.fdstore.content.FilteringFlowResult;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.content.StarterList;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDOrderHistory;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.fdstore.customer.FDProductSelection;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerListItem;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.fdstore.pricing.ProductModelPricingAdapter;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.fdstore.pricing.SkuModelPricingAdapter;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.fdstore.ScoreProvider;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.product.ProductDetailPopulator;
import com.freshdirect.webapp.ajax.quickshop.data.EnumQuickShopTab;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItem;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItemWrapper;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class QuickShopHelper {

	private static final int TIME_LIMIT = -13;
	public final static Logger LOG = LoggerFactory.getInstance(QuickShopHelper.class);

	public static List<FilteringSortingItem<QuickShopLineItemWrapper>> getWrappedOrderHistory(FDUserI user, EnumQuickShopTab tab) throws FDResourceException {

		List<FilteringSortingItem<QuickShopLineItemWrapper>> result = new ArrayList<FilteringSortingItem<QuickShopLineItemWrapper>>();
		
		List<FDProductSelectionI> items = FDListManager.getQsSpecificEveryItemEverOrderedList(user.getIdentity());
		
		if(items==null || items.isEmpty()){
			return result;
		}
		
		List<Date> orderDates = new ArrayList<Date>();
		
		for(FDProductSelectionI item : items){
			
			FilteringSortingItem<QuickShopLineItemWrapper> fsi = createItemCore(item, null, null, user, tab);
			if(fsi==null){
				continue;
			}

			fsi.getNode().setDeliveryDate(item.getDeliveryStartDate());
			fsi.getNode().setOrderId(item.getOrderId());
			fsi.getNode().setOrderStatus(item.getSaleStatus().getDisplayName());

			result.add(fsi);
			
			if(!orderDates.contains(item.getDeliveryStartDate())){
				orderDates.add(item.getDeliveryStartDate());				
			}
		}
		
		Collections.sort(orderDates);
		Collections.reverse(orderDates);
		setLastOrderFlag(result, orderDates.get(0));

		return result;

	}
	
	private static void setLastOrderFlag(List<FilteringSortingItem<QuickShopLineItemWrapper>> items, Date last){
		for(FilteringSortingItem<QuickShopLineItemWrapper> item : items){
			if(item.getNode().getDeliveryDate().equals(last)){
				item.getNode().setInLastOrder(true);
			}
		}
	}
	
	public static List<FilteringSortingItem<QuickShopLineItemWrapper>> getWrappedCustomerCreatedLists(FDUserI user, EnumQuickShopTab tab) throws FDResourceException {

		List<FilteringSortingItem<QuickShopLineItemWrapper>> result = new ArrayList<FilteringSortingItem<QuickShopLineItemWrapper>>();

		for(FDCustomerCreatedList list: FDListManager.getCustomerCreatedLists(user)){
			List<FDCustomerListItem> cclItems = list.getLineItems();
			if(cclItems!=null){
				for(FDProductSelectionI productSelection: OrderLineUtil.getValidProductSelectionsFromCCLItems( cclItems )){
					
					FilteringSortingItem<QuickShopLineItemWrapper> fsi = createItemCore(productSelection, list, null, user, tab);
					if(fsi==null){
						continue;
					}

					result.add(fsi);
					
				}
			}
		}
		
		return result;
		
	}
	
	public static FilteringSortingItem<QuickShopLineItemWrapper> createItemCore(FDProductSelectionI productSelection, FDCustomerList list, StarterList starterList, FDUserI user, EnumQuickShopTab tab) throws FDResourceException{
		
		String idSuffix = null;
		String compositeId = null;
		if(tab!=null){
			switch(tab){
				case PAST_ORDERS:{
					idSuffix = productSelection.getOrderLineId();					
					compositeId = tab + "_" + idSuffix;
					break;
				}
				case CUSTOMER_LISTS:{
					idSuffix = productSelection.getCustomerListLineId();
					compositeId = tab + "_" + idSuffix;
					break;
				}
				case FD_LISTS:{
					idSuffix = starterList.getContentKey().getId();
					compositeId = tab + "_" + productSelection.getSkuCode() + "_" + idSuffix;
				}
			}			
		}
		
		if(compositeId==null){
			compositeId = tab + "_" + productSelection.getSkuCode() + "_" + idSuffix;			
		}
		
		// Create quickshop line item (unpopulated)
		QuickShopLineItem item = new QuickShopLineItem();
		item.setItemId(compositeId);
		
		// Refresh configuration TODO: invalid config scenario ?! 
		try {
			productSelection.refreshConfiguration();
		} catch ( Exception e ) {
			LOG.error("Refresh configuration failed with exception: " + e);
			return null;
		}
		
		// Get CMS product model
		ProductModel productModel = productSelection.lookupProduct();
		if (productModel == null) {
			return null;
		}
		
		// is hidden in quickshop?
		if(productModel.getPrimaryHome().getDepartment().isHidddenInQuickshop()){
			return null;
		}
		
		if ( productModel instanceof ProductModelPricingAdapter ) {
			// strip down, get the naked product model
			productModel = ((ProductModelPricingAdapter)productModel).getRealProduct();
			
			// create valid pricing adapter with valid pricing context
			productModel = ProductPricingFactory.getInstance().getPricingAdapter( productModel, user.getPricingContext() );
		}
		
		// Selected sku code
		String skuCode = productSelection.getSkuCode();

		// Get CMS sku model
		SkuModel skuModel = productModel.getSku( skuCode );
		if (skuModel == null) {
			return null;
		}
		if ( skuModel instanceof SkuModelPricingAdapter ) {
			// strip down, get the naked sku model, we don't need an adapter for the sku ???
			skuModel = ((SkuModelPricingAdapter)skuModel).getRealSku();
		}
		
		// Get ERPS FDProduct - latest version 
		FDProduct latestFdProduct;
		try {
			latestFdProduct = skuModel.getProduct();
		} catch (FDSkuNotFoundException e1) {
			LOG.warn("Sku not found: " + skuCode);
			return null;
		}
		if ( latestFdProduct == null ) {
			return null;
		}

		// Get ERPS FDProductInfo - latest version 
		FDProductInfo latestFdProductInfo;
		try {
			latestFdProductInfo = skuModel.getProductInfo();
		} catch (FDSkuNotFoundException e) {
			LOG.error("Cannot collect subtotal calculation data with "+productModel+", e:"+e.getMessage(), e);
			return null;
		}
		if ( latestFdProductInfo == null ) {
			return null;
		}
				
		// create a valid price calculator 
		PriceCalculator priceCalculator = productModel.getPriceCalculator( skuModel );		// TODO verify pricing context
		if ( priceCalculator == null ) {
			return null;
		}
		
		// Wrap the resulting item - adds filtering info
		QuickShopLineItemWrapper wrapper = new QuickShopLineItemWrapper(item, (ProductModelPricingAdapter) productModel);
		if (list != null) {
			wrapper.setCclId(list.getId());
			item.setListId(list.getId());
			wrapper.setListName(list.getName());
			wrapper.setRecipeName(list.getRecipeName());
			wrapper.setRecipeId(list.getRecipeId());

			// check if recipe is still alive
			if (list.getRecipeId() != null) {
				wrapper.setRecipeAlive(true);
				Recipe recipe = (Recipe) ContentFactory.getInstance().getContentNodeByKey(
						new ContentKey(ContentType.get("Recipe"), list.getRecipeId()));
				if (recipe.isOrphan() || !recipe.isAvailable()) {
					wrapper.setRecipeAlive(false);
				}
			}
		}

		// === POPULATE ===
		
		ProductDetailPopulator.populateBasicProductData( item, user, productModel );
		ProductDetailPopulator.populateProductData( item, user, productModel, skuModel, latestFdProduct, priceCalculator, productSelection, true );
		ProductDetailPopulator.populatePricing( item, latestFdProduct, latestFdProductInfo, priceCalculator );
		
		populateOrderLineData( item, productSelection );
		
		try {
			ProductDetailPopulator.populateSkuData( item, user, productModel, skuModel, latestFdProduct );
		} catch (FDSkuNotFoundException e) {
			LOG.error( "Failed to populate sku data", e );
		} catch ( HttpErrorResponse e ) {
			LOG.error( "Failed to populate sku data", e );
		}

		ProductDetailPopulator.postProcessPopulate( user, item, item.getSkuCode() );
		
		// User specific data - scores
		if (user != null) {
			FDIdentity identity = user.getIdentity();
			if (identity != null){
				Float score = ScoreProvider.getInstance().getUserProductScore(identity.getErpCustomerPK(), productModel.getContentKey());
				if(score!=null){
					wrapper.setUserScore(score);													
				}
			}
		}

		// Wrap it in a FilteringSortingItem for the FilteringFlow
		FilteringSortingItem<QuickShopLineItemWrapper> fsi = new FilteringSortingItem<QuickShopLineItemWrapper>(wrapper);
		
		return fsi;
	}
	
	public static QuickShopLineItem createItemFromProduct(ProductModel productModel, SkuModel skuModel, FDUserI user, boolean useFavBurst) throws FDResourceException {
		
		QuickShopLineItem item = new QuickShopLineItem();
		
		if ( !(productModel instanceof ProductModelPricingAdapter) ) {
			// wrap it into a pricing adapter if naked
			productModel = ProductPricingFactory.getInstance().getPricingAdapter( productModel, user.getPricingContext() );
		}
		
		if ( skuModel == null ) {
			skuModel = productModel.getDefaultSku();
		}
		String skuCode = skuModel.getSkuCode();
		
		try {
			FDProductInfo productInfo = skuModel.getProductInfo();
			FDProduct fdProduct = skuModel.getProduct();
		
			PriceCalculator priceCalculator = productModel.getPriceCalculator();
			
			ProductDetailPopulator.populateBasicProductData( item, user, productModel );
			ProductDetailPopulator.populateProductData( item, user, productModel, skuModel, fdProduct, priceCalculator, null, useFavBurst );
			ProductDetailPopulator.populatePricing( item, fdProduct, productInfo, priceCalculator );
			
			try {
				ProductDetailPopulator.populateSkuData( item, user, productModel, skuModel, fdProduct );
			} catch (FDSkuNotFoundException e) {
				LOG.error( "Failed to populate sku data", e );
			} catch ( HttpErrorResponse e ) {
				LOG.error( "Failed to populate sku data", e );
			}
			
			ProductDetailPopulator.postProcessPopulate( user, item, item.getSkuCode() );
			
			return item;
			
		} catch (FDSkuNotFoundException e) {
			LOG.warn( "Sku not found: "+skuCode, e );
			return null;
		} /*catch ( HttpErrorResponse e ) {
			LOG.warn( "Error creating item: "+skuCode, e );
			return null;
		}*/
	}

	
	private static void populateProductData( QuickShopLineItem item, FDUserI user, ProductModel productModel, SkuModel sku, FDProduct fdProduct, PriceCalculator priceCalculator, FDProductSelectionI orderLine, boolean useFavBurst ) {
		
		if ( sku.isUnavailable() ) {
			item.setAvailable( false );
			// if unavailable add product replacements
			populateReplacements( item, productModel, user );
		} else {
			item.setAvailable( true );
		}
		item.setCatId(productModel.getPrimaryHome().getContentKey().getId());
		item.setProductImage( productModel.getProdImage().getPathWithPublishId() );
		item.setProductName( productModel.getFullName() );
		item.setProductId( productModel.getContentKey().getId() );
		item.setSkuCode( sku.getSkuCode() );
		item.setCustomizePopup( !productModel.isAutoconfigurable() );
		item.setAlcoholic( isAlcoholic( productModel, fdProduct ) );
		item.setUsq( isUsq( productModel, fdProduct ) );		
		item.setProductPageUrl( FDURLUtil.getProductURI( productModel, (String)null ) );
					
		populateRatings( item, user, productModel, sku.getSkuCode() );
		populateBursts( item, user, productModel, priceCalculator, useFavBurst );
		populateQuantityAndSalesUnits( item, user, productModel, fdProduct, orderLine );
		
		// make a variation map
		HashMap<String, String> optionMap = new HashMap<String, String>();
		FDVariation[] variations = fdProduct.getVariations();
		if(variations != null) {
			boolean autoConfiguration = false;
			for (FDVariation variation : variations) {
				FDVariationOption[] options = variation.getVariationOptions();
				if(options != null && options.length == 1) {
					autoConfiguration = true;
				} else {
					autoConfiguration = false;
				}
			}
			
			if(autoConfiguration) {
				for (FDVariation variation : variations) {
					FDVariationOption[] options = variation.getVariationOptions();
					if(options != null) {
						for (int n = 0;n < options.length; n++) {
							FDVariationOption option = options[n % options.length];						
							optionMap.put(variation.getName(), option.getName());
						}
					}
				}
				item.setConfiguration(optionMap);
			}
		}
		
		//get frequency and recency values from smartstore
		String[] variables = new String[2];
		variables[0] = ScoreProvider.USER_FREQUENCY;
		variables[1] = ScoreProvider.RECENCY_DISCRETIZED;
		double[] scores = ScoreProvider.getInstance().getVariables(user.getIdentity().getErpCustomerPK(), productModel.getPricingContext(), productModel.getContentKey(), variables);
		item.setFrequency(scores[0]);
		item.setRecency(scores[1]);
		
	}

	public static boolean isUsq( ProductModel productModel, FDProduct fdProduct ) {
		return fdProduct.isWine() && ContentNodeModelUtil.hasWineDepartment( productModel.getContentKey() );
	}

	public static boolean isAlcoholic( ProductModel productModel, FDProduct fdProduct ) {
		return fdProduct.isAlcohol() || productModel.getPrimaryHome().isHavingBeer();
	}
	

	/**
	 * Populates quickshop specific replacement items
	 * 
	 * @param item
	 * @param originalProduct
	 * @param user
	 */
	public static void populateReplacements( QuickShopLineItem item, ProductModel originalProduct, FDUserI user ) {
		// look up a recommended replacement
		List<ContentNodeModel> alternatives = originalProduct.getRecommendedAlternatives();
		
		for ( ContentNodeModel node : alternatives ) {
			ProductModel altProd = null;
			SkuModel altSku = null;
			
			// this attribute can have multiple non-compatible types, sort this out ... 
			if ( node instanceof ProductModel ) {
				altProd = (ProductModel)node;
				altSku = altProd.getDefaultSku();
			} else if ( node instanceof SkuModel ) {
				altSku = (SkuModel)node;
				altProd = altSku.getProductModel();
			} else {
				// unknown type, skip
				continue;
			}
			if ( altProd == null || altSku == null || altSku.isUnavailable() ) {
				// unavailable, skip
				continue;
			}
			
			// now we have an available product alternative to recommend				
			// create the quickshop line item, and add it to the original item
			try {
				QuickShopLineItem altItem = createItemFromProduct( altProd, altSku, user, true );
				altItem.setItemId( "REPLACEMENT_" + item.getItemId() );
				item.setReplacement( altItem );
				
				//populate props from the original lineItem
				altItem.setListId(item.getListId());
				altItem.setOriginalLineId(item.getOriginalLineId());
			} catch (FDResourceException e) {
				LOG.warn( "Failed to add replacements: ", e );
				continue;
			}			
			// we need only the first one available, so no more iterations 
			break;
		}
	}


	private static void populateRatings( QuickShopLineItem item, FDProductSelectionI orderLine ) {
		
		int wineRating = 0;
		int expertRating = 0;
		int customerRating = 0;
		int customerRatingReviewCount = 0;
		String sustainabilityRating = null;

		// Wine Rating		
		final String deptName = product.getDepartment() != null ? product.getDepartment().getContentName() : "";
		if ( WineUtil.getWineAssociateId().toLowerCase().equalsIgnoreCase( deptName ) ) {
			try {
				EnumOrderLineRating r = product.getProductRatingEnum( skuCode );
				if ( r != null ) {
					wineRating = r.getValue();
				}
			} catch ( FDResourceException ignore ) {
			}
		}

		
		// Expert Rating
		try {
			EnumOrderLineRating r = product.getProductRatingEnum( skuCode );
			if ( r != null ) {
				expertRating = r.getValue();
			}
		} catch ( FDResourceException ignore ) {
		}

		
		// Customer Rating
		CustomerRatingsDTO customerRatingsDTO = CustomerRatingsContext.getInstance().getCustomerRatingByProductId( product.getContentKey().getId() );
		if ( customerRatingsDTO != null ) {
			BigDecimal averageRating = customerRatingsDTO.getAverageOverallRating();
			customerRating = ((int) Math.ceil(averageRating.doubleValue())) * 2;
			customerRatingReviewCount = customerRatingsDTO.getTotalReviewCount();
		}
		
		
		// Sustainability Rating
		if ( user.isProduceRatingEnabled() ) {
			try {
				sustainabilityRating = product.getSustainabilityRating( skuCode );
			} catch ( FDResourceException ignore ) {
			}
		}

		
		// Now set only(!) the appropriate values on the item
		
		// 1. always(!) add sustainability, regardless of others
		if ( sustainabilityRating != null && sustainabilityRating.trim().length() > 0 ) {
			item.setSustainabilityRating( sustainabilityRating );
		}
		
		// 2. wine rating is the strongest of all
		if ( wineRating > 0 ) {
			item.setWineRating( wineRating );
		} else 
		
		// 3. expert rating comes next
		if ( expertRating > 0 ) {
			item.setExpertRating( expertRating );
		} else 
		
		// 4. customer rating is the last resort
		if ( customerRating > 0 ) {
			item.setCustomerRating( customerRating );
			item.setCustomerRatingReviewCount(customerRatingReviewCount);
		}
	}
	
	private static void populateBursts( QuickShopLineItem item, FDUserI user, ProductModel product, PriceCalculator priceCalculator, boolean useFavBurst ) {

		boolean showBurstImage = true;
		ZonePriceInfoModel model;
		
		try {
			model = priceCalculator.getZonePriceInfoModel();
			showBurstImage = model.isShowBurstImage();
		} catch ( FDResourceException ignore ) {
		} catch ( FDSkuNotFoundException ignore ) {
		}
		
		int deal = showBurstImage ? priceCalculator.getHighestDealPercentage() : 0;
		
		boolean isNew = product.isNew();
		boolean isYourFave = DYFUtil.isFavorite( product, user );
		boolean isBackInStock = product.isBackInStock();

		item.setDeal( deal );
		// determine what to display
		if ( useFavBurst && isYourFave ) {
			item.setBadge( "fav" );
		} else if ( isNew ) {
			item.setBadge( "new" );
		} else if ( isBackInStock ) {
			item.setBadge( "back" );
		}
	}
	

	private static void populateQuantityAndSalesUnits( QuickShopLineItem item, FDUserI user, ProductModel productModel, FDProduct fdProduct, FDProductSelectionI orderLine ) {
		
		boolean soldBySalesUnits = productModel.isSoldBySalesUnits(); 
		item.setSoldBySalesUnit( soldBySalesUnits );
		
			// Sales units
			List<SalesUnit> sus = new ArrayList<SalesUnit>();					
			String selectedSu = null;
			if ( orderLine != null ) {
				selectedSu = orderLine.getSalesUnit(); 
			} else {
				FDSalesUnit fdsu = fdProduct.getDefaultSalesUnit();
				if ( fdsu != null ) {
					selectedSu = fdsu.getName();
				}
			}
			
			for ( FDSalesUnit fdsu : fdProduct.getSalesUnits() ) {
				SalesUnit sue = new SalesUnit();
				String id = fdsu.getName();
				sue.setId( id );
				sue.setName( fdsu.getDescription() );
				sue.setSelected( id.equals( selectedSu ) );
				sus.add( sue );
			}					
			item.setSalesUnit(sus);
			// Numeric quantity
			Quantity quantity = new Quantity();
			quantity.setqMin( productModel.getQuantityMinimum() );
			quantity.setqMax( user.getQuantityMaximum( productModel ) );
			quantity.setqInc( productModel.getQuantityIncrement() );
			quantity.setQuantity( orderLine != null ? orderLine.getQuantity() : quantity.getqMin() );	
			item.setQuantity( quantity );
				
	}

	
	private static void populateOrderLineData( QuickShopLineItem item, FDProductSelectionI orderLine ) throws Exception {
		
		// Orderline data
		
		item.setConfigInvalid( orderLine.isInvalidConfig() );

		if ( orderLine.getConfiguration().getOptions() != null && !orderLine.getConfiguration().getOptions().isEmpty() ) {
			item.setConfiguration( orderLine.getConfiguration().getOptions() );
		}

		item.setConfigDescr( orderLine.getConfigurationDesc() );

		item.setDescription( orderLine.getDescription() );

		item.setDepartmentDesc( orderLine.getDepartmentDesc() );

		if(orderLine.getCustomerListLineId()!=null){
			item.setOriginalLineId( orderLine.getCustomerListLineId() );			
		}else{
			item.setOriginalLineId( orderLine.getOrderLineId() );
		}
		
		item.setConfiguredPrice(orderLine.getConfiguredPrice());
		
	}

	
	public static void postProcessPopulate( FDUserI user, FilteringFlowResult<QuickShopLineItemWrapper> result, HttpSession session ) {		
		
		for ( FilteringSortingItem<QuickShopLineItemWrapper> fsItem : result.getItems() ) {
			QuickShopLineItem qsItem = fsItem.getNode().getItem();
			
			ProductDetailPopulator.postProcessPopulate( user, qsItem, qsItem.getSkuCode() );
	    	
	    	//check if we have temporary configuration for this item in session
			@SuppressWarnings( "unchecked" )
			Map<String, QuickShopLineItem> tempConfigs = (Map<String, QuickShopLineItem>) session.getAttribute(SessionName.SESSION_QS_CONFIG_REPLACEMENTS);
	    	
			if(tempConfigs!=null){
				if(tempConfigs.get(qsItem.getItemId())!=null){
					qsItem.setTempConfig(tempConfigs.get(qsItem.getItemId()));
				}
				
				//check if the replacement item has a temp config
				if(qsItem.getReplacement()!=null){
					QuickShopLineItem replacement = qsItem.getReplacement();
					if(tempConfigs.get(replacement.getItemId())!=null){
						replacement.setTempConfig(tempConfigs.get(replacement.getItemId()));
					}
				}				
			}
		}		
	}
	
	public static List<String> getRecentOrderHistoryInfoIds(FDUserI user) throws FDResourceException {

		List<FDOrderInfoI> orderHistoryInfo = getOrderHistoryInfo(user, true);
		
		Calendar timeLimit = Calendar.getInstance();
		timeLimit.add(Calendar.MONTH, TIME_LIMIT);
		List<String> ids = new ArrayList<String>();
		for (FDOrderInfoI info : orderHistoryInfo) {
			if (info.getRequestedDate().after(timeLimit.getTime())) {
				ids.add(info.getErpSalesId());
			}
		}

		return ids;
	}
	
	public static List<FDOrderInfoI> getOrderHistoryInfo(FDUserI user, boolean regularOnly) throws FDResourceException {

		FDOrderHistory history = (FDOrderHistory) user.getOrderHistory();
		List<FDOrderInfoI> orderHistoryInfo = new ArrayList<FDOrderInfoI>(history.getFDOrderInfos(EnumSaleType.REGULAR));
		
		if(!regularOnly){
			// Add gift cards orders too.
			orderHistoryInfo.addAll(history.getFDOrderInfos(EnumSaleType.GIFTCARD));
			
			// ADD Donation Orders too-for Robin Hood.
			orderHistoryInfo.addAll(history.getFDOrderInfos(EnumSaleType.DONATION));			
		}
		

		return orderHistoryInfo;
	}

	@SuppressWarnings( "unchecked" )
	public static List<String> getActiveReplacements( HttpSession session ) {
		return (List<String>)session.getAttribute( SessionName.SESSION_QS_REPLACEMENT );
	}

	
	public static List<StarterList> getStarterLists() {
		List<StarterList> result = new ArrayList<StarterList>();
		FDFolder folder = (FDFolder)ContentFactory.getInstance().getContentNode("starterLists");
		if (folder == null) {
			return result;
		}
		getStarterLists(folder.getContentKey(), result, true, true);
		return result;
	}

	public static List<FilteringSortingItem<QuickShopLineItemWrapper>> getWrappedProductFromStarterList(FDUserI user, List<StarterList> starterLists, List<String> activeReplacements) throws FDResourceException {
		List<FilteringSortingItem<QuickShopLineItemWrapper>> result = new ArrayList<FilteringSortingItem<QuickShopLineItemWrapper>>();

		if(starterLists==null){
			starterLists = getStarterLists();			
		}
		if(starterLists == null) {
			return result;
		}
		for (StarterList sl : starterLists) {
			StarterList starterList = (StarterList)ContentFactory.getInstance().getContentNode(sl.getContentKey().getId());
			if (starterList == null) {
				return result;
			}
			
			for ( ProductModel p : starterList.getListContents() ) {
				if ( p == null || !(p instanceof ConfiguredProduct) ) 
					continue;
				
			    ConfiguredProduct product = (ConfiguredProduct)p;
			    
			    if (product instanceof ConfiguredProductGroup) 
			    	product = (ConfiguredProduct)product.getProduct();
			    
			    if (product == null) 
			    	continue;
			    
			    try {
					FDProductInfo prodInfo = FDCachedFactory.getProductInfo(product.getSkuCode());
					FDProductSelection productSelection = new FDProductSelection(prodInfo, product, product.getConfiguration(), user.getPricingZoneId());
					productSelection.refreshConfiguration();
				    OrderLineUtil.describe(productSelection);
				    
				    FilteringSortingItem<QuickShopLineItemWrapper> fsi = QuickShopHelper.createItemCore(productSelection, null, starterList, user, EnumQuickShopTab.FD_LISTS);
					if ( fsi == null )
						continue;

					fsi.getNode().setStarterList(starterList);
					
					result.add(fsi);
					
				} catch (FDSkuNotFoundException e) {
					LOG.warn("SKU NOT FOUND: " + e);
					throw new FDResourceException(e);
				} catch (FDInvalidConfigurationException e) {
					LOG.warn("INVALID CONFIGURATION " + e);
					throw new FDResourceException(e);
				} catch (FDRuntimeException e){
					LOG.error("CANNOT REFRESH PRODUCT CONFIGURATION. e: ", e);
					continue;
				}
			}					
		}
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	private static void getStarterLists(ContentKey key, List<StarterList> starterLists, boolean active, boolean recursive) {
		ContentNodeI node = CmsManager.getInstance().getContentNode(key);
		List children = null;
		
		if(node.getAttribute("children") == null) {
			return;
		}
		
		children = (List) node.getAttribute("children").getValue();
		if (children == null) {
			return;
		}
		
		for (Object object : children) {
	    	ContentKey childKey = (ContentKey)object;
	    	if (childKey.getType().equals(FDContentTypes.STARTER_LIST)) {  
	    		StarterList starterList = (StarterList)ContentFactory.getInstance().getContentNodeByKey(childKey);
	    		if (!active || starterList.isAvailable()) {
	    			starterLists.add(starterList);
	    		}
	    	} else if (recursive) {
	    		if (childKey.getType().equals(FDContentTypes.FDFOLDER)) {
		    		getStarterLists(childKey,starterLists,active,true);	    			
	    		}
	    	}
	    }
	}

	public static boolean hasYourFavoritesRecommendation( FDUserI user ) {
		try {
			List<QuickShopLineItem> items = QuickShopYmalServlet.doRecommend( user, null, EnumSiteFeature.DYF, 10, null, null );
			return !items.isEmpty();
		} catch (FDResourceException e) {
			LOG.warn( "FDResourceException while checking favorites recommender", e );
			return false;
		}
	}
}

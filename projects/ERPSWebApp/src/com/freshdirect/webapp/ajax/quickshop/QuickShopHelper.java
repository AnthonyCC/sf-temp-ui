package com.freshdirect.webapp.ajax.quickshop;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.util.GroupScaleUtil;
import com.freshdirect.customer.EnumSaleType;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.ZonePriceInfoModel;
import com.freshdirect.fdstore.content.ConfiguredProduct;
import com.freshdirect.fdstore.content.ConfiguredProductGroup;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ContentNodeModelUtil;
import com.freshdirect.fdstore.content.FDFolder;
import com.freshdirect.fdstore.content.FilteringFlowResult;
import com.freshdirect.fdstore.content.FilteringSortingItem;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.Recipe;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.content.StarterList;
import com.freshdirect.fdstore.content.customerrating.CustomerRatingsContext;
import com.freshdirect.fdstore.content.customerrating.CustomerRatingsDTO;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDOrderHistory;
import com.freshdirect.fdstore.customer.FDOrderInfoI;
import com.freshdirect.fdstore.customer.FDProductSelection;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.fdstore.ecoupon.EnumCouponStatus;
import com.freshdirect.fdstore.ecoupon.FDCustomerCoupon;
import com.freshdirect.fdstore.lists.FDCustomerCreatedList;
import com.freshdirect.fdstore.lists.FDCustomerList;
import com.freshdirect.fdstore.lists.FDCustomerListItem;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.fdstore.pricing.ProductModelPricingAdapter;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.fdstore.pricing.SkuModelPricingAdapter;
import com.freshdirect.fdstore.util.DYFUtil;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.fdstore.ScoreProvider;
import com.freshdirect.webapp.ajax.cart.CartOperations;
import com.freshdirect.webapp.ajax.cart.data.CartData.Quantity;
import com.freshdirect.webapp.ajax.cart.data.CartData.SalesUnit;
import com.freshdirect.webapp.ajax.quickshop.data.EnumQuickShopTab;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItem;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItemWrapper;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.taglib.fdstore.display.ProductSavingTag;
import com.freshdirect.webapp.util.FDURLUtil;

public class QuickShopHelper {

	private static final int TIME_LIMIT = -13;
	private final static Logger LOG = LoggerFactory.getInstance(QuickShopHelper.class);

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
				
		populatePricing( item, latestFdProduct, latestFdProductInfo, priceCalculator );
		
		try {
			populateOrderLineData( item, productSelection );
		} catch (Exception e) {
			LOG.error( "Failed to populate orderline data", e );
		}
		
		populateProductData( item, user, productModel, skuModel, latestFdProduct, priceCalculator, productSelection, true );
		
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
		
		FDProductInfo productInfo;
		FDProduct fdProduct;
		try {
			productInfo = skuModel.getProductInfo();
			fdProduct = skuModel.getProduct();
		} catch (FDSkuNotFoundException e) {
			LOG.warn( "Sku not found: "+skuCode, e );
			return null;
		}
		
		PriceCalculator priceCalculator = productModel.getPriceCalculator();
		
		populateProductData( item, user, productModel, skuModel, fdProduct, priceCalculator, null, useFavBurst );
		populatePricing( item, fdProduct, productInfo, priceCalculator );
		
		postProcessPopulate( user, item );
		
		return item;		
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
	

	private static void populateReplacements( QuickShopLineItem item, ProductModel originalProduct, FDUserI user ) {
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


	private static void populateRatings( QuickShopLineItem item, FDUserI user, ProductModel product, String skuCode ) {
		
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

	
	private static void populatePricing( QuickShopLineItem item, FDProduct fdProduct, FDProductInfo productInfo, PriceCalculator priceCalculator ) throws FDResourceException {

		ZonePriceInfoModel zpi;
		try {
			zpi = priceCalculator.getZonePriceInfoModel();
			item.setPrice( zpi.getDefaultPrice() );
			item.setScaleUnit( productInfo.getDisplayableDefaultPriceUnit().toLowerCase() );
		} catch ( FDSkuNotFoundException e ) {
			// No sku (cannot happen) - don't even try the pricing
			return;
		} 
		
		// Tax and Deposit
		
		StringBuilder taxAndDepositBuilder = new StringBuilder();
		boolean hasTax = fdProduct.isTaxable();
		boolean hasDeposit = fdProduct.hasDeposit();
		if ( hasTax || hasDeposit ) {
			taxAndDepositBuilder.append( "plus " );
			if ( hasTax ) {
				taxAndDepositBuilder.append( "tax " );
			}
			if ( hasTax && hasDeposit ) {
				taxAndDepositBuilder.append( "& " );
			}
			if ( hasDeposit ) {
				taxAndDepositBuilder.append( "deposit" );
			}
		}
		item.setTaxAndDeposit( taxAndDepositBuilder.toString() );    	
		item.setAboutPriceText( priceCalculator.getAboutPriceFormatted( priceCalculator.getDealPercentage() ) );
		
		populateWithSubtotalInfo( item, fdProduct, productInfo, priceCalculator );
		populateSaving( item, productInfo, priceCalculator );
	}	
	

	private static void populateWithSubtotalInfo( QuickShopLineItem item, FDProduct fdProduct, FDProductInfo productInfo, PriceCalculator priceCalculator ) throws FDResourceException {

		String pricingZoneId = priceCalculator.getPricingContext().getZoneId();
		MaterialPrice[] availMatPrices = fdProduct.getPricing().getZonePrice( pricingZoneId ).getMaterialPrices();
		MaterialPrice[] matPrices = null;
		List<MaterialPrice> matPriceList = new ArrayList<MaterialPrice>();

		if ( productInfo.isGroupExists() ) {
			// Has a Group Scale associated with it. Check if there is GS price defined for current pricing zone.
			FDGroup group = productInfo.getGroup();
			MaterialPrice[] grpPrices = null;
			try {
				grpPrices = GroupScaleUtil.getGroupScalePrices( group, pricingZoneId );
			} catch ( FDResourceException fe ) {
				// Never mind. Show regular price for the material.
			}
			if ( grpPrices != null && grpPrices.length > 0 ) {
				// Group scale price applicable to this material. So modify material prices array to accomodate GS price.
				MaterialPrice regularPrice = availMatPrices[0];// Get the regular price/single unit price first.

				// Get the first group scale price and set the lower bound to be upper bound of regular price.
				MaterialPrice newRegularPrice = new MaterialPrice( regularPrice.getPrice(), regularPrice.getPricingUnit(), regularPrice.getScaleLowerBound(), grpPrices[0].getScaleLowerBound(), grpPrices[0].getScaleUnit(), regularPrice.getPromoPrice() );
				// Add the modified regular price.
				matPriceList.add( newRegularPrice );
				// Add the remaining group scale prices.
				for ( int i = 0; i < grpPrices.length; i++ ) {
					matPriceList.add( grpPrices[i] );
				}
				matPrices = matPriceList.toArray( new MaterialPrice[0] );
			}
		}

		if ( matPrices == null ) {
			// Set the default prices defined for the material.
			matPrices = availMatPrices;
		}

		if ( fdProduct.getPricing() != null ) {
			item.setAvailMatPrices( matPrices );
			item.setCvPrices( fdProduct.getPricing().getCharacteristicValuePrices() );
			item.setSuRatios( fdProduct.getPricing().getSalesUnitRatios() );
		}
		if ( productInfo.isGroupExists() && productInfo.getGroup() != null ) {
			item.setGrpPrices( GroupScaleUtil.getGroupScalePrices( productInfo.getGroup(), pricingZoneId ) );
		}
		ZonePriceInfoModel zone = productInfo.getZonePriceInfo( pricingZoneId );
		if ( zone != null && zone.isItemOnSale() ) {
			item.setWasPrice( zone.getSellingPrice() );
		}
	}

	
	private static void populateSaving( QuickShopLineItem item, FDProductInfo productInfo, PriceCalculator priceCalculator ) throws FDResourceException {
		
		FDGroup group = productInfo.getGroup();
		
		StringBuffer buf = new StringBuffer();
		MaterialPrice matPrice = null;
		
		if (group != null) {
			matPrice = GroupScaleUtil.getGroupScalePrice(group, priceCalculator.getPricingContext().getZoneId());
		}

		if (matPrice != null) {
			item.setMixNMatch(true);
		} else {
			String scaleString = priceCalculator.getTieredPrice(0, null);
			if (scaleString != null) {
				buf.append("Save! ");
				buf.append(scaleString);
			} else if (priceCalculator.isOnSale()) {
				buf.append("Save ");
				buf.append(priceCalculator.getDealPercentage());
				buf.append("%");
			} else {
				// no sales, do nothing
			}
		}
		
		item.setSavingString(buf.toString());
		
		if ( group != null ) {
			item.setDealInfo( ProductSavingTag.getGroupPrice(group, priceCalculator.getPricingContext().getZoneId()) );
		}

	}
		
	public static void postProcessPopulate( FDUserI user, QuickShopLineItem qsItem ) {
		
		// lookup product data
		ProductModel productModel;
		FDProductInfo productInfo;
		try {
			productModel = ContentFactory.getInstance().getProduct( qsItem.getSkuCode() );
			productInfo = FDCachedFactory.getProductInfo( qsItem.getSkuCode() );
		} catch ( FDSkuNotFoundException e ) {
			LOG.warn( "Sku not found in quickshop post-process populate. This is unexpected. Skipping item." );
			return;
		} catch ( FDResourceException e ) {
			LOG.error( "Error in quickshop post-process populate. Skipping item." );
			return;
		}
		
		// populate Ecoupons data
		FDCustomerCoupon coupon = user.getCustomerCoupon(productInfo, EnumCouponContext.PRODUCT, productModel.getParentId(), productModel.getContentName());
		qsItem.setCoupon(coupon);
		
		if ( coupon != null ) {
			EnumCouponStatus status = coupon.getStatus();
			qsItem.setCouponDisplay( status != EnumCouponStatus.COUPON_CLIPPED_REDEEMED && status != EnumCouponStatus.COUPON_CLIPPED_EXPIRED );
			qsItem.setCouponClipped( status != EnumCouponStatus.COUPON_ACTIVE );
			qsItem.setCouponStatusText( CartOperations.generateFormattedCouponMessage( coupon, status ) );
		} else {
			qsItem.setCouponDisplay( false );
			qsItem.setCouponClipped( false );
		}
		
    	// populate in cart amount    	
    	FDCartModel cart = user.getShoppingCart(); 
    	qsItem.setInCartAmount( cart.getTotalQuantity( productModel ) );
    	
    	// post-process replacement item too, if any
    	QuickShopLineItem replItem = qsItem.getReplacement();
    	if ( replItem != null ) {
    		postProcessPopulate( user, replItem );
    	}
    	
	}
	
	public static void postProcessPopulate( FDUserI user, FilteringFlowResult<QuickShopLineItemWrapper> result, HttpSession session ) {		
		
		for ( FilteringSortingItem<QuickShopLineItemWrapper> fsItem : result.getItems() ) {
			QuickShopLineItem qsItem = fsItem.getNode().getItem();
			
			postProcessPopulate( user, qsItem );
	    	
	    	//check if we have temporary configuration for this item in session
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

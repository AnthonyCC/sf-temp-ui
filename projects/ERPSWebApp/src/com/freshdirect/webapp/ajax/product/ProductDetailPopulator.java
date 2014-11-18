package com.freshdirect.webapp.ajax.product;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.freshdirect.WineUtil;
import com.freshdirect.common.pricing.CharacteristicValuePrice;
import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.common.pricing.util.GroupScaleUtil;
import com.freshdirect.content.nutrition.ErpNutritionType;
import com.freshdirect.delivery.restriction.DlvRestrictionsList;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionCriterion;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionReason;
import com.freshdirect.delivery.restriction.EnumDlvRestrictionType;
import com.freshdirect.delivery.restriction.RestrictionI;
import com.freshdirect.fdstore.EnumOrderLineRating;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDNutrition;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.GroupScalePricing;
import com.freshdirect.fdstore.ZonePriceInfoModel;
import com.freshdirect.fdstore.content.CategoryModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ContentNodeModel;
import com.freshdirect.fdstore.content.ContentNodeModelUtil;
import com.freshdirect.fdstore.content.DepartmentModel;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.PopulatorUtil;
import com.freshdirect.fdstore.content.PriceCalculator;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.content.customerrating.CustomerRatingsContext;
import com.freshdirect.fdstore.content.customerrating.CustomerRatingsDTO;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDProductSelection;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.fdstore.ecoupon.EnumCouponStatus;
import com.freshdirect.fdstore.ecoupon.FDCustomerCoupon;
import com.freshdirect.fdstore.pricing.ProductModelPricingAdapter;
import com.freshdirect.fdstore.pricing.ProductPricingFactory;
import com.freshdirect.fdstore.util.DYFUtil;
import com.freshdirect.framework.template.TemplateException;
import com.freshdirect.framework.util.DateRange;
import com.freshdirect.framework.util.DayOfWeekSet;
import com.freshdirect.framework.util.TimeOfDay;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.fdstore.ScoreProvider;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.BaseJsonServlet.HttpErrorResponse;
import com.freshdirect.webapp.ajax.cart.CartOperations;
import com.freshdirect.webapp.ajax.cart.data.CartData.Quantity;
import com.freshdirect.webapp.ajax.cart.data.CartData.SalesUnit;
import com.freshdirect.webapp.ajax.product.data.BasicProductData;
import com.freshdirect.webapp.ajax.product.data.CartLineData;
import com.freshdirect.webapp.ajax.product.data.ProductConfigResponseData.VarItem;
import com.freshdirect.webapp.ajax.product.data.ProductConfigResponseData.Variation;
import com.freshdirect.webapp.ajax.product.data.ProductData;
import com.freshdirect.webapp.ajax.product.data.SkuData;
import com.freshdirect.webapp.ajax.quickshop.QuickShopHelper;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItem;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.display.ProductSavingTag;
import com.freshdirect.webapp.util.FDURLUtil;
import com.freshdirect.webapp.util.JspMethods;
import com.freshdirect.webapp.util.MediaUtils;
import com.freshdirect.webapp.util.ProductRecommenderUtil;
import com.freshdirect.webapp.util.RestrictionUtil;

public class ProductDetailPopulator {

	private static final Logger LOG = LoggerFactory.getInstance( ProductDetailPopulator.class );

	
	/**
	 * Create generic product data from product & category ID-s
	 * 
	 * 	Mainly for potato diggers.
	 * 
	 * @param user
	 * @param productId
	 * @param categoryId
	 * @return
	 * @throws HttpErrorResponse
	 * @throws FDResourceException
	 * @throws FDSkuNotFoundException 
	 */
	public static ProductData createProductData( FDUserI user, String productId, String categoryId ) throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {
		
		if ( productId == null ) {
			BaseJsonServlet.returnHttpError( 400, "productId not specified" );	// 400 Bad Request
		}
	
		// Get the ProductModel
		ProductModel product = PopulatorUtil.getProduct( productId, categoryId );
		if (PopulatorUtil.isProductIncomplete(product)) {
			return createProductDataLight(user, product);
		}
		
		return createProductData( user, product );
	}
	
	public static CartLineData createCartLineData (FDUserI user, FDCartLineI cartLine) throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {

		if ( cartLine == null ) {
			BaseJsonServlet.returnHttpError( 400, "missing cartline" );	// 400 Bad Request
		}

		CartLineData cartLineData = new CartLineData();
		cartLineData.setRandomId(cartLine.getRandomId());
		cartLineData.setProduct(createProductData(user, cartLine, false));
		cartLineData.setPrice(JspMethods.formatPrice(cartLine.getPrice()));
		String label = cartLine.getLabel();
		if (!"".equals(label)){
			label = " " + label;
		}
		cartLineData.setQuantity(new DecimalFormat("0.##").format(cartLine.getQuantity()) + label);
		cartLineData.setDescription(cartLine.getDescription());
		cartLineData.setConfigurationDescription(cartLine.getConfigurationDesc());
		
		ContentNodeModel recipe = ContentFactory.getInstance().getContentNode(cartLine.getRecipeSourceId());
		if (recipe != null) {
			cartLineData.setRecipeName(recipe.getFullName());
		}
			
		return cartLineData;
	}

	
	/**
	 * Create generic product data from a cartline object
	 * 
	 * 	Mainly for potato diggers.
	 * 
	 * @param user
	 * @param cartLine
	 * @return
	 * @throws HttpErrorResponse
	 * @throws FDResourceException
	 * @throws FDSkuNotFoundException 
	 */
	public static ProductData createProductData( FDUserI user, FDCartLineI cartLine, boolean showCouponStatus ) throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {
		
		if ( cartLine == null ) {
			BaseJsonServlet.returnHttpError( 400, "missing cartline" );	// 400 Bad Request
		}
	
		//  Note : these are product & category IDs !!!
		String productId = cartLine.getProductName();
		String categoryId = cartLine.getCategoryName();
		
		// Get the models
		ProductModel product = PopulatorUtil.getProduct( productId, categoryId );
		SkuModel sku = product.getSku( cartLine.getSkuCode() );
		
		return createProductData( user, product, sku, cartLine, showCouponStatus );
	}
	
	/**
	 * Create generic product data from ProductModel and SkuModel
	 * 
	 *  Mainly for potato diggers.
	 *  
	 * @param user
	 * @param product
	 * @return
	 * @throws HttpErrorResponse
	 * @throws FDResourceException
	 * @throws FDSkuNotFoundException 
	 */
	public static ProductData createProductData( FDUserI user, ProductModel product, SkuModel sku, FDProductSelectionI lineData, boolean showCouponStatus ) throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {

		if ( product == null ) {
			BaseJsonServlet.returnHttpError( 500, "product not found" );
		}
		
		if ( sku == null ) {
			BaseJsonServlet.returnHttpError( 500, "sku not found" );
		}
		
		if ( !(product instanceof ProductModelPricingAdapter) ) {
			// wrap it into a pricing adapter if naked
			product = ProductPricingFactory.getInstance().getPricingAdapter( product, user.getPricingContext() );
		}
		
		FDProductInfo productInfo = sku.getProductInfo();
		if ( productInfo == null ) {
			BaseJsonServlet.returnHttpError( 500, "productInfo does not exist for this product" );
		}
		
		FDProduct fdProduct = sku.getProduct();		
		if ( fdProduct == null ) {
			BaseJsonServlet.returnHttpError( 500, "fdProduct does not exist for this product" );
		}
		
		PriceCalculator priceCalculator = product.getPriceCalculator();		
		if ( priceCalculator == null ) {
			BaseJsonServlet.returnHttpError( 500, "priceCalculator does not exist for this product" );
		}
		
		if ( lineData == null ) {
			lineData = new FDProductSelection( fdProduct, product, getProductConfiguration( product, fdProduct ), user.getPricingContext().getZoneId() );		
			try {
				lineData.refreshConfiguration();
			} catch (FDInvalidConfigurationException e) {
				LOG.warn( "Invalid configuration" + e.getMessage() );
			}
		}

				
		// Create response data object
		ProductData data = new ProductData();
		
		// Populate product basic-level data
		populateBasicProductData( data, user, product );
		
		// Populate product level data
		populateProductData( data, user, product, sku, fdProduct, priceCalculator, lineData, true, false );
		
		// Populate pricing data
		populatePricing( data, fdProduct, productInfo, priceCalculator );
		
		// Populate sku-level data for the default sku only
		populateSkuData( data, user, product, sku, fdProduct );
		
		// Populate transient-data
		postProcessPopulate( user, data, sku.getSkuCode(), showCouponStatus, lineData );

		return data;
	}
	
	/**
	 * Create generic product data from ProductModel using default sku
	 * 
	 *  Mainly for potato diggers.
	 *  
	 *  Will populate pricing data based on default sku!
	 * 
	 * @param user
	 * @param product
	 * @return
	 * @throws HttpErrorResponse
	 * @throws FDResourceException
	 * @throws FDSkuNotFoundException 
	 */
	public static ProductData createProductData( FDUserI user, ProductModel product ) throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {
		
		if ( product == null ) {
			BaseJsonServlet.returnHttpError( 500, "product not found" );
		}
		
		if ( !(product instanceof ProductModelPricingAdapter) ) {
			// wrap it into a pricing adapter if naked
			product = ProductPricingFactory.getInstance().getPricingAdapter( product, user.getPricingContext() );
		}
		
		if (PopulatorUtil.isProductIncomplete(product)) {
			return createProductDataLight(user, product);
		}
		
		SkuModel sku = PopulatorUtil.getDefSku( product );
		if ( sku == null ) {
			BaseJsonServlet.returnHttpError( 500, "default sku does not exist for this product: " + product.getContentName() );
		}
		
		return createProductData( user, product, sku, null, false );
	}

	public static ProductData populateBrowseRecommendation(FDUserI user, ProductData data, ProductModel product) throws FDResourceException, FDSkuNotFoundException, HttpErrorResponse {
		if ( data == null ) {
			data = new ProductData();
		}
		ProductModel browseRecommendation = ProductRecommenderUtil.getBrowseRecommendation(product);
		if(browseRecommendation!=null){
			data.setBrowseRecommandation(createProductData(user, browseRecommendation));			
		}
		return data;
	}

	public static ProductData populateSelectedNutritionFields(FDUserI user, ProductData data, FDProduct fdProduct, ErpNutritionType.Type erpsNutritionTypeType) {
		if ( data == null ) {
			data = new ProductData();
		}

		if (erpsNutritionTypeType!=null){

			//format nutrition title based on i_grocery_product_separator.jspf
			String nutritionSortTitle = StringUtils.replace( erpsNutritionTypeType.getDisplayName(), " quantity", "");
	        
			if("Total Carbohydrate".equalsIgnoreCase(nutritionSortTitle)){ 
	            nutritionSortTitle = "Total Carbs";
	        }
	        if("%".equals(erpsNutritionTypeType.getUom())){
	            nutritionSortTitle += ", %DV";
	        }
			data.setNutritionSortTitle(nutritionSortTitle);
			data.setNutritionSortValue("no data"); //fallback
			
			FDNutrition selectedNutrition = fdProduct.getNutritionItemByType(erpsNutritionTypeType);
			ErpNutritionType.Type servingSizeType = ErpNutritionType.getType(ErpNutritionType.SERVING_SIZE);
			NumberFormat nf = NumberFormat.getNumberInstance();
			
			if (selectedNutrition!=null){
				
				//format nutrition value based on i_grocery_product_line.jspf
				double nurtitionSortValue = selectedNutrition.getValue();
				String nurtitionSortUom = selectedNutrition.getUnitOfMeasure();
				String nutritionSortValueText = (nurtitionSortValue < 0 ? "<1":nf.format(nurtitionSortValue)) + ("%".equals(nurtitionSortUom)?"":" ") + nurtitionSortUom;
				String nutritionServingSizeValueText = null; //this field is only populated if nutritionSortValueText does not contain serving size info

				double servingSizeValue = 0;
				if (servingSizeType == erpsNutritionTypeType) { 
					servingSizeValue = nurtitionSortValue;
							
				} else { //add serving size info
					FDNutrition servingSizeNutrition = fdProduct.getNutritionItemByType(servingSizeType);
					if (servingSizeNutrition!=null){
						servingSizeValue = servingSizeNutrition.getValue();
						nutritionServingSizeValueText = nf.format(servingSizeValue) + " " + servingSizeNutrition.getUnitOfMeasure();
					}
				}

				if (servingSizeValue != 0){
					FDNutrition servingWeightNutrition = fdProduct.getNutritionItemByType(ErpNutritionType.getType(ErpNutritionType.SERVING_WEIGHT));
					if (servingWeightNutrition!=null){
						String servingWeightText = " (" + nf.format(servingWeightNutrition.getValue()) + "g)";
						if (nutritionServingSizeValueText == null){
							nutritionSortValueText += servingWeightText;							
						} else {
							nutritionServingSizeValueText += servingWeightText;
						}
					}
					
					data.setNutritionSortValue(nutritionSortValueText);
					data.setNutritionServingSizeValue(nutritionServingSizeValueText);
				}
			}
		}
		
		
		//add nutrition fields to try recommendation as well
		ProductData browseRecommendationData = data.getBrowseRecommandation();
		if (browseRecommendationData!=null)	{
			try {
				FDProduct browseRecommendationFDProduct = FDCachedFactory.getProduct(FDCachedFactory.getProductInfo(browseRecommendationData.getSkuCode()));;
				data.setBrowseRecommandation(populateSelectedNutritionFields(user, browseRecommendationData, browseRecommendationFDProduct, erpsNutritionTypeType));
			
			} catch (FDSkuNotFoundException e){
				LOG.error ("Couldnt't add nutrion fields to browseRecommendationData",e);
			} catch (FDResourceException e){
				LOG.error ("Couldnt't add nutrion fields to browseRecommendationData",e);
			}
		}
		
		return data;
	}
	
	
	/**
	 * Populates product basic-level data.
	 * Does not populate any sku level attributes.
	 * 
	 * @param data
	 * @param user
	 * @param product
	 * @return
	 * @throws HttpErrorResponse
	 * @throws FDResourceException
	 */
	public static BasicProductData populateBasicProductData( BasicProductData data, FDUserI user, ProductModel product ) throws FDResourceException {
		if ( data == null ) {
			data = new ProductData();
		}
		
		data.setProductId( product.getContentKey().getId() );
		
		data.setCMSKey( product.getContentKey().getEncoded() );
		
		// Product & brand name - we need to separate them if applicable
        String fullName = product.getFullName();
        String productNameNoBrand = fullName;
        String brandName = product.getPrimaryBrandName();
		if (	brandName != null && 
				brandName.length() > 0 && 
				fullName.length() >= brandName.length() && 
				fullName.substring( 0, brandName.length() ).equalsIgnoreCase( brandName ) 
			) {			
			productNameNoBrand = fullName.substring( brandName.length() ).trim();
		}
		
		data.setProductName( fullName );
		data.setProductNameNoBrand( productNameNoBrand );
		data.setBrandName( brandName );
		
		data.setAkaName( product.getAka() );
		data.setProductImage( product.getProdImage().getPathWithPublishId() );
		data.setProductDetailImage( product.getDetailImage().getPathWithPublishId() );
		data.setProductZoomImage( product.getZoomImage().getPathWithPublishId() );
		
		data.setProductPageUrl( FDURLUtil.getNewProductURI( product ) );
		
		data.setQuantityText( product.getQuantityText() );
		data.setPackageDescription( product.getPackageDescription() );
		data.setSoldBySalesUnit( product.isSoldBySalesUnits() );
		data.setHasTerms( product.hasTerms() );
		
		// alcoholic & usq flags
		try {
			// For alcoholic and usq flags check the default sku
			FDProduct fdProduct = FDCachedFactory.getProduct( FDCachedFactory.getProductInfo( product.getDefaultSkuCode() ) );
			data.setAlcoholic( isAlcoholic( product, fdProduct ) );
			data.setUsq( isUsq( product, fdProduct ) );
		} catch (Exception ignore) {
			LOG.info( "Failed to set alcoholic and usq flags" + ignore.getMessage() );
			// ignore any errors
		}
		
		// bazaar-voice flag
		if ( FDStoreProperties.isBazaarvoiceEnabled() ) {
			List<String> BV_FREE_DEPTS = FDStoreProperties.getBazaarvoiceExcludedDepts();
			
			boolean showReviews = true;
			DepartmentModel dept = null;
			
			// First check primary home's dept
			CategoryModel prHome = product.getPrimaryHome();
			if ( prHome != null )
				dept = prHome.getDepartment();
			
			// Fall back to product's dept if there is no primary home set
			if ( dept == null )
				dept = product.getDepartment();
			
			if ( dept == null || BV_FREE_DEPTS.contains( dept.getContentKey().getId() ) ) {
				// Disable if dept is missing or is in the exclude list
				showReviews = false;
			}
			
			data.setBazaarVoice( showReviews );
		}
					
		return data;
	}

	
	
	/**
	 * Populates product level data.
	 * 
	 * @param item
	 * @param user
	 * @param productModel
	 * @param sku
	 * @param fdProduct
	 * @param priceCalculator
	 * @param orderLine
	 * @param useFavBurst
	 * @param useFavBurst
	 */
	public static void populateProductData( ProductData item, FDUserI user, ProductModel productModel, SkuModel sku, FDProduct fdProduct, PriceCalculator priceCalculator, FDProductSelectionI orderLine, boolean useFavBurst, boolean usePrimaryHome ) {

		if (productModel.isUnavailable()) {
			item.setAvailable( false );
			// if unavailable add product replacements
			if ( item instanceof QuickShopLineItem ) {
				QuickShopHelper.populateReplacements( (QuickShopLineItem)item, productModel, user );
			}
		} else {
			item.setAvailable( true );
		}
		
		item.setCatId( usePrimaryHome ? productModel.getPrimaryHome().getContentKey().getId() : productModel.getCategory().getContentName() );
		item.setSkuCode( sku.getSkuCode() );
		item.setCustomizePopup( !productModel.isAutoconfigurable() );
		item.setHasTerms( productModel.hasTerms() );
		
		populateRatings( item, user, productModel, sku.getSkuCode() );
		populateBursts( item, user, productModel, priceCalculator, useFavBurst );
		populateQuantity( item, user, productModel, fdProduct, orderLine );		
		populateScores( item, user, productModel );
		populateLineData( item, orderLine, fdProduct, productModel, sku);
		populateAvailabilityMessages(item, productModel, fdProduct, sku);
	}


	// ==============
	//   Messaging
	// ==============
	private static void populateAvailabilityMessages(ProductData item,
			ProductModel productModel, FDProduct fdProduct, SkuModel sku) {
		// Party platter cancellation notice
		if ( productModel.isPlatter() ) {
			item.setMsgCancellation( "* Orders for this item cancelled after 3PM the day before delivery (or Noon on Friday/Saturday/Sunday and major holidays) will be subject to a 50% fee." );
		}

		// Party platter cutoff notice (header+text)
		try {
			TimeOfDay cutoffTime = RestrictionUtil.getPlatterRestrictionStartTime();
			if ( productModel.isPlatter() && cutoffTime != null ) {
				String headerTime;
				//String bodyTime;
				
				SimpleDateFormat headerTimeFormat = new SimpleDateFormat("h:mm a");
				//SimpleDateFormat bodyTimeFormat = new SimpleDateFormat("ha");
				
				headerTime = headerTimeFormat.format(cutoffTime.getAsDate());
				//bodyTime = bodyTimeFormat.format(cutoffTime.getAsDate());
				
				item.setMsgCutoffHeader("Order by " + headerTime + " for Delivery Tomorrow");
				item.setMsgCutoffNotice( "" );				
				//item.setMsgCutoffHeader( "Please <b>complete checkout by " + bodyTime + "</b> to order for delivery tomorrow." );
				
			}
		} catch (FDResourceException e) {
		}

		
		// Limited availability messaging
		
		// msgDayOfWeek		- Blocked days of the week notice		
		// msgDeliveryNote	- Another blocked days of the week notice
		
		DayOfWeekSet blockedDays = productModel.getBlockedDays();
		if (!blockedDays.isEmpty()) {
			int numOfDays=0;
			StringBuffer daysStringBuffer = null;
			boolean isInverted=true;
			
			if (blockedDays.size() > 3) {
				numOfDays = (7-blockedDays.size() );
			 	daysStringBuffer= new StringBuffer(blockedDays.inverted().format(true));
			} else {
				isInverted=false;
			  	daysStringBuffer = new StringBuffer(blockedDays.format(true));
				numOfDays = blockedDays.size();
			}
			
			
			if (numOfDays > 1 ) {
				//** make sundays the last day, if more than one in the list 
				if (daysStringBuffer.indexOf("Sundays, ")!=-1)  {
					daysStringBuffer.delete(0,9);
					daysStringBuffer.append(" ,Sundays");
				}
				
				//replace final comma with "and" or "or"
				int li = daysStringBuffer.lastIndexOf(",");
				daysStringBuffer.replace(li,li+1,(isInverted ?" and ": " or ") );
			}
			
			//item.setMsgDayOfWeekHeader( "Limited Availability" );
			item.setMsgDayOfWeekHeader( "<b>" + ( isInverted ? "Only" : "Not" ) + "</b> available for delivery on <b>" + daysStringBuffer.toString() + "</b>" );
			item.setMsgDayOfWeek( "" );
			
			item.setMsgDeliveryNote( "Only available for delivery on " + blockedDays.inverted().format(true) + "." );
		}
		
		

		// Lead time message
		if ( fdProduct != null ) {
			int leadTime = fdProduct.getMaterial().getLeadTime();		
			if( leadTime > 0 && FDStoreProperties.isLeadTimeOasAdTurnedOff() ) {
				//item.setMsgLeadTimeHeader( JspMethods.convertNumToWord(leadTime) + "-Day Lead Time" );
				item.setMsgLeadTimeHeader("Please complete checkout at least two days in advance. (Order by Thursday for Saturday).");
				item.setMsgLeadTime( "" );
			}
		}
		
		// Kosher restrictions
		if ( fdProduct != null && fdProduct.getKosherInfo() != null && fdProduct.getKosherInfo().isKosherProduction() ) {
			try {
				DlvRestrictionsList globalRestrictions = FDDeliveryManager.getInstance().getDlvRestrictions();

				StringBuilder buf = new StringBuilder( "* Not available for delivery on Friday, Saturday, or Sunday morning." );
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, 1);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				Date startDate = cal.getTime();

				cal.add(Calendar.DATE, 7);
				Date endDate = cal.getTime();
				
				DateRange dateRange = new DateRange(startDate, endDate);

				List<RestrictionI> kosherRestrictions = globalRestrictions.getRestrictions(
						EnumDlvRestrictionCriterion.DELIVERY,
						EnumDlvRestrictionReason.KOSHER,
						EnumDlvRestrictionType.ONE_TIME_RESTRICTION,
						dateRange);

		    	if ( kosherRestrictions.size() > 0 ) {
		    		buf.append( "<br/>Also unavailable during " );
		    		int s = kosherRestrictions.size();
					for ( int i = 0; i < s; i++ ) {
						RestrictionI r = kosherRestrictions.get( i );
						buf.append( "<b>" + r.getName() + "</b>, " + r.getDisplayDate() + ( ( i == s - 1 ) ? "." : "; " ) );
					}
		    	}
				
		    	item.setMsgKosherRestriction( buf.toString() );
			} catch (FDResourceException e) {
			}
			
		}
		// earliest availability - product not yet available but will in the near future
		if (sku != null) {
			item.setMsgEarliestAvailability( sku.getEarliestAvailabilityMessage() );
		}
	}

	/**
	 * Populates the default sku for the product.
	 * Returns a ProductData object.
	 * 
	 * As multiple sku products are not used anymore, this is the combined product+sku data for any future use. 
	 *  
	 * @param data
	 * @param user
	 * @param product
	 * @param fdProduct 
	 * @param sku 
	 * @return
	 * @throws HttpErrorResponse
	 * @throws FDResourceException
	 * @throws FDSkuNotFoundException 
	 */
	public static ProductData populateSkuData( ProductData data, FDUserI user, ProductModel product, SkuModel sku, FDProduct fdProduct ) throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {
		
		// Get the sku
		if ( sku == null ) {
			sku = product.getDefaultSku();
		}

		// Get the fdproduct
		if ( fdProduct == null ) {
			fdProduct = sku.getProduct();
		}
		
		// Automatic configuration
		Map<String,String> currentConfig = null;
		if ( fdProduct.isAutoconfigurable( product.isSoldBySalesUnits() ) ) {	        		
			// try to autoconfigure
			FDConfigurableI autoConfigured = fdProduct.getAutoconfiguration( product.isSoldBySalesUnits(), product.getQuantityMinimum() );
			if ( autoConfigured != null ) {	        			
				currentConfig = autoConfigured.getOptions();
			}	        		
		}

		// Set data
		
		populateSkuVariations(data, getVariations( fdProduct, currentConfig ) );
		data.setLabel( getLabel( sku ) );
		
		data.setSalesUnitLabel( product.getSalesUnitLabel() );
		
		if ( product.isHasSalesUnitDescription() ) {
			data.setHasSalesUnitDescription( true );
			
			String parentCat = product.getParentNode().getContentName();
			
			StringBuilder popupUrl = new StringBuilder(); 
			popupUrl.append("/shared/popup.jsp?catId=");
			popupUrl.append(parentCat);
			popupUrl.append("&prodId="); 
			popupUrl.append(product.getContentName()); 
			popupUrl.append("&attrib=SALES_UNIT_DESCRIPTION&tmpl=");
			
			// Strange stuff copied from i_product.jspf
            if ( parentCat.equalsIgnoreCase("fwhl") || parentCat.equalsIgnoreCase("fstk") || parentCat.equalsIgnoreCase("fflt") || 
        		parentCat.equalsIgnoreCase("prchp") || parentCat.equalsIgnoreCase("bovnrst") || parentCat.equalsIgnoreCase("bpotrst") || 
        		parentCat.equalsIgnoreCase("kosher_meat_beef_roast") || parentCat.indexOf("kosher_seafood") > -1 ) 
            {
            	popupUrl.append( "small" );
            } else {
            	popupUrl.append( "large" );			            	
            }

			data.setSalesUnitDescrPopup( popupUrl.toString() );
			
		}

		return data;		
	}

	private static void populateRatings( ProductData item, FDUserI user, ProductModel product, String skuCode ) {
		
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
	
		//check if sku should show ratings at all
		item.setShowRatings(PopulatorUtil.isShowRatings(skuCode));
		
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
		
		// 5. heat rating
		item.setHeatRating(product.getHeatRating());
		if (item.getHeatRating() > 0) {
			String heatRatingMediaPath = "/media/brands/fd_heatscale/fd_heatscale.html";
			try {
				item.setHeatRatingScale(fetchMedia(heatRatingMediaPath, user, false));
			} catch (IOException e) {
				LOG.error("Failed to fetch Heat Scale Legend Media " + heatRatingMediaPath + " " + e.getMessage());
			} catch (TemplateException e) {
				LOG.error("Failed to fetch Heat Scale Legend Media " + heatRatingMediaPath + " " + e.getMessage());
			}
		}
	}

	private static void populateBursts( ProductData item, FDUserI user, ProductModel product, PriceCalculator priceCalculator, boolean useFavBurst ) {
	
		boolean showBurstImage = true;
		ZonePriceInfoModel model;
		
		try {
			model = priceCalculator.getZonePriceInfoModel();
			if ( model != null ) {
				showBurstImage = model.isShowBurstImage();
			}
		} catch ( FDResourceException ignore ) {
		} catch ( FDSkuNotFoundException ignore ) {
		}
		
		int deal = showBurstImage ? priceCalculator.getHighestDealPercentage() : priceCalculator.getGroupDealPercentage();
		
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

	private static final float calculateSafeMaximumQuantity( FDUserI user, ProductModel product ) {
		float min = product.getQuantityMinimum();
		float max = user.getQuantityMaximum( product );
		float inc = product.getQuantityIncrement();

		while ( min <= max - inc )
			min += inc;
		return min;
	}
	
	private static void populateQuantity( ProductData item, FDUserI user, ProductModel productModel, FDProduct fdProduct, FDProductSelectionI orderLine ) {
		
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
		quantity.setqMax( calculateSafeMaximumQuantity( user, productModel ) );
		quantity.setqInc( productModel.getQuantityIncrement() );
		quantity.setQuantity( orderLine != null ? orderLine.getQuantity() : quantity.getqMin() );	
		item.setQuantity( quantity );
		
		// populate in cart amount    	
		FDCartModel cart = user.getShoppingCart(); 
		item.setInCartAmount( cart.getTotalQuantity( productModel, false ) );
				
	}

	private static void populateScores( ProductData item, FDUserI user, ProductModel productModel ) {
		//get frequency and recency values from smartstore
		String[] variables = new String[2];
		variables[0] = ScoreProvider.USER_FREQUENCY;
		variables[1] = ScoreProvider.RECENCY_DISCRETIZED;
		String userId = user.getIdentity() != null ? user.getIdentity().getErpCustomerPK() : null;
		double[] scores = ScoreProvider.getInstance().getVariables(userId, productModel.getPricingContext(), productModel.getContentKey(), variables);
		item.setFrequency(scores[0]);
		item.setRecency(scores[1]);
	}

	private static void populateLineData( ProductData item, FDProductSelectionI lineItem, FDProduct product, ProductModel productModel, SkuModel sku ) {
		
		// orderline/cartline/productselection data
		if ( lineItem != null ) {
			
			item.setConfigInvalid( lineItem.isInvalidConfig() );
	
			Map<String,String> config = lineItem.getConfiguration().getOptions();
			if ( config != null && !config.isEmpty() ) {
				item.setConfiguration( config );
			}
	
			item.setConfigDescr( lineItem.getConfigurationDesc() );

			item.setSalesUnitDescrPDP( generateSalesUnitDescrPDP(product, lineItem, productModel, sku, (item.getSalesUnit() != null && item.getSalesUnit().size() > 1) ? true : false));

			item.setDescription( lineItem.getDescription() );
	
			item.setDepartmentDesc( lineItem.getDepartmentDesc() );
	
			item.setConfiguredPrice( lineItem.getConfiguredPrice() );
			
		}		
	}
	
	private static String generateSalesUnitDescrPDP(FDProduct product, FDProductSelectionI theProduct, ProductModel productModel, SkuModel sku, boolean isMultiChoice) {
		
		FDSalesUnit unit = product.getSalesUnit(theProduct.getConfiguration().getSalesUnit());
		String salesUnitDescr = unit.getDescription();

		StringBuffer PDPsalesUnitDescr = new StringBuffer();

		// clean sales unit description
		if (salesUnitDescr != null) {
			salesUnitDescr = salesUnitDescr.trim();
			
			String salesUnitDescrHead = salesUnitDescr; // if the part before '('
			if (salesUnitDescr.indexOf("(") > -1) {
				salesUnitDescrHead = salesUnitDescr.substring(0, salesUnitDescr.indexOf("("));
				salesUnitDescrHead = salesUnitDescrHead.trim();
			}
			if ((!"".equals(salesUnitDescr)) //original is empty
			
				// if the part before '(' is "nm" and "ea", it should be ignored
				&& (!"nm".equalsIgnoreCase(salesUnitDescrHead))
				&& (!isMultiChoice) //this is different compared to OrderLineUtil.createConfigurationDescription()
				&& (!"ea".equalsIgnoreCase(salesUnitDescrHead))) {
				if (!productModel.getSellBySalesunit().equals("SALES_UNIT")) {
					PDPsalesUnitDescr.append(salesUnitDescr); //append original
				} else if ((productModel.getPrimarySkus().size() == 1)
					&& (productModel.getVariationMatrix().isEmpty())
					&& (product.getSalesUnits().length == 1)
					&& (product.getSalesUnits()[0].getName().equalsIgnoreCase("EA"))) {
					PDPsalesUnitDescr.append(salesUnitDescr); //append original
				}
			}
		}
		
		return PDPsalesUnitDescr.toString();
	}
	
	public static void populatePricing( ProductData item, FDProduct fdProduct, FDProductInfo productInfo, PriceCalculator priceCalculator ) throws FDResourceException {
	
		ZonePriceInfoModel zpi;
		try {
			zpi = priceCalculator.getZonePriceInfoModel();
			if ( zpi != null ) {
				item.setPrice( zpi.getDefaultPrice() );
				item.setScaleUnit( productInfo.getDisplayableDefaultPriceUnit().toLowerCase() );
			}
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
		
		populateSubtotalInfo( item, fdProduct, productInfo, priceCalculator );
		populateSaving( item, productInfo, priceCalculator );
		
		// [APPDEV-3438] unit price thingy
		FDSalesUnit su = fdProduct.getDefaultSalesUnit();
		if (su != null) {
			// validate unit price values
			final int n = su.getUnitPriceNumerator();
			final int d = su.getUnitPriceDenominator();
			if (n > 0 && n > 0) {
				final double p = (item.getPrice() * n) / d;

				item.setUtPrice( formatDecimal(p) );
				item.setUtSalesUnit( su.getUnitPriceUOM() );
			}
		}
	}

	private static void populateSubtotalInfo( ProductData item, FDProduct fdProduct, FDProductInfo productInfo, PriceCalculator priceCalculator ) throws FDResourceException {
	
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

	private static void populateSaving( BasicProductData item, FDProductInfo productInfo, PriceCalculator priceCalculator ) throws FDResourceException {
		
		MaterialPrice matPrice = null;
		GroupScalePricing grpPricing = null;
		
		FDGroup group = productInfo.getGroup();
		
		if ( group != null ) {
			grpPricing = GroupScaleUtil.lookupGroupPricing( group );
			matPrice = GroupScaleUtil.getGroupScalePrice( group, priceCalculator.getPricingContext().getZoneId() );
		}
	
		StringBuilder buf = new StringBuilder();
		if ( grpPricing != null && matPrice != null ) {
			
			// Group scale pricing (a.k.a. mix'n'match)
			item.setMixNMatch( true );
			item.setDealInfo( ProductSavingTag.getGroupPrice( group, priceCalculator.getPricingContext().getZoneId() ) );
			
			item.setGrpShortDesc( grpPricing.getShortDesc() );
			item.setGrpLongDesc( grpPricing.getLongDesc() );
			item.setGrpLink( grpPricing.getGroupPageUrl( productInfo.getSkuCode() ) );
			
			// Group Scale Pricing - price string
			StringBuilder priceStr = new StringBuilder();
		    NumberFormat FORMAT_CURRENCY = NumberFormat.getCurrencyInstance(Locale.US);
		    DecimalFormat FORMAT_QUANTITY = new java.text.DecimalFormat("0.##");
			
			boolean isSaleUnitDiff = false;
			double displayPrice = 0.0;
			if (matPrice.getPricingUnit().equals(matPrice.getScaleUnit()))
				displayPrice = matPrice.getPrice() * matPrice.getScaleLowerBound();
			else {
				displayPrice = matPrice.getPrice();
				isSaleUnitDiff = true;
			}
		    
		    priceStr.append(FORMAT_QUANTITY.format(matPrice.getScaleLowerBound()));

            if (matPrice.getScaleUnit().equals("LB")) {
            	priceStr.append(matPrice.getScaleUnit().toLowerCase()).append("s");
            }

            priceStr.append(" for ");
            priceStr.append(FORMAT_CURRENCY.format(displayPrice));

            if (isSaleUnitDiff) {
            	priceStr.append("/").append(matPrice.getPricingUnit().toLowerCase());
            }
            
            item.setGrpPrice( priceStr.toString() );
            
		} else {
			// Regular deal
			String scaleString = priceCalculator.getTieredPrice( 0, null );
			if ( scaleString != null ) {
				buf.append( "Save! " );
				buf.append( scaleString );
			} else if ( priceCalculator.isOnSale() ) {
				buf.append( "Save " );
				buf.append( priceCalculator.getDealPercentage() );
				buf.append( "%" );
			} else {
				// no sales, do nothing
			}
		}
		item.setSavingString( buf.toString() );
		
	}
	
	public static void postProcessPopulate( FDUserI user, BasicProductData item, String skuCode ) {
		postProcessPopulate(user, item, skuCode, false, null);
	}

	public static void postProcessPopulate( FDUserI user, BasicProductData item, String skuCode, boolean showCouponStatus, FDProductSelectionI lineData ) {
		
		// lookup product data
		ProductModel productModel;
		FDProductInfo productInfo;
		try {
			productModel = ContentFactory.getInstance().getProduct( skuCode );
			productInfo = FDCachedFactory.getProductInfo( skuCode );
		} catch ( FDSkuNotFoundException e ) {
			LOG.warn( "Sku not found in post-process populate. This is unexpected. Skipping item." );
			return;
		} catch ( FDResourceException e ) {
			LOG.error( "Error in post-process populate. Skipping item." );
			return;
		}
		
		postProcessPopulate( user, item, productModel, productInfo, showCouponStatus, lineData );
		
		// post-process replacement item too, if any
		if ( item instanceof QuickShopLineItem ) {
	    	QuickShopLineItem replItem = ((QuickShopLineItem)item).getReplacement();
	    	if ( replItem != null ) {
	    		postProcessPopulate( user, replItem, replItem.getSkuCode(), showCouponStatus, lineData );
	    	}
		}
		
	}
		
	public static void postProcessPopulate( FDUserI user, BasicProductData item, ProductModel productModel, FDProductInfo productInfo, boolean showCouponStatus, FDProductSelectionI lineData ) {
		
		// populate Ecoupons data
		FDCustomerCoupon coupon = null;
		if(!showCouponStatus){
			coupon = user.getCustomerCoupon(productInfo, EnumCouponContext.PRODUCT, productModel.getParentId(), productModel.getContentName());			
		}else if (lineData instanceof FDCartLineI){
			coupon = user.getCustomerCoupon((FDCartLineI)lineData, EnumCouponContext.VIEWCART);
		}
		item.setCoupon(coupon);
		
		if ( coupon != null ) {
			EnumCouponStatus status = coupon.getStatus();
			item.setCouponDisplay( status != EnumCouponStatus.COUPON_CLIPPED_REDEEMED && status != EnumCouponStatus.COUPON_CLIPPED_EXPIRED );
			item.setCouponClipped( status != EnumCouponStatus.COUPON_ACTIVE );
			if(showCouponStatus){
				item.setCouponStatusText( CartOperations.generateFormattedCouponMessage( coupon, status ) );				
			}else{
				item.setCouponStatusText( "" );				
			}
		} else {
			item.setCouponDisplay( false );
			item.setCouponClipped( false );
		}
		
		// populate in cart amount if it hasn't been populated yet
		if(item.getInCartAmount()==0){
			FDCartModel cart = user.getShoppingCart(); 
			item.setInCartAmount( cart.getTotalQuantity( productModel ) );			
		}
	}

	
	public static boolean isUsq( ProductModel productModel, FDProduct fdProduct ) {
		return fdProduct.isWine() && ContentNodeModelUtil.hasWineDepartment( productModel.getContentKey() );
	}

	public static boolean isAlcoholic( ProductModel productModel, FDProduct fdProduct ) {
		return fdProduct.isAlcohol() || productModel.getPrimaryHome().isHavingBeer();
	}

	public static FDConfigurableI getProductConfiguration( ProductModel product, FDProduct fdProduct ) {
		FDConfigurableI config = null;
		if ( fdProduct.isAutoconfigurable( product.isSoldBySalesUnits() ) ) {	        		
			// try to autoconfigure
			config = fdProduct.getAutoconfiguration( product.isSoldBySalesUnits(), product.getQuantityMinimum() );
		}
		if ( config == null ) {
			String salesUnit = getDefaultSalesUnit( fdProduct );
			config = new FDConfiguration( product.getQuantityMinimum(), salesUnit );
		}
		return config;
	}

	public static String getDefaultSalesUnit( FDProduct fdProduct ) {
		FDSalesUnit su = fdProduct.getDefaultSalesUnit();
		if ( su == null ) {
			su = fdProduct.getSalesUnits()[0];
		}
		String salesUnit = su != null ? su.getName() : "unknown salesunit";
		return salesUnit;
	}

	public static String getLabel( SkuModel sku ) {
		StringBuilder sb = new StringBuilder();
		List<DomainValue> varMx = sku.getVariationMatrix();
		boolean first = true;
		for ( DomainValue dv : varMx ) {
			if ( first ) {
				first = false;
			} else {
				sb.append( ", " );	        			
			}
			sb.append( dv.getLabel() );
		}
		String s = sb.toString();
		return s;
	}


	public static List<Variation> getVariations( FDProduct fdProd, Map<String, String> currentConfig ) {
		FDVariation[] variations = fdProd.getVariations();
		List<Variation> varList = new ArrayList<Variation>();
		
		for ( FDVariation fdVar : variations ) {
			String varName = fdVar.getName();
			Variation var = new Variation();
			var.setName( varName );
			var.setLabel( fdVar.getDescription() );
			var.setOptional( fdVar.isOptional() );
			var.setDisplay( fdVar.getDisplayFormat() );
			var.setUnderLabel( fdVar.getUnderLabel() );
			
			// Bizarre help popup link generation, copied from i_product.jspf
			try {
	            String charFileName = "media/editorial/fd_defs/characteristics/"+varName.toLowerCase()+ ".html";
		    	URL url = MediaUtils.resolve( FDStoreProperties.getMediaPath(), charFileName );
				InputStream in = url.openStream();
				if ( in != null ) {
			  		in.close();
			  		
			  		// Media file for popup does exist, generate data 
					StringBuilder popupUrl = new StringBuilder();			
					popupUrl.append( "/shared/fd_def_popup.jsp?charName=" );
					popupUrl.append( varName );
					popupUrl.append( "&tmpl=" );
					popupUrl.append( ( fdVar.getDescription().equalsIgnoreCase("MARINADE/RUB") ) ? "large_pop" : "small_pop" );
					popupUrl.append( "&title=" );
					popupUrl.append( fdVar.getDescription() );
					
					var.setDescrPopup( popupUrl.toString() );
				}
			} catch ( Exception ex ) { } 


			List<FDVariationOption> availableFDVarOptions = collectAvailableVariations(fdVar);

			List<VarItem> varOpts = new ArrayList<VarItem>();
			for ( FDVariationOption varOpt : availableFDVarOptions ) {
				VarItem v = new VarItem();
				String vName = varOpt.getName();
				v.setLabelValue( varOpt.isLabelValue() );
				v.setName( vName );
				v.setLabel( varOpt.getDescription() );	        			
				v.setSelected( currentConfig == null ? false : vName.equals( currentConfig.get( varName ) ) );
				CharacteristicValuePrice cvp = fdProd.getPricing().findCharacteristicValuePrice(varName, vName);
				v.setCvp(cvp == null ? "0" : JspMethods.formatPrice( cvp.getPrice() ) );
				varOpts.add( v );
			}
			var.setValues( varOpts );
			varList.add( var );
		}
		return varList;
	}



	private static List<FDVariationOption> collectAvailableVariations(
			FDVariation fdVar) {
		// collect all available variation options
		List<FDVariationOption> availableFDVarOptions = new ArrayList<FDVariationOption>();

		final ContentFactory cf = ContentFactory.getInstance();

		FDVariationOption[] varOpts = fdVar.getVariationOptions();

		for (int optIdx = 0; optIdx < varOpts.length; optIdx++) {
			String optSkuCode = varOpts[optIdx].getSkuCode();

			// sometimes skucode attrib in erps may be missing..so handle
			// it, so we don't get SkuNotFoundException
			if (optSkuCode == null || "".equals(optSkuCode.trim())) {
				 // not a product, add to list
				availableFDVarOptions.add(varOpts[optIdx]);
			} else {
				ProductModel pm = null;
				try {
					pm = cf.getProduct(optSkuCode);
				} catch (FDSkuNotFoundException e) {
					LOG.error(e);
				}

				if (pm == null) {
					LOG.debug("Variation has orphan sku with no ProductModel): " + varOpts[optIdx] + " (Sku: " + optSkuCode + ")");
				} else {
					SkuModel sku = pm.getSku(optSkuCode);

					if (sku != null && !sku.isUnavailable()) {
						availableFDVarOptions.add(varOpts[optIdx]);
					} else {
						if (sku.isUnavailable()) {
							LOG.debug("Skipping as unavailable: " + varOpts[optIdx] + " (Sku: " + optSkuCode + ")");
						}
					}
				}
			}
		}

		return availableFDVarOptions;
	}


	/**
	 * This utility method not just populates {@link SkuData#variations} field
	 * but also the {@link SkuData#variationDisplay} indicator
	 *  
	 * @param data
	 * @param variations
	 */
	public static void populateSkuVariations(SkuData data, List<Variation> variations) {
		if (data == null || variations == null)
			return;

		data.setVariations(variations);
	
		for (Variation v : variations) {
			if (v.getValues() != null && v.getValues().size() > 1) {
				data.setVariationDisplay(true);
				break;
			}
		}
	}
	
	
	/**
	 * Produce lightweight potato data
	 * 
	 * @param user
	 * @param product
	 * 
	 * @return
	 * 
	 * @throws HttpErrorResponse
	 * @throws FDResourceException
	 * @throws FDSkuNotFoundException
	 */
	public static ProductData createProductDataLight(FDUserI user, ProductModel product) throws HttpErrorResponse, FDResourceException, FDSkuNotFoundException {
		// Episode I - DO THE MAGIC / PREPARATIONS
		
		if ( !(product instanceof ProductModelPricingAdapter) ) {
			// wrap it into a pricing adapter if naked
			product = ProductPricingFactory.getInstance().getPricingAdapter( product, user.getPricingContext() );
		}

		PriceCalculator priceCalculator = product.getPriceCalculator();		
		if ( priceCalculator == null ) {
			BaseJsonServlet.returnHttpError( 500, "priceCalculator does not exist for this product" );
		}

		SkuModel sku = null;
		if (product.getSkus() != null && product.getSkus().size() > 0) {
			// just pick the first
			sku = product.getSku(0);
		}

		
		// Episode II - POPULATE DATA
		LOG.debug("Product["+product.getContentKey().getId() + "] with SKU[" + (sku != null ? sku.getContentKey().getId() : "null") + "] is considered incomplete");
		
		// Create response data object
		ProductData data = new ProductData();
		data.setIncomplete(true);

		// Populate product basic-level data
		populateBasicProductData( data, user, product );

		// FIXME Block below is possibly ignored
		if (sku != null) {
			// Populate sku-level data for the default sku only
			// populateSkuData( data, user, product, sku, null );

			// Populate transient-data
			postProcessPopulate( user, data, sku.getSkuCode() );
		}
		
		return data;
	}

	private static String fetchMedia(String mediaPath, FDUserI user, boolean quoted) throws IOException, TemplateException {
		if (mediaPath == null)
			return null;

		Map<String,Object> parameters = new HashMap<String,Object>();
		
		/* pass user/sessionUser by default, so it doesn't need to be added every place this tag is used. */
		parameters.put("user", (FDUserI)user);
		parameters.put("sessionUser", (FDSessionUser)user);
		
		StringWriter out = new StringWriter();
				
		MediaUtils.render(mediaPath, out, parameters, false, 
				user != null && user.getPricingContext() != null ? user.getPricingContext() : PricingContext.DEFAULT);

		String outString = out.toString();
		
		//fix media if needed
		outString = MediaUtils.fixMedia(outString);
		
		return quoted ? JSONObject.quote( outString ) : outString;
	}


	private static String FORMAT_STR = "0.##";
	private static double formatDecimal(double number) {
		DecimalFormat decimalFormat = new DecimalFormat( FORMAT_STR );
		String strNumber = decimalFormat.format(number);
		strNumber = strNumber.replaceAll(",", ".");
		Double numberDouble = new Double(strNumber);
		return numberDouble.doubleValue();
	}
}

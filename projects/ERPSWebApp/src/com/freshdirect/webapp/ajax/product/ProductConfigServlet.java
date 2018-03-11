package com.freshdirect.webapp.ajax.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.freshdirect.cms.core.domain.ContentKeyFactory;
import com.freshdirect.cms.core.domain.ContentType;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.storeapi.content.SkuModel;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.cart.data.CartData.Quantity;
import com.freshdirect.webapp.ajax.cart.data.CartData.SalesUnit;
import com.freshdirect.webapp.ajax.product.data.ProductConfigRequestData;
import com.freshdirect.webapp.ajax.product.data.ProductConfigResponseData;
import com.freshdirect.webapp.ajax.product.data.ProductConfigResponseData.Sku;
import com.freshdirect.webapp.ajax.quickshop.QuickShopHelper;
import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItem;


public class ProductConfigServlet extends BaseJsonServlet {

	private static final long	serialVersionUID	= 4376343201345823580L;

	@SuppressWarnings( "unused" )
	private static final Logger LOG = LoggerFactory.getInstance( ProductConfigServlet.class );

	@Override
	protected boolean synchronizeOnUser() {
		return false;
	}

	@Override
	protected int getRequiredUserLevel() {
		return FDUserI.GUEST;
	}

	@Override
	protected void doGet( HttpServletRequest request, HttpServletResponse response, FDUserI user ) throws HttpErrorResponse {

		// Parse request data
        ProductConfigRequestData reqData = parseRequestData( request, ProductConfigRequestData.class );

       	try {

	        ProductConfigResponseData responseData = createProductConfigData(user,reqData);

			Map<String,Object> productConfigResult = new HashMap<String,Object>();
			productConfigResult.put("productConfig", responseData );

			writeResponseData( response, productConfigResult );

    	} catch (Exception e) {
        	returnHttpError( 500, "Error while getting product configuration for user: " + user.getUserId(), e );	// 500 Internal Server Error
		}
	}

	/**
	 * Creates response data for product-config servlet (customize popup)
	 *
	 * Will be populated with all skus for legacy reasons and quickshop.
	 *
	 * @param user
	 * @param reqData
	 * @return
	 * @throws FDResourceException
	 * @throws HttpErrorResponse
	 */
	protected ProductConfigResponseData createProductConfigData( FDUserI user, ProductConfigRequestData reqData ) throws FDResourceException, HttpErrorResponse {

		String productId = reqData.getProductId();
		if ( productId == null ) {
			returnHttpError( 400, "productId not specified" );	// 400 Bad Request
		}

		// Get the ProductModel
		ProductModel product = null;

		String categoryId = reqData.getCategoryId();
		if ( categoryId == null ) {
			// get product in its primary home
			LOG.debug("get product in its primary home "+productId);
			LOG.debug("ContentFactory.getInstance()"+ContentFactory.getInstance());
			product = (ProductModel)ContentFactory.getInstance().getContentNodeByKey( ContentKeyFactory.get(ContentType.Product, productId) );
		} else {
			// get product in specified category context
			LOG.debug("get product in specified category context"+categoryId+":"+productId);
			LOG.debug("ContentFactory.getInstance()========"+ContentFactory.getInstance());
			LOG.debug("ContentFactory.getInstance()===="+ContentFactory.getInstance());
			LOG.debug("ContentFactory.getInstance().getCurrentUserContext()========"+ContentFactory.getInstance().getCurrentUserContext());
			LOG.debug("ContentFactory.getInstance().getCurrentUserContext().getPricingContext()====="+ContentFactory.getInstance().getCurrentUserContext().getPricingContext());
			LOG.debug("ContentFactory.getInstance().getCurrentUserContext().getPricingContext().getZoneInfo()==="+ContentFactory.getInstance().getCurrentUserContext().getPricingContext().getZoneInfo());
			product = ContentFactory.getInstance().getProductByName( categoryId, productId );
		}

		if ( product == null ) {
			returnHttpError( 500, "product not found" );
		}

		// Create response data object
		ProductConfigResponseData responseData = new ProductConfigResponseData();

		// Populate product basic-level data
		ProductDetailPopulator.populateBasicProductData( responseData, user, product );

		// Fill sku level data for all skus
		populateWithAllSkus( responseData, user, product, reqData );

		// Populate transient-data
		ProductDetailPopulator.postProcessPopulate( user, responseData, product.getDefaultSkuCode() );

		// Done
		return responseData;
	}


	/**
	 * Populates all skus for product config servlet.
	 * Returns a ProductConfigResponseData object.
	 *
	 * This is to be used in legacy codes, which still need support for multiple sku products.
	 *
	 * @param data
	 * @param user
	 * @param product
	 * @return
	 * @throws HttpErrorResponse
	 * @throws FDResourceException
	 */
	protected static ProductConfigResponseData populateWithAllSkus( ProductConfigResponseData responseData, FDUserI user, ProductModel product, ProductConfigRequestData reqData ) throws HttpErrorResponse, FDResourceException {

		// Get the sku list
		List<SkuModel> skus = product.getSkus();
		String defaultSku = product.getDefaultSkuCode();
		String selectedSku = reqData.getSkuCode();

		if ( selectedSku == null ) {
			// use default sku
			selectedSku = defaultSku;
		}

		// Prepare map for results
		List<Sku> skusList = new ArrayList<Sku>();

		// Collect sku data
		for ( SkuModel sku : skus ) {

			Sku skuItem = createSkuData( user, product, sku, defaultSku, selectedSku, reqData.getConfiguration(), reqData.getQuantity(), reqData.getSalesUnit() );

			if ( skuItem != null ) {
				skusList.add( skuItem );
			}
		}

		responseData.setSkus( skusList );
		return responseData;

	}

	/**
	 * Creates sku level data for a single sku.
	 *
	 * @param user
	 * @param product
	 * @param sku
	 * @param defaultSku
	 * @param selectedSku
	 * @param currentConfig
	 * @param currentQuantity
	 * @param currentSalesUnit
	 * @return
	 * @throws FDResourceException
	 */
	protected static Sku createSkuData( FDUserI user, ProductModel product, SkuModel sku, String defaultSku, String selectedSku, Map<String,String> currentConfig, double currentQuantity, String currentSalesUnit ) throws FDResourceException {

		String skuCode = sku.getSkuCode();

		Sku skuItem = new Sku();

		skuItem.setSkuCode( skuCode );
		skuItem.setAvailable( !sku.isUnavailable() );
		skuItem.setSelected( skuCode.equals( selectedSku ) );
		skuItem.setDefaultSku( skuCode.equals( defaultSku ) );
		skuItem.setSalesUnitLabel( product.getSalesUnitLabel() );
		skuItem.setHasSalesUnitDescription( product.isHasSalesUnitDescription() );

		FDProductInfo fdProdInf;
		FDProduct fdProd;
		try {
	    	fdProdInf = FDCachedFactory.getProductInfo( skuCode );
	    	fdProd = FDCachedFactory.getProduct( fdProdInf );
		} catch (FDSkuNotFoundException e) {
			// sku not found - strange, but just skip it
			return null;
		}

		// Automatic configuration
		if ( currentConfig == null && fdProd.isAutoconfigurable( product.isSoldBySalesUnits() ) ) {
			// try to autoconfigure
			FDConfigurableI autoConfigured = fdProd.getAutoconfiguration( product.isSoldBySalesUnits(), product.getQuantityMinimum() );
			if ( autoConfigured != null ) {
				currentConfig = autoConfigured.getOptions();
			}
		}

		ProductDetailPopulator.populateSkuVariations(skuItem, ProductDetailPopulator.getVariations( fdProd, product, currentConfig,user.getUserContext().getPricingContext() ) );
		skuItem.setLabel( ProductDetailPopulator.getLabel( sku ) );
		skuItem.setPrice( fdProdInf.getZonePriceInfo(user.getPricingContext().getZoneInfo()).getDefaultPrice() );

		QuickShopLineItem qsSku = QuickShopHelper.createItemFromProduct( product, sku, user, true );

		skuItem.setAvailMatPrices( qsSku.getAvailMatPrices() );
		skuItem.setCvPrices( qsSku.getCvPrices() );
		skuItem.setSuRatios( qsSku.getSuRatios() );
		skuItem.setGrpPrices( qsSku.getGrpPrices() );

		skuItem.setDeal( qsSku.getDeal() );
		skuItem.setDealInfo( qsSku.getDealInfo() );

		skuItem.setWineRating( qsSku.getWineRating() );
		skuItem.setCustomerRating( qsSku.getCustomerRating() );
		skuItem.setExpertRating( qsSku.getExpertRating() );
		skuItem.setSustainabilityRating( qsSku.getSustainabilityRating() );

		skuItem.setBadge( qsSku.getBadge() );

		skuItem.setWasPrice( qsSku.getWasPrice() );
		skuItem.setScaleUnit( qsSku.getScaleUnit() );
		skuItem.setTaxAndDeposit( qsSku.getTaxAndDeposit() );
		skuItem.setSavingString( qsSku.getSavingString() );

		Quantity q = qsSku.getQuantity();
		q.setQuantity( currentQuantity );
		skuItem.setQuantity( q );

		List<SalesUnit> su = qsSku.getSalesUnit();
		if ( currentSalesUnit != null && skuCode.equals( selectedSku ) ) {
	    	for( SalesUnit s : su ) {
				s.setSelected( s.getId().equals( currentSalesUnit ) );
	    	}
		}
		skuItem.setSalesUnit( su );

		skuItem.setAboutPriceText( qsSku.getAboutPriceText() );

		return skuItem;
	}


}

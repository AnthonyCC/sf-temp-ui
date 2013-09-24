package com.freshdirect.webapp.taglib.cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.common.pricing.CharacteristicValuePrice;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfigurableI;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.DomainValue;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SkuModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.BaseJsonServlet;
import com.freshdirect.webapp.quickshop.contentmodels.QuickShopLineItem;
import com.freshdirect.webapp.taglib.cart.CartData.Quantity;
import com.freshdirect.webapp.taglib.cart.CartData.SalesUnit;
import com.freshdirect.webapp.taglib.cart.ProductConfigResponseData.Sku;
import com.freshdirect.webapp.taglib.cart.ProductConfigResponseData.VarItem;
import com.freshdirect.webapp.taglib.cart.ProductConfigResponseData.Variation;
import com.freshdirect.webapp.taglib.content.QuickShopHelper;
import com.freshdirect.webapp.util.FDURLUtil;
import com.freshdirect.webapp.util.JspMethods;


public class ProductConfigServlet extends BaseJsonServlet {

	private static final long	serialVersionUID	= 4376343201345823580L;
	
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
        
        String productId = reqData.getProductId();        
        if ( productId == null ) {
        	returnHttpError( 400, "productId not specified" );	// 400 Bad Request
        }
        
       	try {
	        // Get the ProductModel
	        ProductModel product = null;
	        
	        String categoryId = reqData.getCategoryId();
	        if ( categoryId == null ) {
	        	// get product in its primary home
	        	product = (ProductModel)ContentFactory.getInstance().getContentNodeByKey( new ContentKey(ContentType.get( "Product" ), productId) );
	        } else {
	        	// get product in specified category context
	        	product = ContentFactory.getInstance().getProductByName( categoryId, productId );
	        }
	        
	        if ( product == null ) {
	        	returnHttpError( 500, "product not found" );
	        }
	        
	        // Get the sku list
	        List<SkuModel> skus = product.getSkus();        
	    	String defaultSku = product.getDefaultSkuCode();
	        String selectedSku = reqData.getSkuCode();
	        
	        if ( selectedSku == null ) {
	        	// use default sku
	        	selectedSku = defaultSku;
	        }

	        // Configuration
        	Map<String,String> currentConfig = reqData.getConfiguration();
        	
	        // Prepare map for results
	        List<Sku> skusList = new ArrayList<Sku>(); 

	        // Create response data object
	        ProductConfigResponseData responseData = new ProductConfigResponseData();

    		responseData.setSoldBySalesUnit(product.isSoldBySalesUnits());
    		
    		responseData.setProductPageUrl( FDURLUtil.getProductURI( product, (String)null ) );
    		

        	// =============================
        	// START OF TEMPORARY SOLUTION
        	// =============================
        	// We need a lot of additional data for every product, which are already present in the quickshoplineitems
        	// As a quick fix we create a qs item with quickshophelper, grab what we need, and drop it.
        	// This should be refactored to some sensible structure.
        	// =============================
        	
	        // item with default sku, for product level attributes
        	QuickShopLineItem qsProduct = QuickShopHelper.createItemFromProduct( product, null, user, true );        	
        	responseData.setProductId( qsProduct.getProductId() );
        	responseData.setProductName( qsProduct.getProductName() );
        	responseData.setInCartAmount( qsProduct.getInCartAmount() );
        	responseData.setQuantityText( product.getQuantityText() );

        	responseData.setProductImage( product.getDetailImage().getPath() );
        	responseData.setAkaName(product.getAka());
        	responseData.setPackageDescription(product.getPackageDescription());
        	
        	try {
        		// For alcoholic and usq flags check the default sku
        		FDProduct fdProduct = FDCachedFactory.getProduct( FDCachedFactory.getProductInfo( product.getDefaultSkuCode() ) );
	        	responseData.setAlcoholic( QuickShopHelper.isAlcoholic( product, fdProduct ) );
	        	responseData.setUsq( QuickShopHelper.isUsq( product, fdProduct ) );
        	} catch (Exception ignore) {
        		LOG.info( "Failed to set alcoholic and usq flags", ignore );
				// ignore any errors
			}

        	// =============================
        	// END OF TEMPORARY SOLUTION	        	
        	// =============================

	        
	        // Collect sku data
	        for ( SkuModel sku : skus ) {
	        	String skuCode = sku.getSkuCode();
	        	
	        	Sku skuItem = new Sku();
	        	
	        	skuItem.setSkuCode( skuCode );
	        	skuItem.setAvailable( !sku.isUnavailable() );		//TODO : this is strange, verify	        	
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
	        		continue;
				}
	        	
	        	// Configuration to use
	        	Map<String,String> config = currentConfig;
	        	
	        	// Automatic configuration
	        	if ( currentConfig == null && fdProd.isAutoconfigurable( product.isSoldBySalesUnits() ) ) {	        		
	        		// try to autoconfigure
	        		FDConfigurableI autoConfigured = fdProd.getAutoconfiguration( product.isSoldBySalesUnits(), product.getQuantityMinimum() );
	        		if ( autoConfigured != null ) {	        			
	        			config = autoConfigured.getOptions();
	        		}	        		
	        	}
	        	
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
	        		List<VarItem> varOpts = new ArrayList<VarItem>();
	        		for ( FDVariationOption varOpt : fdVar.getVariationOptions() ) {
	        			VarItem v = new VarItem();
	        			String vName = varOpt.getName();
	        			v.setLabelValue( varOpt.isLabelValue() );
	        			v.setName( vName );
	        			v.setLabel( varOpt.getDescription() );	        			
	        			v.setSelected( vName.equals( config.get( varName ) ) );
	        			CharacteristicValuePrice cvp = fdProd.getPricing().findCharacteristicValuePrice(varName, vName);
	        			v.setCvp(cvp == null ? "0" : JspMethods.formatPrice( cvp.getPrice() ) );
	        			varOpts.add( v );
	        		}
	        		var.setValues( varOpts );
	        		varList.add( var );
	        	}
	        	skuItem.setVariations( varList );
	        	
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
	        	skuItem.setLabel( sb.toString() );
	        	
	        	// =============================
	        	// START OF TEMPORARY SOLUTION
	        	// =============================
	        	// We need a lot of additional data for every sku, which are already present in the quickshoplineitems
	        	// As a quick fix we create a qs item with quickshophelper, grab what we need, and drop it.
	        	// This should be refactored to some sensible structure.
	        	// =============================
	        	
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
	        	
	        	skuItem.setPrice( fdProdInf.getZonePriceInfo(user.getPricingContext().getZoneId()).getDefaultPrice() );
	        	skuItem.setWasPrice( qsSku.getWasPrice() );
	        	skuItem.setScaleUnit( qsSku.getScaleUnit() );
	        	skuItem.setTaxAndDeposit( qsSku.getTaxAndDeposit() );
	        	skuItem.setSavingString( qsSku.getSavingString() );

	        	Quantity q = qsSku.getQuantity();
	        	q.setQuantity( reqData.getQuantity() );	        	
	        	skuItem.setQuantity( q );	        	
	        	
	        	String reqSu = reqData.getSalesUnit();
	        	List<SalesUnit> su = qsSku.getSalesUnit();	        	
	        	if ( reqSu != null && skuCode.equals( reqData.getSkuCode() ) ) {
		        	for( SalesUnit s : su ) {
	        			s.setSelected( s.getId().equals( reqSu ) );
		        	}
	        	}
	        	skuItem.setSalesUnit( su );
	        	
	        	skuItem.setAboutPriceText( qsSku.getAboutPriceText() );
	        	
	        	// =============================
	        	// END OF TEMPORARY SOLUTION	        	
	        	// =============================
	        	
	        	skusList.add( skuItem );
	        }	        
	        
	        
			responseData.setSkus( skusList );
			
			Map<String,Object> productConfigResult = new HashMap<String,Object>();
			productConfigResult.put("productConfig", responseData );
			
			writeResponseData( response, productConfigResult );
			
    	} catch (Exception e) {
        	returnHttpError( 500, "Error while getting product configuration for user: " + user.getUserId() + ", productId: "+ productId, e );	// 500 Internal Server Error
		}
	}

}

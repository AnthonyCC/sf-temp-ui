package com.freshdirect.webapp.ajax.cart;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.freshdirect.affiliate.ExternalAgency;
import com.freshdirect.common.context.MasqueradeContext;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.customer.EnumATCContext;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.customer.ErpClientCode;
import com.freshdirect.event.EventLogger;
import com.freshdirect.event.FDAddToCartEvent;
import com.freshdirect.event.FDCartLineEvent;
import com.freshdirect.event.FDEditCartEvent;
import com.freshdirect.event.FDRemoveCartEvent;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDGroup;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.FDVariation;
import com.freshdirect.fdstore.FDVariationOption;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.coremetrics.CmContextUtility;
import com.freshdirect.fdstore.coremetrics.builder.AbstractShopTagModelBuilder;
import com.freshdirect.fdstore.coremetrics.builder.SkipTagException;
import com.freshdirect.fdstore.coremetrics.tagmodel.ShopTagModel;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDModifyCartLineI;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserCouponUtil;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.ecoupon.EnumCouponContext;
import com.freshdirect.fdstore.ecoupon.EnumCouponStatus;
import com.freshdirect.fdstore.ecoupon.FDCustomerCoupon;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.util.EnumSiteFeature;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.smartstore.Variant;
import com.freshdirect.smartstore.fdstore.VariantSelectorFactory;
import com.freshdirect.webapp.ajax.browse.FilteringFlowType;
import com.freshdirect.webapp.ajax.cart.data.AddToCartCouponResponse;
import com.freshdirect.webapp.ajax.cart.data.AddToCartItem;
import com.freshdirect.webapp.ajax.cart.data.AddToCartRequestData;
import com.freshdirect.webapp.ajax.cart.data.AddToCartResponseData;
import com.freshdirect.webapp.ajax.cart.data.AddToCartResponseDataItem;
import com.freshdirect.webapp.ajax.cart.data.AddToCartResponseDataItem.Status;
import com.freshdirect.webapp.ajax.cart.data.CartData;
import com.freshdirect.webapp.ajax.reorder.QuickShopHelper;
import com.freshdirect.webapp.features.service.FeaturesService;
import com.freshdirect.webapp.taglib.coremetrics.AbstractCmShopTag;
import com.freshdirect.webapp.taglib.coremetrics.CmShop5Tag;
import com.freshdirect.webapp.taglib.fdstore.FDCustomerCouponUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.display.FDCouponTag;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventFactory;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventI;
import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventType;
import com.freshdirect.webapp.unbxdanalytics.event.LocationInfo;
import com.freshdirect.webapp.unbxdanalytics.service.EventLoggerService;
import com.freshdirect.webapp.unbxdanalytics.visitor.Visitor;
import com.freshdirect.webapp.util.FDEventFactory;

/**
 *	Helper class for cart operations
 *
 *	Static, stateless utility class. 
 *
 *	Implements only simple cart operations for the cart widget and add-to-cart servlet (e.g. change quantity, remove items, add to cart),
 *	not the full set of cart operations (e.g. view cart page, checkout, ccl, etc...)
 *
 *	Implements 'pure' operations: 
 *	i.e. change quantity will not remove cartlines, only remove operation does.
 *	change quantity to zero will set quantity to the minimum and will not remove the cartline.
 *
 *	So the decision of what type of operation should happen has to be made before calling.
 *
 *  business logic parts copied from FDShoppingCartController, 2013/03 (change/remove cartlines)
 *  business logic parts copied from FDShoppingCartController, 2013/05 (add to cart)
 *  
 * @author treer
 */
public class CartOperations {
	
	private static final Logger LOG = LoggerFactory.getInstance( CartOperations.class );
	
	// Epsilon value for comparing floating point values - as we use that for quantities a very high value is used - quantities should have a granularity of about two decimal points only.
	private static final double	EPSILON	= 5E-3;	//0.005
	
	public static boolean addToCart( FDUserI user, FDCartModel cart, List<AddToCartItem> items, String serverName, AddToCartRequestData reqData, AddToCartResponseData responseData, HttpSession session, EnumEventSource evtSrc ) {
		
		// parameter validation
		if ( user == null ) {
			LOG.error( "user is null" );
			return false;
		}
		if ( cart == null ) {
			LOG.error( "cart is null" );
			return false;
		}
		if ( items == null ) {
			LOG.error( "items is null" );
			return false;
		}
		
		synchronized ( cart ) {
			// Get variant id
			String variantId = reqData.getVariantId();			
			if ( variantId == null ) {
				String siteFeature = reqData.getSiteFeature();
				if ( siteFeature != null ) {
					EnumSiteFeature siteFeat = EnumSiteFeature.getEnum(siteFeature);
					if (siteFeat != null) {
						Variant variant = VariantSelectorFactory.getSelector(siteFeat).select(user);
						if (variant != null) {
							variantId = variant.getId();							
						}
					}
				}
			}
			
			// [APPDEV-5353] UNBXD analytics
            final boolean isUNBXDAnalyticsAvailable = FeaturesService.defaultService().isFeatureActive(EnumRolloutFeature.unbxdanalytics2016, reqData.getCookies(), user);
            final Visitor visitor = Visitor.withUser(user);
            final LocationInfo loc = LocationInfo.withUrl(reqData.getRequestUrl());
			
			// Create cartlines and collect them in a list 
			List<FDCartLineI> cartLinesToAdd = new ArrayList<FDCartLineI>(items.size());

			for ( AddToCartItem item : items ) {
				AddToCartResponseDataItem responseItem = new AddToCartResponseDataItem();
				responseData.getAtcResult().add(responseItem);
				
				responseItem.setCategoryId( item.getCategoryId() );
				responseItem.setProductId( item.getProductId() );
				
				if(variantId == null){
					// try the item specific variant
					variantId = item.getVariantId();
				}
				
				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				FDCartLineI cartLine = processCartLine(user,cart,item,cartLinesToAdd,responseItem, variantId);
				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				
				if (cartLine == null) {
					continue;
				}
				
				cartLine.setSource( evtSrc ); 					
				cartLine.setAddedFromSearch(false);	 //obsolete 
				
				String pageType = item.getPageType();
				if (FilteringFlowType.SEARCH.toString().equalsIgnoreCase(pageType)){
					cartLine.setAddedFrom(EnumATCContext.SEARCH);
				
				} else if (FilteringFlowType.ECOUPON.toString().equalsIgnoreCase(pageType)){
					cartLine.setAddedFrom(EnumATCContext.ECOUPON);
				
				} else if (FilteringFlowType.PRES_PICKS.toString().equalsIgnoreCase(pageType)){
					cartLine.setAddedFrom(EnumATCContext.DDPP);
				
				} else if (FilteringFlowType.NEWPRODUCTS.toString().equalsIgnoreCase(pageType)){
					cartLine.setAddedFrom(EnumATCContext.NEWPRODUCTS);
				}
				
				cartLine.setCoremetricsPageContentHierarchy( reqData.getCoremetricsPageContentHierarchy() );
				cartLine.setCoremetricsPageId( reqData.getCoremetricsPageId() );
				cartLine.setCoremetricsVirtualCategory( reqData.getCoremetricsVirtualCategory() );
				
				cartLine.setEStoreId(user.getUserContext().getStoreContext().getEStoreId());
//				cartLine.setPlantId(user.getUserContext().getFulfillmentContext().getPlantId());

				cartLinesToAdd.add(cartLine);
				
				// [APPDEV-4558]
				if (CmContextUtility.isCoremetricsAvailable(user)) {
					populateCoremetricsShopTag( responseData, cartLine );
				}
				// [APPDEV-5353] UNBXD Analytics Events
                if (isUNBXDAnalyticsAvailable) {
                    final AnalyticsEventI event = AnalyticsEventFactory.createEvent(AnalyticsEventType.ATC, visitor, loc, null, null, cartLine);
                    
                    EventLoggerService.getInstance().log(event);
                }
			}
								
			// add collected cartlines to cart
			cart.addOrderLines(cartLinesToAdd);
			
			// log multiple add to cart events
			logAddToCart( user, cartLinesToAdd, evtSrc, serverName );
	
			// Save
			saveUserAndCart( user, cart );				

			// ecoupons status - after add to cart 
			populateECouponsStatus( responseData, user, cart, cartLinesToAdd, session );
			
		}
		
		return true;

	}

	private static void populateCoremetricsShopTag( AddToCartResponseData responseData, FDCartLineI cartLine ) {
		// 		COREMETRICS SHOP5
		try {
			
			cartLine.refreshConfiguration();
			
			ShopTagModel cmTag = AbstractShopTagModelBuilder.createTagModel( cartLine, cartLine.getProductRef(), false );			
			responseData.addCoremetrics( cmTag.toStringList() );

			List<String> cmFinalTag = new ArrayList<String>(1);
			cmFinalTag.add( AbstractCmShopTag.DISPLAY_SHOPS );
			responseData.addCoremetrics( cmFinalTag );
			
		} catch ( SkipTagException ignore ) {
			LOG.warn( "Failed to generate coremetrics data", ignore );
		} catch ( FDResourceException ignore ) {
			LOG.warn( "Failed to generate coremetrics data", ignore );
		} catch ( FDInvalidConfigurationException ignore ) {
			LOG.warn( "Failed to generate coremetrics data", ignore );
		}
	}

	public static void populateCoremetricsShopTag( CartData responseData, List<FDCartLineI> cartLines, FDCartModel cart ) {
		// 		COREMETRICS SHOP5
		String cmScript = null;
		try {
			CmShop5Tag cmTag = new CmShop5Tag();
			cmTag.setWrapIntoScriptTag( false );
			cmTag.setOutStringVar( "cmStr" );
			cmTag.setCart( cart );
			cmTag.setExplicitList( cartLines );
			cmScript = cmTag.getTagJs();
		} catch (SkipTagException ignore) {
			// ignore exception, cmScript is already null
		}
		if ( cmScript != null && cmScript.trim().isEmpty() ) {
			cmScript = null;
		}
		responseData.setCoremetricsScript( cmScript );		
	}
	
	
	private static void populateECouponsStatus(AddToCartResponseData responseData, FDUserI user, FDCartModel cart, List<FDCartLineI> cartLinesToAdd, HttpSession session) {
		
		// 		E-COUPONS status
		try {
			
			FDCustomerCouponUtil.evaluateCartAndCoupons( session );

			Map<String, AddToCartCouponResponse> coupons = responseData.getCouponStatus();
			
			for ( FDCartLineI cartLine : cartLinesToAdd ) {
				
				// populate Ecoupons data
				FDCustomerCoupon coupon = user.getCustomerCoupon( cartLine, EnumCouponContext.PRODUCT );
				if ( coupon != null ) {
					
					String couponId = coupon.getCouponId();
					
					// DO NOT USE the coupon status in the FDCustomerCoupon object!
					// We need a mocked version to display for add-to-cart:
					EnumCouponStatus couponStatus = FDUserCouponUtil.getCouponStatus(coupon, cart.getRecentlyAppliedCoupons());
					coupons.put( couponId, new AddToCartCouponResponse(cartLine.getAtcItemId(), generateFormattedCouponMessage( coupon, couponStatus )));
				}
			}
			
		} catch (Exception e) {
			LOG.error( "Failed to populate ecoupons status messages after add to cart!", e );
		}
	}

	public static String generateFormattedCouponMessage( FDCustomerCoupon coupon, EnumCouponStatus status ) {
		if (null == status || !status.isDisplayMessage() ) {
			return "";
		}
		
		FDCouponTag manFdCouponTag = new FDCouponTag();
		manFdCouponTag.setCoupon(coupon);
		manFdCouponTag.setCouponStatusText( status.getDescription() );
		manFdCouponTag.initContent( null );
		
		return manFdCouponTag.getStatusTextHtml();		
	}

	public static void changeQuantity( FDUserI user, FDCartModel cart, FDCartLineI cartLine, double newQ, String serverName ) {
		
		// parameter validation
		if ( user == null ) {
			LOG.error( "user is null" );
			return;
		}
		if ( cart == null ) {
			LOG.error( "cart is null" );
			return;
		}
		if ( cartLine == null ) {
			LOG.error( "cartLine is null" );
			return;
		}
		
		// Fetch additional data
		ProductModel product = cartLine.lookupProduct();
		if ( product == null ) {
			LOG.error( "Failed to get product node for " + cartLine.getCategoryName() + " / " + cartLine.getProductName() + ", skipping." );
			return;
		}
		
		synchronized ( cart ) {
			
			// ====================
			// Quantity calculation
			// ====================
			
			double min = product.getQuantityMinimum();
			double max = extractMaximumQuantity(user, cartLine.getOrderLineId(), product);
			double inc = product.getQuantityIncrement();
			double oldQ = cartLine.getQuantity();
			
			if ( newQ <= min ) {
				newQ = min;
			} else {
				double totalQty = cart.getTotalQuantity( product );
				if ( newQ + totalQty - oldQ > max ) {
					newQ = max - totalQty + oldQ;
				}
				newQ = Math.floor( ( newQ - min ) / inc ) * inc + min;
			}
			
			if ( newQ <= 0 ) {
				// Strange, something is not right, skip
				LOG.warn( "new quantity <= 0" );
				return;
			}
	
			if ( Math.abs( newQ - oldQ ) < EPSILON ) {
				// Quantity did not change after all, do nothing
				LOG.info( "cartline quantity did not change" );
				return;
			}
	
			
			// ===============
			// Modify cartline
			// ===============
			
			if ( !(cartLine instanceof FDModifyCartLineI) ) {			
				// Normal cart
				
				cartLine.setQuantity( newQ );
				logEditCart( user, cartLine, product, serverName );
				
			} else {			
				// Modify order cart
	
				// how much we're adding/removing
				double deltaQty = newQ - oldQ;
	
				if ( Math.abs(deltaQty) < EPSILON ) {
					// nothing to do
					return;
					
				} else if ( deltaQty < 0 ) {
					// need to remove some
					cartLine.setQuantity( newQ );
					logEditCart( user, cartLine, product, serverName );
					
				} else {
					// deltaQty > 0, see how much can we add to this orderline
					
					double origQuantity = ((FDModifyCartLineI) cartLine).getOriginalOrderLine().getQuantity();
					double origDiff = origQuantity - oldQ;
	
					if ( origDiff > 0 ) {
						double addToLine = Math.min( origDiff, deltaQty );
						cartLine.setQuantity( oldQ + addToLine );
						logEditCart( user, cartLine, product, serverName );
						deltaQty -= addToLine;
					}
	
					// add a new orderline for rest of the difference, if any
					if ( deltaQty > 0 ) {
	
						FDCartLineI newLine = cartLine.createCopy();
						newLine.setUserContext(  user.getUserContext());					
						newLine.setQuantity( deltaQty );
						
						try {
							OrderLineUtil.cleanup( newLine );
						} catch ( FDInvalidConfigurationException e ) {
							LOG.error( "Orderline [" + newLine.getDescription() +"] configuration no longer valid", e );
							return;
						} catch ( FDResourceException e ) {
							LOG.error( "Failed to add new orderline [" + newLine.getDescription() +"]", e );
							return;
						}
						
						cart.addOrderLine( newLine );
						logAddToCart( user, newLine, product, serverName );
					}
				}
			}
			
			saveUserAndCart( user, cart );
		}		
	}
	
	
	public static void changeSalesUnit( FDUserI user, FDCartModel cart, FDCartLineI cartLine, String newSalesUnit, String serverName ) {
		
		// parameter validation
		if ( user == null ) {
			LOG.error( "user is null" );
			return;
		}
		if ( cart == null ) {
			LOG.error( "cart is null" );
			return;
		}
		if ( cartLine == null ) {
			LOG.error( "cartLine is null" );
			return;
		}
		if ( newSalesUnit == null ) {
			LOG.error( "newSalesUnit is null" );
			return;
		}
		
		// Fetch additional data
		ProductModel product = cartLine.lookupProduct();
		if ( product == null ) {
			LOG.error( "Failed to get product node for " + cartLine.getCategoryName() + " / " + cartLine.getProductName() + ", skipping." );
			return;
		}
		
		FDProduct fdProduct = cartLine.lookupFDProduct();				
		if ( fdProduct == null ) {
			LOG.error( "Failed to get fdproduct for " + cartLine.getCategoryName() + " / " + cartLine.getProductName() + ", skipping." );
			return;
		}

		synchronized ( cart ) {
		
			// =================
			// Modify sales unit
			// =================
	
			if ( !newSalesUnit.equals( cartLine.getSalesUnit() ) ) {
	
				if ( fdProduct.getSalesUnit( newSalesUnit ) == null ) {
					LOG.error( "Sales unit " + newSalesUnit + " is not valid" );
					return;
				}
	
				cartLine.setSalesUnit( newSalesUnit );
				logEditCart( user, cartLine, product, serverName );
				
				saveUserAndCart( user, cart );
			}
		}		
	}	
	
	
	public static void removeCartLine( FDUserI user, FDCartModel cart, FDCartLineI cartLine, String serverName ) {

		// parameter validation
		if ( user == null ) {
			LOG.error( "user is null" );
			return;
		}
		if ( cart == null ) {
			LOG.error( "cart is null" );
			return;
		}
		if ( cartLine == null ) {
			LOG.error( "cartLine is null" );
			return;
		}
		
		// Fetch additional data
		ProductModel product = cartLine.lookupProduct();
		if ( product == null ) {
			LOG.error( "Failed to get product node for " + cartLine.getCategoryName() + " / " + cartLine.getProductName() + ", skipping." );
			return;
		}

		synchronized ( cart ) {
			
			cart.removeOrderLine( cartLine );
			
			logRemoveFromCart( user, cartLine, product, serverName );
	
			saveUserAndCart( user, cart );
		}
	}

	
	public static void saveUserAndCart( FDUserI user, FDCartModel cart ) {
		
		synchronized ( cart ) {			
			try {
				cart.refreshAll( true );
			} catch ( FDResourceException e ) {
				LOG.error( "Cart refresh failed", e );
			} catch ( FDInvalidConfigurationException e ) {
				LOG.error( "Cart refresh failed, invalid configuration", e );
			}
			
			cart.sortOrderLines();		
			user.updateUserState();
			
			if (user.getIdentity() != null && user.getIdentity().getErpCustomerPK() != null) {
				QuickShopHelper.emptyQuickShopCaches(user.getIdentity().getErpCustomerPK());
			}
			
			if(!(cart instanceof FDModifyCartModel)){
				try {
					if ( user instanceof FDUser ) {
						FDCustomerManager.storeUser( (FDUser)user );
					} else if ( user instanceof FDSessionUser ) {
						FDCustomerManager.storeUser( ((FDSessionUser)user).getUser() );
					}
				} catch (FDResourceException e) {
					LOG.error( "Store user (save cart) failed", e );
				}				
			}
		}
	}


	
	private static void logEditCart( FDUserI user, FDCartLineI cartLine, ProductModel product, String serverName ) {		
		FDCartLineEvent event = new FDEditCartEvent();
		event.setEventType( FDEventFactory.FD_MODIFY_CART_EVENT );
		logCartEvent( event, user, cartLine, product, EnumEventSource.CART, serverName );
	}
	
	private static void logAddToCart( FDUserI user, FDCartLineI cartLine, ProductModel product, String serverName ) {
		FDCartLineEvent event = new FDAddToCartEvent();
		event.setEventType( FDEventFactory.FD_ADD_TO_CART_EVENT );
		logCartEvent( event, user, cartLine, product, EnumEventSource.CART, serverName );
	}
	
	private static void logAddToCart( FDUserI user, List<FDCartLineI> cartLines, EnumEventSource eventSource, String serverName ) {		
		for ( FDCartLineI cartLine : cartLines ) {
			FDCartLineEvent event = new FDAddToCartEvent();
			event.setEventType( FDEventFactory.FD_ADD_TO_CART_EVENT );
			ProductModel product = cartLine.lookupProduct();
			logCartEvent( event, user, cartLine, product, eventSource, serverName );
		}
	}	
	
	private static void logRemoveFromCart( FDUserI user, FDCartLineI cartLine, ProductModel product, String serverName ) {
		FDCartLineEvent event = new FDRemoveCartEvent();
		event.setEventType( FDEventFactory.FD_REMOVE_CART_EVENT );
		logCartEvent( event, user, cartLine, product, EnumEventSource.CART, serverName );
	}	
	
	private static void logCartEvent( FDCartLineEvent event, FDUserI user, FDCartLineI cartLine, ProductModel product, EnumEventSource eventSource, String serverName ) {		
		try {
			
			FDIdentity identity = user.getIdentity();
			if ( identity != null ) {
				event.setCustomerId( identity.getErpCustomerPK() );
			}
			
			event.setServer( serverName );
			event.setCookie( user.getCookie() );
			event.setTimestamp( new Date() );		
			event.setUrl( null );
			event.setQueryString( null );
			
			EnumTransactionSource src = user.getApplication();
			event.setApplication( src != null ? src.getCode() : EnumTransactionSource.WEBSITE.getCode() );
			
			event.setCartlineId( cartLine.getCartlineId() );		
			event.setDepartment( product.getDepartment().getContentName() );
			event.setCategoryId( cartLine.getCategoryName() );
			event.setProductId( cartLine.getProductName() );		
			event.setSkuCode( cartLine.getSkuCode() );		
			event.setQuantity( String.valueOf( cartLine.getQuantity() ) );		
			event.setSalesUnit( cartLine.getSalesUnit() );		
			event.setConfiguration( cartLine.getConfigurationDesc() != null ? cartLine.getConfigurationDesc() : "");
			event.setOriginatingProduct( cartLine.getOriginatingProductId() );			
			event.setSource( eventSource );
			
			// Ymal attributes? Should we keep them? Probably not.
			//	event.setYmalSet( cartLine.getYmalSetId() ); 
			//	event.setYmalCategory( cartLine.getYmalCategoryId() ); 
			
			event.setVariantId( cartLine.getVariantId() );
			
			// [APPDEV-3683] Duplicate value of external agency field (e.g. Foodily) into tracking code
			if (FDEventFactory.FD_ADD_TO_CART_EVENT.equals(event.getEventType())
					&& event.getTrackingCode() == null
					&& cartLine.getExternalAgency() != null) {
				event.setTrackingCode( cartLine.getExternalAgency().toString() );
			}

			EventLogger.getInstance().logEvent(event);
		
		} catch (Exception e) {
			LOG.error( "Error while logging cart event", e );
		}
	}	
	
	
	// =========================================== Methods for add to cart processing =========================================== 
	
	
	
	public static ProductModel getProductModelFromAddToCartItem (AddToCartItem item, AddToCartResponseDataItem responseItem){
		
		String skuCode =  item.getSkuCode();
		responseItem.setItemId(item.getAtcItemId());
		if ( skuCode == null || "".equals(skuCode)) {
			// Missing skuCode
			responseItem.setStatus( Status.ERROR );
			responseItem.setMessage("Not available (SKU not specified)");
			return null;
		}
		
		String productId = item.getProductId();
		String categoryId = item.getCategoryId();
		
		//TODO : what is the extra trick with wine categories?		
		//		if (request.getParameter(paramWineCatId) != null)
		//			catName = request.getParameter(paramWineCatId);
		//		else
		//			catName = request.getParameter(paramCatId);
		
		
		ProductModel prodNode = null;		
		try {
			if ( productId != null && categoryId != null ) {
				prodNode = ContentFactory.getInstance().getProductByName( categoryId, productId );
			}			
			if ( prodNode == null ) {
				prodNode = ContentFactory.getInstance().getProduct(skuCode);
			}			
		} catch (FDSkuNotFoundException ex) {
			responseItem.setStatus( Status.ERROR );
			responseItem.setMessage("Not available (SKU not found)");
			return null;
		}
		
		if ( prodNode == null ) {
			responseItem.setStatus( Status.ERROR );
			responseItem.setMessage("Not available (SKU not found)");
			return null;
		}
		
		if ( prodNode.getSku( skuCode ).isUnavailable() ) {
			responseItem.setStatus( Status.ERROR );
			responseItem.setMessage("Product not available (unavailable sku)");
			return null;
		}
		
		return prodNode;
	}
	
	public static FDProduct getFDProductFromAddToCartItem(AddToCartItem item, AddToCartResponseDataItem responseItem){
		
		String skuCode = item.getSkuCode();
		FDProduct product = null;
		try {
			product = FDCachedFactory.getProduct(FDCachedFactory.getProductInfo(skuCode));
		} catch (FDResourceException fdre) {
			responseItem.setStatus( Status.ERROR );
			responseItem.setMessage("Not available (system error)");
			return null;
		} catch (FDSkuNotFoundException fdsnfe) {
			responseItem.setStatus( Status.ERROR );
			responseItem.setMessage("Not available (SKU not found)");
			return null;
		}
		return product;
	}
	
	/**
	 * 	Cartline processing: lookups, validation, post-processing. 
	 * 
	 * Salvaged from FDShoppingCartControllerTag.
	 * 
	 * No support for 'original cartline scenario'.
	 * 
	 * Inputs (AddToCartItem) : 
	 * 	sku code is always required.
	 *  productId, categoryId, recipeId are optional to provide context. 
	 *   
	 * @param user
	 * @param cart
	 * @param item
	 * @param cartLinesToAdd
	 * @param messages
	 * @return the created cartline or null on error.
	 */
	private static FDCartLineI processCartLine(FDUserI user, FDCartModel cart, AddToCartItem item, List<FDCartLineI> cartLinesToAdd, AddToCartResponseDataItem responseItem, String variantId ) {

		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//									PRODUCTMODEL
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		ProductModel prodNode = getProductModelFromAddToCartItem(item, responseItem);
		if (prodNode == null){
			return null;
		}

		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//									FDPRODUCT
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		FDProduct product = getFDProductFromAddToCartItem(item, responseItem);
		if (product == null){
			return null;
		}
		
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//									QUANTITY & SALESUNIT
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		// set in cart amount in case of error message as well
		responseItem.setInCartAmount( cart.getTotalQuantity( prodNode ) );
		
		FDSalesUnit salesUnit = null;
		double quantity = 0.0;
		double maximumQuantity = 0.0;

		String requestedUnit = item.getSalesUnit();
		
		if (requestedUnit == null || "".equals(requestedUnit) ) {
			// no sales unit, this is an error			
			responseItem.setStatus( Status.ERROR );
			responseItem.setMessage( "Missing sales unit" );
			return null;
		}
		
		salesUnit = product.getSalesUnit(requestedUnit);
		
		if (salesUnit == null) {
			// skip this item
			responseItem.setStatus( Status.ERROR );
			responseItem.setMessage( "Invalid sales unit" );
			return null;
		}
		
		quantity = extractQuantity( item );

		if ( quantity == 0.0 ) {
			// skip this item
			responseItem.setStatus( Status.ERROR );
			responseItem.setMessage( "Missing quantity" );
			return null;
		}
		
		maximumQuantity = extractMaximumQuantity(user, item.getLineId(), prodNode);
		
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//									VALIDATION
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		String errorMessage;
		
		MasqueradeContext masqueradeContext = user.getMasqueradeContext();
		if (masqueradeContext!=null){
			errorMessage = masqueradeContext.validateAddToCart(item.getLineId());
			if (errorMessage != null) {
				responseItem.setStatus( Status.ERROR );
				responseItem.setMessage(errorMessage);
				return null;
			}
		}
		
        errorMessage = validateQuantity(cart, getCartlinesQuantity(prodNode, cartLinesToAdd), prodNode, quantity, 0, maximumQuantity);
		
		if (errorMessage != null) {
			responseItem.setStatus( Status.ERROR );
			responseItem.setMessage(errorMessage);
			return null;
		}
		
		errorMessage = validateConfiguration(product,item);

		if (errorMessage != null) {
			responseItem.setStatus( Status.ERROR );
			responseItem.setMessage(errorMessage);
			return null;
		}

		// TODO: add 'agree to terms' warning/error		
//		LOGGER.debug("Consented " + request.getParameter("consented" + suffix));
//		if (prodNode.hasTerms() && !"true".equals(request.getParameter("consented" + suffix))) {
//			LOGGER.debug("ADDING ERROR, since consented" + suffix + "=" + request.getParameter("consented" + suffix));
//			if (!"yes".equalsIgnoreCase(request.getParameter("agreeToTerms"))) {
//				result .addError(new ActionError("agreeToTerms", "Product terms"));
//			}
//		}

		
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//									ZONE & GROUP PRICING
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		//String pricingZoneId = user.getPricingZoneId();
		//ZoneInfo pricingZone=user.getUserContext().getPricingContext().getZoneInfo();
		
		// TODO : anything to do for group pricing ?
		
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//									MAIN PROCESSING
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		FDCartLineI theCartLine = processSimple(prodNode, product, quantity, salesUnit, null, variantId, user.getUserContext(), null, item.getConfiguration());
		
		if ( theCartLine == null ) {
			responseItem.setStatus( Status.ERROR );
			responseItem.setMessage("Not available (Processing error)");
			return null;
		}
		
		// add to cart id tracking
		theCartLine.setAtcItemId(item.getAtcItemId());
		
		//add Avalara taxcode to cartItem
		theCartLine.setTaxCode(product.getTaxCode());
		
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//									RECIPE SOURCE
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		// recipe source tracking
		String recipeId = item.getRecipeId();
		
		if (recipeId != null) {
			theCartLine.setRecipeSourceId(recipeId);
			
			//TODO: request notification ??? something recipe specific stuff...
			boolean requestNotification = false; 
			theCartLine.setRequestNotification(requestNotification);
		}		
		
		theCartLine.setExternalAgency(ExternalAgency.safeValueOf(item.getExternalAgency()));
		theCartLine.setExternalSource(item.getExternalSource());
		theCartLine.setExternalGroup(item.getExternalGroup());
		
		
		

		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//									POST-PROCESSING
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		//					+++ DISCOUNTS +++		
		// Get the original discount applied flag if available
		// TODO : how does this work if we have no original cartline?
		boolean discountApplied = false;
		theCartLine.setDiscountFlag(discountApplied);
		
		
		
		//					+++ SMART-STORE +++		
		// TODO : add smart-store specific tracking
		//			theCartLine.setYmalCategoryId(catId);
		//			theCartLine.setYmalSetId(ymalSetId);			
		theCartLine.setSavingsId(variantId);

		//					+++ ORIGINAL ID +++		
		// TODO : originating/original ID-s ?
		//			theCartLine.setOriginatingProductId(originatingProductId);
		theCartLine.setOrderLineId(item.getLineId());
		
		
		//				+++ CARTON NUMBER +++
		// TODO: Maybe this will be needed later? (for CRM)
		// 			theCartLine.setCartonNumber(cartonNumber);
		

		//				+++ DESCRIBE +++		
		try {
			OrderLineUtil.describe(theCartLine);
		} catch (FDInvalidConfigurationException e) {
			responseItem.setStatus( Status.ERROR );
			responseItem.setMessage("Warning - Invalid configuration: " + e.getMessage() );
		}

		responseItem.setInCartAmount( calculateInCartAmount( prodNode, cartLinesToAdd, cart, quantity ) );
		responseItem.setCartlineId( theCartLine.getRandomId() );
		
		if(responseItem.getMessage()==null){
			responseItem.setStatus( Status.SUCCESS );
			responseItem.setMessage("Added to Cart");
		}
		return theCartLine;
	}
	
	public static double calculateInCartAmount( ProductModel product, List<FDCartLineI> cartLinesToAdd, FDCartModel cart, double quantity ) {

		double amount = getCartlinesQuantity( product, cartLinesToAdd ); // items not yet added
		amount += cart.getTotalQuantity( product );
		amount += quantity; // this item

		return amount;
	}
	
	/**
	 * Core processing. This creates the actual cartline.
	 * 
	 * Salvaged from FDShoppingCartControllerTag.
	 * 
	 * Support for 'original cartline scenario' is preserved, but not used.
	 * 
	 * @param prodNode
	 * @param product
	 * @param quantity
	 * @param salesUnit
	 * @param origCartLineId
	 * @param variantId
	 * @param pZoneId
	 * @param group
	 * @param varMap
	 * @return
	 */
	private static FDCartLineI processSimple(ProductModel prodNode,FDProduct product, double quantity, FDSalesUnit salesUnit,	String origCartLineId, String variantId, UserContext userCtx , FDGroup group,Map<String,String> varMap) {
		
		// Does not work with null...
		if ( varMap == null ) {
			varMap = new HashMap<String, String>();
		}
				
		//
		// make the order line and add it to the cart
		//
		FDCartLineModel cartLine = null;
		if (origCartLineId == null || origCartLineId.length() == 0) {
			/*
			 * This condition is true whenever there is a new item added to the
			 * cart.
			 */
			cartLine = new FDCartLineModel(new FDSku(product), prodNode,
					new FDConfiguration(quantity, salesUnit .getName(), varMap), 
					variantId, userCtx);
		} else {
			/*
			 * When an existing item in the cart is modified, reuse the same
			 * cartlineId instead of generating a new one.
			 */
			List<ErpClientCode> clientCodes = Collections.emptyList();
			cartLine = new FDCartLineModel(new FDSku(product), prodNode,
					new FDConfiguration(quantity, salesUnit .getName(), varMap), 
					origCartLineId, null, false, variantId, userCtx, clientCodes);
			//Any group info from original cartline is moved to new cartline on modify.
			cartLine.setFDGroup(group);
		
		}

		return cartLine;
	}
	
	
	
	/**
	 * Validates item quantity in cart, returns an error message string, or null if OK.
	 * 
	 * Salvaged from FDShoppingCartControllerTag.
	 * 
	 * @param user
	 * @param cart
	 * @param originalQuantity	total quantity sum of existing line items for this product
	 * @param prodNode
	 * @param quantity
	 * @param originalLineQuantity quantity of this cartline (for modify cartline scenario)
	 * @param maximumQuantity
	 * @return
	 */
    private static String validateQuantity(FDCartModel cart, double originalQuantity, ProductModel prodNode, double quantity, double originalLineQuantity, double maximumQuantity) {
        String errorMessage = null;
        DecimalFormat formatter = new DecimalFormat("0.##");
        if (quantity < prodNode.getQuantityMinimum()) {
            errorMessage = "FreshDirect cannot deliver less than " + formatter.format(prodNode.getQuantityMinimum()) + " " + prodNode.getFullName();
        }

        if ((quantity - prodNode.getQuantityMinimum()) % prodNode.getQuantityIncrement() != 0) {
            errorMessage = "Quantity must be an increment of " + formatter.format(prodNode.getQuantityIncrement());
        }

        if (originalQuantity + cart.getTotalQuantity(prodNode) + quantity - originalLineQuantity > maximumQuantity) {
            errorMessage = "Please note: there is a limit of " + formatter.format(maximumQuantity) + " per order of " + prodNode.getFullName();
        }

        return errorMessage;
    }

	/**
	 * Validates configuration, returns an error message string, or null if OK.
	 * 
	 * Salvaged from FDShoppingCartControllerTag.
	 * 
	 */
	public static String validateConfiguration( FDProduct product, AddToCartItem item) {
		//
		// walk through the variations to see what's been set and try to build a
		// variation map
		//
		
		if(null==item){
			return null;
		}
		
		Map<String,String> varMap = item.getConfiguration();
		FDVariation[] variations = product.getVariations();
		if((null == variations || variations.length <=0) && (null != varMap && !varMap.isEmpty())){
			item.setConfiguration(Collections.EMPTY_MAP);//clear the configuration from the item, if the product doesn't have any configuration options.
		}
		
		
		for (int i = 0; i < variations.length; i++) {
			FDVariation variation = variations[i];
			FDVariationOption[] options = variation.getVariationOptions();

			String optionName = varMap.get( variation.getName() );

			if (options.length == 1) {
				//
				// there's only a single option, pick that
				//
				varMap.put(variation.getName(), options[0].getName());

			} else if (((optionName == null) || "".equals(optionName)) && variation.isOptional()) {
				//
				// user didn't select anything for an optional variation, pick
				// the SELECTED option for them
				//
				String selected = null;
				for (int j = 0; j < options.length; j++) {
					if (options[j].isSelected())
						selected = options[j].getName();
				}
				varMap.put(variation.getName(), selected);
			} else if (optionName != null && !"".equals(optionName)) {
				//
				// validate & add the option the user selected
				//
				boolean validOption = false;
				for (int j = 0; j < options.length; j++) {
					if (optionName.equals(options[j].getName())) {
						validOption = true;
						break;
					}
				}
				if (!validOption) {
					return "Please select " + variation.getDescription();
				}
			} else {
				//
				// user didn't select anything for a required variation, alert
				// them
				//
				return "Please select " + variation.getDescription();
			}
		}
		return null;
	}
	
	/**
	 * Deduce the quantity that has already been processed but not yet added to
	 * the cart.
	 * 
	 * Salvaged from FDShoppingCartControllerTag.
	 * 
	 * @param product
	 * @return total quantity of product already processed
	 */
	private static double getCartlinesQuantity(ProductModel product, List<FDCartLineI> cartLinesToAdd) {
		String productId = product.getContentName();
		double sum = 0;
		for (Iterator<FDCartLineI> i = cartLinesToAdd.iterator(); i.hasNext();) {
			FDCartLineI line = i.next();
			if (productId.equals(line.getProductName())) {
				sum += line.getQuantity();
			}
		}
		return sum;
	}

	public static double extractQuantity( AddToCartItem item ) {
		double quantity = 0.0;
		try {
			quantity = Double.parseDouble( item.getQuantity() );
		} catch (NumberFormatException ignore) {
		} catch (NullPointerException ignore) {
		}
		
		return quantity;
	}
	
    public static double extractMaximumQuantity(FDUserI user, String lineId, ProductModel prodNode) {
        double maximumQuantity = 0.0;
        MasqueradeContext context = user.getMasqueradeContext();
        if (context != null && context.isMakeGood() && context.containsMakeGoodOrderLineIdQuantity(lineId)) {
            maximumQuantity = context.getMakeGoodOrderLineIdQuantity(lineId);
        } else {
            maximumQuantity = user.getQuantityMaximum(prodNode);
        }
        return maximumQuantity;
    }

}
package com.freshdirect.webapp.taglib.cart;

import java.util.Date;

import org.apache.log4j.Logger;

import com.freshdirect.common.pricing.PricingContext;
import com.freshdirect.customer.EnumTransactionSource;
import com.freshdirect.event.EventLogger;
import com.freshdirect.event.FDAddToCartEvent;
import com.freshdirect.event.FDCartLineEvent;
import com.freshdirect.event.FDEditCartEvent;
import com.freshdirect.event.FDRemoveCartEvent;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDModifyCartLineI;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.framework.event.EnumEventSource;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.util.FDEventFactory;

/**
 *	Helper class for cart operations
 *
 *	Static, stateless utility class. 
 *
 *	Implements only simple cart operations for the cart widget (e.g. change quantity, remove items),
 *	not the full set of cart operations (e.g. view cart page, checkout, ccl, etc...)
 *
 *	Implements 'pure' operations: 
 *	i.e. change quantity will not remove cartlines, only remove operation does.
 *	change quantity to zero will set quantity to the minimum and will not remove the cartline.
 *
 *	So the decision of what type of operation should happen has to be made before calling.
 *
 *  business logic parts copied from FDShoppingCartController, 2013/03
 * 
 * @author treer
 */
public class CartOperations {
	
	// TODO : error handling !

	private static final Logger LOG = LoggerFactory.getInstance( CartOperations.class );
	
	
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
			double max = user.getQuantityMaximum( product );
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
	
			if ( newQ == oldQ ) {
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
	
				if ( deltaQty == 0 ) {
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
						newLine.setPricingContext( new PricingContext( user.getPricingZoneId() ) );					
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
			
			try {
				if ( user instanceof FDUser ) {
					FDCustomerManager.storeUser( (FDUser)user );
				} else if ( user instanceof FDSessionUser ) {
					FDCustomerManager.storeUser(( (FDSessionUser)user ).getUser() );
				}
			} catch (FDResourceException e) {
				LOG.error( "Store user (save cart) failed", e );
			}			
		}
	}


	
	private static void logEditCart( FDUserI user, FDCartLineI cartLine, ProductModel product, String serverName ) {		
		FDCartLineEvent event = new FDEditCartEvent();
		event.setEventType( FDEventFactory.FD_MODIFY_CART_EVENT );
		logCartEvent( event, user, cartLine, product, serverName );
	}
	private static void logAddToCart( FDUserI user, FDCartLineI cartLine, ProductModel product, String serverName ) {
		FDCartLineEvent event = new FDAddToCartEvent();
		event.setEventType( FDEventFactory.FD_ADD_TO_CART_EVENT );
		logCartEvent( event, user, cartLine, product, serverName );
	}	
	
	private static void logRemoveFromCart( FDUserI user, FDCartLineI cartLine, ProductModel product, String serverName ) {
		FDCartLineEvent event = new FDRemoveCartEvent();
		event.setEventType( FDEventFactory.FD_REMOVE_CART_EVENT );
		logCartEvent( event, user, cartLine, product, serverName );
	}	
	
	private static void logCartEvent( FDCartLineEvent event, FDUserI user, FDCartLineI cartLine, ProductModel product, String serverName ) {		
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
			
			// Should we keep original source? Seems better to set it explicitly to s.g. new... 
			//	event.setSource( cartLine.getSource() );
			event.setSource( EnumEventSource.CART );
			
			// Ymal attributes? Should we keep them? Probably not.
			//	event.setYmalSet( cartLine.getYmalSetId() ); 
			//	event.setYmalCategory( cartLine.getYmalCategoryId() ); 
			
			// variant ID is not needed as we do not add new products to cart, we only change existing cartlines
			//		event.setVariantId( cartLine.getVariantId() );
			
			EventLogger.getInstance().logEvent(event);
		
		} catch (Exception e) {
			LOG.error( "Error while logging cart event", e );
		}
	}	
}
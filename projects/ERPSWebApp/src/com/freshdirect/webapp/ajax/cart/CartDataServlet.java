package com.freshdirect.webapp.ajax.cart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDModifyCartLineI;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.ajax.BaseJsonServlet;
import com.freshdirect.webapp.ajax.analytics.service.GoogleAnalyticsDataService;
import com.freshdirect.webapp.ajax.cart.data.CartData;
import com.freshdirect.webapp.ajax.cart.data.CartRequestData;
import com.freshdirect.webapp.taglib.coremetrics.CmShop5Tag;
import com.freshdirect.webapp.util.JspMethods;

public class CartDataServlet extends BaseJsonServlet {

    private static final long serialVersionUID = -3650318272577031376L;

    private static final Logger LOG = LoggerFactory.getInstance(CartDataServlet.class);

    @Override
    protected boolean synchronizeOnUser() {
        return false; // synchronization is done on cart
    }

    @Override
    protected int getRequiredUserLevel() {
        return FDUserI.GUEST;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        process(request, response, user);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {
        process(request, response, user);
    }

    // TODO: correctly separate GET and POST calls - until then it stays combined in one method as refactored from CartDataTag.doStartTag()
    private static void process(HttpServletRequest request, HttpServletResponse response, FDUserI user) throws HttpErrorResponse {

        // Fetch server name
        String serverName = request.getServerName();

        String userId = user.getUserId();
        if (userId == null || userId.trim().equals("")) {
            userId = "[UNIDENTIFIED-USER]";
        }

        FDCartModel cart = user.getShoppingCart();
        if (cart == null) {
            // user doesn't have a cart, this is a bug, as login or site_access should put it there
            LOG.error("No cart found for user " + userId);
            returnHttpError(500, "No cart found for user " + userId); // 500 Internal Server Error
        }

        synchronized (cart) {
            // line items added to the list will be passed towards CM Shop5 tag
            List<FDCartLineI> clines2report = new ArrayList<FDCartLineI>();

	    	// Create response data object
	    	CartData cartData = new CartData();		
	
	    	// POST requests
	    	if ( "POST".equalsIgnoreCase( request.getMethod() ) ) {
	    		
	    		// Parse request data
	    		String reqJson = request.getParameter( "change" );
	    		if ( reqJson == null ) {
	    			LOG.error( "Empty POST request. Aborting." );
	    			returnHttpError( 400, "Empty POST request. Aborting." );	// 400 Bad Request
	    		}
	    		
	    		CartRequestData reqData = null;
	    		try {
	    			reqData = new ObjectMapper().readValue(reqJson, CartRequestData.class);
	    		} catch (IOException e) {
	    			LOG.error("Cannot read JSON", e);
	    			returnHttpError( 400, "Cannot read JSON" );	// 400 Bad Request
				}
	    		if ( reqData == null ) {
	    			LOG.error("Cannot read JSON");
	    			returnHttpError( 400, "Cannot read JSON" );	// 400 Bad Request
	    		}
	    		
	    		// Send back 'header'
	    		cartData.setHeader( reqData.getHeader() );
	
	        	// First iteration - do any operations that modify cart first
	        	try {
	                List<FDCartLineI> cartLines = cart.getOrderLines();
	                if ( cartLines == null ) {
	                	LOG.error( "Orderlines in cart is a null object. Aborting. User:" + userId );
	                	returnHttpError( 500, "Orderlines in cart is a null object. Aborting." );	// 500 Internal Server Error
	                }
	                
	                Map<Integer,CartRequestData.Change> changes = reqData.getData();
	                
	    			for ( FDCartLineI cartLine : new ArrayList<FDCartLineI>( cartLines ) ) {    				
	    				Integer id = cartLine.getRandomId();    				
	    				CartRequestData.Change change = changes.get( id );
	    				
	    				double oldQuantity = cartLine.getQuantity();
	    				
	    				// No change for this item
	    				if ( change == null )
	    					continue;
	    				
	    				// Do the change - 3 types of changes		//TODO validation!
	    				String chType = change.getType();
	    				if ( CartRequestData.Change.CHANGE_QUANTITY.equals( chType ) ) {
	    					double qu = Double.parseDouble( (String)change.getData() );
	    					CartOperations.changeQuantity( user, cart, cartLine, qu, serverName );
	    					
	    					clines2report.add( cartLine ); // <-- it will be reported!
                            cartData.setGoogleAnalyticsData(
                                    GoogleAnalyticsDataService.defaultService().populateAddToCartGAData(cartLine, Double.toString(cartLine.getQuantity() - oldQuantity)));
	    				} else if ( CartRequestData.Change.CHANGE_SALESUNIT.equals( chType ) ) {
	    					String su = (String)change.getData();
	    					CartOperations.changeSalesUnit( user, cart, cartLine, su, serverName );
	    					
	    					clines2report.add( cartLine ); // <-- it will be reported!
	    				} else if ( CartRequestData.Change.REMOVE.equals( chType ) ) {
	    					CartOperations.removeCartLine( user, cart, cartLine, serverName );
                            cartData.setGoogleAnalyticsData(GoogleAnalyticsDataService.defaultService().populateCartLineChangeGAData(cartLine, "-" + Double.toString(oldQuantity)));
	    				}
	    			}
	    			
	    			// populate coremetrics data
                    CmShop5Tag cmTag = new CmShop5Tag();
                    cmTag.setSession(request.getSession());
	    			CartOperations.populateCoremetricsShopTag( cartData, clines2report, cart, cmTag);
	    			
	        	} catch (Exception e) {
	        		LOG.error("Error while modifying cart for user " + userId, e);
	        		returnHttpError( 500, "Error while modifying cart" );	// 500 Internal Server Error
	    		}
	    	}
	    	
	    	
	    	// Second iteration - collect data from resulting cart contents
	        try {
	        	
	        	// Fetch new cartlines
	        	List<FDCartLineI> cartLines = cart.getOrderLines();
	            if ( cartLines == null ) {
	            	LOG.error( "Orderlines in cart is a null object. Aborting. User:" + userId );
	            	returnHttpError( 500, "Orderlines in cart is a null object. Aborting." );	// 500 Internal Server Error
	            }
	            
	            // Fetch recent cartline ids
	            SortedSet<Integer> recentIds = new TreeSet<Integer>();
	            for ( FDCartLineI rc : cart.getRecentOrderLines() ) {
	            	recentIds.add( new Integer( rc.getRandomId() ) );
	            }            
	        	
	            // Customer credit history - it may not be needed but the old sidecart had this
	            // Siva: On Mar 31 we evaluated this call and found that it is not needed for the side cart implementation
				/*FDCustomerCreditHistoryModel creditHistory = null;
				try {
					FDIdentity identity = user.getIdentity();
					if ( identity != null ) {
						creditHistory = FDCustomerManager.getCreditHistory( identity );
					}
				} catch (FDResourceException e) {
					LOG.error( "Failed to get credit history for " + userId, e );
				}
				if ( creditHistory == null ) {
					LOG.error( "Failed to get credit history for " + userId );
				} else {
					double remaining = creditHistory.getRemainingAmount();
		            if ( remaining > 0.00 ) {
		    			cartData.setRemainingCredits( remaining );
		            }	            
				}*/
				
				
				// =======================================================================================================================================
				
				Map<String,List<CartData.Item>> sectionMap = new HashMap<String,List<CartData.Item>>();
				Map<String,String> sectionHeaderImgMap = new HashMap<String,String>();
				double itemCount = 0;
				
				for ( FDCartLineI cartLine : cartLines ) {
					
					// Lookup CMS product node
					ProductModel productNode = cartLine.lookupProduct();
					if ( productNode == null ) {
						LOG.error( "Failed to get product node for " + cartLine.getCategoryName() + " / " + cartLine.getProductName() + ", skipping." );
						continue;
					}
					
					// Lookup ERPS product node
					FDProduct fdProduct = cartLine.lookupFDProduct();				
					if ( fdProduct == null ) {
						LOG.error( "Failed to get fdproduct for " + cartLine.getCategoryName() + " / " + cartLine.getProductName() + ", skipping." );
						continue;
					}
					
					// Department description string determines the sections
					String deptDesc = cartLine.getDepartmentDesc();				
					List<CartData.Item> sectionList = sectionMap.get( deptDesc );
					if ( sectionList == null ) {
						sectionList = new ArrayList<CartData.Item>();
						sectionMap.put( deptDesc, sectionList );
					}
					if ( !sectionHeaderImgMap.containsKey( deptDesc ) ) {
						// Reverse lookup of actual department, as we only have a string description ...
						// Also for recipes we check the description string ...
						// Kind of the same ugly hack as in view cart page jsp
						String deptId = productNode.getDepartment().getContentName();
						if ( null != deptDesc && deptDesc.startsWith("Recipe: ") ) {
							deptId = "rec";
						}
						String imgUrl = "/media_stat/images/layout/department_headers/" + deptId + "_cart.gif";
						sectionHeaderImgMap.put( deptDesc, imgUrl );
					}
					
					CartData.Item item = new CartData.Item();
					
					int randomId = cartLine.getRandomId();
					item.setId( randomId );
					item.setRecent( recentIds.contains( randomId ) );
					item.setNewItem( (cart instanceof FDModifyCartModel) && !(cartLine instanceof FDModifyCartLineI) );
					
					item.setPrice( JspMethods.formatPrice( cartLine.getPrice() ) );
					item.setDescr( cartLine.getDescription() );
					item.setConfDescr( cartLine.getConfigurationDesc() );
					
					/* placeholder for marking products as free sample products
					 * do the same logic here as view cart
					 * */
					Discount tempDisc = cartLine.getDiscount();
					if (tempDisc != null && (EnumDiscountType.FREE).equals(tempDisc.getDiscountType())) {
							item.setFreeSamplePromoProduct(true);
					}
					
					
					if ( cartLine.isSoldBySalesUnits() ) {
						List<CartData.SalesUnit> sus = new ArrayList<CartData.SalesUnit>();					
						String cartlineSu = cartLine.getSalesUnit();
						
						for ( FDSalesUnit fdsu : fdProduct.getSalesUnits() ) {
							CartData.SalesUnit sue = new CartData.SalesUnit();
							String id = fdsu.getName();
							sue.setId( id );
							sue.setName( fdsu.getDescription() );
							sue.setSelected( id.equals( cartlineSu ) );
							sus.add( sue );
						}					
						item.setSu( sus );
						itemCount++;						
					} else {
						CartData.Quantity q = new CartData.Quantity();
						q.setQuantity( cartLine.getQuantity() );
						q.setqMin( productNode.getQuantityMinimum() );
						q.setqMax( user.getQuantityMaximum( productNode ) );
						q.setqInc( productNode.getQuantityIncrement() );
						item.setQu( q );
						itemCount += q.getQuantity();						
					}
					
					sectionList.add( item );
				}
				
				List<CartData.Section> sections = new ArrayList<CartData.Section>();
				for ( String sectionTitle : sectionMap.keySet() ) {
					CartData.Section section = new CartData.Section();
					section.setTitle( sectionTitle );
					
					// Set department header image url
					// continuation of the ugly hack - as in view cart page jsp
					section.setTitleImg( sectionHeaderImgMap.get( sectionTitle ) );
					
					section.setCartLines( sectionMap.get( sectionTitle ) );
					sections.add( section );
				}
				
				Collections.sort( sections, new Comparator<CartData.Section>() {
					@Override
					public int compare( CartData.Section o1, CartData.Section o2 ) {
						int compareTitles = 0;
						if(o1.getTitle().equals("FREE SAMPLE(S)")){
							return 1;
						}
						else if(o2.getTitle().equals("FREE SAMPLE(S)")){
							return -1;
						}
						else
						return o1.getTitle().compareTo( o2.getTitle() );
					}					
				});
				
				cartData.setCartSections( sections );
				
				cartData.setItemCount( itemCount );
				cartData.setSubTotal( JspMethods.formatPrice(cart.getSubTotal()) );
				double saveAmount = cart.getSaveAmount(false);
				cartData.setSaveAmount( saveAmount> 0? JspMethods.formatPrice(saveAmount) : null );
				cartData.setModifyOrder( cart instanceof FDModifyCartModel );
				
				cartData.setErrorMessage( null );
				
			} catch (Exception e) {
				LOG.error("Error while processing cart for user " + userId, e);
				returnHttpError( 500, "Error while processing cart for user " + userId );	// 500 Internal Server Error
			}    
	        
	        writeResponseData( response, cartData );
		}
	}

}

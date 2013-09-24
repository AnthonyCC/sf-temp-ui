package com.freshdirect.webapp.taglib.cart;

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
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerCreditHistoryModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDModifyCartLineI;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.webapp.taglib.fdstore.SessionName;
import com.freshdirect.webapp.util.JspMethods;

/**
 *	JSP tag to include cart data in JSON serialized form.
 *	
 * @author treer
 */
public class CartDataTag extends BodyTagSupport {

	private static final long serialVersionUID = 1218690545170566016L;

	private static final Logger LOG = LoggerFactory.getInstance( CartDataTag.class );

	private static final String cartVarName = "cart";
	private static final String cartDataVarName = "cartData";
	private static final String cartItemListVarName = "explicitList";
	
	/**
	 * Coremetrics script
	 */
	private String cmScript;
	
	
	public String getCmScript() {
		return cmScript;
	}	
	public void setCmScript( String cmScript ) {
		this.cmScript = cmScript;
	}

	@Override
	public int doStartTag() throws JspException {
		
        HttpSession session = pageContext.getSession();        
    	HttpServletRequest request = ((HttpServletRequest)pageContext.getRequest());
    	HttpServletResponse response = ((HttpServletResponse)pageContext.getResponse());
    	
    	// Fetch server name
    	String serverName = request.getServerName();    	

        FDUserI user = ((FDUserI)session.getAttribute(SessionName.USER));
        if ( user == null ) {
        	// There is no user data in the session. Serious problem, should not happen.
        	LOG.error( "No user in session!" );
        	sendError( response, 401 );	// 401 Unauthorized
    		return SKIP_BODY;
        }
        
        String userId = user.getUserId();
        if ( userId == null || userId.trim().equals( "" ) ) {
        	userId = "[UNIDENTIFIED-USER]";
        }
        
        FDCartModel cart = user.getShoppingCart();        
        if ( cart == null ) {
            // user doesn't have a cart, this is a bug, as login or site_access should put it there
        	LOG.error( "No cart found for user " + userId );
        	sendError( response, 500 );	// 500 Internal Server Error
    		return SKIP_BODY;
        }
        
        synchronized ( cart ) {
        	// line items added to the list will be passed towards CM Shop5 tag
        	List<FDCartLineI> clines2report = new ArrayList<FDCartLineI>();

	        // Expose FDCartModel to jsp context
	        pageContext.setAttribute( cartVarName, cart );
			pageContext.setAttribute( cartItemListVarName, clines2report );
	        
	    	// Create response data object
	    	CartData cartData = new CartData();		
	
	    	// POST requests
	    	if ( "POST".equalsIgnoreCase( request.getMethod() ) ) {
	    		
	    		// Parse request data
	    		String reqJson = (String)request.getParameter( "change" );
	    		if ( reqJson == null ) {
	    			LOG.error( "Empty POST request. Aborting" );
	            	sendError( response, 400 );	// 400 Bad Request
	    			return SKIP_BODY;
	    		}
	    		
	    		CartRequestData reqData;
	    		try {
	    			reqData = new ObjectMapper().readValue(reqJson, CartRequestData.class);
	    			LOG.debug( reqData.toString() ); 
	    		} catch (IOException e) {
	    			LOG.error("Cannot read JSON", e);
	            	sendError( response, 400 );	// 400 Bad Request
	    			return SKIP_BODY;
				}
	    		
	    		// Send back 'header'
	    		cartData.setHeader( reqData.getHeader() );
	
	        	// First iteration - do any operations that modify cart first
	        	try {
	                List<FDCartLineI> cartLines = cart.getOrderLines();
	                if ( cartLines == null ) {
	                	LOG.error( "Orderlines in cart is a null object. Aborting. User:" + userId );
	                	sendError( response, 500 );	// 500 Internal Server Error
	            		return SKIP_BODY;
	                }
	                
	                Map<Integer,CartRequestData.Change> changes = reqData.getData();
	                
	    			for ( FDCartLineI cartLine : new ArrayList<FDCartLineI>( cartLines ) ) {    				
	    				Integer id = cartLine.getRandomId();    				
	    				CartRequestData.Change change = changes.get( id );
	    				
	    				// No change for this item
	    				if ( change == null )
	    					continue;
	    				
	    				// Do the change - 3 types of changes		//TODO validation!
	    				String chType = change.getType();
	    				if ( CartRequestData.Change.CHANGE_QUANTITY.equals( chType ) ) {
	    					double qu = Double.parseDouble( (String)change.getData() );
	    					CartOperations.changeQuantity( user, cart, cartLine, qu, serverName );
	    					
	    					clines2report.add( cartLine ); // <-- it will be reported!
	    				} else if ( CartRequestData.Change.CHANGE_SALESUNIT.equals( chType ) ) {
	    					String su = (String)change.getData();
	    					CartOperations.changeSalesUnit( user, cart, cartLine, su, serverName );
	    					
	    					clines2report.add( cartLine ); // <-- it will be reported!
	    				} else if ( CartRequestData.Change.REMOVE.equals( chType ) ) {
	    					CartOperations.removeCartLine( user, cart, cartLine, serverName );
	    					
	    				}
	    				
	    			}
	        	} catch (Exception e) {
	        		LOG.error("Error while modifying cart for user " + userId, e);
	            	sendError( response, 500 );	// 500 Internal Server Error
	    		}
	    	}
	    	
	    	
	    	// Second iteration - collect data from resulting cart contents
	        try {
	        	
	        	// Fetch new cartlines
	        	List<FDCartLineI> cartLines = cart.getOrderLines();
	            if ( cartLines == null ) {
	            	LOG.error( "Orderlines in modified cart is a null object. Aborting. User:" + userId );
	            	sendError( response, 500 );	// 500 Internal Server Error
	        		return SKIP_BODY;
	            }
	            
	            // Fetch recent cartline ids
	            SortedSet<Integer> recentIds = new TreeSet<Integer>();
	            for ( FDCartLineI rc : cart.getRecentOrderLines() ) {
	            	recentIds.add( new Integer( rc.getRandomId() ) );
	            }            
	        	
	            // Customer credit history - it may not be needed but the old sidecart had this
				FDCustomerCreditHistoryModel creditHistory = null;
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
		            } else {
	//	            	LOG.debug( "No credits remaining. ("+ JspMethods.formatPrice( creditHistory.getRemainingAmount() ) + " )" );
		            }	            
				}
				
				
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
						return o1.getTitle().compareTo( o2.getTitle() );
					}				
				});
				
				cartData.setCartSections( sections );
				
				cartData.setItemCount( itemCount );
				cartData.setSubTotal( JspMethods.formatPrice(cart.getSubTotal()) );
				cartData.setModifyOrder( cart instanceof FDModifyCartModel );
				
				cartData.setErrorMessage( null );
				cartData.setCoremetricsScript( cmScript );
				
				pageContext.setAttribute( cartDataVarName, cartData );
				
				return EVAL_BODY_BUFFERED;
				
			} catch (Exception e) {
				LOG.error("Error while processing cart for user " + userId, e);
	        	sendError( response, 500 );	// 500 Internal Server Error
			}        
			return SKIP_BODY;   
		}
	}

    public static class TagEI extends TagExtraInfo {
        public VariableInfo[] getVariableInfo(TagData data) {
            return new VariableInfo[] {
                new VariableInfo(cartVarName, "com.freshdirect.fdstore.customer.FDCartModel", true, VariableInfo.AT_END),
                new VariableInfo(cartDataVarName, "com.freshdirect.webapp.taglib.cart.CartData", true, VariableInfo.AT_END),
                new VariableInfo(cartItemListVarName, "java.util.List<com.freshdirect.fdstore.customer.FDCartLineI>", true, VariableInfo.AT_END)
            };
        }
    }

    private static final void sendError( HttpServletResponse response, int errorCode ) {
    	try {
			response.sendError( errorCode );
		} catch ( IOException e ) {
			LOG.error( "Failed to send error response", e );
		}
    }
}

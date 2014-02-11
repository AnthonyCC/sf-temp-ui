package com.freshdirect.webapp.ajax;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.EnumProductLayout;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.BodyTagSupport;


public class PDPRedirector extends BodyTagSupport {

	private static final long	serialVersionUID	= -8276493946678531129L;

	private static final Logger LOG = LoggerFactory.getInstance( PDPRedirector.class );
	
	public static final String	PDP_PAGE_URL		= "/pdp.jsp";
	public static final String	OLD_CATEGORY_PAGE	= "/category.jsp";
	public static final String	OLD_PRODUCT_PAGE	= "/product.jsp";
	
	private FDUserI user;
	
	public FDUserI getUser() {
		return user;
	}
	
	public void setUser( FDUserI user ) {
		this.user = user;
	}

	
	/* =======================================================
	 * 				JSP-TAG doStartTag
	 * ======================================================= */	

	@Override
	public int doStartTag() throws JspException {
		
		// first check property that disables the whole partial rollout stuff - do not redirect anything
		if ( FDStoreProperties.isPDPIgnorePartialRollout() ) {
			return EVAL_BODY_BUFFERED;
		}
		
		// partial rollout check
		boolean isPDP = isEligibleForPDP( user );		
		String redirectUrl = null;

		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest(); 
		String originalUrl = request.getRequestURI();
		
		if ( isPDP ) {
			if ( OLD_PRODUCT_PAGE.equalsIgnoreCase( originalUrl ) ) {
				// redirect to new pdp page from old product page
				String categoryId = request.getParameter( "catId" );
				String productId = request.getParameter( "productId" );					
	
				if ( productId != null && categoryId != null ) { 
					ProductModel productNode = ContentFactory.getInstance().getProductByName( categoryId, productId );
					EnumProductLayout prodLayout = productNode.getProductLayout(); 
					if ( prodLayout != EnumProductLayout.COMPONENTGROUP_MEAL && prodLayout != EnumProductLayout.MULTI_ITEM_MEAL ) {
						redirectUrl = PDP_PAGE_URL + "?catId=" + categoryId + "&productId=" + productId;
					}
				}
				
			} else if ( OLD_CATEGORY_PAGE.equalsIgnoreCase( originalUrl ) ) {
				// redirect to new pdp page from old grocery category-product page
				String categoryId = request.getParameter( "prodCatId" );
				String productId = request.getParameter( "productId" );					
				
				if ( productId != null && categoryId != null ) { 
					ProductModel productNode = ContentFactory.getInstance().getProductByName( categoryId, productId );
					if ( productNode.getLayout().isGroceryLayout() ) {
						redirectUrl = PDP_PAGE_URL + "?catId=" + categoryId + "&productId=" + productId;
					}
				}
			}
		} else {
			if ( PDP_PAGE_URL.equalsIgnoreCase( originalUrl ) ) {
	 			 // redirect back to old page
				String categoryId = request.getParameter( "catId" );
				String productId = request.getParameter( "productId" );
				
				if ( productId != null && categoryId != null ) { 
					ProductModel productNode = ContentFactory.getInstance().getProductByName( categoryId, productId );
					if ( productNode.getLayout().isGroceryLayout() ) {
						// if grocery layout redirect to category page
						redirectUrl = OLD_CATEGORY_PAGE + "?catId=" + categoryId + "&prodCatId=" + categoryId + "&productId=" + productId;
					} else {
						// redirect to product page
						redirectUrl = OLD_PRODUCT_PAGE + "?catId=" + categoryId + "&productId=" + productId;
					}
				}
	 		}
		}
		

		// No need for redirecting, (continue rendering the old page)
		if ( redirectUrl == null ) {
			return EVAL_BODY_BUFFERED;
		}
		
		// Do the redirect for real, and skip processing this page further
		redirect( redirectUrl );
		return SKIP_PAGE;
	}

	

	/* =======================================================
	 * 			ELIGIBILITY testing logic
	 * ======================================================= */

	public static boolean isEligibleForPDP( FDUserI user ) {

		// If partial rollout is disabled anyone is eligible for anything
		// Also if the global 'enable for all' flag is set 
		if ( FDStoreProperties.isPDPIgnorePartialRollout() || FDStoreProperties.isPDPEnableToAll() ) {
			return true;
		}
		
		// customer profile attribute
		try {
			if ( user == null || user.getIdentity() == null || user.getIdentity().getFDCustomerPK() == null ) {
				return false;
			}
			FDCustomerModel fdCust = FDCustomerFactory.getFDCustomer( user.getIdentity() );
			
			String eligibleAttr = fdCust.getProfile().getAttribute( "PDP_ELIGIBLE" );
			if ( "true".equalsIgnoreCase( eligibleAttr ) ) {
				return true;
			}
			
		} catch ( FDResourceException e ) {
			LOG.warn( "Failed to get customer profile", e );
		} catch ( Exception e ) {
			LOG.warn( "Failed to get customer profile", e );
		}
		
        return false;
	}

	/* =======================================================
	 * 			REDIRECT utility method
	 * ======================================================= */
	
    private void redirect( String redirectUrl ) throws JspException {    	
        try {
        	
        	HttpServletResponse response = ((HttpServletResponse)pageContext.getResponse());
            response.sendRedirect( response.encodeRedirectURL( redirectUrl ) );
            pageContext.getOut().close();
            
        } catch (IOException ioe) {
            throw new JspException(ioe.getMessage());
        }
    }
	
}

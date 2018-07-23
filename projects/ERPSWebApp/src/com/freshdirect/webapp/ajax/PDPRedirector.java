package com.freshdirect.webapp.ajax;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.FDNotFoundException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumFeatureRolloutStrategy;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.framework.webapp.BodyTagSupport;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.PopulatorUtil;
import com.freshdirect.storeapi.content.ProductModel;


public class PDPRedirector extends BodyTagSupport {

    private static final long	serialVersionUID	= -8276493946678531129L;

	public static final String	PDP_PAGE_URL		= "/pdp.jsp";
	public static final String	OLD_CATEGORY_PAGE	= "/category.jsp";
	public static final String	OLD_PRODUCT_PAGE	= "/product.jsp";

	private boolean redirected = false;
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
		
		// partial rollout check
		boolean isPDP = !EnumFeatureRolloutStrategy.NONE.equals(FeatureRolloutArbiter.getFeatureRolloutStrategy(EnumRolloutFeature.pdplayout2014, user));
		String redirectUrl = null;
		this.redirected = false;

		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest(); 
		String originalUrl = request.getRequestURI();

		if ( isPDP ) {
			if ( OLD_PRODUCT_PAGE.equalsIgnoreCase( originalUrl ) ) {
				// redirect to new pdp page from old product page
				String categoryId = request.getParameter( "catId" );
				String productId = request.getParameter( "productId" );					
	
				if ( productId != null && categoryId != null ) { 
                    ProductModel productNode = null;
					try {
						productNode = PopulatorUtil.getProductByName(categoryId, productId);
					} catch (FDResourceException e) {
						throw new FDNotFoundException(e.getMessage());
					}
					if(productNode!=null){
                        redirectUrl = PDP_PAGE_URL + "?catId=" + categoryId + "&productId=" + productId;
					}
				}
				
			} else if ( OLD_CATEGORY_PAGE.equalsIgnoreCase( originalUrl ) ) {
				// redirect to new pdp page from old grocery category-product page
				String categoryId = request.getParameter( "prodCatId" );
				String productId = request.getParameter( "productId" );					
				
				if ( productId != null && categoryId != null ) { 
					ProductModel productNode = ContentFactory.getInstance().getProductByName( categoryId, productId );
					if ( productNode!=null && productNode.getLayout().isGroceryLayout() ) {
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
                    ProductModel productNode = null;
					try {
						productNode = PopulatorUtil.getProductByName(categoryId, productId);
					} catch (FDResourceException e) {
						throw new FDNotFoundException(e.getMessage());
					}
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
	 * 			REDIRECT utility method
	 * ======================================================= */
	
    private void redirect( String redirectUrl ) throws JspException {    	
        try {
        	
        	HttpServletResponse response = ((HttpServletResponse)pageContext.getResponse());
            response.sendRedirect( response.encodeRedirectURL( redirectUrl ) );
            pageContext.getOut().close();
            this.redirected = true;
            
        } catch (IOException ioe) {
            throw new JspException(ioe.getMessage());
        }
    }
    
    @Override
    public int doEndTag() throws JspException {
    	 if (this.redirected) {
             return SKIP_PAGE;
         } else {
        	 return super.doEndTag();
         }
       
    }
	
}

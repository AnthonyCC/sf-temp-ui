/*
 * $Workfile$
 *
 * $Date$
 *
 * Copyright (c) 2001 FreshDirect, Inc.
 *
 */

package com.freshdirect.webapp.taglib.fdstore;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.apache.log4j.Category;

import com.freshdirect.common.context.UserContext;
import com.freshdirect.customer.ErpDeliveryPlantInfoModel;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 *
 * @version $Revision$
 * @author $Author$
 */
public class MergeCartControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport implements SessionName {

	private static Category LOGGER = LoggerFactory.getInstance( MergeCartControllerTag.class );

	private String successPage = null;
	
	public void setSuccessPage(String successPage) {
		this.successPage = successPage;
	}
	
	public int doStartTag() throws JspException {
		HttpSession session = pageContext.getSession();	
		if (session.getAttribute(CURRENT_CART) == null) {
			session.setAttribute(CURRENT_CART, new FDCartModel());
			return EVAL_BODY_BUFFERED;
		}
		
		//
        // the uers's saved cart has already been recalled in the login process
        //
		FDSessionUser user = (FDSessionUser) session.getAttribute( SessionName.USER );
		FDCartModel cartSaved = user.getShoppingCart();
        //
        // and the cart they previously were working on was stored in the session as well
        //
		FDCartModel cartCurrent = (FDCartModel) session.getAttribute(CURRENT_CART);
		
		/* check for null here */
		if (cartCurrent == null) {
			cartCurrent = new FDCartModel();
		}
		cartCurrent.setEStoreId(user.getUserContext().getStoreContext().getEStoreId());
		ErpDeliveryPlantInfoModel delPlantInfo=new ErpDeliveryPlantInfoModel();
		delPlantInfo.setPlantId(user.getUserContext().getFulfillmentContext().getPlantId());
		delPlantInfo.setSalesOrg(user.getUserContext().getPricingContext().getZoneInfo().getSalesOrg());
		delPlantInfo.setDistChannel(user.getUserContext().getPricingContext().getZoneInfo().getDistributionChanel());
		cartCurrent.setDeliveryPlantInfo(delPlantInfo);
		
		ContentFactory.getInstance().setCurrentUserContext(user.getUserContext());
		cartCurrent.setUserContextToOrderLines(user.getUserContext());
		cartCurrent.setEStoreId(user.getUserContext().getStoreContext().getEStoreId());
		FDCartModel cartMerged = new FDCartModel( cartCurrent );
		cartMerged.mergeCart( cartSaved );
		cartMerged.sortOrderLines();
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		if ("POST".equalsIgnoreCase(request.getMethod())) {
			// figure out which option was selected
			String opt = request.getParameter("chosen_cart");
			
			if (opt!=null) {
				if (opt.equalsIgnoreCase("current")) {
					// keep current cart
					user.setShoppingCart( cartCurrent );
					// invalidate promotion and recalc
					user.updateUserState();
                    session.setAttribute(USER, user);
				} else if(opt.equalsIgnoreCase("merge")) {
					user.setShoppingCart( cartMerged );
                    //Check for multiple savings. 
                    checkForMultipleSavings(cartCurrent, cartSaved, cartMerged);
					// invalidate promotion and recalc
                    user.updateUserState();
                    session.setAttribute(USER, user);
				}
//				else {
//					this.successPage="/login/index.jsp";
//				}
				if(null == user.getShoppingCart().getDeliveryPlantInfo()){
					user.getShoppingCart().setDeliveryPlantInfo(FDUserUtil.getDeliveryPlantInfo(user.getUserContext()));
				}
				user.getShoppingCart().setUserContextToOrderLines(user.getUserContext());	
				
				//evaluate the coupons, after the merge cart.
				FDCustomerCouponUtil.evaluateCartAndCoupons(session);
				
                // get rid of the extra cart in the session
                session.removeAttribute(CURRENT_CART);
			}
			HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
            try {
                LOGGER.debug("redirect after mergeCart ==> " + this.successPage);
                response.sendRedirect(response.encodeRedirectURL( this.successPage ));
                JspWriter writer = pageContext.getOut();
                writer.close();
            } catch (IOException ioe) {
                throw new JspException(ioe.getMessage());
            }
			return SKIP_BODY;
		}
	
		pageContext.setAttribute("cartCurrent", cartCurrent );
		pageContext.setAttribute("cartSaved", cartSaved );
		pageContext.setAttribute("cartMerged", cartMerged );

	
		return EVAL_BODY_BUFFERED;
	}
	
	private void checkForMultipleSavings(FDCartModel cartCurrent, FDCartModel cartSaved, FDCartModel cartMerged) {
		Set savedProdKeys = cartSaved.getProductKeysForLineItems();
		Set currProdKeys = cartCurrent.getProductKeysForLineItems();
		Set savedSavingIds = cartSaved.getUniqueSavingsIds();
		Set currSavingIds = cartCurrent.getUniqueSavingsIds();
		
		for (Iterator i = cartMerged.getOrderLines().iterator(); i.hasNext();) {
			FDCartLineI line     = (FDCartLineI) i.next();
			String productId = line.getProductRef().getContentKey().getId();
			if((savedProdKeys.contains(productId) && !currProdKeys.contains(productId)) && 
					savedSavingIds.contains(line.getSavingsId()) && currSavingIds.contains(line.getSavingsId())){
				//Product is only in the saved cart and savings id is in both carts then reset savings id for saved cart line.
				line.setSavingsId(null);
			}
		}
			
	}
	
}

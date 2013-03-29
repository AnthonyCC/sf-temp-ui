package com.freshdirect.webapp.taglib.standingorder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDException;
import com.freshdirect.fdstore.FDReservation;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDInvalidConfigurationException;
import com.freshdirect.fdstore.customer.FDModifyCartModel;
import com.freshdirect.fdstore.customer.FDProductSelectionI;
import com.freshdirect.fdstore.customer.FDTransientCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.OrderLineUtil;
import com.freshdirect.fdstore.customer.adapter.FDOrderAdapter;
import com.freshdirect.fdstore.customer.ejb.EnumCustomerListType;
import com.freshdirect.fdstore.lists.FDListManager;
import com.freshdirect.fdstore.lists.FDStandingOrderList;
import com.freshdirect.fdstore.promotion.EnumOfferType;
import com.freshdirect.fdstore.promotion.ExtendDeliveryPassApplicator;
import com.freshdirect.fdstore.promotion.Promotion;
import com.freshdirect.fdstore.promotion.PromotionFactory;
import com.freshdirect.fdstore.promotion.PromotionI;
import com.freshdirect.fdstore.promotion.RedemptionCodeStrategy;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.fdstore.standingorders.FDStandingOrdersManager;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.checkout.RedirectToPage;
import com.freshdirect.webapp.taglib.AbstractGetterTag;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.ModifyOrderControllerTag;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

/**
 * 
 * 
 * @author segabor
 *
 */
public class ManageStandingOrdersTag extends AbstractGetterTag {
	
	private static final long serialVersionUID = 3899271253313578651L;

	String actionResultName;
	
	public void setActionResult(String n) {
		this.actionResultName = n;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int doStartTag() throws JspException {
		// select lists first
		final int result = super.doStartTag();
		
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		final String ccListId = request.getParameter("ccListId"); // customer list ID
		final String action = request.getParameter("action");
		final String orderId = request.getParameter("orderId");
		
		ActionResult r = new ActionResult();
		
		Collection<FDStandingOrder> lists = (Collection<FDStandingOrder>) pageContext.getAttribute(getId());
		if (ccListId != null && ccListId.length() > 0 &&
				action != null && action.length() > 0 &&
				lists != null && lists.size() > 0 )
		{
			for (FDStandingOrder so : lists) {
				// find standing order bound to customer list
				if (so.getCustomerListId().equals(ccListId)) {
					try {
						processAction(so, action, r, orderId);
					} catch (RedirectToPage e) {
						try {
							((HttpServletResponse)pageContext.getResponse()).sendRedirect( e.getPage() );
						} catch (IOException e1) {
							throw new JspException(e);
						}
					} catch (FDResourceException e) {
						throw new JspException(e);
					} catch (FDInvalidConfigurationException e) {
						throw new JspException(e);
					}
					break;
				}
			}
		}
		
		if ( lists == null || lists.size() < 1 ) {
			return SKIP_BODY;
		}
		

		// record action result
		if (actionResultName != null) {
			pageContext.setAttribute(actionResultName, r);
		}

		return result;
	}


	/**
	 * Perform action on a standing order
	 * @param so
	 * @param action
	 * @throws RedirectToPage 
	 * @throws FDResourceException 
	 * @throws FDInvalidConfigurationException 
	 */
	private void processAction(FDStandingOrder so, String action, ActionResult result, String orderId) throws RedirectToPage, FDResourceException, FDInvalidConfigurationException {
		FDStandingOrdersManager mgr = FDStandingOrdersManager.getInstance();
		FDSessionUser user = (FDSessionUser) pageContext.getSession().getAttribute(SessionName.USER);

		FDActionInfo info = AccountActivityUtil.getActionInfo(pageContext.getSession());
		if ("skip".equalsIgnoreCase(action)) {
			if (!so.isDeleted()) {
				try {
					so.skipDeliveryDate();
					mgr.save(info, so);
				} catch (FDResourceException e) {
					result.addError(true, "GENERAL", "Failed to execute action '"+action+"' due to an exception " + e);
				}
				
				if (result.isSuccess()) {
					throw new RedirectToPage( (( HttpServletRequest) pageContext.getRequest()).getRequestURI() + "?listId=" + so.getId() );
				}
			}
		} else if ("cancel".equalsIgnoreCase(action)) {
			if (!so.isDeleted()) {
				try {
					mgr.delete(info, so);
				} catch (FDResourceException e) {
					result.addError(true, "GENERAL", "Failed to execute action '"+action+"' due to an exception " + e);
				}
			}
		} else if ("modify".equalsIgnoreCase(action)) {
			
			
			FDCartModel cart;
			if (orderId == null){
				user.setCurrentStandingOrder(so);
				// [A] Modify SO template then checkout a new order instance
				// user.setCheckoutMode(EnumCheckoutMode.MODIFY_SO_CSOI);

				// [B] Modify SO template WITHOUT checking out a new order instance 
				user.setCheckoutMode(EnumCheckoutMode.MODIFY_SO_TMPL);

				/* First store original cart */
				FDCustomerManager.storeUser(user.getUser());
				
				/* Create a new transient cart */
				cart = new FDTransientCartModel();
	
				user.setShoppingCart(cart);
			
			} else {
				cart = ModifyOrderControllerTag.modifyOrder((HttpServletRequest) pageContext.getRequest(), user, orderId,
						request.getSession(), so, EnumCheckoutMode.MODIFY_SO_MSOI, false);
				cart.clearOrderLines();
			}	

			if (so.isAlcoholAgreement()) {
				cart.setAgeVerified(true);
			}			
			
			/* Load list items to cart */
			for (FDCartLineI cartLine : getCartLinesFromSo(so, user)){
				if (orderId == null){
					cart.addOrderLine(cartLine);
				} else {
					((FDModifyCartModel)cart).addOriginalOrderLine(cartLine);
				}
			}
			
			user.setSuspendShowPendingOrderOverlay(true);
			
			cart.refreshAll(true);
			throw new RedirectToPage( "/checkout/view_cart.jsp" );
		}
	}

	private Collection<FDCartLineI> getCartLinesFromSo(FDStandingOrder so, FDSessionUser user) throws FDResourceException {
		FDStandingOrderList l = (FDStandingOrderList) FDListManager.getCustomerList(user.getIdentity(), EnumCustomerListType.SO, so.getCustomerListName());
		List<FDProductSelectionI> pi = OrderLineUtil.getValidProductSelectionsFromCCLItems(l.getLineItems());
		// OrderLineUtil.update(pi, true);
		
		Collection<FDCartLineI> cartLines = new ArrayList<FDCartLineI>();
		
		for (FDProductSelectionI p : pi) {
				// p.refreshConfiguration();

			final ProductModel prd = p.getProductRef().lookupProductModel();
			
			FDCartLineI cartLine = new FDCartLineModel(p.getSku(), prd, p.getConfiguration(), null, p.getPricingContext().getZoneId());
			//cartLine.refreshConfiguration();
			
			if (cartLine.lookupFDProductInfo().isAvailable()) {
				cartLines.add(cartLine);
			}
		}
		return cartLines;
	}

	
	/**
	 * Returns list of not deleted standing orders 
	 */
	@Override
	protected Object getResult() throws Exception {
		final FDStandingOrdersManager m = FDStandingOrdersManager.getInstance();
        HttpSession session = pageContext.getSession();
	
        FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
        
        Collection<FDStandingOrder> so = m.loadCustomerStandingOrders( user.getIdentity() );
		
		return so;
	}

    public static class TagEI extends AbstractGetterTag.TagEI {
        @Override
		protected String getResultType() {
            return "java.util.Collection<com.freshdirect.fdstore.standingorders.FDStandingOrder>";
        }
    }
}

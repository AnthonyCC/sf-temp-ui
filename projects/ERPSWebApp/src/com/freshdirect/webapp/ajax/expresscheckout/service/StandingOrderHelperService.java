/**
 * 
 */
package com.freshdirect.webapp.ajax.expresscheckout.service;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpDepotAddressModel;
import com.freshdirect.customer.ErpPaymentMethodModel;
import com.freshdirect.fdstore.EnumCheckoutMode;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.fdstore.standingorders.FDStandingOrder;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.action.WebActionSupport;
import com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil;
import com.freshdirect.webapp.util.StandingOrderUtil;

/**
 * @author kumarramachandran
 * 
 */
public class StandingOrderHelperService extends WebActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5064982528437634297L;
	private static Category LOGGER = LoggerFactory.getInstance(StandingOrderHelperService.class);

	private FDStandingOrder standingOrder;

	/**
	 * @return the standingOrder
	 */
	public FDStandingOrder getStandingOrder() {
		return standingOrder;
	}

	/**
	 * @param standingOrder
	 *            the standingOrder to set
	 */
	public void setStandingOrder(FDStandingOrder standingOrder) {
		this.standingOrder = standingOrder;
	}

	@Override
	public String execute() throws Exception {

		return null;

	}

	public  String saveStandingOrder(FDUserI user) throws FDResourceException {
		HttpSession session = this.getWebActionContext().getSession();

		FDCartModel cart = user.getSoTemplateCart();

		setStandingOrder(user.getCurrentStandingOrder());
		
		final EnumCheckoutMode mode = user.getCheckoutMode();

		if (!cart.getPaymentMethod().isGiftCard()) {
			// set the default credit card to the one that is in the cart
			FDCustomerManager.setDefaultPaymentMethod(AccountActivityUtil.getActionInfo(session),
					((ErpPaymentMethodModel) cart.getPaymentMethod()).getPK(), null, FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, user));
		}
		ErpAddressModel address = cart.getDeliveryAddress();

		
		// get the address pk and set the default address
		FDCustomerManager.setDefaultShipToAddressPK(user.getIdentity(), address.getPK().getId());

		/**
		 * Prepare a standing order object
		 * 
		 */
		if (EnumCheckoutMode.CREATE_SO == mode) {
			// this condition is equal to that SO exists but it does not have PK
			// yet
			standingOrder.setCustomerId(user.getIdentity().getErpCustomerPK());

			standingOrder.setPaymentMethodId(FDCustomerManager.getDefaultPaymentMethodPK(user.getIdentity()));

			standingOrder.setAddressId(cart.getDeliveryAddress().getPK().getId());

			standingOrder.setAlcoholAgreement(cart.isAgeVerified());

			standingOrder.setupDelivery(cart.getDeliveryReservation());
		}

		/**
		 * Standing Order related post actions
		 */
		if (mode.isModifyStandingOrder()) {
			// modify SO
			StandingOrderUtil.updateStandingOrder(session, mode, cart, standingOrder, null);
		} else {
			// create SO
			StandingOrderUtil.createStandingOrder(session, cart, standingOrder, null);
		}

		return this.getResult().isSuccess() ? "SUCCESS" : "ERROR";
	}
}

package com.freshdirect.webapp.checkout;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.ErpServicesProperties;
import com.freshdirect.customer.EnumChargeType;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.action.fdstore.ChooseTimeslotAction;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class TimeslotManipulator extends CheckoutManipulator {
	private static Category		LOGGER	= LoggerFactory.getInstance( TimeslotManipulator.class );

    public TimeslotManipulator(HttpServletRequest request, HttpServletResponse response, ActionResult result, String actionName) {
        super(request, response, result, actionName);
	}

	public String performReserveDeliveryTimeSlot() throws Exception {
		setPhoneCharge();
		// return setAndReserveDeliveryTimeSlot( request, result );
		ChooseTimeslotAction cta = new ChooseTimeslotAction();
		this.configureAction( cta, result );
		return cta.execute();
	}

	public void setPhoneCharge() throws FDResourceException {
		HttpSession session = request.getSession();
		FDSessionUser user = (FDSessionUser)session.getAttribute( SessionName.USER); 
		String app = (String)session.getAttribute( SessionName.APPLICATION );
		if ( !"CALLCENTER".equalsIgnoreCase( app ) && (user != null && user.getMasqueradeContext()==null) ) {
			return;
		}
		
		int phoneOrders = getUser().getOrderHistory().getPhoneOrderCount();
		if ( phoneOrders >= 3 ) {
			LOGGER.debug( "setting phone handling charge of $" + ErpServicesProperties.getPhoneHandlingFee() );
			FDCartModel cart = getCart();
			cart.setChargeAmount( EnumChargeType.PHONE, ErpServicesProperties.getPhoneHandlingFee());
		}

	}

}

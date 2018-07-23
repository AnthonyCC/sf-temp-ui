package com.freshdirect.webapp.ajax.expresscheckout.drawer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.deliverypass.DeliveryPassSubscriptionUtil;
import com.freshdirect.fdstore.services.tax.AvalaraContext;
import com.freshdirect.webapp.ajax.expresscheckout.data.DrawerData;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;

public class DrawerService {

	private static final DrawerService INSTANCE = new DrawerService();

	private static final String DRAWERS_KEY = "drawers";
	private static final String PAYMENT_DRAWER_ID = "payment";
	private static final String PAYMENT_DRAWER_TITLE = "Pay with";
	private static final String DELIVERY_ADDRESS_DRAWER_ID = "address";
	private static final String DELIVERY_ADDRESS_DRAWER_TITLE = "Deliver to";
	private static final String DELIVERY_TIMESLOT_DRAWER_ID = "timeslot";
	private static final String DELIVERY_TIMESLOT_DRAWER_TITLE = "Arrive by";
	
	private DrawerService() {
	}

	public static DrawerService defaultService() {
		return INSTANCE;
	}

	public Map<String, List<DrawerData>> loadDrawer(FDUserI user,boolean dlvPassCart ) {
		Map<String, List<DrawerData>> drawer = new HashMap<String, List<DrawerData>>();
		drawer.put(DRAWERS_KEY, loadDrawers(user,dlvPassCart));
		return drawer;
	}

	private List<DrawerData> loadDrawers(FDUserI user,boolean dlvPassCart) {
		List<DrawerData> drawers = new ArrayList<DrawerData>();
		
		FDCartModel cart=UserUtil.getCart(user, "", dlvPassCart);
		if((FDStoreProperties.isDlvPassStandAloneCheckoutEnabled() && cart.containsDlvPassOnly())){
			drawers.add(loadPaymentDrawer());
			// Changes as part of APPBUG-5583 - Set dummy address in the cart when the user does not have any for DeliveryPass only purchase flow       	
			if(null == cart.getDeliveryAddress()){
				cart.setDeliveryAddress(DeliveryPassSubscriptionUtil.setDeliveryPassDeliveryAddress(user.getSelectedServiceType()));
			}
			if(FDStoreProperties.getAvalaraTaxEnabled()){
				AvalaraContext context = new AvalaraContext(cart);
				context.setCommit(false);
				cart.getAvalaraTaxValue(context);
			}
		} else {
			drawers.add(loadDeliveryAddressDrawer(user));
			drawers.add(loadTimeslotDrawer(user));
			drawers.add(loadPaymentDrawer());
		}
		return drawers;
	}

	private DrawerData loadDeliveryAddressDrawer(FDUserI user) {
	    boolean isAddressDrawerEnabled = !(user.getMasqueradeContext() != null && user.getMasqueradeContext().isAddOnOrderEnabled());
        return createDrawer(DELIVERY_ADDRESS_DRAWER_ID, DELIVERY_ADDRESS_DRAWER_TITLE, isAddressDrawerEnabled);
	}

	private DrawerData loadTimeslotDrawer(FDUserI user) {
	    boolean isTimeslotDrawerEnabled = !(user.getMasqueradeContext() != null && user.getMasqueradeContext().isAddOnOrderEnabled());
        return createDrawer(DELIVERY_TIMESLOT_DRAWER_ID, DELIVERY_TIMESLOT_DRAWER_TITLE, isTimeslotDrawerEnabled);
	}

	private DrawerData loadPaymentDrawer() {
        return createDrawer(PAYMENT_DRAWER_ID, PAYMENT_DRAWER_TITLE);
	}

    private DrawerData createDrawer(final String id, final String title) {
        return createDrawer(id, title, true);
    }
	
    private DrawerData createDrawer(final String id, final String title, final boolean enabled) {
		DrawerData timeslotDrawer = new DrawerData();
		timeslotDrawer.setId(id);
		timeslotDrawer.setTitle(title);
		timeslotDrawer.setEnabled(enabled);
		return timeslotDrawer;
	}
}

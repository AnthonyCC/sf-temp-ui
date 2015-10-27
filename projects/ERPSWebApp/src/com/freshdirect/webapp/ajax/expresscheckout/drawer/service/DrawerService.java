package com.freshdirect.webapp.ajax.expresscheckout.drawer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.freshdirect.webapp.ajax.expresscheckout.coremetrics.service.CoremetricsService;
import com.freshdirect.webapp.ajax.expresscheckout.data.DrawerData;

public class DrawerService {

	private static final DrawerService INSTANCE = new DrawerService();

	private static final String DRAWERS_KEY = "drawers";
	private static final String PAYMENT_DRAWER_ID = "payment";
	private static final String PAYMENT_DRAWER_TITLE = "Payment Method";
	private static final String DELIVERY_ADDRESS_DRAWER_ID = "address";
	private static final String DELIVERY_ADDRESS_DRAWER_TITLE = "Delivery Address";
	private static final String DELIVERY_TIMESLOT_DRAWER_ID = "timeslot";
	private static final String DELIVERY_TIMESLOT_DRAWER_TITLE = "Delivery Time";
	
	private DrawerService() {
	}

	public static DrawerService defaultService() {
		return INSTANCE;
	}

	public Map<String, List<DrawerData>> loadDrawer() {
		Map<String, List<DrawerData>> drawer = new HashMap<String, List<DrawerData>>();
		drawer.put(DRAWERS_KEY, loadDrawers());
		return drawer;
	}

	private List<DrawerData> loadDrawers() {
		List<DrawerData> drawers = new ArrayList<DrawerData>();
		drawers.add(loadDeliveryAddressDrawer());
		drawers.add(loadTimeslotDrawer());
		drawers.add(loadPaymentDrawer());
		return drawers;
	}

	private DrawerData loadDeliveryAddressDrawer() {
        return createDrawer(DELIVERY_ADDRESS_DRAWER_ID, DELIVERY_ADDRESS_DRAWER_TITLE, CoremetricsService.defaultService().getCoremetricsData("address"));
	}

	private DrawerData loadTimeslotDrawer() {
        return createDrawer(DELIVERY_TIMESLOT_DRAWER_ID, DELIVERY_TIMESLOT_DRAWER_TITLE, CoremetricsService.defaultService().getCoremetricsData("timeslot"));
	}

	private DrawerData loadPaymentDrawer() {
        return createDrawer(PAYMENT_DRAWER_ID, PAYMENT_DRAWER_TITLE, CoremetricsService.defaultService().getCoremetricsData("payment"));
	}

	private DrawerData createDrawer(final String id, final String title, final List<String> onOpenCoremetrics) {
		DrawerData timeslotDrawer = new DrawerData();
		timeslotDrawer.setId(id);
		timeslotDrawer.setTitle(title);
		timeslotDrawer.setOnOpenCoremetrics(onOpenCoremetrics);
		return timeslotDrawer;
	}
}

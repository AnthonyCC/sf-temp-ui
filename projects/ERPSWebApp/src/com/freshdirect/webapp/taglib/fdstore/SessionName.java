package com.freshdirect.webapp.taglib.fdstore;

public interface SessionName {
    
    public final static String APPLICATION			= "fd.application";
    public final static String USER					= "fd.user";
    public final static String QUICKCART            = "fd.quickcart";
    public final static String TIMESLOT_ID			= "fd.timeslotId";
    public final static String RECENT_ORDER_NUMBER	= "fd.recentOrderNumber";
    public final static String SIGNUP_WARNING		   = "fd.signup.fraud.warning";
    public final static String BLOCKED_ADDRESS_WARNING = "fd.blocked.address.warning";
    public final static String CURRENT_CART			= "fd.user.currentCart"; // used to hang on the user's anonymous cart for merge_cart
    public final static String TELL_A_FRIEND			= "fd.tellafriend";
    public final static String PICKUP_AGREEMENT		= "fd.pickup_agreement";
    public final static String AUTHORIZED_PEOPLE		= "fd.authorized_people";
    public final static String ATP_WARNING			= "fd.atp.warning";
    
    public final static String FDCUSTOMER			= "fd.cc.fdcustomer";
    public final static String ERPCUSTOMER			= "fd.cc.erpcustomer";
    public final static String RECENT_ORDER			= "fd.cc.recentOrder";
    public final static String CUSTOMER_SERVICE_REP	= "fd.cc.csr";
    public final static String COMPLAINT_ID			= "fd.cc.complaintId";
    public final static String MAKEGOOD_COMPLAINT	= "fd.cc.makegoodComplaint";
    public final static String CC_SEARCH_RESULTS		= "fd.cc.search.custOrders";
    public final static String LIST_SEARCH_RAW		= "fd.cc.search.listRaw";
    public final static String REMOVED_RESERVATION  = "fd.removedReservation";    

    public final static String CUSTOMER_CREATED_LISTS = "fd.cclists";

	public static final String EDIT_PROMOTION = "fd.editPromotion";

	public static final String EDIT_PROMO_CUSTOMER_INFO_LIST = "fd.editPromoCustomerInfoList";
	
	public static final String TEST_DISTRIBUTION_DIRECTORY = "tests.distributions.path";

	public static final String SMART_SEARCH_VIEW = "fd.smartSearchView";
}

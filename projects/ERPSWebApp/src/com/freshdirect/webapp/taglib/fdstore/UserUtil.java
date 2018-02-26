package com.freshdirect.webapp.taglib.fdstore;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.context.MasqueradeContext;
import com.freshdirect.common.context.StoreContext;
import com.freshdirect.common.context.UserContext;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.pricing.CatalogKey;
import com.freshdirect.customer.EnumDeliveryType;
import com.freshdirect.customer.EnumExternalLoginSource;
import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.customer.ErpDeliveryPlantInfoModel;
import com.freshdirect.erp.model.ErpInventoryEntryModel;
import com.freshdirect.fdlogistics.model.FDDeliveryZoneInfo;
import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdlogistics.model.FDReservation;
import com.freshdirect.fdlogistics.model.FDTimeslot;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDCachedFactory;
import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDProduct;
import com.freshdirect.fdstore.FDProductInfo;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.FDSalesUnit;
import com.freshdirect.fdstore.FDSku;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.WineFilter;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.FDAuthenticationException;
import com.freshdirect.fdstore.customer.FDBulkRecipientList;
import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.fdstore.customer.FDCartLineModel;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerCreditUtil;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.customer.FDUserUtil;
import com.freshdirect.fdstore.customer.SavedRecipientModel;
import com.freshdirect.fdstore.customer.accounts.external.ExternalAccountManager;
import com.freshdirect.fdstore.rollout.EnumRolloutFeature;
import com.freshdirect.fdstore.rollout.FeatureRolloutArbiter;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.MD5Hasher;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionError;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.giftcard.EnumGiftCardType;
import com.freshdirect.giftcard.RecipientModel;
import com.freshdirect.logistics.delivery.model.EnumReservationType;
import com.freshdirect.logistics.delivery.model.EnumZipCheckResponses;
import com.freshdirect.mail.EmailUtil;
import com.freshdirect.storeapi.application.CmsManager;
import com.freshdirect.storeapi.content.ContentFactory;
import com.freshdirect.storeapi.content.ProductModel;
import com.freshdirect.webapp.taglib.coremetrics.CmRegistrationTag;
import com.freshdirect.webapp.util.FDURLUtil;
import com.freshdirect.webapp.util.LocatorUtil;
import com.freshdirect.webapp.util.RequestUtil;
import com.freshdirect.webapp.util.RobotRecognizer;
import com.freshdirect.webapp.util.RobotUtil;
import com.freshdirect.webapp.util.StoreContextUtil;

public class UserUtil {

    private static final Category LOGGER = LoggerFactory.getInstance(UserUtil.class);

    public static FDSessionUser createSessionUser(HttpServletRequest request, HttpServletResponse response, boolean guestAllowed) throws InvalidUserException {
        HttpSession session = request.getSession();
        String userAgent = request.getHeader("User-Agent");
        String serverName = request.getServerName();
        FDSessionUser user = null;

        if (RobotRecognizer.isHostileRobot(userAgent)) {
            throw new InvalidUserException("Hostile robot user agent is found: " + userAgent);
        }

        try {
            user = UserUtil.getSessionUserByCookie(session, CookieMonster.getCookie(request));
        } catch (FDResourceException ex) {
            LOGGER.warn(ex);
        }

        if (user != null) {
        	session.setAttribute(SessionName.USER, user);
            // prevent asking for e-mail address in case of returning customer
            user.setFutureZoneNotificationEmailSentForCurrentAddress(true);
            FDCustomerCouponUtil.initCustomerCoupons(session);
        }

        if (user == null && RobotRecognizer.isFriendlyRobot(serverName, userAgent)) {
            user = RobotUtil.createRobotUser(session);
        }

        if (user == null) {
            user = LocatorUtil.useIpLocator(session, request, response);
        }

        session.setAttribute(SessionName.USER, user);

        return user;
    }

    public static void touchUser(HttpServletRequest request, FDSessionUser user) {
        if (user != null) {
            LOGGER.debug("user was found! user id:" + user.getUserId());
            user.touch();
            user.setClientIp(RequestUtil.getClientIp(request));
            user.setServerName(RequestUtil.getServerName());
            user.setMobilePlatForm(isMobile(request.getHeader("User-Agent")) && FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user));
        } else {
            LOGGER.debug("user was not found!");
        }
    }

    public static void updateUserRelatedContexts(FDSessionUser user) {
        UserContext userContext = null;
        boolean isEligible;
        MasqueradeContext masqueradeContext = null;

        if (user != null) {
            userContext = user.getUserContext();
            isEligible = user.isEligibleForDDPP();
            masqueradeContext = user.getMasqueradeContext();
        } else {
            userContext = UserContext.createUserContext(CmsManager.getInstance().getEStoreEnum());
            isEligible = FDStoreProperties.isDDPPEnabled();
        }

        ContentFactory.getInstance().setCurrentUserContext(userContext);
        WineFilter.clearAvailabilityCache(userContext.getPricingContext());
        ContentFactory.getInstance().setEligibleForDDPP(FDStoreProperties.isDDPPEnabled() || isEligible);
        FDActionInfo.setMasqueradeAgentTL(masqueradeContext == null ? null : masqueradeContext.getAgentId());
    }

    public static boolean isMobile(String userAgent) {
        return (userAgent != null && !"".equals(userAgent) && (userAgent.toLowerCase().matches(
                    "(?i).*((android|bb\\d+|meego).+mobile|avantgo|bada\\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\\.(browser|link)|vodafone|wap|windows ce|xda|xiino).*")
                    || userAgent.toLowerCase().substring(0, 4).matches(
                        "(?i)1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)|le(no|xi)|lg( g|\\/(k|l|u)|50|54|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)|m\\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\\-|your|zeto|zte\\-")));
    }

    public static void newSession(HttpSession session, HttpServletResponse response) {
        // clear session
        // [segabor]: instead of wiping out all session entries delete just the 'customer'
        session.removeAttribute(SessionName.USER);
        // remove cookie
        CookieMonster.clearCookie(response);
    }

    public static FDSessionUser getSessionUserByCookie(HttpServletRequest request) throws FDResourceException {
        return getSessionUserByCookie(request.getSession(), CookieMonster.getCookie(request));
    }

    public static FDSessionUser getSessionUserByCookie(HttpSession session, String cookie) throws FDResourceException {
        FDSessionUser sessionUser = null;
        if(null !=cookie){
	        try {
                FDUser user = FDCustomerManager.recognize(cookie, CmsManager.getInstance().getEStoreEnum());
	            sessionUser = new FDSessionUser(user, session);
	        } catch (FDAuthenticationException e) {
	            LOGGER.warn(e);
	        }
        }
        return sessionUser;
    }

    public static FDSessionUser createSessionUser(EnumServiceType serviceType, Set<EnumServiceType> availableServices, HttpSession session, HttpServletResponse response,
            AddressModel address) throws FDResourceException {

        FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);

        if ((user == null) || ((user.getZipCode() == null) && (user.getDepotCode() == null))) {
            //
            // if there is no user object or a dummy user object created in
            // CallCenter, make a new using this zipcode
            // make sure to hang on to the cart that might be in progress in
            // CallCenter
            //
            FDCartModel oldCart = null;

            if (user != null) {
                oldCart = user.getShoppingCart();
            }
            StoreContext storeContext = StoreContextUtil.getStoreContext(session);
            user = new FDSessionUser(FDCustomerManager.createNewUser(address, serviceType, storeContext.getEStoreId()), session);
            user.setUserCreatedInThisSession(true);
            user.setSelectedServiceType(serviceType);
            // Added the following line for zone pricing to keep user service type up-to-date.
            user.setZPServiceType(serviceType);
            user.setAvailableServices(availableServices);

            if (oldCart != null) {
                user.setShoppingCart(oldCart);
            }

            CookieMonster.storeCookie(user, response);
            session.setAttribute(SessionName.USER, user);
        } else {
            //
            // otherwise, just update the zipcode in their existing object if
            // they haven't yet registered
            //
            if (user.getLevel() < FDUser.RECOGNIZED) {
                user.setAddress(address);
                user.setSelectedServiceType(serviceType);
                // Added the following line for zone pricing to keep user service type up-to-date.
                user.setZPServiceType(serviceType);
                user.setAvailableServices(availableServices);

                CookieMonster.storeCookie(user, response);
                FDCustomerManager.storeUser(user.getUser());
                session.setAttribute(SessionName.USER, user);
            }
        }

        // To fetch and set customer's coupons.
        if (user != null) {
            FDCustomerCouponUtil.initCustomerCoupons(session);
        }

        // The previous recommendations of the current session need to be removed.
        session.removeAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS);
        session.removeAttribute(SessionName.SAVINGS_FEATURE_LOOK_UP_TABLE);
        session.removeAttribute(SessionName.PREV_SAVINGS_VARIANT);
        return user;
    }

	public static void createSessionUser(HttpServletRequest request, HttpServletResponse response, FDUser loginUser)
		throws FDResourceException {
		HttpSession session = request.getSession();

		FDSessionUser sessionUser = new FDSessionUser(loginUser, session);
		sessionUser.isLoggedIn(true);

		if (loginUser.getMasqueradeContext() != null)
			CookieMonster.clearCookie(response);
		else
			CookieMonster.storeCookie(sessionUser, response);
		sessionUser.updateUserState();

		session.setAttribute(SessionName.USER, sessionUser);

		FDCustomerCouponUtil.initCustomerCoupons(session);
	}

	public static void createSessionUser(HttpServletRequest request, HttpServletResponse response, FDSessionUser sessionUser)
			throws FDResourceException {
			HttpSession session = request.getSession();
			sessionUser.isLoggedIn(true);
			CookieMonster.storeCookie(sessionUser, response);
			sessionUser.updateUserState();
			session.setAttribute(SessionName.USER, sessionUser);
			FDCustomerCouponUtil.initCustomerCoupons(session);
		}

	public static String getCustomerServiceContact(FDUserI user) {
		if (user==null) {
			return SystemMessageList.CUSTOMER_SERVICE_CONTACT;
		}
		return user.getCustomerServiceContact();
	}

	public static String getCustomerServiceContact(HttpServletRequest request) {
		FDUserI user = (FDUserI) request.getSession().getAttribute(SessionName.USER);
		return getCustomerServiceContact(user);
	}

	public static void initializeGiftCart(FDUserI user){
		try {
			FDCartModel cart = user.getGiftCart();
			FDReservation reservation= getFDReservation(user.getUserId(),null);
			cart.setDeliveryReservation(reservation);
			//Currently defaults to Personal gift card order type. This info should come from UI.
			/*
			EnumServiceType sType = EnumServiceType.GIFT_CARD_PERSONAL;
			if(user.getSelectedServiceType() != null && EnumServiceType.CORPORATE.equals(user.getSelectedServiceType())) {
				sType = user.getSelectedServiceType();
			}
			*/
			cart.setDeliveryAddress(getGiftCardDeliveryAddress(EnumServiceType.WEB));
			cart.setZoneInfo(getZoneInfo(cart.getDeliveryAddress()));
			ArrayList lines = new ArrayList();
			ContentFactory contentFactory = ContentFactory.getInstance();
			int n = 0;

			List recipList = user.getRecipientList().getRecipients(user.getGiftCardType()); //FDCustomerManager.loadSavedRecipients(user.getUser());
			int quantity = 1;
			Map optionMap = new HashMap();

			FDProductInfo productInfo = FDCachedFactory.getProductInfo(FDStoreProperties.getGiftcardSkucode());

		    FDProduct product = FDCachedFactory.getProduct(productInfo);

		    ProductModel productmodel = contentFactory.getProduct(FDStoreProperties.getGiftcardSkucode());

		    FDSalesUnit[] units = product.getSalesUnits();

		    FDSalesUnit salesUnit = units[n % units.length];

		    if(!recipList.isEmpty()) {
		    	ListIterator i = recipList.listIterator();
		    	while(i.hasNext()) {
		    		SavedRecipientModel srm = (SavedRecipientModel)i.next();

			            FDCartLineModel cartLine = new FDCartLineModel( new FDSku(productInfo),
		                		  productmodel, new FDConfiguration(quantity, salesUnit.getName(), optionMap),null, user.getUserContext());

		            cartLine.setFixedPrice(srm.getAmount());
		            cartLine.refreshConfiguration();

		            lines.add(cartLine);
		    	}

		        cart.setOrderLines(lines);
		        //cart.setRecipentList(recipList);
		        user.setGiftCart(cart);
		        if(user.getIdentity() != null) {
		        	FDCustomerCreditUtil.applyCustomerCredit(user.getGiftCart(), user.getIdentity());
		        }
			}
		}catch(Exception e){
			throw new FDRuntimeException(e);
		}

	}

	public static void initializeBulkGiftCart(FDUserI user){
		try {


			FDCartModel cart = user.getGiftCart();
			FDReservation reservation= getFDReservation(user.getUserId(),null);
			cart.setDeliveryReservation(reservation);
			//Currently defaults to Personal gift card order type. This info should come from UI.
			//EnumServiceType sType = EnumServiceType.GIFT_CARD_PERSONAL;
			cart.setDeliveryAddress(getGiftCardDeliveryAddress(EnumServiceType.WEB));
			cart.setZoneInfo(getZoneInfo(cart.getDeliveryAddress()));
			ArrayList lines = new ArrayList();
			ContentFactory contentFactory = ContentFactory.getInstance();
			int n = 0;


			FDBulkRecipientList bulkRecList = user.getBulkRecipentList(); //FDCustomerManager.loadSavedRecipients(user.getUser());
			if(bulkRecList==null || bulkRecList.size()==0) return;

			bulkRecList.constructFDRecipientsList();
			//FDRecipientList oldRecList=new FDRecipientList();
			//oldRecList.addRecipients(recipList);
			//user.setRecipientList(oldRecList);


			int quantity = 1;
			Map optionMap = new HashMap();

			FDProductInfo productInfo = FDCachedFactory.getProductInfo(FDStoreProperties.getGiftcardSkucode());

		    FDProduct product = FDCachedFactory.getProduct(productInfo);

		    ProductModel productmodel = contentFactory.getProduct(FDStoreProperties.getGiftcardSkucode());

		    FDSalesUnit[] units = product.getSalesUnits();

		    FDSalesUnit salesUnit = units[n % units.length];
		    List recipList= bulkRecList.getFDRecipentsList().getRecipients();
		    if(!recipList.isEmpty()) {
		    	ListIterator i = recipList.listIterator();
		    	while(i.hasNext()) {
		    		SavedRecipientModel srm = (SavedRecipientModel)i.next();

			            FDCartLineModel cartLine = new FDCartLineModel( new FDSku(productInfo),
		                		  productmodel, new FDConfiguration(quantity, salesUnit.getName(), optionMap),null, user.getUserContext());

		            cartLine.setFixedPrice(srm.getAmount());
		            cartLine.refreshConfiguration();

		            lines.add(cartLine);
		    	}

		        cart.setOrderLines(lines);
		        //cart.setRecipentList(recipList);
		        user.setGiftCart(cart);
		        if(user.getIdentity() != null) {
		        	FDCustomerCreditUtil.applyCustomerCredit(user.getGiftCart(), user.getIdentity());
		        }

			}
		}catch(Exception e){
			throw new FDRuntimeException(e);
		}

	}


	public static FDReservation getFDReservation(String customerID, String addressID) {
		Date expirationDT = new Date(System.currentTimeMillis() + 1000);
		FDTimeslot timeSlot=getFDTimeSlot();
		FDReservation reservation=new FDReservation(new PrimaryKey("1"), timeSlot, expirationDT, EnumReservationType.STANDARD_RESERVATION,
				customerID, addressID, false,null,-1,null, false, null,null);
		return reservation;

	}

	private static FDTimeslot getFDTimeSlot() {
		// TODO Auto-generated method stub
		return null;
	}

	private static ErpAddressModel getGiftCardDeliveryAddress(EnumServiceType sType){
		ErpAddressModel address=new ErpAddressModel();
		address.setAddress1("23-30 borden ave");
		address.setCity("Long Island City");
		address.setState("NY");
		address.setCountry("US");
		address.setZipCode("11101");
		address.setServiceType(sType);
		return address;

	}

	private static ErpAddressModel getDonationOrderDeliveryAddress(EnumServiceType sType){
		ErpAddressModel address=new ErpAddressModel();
		address.setAddress1("23-30 borden ave");
		address.setCity("Long Island City");
		address.setState("NY");
		address.setCountry("US");
		address.setZipCode("11101");
		address.setServiceType(sType);
		address.setCharityName("ROBIN HOOD");
		return address;

	}

	public static FDDeliveryZoneInfo getZoneInfo(ErpAddressModel address) throws FDResourceException, FDInvalidAddressException {

		FDDeliveryZoneInfo zInfo =new FDDeliveryZoneInfo("1","1","1", EnumZipCheckResponses.DELIVER);
		return zInfo;
	}

	public static void initializeCartForDonationOrder(FDUserI user){
		try {
			FDCartModel cart = user.getDonationCart();
			FDReservation reservation= getFDReservation(user.getUserId(),null);
			cart.setDeliveryReservation(reservation);
			//Currently defaults to Corporate Donation order type. This info should come from UI.
			//EnumServiceType sType = EnumServiceType.DONATION_BUSINESS;
			/*if(user.getSelectedServiceType() != null && EnumServiceType.CORPORATE.equals(user.getSelectedServiceType())) {
				sType = user.getSelectedServiceType();
			}*/
			cart.setDeliveryAddress(getDonationOrderDeliveryAddress(EnumServiceType.WEB));
			cart.setZoneInfo(getZoneInfo(cart.getDeliveryAddress()));
			ArrayList lines = new ArrayList();
			ContentFactory contentFactory = ContentFactory.getInstance();
			int n = 0;

			Integer totalQuantity = user.getDonationTotalQuantity();
			int quantity = 1;
			Map optionMap = new HashMap();

			FDProductInfo productInfo = FDCachedFactory.getProductInfo(FDStoreProperties.getRobinHoodSkucode());

		    FDProduct product = FDCachedFactory.getProduct(productInfo);

		    ProductModel productmodel = contentFactory.getProduct(FDStoreProperties.getRobinHoodSkucode());

		    FDSalesUnit[] units = product.getSalesUnits();

		    FDSalesUnit salesUnit = units[n % units.length];

		    FDCartLineModel cartLine = new FDCartLineModel( new FDSku(productInfo),
          		  productmodel, new FDConfiguration(totalQuantity, salesUnit.getName(), optionMap),null, user.getUserContext());

		    cartLine.setFixedPrice(productInfo.getZonePriceInfo(user.getUserContext().getPricingContext().getZoneInfo()).getDefaultPrice()*totalQuantity);//TODO: check whether it gives the required price or not.

		    cartLine.refreshConfiguration();

		    lines.add(cartLine);
		    /*for(int i=0; i< totalQuantity; i++){
		            FDCartLineModel cartLine = new FDCartLineModel( new FDSku(productInfo),
		                		  productmodel.getProductRef(), new FDConfiguration(quantity, salesUnit.getName(), optionMap),null);
		            cartLine.setFixedPrice(productInfo.getDefaultPrice());//TODO: check whether it gives the required price or not.
		            cartLine.refreshConfiguration();

		            lines.add(cartLine);
		    }*/
	        cart.setOrderLines(lines);
	        user.setDonationCart(cart);
	        if(user.getIdentity() != null) {
	        	FDCustomerCreditUtil.applyCustomerCredit(user.getGiftCart(), user.getIdentity());
	        }

		}catch(Exception e){
			throw new FDRuntimeException(e);
		}

	}

	public static double getRobinHoodAvailability(Date currentDate, FDProductInfo prodInfo) {

		double quantity = 0.0;
		ErpInventoryEntryModel inventoryMatch = null;
		Date lastInventoryDate = null;

		if(!FDStoreProperties.getPreviewMode()){
			if(prodInfo != null && prodInfo.getInventory() != null && prodInfo.getInventory().getEntries() != null
						&& !prodInfo.getInventory().getEntries().isEmpty()) {

				for (Iterator i = prodInfo.getInventory().getEntries().iterator(); i.hasNext();) {
					ErpInventoryEntryModel e = (ErpInventoryEntryModel) i.next();
	//				System.out.println("getRobinHoodAvailability >>"+e.getStartDate()+"->"+currentDate+"->"+e.getQuantity());

					if (null!= e.getStartDate() && !e.getStartDate().after(currentDate)) {
						if(lastInventoryDate == null || e.getStartDate().after(lastInventoryDate)) {
							inventoryMatch = e;
							lastInventoryDate = e.getStartDate();
						}
					}
				}
			}
			if(inventoryMatch != null) {
				quantity = inventoryMatch.getQuantity();
			}
		}else{
			quantity=15000;//Fixed value for preview mode, to test Robin Hood in preview mode.
		}
		return quantity;
	}

	public static String loginUser(HttpSession session, HttpServletRequest request, HttpServletResponse response
										, ActionResult actionResult, String userId, String password
										, String mergePage, String successPage, boolean externalLogin) {

		String updatedSuccessPage = successPage;

		//hard-code redirects and allow logic to override later
		if (updatedSuccessPage.indexOf("/registration/signup.jsp")!=-1 || updatedSuccessPage.indexOf("/checkout/signup_ckt.jsp")!=-1) {
			//hard-code redirects (be sure to set both vars)
			successPage = "/index.jsp";
			updatedSuccessPage = "/index.jsp";
		}


		if (!externalLogin) {
			if (userId == null || userId.length() < 1) {
				actionResult.addError(new ActionError(EnumUserInfoName.USER_ID
						.getCode(), SystemMessageList.MSG_REQUIRED));
			} else if (!EmailUtil.isValidEmailAddress(userId)) {
				actionResult.addError(new ActionError(
						EnumUserInfoName.EMAIL_FORMAT.getCode(),
						SystemMessageList.MSG_EMAIL_FORMAT));
			}
			if (password == null || password.length() < 1) {
				actionResult.addError(new ActionError(EnumUserInfoName.PASSWORD
						.getCode(), SystemMessageList.MSG_REQUIRED));
			} else if (password.length() < 4) {
				actionResult.addError(new ActionError(EnumUserInfoName.PASSWORD
						.getCode(), SystemMessageList.MSG_PASSWORD_TOO_SHORT));
			}
			if (!actionResult.isSuccess()) {
				return updatedSuccessPage;
			}
		}

		try {

			if (!externalLogin) {
				if(ExternalAccountManager.isSocialLoginOnlyUser(userId)){List<String> connectedProviders = null;
				String providerStr = "";
				connectedProviders = ExternalAccountManager.getConnectedProvidersByUserId(userId, EnumExternalLoginSource.SOCIAL);
				if(connectedProviders!=null && connectedProviders.size() >1) {
				for(String provider : connectedProviders)
				 	{
				providerStr += "'"+provider+"',";
				 	}
				} else if(connectedProviders!=null && connectedProviders.size() ==1){
				providerStr += "'"+connectedProviders.get(0)+"'";
				}
				session.setAttribute("IS_SOCIAL_LOGIN_USER_VALIDATION", "true");
				session.setAttribute("CONNECTED_SOCIAL_PROVIDERS", providerStr);
				actionResult.addError(new ActionError("IS_SOCIAL_LOGIN_USER_VALIDATION"));
				return updatedSuccessPage;
				}
			}

			FDIdentity identity = null;
			if(externalLogin){
				identity = FDCustomerManager.login(userId);
			}else{
				identity = FDCustomerManager.login(userId,password);
			}
            LOGGER.info("Identity : erpId = " + identity.getErpCustomerPK() + " : fdId = " + identity.getFDCustomerPK());
            String loginCookie = FDCustomerManager.getCookieByFdCustomerId(identity.getFDCustomerPK());
//            FDUser loginUser = FDCustomerManager.recognize(identity);
//            LOGGER.info("FDUser : erpId = " + loginUser.getIdentity().getErpCustomerPK() + " : " + loginUser.getIdentity().getFDCustomerPK());
            
            FDSessionUser currentUser = null;
            try{
            	currentUser = (FDSessionUser) session.getAttribute(SessionName.USER);
            }catch(IllegalStateException e){
            }

           /* // FDX-1873 - Show timeslots for anonymous address
            if(currentUser!=null && currentUser.getAddress() != null && currentUser.getAddress().getAddress1() != null
            				&& currentUser.getAddress().getAddress1().trim().length() > 0 && currentUser.getAddress().isCustomerAnonymousAddress()) {
            	loginUser.setAddress(currentUser.getAddress());
            }*/

            if(session.getAttribute("TICK_TIE_CUSTOMER") != null) {
            	session.removeAttribute(SessionName.USER);
            	currentUser = null;
            }

//            LOGGER.info("loginUser is " + loginUser.getFirstName() + " Level = " + loginUser.getLevel());
            // LOGGER.info("currentUser is " + (currentUser==null?"null":currentUser.getFirstName()+currentUser.getLevel()));
            String currentUserId=null;
			//[OPT-45]
            if(currentUser == null || (loginCookie != null && !loginCookie.equals(currentUser.getCookie()))){
    			// Pass updateUserState as currentUser != null.
    			// If currentUser == null, createSessionUser will be called later which will invoke updateUserState.
            	FDUser loginUser = FDCustomerManager.recognize(identity, currentUser != null);

            	// LOGGER.info("loginUser is " + loginUser.getFirstName() + " Level = " + loginUser.getLevel());
            	// FDX-1873 - Show timeslots for anonymous address
                if(currentUser!=null && currentUser.getAddress() != null && currentUser.getAddress().getAddress1() != null
                				&& currentUser.getAddress().getAddress1().trim().length() > 0 && currentUser.getAddress().isCustomerAnonymousAddress()) {
                	loginUser.setAddress(currentUser.getAddress());
                }
		        if (currentUser == null) {
		            // this is the case right after signout
		            UserUtil.createSessionUser(request, response, loginUser);

		        } else if (!loginUser.getCookie().equals(currentUser.getCookie())) {
		            // current user is different from user who just logged in
		            int currentLines = currentUser.getShoppingCart().numberOfOrderLines();
		            int loginLines = loginUser.getShoppingCart().numberOfOrderLines();

		            //address needs to be set using logged in user's information - in case existing cart is used or cart merge
		            currentUser.getShoppingCart().setDeliveryAddress(loginUser.getShoppingCart().getDeliveryAddress());

		            if ((currentLines > 0) && (loginLines > 0)) {
		                // keep the current cart in the session and send them to the merge cart page
		                if (
		                		successPage != null && !successPage.contains("/robin_hood") && !successPage.contains("/gift_card")
		                		&& mergePage != null && mergePage.trim().length() > 0
		                ){
		                	/* APPDEV-5781 */
		                	if (!FDStoreProperties.isObsoleteMergeCartPageEnabled()) {
		                	/* ---- logic from MergeCartControllerTag */
		                		//
		                        // the uers's saved cart has already been recalled in the login process
		                        //
		                		//FDSessionUser user = (FDSessionUser) session.getAttribute( SessionName.USER );
		                		FDCartModel cartSaved = loginUser.getShoppingCart();
		                        //
		                        // and the cart they previously were working on was stored in the session as well
		                        //
		                		//FDCartModel cartCurrent = (FDCartModel) session.getAttribute(SessionName.CURRENT_CART);
		                		/* currentUser is only used here and when set to session */
		                		FDCartModel cartCurrent = currentUser.getShoppingCart();

		                		/* check for null here */
		                		if (cartCurrent == null) {
		                			cartCurrent = new FDCartModel();
		                		}
		                		cartCurrent.setEStoreId(loginUser.getUserContext().getStoreContext().getEStoreId());
		                		ErpDeliveryPlantInfoModel delPlantInfo=new ErpDeliveryPlantInfoModel();
		                		delPlantInfo.setPlantId(loginUser.getUserContext().getFulfillmentContext().getPlantId());
		                		delPlantInfo.setSalesOrg(loginUser.getUserContext().getPricingContext().getZoneInfo().getSalesOrg());
		                		delPlantInfo.setDistChannel(loginUser.getUserContext().getPricingContext().getZoneInfo().getDistributionChanel());
		                		CatalogKey catalogKey = new CatalogKey(
		                			loginUser.getUserContext().getStoreContext().getEStoreId().name(),
		                			Long.parseLong(loginUser.getUserContext().getFulfillmentContext().getPlantId()),
		                			loginUser.getUserContext().getPricingContext().getZoneInfo()
		                		);
		                		delPlantInfo.setCatalogKey(catalogKey);
		                		cartCurrent.setDeliveryPlantInfo(delPlantInfo);

		                		ContentFactory.getInstance().setCurrentUserContext(loginUser.getUserContext());
		                		cartCurrent.setUserContextToOrderLines(loginUser.getUserContext());
		                		cartCurrent.setEStoreId(loginUser.getUserContext().getStoreContext().getEStoreId());
		                		FDCartModel cartMerged = new FDCartModel( cartCurrent );
		                		cartMerged.mergeCart( cartSaved );
		                		cartMerged.sortOrderLines();
		                		cartMerged.setUserContextToOrderLines(loginUser.getUserContext());
		                		cartMerged.setEStoreId(loginUser.getUserContext().getStoreContext().getEStoreId());

		                		/* "merge" choice logic */
		                		loginUser.setShoppingCart( cartMerged );
			                        //Check for multiple savings.
			                        checkForMultipleSavings(cartCurrent, cartSaved, cartMerged);
			    					// invalidate promotion and recalc
			                        loginUser.updateUserState(false);
			                        session.setAttribute(SessionName.USER, currentUser);

		        				if(null == loginUser.getShoppingCart().getDeliveryPlantInfo()){
		        					loginUser.getShoppingCart().setDeliveryPlantInfo(FDUserUtil.getDeliveryPlantInfo(loginUser));
		        				}
		        				loginUser.getShoppingCart().setUserContextToOrderLines(currentUser.getUserContext());

		        				//evaluate the coupons, after the merge cart.
		        				FDCustomerCouponUtil.evaluateCartAndCoupons(session);

		                        // get rid of the extra cart in the session
		                        session.removeAttribute(SessionName.CURRENT_CART);

		                        //LOGGER.debug("redirect after mergeCart ==> " + this.successPage);
		                        //response.sendRedirect(response.encodeRedirectURL( this.successPage ));
		                        //JspWriter writer = pageContext.getOut();
		                        //writer.close();
		                    /* ---- logic from MergeCartControllerTag */

		                    	//set in to session (from login user)
			                    session.setAttribute(SessionName.CURRENT_CART, loginUser.getShoppingCart());
		                	} else { //using merge cart page
		                    	//set in to session (from cur user)
		                		session.setAttribute(SessionName.CURRENT_CART, currentUser.getShoppingCart());
		                	}
		                    if (FDStoreProperties.isSocialLoginEnabled() ) {
								String preSuccessPage = (String) session.getAttribute(SessionName.PREV_SUCCESS_PAGE);
								if (preSuccessPage != null) {
									session.removeAttribute(SessionName.PREV_SUCCESS_PAGE);
									successPage = preSuccessPage;
								}
		                    }

		                    if (!FDStoreProperties.isObsoleteMergeCartPageEnabled()) {
		                    	/* don't go to merge cart page */
		                    	updatedSuccessPage = successPage;
		                    } else {
		                    	updatedSuccessPage =  mergePage + "?successPage=" + URLEncoder.encode( successPage ) ;
		                    }
		                }

		            } else if ((currentLines > 0) && (loginLines == 0)) {
		                // keep current cart
		                loginUser.setShoppingCart(currentUser.getShoppingCart());
		                loginUser.getShoppingCart().setDeliveryPlantInfo(FDUserUtil.getDeliveryPlantInfo(loginUser.getUserContext()));
		                loginUser.getShoppingCart().setUserContextToOrderLines(loginUser.getUserContext());

		            }

		            // merge coupons
		            currentUserId= currentUser.getPrimaryKey();

		            // current user has gift card recipients that need to be added to the login user's recipients list
		            if(currentUser.getLevel()==FDUserI.GUEST &&  currentUser.getRecipientList().getRecipients().size() > 0) {
		            	List<RecipientModel> tempList = currentUser.getRecipientList().getRecipients();
		            	ListIterator<RecipientModel> iterator = tempList.listIterator();
		            	//add currentUser's list to login user
		            	while(iterator.hasNext()) {
		            		SavedRecipientModel srm = (SavedRecipientModel)iterator.next();
		            		// reset the FDUserId to the login user
		            		srm.setFdUserId(loginUser.getUserId());
		            		loginUser.getRecipientList().removeRecipients(EnumGiftCardType.DONATION_GIFTCARD);
		            		loginUser.getRecipientList().addRecipient(srm);
		            	}
		            }

		            loginUser.setGiftCardType(currentUser.getGiftCardType());

		            if(currentUser.getDonationTotalQuantity()>0){
		            	loginUser.setDonationTotalQuantity(currentUser.getDonationTotalQuantity());
		            }
		            UserUtil.createSessionUser(request, response, loginUser);
		            //The previous recommendations of the current session need to be removed.
		            session.removeAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS);
		            session.removeAttribute(SessionName.SAVINGS_FEATURE_LOOK_UP_TABLE);
		            session.removeAttribute(SessionName.PREV_SAVINGS_VARIANT);

		        }
            }else {
                // the logged in user was the same as the current user,
                // that means that they were previously recognized by their cookie before log in
                // just set their login status and move on
                currentUser.isLoggedIn(true);
                session.setAttribute(SessionName.USER, currentUser);
            }
//          loginUser.setEbtAccepted(loginUser.isEbtAccepted()&&(loginUser.getOrderHistory().getUnSettledEBTOrderCount()<=0));
          FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
          if(user != null) {
              user.setEbtAccepted(user.isEbtAccepted()&&(user.getOrderHistory().getUnSettledEBTOrderCount()<1)&&!user.hasEBTAlert());
              FDCustomerCouponUtil.initCustomerCoupons(session,currentUserId);
          }


          if (user!=null && EnumServiceType.CORPORATE.equals(user.getUserServiceType()) && user.getUserContext() != null
        		  	&& user.getUserContext().getStoreContext() != null && !EnumEStoreId.FDX.equals(user.getUserContext().getStoreContext().getEStoreId())) {
        	  if(request.getRequestURI().indexOf("index.jsp")!=-1 || (successPage != null && successPage.indexOf("/login/index.jsp")!=-1)){
                    updatedSuccessPage = FDURLUtil.getLandingPageUrl(EnumServiceType.CORPORATE);
        	  }
          }

          if(null !=updatedSuccessPage && updatedSuccessPage.indexOf("/index.jsp")!=-1){
				try {
				    EnumDeliveryType lastOrderType = user.getOrderHistory().getLastOrderType();
				    if (EnumDeliveryType.CORPORATE.equals(lastOrderType)) {
				        updatedSuccessPage = FDURLUtil.getLandingPageUrl(EnumServiceType.CORPORATE);
				    } else if (EnumDeliveryType.HOME.equals(lastOrderType)) {
				        updatedSuccessPage = FDURLUtil.getLandingPageUrl(EnumServiceType.HOME);
				    }
				} catch (FDResourceException e) {
				    LOGGER.error("Error during getting order history", e);
				}
          }

          //tick and tie for refer a friend program
          if(session.getAttribute("TICK_TIE_CUSTOMER") != null) {
        	  String ticktie = (String) session.getAttribute("TICK_TIE_CUSTOMER");
        	  String custID = ticktie.substring(0, ticktie.indexOf("|"));
        	  String refName = ticktie.substring(ticktie.indexOf("|"));
        	  if(custID.equals(identity.getErpCustomerPK())) {
        		  //the session is for this user only
        		  String referralCustomerId = FDCustomerManager.recordReferral(custID, (String) session.getAttribute("REFERRALNAME"), user.getUserId());
        		  LOGGER.debug("Tick and tie:" + user.getUserId() + " with:" + referralCustomerId);
        		  user.setReferralCustomerId(referralCustomerId);
        		  user.setReferralPromoList();
        		  session.setAttribute(SessionName.USER, user);
        	  }
        	  session.removeAttribute("TICK_TIE_CUSTOMER");
          }

          if(user != null) {
        	user.setJustLoggedIn(true);
          }
          if(!FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.debitCardSwitch, user)){
        	  if(null!=user.getFDCustomer().getDefaultPaymentType() && !user.getFDCustomer().getDefaultPaymentType().getName().equals(EnumPaymentMethodDefaultType.UNDEFINED.getName())){
        	 user.resetDefaultPaymentValueType();
        	  }
          }else {
				FDActionInfo info = AccountActivityUtil.getActionInfo(request.getSession());
				boolean isDefaultPaymentMethodRegistered = false;
				try {
					if(user != null && user.getIdentity() != null && user.getFDCustomer() != null) {
						isDefaultPaymentMethodRegistered = user.getFDCustomer().getDefaultPaymentType() != null && user.getFDCustomer().getDefaultPaymentType().getName() != null
																	&& !(user.getFDCustomer().getDefaultPaymentType().getName().equals(EnumPaymentMethodDefaultType.UNDEFINED.getName()));
					}					
				} catch (FDResourceException e) {
					LOGGER.error(e);
				}
				if (user!=null&&!isDefaultPaymentMethodRegistered) {
					new Thread(new DefaultPaymentMethod(info, EnumPaymentMethodDefaultType.DEFAULT_SYS, user.getPaymentMethods())).start();
				}
			}

          CmRegistrationTag.setPendingLoginEvent(session);
          logExtraLoginDetailsLogin(request, userId, password, mergePage, successPage, externalLogin, "LOGINSUCCESS"); //THis is commented because it breaks using https://mobileapi.freshdirect.com/mobileapi/v/1/social/login/

        } catch (FDResourceException fdre) {
        	logExtraLoginDetailsLogin(request, userId, password, mergePage, successPage, externalLogin, "LOGINFAILED");
            LOGGER.warn("Resource error during authentication", fdre);
            actionResult.addError(new ActionError("technical_difficulty", SystemMessageList.MSG_TECHNICAL_ERROR));

        } catch (FDAuthenticationException fdae) {

            logExtraLoginDetailsLogin(request, userId, password, mergePage, successPage, externalLogin, "LOGINFAILED");

            if ("Account disabled".equals(fdae.getMessage())) {
                actionResult.addError(
                        new ActionError("authentication", MessageFormat.format(SystemMessageList.MSG_DEACTIVATED, new Object[] { UserUtil.getCustomerServiceContact(request) })));
            } else {
                if ("voucherredemption".equals(fdae.getMessage())) {
                    actionResult.addError(new ActionError("authentication",
                            MessageFormat.format(SystemMessageList.MSG_VOUCHER_REDEMPTION_FDX_NOT_ALLOWED, new Object[] { UserUtil.getCustomerServiceContact(request) })));
                } else {
                    actionResult.addError(new ActionError("authentication",
                            MessageFormat.format(SystemMessageList.MSG_AUTHENTICATION, new Object[] { UserUtil.getCustomerServiceContact(request) })));
                }
            }

        }
        return updatedSuccessPage;
	}

	private static void logExtraLoginDetailsLogin(HttpServletRequest request, String userId, String password,
			String mergePage, String successPage, boolean externalLogin, String result) {

		LOGGER.info("FDSECU01:"+ result + ": " +RequestUtil.getClientIp(request)+" : "+userId+" : "+ (FDStoreProperties.isExtraLogForLoginFailsEnabled() ? password : "[MASKED]") +" : "+mergePage+" : "+successPage+" : "+externalLogin
				+" : "+CookieMonster.getCookie(request)+" : "+request.getRequestURL());
	}

	/*
	 * In RegistrationAction, dummy password "^0X!3X!X!1^" has been set for social login.
	 */

	public static boolean isPasswordAddedForSocialUser(String erpCustomerId) throws Exception{

		ErpCustomerModel erpCustomer = FDCustomerFactory.getErpCustomer(erpCustomerId);
		String dummyPassword = "^0X!3X!X!1^";
		String dummyPasswordHash = MD5Hasher.hash(dummyPassword);
		if((dummyPasswordHash.equalsIgnoreCase(erpCustomer.getPasswordHash()))){
			return false;
		}

		return true;
	}

	/* copied from MergeCartControllerTag */
	private static void checkForMultipleSavings(FDCartModel cartCurrent, FDCartModel cartSaved, FDCartModel cartMerged) {
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

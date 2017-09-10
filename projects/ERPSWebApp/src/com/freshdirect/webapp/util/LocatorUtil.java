package com.freshdirect.webapp.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.context.StoreContext;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.customer.ServiceTypeUtil;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCartModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.fdstore.iplocator.IpLocatorClient;
import com.freshdirect.fdstore.iplocator.IpLocatorData;
import com.freshdirect.fdstore.iplocator.IpLocatorEventDTO;
import com.freshdirect.fdstore.iplocator.IpLocatorException;
import com.freshdirect.fdstore.iplocator.IpLocatorUtil;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.CookieMonster;
import com.freshdirect.webapp.taglib.fdstore.FDCustomerCouponUtil;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class LocatorUtil {
	
	private static String IP_LOCATOR_MOCKED_IP_ADDRESS = "iplocator_mocked_ip_address";
	private static Category LOGGER = LoggerFactory.getInstance(LocatorUtil.class);
	
	public static FDSessionUser useIpLocator(HttpSession session, HttpServletRequest request, HttpServletResponse response, AddressModel address) {
    	FDSessionUser user = null;
    	
    	if (FDStoreProperties.isIpLocatorEnabled()) {
    		RequestClassifier requestClassifier = new RequestClassifier(request);
    		int rolloutPercent = FDStoreProperties.getIpLocatorRolloutPercent(); 
    		
    		if (requestClassifier.isInHashRange(rolloutPercent)){ //check if rolled out to user
		    	try {
	    			//used mocked ip address parameter (for testing) if exists
			    	String ip = NVL.apply(request.getParameter(IP_LOCATOR_MOCKED_IP_ADDRESS), RequestUtil.getClientIp(request)); 
			    	
			    	if(FDStoreProperties.isLoggingAkamaiEdgescapgeHeaderInfoEnabled()){
			    		logAkamaiEdgescapeHeaderInfo(request);
			    	}
		    		IpLocatorData ipLocatorData = IpLocatorClient.getInstance().getData(ip);
		    		
		    		IpLocatorEventDTO ipLocatorEventDTO = new IpLocatorEventDTO();
		    		ipLocatorEventDTO.setIp(ip);
		    		ipLocatorEventDTO.setIpLocZipCode(ipLocatorData.getZipCode());
		    		ipLocatorEventDTO.setIpLocCountry(ipLocatorData.getCountryCode());
		    		ipLocatorEventDTO.setIpLocRegion(ipLocatorData.getRegion());
		    		ipLocatorEventDTO.setIpLocCity(ipLocatorData.getCity());
		    		ipLocatorEventDTO.setUserAgent(requestClassifier.getUserAgent());
		    		ipLocatorEventDTO.setUaHashPercent(requestClassifier.getHashPercent());
		    		ipLocatorEventDTO.setIplocRolloutPercent(rolloutPercent);
		    		
		    		user = createUser(session, request, response, address, ipLocatorData, ipLocatorEventDTO);
	
		    	} catch (Exception e) {
					LOGGER.error("IP Locator failed: ", e);
				}

    		}    	
    	} 
    	
   		return user;
    }

	private static void logAkamaiEdgescapeHeaderInfo(HttpServletRequest request) {
		try {
			String akamaiEdgeHeader = request.getHeader("X-Akamai-Edgescape");
			LOGGER.info("Akamai Edgescape Header: "+akamaiEdgeHeader);				
			if(akamaiEdgeHeader !=null && akamaiEdgeHeader.length() >0){
				String[] attributes = akamaiEdgeHeader.split("\\s*,\\s*");
				if(null !=attributes){
					for(String attr:attributes) {
						if(null !=attr){
							String[] kv = attr.split("=");
							if("zip"==kv[0]){
								LOGGER.info("zips from Akamai Edgescape Header: "+kv[1]);
								break;
							}
						}
					}
				}
				
			}
		} catch (Exception e) {
			LOGGER.warn(e);
		}
	}
    
    /** based on SiteAccessControllerTag.doStartTag()*/
    private static FDSessionUser createUser(HttpSession session, HttpServletRequest request, HttpServletResponse response, AddressModel address
    												, IpLocatorData ipLocatorData, IpLocatorEventDTO ipLocatorEventDTO) throws IpLocatorException{
    	FDSessionUser user = null;
    	
    	try {
    	    EnumEStoreId eStoreId = StoreContextUtil.getStoreContext(session).getEStoreId();
    		boolean useIpLocatorData = IpLocatorUtil.validate(ipLocatorData);
    		String zipCode = (EnumEStoreId.FD == eStoreId) ? FDStoreProperties.getDefaultPickupZoneId() : FDStoreProperties.getDefaultFdxZoneId();
    		if (useIpLocatorData){
    		    zipCode = ipLocatorData.getZipCode();
    		}

	    	newSession(session, request, response);
	    	address = new AddressModel(); // was this. from CheckLoginStatusTag
	    	address.setZipCode(zipCode);
            Set<EnumServiceType> availableServices = FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(zipCode, eStoreId).getAvailableServices();
	    	
	    	//FDCustomerManager.createNewUser() inside createUser() will only use zipCode and resolve location based on that.
	    	//City and State information will be appended to user.address.
	    	user = createUser(ServiceTypeUtil.getPreferedServiceType(availableServices), availableServices, session, response, address);
	    	
	    	ipLocatorEventDTO.setFdUserId(user.getPrimaryKey());
	    	AddressModel _address = user.getAddress();
	    	if (_address != null){
		    	ipLocatorEventDTO.setFdZipCode(_address.getZipCode());
		    	ipLocatorEventDTO.setFdState(_address.getState());
		    	ipLocatorEventDTO.setFdCity(_address.getCity());
	    	}
	    	
	    	if (FDStoreProperties.isIpLocatorEventLogEnabled()){
		    	try {
		    		//log IpLocatorEvent before appending data to user from IpLocatorData
		    		FDCustomerManager.logIpLocatorEvent(ipLocatorEventDTO);
		    	} catch (Exception e){
		    		LOGGER.error("logIpLocatorEvent failed", e);
		    	}
		    }
	    	LOGGER.debug("ipLocatorEventDTO: " + ipLocatorEventDTO);
	    	
	    	//If no data city/state is appended by createUser(), city and state fields will be taken from ipLocatorData
    		if (useIpLocatorData){
    			IpLocatorUtil.appendMissingFieldsToUserAddress(ipLocatorData, user.getUser());
    		}
	    	
    	} catch (Exception e) {
			LOGGER.error(e);
			throw new IpLocatorException(e);
		}
    	
    	return user;
    }
    
    public static FDSessionUser createUser(EnumServiceType serviceType,
            Set<EnumServiceType> availableServices, HttpSession session, HttpServletResponse response, AddressModel address) throws FDResourceException {
            
            FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);

            if ((user == null) ||
                    ((user.getZipCode() == null) && (user.getDepotCode() == null))) {
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
                StoreContext storeContext =StoreContextUtil.getStoreContext(session);
                user = new FDSessionUser(FDCustomerManager.createNewUser(address, serviceType, storeContext.getEStoreId()), session);
                user.setUserCreatedInThisSession(true);
                user.setSelectedServiceType(serviceType);
                //Added the following line for zone pricing to keep user service type up-to-date.
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
                    //Added the following line for zone pricing to keep user service type up-to-date.
                    user.setZPServiceType(serviceType);
                    user.setAvailableServices(availableServices);

                    CookieMonster.storeCookie(user, response);
                    FDCustomerManager.storeUser(user.getUser());
                    session.setAttribute(SessionName.USER, user);
                }
            }

            //To fetch and set customer's coupons.
    		if(user != null){
    			FDCustomerCouponUtil.initCustomerCoupons(session);
    		}
            
            //The previous recommendations of the current session need to be removed.
            session.removeAttribute(SessionName.SMART_STORE_PREV_RECOMMENDATIONS);
            session.removeAttribute(SessionName.SAVINGS_FEATURE_LOOK_UP_TABLE);
            session.removeAttribute(SessionName.PREV_SAVINGS_VARIANT);
            return user;
    }

    public static void newSession(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        // clear session
        // [segabor]: instead of wiping out all session entries delete just the 'customer'
        session.removeAttribute(SessionName.USER);
        // remove cookie
        CookieMonster.clearCookie(response);
    }

}

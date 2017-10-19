package com.freshdirect.webapp.util;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.common.customer.ServiceTypeUtil;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.iplocator.IpLocatorClient;
import com.freshdirect.fdstore.iplocator.IpLocatorData;
import com.freshdirect.fdstore.iplocator.IpLocatorEventDTO;
import com.freshdirect.fdstore.iplocator.IpLocatorException;
import com.freshdirect.fdstore.iplocator.IpLocatorUtil;
import com.freshdirect.framework.util.NVL;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.UserUtil;

public class LocatorUtil {
	
    private static final Category LOGGER = LoggerFactory.getInstance(LocatorUtil.class);

    private static final String IP_LOCATOR_MOCKED_IP_ADDRESS = "iplocator_mocked_ip_address";
	
    public static FDSessionUser useIpLocator(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
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
		    		
                    user = createUser(session, response, ipLocatorData, ipLocatorEventDTO);
	
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
    private static FDSessionUser createUser(HttpSession session, HttpServletResponse response, IpLocatorData ipLocatorData, IpLocatorEventDTO ipLocatorEventDTO)
            throws IpLocatorException {
    	FDSessionUser user = null;
    	
    	try {
    	    EnumEStoreId eStoreId = StoreContextUtil.getStoreContext(session).getEStoreId();
    		boolean useIpLocatorData = IpLocatorUtil.validate(ipLocatorData);
    		String zipCode = (EnumEStoreId.FD == eStoreId) ? FDStoreProperties.getDefaultPickupZoneId() : FDStoreProperties.getDefaultFdxZoneId();
    		if (useIpLocatorData){
    		    zipCode = ipLocatorData.getZipCode();
    		}

            UserUtil.newSession(session, response);
            AddressModel address = new AddressModel(); // was this. from CheckLoginStatusTag
	    	address.setZipCode(zipCode);
            Set<EnumServiceType> availableServices = FDDeliveryManager.getInstance().getDeliveryServicesByZipCode(zipCode, eStoreId).getAvailableServices();
	    	
	    	//FDCustomerManager.createNewUser() inside createUser() will only use zipCode and resolve location based on that.
	    	//City and State information will be appended to user.address.
            user = UserUtil.createSessionUser(ServiceTypeUtil.getPreferedServiceType(availableServices), availableServices, session, response, address);
	    	
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

}

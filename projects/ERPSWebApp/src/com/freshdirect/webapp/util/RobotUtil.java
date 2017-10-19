package com.freshdirect.webapp.util;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.customer.FDUser;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.fdstore.StateCounty;
import com.freshdirect.webapp.taglib.fdstore.FDSessionUser;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class RobotUtil {
	
    private static final Category LOGGER = LoggerFactory.getInstance(RobotUtil.class);

    private static final String ROBOT_USER_NAME = "robot";

	public static FDSessionUser createRobotUser(HttpSession session){
        FDUser robotUser = new FDUser(new PrimaryKey(ROBOT_USER_NAME));
        robotUser.setRobot(true);
        
        Set<EnumServiceType> availableServices = new HashSet<EnumServiceType>();
        availableServices.add(EnumServiceType.HOME);
        availableServices.add(EnumServiceType.CORPORATE);
        
        robotUser.setSelectedServiceType(EnumServiceType.HOME);
        robotUser.setAvailableServices(availableServices);

        String zipCode = FDStoreProperties.getDefaultPickupZoneId(); 
        AddressModel address = new AddressModel();
		address.setZipCode(zipCode);

		try {
			StateCounty stateCounty = FDDeliveryManager.getInstance().lookupStateCountyByZip(zipCode);
			if (stateCounty == null){
				LOGGER.info("stateCountry is null for zip: "+ zipCode);
			} else {
				address.setState(stateCounty.getState());
				address.setCity(stateCounty.getCity());
			}

		} catch (FDResourceException e) {
			LOGGER.error(e);
		}

        robotUser.setAddress(address);
        robotUser.isLoggedIn(false);
        
        FDSessionUser user = new FDSessionUser(robotUser, session);
        session.setAttribute(SessionName.USER, user);
        
        LOGGER.debug("robot user created: " + user);
        return user;
	}
}

package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;
import java.sql.SQLException;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;


public class ProfileCreatorService extends AbstractEcommService implements  ProfileCreatorServiceI{
	
	private static ProfileCreatorService INSTANCE;
	
	private final static Category LOGGER = LoggerFactory.getInstance(ProfileCreatorService.class);

	private static final String CREATE_PROFILES = "profileCreator/create/batchId/";
	
	public static ProfileCreatorServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ProfileCreatorService();

		return INSTANCE;
	}

	@Override
	public void createProfiles(String batchId) throws SQLException,FDResourceException, RemoteException {
		Response<String> response = null;
		
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(CREATE_PROFILES +batchId),  new TypeReference<Response<String>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new RemoteException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}
	
	

}

package com.freshdirect.mktAdmin.util;

import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.delivery.DlvAddressGeocodeResponse;
import com.freshdirect.delivery.DlvAddressVerificationResponse;
import com.freshdirect.delivery.EnumAddressVerificationResult;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mktAdmin.exception.MktAdminApplicationException;
import com.freshdirect.mktAdmin.exception.MktAdminSystemException;
import com.freshdirect.mktAdmin.model.CustomerAddressModel;

public class MarketAdminUtil {
	
	private final static Category LOGGER = LoggerFactory.getInstance(MarketAdminUtil.class);
	
	public static String getFormatedZipCode(String zipCode){
		// check and throw system exception if it is not number
		int length=zipCode.length();
		StringBuffer zipBuffer=new StringBuffer();
		if(length<5)
		{
			for(int i=0;i<(5-length);i++)
			{
				zipBuffer.append("0");
			}
		}
		zipBuffer.append(zipCode);
		return zipBuffer.toString();
	}
	
	
	public static void performAddressCheck(AddressModel dlvAddress) throws MktAdminApplicationException {

		AddressModel dlvAddressTmp=null;
		try {
			LOGGER.debug("----------- START performAddressCheck ------------------"+dlvAddress);
			DlvAddressVerificationResponse verifyResponse = FDDeliveryManager.getInstance().scrubAddress(dlvAddress);

			//
			// set to scrubbed address
			//
			dlvAddressTmp = verifyResponse.getAddress();
			
			LOGGER.debug("----------- AFTER SCRUB ------------------"+dlvAddressTmp);
			
			EnumAddressVerificationResult verificationResult = verifyResponse.getResult();
			
			LOGGER.debug("----------- SCRUB RESULT------------------"+verificationResult);
			
			if (!EnumAddressVerificationResult.ADDRESS_BAD.equals(verificationResult) || EnumAddressVerificationResult.ADDRESS_NOT_UNIQUE.equals(verificationResult)) {
				//
				// geocode address
				//
				try {
					LOGGER.debug("----------- START GEOCODE------------------");
					DlvAddressGeocodeResponse geocodeResponse = FDDeliveryManager.getInstance().geocodeAddress(dlvAddressTmp);					
				    String geocodeResult = geocodeResponse.getResult();
				    LOGGER.debug("----------- GEOCODE RESPONSE------------------"+geocodeResult+"\n"+geocodeResponse);
				    if(!"GEOCODE_OK".equalsIgnoreCase(geocodeResult))
				    {
				    	if(dlvAddressTmp.getCompanyName()==null){
				    		LOGGER.debug("----------- ERROR 115------------------");
				    		CustomerAddressModel model=(CustomerAddressModel)dlvAddressTmp;
				    		throw new MktAdminApplicationException("115",new String[]{model.getAddress1(),model.getCustomerId()});
				    	}
				    	else{	
				    		LOGGER.debug("----------- ERROR 110------------------");	
				    	  throw new MktAdminApplicationException("110",new String[]{dlvAddressTmp.getAddress1(),dlvAddressTmp.getCompanyName()});
				    	}
				    }
				    AddressModel addrModel=geocodeResponse.getAddress();
				    dlvAddress.setFrom(addrModel);
					LOGGER.debug(dlvAddress+"\n----------- GEOCODE UTIL COMPLETE------------------");
				} catch (FDInvalidAddressException iae) {
					LOGGER.debug("----------- INVALID ADDRESS EXCEPTIOn------------------");	
					if(dlvAddressTmp.getCompanyName()==null){
			    		CustomerAddressModel model=(CustomerAddressModel)dlvAddressTmp;
			    		throw new MktAdminApplicationException("116",new String[]{model.getAddress1(),model.getCustomerId()});
			    	}
			    	else{
			    		throw new MktAdminApplicationException("108",new String[]{dlvAddressTmp.getAddress1(),dlvAddressTmp.getCompanyName()});
			    	}
				}			

			}

			
		} catch (FDResourceException e) {
			// TODO Auto-generated catch block
			throw new MktAdminSystemException("1001",e);
		} 

	}
	
	public static void main(String args[]) throws MktAdminApplicationException{
		AddressModel model=new AddressModel();
		model.setAddress1("20 chambers st");
		model.setApartment("18e");
		model.setCity("New York");
		model.setState("NY");
		model.setZipCode("10007");
		performAddressCheck(model);
	}
	
}

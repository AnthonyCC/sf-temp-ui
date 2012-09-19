package com.freshdirect.fdstore.coremetrics.builder;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.coremetrics.tagmodel.RegistrationTagModel;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;

public class RegistrationTagModelBuilder  {
	
	private static final Logger LOGGER = LoggerFactory.getInstance(RegistrationTagModelBuilder.class);
	
	private FDUserI user;
	private RegistrationTagModel tagModel = new RegistrationTagModel();
	private AddressModel addressModel;
	private String location;
	private String origZipCode;
	
	
	public RegistrationTagModel buildTagModel() throws SkipTagException{
		
		//try to find out if user has a defaultShipToAddress (for existing users)
		identifyDefaultShipToAddress();
		
		//try to get address info of newly registered COS user
		if (addressModel == null) {
			identifyFirstShipToAddress();
		}
		
		//address info of newly registered residential user (fall back if no defaultShipToAddress or COS address exists)
		if (addressModel == null){
			//user.getAddress() doesn't work because original zip code is lost in lite reg
			addressModel = new AddressModel(null, null, null, null, origZipCode);
		}
		
		if (addressModel != null){
			tagModel.setRegistrantCity(addressModel.getCity());
			tagModel.setRegistrantState(addressModel.getState());
			tagModel.setRegistrantPostalCode(addressModel.getZipCode());
			tagModel.setRegistrantCountry(addressModel.getCountry());
		}
		
		tagModel.setRegistrationId(user.getPrimaryKey());
		tagModel.setRegistrantEmail(user.getUserId());

		identifyAttributes();
		return tagModel;
	}

	public void identifyDefaultShipToAddress() throws SkipTagException{
		ErpAddressModel erpAddressModel = TagModelUtil.getDefaultShipToErpAddressModel(user);
		
		if (erpAddressModel != null){
			addressModel = new AddressModel(erpAddressModel);
		}
	}
	
	public void identifyFirstShipToAddress() throws SkipTagException{
		try {
			ErpCustomerModel custModel = FDCustomerFactory.getErpCustomer(user.getIdentity().getErpCustomerPK());
			List<ErpAddressModel> addresses = custModel.getShipToAddresses();
			if (addresses != null && addresses.size()>0){
				addressModel = new AddressModel(addresses.get(0));
			}
			
		} catch (FDResourceException e) {
			LOGGER.error(e);
			throw new SkipTagException("FDResourceException occured", e);
		}
	}
	
	public void identifyAttributes() throws SkipTagException{
		Map<Integer, String> attributesMap = tagModel.getAttributesMaps();
		
		if (location != null){
			attributesMap.put(2, location);
		}
		
		attributesMap.put(3, Integer.toString(TagModelUtil.getOrderCount(user)));
		attributesMap.put(4, user.getCohortName());
	}
	
	public void setUser(FDUserI user) {
		this.user = user;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}

	public void setOrigZipCode(String origZipCode) {
		this.origZipCode = origZipCode;
	}
}
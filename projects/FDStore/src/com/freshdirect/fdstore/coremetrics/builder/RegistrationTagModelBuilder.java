package com.freshdirect.fdstore.coremetrics.builder;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.coremetrics.tagmodel.RegistrationTagModel;
import com.freshdirect.fdstore.customer.FDCustomerFactory;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDOrderI;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.framework.util.log.LoggerFactory;

public class RegistrationTagModelBuilder  {
	
	private static final Logger LOGGER = LoggerFactory.getInstance(RegistrationTagModelBuilder.class);

	private FDUserI user;
	private FDOrderI order;
	private boolean update;
	private RegistrationTagModel tagModel = new RegistrationTagModel();
	private AddressModel addressModel;
	
	
	public RegistrationTagModel buildTagModel() throws SkipTagException{
		
		//try to find out if user has a defaultShipToAddress (for existing users)
		if (update) {
			identifyDefaultShipToAddress();

		} else {
			//try to get address info of newly registered COS user
			identifyFirstShipToAddress();
		}
		
		//address info of newly registered residential user (fall back if no defaultShipToAddress or COS address exists)
		if (addressModel == null){
			addressModel = user.getAddress();
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
		try {
			FDIdentity identity = user.getIdentity();
			String addrPk = FDCustomerManager.getDefaultShipToAddressPK(identity);
			if (addrPk!=null){
				ErpAddressModel erpAddressModel = FDCustomerManager.getAddress(identity, addrPk);
				if (erpAddressModel != null){
					addressModel = new AddressModel(erpAddressModel);
				}
			}
		} catch (FDResourceException e) {
			LOGGER.error(e);
			throw new SkipTagException("FDResourceException occured", e);
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
		
		EnumServiceType serviceType = user.getUserServiceType();
		attributesMap.put(1, serviceType==null ? "" : serviceType.toString());
		
		if (addressModel != null){
			attributesMap.put(2, addressModel.toShortString(EnumServiceType.CORPORATE.equals(serviceType), " / "));
		}
		
		if (order != null){
			attributesMap.put(3, TagModelUtil.getCmOrderId(order));
		}
		
		attributesMap.put(4, user.getCohortName());
	}
	
	public void setUser(FDUserI user) {
		this.user = user;
	}
		
	public void setOrder(FDOrderI order){
		this.order = order;
		setUpdate(true);
	}
	
	public void setUpdate(boolean update){
		this.update = update;
	}

}
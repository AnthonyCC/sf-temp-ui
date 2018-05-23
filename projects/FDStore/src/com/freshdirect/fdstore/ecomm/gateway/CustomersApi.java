package com.freshdirect.fdstore.ecomm.gateway;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collection;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.freshdirect.crm.CrmAgentModel;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.customer.EnumPaymentMethodDefaultType;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPromotionHistory;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.customer.DefaultPaymentMethodData;
import com.freshdirect.ecommerce.data.customer.ProfileData;
import com.freshdirect.ecommerce.data.ecoupon.CrmAgentModelData;
import com.freshdirect.ecommerce.data.list.FDActionInfoData;
import com.freshdirect.ecommerce.data.list.RenameCustomerListData;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDActionInfo;
import com.freshdirect.fdstore.customer.ProfileModel;
import com.freshdirect.fdstore.ecomm.converter.ListConverter;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CustomersApi extends AbstractEcommService implements CustomersApiClientI {

	private final static Category LOGGER = LoggerFactory.getInstance(CustomersApi.class);
	
	private static CustomersApiClientI INSTANCE=new CustomersApi();
	
	public static CustomersApiClientI getInstance() {
		
		return INSTANCE;
	}
	
	public Collection<ErpPaymentMethodI> getPaymentMethods(String customerId) throws FDResourceException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean isActive(String customerId) throws FDResourceException {
		
			String data= httpGetData(getFdCommerceEndPoint(EndPoints.STATUS.getValue()), String.class, new Object[]{customerId});
			String isActive="";
			try {
				
				Response<String> response= getMapper().readValue(data, new TypeReference<Response<String>>() { });
				isActive=response.getData();
			} catch (JsonParseException e) {
				throw new FDResourceException(e);
			} catch (JsonMappingException e) {
				throw new FDResourceException(e);
			} catch (IOException e) {
				throw new FDResourceException(e);
			}
			return Boolean.getBoolean(isActive);
		
	}
	

	@Override
	public String setActive(String customerId) throws FDResourceException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String setInActive(String customerId) throws FDResourceException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getCustomerIdForUserId(String userId)throws FDResourceException {
		
		String data="";
		String customerId="";
		data = httpGetData(getFdCommerceEndPoint(EndPoints.CUSTOMER_ID_FOR_USER.getValue() +userId), String.class);
		try {
			Response<String> response= getMapper().readValue(data, new TypeReference<Response<String>>() { });
			customerId=response.getData();
		} catch (JsonParseException e) {
				throw new FDResourceException(e);
		} catch (JsonMappingException e) {
				throw new FDResourceException(e);
		} catch (IOException e) {
				throw new FDResourceException(e);
		}
		return customerId;
	}


	@Override
	public String getDefaultPaymentMethodId(String fdCustomerId) throws FDResourceException {
		
		String data="";
		String defaultPaymentMethodId="";
		data = httpGetData(getFdCommerceEndPoint(EndPoints.DEFAULT_PAYMENT_METHOD_ID_FOR_FDCUSTOMER.getValue() ), String.class, new Object[]{fdCustomerId});
		try {
			Response<String> response= getMapper().readValue(data, new TypeReference<Response<String>>() { });
			defaultPaymentMethodId=response.getData();
		} catch (JsonParseException e) {
				throw new FDResourceException(e);
		} catch (JsonMappingException e) {
				throw new FDResourceException(e);
		} catch (IOException e) {
				throw new FDResourceException(e);
		}
		return defaultPaymentMethodId;	
	}
	
	public static void main (String[] a) throws FDResourceException, RemoteException {
			
			CustomersApi customer =new CustomersApi();
			/*
			System.out.println(customer.isActive("12345"));
			System.out.println(customer.isActive("10236962595"));
			*/
			//System.out.println(customer.getCustomerIdForUserId("jayatest@gmail.com"));
			 // System.out.println(customer.getDefaultPaymentMethodId("10236962695"));
			  //System.out.println(customer.getDefaultShippingAddressId("10236962695"));//10236962695
			//System.out.println(customer.getProfile("10236962695"));//10236962695
			System.out.println(customer.getPromotionHistory("10236962595"));
			//System.out.println(customer.getProfileForCustomerId("10236962595"));//10236962695 
			
		}


	@Override
	public String getPaymentMethodDefaultType(String fdCustomerId)throws FDResourceException {
		String data="";
		String paymentMethodDefaultType="";
		//data = httpGetData(getFdCommerceEndPoint(EndPoints.PAYMENT_METHOD_DEFAULT_TYPE_FOR_FDCUSTOMER.getValue() +fdCustomerId), String.class);
		data = httpGetData(getFdCommerceEndPoint(EndPoints.PAYMENT_METHOD_DEFAULT_TYPE_FOR_FDCUSTOMER.getValue() ), String.class, new Object[]{fdCustomerId});
		try {
			Response<String> response= getMapper().readValue(data, new TypeReference<Response<String>>() { });
			paymentMethodDefaultType=response.getData();
		} catch (JsonParseException e) {
				throw new FDResourceException(e);
		} catch (JsonMappingException e) {
				throw new FDResourceException(e);
		} catch (IOException e) {
				throw new FDResourceException(e);
		}
		return paymentMethodDefaultType;	
	}


	@Override
	public String getDefaultShippingAddressId(String fdCustomerId) throws FDResourceException {
		
		String data="";
		String defaultShippingAddressId="";
		//data = httpGetData(getFdCommerceEndPoint(EndPoints.DEFAULT_SHIPPING_ADDRESS_ID_FOR_FDCUSTOMER.getValue() +fdCustomerId), String.class);
		data = httpGetData(getFdCommerceEndPoint(EndPoints.DEFAULT_SHIPPING_ADDRESS_ID_FOR_FDCUSTOMER.getValue() ), String.class, new Object[]{fdCustomerId});
		try {
			Response<String> response= getMapper().readValue(data, new TypeReference<Response<String>>() { });
			defaultShippingAddressId=response.getData();
		} catch (JsonParseException e) {
				throw new FDResourceException(e);
		} catch (JsonMappingException e) {
				throw new FDResourceException(e);
		} catch (IOException e) {
				throw new FDResourceException(e);
		}
		return defaultShippingAddressId;	

	}
	
	@Override
    public ProfileModel getProfile(String fdCustomerId) throws FDResourceException {
		
		String data="";
		ProfileData profileData=null;
		data = httpGetData(getFdCommerceEndPoint(EndPoints.PROFILES.getValue() ), String.class, new Object[]{fdCustomerId});
		try {
			Response<ProfileData> response= getMapper().readValue(data, new TypeReference<Response<ProfileData>>() { });
			profileData=response.getData();
			ProfileModel model=new ProfileModel();
			model.setPK(new PrimaryKey(fdCustomerId));
			model.setAttributes(profileData.getAttributes());
			return model;
			
		} catch (JsonParseException e) {
				throw new FDResourceException(e);
		} catch (JsonMappingException e) {
				throw new FDResourceException(e);
		} catch (IOException e) {
				throw new FDResourceException(e);
		}
			

	}

	@Override
    public ProfileModel getProfileForCustomerId(String customerId) throws FDResourceException {
		
		String data="";
		ProfileModel profileData=null;
		data = httpGetData(getFdCommerceEndPoint(EndPoints.PROFILES_FOR_CUSTOMER.getValue() ), String.class, new Object[]{customerId});
		try {
			Response<ProfileModel> response= getMapper().readValue(data, new TypeReference<Response<ProfileModel>>() { });
			profileData=response.getData();
			
			return profileData;
			
		} catch (JsonParseException e) {
				throw new FDResourceException(e);
		} catch (JsonMappingException e) {
				throw new FDResourceException(e);
		} catch (IOException e) {
				throw new FDResourceException(e);
		}
			

	}

	@Override
	public ErpPromotionHistory getPromotionHistory(String customerId) throws FDResourceException {
		
		String data="";
		data = httpGetData(getFdCommerceEndPoint(EndPoints.PROMOTION_HISTORY.getValue() ), String.class, new Object[]{customerId});
		try {
			Response<ErpPromotionHistory> response= getMapper().readValue(data, new TypeReference<Response<ErpPromotionHistory>>() { });
			return response.getData();
			
			
		} catch (JsonParseException e) {
				throw new FDResourceException(e);
		} catch (JsonMappingException e) {
				throw new FDResourceException(e);
		} catch (IOException e) {
				throw new FDResourceException(e);
		}
			
	}


	@Override
	public void setDefaultPaymentMethod( FDActionInfo info,
			                             PrimaryKey paymentMethodPK, 
			                             EnumPaymentMethodDefaultType type,
			                             boolean isDebitCardSwitch ) throws FDResourceException, RemoteException {
		
		DefaultPaymentMethodData defaultpymtMethodData=new DefaultPaymentMethodData();
		info.setPaymentDefaultType(type);
		FDActionInfoData actionInfoData=get(info);
		defaultpymtMethodData.setActionInfo(actionInfoData);
		defaultpymtMethodData.setFdCustomerId(actionInfoData.getFdCustomerId());
		defaultpymtMethodData.setDefaultType(type.getName());
		defaultpymtMethodData.setDebitCardSwitch(isDebitCardSwitch);
		defaultpymtMethodData.setPaymentMethodId(paymentMethodPK.getId());
		
		Request<DefaultPaymentMethodData> request = new Request<DefaultPaymentMethodData>();
		Response<String> response = new Response<String>();
		try{
			request.setData(defaultpymtMethodData);
			String inputJson = buildRequest(request);
			response = postData(inputJson,getFdCommerceEndPoint(EndPoints.DEFAULT_PAYMENT_METHOD_FOR_FDCUSTOMER.getValue()),Response.class,new Object[]{defaultpymtMethodData.getFdCustomerId()});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		
		
		
	}
	
	private FDActionInfoData get(FDActionInfo actionInfo) {
		
		FDActionInfoData data=new FDActionInfoData();
		if (actionInfo.getType()!=null)
			data.setAccountActivityType(actionInfo.getType().getCode());
		data.setAgent(getCrmAgentModel(actionInfo.getAgent()));
		data.setDebitCardSwitch(actionInfo.isDebitCardSwitch());
		if(actionInfo.getIdentity()!=null) {
			data.setErpCustomerId(actionInfo.getIdentity().getErpCustomerPK());
			data.setFdCustomerId(actionInfo.getIdentity().getFDCustomerPK());
		}
		data.seteStore(actionInfo.geteStore().getContentId());
		data.setFdUserId(actionInfo.getFdUserId());
		data.setInitiator(actionInfo.getInitiator());
		data.setMasqueradeAgent(actionInfo.getMasqueradeAgentTL());
		data.setNote(actionInfo.getNote());
		data.setPaymentMethodDefaultType(actionInfo.getPaymentDefaultType().getName());
		data.setPR1(actionInfo.isPR1());
		data.setSource(actionInfo.getSource().getCode());
		if(actionInfo.getTaxationType()!=null)
			data.setTaxationType(actionInfo.getTaxationType().getCode());
		
		
		return data;
		
	}
	
private CrmAgentModelData getCrmAgentModel(CrmAgentModel agentModel) {
		
		if(agentModel==null) 
			return null;
		
		CrmAgentModelData agent=new CrmAgentModelData();
		agent.setUserId(agentModel.getUserId());
		agent.setPassword(agentModel.getPassword());
		agent.setFirstName(agentModel.getFirstName());
		agent.setLastName(agentModel.getLastName());
		agent.setActive(agentModel.isActive());
		agent.setRoleCode(agentModel.getRoleCode());
		agent.setLdapId(agentModel.getLdapId());
		agent.setMasqueradeAllowed(agentModel.isMasqueradeAllowed());
		agent.setCurFacilityContext(agentModel.getCurFacilityContext());
		
		return agent;
	}
}

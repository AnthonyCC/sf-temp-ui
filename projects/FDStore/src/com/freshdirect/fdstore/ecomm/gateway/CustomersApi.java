package com.freshdirect.fdstore.ecomm.gateway;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collection;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.freshdirect.customer.ErpPaymentMethodI;
import com.freshdirect.customer.ErpPromotionHistory;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.customer.ProfileData;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.ProfileModel;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;

public class CustomersApi extends AbstractEcommService implements CustomersApiClientI {

	private final static Category LOGGER = LoggerFactory.getInstance(CustomersApi.class);
	
	private static CustomersApiClientI INSTANCE=new CustomersApi();
	
	public Collection<ErpPaymentMethodI> getPaymentMethods(String customerId) throws FDResourceException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String isActive(String customerId) throws FDResourceException {
		
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
			return isActive;
		
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
}

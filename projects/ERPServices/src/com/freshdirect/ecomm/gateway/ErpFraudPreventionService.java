package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.common.address.ContactAddressModel;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.crm.CrmAgentRole;
import com.freshdirect.customer.EnumFraudReason;
import com.freshdirect.customer.ErpAbstractOrderModel;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.customer.ErpCustomerModel;
import com.freshdirect.ecomm.converter.ErpFraudPreventionConverter;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.customer.OrderFraudData;
import com.freshdirect.ecommerce.data.dlv.ContactAddressData;
import com.freshdirect.ecommerce.data.dlv.ErpAddressData;
import com.freshdirect.ecommerce.data.dlv.PhoneNumberData;
import com.freshdirect.ecommerce.data.sap.ErpCustomerData;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.core.PrimaryKey;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ErpFraudPreventionService extends AbstractEcommService implements ErpFraudPreventionServiceI {

	private static ErpFraudPreventionService INSTANCE;

	private final static Category LOGGER = LoggerFactory
			.getInstance(ErpFraudPreventionService.class);

	private static final String CHECK_REGISTRATION_FRAUD = "fraud/registration/check";
	private static final String PRE_CHECK_ORDER_FRAUD = "fraud/order/precheck";
	private static final String POST_CHECK_ORDER_FRAUD = "fraud/order/postcheck";
	private static final String CHECK_SHIP_TO_ADDRESS_FRAUD = "fraud/shipAddress/check/erpCustomerId/";
	private static final String CHECK_BILL_TO_ADDRESS_FRAUD = "fraud/billAddress/check/erpCustomerId/";
	private static final String CHECK_PHONE_FRAUD = "fraud/phone/check/erpCustomerId/";
	private static final String PRE_CHECK_GIFTCARD_FRAUD = "fraud/giftCard/precheck";
	private static final String POST_CHECK_GIFTCARD_FRAUD = "fraud/giftCard/postCheck";
	private static final String PRE_CHECK_DONATION_FRAUD = "fraud/donation/precheck";
	private static final String POST_CHECK_DONATION_FRAUD = "fraud/donation/postCheck";
	
	
	public static ErpFraudPreventionServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ErpFraudPreventionService();

		return INSTANCE;
	}

	@Override
	public Set<EnumFraudReason> checkRegistrationFraud(ErpCustomerModel erpCustomer) throws RemoteException {
		Response<Set<String>> response = null;
		Request<ErpCustomerData> request = new Request<ErpCustomerData>();
		String inputJson;
		 Set<EnumFraudReason> fraudReason = new HashSet<EnumFraudReason>();
		try{
			request.setData(ErpFraudPreventionConverter.buildErpCustomerData(erpCustomer));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(CHECK_REGISTRATION_FRAUD),new TypeReference<Response<Set<String>>>() {});
			if(!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());
			for (String farudReasonData : response.getData()) {
				fraudReason.add(EnumFraudReason.getEnum(farudReasonData));
			}
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return fraudReason;
	}

	@Override
	public EnumFraudReason preCheckOrderFraud(PrimaryKey erpCustomerPk,ErpAbstractOrderModel order, CrmAgentRole agentRole)
			throws RemoteException {
		Response<String> response = null;
		Request<OrderFraudData> request = new Request<OrderFraudData>();
		String inputJson;
		try{
			request.setData(ErpFraudPreventionConverter.buildOrderFraudData(erpCustomerPk,order,agentRole,null));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(PRE_CHECK_ORDER_FRAUD),new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return EnumFraudReason.getEnum(response.getData());
	}

	@Override
	public void postCheckOrderFraud(PrimaryKey salePk,PrimaryKey erpCustomerPk, ErpAbstractOrderModel order,
			CrmAgentRole agentRole) throws RemoteException {
		Request<OrderFraudData> request = new Request<OrderFraudData>();
		String inputJson;
		try{
			request.setData(ErpFraudPreventionConverter.buildOrderFraudData(erpCustomerPk,order,agentRole,salePk));
			inputJson = buildRequest(request);
			postDataTypeMap(inputJson,getFdCommerceEndPoint(POST_CHECK_ORDER_FRAUD),new TypeReference<Response<String>>() {});
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public boolean checkShipToAddressFraud(String erpCustomerId,ErpAddressModel address) throws RemoteException {
		Response<Boolean> response = null;
		Request<ErpAddressData> request = new Request<ErpAddressData>();
		String inputJson;
		try{
			request.setData(ErpFraudPreventionConverter.buildErpAddressData(address));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(CHECK_SHIP_TO_ADDRESS_FRAUD+erpCustomerId),new TypeReference<Response<Boolean>>() {});
			if(!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public boolean checkBillToAddressFraud(String erpCustomerId,ContactAddressModel address) throws RemoteException {
		Response<Boolean> response = null;
		Request<ContactAddressData> request = new Request<ContactAddressData>();
		String inputJson;
		try{
			request.setData(ErpFraudPreventionConverter.buildContactAddressData(address));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(CHECK_BILL_TO_ADDRESS_FRAUD+erpCustomerId),new TypeReference<Response<Boolean>>() {});
			if(!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public boolean checkPhoneFraud(String erpCustomerId,Collection<PhoneNumber> phones) throws RemoteException {
		Response<Boolean> response = null;
		Request<Collection<PhoneNumberData>> request = new Request<Collection<PhoneNumberData>>();
		String inputJson;
		try{
			request.setData(ErpFraudPreventionConverter.buildPhoneNumberCollectionData(phones));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(CHECK_PHONE_FRAUD+erpCustomerId),new TypeReference<Response<Boolean>>() {});
			if(!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}

	@Override
	public EnumFraudReason preCheckGiftCardFraud(PrimaryKey erpCustomerPk,ErpAbstractOrderModel order, CrmAgentRole agentRole)
			throws RemoteException {
		Response<String> response = null;
		Request<OrderFraudData> request = new Request<OrderFraudData>();
		String inputJson;
		try{
			request.setData(ErpFraudPreventionConverter.buildOrderFraudData(erpCustomerPk,order,agentRole,null));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(PRE_CHECK_GIFTCARD_FRAUD),new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return EnumFraudReason.getEnum(response.getData());
	}

	@Override
	public void postCheckGiftCardFraud(PrimaryKey salePk,PrimaryKey erpCustomerPk, ErpAbstractOrderModel order,
			CrmAgentRole agentRole) throws RemoteException {
		Request<OrderFraudData> request = new Request<OrderFraudData>();
		String inputJson;
		try{
			request.setData(ErpFraudPreventionConverter.buildOrderFraudData(erpCustomerPk,order,agentRole,salePk));
			inputJson = buildRequest(request);
			postDataTypeMap(inputJson,getFdCommerceEndPoint(POST_CHECK_GIFTCARD_FRAUD),new TypeReference<Response<String>>() {});
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public EnumFraudReason preCheckDonationFraud(PrimaryKey erpCustomerPk,ErpAbstractOrderModel order, CrmAgentRole agentRole)
			throws RemoteException {
		Response<String> response = null;
		Request<OrderFraudData> request = new Request<OrderFraudData>();
		String inputJson;
		try{
			request.setData(ErpFraudPreventionConverter.buildOrderFraudData(erpCustomerPk,order,agentRole,null));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(PRE_CHECK_DONATION_FRAUD),new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK"))
				throw new FDResourceException(response.getMessage());
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		return EnumFraudReason.getEnum(response.getData());
	}

	@Override
	public void postCheckDonationFraud(PrimaryKey salePk,PrimaryKey erpCustomerPk, ErpAbstractOrderModel order,
			CrmAgentRole agentRole) throws RemoteException {
		Request<OrderFraudData> request = new Request<OrderFraudData>();
		String inputJson;
		try{
			request.setData(ErpFraudPreventionConverter.buildOrderFraudData(erpCustomerPk,order,agentRole,salePk));
			inputJson = buildRequest(request);
			postDataTypeMap(inputJson,getFdCommerceEndPoint(POST_CHECK_DONATION_FRAUD),new TypeReference<Response<String>>() {});
		} catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
	}

}

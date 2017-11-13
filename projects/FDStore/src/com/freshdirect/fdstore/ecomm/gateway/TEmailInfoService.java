package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.customer.FDCustomerInfoData;
import com.freshdirect.ecommerce.data.dlv.PhoneNumberData;
import com.freshdirect.ecommerce.data.mail.TransEmailInfoData;
import com.freshdirect.ecommerce.data.temails.SendMailData;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerInfo;
import com.freshdirect.fdstore.temails.TEmailConstants;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.EnumTranEmailType;

public class TEmailInfoService extends AbstractEcommService implements TEmailInfoServiceI{
	
	private final static Category LOGGER = LoggerFactory.getInstance(FDReferralManagerService.class);
	private static final String SEND_EMAIL = "temailInfo/sendEmail";
	private static final String SEND_FAILED_TRANSACTION = "temailInfo/failedTransactions/send/timeout/";
	private static final String GET_FAILED_TRANSACTIOn_LIST = "temailInfo/failedTransactions/maxCount/";
	private static final String GET_FAILED_TRANSACTION_STAT = "temailInfo/failedTransactions/stats";
	private static TEmailInfoService INSTANCE;
	
	public static TEmailInfoServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new TEmailInfoService();

		return INSTANCE;
	}

	@Override
	public void sendEmail(EnumTranEmailType tranType, Map input)throws FDResourceException, RemoteException {
		Request<SendMailData> request = new Request<SendMailData>();
		Response<String> response = new Response<String>();
		try{
			SendMailData sendMail = new SendMailData();
			sendMail.setInput(buildSendMailInput(input));
			sendMail.setTranType(tranType.getName());
			request.setData(sendMail);
			String inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(SEND_EMAIL),new TypeReference<Response<String>>() {});
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

	private Map<String, Object> buildSendMailInput( Map<String, Object>  input) {
		 Map<String, Object> response = new HashMap<String, Object>();
		for ( String key : input.keySet()) {
				if(TEmailConstants.CUSTOMER_ID_INP_KEY.equals(key)){
					FDCustomerInfo customerInfo = (FDCustomerInfo) input.get(key);
					FDCustomerInfoData customerInfoData = buildFDcustomerInfoData(customerInfo);
					response.put(key, customerInfoData);
				}
				else if(TEmailConstants.EMAIL_TYPE.equals(key) || "oasQuery".equals(key)){
					 String type =  (String) input.get(key);
					 response.put(key, type);
				}
				/*else if (TEmailConstants.ORDER_INP_KEY.equals(key)){
					FDOrderI order = (FDOrderI) input.get(key);
					
				}*/
				
		}
		return response;
	}

	public  FDCustomerInfoData buildFDcustomerInfoData(FDCustomerInfo customerInfo) {
		FDCustomerInfoData customerInfoData = new FDCustomerInfoData();
		customerInfoData.setAltEmailAddress(customerInfo.getAltEmailAddress());
		customerInfoData.setCellPhone(buildPhoneData(customerInfo.getCellPhone()));
		customerInfoData.setChefsTable(customerInfo.isChefsTable());
		customerInfoData.setCorporateUser(customerInfo.isCorporateUser());
		customerInfoData.setCustomerCredit(customerInfo.getCustomerCredit());
		customerInfoData.setCustomerServiceContact(customerInfo.getCustomerServiceContact());
		customerInfoData.setDepotCode(customerInfo.getDepotCode());
		customerInfoData.setEligibleForPromotion(customerInfo.isEligibleForPromotion());
		customerInfoData.setEmailAddress(customerInfo.getEmailAddress());
		customerInfoData.setFirstName(customerInfo.getFirstName());
		customerInfoData.setGoGreen(customerInfo.isGoGreen());
		customerInfoData.setHomePhone(buildPhoneData(customerInfo.getHomePhone()));
		customerInfoData.setHtmlEmail(customerInfo.isHtmlEmail());
		customerInfoData.setLastName(customerInfo.getLastName());
		customerInfoData.setLastOrderId(customerInfo.getLastOrderId());
		customerInfoData.setNumberOfOrders(customerInfo.getNumberOfOrders());
		customerInfoData.setPickupOnly(customerInfo.isPickupOnly());
		customerInfoData.setSegmentCode(customerInfo.getSegmentCode());
		customerInfoData.setUserGiftCardsBalance(customerInfo.getUserGiftCardsBalance());
		customerInfoData.setWorkPhone(buildPhoneData(customerInfo.getWorkPhone()));
		return customerInfoData;
	}

	private PhoneNumberData buildPhoneData(PhoneNumber phone) {
		PhoneNumberData phoneData = null;
		if(phone != null)
		 phoneData = new PhoneNumberData(phone.getPhone(), phone.getExtension(), phone.getType());
		return phoneData;
	}

	@Override
	public void sendFailedTransactions(int timeout) throws RemoteException {
		Response<String> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(SEND_FAILED_TRANSACTION +timeout),  new TypeReference<Response<String>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	}

	@Override
	public List getFailedTransactionList(int max_count,	boolean isMailContentReqd) throws RemoteException {
		Response<List<TransEmailInfoData>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_FAILED_TRANSACTIOn_LIST+max_count+"/isMailContentReqd/"+isMailContentReqd),  new TypeReference<Response<List<TransEmailInfoData>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return ModelConverter.buildTransMail(response.getData());
	}

	@Override
	public Map getFailedTransactionStats() throws RemoteException {
		Response<Map<String,Integer>> response = null;
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(GET_FAILED_TRANSACTION_STAT),  new TypeReference<Response<Map<String,Integer>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	}
	
	

}

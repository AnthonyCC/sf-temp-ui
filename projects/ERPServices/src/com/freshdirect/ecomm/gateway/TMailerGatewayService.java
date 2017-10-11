package com.freshdirect.ecomm.gateway;

import java.rmi.RemoteException;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.mail.EmailAddressData;
import com.freshdirect.ecommerce.data.mail.TransEmailInfoData;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.mail.EmailAddress;
import com.freshdirect.framework.mail.TEmailI;
import com.freshdirect.framework.util.log.LoggerFactory;

public class TMailerGatewayService extends AbstractEcommService implements TMailerGatewayServiceI{
	
	private static TMailerGatewayService INSTANCE;
	
	private final static Category LOGGER = LoggerFactory.getInstance(TMailerGatewayService.class);

	private static final String NOTIFY_FAILED_TRANSACTION_EMAILS = "tMailer/failedTransaction/notify";
	private static final String ENQUEUE = "tMailer/enqueue";
	
	public static TMailerGatewayServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new TMailerGatewayService();

		return INSTANCE;
	}

	@Override
	public void enqueue(TEmailI email) throws RemoteException {
		Request<TransEmailInfoData> request = new Request<TransEmailInfoData>();
		String inputJson;
		Response<String> response = null;
		try{
			request.setData(buildTransMailInfoData(email));
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(ENQUEUE), new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}

	private TransEmailInfoData buildTransMailInfoData(TEmailI email) {
		TransEmailInfoData transEmailInfoData = new TransEmailInfoData();
		transEmailInfoData.setBccList(email.getBCCList());
		transEmailInfoData.setCcList(email.getCCList());
		transEmailInfoData.setCroModDate(email.getCroModDate());
		transEmailInfoData.setCustomerId(email.getCustomerId());
		transEmailInfoData.setEmailContent(email.getEmailContent());
		if(email.getEmailStatus() != null)
		transEmailInfoData.setEmailStatus(email.getEmailStatus());
		if( email.getEmailTransactionType()!= null)
		transEmailInfoData.setEmailTransactionType(email.getEmailTransactionType());
		if(email.getEmailType() != null)
		transEmailInfoData.setEmailType(email.getEmailType());
		transEmailInfoData.setFrom(buildEmailAddressData(email.getFromAddress()));
		transEmailInfoData.setId(email.getId());
		transEmailInfoData.setOasQueryString(email.getOasQueryString());
		transEmailInfoData.setOrderId(email.getOrderId());
		transEmailInfoData.setProductionReady(email.isProductionReady());
		if(email.getProvider() != null)
		transEmailInfoData.setProvider((email.getProvider()));
		transEmailInfoData.setRecipient(email.getRecipient());
		transEmailInfoData.setSubject(email.getSubject());
		transEmailInfoData.setTargetProgId(email.getTargetProgId());
		transEmailInfoData.setTemplateId(email.getTemplateId());
		return transEmailInfoData;
	}

	private EmailAddressData buildEmailAddressData(EmailAddress fromAddress) {
		EmailAddressData emailAddressData = new EmailAddressData();
		emailAddressData.setAddress(fromAddress.getAddress());
		emailAddressData.setName(fromAddress.getName());
		return emailAddressData;
	}

	@Override
	public void notifyFailedTransactionEmails() throws RemoteException {
		Request<TransEmailInfoData> request = new Request<TransEmailInfoData>();
		String inputJson;
		Response<String> response = null;
		try{
			request.setData(null);
			inputJson = buildRequest(request);
			response = postDataTypeMap(inputJson,getFdCommerceEndPoint(NOTIFY_FAILED_TRANSACTION_EMAILS), new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		}  catch (FDEcommServiceException e) {
			throw new RemoteException(e.getMessage());
		} catch (FDResourceException e) {
			throw new RemoteException(e.getMessage());
		}
		
	}

}

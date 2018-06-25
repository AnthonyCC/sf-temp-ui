package com.freshdirect.fdstore.ecomm.gateway;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freshdirect.customer.EnumNotificationType;
import com.freshdirect.customer.EnumSaleStatus;
import com.freshdirect.customer.ErpTransactionException;
import com.freshdirect.ecomm.gateway.AbstractEcommService;
import com.freshdirect.ecommerce.data.common.Request;
import com.freshdirect.ecommerce.data.common.Response;
import com.freshdirect.ecommerce.data.list.CustomerCreatedListData;
import com.freshdirect.erp.model.NotificationModel;
import com.freshdirect.fdstore.FDEcommServiceException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDRuntimeException;
import com.freshdirect.fdstore.ecomm.converter.ListConverter;
import com.freshdirect.fdstore.lists.FDCustomerListExistsException;
import com.freshdirect.framework.util.log.LoggerFactory;

public class PostSettlementNotifyService extends AbstractEcommService implements PostSettlementNotifyServiceI {

	
	private final static Category LOGGER = LoggerFactory.getInstance(PostSettlementNotifyService.class);


	private static final String NOTIFICATION_UPDATE = "postSettleNotify/update"; 
	private static final String NOTIFICATION_FIND_BY_STATUS_TYPE = "postSettleNotify/findByStatusAndType/";
	private static final String NOTIFICATION_FIND_BY_SALE_TYPE = "postSettleNotify/findBySaleIdAndType/";
	private static final String COMMIT_TO_AVALARA = "postSettleNotify/commitToAvalara";	
	
	private static PostSettlementNotifyServiceI INSTANCE;
	
	
	public static PostSettlementNotifyServiceI getInstance() {
		if (INSTANCE == null)
			INSTANCE = new PostSettlementNotifyService();
		return INSTANCE;
	}
	
	public void updateNotification(NotificationModel notificationModel) throws RemoteException, ErpTransactionException {
	
		Request<NotificationModel> request = new Request<NotificationModel>();
		Response<String> response = new Response<String>();
		try{
			request.setData(notificationModel);
			String inputJson = buildRequest(request);
			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(NOTIFICATION_UPDATE),  new TypeReference<Response<String>>() {});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new ErpTransactionException(e.getMessage());
		} catch (FDEcommServiceException e) {
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
	
	}
	public Collection<String> findByStatusAndType(EnumSaleStatus saleStatus, EnumNotificationType type) throws RemoteException {

		Response<Collection<String>> response = new Response<Collection<String>>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(NOTIFICATION_FIND_BY_STATUS_TYPE+"saleStatus/"+saleStatus.getStatusCode()+"/notificationtype/"+type.getCode()),  new TypeReference<Response<Collection<String>>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	
	}
	public NotificationModel findBySalesIdAndType(String salesId, EnumNotificationType type) throws RemoteException {


		Response<NotificationModel> response = new Response<NotificationModel>();
		try {
			response = this.httpGetDataTypeMap(getFdCommerceEndPoint(NOTIFICATION_FIND_BY_SALE_TYPE+"saleId/"+salesId+"/notificationtype/"+type.getCode()),  new TypeReference<Response<NotificationModel>>(){});
			if(!response.getResponseCode().equals("OK")){
				throw new FDResourceException(response.getMessage());
			}
		} catch (FDRuntimeException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}catch (FDResourceException e){
			LOGGER.error(e.getMessage());
			throw new RemoteException(e.getMessage());
		}
		return response.getData();
	
		}
	public List<String> commitToAvalara(Collection<String> pendingNotifications) throws RemoteException {
	
		Request<Collection<String>> request = new Request<Collection<String>>();
		Response<List<String>> response = new Response<List<String>>();
		try{
			request.setData(pendingNotifications);
			String inputJson = buildRequest(request);
			response = this.postDataTypeMap(inputJson, getFdCommerceEndPoint(COMMIT_TO_AVALARA),  new TypeReference<Response<List<String>>>() {});
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
	return response.getData();
	}

}

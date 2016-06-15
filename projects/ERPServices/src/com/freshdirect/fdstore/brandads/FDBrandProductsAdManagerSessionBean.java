package com.freshdirect.fdstore.brandads;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.freshdirect.common.ERPSessionBeanSupport;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdInfo;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdRequest;
import com.freshdirect.fdstore.brandads.model.HLBrandProductAdResponse;
import com.freshdirect.fdstore.brandads.service.BrandProductAdServiceException;
import com.freshdirect.framework.util.log.LoggerFactory;
/**
 * 
 * @author sathishkumar Merugu
 *
 */
public class FDBrandProductsAdManagerSessionBean extends ERPSessionBeanSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getInstance(FDBrandProductsAdManagerSessionBean.class);
	public HLBrandProductAdResponse getSearchbykeyword(HLBrandProductAdRequest hLRequestData) throws FDResourceException, BrandProductAdServiceException {
		HLBrandProductAdResponse hlBrandProductAdResponse=null;
		if(!FDStoreProperties.isHookLogicBlackHoleEnabled()){
			hlBrandProductAdResponse = FDBrandProductsAdGateway.getSearchbykeyword(hLRequestData);
			return hlBrandProductAdResponse;
		}
		return null;
	}
	
	public HLBrandProductAdResponse getCategoryProducts(HLBrandProductAdRequest hLRequestData) throws FDResourceException, BrandProductAdServiceException {
		HLBrandProductAdResponse hlBrandProductAdResponse=null;
		if(!FDStoreProperties.isHookLogicBlackHoleEnabled()){
			hlBrandProductAdResponse = FDBrandProductsAdGateway.getCategoryProducts(hLRequestData);
			return hlBrandProductAdResponse;
		}
		return null;
	}
	
	public void submittedOrderdDetailsToHL(Date productsOrderFeedDate) throws FDResourceException, RemoteException {
		
			Connection	conn=null;
			Map<String, List<HLOrderFeedDataModel>> map = null;
			
		try{
			LOGGER.info("Sending sumbmitted order details to the Hooklogic");
			conn= getConnection();
			FDBrandProductsAdManagerDAO fdBrandProductsManagerDAO=new FDBrandProductsAdManagerDAO(); 
			map=fdBrandProductsManagerDAO.getOrderProductFeedDataInfo(conn, productsOrderFeedDate);
			
			sendOrderDetailsToHL(map, true);
			
			
		} catch (Exception e) {
			LOGGER.error("Getting excetption while sending sumbmitted order details to the Hooklogic :"+e);
			throw new FDResourceException(e);
		}
	
	 }
	
	public void submittedOrderdDetailsToHL(List<String> orders) throws FDResourceException, RemoteException {
		
		Connection	conn=null;
		Map<String, List<HLOrderFeedDataModel>> map = null;
		FDBrandProductsAdManagerDAO fdBrandProductsManagerDAO=null;
		
	try{
		LOGGER.info("Sending sumbmitted order details to the Hooklogic");
		conn= getConnection();
		 fdBrandProductsManagerDAO=new FDBrandProductsAdManagerDAO(); 
		for (String order : orders) {
		map=fdBrandProductsManagerDAO.getOrderProductFeedDataInfo(conn, order);
		sendOrderDetailsToHL(map, false);
		}
		
	} catch (Exception e) {
		LOGGER.error("Getting excetption while sending sumbmitted order details to the Hooklogic :"+e);
		throw new FDResourceException(e);
	}

 }
	
	

	private void sendOrderDetailsToHL(
			Map<String, List<HLOrderFeedDataModel>> map, boolean isOrderFeedLog)
			throws BrandProductAdServiceException {
		String lastOrderId = null;
		Date lastOrderTime = null;
		Date startTime = new Date();
		if(null != map){
			LOGGER.info("Found "+map.entrySet().size()+"  orders to be sent to HL");
			for (Entry<String, List<HLOrderFeedDataModel>> entry : map.entrySet()) {
				HLOrderFeedDataModel orderFeedDataModel=new HLOrderFeedDataModel();
				StringBuffer price=new StringBuffer("");
				StringBuffer quantity=new StringBuffer("");
				StringBuffer sku=new StringBuffer("");
				
				for (HLOrderFeedDataModel s :  entry.getValue()) {
		            	 	orderFeedDataModel.setPrice((s.getPrice()!=null && "".equals(price.toString()) ? price.append(s.getPrice()).toString():price.append("|"+s.getPrice()).toString()));
		            	 	orderFeedDataModel.setSku((s.getProdctSku()!=null && "".equals(sku.toString()) ? sku.append(s.getProdctSku()).toString():sku.append("|"+s.getProdctSku()).toString()));
		            	 	orderFeedDataModel.setQuantity((s.getQuantity()!=null && "".equals(quantity.toString()) ?  quantity.append(s.getQuantity()).toString():quantity.append("|"+s.getQuantity()).toString()));
			            	orderFeedDataModel.setClientId(s.getClientId());
			            	orderFeedDataModel.setpUserId(s.getpUserId());
			            	orderFeedDataModel.setcUserId(s.getcUserId());
			            	orderFeedDataModel.setOrderId(s.getOrderId());
			            	orderFeedDataModel.setOrderCroModDate(s.getOrderCroModDate());
			            	/*lastOrderId = s.getOrderId();
			            	lastOrderTime = s.getOrderCroModDate();*/
			            	orderFeedDataModel.setOrderTotal(s.getOrderTotal());
		        }
				try {
					FDBrandProductsAdGateway.submittedOrderdDetailsToHL(orderFeedDataModel);
					lastOrderId = orderFeedDataModel.getOrderId();
	            	lastOrderTime = orderFeedDataModel.getOrderCroModDate();
				} catch (BrandProductAdServiceException e) {
					LOGGER.warn("Failed to send order details to HL. Order Id: "+lastOrderId );
				}
		    }
			if(isOrderFeedLog && null !=lastOrderTime){
				insertOrderFeedLog(lastOrderId, lastOrderTime, startTime);
			}
			LOGGER.info("Completed sending order details to HL");
		}
	}

	private void insertOrderFeedLog(String lastOrderId, Date lastOrderTime,
			Date startTime){
		Date endTime = new Date();
		try {
			if(null !=lastOrderId && null !=lastOrderTime){
				HLOrderFeedLogModel orderFeedLogModel = new HLOrderFeedLogModel();
				orderFeedLogModel.setStartTime(startTime);
				orderFeedLogModel.setEndTime(endTime);
				orderFeedLogModel.setLastSentOrderTime(lastOrderTime);
				orderFeedLogModel.setDetails(""+lastOrderId);
				Connection conn = getConnection();
				FDBrandProductsAdManagerDAO dao = new FDBrandProductsAdManagerDAO();
				dao.insertOrderFeedLog(conn, orderFeedLogModel);
			}
		} catch (SQLException e) {
			LOGGER.warn("Exception in insertOrderFeedLog(): "+e);
		}
	}
	
	public Date getLastSentFeedOrderTime() throws FDResourceException, RemoteException{
		Date lastSentFeedOrderTime = null;
		Connection	conn=null;
		FDBrandProductsAdManagerDAO fdBrandProductsManagerDAO = new FDBrandProductsAdManagerDAO();
		try {
			conn = getConnection();
			lastSentFeedOrderTime = fdBrandProductsManagerDAO.getLastSentFeedOrderTime(conn);
		} catch (SQLException e) {
			LOGGER.error("Exception while getting lastSentFeedOrderTime: "+e);
			throw new FDResourceException("Exception while getting lastSentFeedOrderTime: "+e);
		}
		return lastSentFeedOrderTime;
	}
}
	
		
	


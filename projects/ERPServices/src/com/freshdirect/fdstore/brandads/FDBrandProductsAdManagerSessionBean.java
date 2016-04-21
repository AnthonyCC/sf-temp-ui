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
		if(FDStoreProperties.isHookLogicEnabled()){
			LOGGER.debug("HookLogic Enable.");
			hlBrandProductAdResponse = FDBrandProductsAdGateway.getSearchbykeyword(hLRequestData);
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
			FDBrandProductsManagerDAO fdBrandProductsManagerDAO=new FDBrandProductsManagerDAO(); 
			map=fdBrandProductsManagerDAO.getOrderProductFeedDataInfo(conn, productsOrderFeedDate);
			
			sendOrderDetailsToHL(map);
			
			
		} catch (Exception e) {
			LOGGER.error("Getting excetption while sending sumbmitted order details to the Hooklogic :"+e);
			throw new FDResourceException(e);
		}
	
	 }
	
	public void submittedOrderdDetailsToHL(List<String> orders) throws FDResourceException, RemoteException {
		
		Connection	conn=null;
		Map<String, List<HLOrderFeedDataModel>> map = null;
		FDBrandProductsManagerDAO fdBrandProductsManagerDAO=null;
		
	try{
		LOGGER.info("Sending sumbmitted order details to the Hooklogic");
		conn= getConnection();
		 fdBrandProductsManagerDAO=new FDBrandProductsManagerDAO(); 
		for (String order : orders) {
		map=fdBrandProductsManagerDAO.getOrderProductFeedDataInfo(conn, order);
		sendOrderDetailsToHL(map);
		}
		
	} catch (Exception e) {
		LOGGER.error("Getting excetption while sending sumbmitted order details to the Hooklogic :"+e);
		throw new FDResourceException(e);
	}

 }
	
	

	private void sendOrderDetailsToHL(
			Map<String, List<HLOrderFeedDataModel>> map)
			throws BrandProductAdServiceException {
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
			            	orderFeedDataModel.setOrderTotal(s.getOrderTotal());
		           }
				FDBrandProductsAdGateway.submittedOrderdDetailsToHL(orderFeedDataModel);
		    }
			LOGGER.info("Completed sending order details to HL");
		}
	}
	
}
	
		
	


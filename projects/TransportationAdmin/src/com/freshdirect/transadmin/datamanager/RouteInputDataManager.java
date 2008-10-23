package com.freshdirect.transadmin.datamanager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.freshdirect.routing.manager.DeliveryManager;
import com.freshdirect.routing.manager.EstimationManager;
import com.freshdirect.routing.manager.GeographyManager;
import com.freshdirect.routing.manager.IProcessManager;
import com.freshdirect.routing.manager.PlantPackagingManager;
import com.freshdirect.routing.manager.ProcessContext;
import com.freshdirect.routing.model.IOrderModel;
import com.freshdirect.routing.service.exception.RoutingProcessException;
import com.freshdirect.transadmin.datamanager.assembler.OrderModelAssembler;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.util.TransportationAdminProperties;

public class RouteInputDataManager extends RouteDataManager {
	
	public RoutingResult process(byte[] inputInfo1,byte[] inputInfo2, byte[] inputInfo3, String userName
											, Map paramMap, DomainManagerI domainManagerService) throws IOException {
		
		String outputFileName1 = null;
		String outputFileName2 = null;
		String outputFileName3 = null;
		long time = System.currentTimeMillis();
		outputFileName1 = TransportationAdminProperties.getOrderOutputFilename()+userName+time;
		outputFileName2 = TransportationAdminProperties.getLocationOutputFilename()+userName+time;
		outputFileName3 = TransportationAdminProperties.getRoutingErrorFilename()+userName+time;
		return processRoutingInput(inputInfo1, initResult(outputFileName1, outputFileName2, outputFileName3), paramMap);
		
	}
	
	private RoutingResult processRoutingInput(byte[] inputInfo, RoutingResult result, Map paramMap) {
		
		System.out.println(" ################### Process Start >"+Calendar.getInstance().getTime());
		OrderModelAssembler assembler = new OrderModelAssembler();
		List inputDataList = fileManager.parseRouteFile(TransportationAdminProperties.getErpOrderLocationOutputFormat()
														, new ByteArrayInputStream(inputInfo)
														, ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER, assembler);
		
		IProcessManager rootProcessMgr = getProcessChain();
		ProcessContext context = new ProcessContext();
		Iterator iterator = inputDataList.iterator();
				
		IOrderModel orderModel = null;
		List outputDataList = new ArrayList();
		
		fillBatchInfo(context, inputDataList);
		context.setDeliveryTypeCache(new HashMap());
		context.addProcessParam(paramMap);
		try {
			rootProcessMgr.startProcess(context);
			
			while(iterator.hasNext()) {
				
				orderModel = (IOrderModel)iterator.next();				
				context.setDataModel(orderModel);
				rootProcessMgr.process(context);
				context.addOrder(context.getDataModel());
				outputDataList.add(context.getDataModel());
			}
			
			Set sessionIds = (Set)rootProcessMgr.endProcess(context);
			result.setAdditionalInfo(sessionIds.toString());
		} catch (RoutingProcessException routExp) {
			result.addError(routExp.getIssueMessage());
		}
		
		fileManager.generateRouteFile(TransportationAdminProperties.getRoutingOrderInputFormat()
										, result.getOutputFile1(), ROW_IDENTIFIER,ROW_BEAN_IDENTIFIER
										, outputDataList, assembler);		
		fileManager.generateRouteFile(TransportationAdminProperties.getRoutingLocationInputFormat()
										, result.getOutputFile2(), ROW_IDENTIFIER,ROW_BEAN_IDENTIFIER
										, outputDataList, assembler);
		
		fileManager.generateRouteFile(TransportationAdminProperties.getErrorFormat()
										, result.getOutputFile3(), ROW_IDENTIFIER,ROW_BEAN_IDENTIFIER
										, (List)context.getProcessInfo(), null);
		
		System.out.println(" ################### Process End >"+Calendar.getInstance().getTime());
		return result;
	}
	
//	Batch group filled to generate sap loading
	private void fillBatchInfo(ProcessContext context, List dataList) {
		
		Iterator iterator = dataList.iterator();
		IOrderModel tmpInputModel = null;
		
		List outputOrderList = new ArrayList();
		List batchOrderList = new ArrayList();
			
		int intCount = 0;
		int batchCount = 500;		
		while(iterator.hasNext()) {
			
			tmpInputModel = (IOrderModel)iterator.next();			
			batchOrderList.add(tmpInputModel.getOrderNumber());
			intCount++;
			if(intCount == batchCount) {				
				outputOrderList.add(batchOrderList);
				batchOrderList = new ArrayList();
				intCount = 0;
			}
		}
		
		if(batchOrderList.size() > 0) {
			outputOrderList.add(batchOrderList);
		}	
		context.setOrderIdLst(outputOrderList);
	}
	
	//Process Chain generated
	private IProcessManager getProcessChain() {
		
		IProcessManager geoManager = new GeographyManager();
		IProcessManager estimationManager = new EstimationManager();
		IProcessManager packagingManager = new PlantPackagingManager();
		IProcessManager deliveryManager = new DeliveryManager();
	   
		packagingManager.setSuccessor(deliveryManager);
		deliveryManager.setSuccessor(geoManager);
		geoManager.setSuccessor(estimationManager);
		
		return packagingManager;
	}
}

package com.freshdirect.transadmin.datamanager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.freshdirect.routing.util.IRoutingParamConstants;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.util.TransportationAdminProperties;

public class RouteOutputDataManager extends RouteDataManager  {
	
	public RoutingResult process(byte[] inputInfo1,byte[] inputInfo2, byte[] inputInfo3, String userName, Map paramMap, DomainManagerI domainManagerService) throws IOException {
		
		String outputFileName1 = null;
		String outputFileName2 = null;
		String outputFileName3 = null;
		long time = System.currentTimeMillis();
		outputFileName1 = TransportationAdminProperties.getRoutingOutputOrderFilename()+userName+time;
		outputFileName2 = TransportationAdminProperties.getRoutingOutputTruckFilename()+userName+time;
		return processRoutingOutput(inputInfo1
										, initResult(outputFileName1, outputFileName2,outputFileName3)
											, paramMap, domainManagerService);
		
	}
	
	private RoutingResult processRoutingOutput(byte[] inputInfo, RoutingResult result, Map paramMap, DomainManagerI domainManagerService) {
		
		String cutOff = (String)paramMap.get(IRoutingParamConstants.ROUTING_CUTOFF);
		List inputDataList = fileManager.parseRouteFile(TransportationAdminProperties.getRoutingOrderRouteOutputFormat()
												, new ByteArrayInputStream(inputInfo), ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER
												, null);
		RouteGenerationResult routeGenResult = generateRouteNumber(inputDataList, cutOff, domainManagerService);
		
		inputDataList = routeGenResult.getRouteInfos();
		
		result.setRouteNoSaveInfos(routeGenResult.getRouteNoSaveInfos());
				
		fileManager.generateRouteFile(TransportationAdminProperties.getErpOrderInputFormat()
										, result.getOutputFile1(), ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER, inputDataList
										, null);
		fileManager.generateRouteFile(TransportationAdminProperties.getErpRouteInputFormat()
										, result.getOutputFile2(), ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER, inputDataList
										, null);
		return result;
	}
	
	

	
}

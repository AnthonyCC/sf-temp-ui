package com.freshdirect.transadmin.datamanager;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.freshdirect.transadmin.datamanager.model.IRoutingOutputInfo;
import com.freshdirect.transadmin.datamanager.model.ITruckScheduleInfo;
import com.freshdirect.transadmin.datamanager.model.OrderRouteInfoModel;
import com.freshdirect.transadmin.datamanager.report.ReportGenerationException;
import com.freshdirect.transadmin.datamanager.report.model.CutOffReportData;
import com.freshdirect.transadmin.model.RouteMapping;
import com.freshdirect.transadmin.model.RouteMappingId;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;

public class RouteOutputDataManager extends RouteDataManager  {
	
	private final String INVALID_ORDERROUTEFILE = "Invalid RoadNet Order/Truck File";
	private final String INVALID_TRUCKSCHEDULEFILE = "Invalid Depot Truck Schedule File : SAP Order No:";
	
	public RoutingResult process(IRoutingOutputInfo routingInfo
			,String userName, IServiceProvider serviceProvider) throws IOException {
		
		long time = System.currentTimeMillis();
		String outputFileName1 = TransportationAdminProperties.getRoutingOutputOrderFilename()+userName+time;
		String outputFileName2 = TransportationAdminProperties.getRoutingOutputTruckFilename()+userName+time;
		String outputFileName3 = TransportationAdminProperties.getRoutingCutOffRptFilename()+userName+time;
		
		RoutingResult result = processRoutingOutput(routingInfo
				, initResult(outputFileName1, outputFileName2,outputFileName3, this.getCutOffReportExtension())
				, serviceProvider);
		
		return result;
	}
	
	
	private RoutingResult processRoutingOutput(IRoutingOutputInfo routingInfo
												, RoutingResult result, IServiceProvider serviceProvider) {

		collectOrders(routingInfo, result);
		validateData(routingInfo, result, serviceProvider);
		
		if(result.getErrors() == null || result.getErrors().size() == 0) {
			preProcessRoutingOutput(routingInfo, result, serviceProvider);
			
			if(result.getErrors() == null || result.getErrors().size() == 0) {
				try {
					convertDptToRegOrders(routingInfo, result, serviceProvider);
					RouteGenerationResult routeGenResult = generateRouteNumber(result.getDepotRouteMapping()
																				, result.getRegularOrders(), routingInfo
																				, serviceProvider);

					List cutOffOrders = routeGenResult.getRouteInfos();
					List regularOrders = new ArrayList();
					regularOrders.addAll(cutOffOrders);
					
					result.setRegularOrders(regularOrders);
					
					postProcessRoutingOutput(routingInfo, result, serviceProvider);

					result.setRouteNoSaveInfos(routeGenResult.getRouteNoSaveInfos());

					fileManager.generateRouteFile(TransportationAdminProperties.getErpOrderInputFormat()
							, result.getOutputFile1(), ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER, result.getRegularOrders()
							, null);
					fileManager.generateRouteFile(TransportationAdminProperties.getErpRouteInputFormat()
							, result.getOutputFile2(), ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER, filterRoutesFromOrders(result.getRegularOrders())
							, null);

									
					CutOffReportData reportData = this.getCutOffReportData(cutOffOrders, routingInfo.getCutOff(), serviceProvider);
					this.getCutOffReportEngine().generateCutOffReport(result.getOutputFile3(), reportData);

				} catch (RouteNoGenException routeNoGen) {
					result.addError(routeNoGen.getMessage());
				} catch (ReportGenerationException reportGen) {
					result.addError(reportGen.getMessage());
				}
			} 
		}
		
		return result;
	}
	
	protected Object preProcessRoutingOutput(IRoutingOutputInfo routingInfo, RoutingResult result, IServiceProvider serviceProvider) {
		
		OrderAreaGroup orderGrp = (OrderAreaGroup)preProcessLoadData(routingInfo, result, serviceProvider);
		TrnArea _area = null;
		if(orderGrp.getTruckOrderGroup() != null) {
			Iterator _iterator = orderGrp.getTruckOrderGroup().keySet().iterator();
			while(_iterator.hasNext()) {
				_area = (TrnArea)_iterator.next();
				if("X".equalsIgnoreCase(_area.getIsDepot())) {
					result.addError("Depot Area "+ _area.getCode() + " is found in roadnet truck file");
				}
			}
		}
		if(orderGrp.getDepotOrderGroup() != null) {
			Iterator _iterator = orderGrp.getDepotOrderGroup().keySet().iterator();
			while(_iterator.hasNext()) {
				_area = (TrnArea)_iterator.next();
				if(!"X".equalsIgnoreCase(_area.getIsDepot())) {
					result.addError("Truck Area "+ _area.getCode() + " is found in roadnet depot file");
				}
			}
		}
		return orderGrp;
	}
	
	protected Object preProcessLoadData(IRoutingOutputInfo routingInfo, RoutingResult result, IServiceProvider serviceProvider) {
		
		Collection areas = serviceProvider.getDomainManagerService().getAreas();
		Map routingArea = this.getAreaMapping(areas);
				
		return groupOrderRouteInfo(result.getRegularOrders(), result.getDepotOrders(), routingArea);
	}
	
	protected void postProcessRoutingOutput(IRoutingOutputInfo routingInfo, RoutingResult result, IServiceProvider serviceProvider) {
		// Nothing Special
	}
	
	protected void validateData(IRoutingOutputInfo routingInfo, RoutingResult result, IServiceProvider serviceProvider) {
		
		if(result.getRegularOrders() == null || result.getRegularOrders().size() == 0) {
			result.addError(INVALID_ORDERROUTEFILE);
		}				
	}
	
	protected void collectOrders(IRoutingOutputInfo routingInfo, RoutingResult result) {
		
		result.setRegularOrders(fileManager.parseRouteFile(TransportationAdminProperties.getRoutingOrderRouteOutputFormat()
				, new ByteArrayInputStream(routingInfo.getTruckRoutingFile()), ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER
				, null));
		
		result.setDepotOrders(fileManager.parseRouteFile(TransportationAdminProperties.getRoutingOrderRouteOutputFormat()
				, new ByteArrayInputStream(routingInfo.getDepotRoutingFile()), ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER
				, null));
		
				
		result.setDepotTruckSchedule(fileManager.parseRouteFile(TransportationAdminProperties.getRoutingTruckScheduleFormat()
				, new ByteArrayInputStream(routingInfo.getDepotTruckScheduleFile()), ROW_IDENTIFIER, ROW_BEAN_IDENTIFIER
				, null));
	}

	protected RouteGenerationResult generateRouteNumber(Map depotRouteMapping, List routeData, IRoutingOutputInfo routingInfo
															, IServiceProvider serviceProvider ) throws RouteNoGenException {

		Map routeNoGenMapping = new HashMap();
		RouteMappingId routeMappingId = null;
		RouteOrderGroupInfo groupInfo = null;
		List orders = null;
		OrderRouteInfoModel _order = null;
		RouteComparator routeComparator = new RouteComparator();


		String areaPrefix = null;
		String areaDeliveryModel = null;

		Map areaMapping = getAreaMapping(serviceProvider.getDomainManagerService().getAreas());
		TrnArea trnArea = null;
		List depotRouteIds = null;

		if(routeData != null && depotRouteMapping != null) {

			Map routeInfoGrp = groupRouteInfoByRouteMapping(routeData, routingInfo.getCutOff(), serviceProvider);
			Iterator _iterator = routeInfoGrp.keySet().iterator();

			while(_iterator.hasNext()) {

				routeMappingId = (RouteMappingId)_iterator.next();
				groupInfo = (RouteOrderGroupInfo)routeInfoGrp.get(routeMappingId);
				orders = groupInfo.getOrders();
				Collections.sort(orders, routeComparator);

				Iterator _orderItr = orders.iterator();
				int _startIndex = groupInfo.getStartIndex();
				String _currRouteId = null;
				String _formattedRouteId = null;

				while(_orderItr.hasNext()) {

					_order = (OrderRouteInfoModel)_orderItr.next();					
					trnArea = (TrnArea)areaMapping.get(_order.getDeliveryArea());

					if(trnArea == null) {
						throw new RouteNoGenException("Area "+_order.getDeliveryArea()+" is missing");
					}
					areaPrefix = trnArea.getPrefix();
					areaDeliveryModel = trnArea.getDeliveryModel();
					if(areaPrefix == null) {
						throw new RouteNoGenException("Area Prefix for "+_order.getDeliveryArea()+" is missing");
					}
					_order.setDeliveryModel(areaDeliveryModel);

					if(_currRouteId == null || !_currRouteId.equalsIgnoreCase(_order.getRouteId())) {
						_currRouteId = _order.getRouteId();
						_startIndex++;
						_formattedRouteId = areaPrefix + _order.getDeliveryArea() + TransStringUtil.formatRouteNumber(""+_startIndex);
						
						depotRouteIds = (List)depotRouteMapping.get(_currRouteId);
						if(depotRouteIds == null || depotRouteIds.size() == 0) {
							updateRouteMapping(routingInfo,depotRouteMapping, trnArea
									, routeNoGenMapping, routeMappingId
											, _formattedRouteId, _currRouteId);
						} else {
							Iterator _dptRptItr = depotRouteIds.iterator();
							String _dptRptId = null;
							while(_dptRptItr.hasNext()) {
								_dptRptId = (String)_dptRptItr.next();
								updateRouteMapping(routingInfo,depotRouteMapping, trnArea
										, routeNoGenMapping, routeMappingId
												, _formattedRouteId, _dptRptId);
							}
						}
						
												
					}
					_order.setRouteId(_formattedRouteId);
				}
			}
		}
		RouteGenerationResult result = new RouteGenerationResult();
		result.setRouteInfos(routeData);
		result.setRouteNoSaveInfos(routeNoGenMapping);
		return result; 
	}
	
	private void updateRouteMapping(IRoutingOutputInfo routingInfo,Map depotRouteMapping
										, TrnArea trnArea, Map routeNoGenMapping
										, RouteMappingId routeMappingId, String formattedRouteId
										, String currRouteId) {
		
		RouteMapping _persistRouteMapping = new RouteMapping();
		_persistRouteMapping.setRouteMappingId(new RouteMappingId(routeMappingId.getRouteDate()
				, routeMappingId.getCutOffId()
				, routeMappingId.getGroupCode()
				, formattedRouteId
				, currRouteId));

		if("X".equalsIgnoreCase(trnArea.getIsDepot())) {
			_persistRouteMapping.setRoutingSessionID(routingInfo.getDepotRoutingSessionDesc());
		} else {
			_persistRouteMapping.setRoutingSessionID(routingInfo.getTruckRoutingSessionDesc());
		}
		
		if(routeNoGenMapping.containsKey(routeMappingId)) {

			((Set)routeNoGenMapping.get(routeMappingId)).add(_persistRouteMapping);							
		} else {

			Set _tmpLst = new HashSet();
			_tmpLst.add(_persistRouteMapping);
			routeNoGenMapping.put(routeMappingId, _tmpLst);
		}			
	}


	private Map groupRouteInfoByRouteMapping(List routeData, String cutOff, IServiceProvider serviceProvider) {

		Map result = new HashMap();
		OrderRouteInfoModel tmpModel = null;
		Iterator _iterator = routeData.iterator();
		RouteMappingId routeMappingId = null;
		String areaCode = null;

		RouteOrderGroupInfo groupInfo = null;

		while(_iterator.hasNext()) {		

			tmpModel = (OrderRouteInfoModel)_iterator.next();
			areaCode = TransStringUtil.splitStringForCode(tmpModel.getRouteId());
			tmpModel.setDeliveryArea(areaCode);

			routeMappingId = getRouteNumberId(tmpModel.getDeliveryDate(), cutOff, areaCode);			

			if(result.containsKey(routeMappingId)) {

				groupInfo = ((RouteOrderGroupInfo)result.get(routeMappingId));

			} else {	

				Map routeCountMapping = serviceProvider.getDispatchManagerService()
											.getRouteNumberGroup(getRouteDate(tmpModel.getDeliveryDate()), null, areaCode);

				Set routeMappingKeys = routeCountMapping.keySet(); 
				List routeMappingIds = new ArrayList();
				routeMappingIds.addAll(routeMappingKeys);

				CutOffComparator comparator = getCutOffComparator(serviceProvider.getDomainManagerService().getCutOffs());
				TrnCutOff _currCutOff = (TrnCutOff)comparator.getReferenceData().get(cutOff);

				Collections.sort(routeMappingIds, comparator);

				int _startIndex = 0;
				Iterator _itr = routeMappingIds.iterator();

				while(_itr.hasNext()) {

					RouteMappingId _routeMappingId = (RouteMappingId) _itr.next();
					TrnCutOff _cutOff = (TrnCutOff)comparator.getReferenceData().get(_routeMappingId.getCutOffId());

					if(_currCutOff != null &&
							_cutOff != null && 
							_cutOff.getSequenceNo().doubleValue() >= _currCutOff.getSequenceNo().doubleValue()) {
						break;
					} else {
						_startIndex = _startIndex+((Integer)routeCountMapping.get(_routeMappingId)).intValue();
					}
				}				
				groupInfo = new RouteOrderGroupInfo(_startIndex, new ArrayList());
				result.put(routeMappingId, groupInfo);
			}
			groupInfo.addOrder(tmpModel);
		}
		return result;
	}

	protected List filterRoutesFromOrders(List orderDataList) {
		List routeIds = new ArrayList();
		List routes = new ArrayList();

		String routeId = null;
		if(orderDataList != null) {
			OrderRouteInfoModel tmpRouteInfo = null;
			Iterator iterator = orderDataList.iterator();
			while(iterator.hasNext()) {
				tmpRouteInfo = (OrderRouteInfoModel)iterator.next();
				routeId = tmpRouteInfo.getRouteId();
				if(!routeIds.contains(routeId)) {
					routes.add(tmpRouteInfo);
					routeIds.add(routeId);
				}
			}
		}
		return routes;
	}
	// Start processing depots 
	
	protected boolean convertDptToRegOrders(IRoutingOutputInfo routingInfo, RoutingResult result, IServiceProvider serviceProvider) {
		
		Date currDepotDeparture = null;
		String currRouteId = null;
		
		if(result.getDepotTruckSchedule() != null) {
			
			Iterator _itrSchedule = result.getDepotTruckSchedule().iterator();
			ITruckScheduleInfo _schInfo = null;
			Map groupSchedule = new TreeMap();
			
			while(_itrSchedule.hasNext()) {
				
				_schInfo = (ITruckScheduleInfo)_itrSchedule.next();
				if(groupSchedule.containsKey(_schInfo.getGroupCode())) {
					((List)groupSchedule.get(_schInfo.getGroupCode())).add
												(new CustomTruckScheduleInfo(new ArrayList(),_schInfo));
				} else {
					List _tmpValues = new ArrayList();
					_tmpValues.add(new CustomTruckScheduleInfo(new ArrayList(),_schInfo));
					groupSchedule.put(_schInfo.getGroupCode(), _tmpValues);
				}
			}
			
			sortGroup(groupSchedule);	
						
			if(result.getDepotOrders() != null) {
				
				Iterator _itrOrders = result.getDepotOrders().iterator();
				OrderRouteInfoModel _order = null;
				CustomTruckScheduleInfo _matchSchedule = null;
				
				while(_itrOrders.hasNext()) {
					
					_order = (OrderRouteInfoModel)_itrOrders.next();
					if(currRouteId == null) {
						currRouteId = _order.getRouteId();
					}
					
					if(_order.getOrderNumber() == null || _order.getOrderNumber().trim().length() == 0) {
						currDepotDeparture = _order.getStopDepartureTime();
						continue;
					} else if(currRouteId != null && !currRouteId.equalsIgnoreCase(_order.getRouteId())) {
						currDepotDeparture = null;
						currRouteId = _order.getRouteId();
					}
					
					if(currDepotDeparture == null) {
						_order.setStopDepartureTime(_order.getRouteStartTime());
					} else {
						_order.setStopDepartureTime(currDepotDeparture);
					}
					_matchSchedule = matchSchedule(_order
													, (List)groupSchedule.get(TransStringUtil.splitStringForCode(_order.getRouteId())));
					if(_matchSchedule != null) {
						_order.setRouteStartTime(_matchSchedule.getTruckDepartureTime());
						_matchSchedule.addOrder(_order);
					} else {
						result.addError(INVALID_TRUCKSCHEDULEFILE+" - > "+_order.getOrderNumber());
						return false;
					}
				}
			}
			updateRouteStop(groupSchedule, result);
		}
		
		return true;
	}
	
	private List updateRouteStop(Map groupSchedule, RoutingResult routingResult) {
		
		List result = new ArrayList();
		Map depotRouteMapping = new HashMap();
		
		Iterator _itrGrpSchedule = groupSchedule.keySet().iterator();		
		String grpKey = null;		
		TruckOrderScheduleComparator _schOrderComparator = new TruckOrderScheduleComparator();				
		List customerInfos  = null;
		
		while(_itrGrpSchedule.hasNext()) {
			
			int intRountCount = 0;
			grpKey = (String)_itrGrpSchedule.next();
			customerInfos  = (List)groupSchedule.get(grpKey);
			
			Iterator _itrCustomerInfos = customerInfos.iterator();
			CustomTruckScheduleInfo _tmpCustTrkInfo = null;
			
			while(_itrCustomerInfos.hasNext()) {
				
				intRountCount++;
				_tmpCustTrkInfo = (CustomTruckScheduleInfo)_itrCustomerInfos.next();
				
				Collections.sort(_tmpCustTrkInfo.getOrders(), _schOrderComparator);
				OrderRouteInfoModel _order = null;
				Iterator _itrOrders = _tmpCustTrkInfo.getOrders().iterator();
				int intStopCount = 0;
				String _tmpRptKey = null;
				while(_itrOrders.hasNext()) {
					intStopCount++;
					_order = (OrderRouteInfoModel)_itrOrders.next();
					_tmpRptKey = grpKey+"-"+intRountCount;
					
					if(depotRouteMapping.containsKey(_tmpRptKey)) {
						((List)depotRouteMapping.get(_tmpRptKey)).add(_order.getRouteId());							
					} else {
						List _tmpLst = new ArrayList();
						_tmpLst.add(_order.getRouteId());
						depotRouteMapping.put(_tmpRptKey, _tmpLst);
					}
					
					_order.setRouteId(grpKey+"-"+intRountCount);
					_order.setStopNumber(""+intStopCount);
					result.add(_order);
				}
			}			
		}
		
		if(routingResult.getRegularOrders() == null) {
			routingResult.setRegularOrders(new ArrayList());			
		}
		routingResult.getRegularOrders().addAll(result);
		
		routingResult.setDepotRouteMapping(depotRouteMapping);
		return result;
	}
	
	protected OrderAreaGroup groupOrderRouteInfo(List truckOrders, List depotOrders, Map routingAreas) {

		OrderAreaGroup orderGroupResult = new OrderAreaGroup();
		Map truckOrderGroup = new HashMap();
		Map depotOrderGroup = new HashMap();

		orderGroupResult.setTruckOrderGroup(truckOrderGroup);
		orderGroupResult.setDepotOrderGroup(depotOrderGroup);
		orderGroupResult.setRoutingAreaCodes(new HashSet());
		orderGroupResult.setDepotAreaCodes(new HashSet());
							
		groupOrders(orderGroupResult, truckOrders, truckOrderGroup, routingAreas);
		groupOrders(orderGroupResult, depotOrders, depotOrderGroup, routingAreas);		

		return orderGroupResult;
	}
	
	private void groupOrders(OrderAreaGroup orderAreaGroup ,List orders, Map orderGroup, Map routingAreas) {
		
		if(orders != null) {
			OrderRouteInfoModel tmpInfo = null;		
			List tmpList = null;
			String areaCode = null;
			TrnArea area = null;
			
			Iterator iterator = orders.iterator();
			while(iterator.hasNext()) {

				tmpInfo = (OrderRouteInfoModel)iterator.next();				
				areaCode = TransStringUtil.splitStringForCode(tmpInfo.getRouteId());
				
				if(routingAreas.containsKey(areaCode)) {	
					area = (TrnArea)routingAreas.get(areaCode);
					if("X".equals(area.getActive())) {
						orderAreaGroup.addRoutingAreaCode(areaCode);
					}
					if("X".equals(area.getIsDepot())) {
						orderAreaGroup.addDepotAreaCode(areaCode);
					}
					if(orderGroup.containsKey(area)) {
						tmpList = (List)orderGroup.get(area);
						tmpList.add(tmpInfo);				
					} else {
						tmpList = new ArrayList();
						tmpList.add(tmpInfo);
						orderGroup.put(area, tmpList);
					}
				} 
			}
		}
	}

	private CustomTruckScheduleInfo matchSchedule(OrderRouteInfoModel order, List scheduleLst) {
		
		CustomTruckScheduleInfo _schInfo = null;
		CustomTruckScheduleInfo _preSchInfo = null;
		
		if(scheduleLst != null) {
			
			Iterator _itrSchedule = scheduleLst.iterator();
									
			while(_itrSchedule.hasNext()) {
				_schInfo = (CustomTruckScheduleInfo)_itrSchedule.next();
						
				if(_schInfo.getDepotArrivalTime().after(order.getStopDepartureTime())) {
					break;
				} else {
					_preSchInfo = _schInfo;
				}
			}
		}
		
		/*if(_preSchInfo != null && _preSchInfo.getGroupCode().equalsIgnoreCase("005")) {
			try {
				System.out.println(order.getOrderNumber()+">>"+order.getRouteId()+">>"+RoutingDateUtil.formatTime(order.getTimeWindowStop())+">>"
												+RoutingDateUtil.formatTime(order.getStopDepartureTime())
												+"-->"+RoutingDateUtil.formatTime(_preSchInfo.getDepotArrivalTime()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		return _preSchInfo;
	}
	
	private void sortGroup(Map groupSchedule) {
		Iterator _itrGrpSchedule = groupSchedule.keySet().iterator();
		String grpKey = null;
		TruckScheduleComparator _schComparator = new TruckScheduleComparator();
		while(_itrGrpSchedule.hasNext()) {
			grpKey = (String)_itrGrpSchedule.next();
			Collections.sort(((List)groupSchedule.get(grpKey)), _schComparator);
		}
	}

}

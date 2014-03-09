package com.freshdirect.dashboard.service.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshdirect.dashboard.dao.IOrderRateDAO;
import com.freshdirect.dashboard.dao.IPlantDispatchDAO;
import com.freshdirect.dashboard.exception.FDServiceException;
import com.freshdirect.dashboard.exception.IIssue;
import com.freshdirect.dashboard.model.PlantDispatchData;
import com.freshdirect.dashboard.model.ProjectedUtilizationBase;
import com.freshdirect.dashboard.model.ProjectedUtilizationVO;
import com.freshdirect.dashboard.service.IPlantDispatchService;
import com.freshdirect.dashboard.util.DateUtil;

@Service
public class PlantDispatchService implements IPlantDispatchService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PlantDispatchService.class);
	
	@Autowired
	private IPlantDispatchDAO plantDispatchDAO;
	
	@Autowired
	private IOrderRateDAO orderRateDAO;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List getDispatchVolume(Date deliveryDate) throws FDServiceException {
		
		List dataList = new ArrayList();
		
		try {
			LOGGER.info("PlantDispatchServiceImpl : deliveryDate " + deliveryDate);
			Map<String, List<PlantDispatchData>> dispatchMap = plantDispatchDAO.getData(deliveryDate);
			int[] projectedOrder = calculateUtilizationFactor(deliveryDate);
			
			double amUtilizationFactor = 0.0, pmUtilizationFactor = 0.0;
			int amCumlPlannedCapacity = 0, pmCumlPlannedCapacity = 0;
			int cumlPlannedCapacity = 0, cumlOrders = 0, cumlAllocatedOrders = 0, cumlTrucks = 0;
			
			if(dispatchMap != null) {
				for(Map.Entry<String, List<PlantDispatchData>> dispatchEntry : dispatchMap.entrySet()) {
					String shift = dispatchEntry.getKey();	
					List<PlantDispatchData> dispatchList = dispatchEntry.getValue();
					if("AM".equals(shift)) {						
						for (ListIterator<PlantDispatchData> i = dispatchList.listIterator(); i.hasNext();) {
							PlantDispatchData data = i.next();
							data.setCumlPlannedCapacity(data.getPlannedCapacity() + cumlPlannedCapacity);							
							amCumlPlannedCapacity += data.getPlannedCapacity();							
						}						
					} else {
						for (ListIterator<PlantDispatchData> i = dispatchList.listIterator(); i.hasNext();) {
							PlantDispatchData data = i.next();
							data.setCumlPlannedCapacity(data.getPlannedCapacity() + cumlPlannedCapacity);							
							pmCumlPlannedCapacity += data.getPlannedCapacity();							
						}
					}
				}
				
				cumlPlannedCapacity = 0;
				amUtilizationFactor = projectedOrder[0] / (double) amCumlPlannedCapacity;
				pmUtilizationFactor = projectedOrder[1] / (double) pmCumlPlannedCapacity;
				for(Map.Entry<String, List<PlantDispatchData>> dispatchEntry : dispatchMap.entrySet()) {
					List<PlantDispatchData> dispatchList = dispatchEntry.getValue();
					Collections.sort(dispatchList, new PlantDispatchDataComparator());
					for (ListIterator<PlantDispatchData> i = dispatchList.listIterator(); i.hasNext();) {
						PlantDispatchData data = i.next();
						data.setCumlPlannedCapacity(0);
						dataList.add(data);
						
						data.setCumlPlannedCapacity(data.getPlannedCapacity() + cumlPlannedCapacity);
						data.setCumlOrders(data.getOrders() + cumlOrders);						
						data.setCumlAllocatedOrders(data.getAllocatedOrders() + cumlAllocatedOrders);
						data.setProjectedOrders(
								"AM".equalsIgnoreCase(dispatchEntry.getKey()) 
									? Math.round((float)(data.getCumlPlannedCapacity() * amUtilizationFactor))
										: Math.round((float)(data.getCumlPlannedCapacity() * pmUtilizationFactor)));
						data.setProductionCapacity(data.getPlantCapacity() - data.getProjectedOrders());
						data.setCumlTrucks(data.getTrucks() + cumlTrucks);
						if(data.getPlantCapacity() > 0) {
							data.setCurrentUtilization(Math.round((float)((double) data.getCumlOrders() / data.getPlantCapacity()) * 100));
						} else {
							data.setCurrentUtilization(0);
						}
						data.setLiveSpareCapacity(data.getPlantCapacity() - data.getCumlOrders());
						
						cumlPlannedCapacity += data.getPlannedCapacity();
						cumlOrders += data.getOrders();
						cumlAllocatedOrders += data.getAllocatedOrders();
						cumlTrucks += data.getTrucks();						
					}
				}
				Collections.sort(dataList, new PlantDispatchDataComparator());
			}	
		
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			throw new FDServiceException(e, IIssue.DISPATCH_VOLUME_ERROR);
		}
		return dataList;
	}
	
	public int[] calculateUtilizationFactor(Date date) throws SQLException, ParseException {
	
		int amProjectedOrders = 0, pmProjectedOrders = 0;
		int[] projectedOrder = new int[2];
		
		String deliveryDate = DateUtil.getDate(date);
		
		Map<Date, String> cutoffMap = orderRateDAO.getCutoffs();
		List<ProjectedUtilizationVO> projectedUtilizationList = orderRateDAO.getProjectedUtilization(deliveryDate);
		
		Map<String, Map<String, ProjectedUtilizationBase>> projectedUtilizationMap = new HashMap<String, Map<String,ProjectedUtilizationBase>>();
		
		ProjectedUtilizationBase _projUtilization = null;
		for (ListIterator<ProjectedUtilizationVO> i = projectedUtilizationList.listIterator(); i.hasNext();) {
			ProjectedUtilizationVO pu = i.next();
			
			if(!projectedUtilizationMap.containsKey(pu.getZone())) {
				projectedUtilizationMap.put(pu.getZone(), new HashMap<String, ProjectedUtilizationBase>());
			}	
			
			if(!projectedUtilizationMap.get(pu.getZone()).containsKey(cutoffMap.get(pu.getCutoffTime()))){
				projectedUtilizationMap.get(pu.getZone()).put(cutoffMap.get(pu.getCutoffTime()), new ProjectedUtilizationBase());
			}
			
			_projUtilization = projectedUtilizationMap.get(pu.getZone()).get(cutoffMap.get(pu.getCutoffTime()));
			if(_projUtilization != null && _projUtilization.getZone() != null) {
				_projUtilization.setProjectedOrderCnt(_projUtilization.getProjectedOrderCnt() + Math.round((float)(pu.getConfirmedOrderCnt() + pu.getExpectedOrderCnt())));
			} else {
				_projUtilization.setZone(pu.getZone());
				_projUtilization.setShift(cutoffMap.get(pu.getCutoffTime()));
				_projUtilization.setProjectedOrderCnt(Math.round((float)(pu.getConfirmedOrderCnt() + pu.getExpectedOrderCnt())));
			}				
		}	
		
		
		for (Map.Entry<String, Map<String, ProjectedUtilizationBase>> zoneEntrySet : projectedUtilizationMap.entrySet()) {

			for (Map.Entry<String, ProjectedUtilizationBase> shiftEntry : zoneEntrySet.getValue().entrySet()) {
				ProjectedUtilizationBase _pu = shiftEntry.getValue();
				if ("AM".equals(shiftEntry.getKey())) {
					amProjectedOrders += _pu.getProjectedOrderCnt();
				} else {
					pmProjectedOrders += _pu.getProjectedOrderCnt();
				}
			}
		}
		projectedOrder[0] = amProjectedOrders;
		projectedOrder[1] = pmProjectedOrders;
		return projectedOrder;
	}
	
	protected class PlantDispatchDataComparator implements Comparator<PlantDispatchData> {

		public int compare(PlantDispatchData obj1, PlantDispatchData obj2) {
			if (obj1 != null && obj2 != null && obj1.getDispatchTime() != null
					&& obj2.getDispatchTime() != null) {				
				return obj1.getDispatchTime().compareTo(obj2.getDispatchTime());				
			}
			return 0;
		}
	}
}

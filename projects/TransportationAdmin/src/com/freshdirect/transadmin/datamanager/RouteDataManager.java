package com.freshdirect.transadmin.datamanager;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.freshdirect.transadmin.datamanager.model.IRoutingOutputInfo;
import com.freshdirect.transadmin.datamanager.model.ITruckScheduleInfo;
import com.freshdirect.transadmin.datamanager.model.OrderRouteInfoModel;
import com.freshdirect.transadmin.datamanager.model.TruckScheduleInfo;
import com.freshdirect.transadmin.datamanager.report.ICutOffReport;
import com.freshdirect.transadmin.datamanager.report.XlsCutOffReport;
import com.freshdirect.transadmin.datamanager.report.model.CutOffReportData;
import com.freshdirect.transadmin.datamanager.report.model.CutOffReportKey;
import com.freshdirect.transadmin.model.RouteMappingId;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;

public class RouteDataManager {
	
	protected IRouteFileManager fileManager = new RouteFileManager();
	
	protected static final String ROW_IDENTIFIER = "row";
	
	protected static final String ROW_BEAN_IDENTIFIER = "rowBean";
	
	
	       
	public RoutingResult process(IRoutingOutputInfo routingInfo
									,String userName, IServiceProvider serviceProvider) throws IOException {		
		return null;
	}
		
	protected RoutingResult initResult(String outputFileName1, String outputFileName2, String outputFileName3) throws IOException  {
		
		RoutingResult result = new RoutingResult();
		
	    if(outputFileName1 != null) {
			File outputFile1 = createFile(outputFileName1, "."+TransportationAdminProperties.getFilenameSuffix());
		    result.setOutputFile1(outputFile1.getAbsolutePath());
	    }
	    
	    if(outputFileName2 != null) {
		    File outputFile2 = createFile(outputFileName2, "."+TransportationAdminProperties.getFilenameSuffix());	    	    
		    result.setOutputFile2(outputFile2.getAbsolutePath());
	    }
	    
	    if(outputFileName3 != null) {
	    	File outputFile3 = createFile(outputFileName3, "."+TransportationAdminProperties.getErrorFilenameSuffix());
	    	result.setOutputFile3(outputFile3.getAbsolutePath());
	    }
	    return result;
	}
	
	protected RoutingResult initResult(String outputFileName1, String outputFileName2, String outputFileName3, String errorExtension) throws IOException  {
		
		RoutingResult result = new RoutingResult();
		
	    if(outputFileName1 != null) {
			File outputFile1 = createFile(outputFileName1, "."+TransportationAdminProperties.getFilenameSuffix());
		    result.setOutputFile1(outputFile1.getAbsolutePath());
	    }
	    
	    if(outputFileName2 != null) {
		    File outputFile2 = createFile(outputFileName2, "."+TransportationAdminProperties.getFilenameSuffix());	    	    
		    result.setOutputFile2(outputFile2.getAbsolutePath());
	    }
	    
	    if(outputFileName3 != null) {
	    	File outputFile3 = createFile(outputFileName3, "."+errorExtension);
	    	result.setOutputFile3(outputFile3.getAbsolutePath());
	    }
	    return result;
	}
	
	protected File createFile(String prefix, String suffix) throws IOException  {		
		if(TransportationAdminProperties.getDownloadFolder() != null 
					&& TransportationAdminProperties.getDownloadFolder().trim().length() > 0) {
			return File.createTempFile(prefix, suffix, new File(TransportationAdminProperties.getDownloadFolder()));
		} else {
			return File.createTempFile(prefix, suffix);
		}
	}
	
//	TrnRouteNumberId
	
	
	
	protected RouteMappingId getRouteNumberId(Date orderDate, String cutOff, String area) {		
		return new RouteMappingId(orderDate, cutOff, area, null, null);
	}
	
	protected CutOffComparator getCutOffComparator(Collection cutOffLst) {
		Map cutOffMapping = new HashMap();
		if(cutOffLst != null) {
			Iterator iterator = cutOffLst.iterator();
			TrnCutOff tmpModel = null;
			
			while(iterator.hasNext()) {
				tmpModel = (TrnCutOff)iterator.next();
				cutOffMapping.put(tmpModel.getCutOffId(), tmpModel);
			}
		}
		return new CutOffComparator(cutOffMapping);
	}
	
	protected Map getAreaMapping(Collection areaLst) {
		Map areaMapping = new HashMap();
		if(areaLst != null) {
			Iterator iterator = areaLst.iterator();
			TrnArea tmpModel = null;
			
			while(iterator.hasNext()) {
				tmpModel = (TrnArea)iterator.next();
				areaMapping.put(tmpModel.getCode(), tmpModel);
			}
		}
		return areaMapping;
	}
	
	
	protected String getRouteDate(Date routeDate) {
		try {
			return TransStringUtil.getServerDate(routeDate);
		} catch(ParseException exp) {
			return TransStringUtil.getCurrentDate();
		}
	}
	
	protected CutOffReportData getCutOffReportData(List orderLst, String cutOff, IServiceProvider serviceProvider ) {
		
		CutOffReportData result = new CutOffReportData();
		result.setCutOff(getCutOffTime(cutOff, serviceProvider));
		result.setReportData(new TreeMap());
		
		if(orderLst != null) {
			Iterator _iterator = orderLst.iterator();
			OrderRouteInfoModel _model = null;
			while(_iterator.hasNext()) {
				_model = (OrderRouteInfoModel)_iterator.next();
				if(_model.getDeliveryDate() != null &&
						_model.getTimeWindowStart() != null &&  
						_model.getTimeWindowStop() != null &&
						_model.getRouteId() != null) {
					result.putReportData(new CutOffReportKey(_model.getDeliveryDate(),
																_model.getTimeWindowStart(),
																_model.getTimeWindowStop(),
																_model.getRouteId()), _model);
				}
			}
		}
		return result;
	}
	
	protected ICutOffReport getCutOffReportEngine() {
		return new XlsCutOffReport();
	}
	
	protected String getCutOffReportExtension() {
		return "xls";
	}
	
	protected String getCutOffTime(String cutOff, IServiceProvider serviceProvider) {
		TrnCutOff tmpModel = serviceProvider.getDomainManagerService().getCutOff(cutOff);
		if(tmpModel != null) {
			return tmpModel.getName();
		}
		return "Cut Off Error";
	}
	
	protected class RouteNoGenException extends Exception {
		public RouteNoGenException(String message) {
			super(message);
		}
	}
	
	protected Map getRoutingAreaMapping(Collection areas) {
		
		Map result = new HashMap();
		
		TrnArea tmpArea = null;
		if(areas != null) {
			Iterator iterator = areas.iterator();
			while(iterator.hasNext()) {
				tmpArea = (TrnArea)iterator.next();				
				if(tmpArea != null && "X".equalsIgnoreCase(tmpArea.getActive())) {
					result.put(tmpArea.getCode(), tmpArea);
				}
			}
		}
		return result;
	}
	
	protected Map getDepotAreaMapping(Collection areas) {
		
		Map result = new HashMap();
		
		TrnArea tmpArea = null;
		if(areas != null) {
			Iterator iterator = areas.iterator();
			while(iterator.hasNext()) {
				tmpArea = (TrnArea)iterator.next();				
				if(tmpArea != null && "X".equalsIgnoreCase(tmpArea.getIsDepot())) {
					result.put(tmpArea.getCode(), tmpArea);
				}
			}
		}
		return result;
	}
	
	protected class RouteOrderGroupInfo {
		
		private int startIndex;
		
		private List orders;
		
		public RouteOrderGroupInfo(int startIndex, List orders) {
			super();
			this.startIndex = startIndex;
			this.orders = orders;
		}
		
		public List getOrders() {
			return orders;
		}
		public void setOrders(List orders) {
			this.orders = orders;
		}
		public int getStartIndex() {
			return startIndex;
		}
		public void setStartIndex(int startIndex) {
			this.startIndex = startIndex;
		}
		
		public void addOrder(Object order) {
			orders.add(order);
		}
		
		public String toString() {
			return ""+startIndex;
		}
	}
	
	protected class CutOffComparator implements Comparator {
		
		private Map referenceData = null;
		
		protected CutOffComparator(Map referenceData) {
			this.referenceData = referenceData;
		}
		
		public int compare(Object obj1, Object obj2){
			
			String cutOffId1 = ( (RouteMappingId) obj1).getCutOffId();

			String cutOffId2 = ( (RouteMappingId) obj2).getCutOffId();
			
			TrnCutOff cutOff1 = (TrnCutOff)referenceData.get(cutOffId1);
			TrnCutOff cutOff2 = (TrnCutOff)referenceData.get(cutOffId2);
			
			return compareCutOff(cutOff1, cutOff2);			
		}
		
		private int compareCutOff(TrnCutOff cutOff1, TrnCutOff cutOff2) {
			
			if(cutOff1 == null && cutOff2 == null) {
				return 0;
			}
			
			if(cutOff1 != null && cutOff2 == null) {
				return -1;
			}
			
			if(cutOff1 == null && cutOff2 != null) {
				return 1;
			}

			if( cutOff1.getSequenceNo().doubleValue() > cutOff2.getSequenceNo().doubleValue() ) {
				return 1;
			} else if( cutOff1.getSequenceNo().doubleValue() < cutOff2.getSequenceNo().doubleValue() ) {
				return -1;
			} else {
				return 0;
			}
		}

		public Map getReferenceData() {
			return referenceData;
		}

		public void setReferenceData(Map referenceData) {
			this.referenceData = referenceData;
		}
	}
	
	protected class RouteComparator implements Comparator {		
				
		public int compare(Object obj1, Object obj2){

			int routeId1 = TransStringUtil.splitStringForValue(( (OrderRouteInfoModel) obj1).getRouteId());
			int routeId2 = TransStringUtil.splitStringForValue(( (OrderRouteInfoModel) obj2).getRouteId());			
			return routeId1 - routeId2;
		}		
	}
	
	protected class TruckScheduleComparator implements Comparator {		
		
		public int compare(Object obj1, Object obj2){

			Date arrivalData1 = ( (ITruckScheduleInfo) obj1).getDepotArrivalTime();
			Date arrivalData2 = ( (ITruckScheduleInfo) obj2).getDepotArrivalTime();			
			return arrivalData1.compareTo(arrivalData2);
		}		
	}
	
	protected class TruckOrderScheduleComparator implements Comparator {		
		
		public int compare(Object obj1, Object obj2){
			
			Date arrivalData1 = ( (OrderRouteInfoModel) obj1).getStopDepartureTime();
			Date arrivalData2 = ( (OrderRouteInfoModel) obj2).getStopDepartureTime();			
			return arrivalData1.compareTo(arrivalData2);
		}		
	}
	
	protected class CustomTruckScheduleInfo extends TruckScheduleInfo {
		
		List orders = null;
		
		CustomTruckScheduleInfo(List orders, ITruckScheduleInfo info) {
			super(info.getGroupCode(), info.getDepotArrivalTime(), info.getTruckDepartureTime());
			this.orders = orders;
		}
		
		public List getOrders() {
			return orders;
		}
		public void setOrders(List orders) {
			this.orders = orders;
		}
		
		public void addOrder(Object order) {
			this.orders.add(order);
		}
		
		public String toString() {
			return orders.toString()+","+super.toString()+"\n";
		}
	}
	
	protected class OrderAreaGroup {
		
		private Map truckOrderGroup = null;
		
		private Map depotOrderGroup = null;
		
		private Set depotAreaCodes = null;
		
		private Set routingAreaCodes = null;

		public Map getDepotOrderGroup() {
			return depotOrderGroup;
		}

		public void setDepotOrderGroup(Map depotOrderGroup) {
			this.depotOrderGroup = depotOrderGroup;
		}

		public Map getTruckOrderGroup() {
			return truckOrderGroup;
		}

		public void setTruckOrderGroup(Map truckOrderGroup) {
			this.truckOrderGroup = truckOrderGroup;
		}

		public Set getDepotAreaCodes() {
			return depotAreaCodes;
		}

		public void setDepotAreaCodes(Set depotAreaCodes) {
			this.depotAreaCodes = depotAreaCodes;
		}

		public Set getRoutingAreaCodes() {
			return routingAreaCodes;
		}

		public void setRoutingAreaCodes(Set routingAreaCodes) {
			this.routingAreaCodes = routingAreaCodes;
		}
		
		public void addDepotAreaCode(String code) {
			if(depotAreaCodes != null) {
				depotAreaCodes.add(code);
			}
		}
		
		public void addRoutingAreaCode(String code) {
			if(routingAreaCodes != null) {
				routingAreaCodes.add(code);
			}
		}
				
	}

}

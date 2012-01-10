package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.transadmin.constants.EnumParkingSlotBarcodeStatus;
import com.freshdirect.transadmin.constants.EnumParkingSlotPavedStatus;
import com.freshdirect.transadmin.model.ParkingLocation;
import com.freshdirect.transadmin.model.ParkingSlot;
import com.freshdirect.transadmin.service.IYardManagerService;
import com.freshdirect.transadmin.util.TransStringUtil;

public class YardController extends AbstractMultiActionController {
	
	private static Logger LOGGER = LoggerFactory.getInstance(YardController.class);
	
	private IYardManagerService yardManagerService;	
	
	public IYardManagerService getYardManagerService() {
		return yardManagerService;
	}

	public void setYardManagerService(IYardManagerService yardManagerService) {
		this.yardManagerService = yardManagerService;
	}

	/**
	 * Custom handler for Yard monitor
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView yardMonitorHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		ModelAndView mav = new ModelAndView("yardMonitorView");
		List<ParkingLocation> locations = new ArrayList<ParkingLocation>();
		Map<String, ParkingLocation> locationMap = yardManagerService.getParkingLocation();
				
		if(locationMap != null){
			for(Map.Entry<String, ParkingLocation> locEntry : locationMap.entrySet()){
				locations.add(locEntry.getValue());
			}
		}
		Collections.sort(locations, new ParkingLocationComparator());
		mav.getModel().put("parkingLocs", locations);
		mav.getModel().put("parkingSlotBarcodeStatus", EnumParkingSlotBarcodeStatus.getEnumList());
		mav.getModel().put("parkingSlotPavedStatus", EnumParkingSlotPavedStatus.getEnumList());
		return mav;
	}
	
	private class ParkingLocationComparator implements Comparator<ParkingLocation> {

		public int compare(ParkingLocation loc1, ParkingLocation loc2) {
			if(loc1.getLocationName() != null &&  loc2.getLocationName() != null) {
				return (loc1.getLocationName().compareTo(loc2.getLocationName()));
			}
			return 0;
		}
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView parkingLocationSlotHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
	
		ModelAndView mav = new ModelAndView("parkingLocSlotView");
		String parkingLocName = request.getParameter("parlingloc");
		
		List<ParkingSlot> slots = new ArrayList<ParkingSlot>();
		slots = yardManagerService.getParkingSlots(parkingLocName);
		
		List<ParkingLocation> locations = new ArrayList<ParkingLocation>();
		Map<String, ParkingLocation> locationMap = yardManagerService.getParkingLocation();
		if(locationMap != null){
			for(Map.Entry<String, ParkingLocation> locEntry : locationMap.entrySet()){
				locations.add(locEntry.getValue());
			}
		}
		mav.getModel().put("parkingLocs", locations);
		mav.getModel().put("parkingLocSlots", slots);
		return mav;
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView deleteParkingSlotHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
	
		ModelAndView mav = new ModelAndView("parkingLocSlotView");
		String parkingLocName = request.getParameter("parlingloc");
		List<String> slotNumbers = new ArrayList<String>();
		String arrEntityList[] = getParamList(request);	
		if (arrEntityList != null) {
			int arrLength = arrEntityList.length;
			for (int intCount = 0; intCount < arrLength; intCount++) {
				slotNumbers.add(arrEntityList[intCount]);
			}
		}
		
		if(slotNumbers.size() > 0 )
			yardManagerService.deleteParkingSlot(slotNumbers);
		
		List<ParkingSlot> slots = new ArrayList<ParkingSlot>();
		slots = yardManagerService.getParkingSlots(parkingLocName);
		
		List<ParkingLocation> locations = new ArrayList<ParkingLocation>();
		Map<String, ParkingLocation> locationMap = yardManagerService.getParkingLocation();
		if(locationMap != null){
			for(Map.Entry<String, ParkingLocation> locEntry : locationMap.entrySet()){
				locations.add(locEntry.getValue());
			}
		}
		mav.getModel().put("parkingLocs", locations);
		mav.getModel().put("parkingLocSlots", slots);
		return mav;
	}
}

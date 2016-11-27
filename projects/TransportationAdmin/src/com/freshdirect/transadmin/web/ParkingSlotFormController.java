package com.freshdirect.transadmin.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.ServletRequestDataBinder;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.transadmin.constants.EnumParkingSlotBarcodeStatus;
import com.freshdirect.transadmin.constants.EnumParkingSlotPavedStatus;
import com.freshdirect.transadmin.model.ParkingLocation;
import com.freshdirect.transadmin.model.ParkingSlot;
import com.freshdirect.transadmin.service.IYardManagerService;
import com.freshdirect.transadmin.web.editor.ParkingLocationPropertyEditor;
import com.freshdirect.transadmin.web.json.YardProviderController;

public class ParkingSlotFormController extends AbstractDomainFormController {
	
	private static Logger LOGGER = LoggerFactory.getInstance(YardProviderController.class);
	
	private IYardManagerService yardManagerService;
	
	public IYardManagerService getYardManagerService() {
		return yardManagerService;
	}

	public void setYardManagerService(IYardManagerService yardManagerService) {
		this.yardManagerService = yardManagerService;
	}
	
	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request)
			throws ServletException {
		Map refData = new HashMap();
		
		List<ParkingLocation> locations = new ArrayList<ParkingLocation>();
		Map<String, ParkingLocation> locationMap = yardManagerService.getParkingLocation();
				
		if(locationMap != null){
			for(Map.Entry<String, ParkingLocation> locEntry : locationMap.entrySet()){
				locations.add(locEntry.getValue());
			}
		}
		Collections.sort(locations, new ParkingLocationComparator());
		refData.put("parkingLocs", locations);
		refData.put("parkingSlotBarcodeStatus", EnumParkingSlotBarcodeStatus.getEnumList());
		refData.put("parkingSlotPavedStatus", EnumParkingSlotPavedStatus.getEnumList());
		
		return refData;
	
	}
	public String getDomainObjectName() {
		return "Parking Slot";
	}

	public Object getBackingObject(String id) {
		return yardManagerService.getParkingSlot(id);
	}
	
	@SuppressWarnings("unchecked")
	public List saveDomainObject(HttpServletRequest request, Object domainObject) {
		ParkingSlot bean =  (ParkingSlot)domainObject;
		List errorList = new ArrayList();
		try{
			yardManagerService.updateParkingSlot(bean);
		} catch (DataIntegrityViolationException objExp) {
			objExp.printStackTrace();
			errorList.add(this.getMessage("app.actionmessage.119", new Object[]{this.getDomainObjectName()}));
		}	
		return errorList;
	}
	
	public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request,binder);
		binder.registerCustomEditor(ParkingLocation.class, new ParkingLocationPropertyEditor());
    }
	
	protected String getIdFromRequest(HttpServletRequest request){
		return request.getParameter("slotNo");
	}
	
	@Override
	public Object getDefaultBackingObject() {
		return new ParkingSlot();
	}

	@Override
	public boolean isNew(Object command) {
		ParkingSlot bean =  (ParkingSlot)command;
		return bean.getSlotNumber() == null;
	}
	
	private class ParkingLocationComparator implements Comparator<ParkingLocation> {

		public int compare(ParkingLocation loc1, ParkingLocation loc2) {
			if(loc1.getLocationName() != null &&  loc2.getLocationName() != null) {
				return (loc1.getLocationName().compareTo(loc2.getLocationName()));
			}
			return 0;
		}
	}
}

package com.freshdirect.transadmin.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.routing.constants.EnumTransportationFacilitySrc;
import com.freshdirect.transadmin.constants.EnumUploadSource;
import com.freshdirect.transadmin.datamanager.ScheduleUploadDataManager;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.ScheduleEmployee;
import com.freshdirect.transadmin.model.Scrib;
import com.freshdirect.transadmin.model.TrnFacility;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.ScheduleFileUploadCommand;
import com.freshdirect.transadmin.web.validation.ScribValidator;
import com.freshdirect.transadmin.web.validation.ValidationInfo;

public class ScheduleUploadFormController extends BaseFormController {
	
	private LocationManagerI locationManagerService;
	private DomainManagerI domainManagerService;
	private DispatchManagerI dispatchManagerService;
		
	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}

	public void setDispatchManagerService(DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
	}

	public LocationManagerI getLocationManagerService() {
		return locationManagerService;
	}
	
	public void setLocationManagerService(LocationManagerI locationManagerService) {
		this.locationManagerService = locationManagerService;
	}
	
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}
	
	protected Map referenceData(HttpServletRequest request)
													throws ServletException {
			Map refData = new HashMap();
			return refData;
	}

	protected Object formBackingObject(HttpServletRequest request)
		throws Exception {
		
		String processType = request.getParameter("processType");		
		ScheduleFileUploadCommand bean = new ScheduleFileUploadCommand();
		bean.setProcessType(processType);
		String currentTime = TransStringUtil.getCurrentTime();		
		return bean;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected ModelAndView onSubmit(HttpServletRequest request,HttpServletResponse response,
		      Object command, BindException errors) throws ServletException, IOException    {
			
		ScheduleFileUploadCommand bean = (ScheduleFileUploadCommand) command;
		EnumUploadSource newSource = EnumUploadSource.SCHEDULE;
		try{
			if(bean.getProcessType() != null) {
				newSource = EnumUploadSource.getEnum(bean.getProcessType());
				
				Collection masterRegions = getDomainManagerService().getRegions();
				Collection masterZones = getDomainManagerService().getZones();
				Collection facilityLocs = getLocationManagerService().getTrnFacilitys();
				Map<String, Zone> zoneMapping = new HashMap<String, Zone>();
				Map<String, Region> regionMapping = new HashMap<String, Region>();
				Map<String, TrnFacility> facilityMapping = new HashMap<String, TrnFacility>();
				StringBuilder  uploadErrMsg = new StringBuilder();
				
				if(facilityLocs != null) {
					TrnFacility facility = null;
					if(masterZones != null) {
						Iterator _facilityItr = facilityLocs.iterator();						
						while(_facilityItr.hasNext()){
							facility = (TrnFacility)_facilityItr.next();
							facilityMapping.put(facility.getName(), facility);
						}
					}
					Zone zone = null;
					if(masterZones != null) {
						Iterator _zoneItr = masterZones.iterator();						
						while(_zoneItr.hasNext()) {
							zone = (Zone)_zoneItr.next();									
							zoneMapping.put(zone.getZoneCode(), zone);
						}
					}					
					Region region = null;
					if(masterRegions != null) {
						Iterator _regionItr = masterRegions.iterator();						
						while(_regionItr.hasNext()) {
							region = (Region)_regionItr.next();									
							regionMapping.put(region.getCode(), region);
						}
					}
					
					if(newSource.equals(EnumUploadSource.SCRIB)) {
						
						List<Scrib> scribs = new ScheduleUploadDataManager().processUploadScrib(request, bean.getFile(), newSource);
						
						if(scribs != null && scribs.size() > 0) {
							Set<Date> scribDates = new TreeSet<Date>();
							
							if(masterZones != null && facilityLocs != null) {
															
								TrnFacility originFacility = null;
								TrnFacility destFacility = null;
								
								List<ValidationInfo> validationerrors = new ArrayList<ValidationInfo>();
								ScribValidator validator = new ScribValidator();
								Set<String> errorSet = new HashSet<String>();
								Set<String> warningSet = new HashSet<String>();
								for(Scrib scrib : scribs) {
									int _codelength = scrib.getZoneS() != null ? scrib.getZoneS().length(): 3;				
									if(_codelength < 3) {
										StringBuffer strBuf = new StringBuffer();
										while(3 - _codelength > 0) {
											strBuf.append("0");
											_codelength++;
										}
										scrib.setZoneS(strBuf.toString() + scrib.getZoneS());
									}
									zone = zoneMapping.get(scrib.getZoneS());
									if(zone != null) {
										scrib.setZone(zone);
										scrib.setRegion(zone.getRegion());
									}

									originFacility = facilityMapping.get(scrib.getOriginFacility() != null ? scrib.getOriginFacility().getName() : "");
									destFacility = facilityMapping.get(scrib.getDestinationFacility() != null ? scrib.getDestinationFacility().getName() : "");
									scrib.setOriginFacility(originFacility);
									scrib.setDestinationFacility(destFacility);
									if(destFacility != null && destFacility.getTrnFacilityType() != null &&
											EnumTransportationFacilitySrc.CROSSDOCK.getName().equalsIgnoreCase(destFacility.getTrnFacilityType().getName())){
										scrib.setZone(null);
										scrib.setEquipmentTypeS(null);
									}
									scribDates.add(scrib.getScribDate());
									
									validationerrors = validator.validateEx(scrib);
									
									if(validationerrors.size()>0){
										for(ValidationInfo error : validationerrors){
											if(ValidationInfo.ERROR.equals(error.getLevel())){
												errorSet.add(getMessage(error.getKey(), error.getArgs()));
											}else{
												warningSet.add(getMessage(error.getKey(), error.getArgs()));
											}
										}
									}
								}
								if(warningSet.size()>0){
									for(String warning: warningSet){
										saveWarningMessage(request, warning);
									}
								}
								if(errorSet.size()>0){
									for(String error: errorSet){
										saveErrorMessage(request, error);
									}
								}else{
									Set<Date> hasPublishedScrib = new TreeSet<Date>();
									StringBuffer publishErrMsg = new StringBuffer();
									for(Date scribDate : scribDates) {
										Collection wavePublishes = getDispatchManagerService().getWaveInstancePublish(scribDate);
										if(wavePublishes != null && wavePublishes.size() > 0) {
											hasPublishedScrib.add(scribDate);
											if(publishErrMsg.length() > 0) {
												publishErrMsg.append(",");
											}
											publishErrMsg.append(TransStringUtil.getDate(scribDate));
										}
									}
									this.getDispatchManagerService().uploadScrib(scribDates, scribs);
									saveMessage(request, getMessage("app.actionmessage.163", new Object[]{newSource.getDescription()}));
								}
							}
						}
					} else {
						
						Set<String> facilityErrorLst = new HashSet<String>();
						Set<String> regionErrorLst = new HashSet<String>();
						
						List<ScheduleEmployee> schedules = new ScheduleUploadDataManager()
																.processUploadSchedules(request, bean.getFile(), newSource);
																		
						boolean validDepotFacility = false;
						boolean validDepotRegion = false;
						boolean validGroupTime = false;
						TrnFacility _depotFacility = null;
						
						if(schedules != null && schedules.size() > 0) {
							for(ScheduleEmployee _scheduleEmp : schedules) {
								
								if(_scheduleEmp.getDepotFacility() != null) {
									_depotFacility = facilityMapping.get(_scheduleEmp.getDepotFacilityS());
									if(_depotFacility == null) {
										facilityErrorLst.add(_scheduleEmp.getDepotFacilityS());
									} else {
										_scheduleEmp.setDepotFacilityS(_depotFacility.getFacilityId());
									}
							    }
								
								if(_scheduleEmp.getRegionS() != null){
									Region _region = regionMapping.get(_scheduleEmp.getRegionS());									
									if(_region == null){
										regionErrorLst.add(_scheduleEmp.getRegionS());
									}
								}
								
								if(_scheduleEmp.getRegionS() != null && "Depot".equals(_scheduleEmp.getRegionS()) && (_scheduleEmp.getDepotFacilityS() == null || TransStringUtil.isEmpty(_scheduleEmp.getDepotFacilityS()))) {
									validDepotFacility = true;
								}
								
								if(_scheduleEmp.getDepotFacilityS() != null && (_scheduleEmp.getRegionS() == null || TransStringUtil.isEmpty(_scheduleEmp.getRegionS()))){
									validDepotRegion = true;
								}
								if(_scheduleEmp.getRegionS() != null &&	(_scheduleEmp.getDispatchGroupS() == null || TransStringUtil.isEmpty(_scheduleEmp.getDispatchGroupS()))) {
									validGroupTime = true;
								}
							}
								
							if((regionErrorLst != null && regionErrorLst.size() > 0) || (facilityErrorLst != null && facilityErrorLst.size() > 0) 
									|| validDepotFacility || validDepotRegion || validGroupTime){
								
								if(facilityErrorLst.size() > 0) 
									uploadErrMsg.append("Depot Facility ");
								for(String _error : facilityErrorLst){
									uploadErrMsg.append(_error);
									if(uploadErrMsg.length() > 0) {
										uploadErrMsg.append(",");
									}
								}
								if(facilityErrorLst.size() > 0) 
									uploadErrMsg.append(" doesn't exist in active Facilities ");
								
								if(regionErrorLst.size() > 0) 
									uploadErrMsg.append("<br>").append(" Region ");
								for(String _error : regionErrorLst){
									uploadErrMsg.append(_error);
									if(uploadErrMsg.length() > 0) {
										uploadErrMsg.append(",");
									}
								}
								if(regionErrorLst.size() > 0) { 
									uploadErrMsg.append(" doesn't exist in active Regions ");
								}
								
								if(validDepotFacility) {
									uploadErrMsg.append("<br>").append(" Facility is empty for one or more entries for Depot region(s).");
								}
								
								if(validDepotRegion) {
									uploadErrMsg.append("<br>").append(" Region is empty for one or more entries for Depot facility(s).");
								}						
	
								if(validGroupTime) {
									uploadErrMsg.append("<br>").append(" Dispatch group Time is empty for one or more entries.");
								}
								
								saveErrorMessage(request, getMessage("app.error.131", new Object[] {newSource.getDescription()}));
								saveErrorMessage(request, uploadErrMsg.toString());
							} else {
								getDomainManagerService().saveOrUpdateEmployeeSchedule(schedules);
								saveMessage(request, getMessage("app.actionmessage.163", new Object[]{newSource.getDescription()}));
							}
					}					
				}
			}	
		  }
		} catch(DataIntegrityViolationException ex) {
			ex.printStackTrace();
			saveErrorMessage(request, getMessage("app.error.131", new Object[] {newSource.getDescription()}));
		} catch(Exception e) {
			e.printStackTrace();
			saveErrorMessage(request, getMessage("app.error.131", new Object[] {newSource.getDescription()}));
		}		
		
		ModelAndView mav = new ModelAndView(getSuccessView(), errors.getModel());
		mav.getModel().put(this.getCommandName(), command);
		mav.getModel().putAll(referenceData(request));
		
		return mav;
	}

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException  {
	    logger.info("FileUploadController -- custom editor to convert Multipart instance to byte[] is registered here.");
	    binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());	   
	}

}

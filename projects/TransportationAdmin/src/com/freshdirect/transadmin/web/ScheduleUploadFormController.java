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

	protected ModelAndView onSubmit(HttpServletRequest request,HttpServletResponse response,
		      Object command, BindException errors) throws ServletException, IOException    {
			
		ScheduleFileUploadCommand bean = (ScheduleFileUploadCommand) command;
		byte[] bytes = bean.getFile();
		EnumUploadSource newSource = EnumUploadSource.SCHEDULE;
		try{
			if(bean.getProcessType() != null) {
				newSource = EnumUploadSource.getEnum(bean.getProcessType());
				
				Collection masterRegions = getDomainManagerService().getRegions();
				Collection masterZones = getDomainManagerService().getZones();
				Collection facilityLocs = getLocationManagerService().getTrnFacilitys();
				Map<String, Zone> zoneMapping = new HashMap<String, Zone>();
				Map<String, Region> regionMapping = new HashMap<String, Region>();
			
				if(newSource != null) {
					if(newSource.equals(EnumUploadSource.SCRIB)) {
						
						List<Scrib> scribs = new ScheduleUploadDataManager()
																		.processUploadScrib(request
																			, bytes
																			, com.freshdirect.transadmin.security.SecurityManager.getUserName(request)
																			, domainManagerService);
						if(scribs != null) {
							Set<Date> scribDates = new TreeSet<Date>();
							
							Map<String, TrnFacility> facilityMapping = new HashMap<String, TrnFacility>();
							
							if(masterZones != null && facilityLocs != null) {
								Iterator _zoneItr = masterZones.iterator();
								Zone zone = null;
								while(_zoneItr.hasNext()) {
									zone = (Zone)_zoneItr.next();									
									zoneMapping.put(zone.getZoneCode(), zone);
								}
								Iterator _facilityItr = facilityLocs.iterator();
								TrnFacility facility = null;
								while(_facilityItr.hasNext()){
									facility = (TrnFacility)_facilityItr.next();
									facilityMapping.put(facility.getName(), facility);
								}
								TrnFacility originFacility = null;
								TrnFacility destFacility = null;
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
									originFacility = facilityMapping.get(scrib.getOriginFacility() != null ? scrib.getOriginFacility().getName() : "");
									destFacility = facilityMapping.get(scrib.getDestinationFacility() != null ? scrib.getDestinationFacility().getName() : "");
									scrib.setOriginFacility(originFacility);
									scrib.setDestinationFacility(destFacility);
									if(zone != null) {
										scrib.setZone(zone);
										scrib.setRegion(zone.getRegion());
									}
									scribDates.add(scrib.getScribDate());
								}
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
								//if(hasPublishedScrib.size() == 0) {
									this.getDispatchManagerService().uploadScrib(scribDates, scribs);
									saveMessage(request, getMessage("app.actionmessage.161", new Object[]{newSource.getDescription()}));
								//} else {
									//saveErrorMessage(request, getMessage("app.error.132", new Object[] {publishErrMsg.toString()}));
								//}								
							}
						}
					} else {
						StringBuffer errorMsg = new StringBuffer();
						Set<String> zoneErrorLst = new HashSet<String>();
						Set<String> regionErrorLst = new HashSet<String>();
						
						List<ScheduleEmployee> schedules = new ScheduleUploadDataManager()
																		.processUploadSchedules(request
																			, bytes
																			, com.freshdirect.transadmin.security.SecurityManager.getUserName(request)
																			, domainManagerService);
						if(masterZones != null) {
							Iterator _zoneItr = masterZones.iterator();
							Zone zone = null;
							while(_zoneItr.hasNext()) {
								zone = (Zone)_zoneItr.next();									
								zoneMapping.put(zone.getZoneCode(), zone);
							}
						}
						
						if(masterRegions != null) {
							Iterator _regionItr = masterRegions.iterator();
							Region region = null;
							while(_regionItr.hasNext()) {
								region = (Region)_regionItr.next();									
								regionMapping.put(region.getCode(), region);
							}
						}
						boolean validDepotZone = false;
						boolean validDepotRegion = false;
						if(schedules != null) {
							for(ScheduleEmployee _scheduleEmp : schedules) {
								if(_scheduleEmp.getDepotZone() != null){
									int _codelength = _scheduleEmp.getDepotZoneS() != null ? _scheduleEmp.getDepotZoneS().length() : 3;				
									if(_codelength < 3) {
										StringBuffer strBuf = new StringBuffer();
										while(3 - _codelength > 0) {
											strBuf.append("0");
											_codelength++;
										}
										_scheduleEmp.setDepotZoneS(strBuf.toString() + _scheduleEmp.getDepotZoneS());
									}
									Zone _depotZone = zoneMapping.get(_scheduleEmp.getDepotZoneS());
									if(_depotZone == null){
										zoneErrorLst.add(_scheduleEmp.getDepotZoneS());
									}
							    }
								if(_scheduleEmp.getRegionS() != null){
									Region _region = regionMapping.get(_scheduleEmp.getRegionS());									
									if(_region == null){
										regionErrorLst.add(_scheduleEmp.getRegionS());
									}
								}
								
								if(_scheduleEmp.getRegionS() != null && _scheduleEmp.getRegionS() == "Depot" &&
										_scheduleEmp.getDepotZoneS() == null) {
									validDepotZone = true;
								}
								
								if(_scheduleEmp.getDepotZoneS() != null && _scheduleEmp.getRegionS() == null){
									validDepotRegion = true;
								}
							}							
						}
						
						if((regionErrorLst != null && regionErrorLst.size() > 0) || (zoneErrorLst != null && zoneErrorLst.size() > 0) || validDepotZone || validDepotRegion){
							StringBuffer uploadErrMsg = new StringBuffer();
							if(zoneErrorLst.size() > 0) 
								uploadErrMsg.append("zone ");
							for(String _error : zoneErrorLst){
								uploadErrMsg.append(_error);
								if(uploadErrMsg.length() > 0) {
									uploadErrMsg.append(",");
								}
							}
							if(zoneErrorLst.size() > 0) 
								uploadErrMsg.append(" doesn't exist in active zones ");
							
							if(regionErrorLst.size() > 0) 
								uploadErrMsg.append("Region ");
							for(String _error : regionErrorLst){
								uploadErrMsg.append(_error);
								if(uploadErrMsg.length() > 0) {
									uploadErrMsg.append(",");
								}
							}
							if(regionErrorLst.size() > 0) { 
								uploadErrMsg.append(" doesn't exist in active regions ");
							}
							
							if(validDepotZone) {
								uploadErrMsg.append(" Zone is empty for one or more entries for Depot region. Please check the file");
							}
							
							if(validDepotRegion) {
								uploadErrMsg.append(" Region is empty for one or more entries for Depot zone(s). Please check the file");
							}
							
							saveErrorMessage(request, getMessage("app.error.131", new Object[] {newSource.getDescription()}));
							saveErrorMessage(request, uploadErrMsg.toString());
						} else {
							getDomainManagerService().saveOrUpdateEmployeeSchedule(schedules);
							saveMessage(request, getMessage("app.actionmessage.161", new Object[]{newSource.getDescription()}));
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
	    // to actually be able to convert Multipart instance to byte[]
	    // we have to register a custom editor
	    logger.info("FileUploadController -- custom editor to convert Multipart instance to byte[] is registered here.");
	    binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
	    // now Spring knows how to handle multipart object and convert them
	}

}

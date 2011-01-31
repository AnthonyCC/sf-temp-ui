package com.freshdirect.transadmin.web;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
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
import com.freshdirect.transadmin.model.ScheduleEmployee;
import com.freshdirect.transadmin.model.Scrib;
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
				if(newSource != null) {
					if(newSource.equals(EnumUploadSource.SCRIB)) {
						
						List<Scrib> scribs = new ScheduleUploadDataManager()
																		.processUploadScrib(request
																			, bytes
																			, com.freshdirect.transadmin.security.SecurityManager.getUserName(request)
																			, domainManagerService);
						if(scribs != null) {
							Set<Date> scribDates = new TreeSet<Date>();
							Collection masterZones = getDomainManagerService().getZones();
							Map<String, Zone> zoneMapping = new HashMap<String, Zone>();
							if(masterZones != null) {
								Iterator _zoneItr = masterZones.iterator();
								Zone zone = null;
								while(_zoneItr.hasNext()) {
									zone = (Zone)_zoneItr.next();									
									zoneMapping.put(zone.getZoneCode(), zone);
								}
								for(Scrib scrib : scribs) {
									int _codelength = scrib.getZoneS().length();				
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
						List<ScheduleEmployee> schedules = new ScheduleUploadDataManager()
																		.processUploadSchedules(request
																			, bytes
																			, com.freshdirect.transadmin.security.SecurityManager.getUserName(request)
																			, domainManagerService);
						getDomainManagerService().saveOrUpdateEmployeeSchedule(schedules);
						saveMessage(request, getMessage("app.actionmessage.161", new Object[]{newSource.getDescription()}));
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

package com.freshdirect.transadmin.web;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.transadmin.constants.EnumIssueStatus;
import com.freshdirect.transadmin.constants.EnumServiceStatus;
import com.freshdirect.transadmin.datamanager.assembler.IDataAssembler;
import com.freshdirect.transadmin.datamanager.parser.FileCreator;
import com.freshdirect.transadmin.datamanager.parser.errors.FlatwormCreatorException;
import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.MaintenanceIssue;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.ResourceI;
import com.freshdirect.transadmin.model.ResourceInfoI;
import com.freshdirect.transadmin.model.ScheduleEmployee;
import com.freshdirect.transadmin.model.ScheduleEmployeeInfo;
import com.freshdirect.transadmin.model.TrnAdHocRoute;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.model.VIRRecord;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.model.ZoneSupervisor;
import com.freshdirect.transadmin.model.comparator.AlphaNumericComparator;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.service.ZoneManagerI;
import com.freshdirect.transadmin.util.EnumCachedDataType;
import com.freshdirect.transadmin.util.ModelUtil;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.web.model.ResourceList;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;
import com.freshdirect.transadmin.web.model.WebTeamSchedule;

/**
 * <code>MultiActionController</code> that handles all non-form URL's.
 *
 * @author Sivachandar
 */
public class DomainController extends AbstractMultiActionController {


	private DomainManagerI domainManagerService;

	private LocationManagerI locationManagerService;

	private EmployeeManagerI employeeManagerService;

	private ZoneManagerI zoneManagerService;

	private static final String IS_OBSOLETE = "X";

	private static final DateFormat DATE_FORMAT=new SimpleDateFormat("MM/dd/yyyy");

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView welcomeHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		return new ModelAndView("welcomeView");
	}

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView accessDeniedHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		return new ModelAndView("accessDeniedView");
	}

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView employeeHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        String empStatus = request.getParameter("empstatus");
        String sDate=request.getParameter("sDate");
        Collection dataList = null;
        Collection empList = null;
       
        if("T".equalsIgnoreCase(empStatus)) {
        	dataList = employeeManagerService.getTerminatedEmployees();
        } else if("S".equalsIgnoreCase(empStatus)) {
        	String scheduleDate = request.getParameter("scheduleDate");
        	Date _scheduleWeekOf = null;
        	if(!TransStringUtil.isEmpty(scheduleDate)) {
        		_scheduleWeekOf = getWeekOf(scheduleDate);
        	} 
        	
        	if(_scheduleWeekOf == null) {
	        	int dayOfWeek = TransStringUtil.getDayOfWeek(Calendar.getInstance().getTime());
	        	
	        	if(dayOfWeek == Calendar.FRIDAY || dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
	        		_scheduleWeekOf = TransStringUtil.getAdjustedWeekOf(Calendar.getInstance().getTime(), 14);
	        	} else {
	        		_scheduleWeekOf = TransStringUtil.getAdjustedWeekOf(Calendar.getInstance().getTime(), 7);
	        	}
        	}
        	
        	dataList = employeeManagerService.getScheduleEmployees(_scheduleWeekOf);
        	String status=request.getParameter("status");
        	if(status == null) {
        		status="a";
        	}
			if ("a".equalsIgnoreCase(status) || "i".equalsIgnoreCase(status)) {
				for (Iterator it = dataList.iterator(); it.hasNext();) {
					ScheduleEmployeeInfo sInfo = (ScheduleEmployeeInfo) it.next();
					if ("a".equalsIgnoreCase(status)
							&& "false".equalsIgnoreCase(sInfo.getTrnStatus())) {
						it.remove();
					}
					if ("i".equalsIgnoreCase(status)
							&& ("true".equalsIgnoreCase(sInfo.getTrnStatus()))) {
						it.remove();
					}
					if ("i".equalsIgnoreCase(status)
							&& sInfo.getTrnStatus() == null
							&& "A".equalsIgnoreCase(sInfo.getStatus())) {
						it.remove();
					}
					if ("a".equalsIgnoreCase(status)
							&& sInfo.getTrnStatus() == null
							&& "I".equalsIgnoreCase(sInfo.getStatus())) {
						it.remove();
					}
				}
			}
			if("y".equalsIgnoreCase(request.getParameter("export"))&&!TransStringUtil.isEmpty(request.getParameter("sDate"))){				
				exportSchedules(request, response, sDate, status);
				return null;
			}
			
        	request.setAttribute("status", status);
        	request.setAttribute("scheduleDate", getClientDate(_scheduleWeekOf));
        	request.setAttribute("uploadScheduleDate", sDate);
        	return new ModelAndView("scheduleView","employees",dataList);
		} else if("C".equalsIgnoreCase(empStatus)) {
        	
        	Date _currentScheduleWeekOf = getCurrentWeekOf();
        	Date _masterScheduleWeekOf = getMasterWeekOf();
        	Collection<WebTeamSchedule> result = new TreeSet<WebTeamSchedule>();
        	
        	WebTeamSchedule sTeamInfo = null;
        	
        	if(_currentScheduleWeekOf != null && _masterScheduleWeekOf != null) {
        		Collection<ScheduleEmployeeInfo> masterSchedule = employeeManagerService
        															.getScheduleEmployees(_masterScheduleWeekOf);
        		Collection<ScheduleEmployeeInfo> currentSchedule = employeeManagerService
        															.getScheduleEmployees(_currentScheduleWeekOf);
        		Map<String, String> teamMapping = employeeManagerService.getTeamMapping();
        		
        		if(masterSchedule != null && currentSchedule != null && teamMapping != null) {
        			Map<String, ScheduleEmployeeInfo> masterSchMapping = ModelUtil.getIdMappedSchedule(masterSchedule);
        			Map<String, ScheduleEmployeeInfo> currentSchMapping = ModelUtil.getIdMappedSchedule(currentSchedule);
        			Map<String, WebTeamSchedule> leadTeamScheduleMp = new HashMap<String, WebTeamSchedule>();
        			String leadId = null;
        			for(String member : teamMapping.keySet()) {
        				leadId = teamMapping.get(member);
        				if(!leadTeamScheduleMp.containsKey(leadId)) {
        					sTeamInfo =  new WebTeamSchedule();
        					sTeamInfo.setLeadMasterSchedule(masterSchMapping.get(leadId));
        					sTeamInfo.setLeadSchedule(currentSchMapping.get(leadId));
        					sTeamInfo.setLead(employeeManagerService.getEmployeeEx(leadId));
        					leadTeamScheduleMp.put(leadId, sTeamInfo);
        				}
        				leadTeamScheduleMp.get(leadId).getMemberMasterSchedule().add(masterSchMapping.get(member));
        				leadTeamScheduleMp.get(leadId).getMemberSchedule().add(currentSchMapping.get(member));
        				leadTeamScheduleMp.get(leadId).getMembers().add(employeeManagerService.getEmployeeEx(member));
        			}
        			result.addAll(leadTeamScheduleMp.values());
        		}
        	}
        	
        	return new ModelAndView("teamScheduleView","employees",result);
		} else {
			if ("true".equalsIgnoreCase(request.getParameter("sync"))) {
				employeeManagerService.syncEmployess();
			}
			dataList = employeeManagerService.getEmployees();
		}
        return new ModelAndView("employeeView","employees",dataList);
	}

	public ModelAndView uploadSchedulesHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		return new ModelAndView("uploadScheduleView");
	}
	private void exportSchedules(HttpServletRequest request,
			HttpServletResponse response, String sDate, String status) {
		Collection empList;
		
			try {
				Date _sDate = TransStringUtil.getWeekOf(TransStringUtil.getServerDate(sDate));
				String day = null;
				String DAY[] = new String[] { "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN" };
				day = TransStringUtil.getDayofWeek(sDate);
				if ("Monday".equalsIgnoreCase(day))
					day = DAY[0];
				else if ("Tuesday".equalsIgnoreCase(day))
					day = DAY[1];
				else if ("Wednesday".equalsIgnoreCase(day))
					day = DAY[2];
				else if ("Thursday".equalsIgnoreCase(day))
					day = DAY[3];
				else if ("Friday".equalsIgnoreCase(day))
					day = DAY[4];
				else if ("Saturday".equalsIgnoreCase(day))
					day = DAY[5];
				else if ("Sunday".equalsIgnoreCase(day))
					day = DAY[6];
				empList = employeeManagerService.getScheduleEmployees(_sDate,day);
				if ("a".equalsIgnoreCase(status) || "i".equalsIgnoreCase(status)) {
					for (Iterator it = empList.iterator(); it.hasNext();) {
						ScheduleEmployeeInfo sInfo = (ScheduleEmployeeInfo) it.next();
						if ("a".equalsIgnoreCase(status)
								&& "false".equalsIgnoreCase(sInfo.getTrnStatus())) {
							it.remove();
						}
						if ("i".equalsIgnoreCase(status)
								&& ("true".equalsIgnoreCase(sInfo.getTrnStatus()))) {
							it.remove();
						}
						if ("i".equalsIgnoreCase(status)
								&& sInfo.getTrnStatus() == null
								&& "A".equalsIgnoreCase(sInfo.getStatus())) {
							it.remove();
						}
						if ("a".equalsIgnoreCase(status)
								&& sInfo.getTrnStatus() == null
								&& "I".equalsIgnoreCase(sInfo.getStatus())) {
							it.remove();
						}
					}
				}
				generateScheduleFile(sDate,empList,request,response);				
			} catch (Exception e) {					
				e.printStackTrace();
			}		
	}

	private void generateScheduleFile(String date, Collection empList,HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		
		Collection schedules=new ArrayList();
		Map<String, ScheduleEmployee> result=new HashMap();
		for (Iterator it = empList.iterator(); it.hasNext();) {
			ScheduleEmployeeInfo _sInfo = (ScheduleEmployeeInfo) it.next();
			schedules = _sInfo.getSchedule();
			ScheduleEmployee _empS=null;
			if(_sInfo.getSchedule().size()==0){
				_empS = new ScheduleEmployee();
				_empS.setEmployeeId(_sInfo.getEmployeeId());
				_empS.setDate(TransStringUtil.getDate(date));
				result.put(_empS.getEmployeeId(),_empS);
			}
			
			for (Iterator iterator = schedules.iterator(); iterator.hasNext();) {
				ScheduleEmployee _empSchedule=(ScheduleEmployee)iterator.next();
					_empSchedule.setDate(TransStringUtil.getDate(date));
					result.put(_empSchedule.getEmployeeId(),_empSchedule);
			}
		}
		
		String exportFileName = TransportationAdminProperties.getExportSchedulesFilename()
								+com.freshdirect.transadmin.security.SecurityManager.getUserName(request)
								+".csv";
		
		generateEmployeeScheduleFile(TransportationAdminProperties.getEmployeeScheduleOutputFormat(), exportFileName, "row", "rowBean", result, null);
				
		File outputFile = new File(exportFileName);
		response.setBufferSize((int)outputFile.length());

		response.setHeader("Content-Disposition", "attachment; filename=\""+outputFile.getName()+"\"");
		response.setContentType("application/x-download");
		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "max-age=0");
		response.setContentLength((int)outputFile.length());
		FileCopyUtils.copy(new FileInputStream(outputFile), response.getOutputStream());
		
	}
	
	public boolean generateEmployeeScheduleFile(String configurationPath, String outputFile, String recordName,String beanName, Map data, IDataAssembler assembler) {
		
        try {
        	FileCreator creator = new FileCreator(configurationPath, outputFile);
        	creator.setRecordSeperator(TransportationAdminProperties.getFileSeparator());
        	creator.open();        	        	
        	
        	if(data != null) {
        		Iterator iterator = data.values().iterator();
        		Object tmp = null;
	        	while(iterator.hasNext()) {
	        		tmp = iterator.next();
	        		creator.setBean(beanName, tmp);
	                creator.write(recordName);
	        	}	        	
        	}        	
            // Close buffered output to write contents
            creator.close();
            return true;
        } catch (FlatwormCreatorException flatwormUnsetFieldValueError) {
            flatwormUnsetFieldValueError.printStackTrace();  
        } catch (IOException ioExp) {
        	ioExp.printStackTrace();  
        } 
        return false;
	}

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView xEmployeeHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
     //   System.out.println("inside terminated EmployeeHandler");
		Collection dataList = employeeManagerService.getTerminatedEmployees();
		return new ModelAndView("terminatedEmployeeView","employees",dataList);
	}


	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView adHocRouteHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		Collection dataList = domainManagerService.getAdHocRoutes();
		return new ModelAndView("routeView","routes",dataList);
	}

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView zoneHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

        String zoneType = request.getParameter("zoneType");
        Collection dataList = null;
        Collection activeZoneCodes = null;

        if("Active".equalsIgnoreCase(zoneType)) {
        	dataList = domainManagerService.getZones();
        	activeZoneCodes = zoneManagerService.getActiveZoneCodes();
        	if(dataList != null && activeZoneCodes != null) {
        		Iterator _iterator = dataList.iterator();
        		Zone _tmpZone = null;
        		while(_iterator.hasNext()) {
        			_tmpZone = (Zone)_iterator.next();        			
        			setResourceInfo(_tmpZone);
        			if(!activeZoneCodes.contains(_tmpZone.getZoneCode())) {
        				_iterator.remove();
        			}
        		}
        	}
        } else {
        	dataList = domainManagerService.getActiveZones();
        }
		return new ModelAndView("zoneView","zones",dataList);
	}

	private void setResourceInfo(Zone _tmpZone) {
		List amZoneSupervisors = new ResourceList();
		List pmZoneSupervisors = new ResourceList();
		Collection supervisorLst = new ArrayList();
		WebEmployeeInfo webEmpInfo = null;
		try {
			supervisorLst = zoneManagerService.getDefaultZoneSupervisor(_tmpZone.getZoneCode(), "AM", TransStringUtil.getCurrentDate());
			for (Iterator<ZoneSupervisor> iterator = supervisorLst.iterator(); iterator.hasNext();) {		
				ZoneSupervisor _zoneSupervisor = iterator.next();
				if("AM".equalsIgnoreCase(_zoneSupervisor.getDayPart())){
					webEmpInfo = employeeManagerService.getEmployee(_zoneSupervisor.getSupervisorId());
					_zoneSupervisor.setSupervisorFirstName(webEmpInfo.getEmpInfo().getFirstName());
					_zoneSupervisor.setSupervisorLastName(webEmpInfo.getEmpInfo().getLastName());
					amZoneSupervisors.add(_zoneSupervisor);
				}
			}
			supervisorLst = zoneManagerService.getDefaultZoneSupervisor(_tmpZone.getZoneCode(), "PM", TransStringUtil.getCurrentDate());
			for (Iterator<ZoneSupervisor> iterator = supervisorLst.iterator(); iterator.hasNext();) {		
				ZoneSupervisor _zoneSupervisor = iterator.next();
				if("PM".equalsIgnoreCase(_zoneSupervisor.getDayPart())){
					webEmpInfo = employeeManagerService.getEmployee(_zoneSupervisor.getSupervisorId());
					_zoneSupervisor.setSupervisorFirstName(webEmpInfo.getEmpInfo().getFirstName());
					_zoneSupervisor.setSupervisorLastName(webEmpInfo.getEmpInfo().getLastName());
					pmZoneSupervisors.add(_zoneSupervisor);
				}
			}
			_tmpZone.setAmZoneSupervisors(amZoneSupervisors);
			_tmpZone.setPmZoneSupervisors(pmZoneSupervisors);
			
		} catch (Exception e) {				
			e.printStackTrace();
		}
	}

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView regionHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		Collection dataList = domainManagerService.getRegions();
		return new ModelAndView("regionView","regions",dataList);
	}

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView truckHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		String isRefreshReqd=request.getParameter("refresh");
		if("true".equalsIgnoreCase(isRefreshReqd)){
			domainManagerService.refreshCachedData(EnumCachedDataType.TRUCK_DATA);
		}
		Collection dataList = domainManagerService.getTrucks();
		return new ModelAndView("truckView","trucks",dataList);
	}

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView areaHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		Collection dataList = domainManagerService.getAreas();
		return new ModelAndView("areaView","areas",dataList);
	}

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView cutOffHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		Collection dataList = domainManagerService.getCutOffs();
		return new ModelAndView("cutOffView","cutoffs",dataList);
	}


	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView employeeDeleteHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		Set employeeSet=new HashSet();
		String arrEntityList[] = getParamList(request);
		WebEmployeeInfo tmpEntity = null;
		if (arrEntityList != null) {
			int arrLength = arrEntityList.length;
			for (int intCount = 0; intCount < arrLength; intCount++) {
				tmpEntity = employeeManagerService.getEmployee(arrEntityList[intCount]);
				//tmpEntity.setObsolete(IS_OBSOLETE);
				employeeSet.add(tmpEntity);
			}
		}
		domainManagerService.saveEntityList(employeeSet);
		saveMessage(request, getMessage("app.actionmessage.103", null));
		return employeeHandler(request, response);
	}

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView routeAdHocDeleteHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		Set routeSet=new HashSet();
		String arrEntityList[] = getParamList(request);
		TrnAdHocRoute tmpEntity = null;
		if (arrEntityList != null) {
			int arrLength = arrEntityList.length;
			for (int intCount = 0; intCount < arrLength; intCount++) {
				//System.out.println(" arrEntityList[intCount] :"+arrEntityList[intCount]);
				tmpEntity = domainManagerService.getAdHocRoute(arrEntityList[intCount]);
				tmpEntity.setObsolete(IS_OBSOLETE);
				routeSet.add(tmpEntity);
			}
		}
		domainManagerService.saveEntityList(routeSet);
		saveMessage(request, getMessage("app.actionmessage.103", null));
		return adHocRouteHandler(request, response);
	}

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView zoneDeleteHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		Set zoneSet=new HashSet();
		String arrEntityList[] = getParamList(request);
		Zone tmpEntity = null;
		if (arrEntityList != null) {
			int arrLength = arrEntityList.length;
			for (int intCount = 0; intCount < arrLength; intCount++) {
				tmpEntity = domainManagerService.getZone(arrEntityList[intCount]);
				tmpEntity.setObsolete(IS_OBSOLETE);
				zoneSet.add(tmpEntity);
			}
		}
		domainManagerService.saveEntityList(zoneSet);
		saveMessage(request, getMessage("app.actionmessage.103", null));
		return zoneHandler(request, response);
	}


	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView regionDeleteHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		Set zoneSet=new HashSet();
		String arrEntityList[] = getParamList(request);
		Region tmpEntity = null;
		if (arrEntityList != null) {
			int arrLength = arrEntityList.length;
			for (int intCount = 0; intCount < arrLength; intCount++) {
				tmpEntity = domainManagerService.getRegion(arrEntityList[intCount]);

				// check if zone exist in db then dont delete
				Set zones=tmpEntity.getZones();

				//System.out.println("zones7836827362873628731267863 :"+zones);

				if(zones!=null && zones.size()>0)
				{
					saveMessage(request, getMessage("app.actionmessage.137", null));
					return regionHandler(request, response);
				}
				tmpEntity.setObsolete(IS_OBSOLETE);
				zoneSet.add(tmpEntity);
			}
		}
		domainManagerService.saveEntityList(zoneSet);
		saveMessage(request, getMessage("app.actionmessage.103", null));
		return regionHandler(request, response);
	}


	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView areaDeleteHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		Set areaSet=new HashSet();
		String arrEntityList[] = getParamList(request);
		TrnArea tmpEntity = null;
		if (arrEntityList != null) {
			int arrLength = arrEntityList.length;
			for (int intCount = 0; intCount < arrLength; intCount++) {
				tmpEntity = domainManagerService.getArea(arrEntityList[intCount]);
				areaSet.add(tmpEntity);
			}
		}
		try {
			domainManagerService.removeEntity(areaSet);
			saveMessage(request, getMessage("app.actionmessage.103", null));
		} catch (DataIntegrityViolationException e) {
			saveMessage(request, getMessage("app.actionmessage.127", null));
		}
		return areaHandler(request, response);
	}

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView cutOffDeleteHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		Set cutOffSet=new HashSet();
		String arrEntityList[] = getParamList(request);
		TrnCutOff tmpEntity = null;
		if (arrEntityList != null) {
			int arrLength = arrEntityList.length;
			for (int intCount = 0; intCount < arrLength; intCount++) {
				tmpEntity = domainManagerService.getCutOff(arrEntityList[intCount]);
				cutOffSet.add(tmpEntity);
			}
		}
		try {
			domainManagerService.removeEntity(cutOffSet);
			saveMessage(request, getMessage("app.actionmessage.103", null));
		} catch (DataIntegrityViolationException e) {
			saveMessage(request, getMessage("app.actionmessage.127", null));
		}
		return cutOffHandler(request, response);
	}

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView zoneTypeDeleteHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		Set zoneTypeSet=new HashSet();
		String arrEntityList[] = getParamList(request);
		//Collection dataLst = locationManagerService.getServiceTimesForZoneTypes(Arrays.asList(arrEntityList));
		//Collection scenarioLst = locationManagerService.getScenariosForZoneTypes(Arrays.asList(arrEntityList));
		TrnZoneType tmpEntity = null;
		boolean hasError = false;
		if (arrEntityList != null) {
			int arrLength = arrEntityList.length;
			for (int intCount = 0; intCount < arrLength; intCount++) {
				tmpEntity = domainManagerService.getZoneType(arrEntityList[intCount]);
				zoneTypeSet.add(tmpEntity);
			}
			try {
				domainManagerService.removeEntity(zoneTypeSet);
				saveMessage(request, getMessage("app.actionmessage.103", null));
			} catch (DataIntegrityViolationException e) {
				e.printStackTrace();
				hasError = true;
			}
		} else {
			hasError = true;
		}
		if(hasError) {
			saveMessage(request, getMessage("app.actionmessage.127", null));
		}
		return zoneTypeHandler(request, response);
	}

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView zoneTypeHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		Collection dataList = domainManagerService.getZoneTypes();
		/*Iterator it=dataList.iterator();
		while(it.hasNext()) {
			TrnZoneType zoneType=(TrnZoneType)it.next();
			//System.out.println("zoneType is "+zoneType.getName());
			Set ztr=zoneType.getZonetypeResources();
			if(ztr!=null) {
				Iterator _it=ztr.iterator();
				while(_it.hasNext()) {
					ZonetypeResource _ztr=(ZonetypeResource)_it.next();
					//System.out.println(_ztr.getEmployeeRoleType().getDescription());
					//System.out.println("_ztr.getMaximumNo() :"+_ztr.getMaximumNo()+"_ztr.getRequiredNo() :"+_ztr.getRequiredNo());

				}
			}
		}*/
		return new ModelAndView("zoneTypeView","zonetypes",dataList);
	}

	
	public ModelAndView compositeRouteHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException  {
		String routeType = request.getParameter("routetype");
		if("R".equalsIgnoreCase(routeType)) {
			return routeHandler(request, response);
		} else {
			return adHocRouteHandler(request, response);
		}
	}

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 * @throws ParseException
	 */
	public ModelAndView routeHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		boolean hasError=false;
		String routeDate = request.getParameter("routeDate");
		ModelAndView mav = new ModelAndView("routeView");

		try {
			if(!TransStringUtil.isEmpty(routeDate)) {
				//DateFormat format= new SimpleDateFormat("MM/dd/yyyy");
					Date rouDate=DATE_FORMAT.parse(routeDate);
					Collection dataList = domainManagerService.getRoutes(TransStringUtil.getServerDate(routeDate));
					mav.getModel().put("routes",dataList);
					mav.getModel().put("routeDate", routeDate);
			} else{
				//routeDate=DATE_FORMAT.format(new Date());
				//System.out.println("requested date :"+routeDate);
				Collection dataList = domainManagerService.getRoutes(TransStringUtil.getServerDate(new Date()));
				mav.getModel().put("routes",dataList);
				mav.getModel().put("routeDate",TransStringUtil.getDate(new Date()));
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			hasError=true;
			saveMessage(request, getMessage("app.error.115", new String[]{"Invalid Date"}));
			return mav;
		}

		return mav;
	}

	/**
	 * Custom handler for Transportation Truck Maintenance Log
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView virRecordLogHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		ModelAndView mav = new ModelAndView("virRecordLogView");
		
		String issueLog = request.getParameter("issueLog");
		String currentDate = request.getParameter("currentDate");
		String createDate = request.getParameter("createDate");		
		String truckNumber = request.getParameter("truckNumber");

		if(createDate==null)createDate = TransStringUtil.getCurrentDate();
		if(TransStringUtil.isEmpty(currentDate) || currentDate == null)
			currentDate = TransStringUtil.getCurrentDate();		
		
		Collection virRecords = null;
		Date _createdDate = null;
		try{
			if(!TransStringUtil.isEmpty(createDate))
				_createdDate = TransStringUtil.getDate(createDate);
									
			if(_createdDate != null || !TransStringUtil.isEmpty(truckNumber))				
				virRecords = getDomainManagerService().getVIRRecords(_createdDate, truckNumber);
			else
				virRecords = getDomainManagerService().getVIRRecords();
			
			if(virRecords != null && virRecords.size()==0 && !"I".equals(issueLog) && !"S".equals(issueLog))
				saveMessage(request, getMessage("app.error.139", new String[]{}));
		}catch(Exception e){
			e.printStackTrace();
			saveMessage(request, getMessage("app.error.138", new String[]{}));
		}
		if(virRecords != null && virRecords.size() > 0)
			Collections.sort((List)virRecords, new VIRRecordComparator());		
		
		mav.getModel().put("createDate",createDate);	
		mav.getModel().put("truckNumber", truckNumber);
		mav.getModel().put("currentDate",currentDate);		
		mav.getModel().put("issueTypes", getDomainManagerService().getIssueTypes());
		mav.getModel().put("issueSubTypes", getDomainManagerService().getIssueSubTypes());
		mav.getModel().put("virRecords", virRecords);
		return mav;
	}
	
	/**
	 * Custom handler for Transportation Truck Maintenance Log
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView maintenanceLogHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		ModelAndView mav = new ModelAndView("maintenanceLogView");
		String serviceStatus = request.getParameter("serviceStatus");
		String issueStatus = request.getParameter("issueStatus");
		if(issueStatus == null)issueStatus = EnumIssueStatus.OPEN.getName();
		
		Collection maintenaceRecords = null;
		try{
			if(!TransStringUtil.isEmpty(issueStatus) || !TransStringUtil.isEmpty(serviceStatus))
				maintenaceRecords = getDomainManagerService().getMaintenanceIssues(issueStatus, serviceStatus);					
			else
				maintenaceRecords = getDomainManagerService().getMaintenanceIssues();
		}catch(Exception e){
			e.printStackTrace();
			saveMessage(request, getMessage("app.error.138", new String[]{}));
		}
		
		if(maintenaceRecords != null && maintenaceRecords.size()==0)
			saveMessage(request, getMessage("app.error.140", new String[]{}));
		
		if(maintenaceRecords != null && maintenaceRecords.size()>0)
			Collections.sort((List)maintenaceRecords, new MaintenanceIssueComparator());
		
		mav.getModel().put("serviceStatus", serviceStatus);
		mav.getModel().put("issueStatus", issueStatus);
		
		mav.getModel().put("issueStatuses", EnumIssueStatus.getEnumList());
		mav.getModel().put("serviceStatuses", EnumServiceStatus.getEnumList());
		mav.getModel().put("issueTypes", getDomainManagerService().getIssueTypes());
		mav.getModel().put("maintenanceRecords", maintenaceRecords);
		
		return mav;
	}

	private static class MaintenanceIssueComparator implements Comparator{

		public int compare(Object o1, Object o2) {
			if(o1 instanceof MaintenanceIssue && o2 instanceof MaintenanceIssue)
			{
				MaintenanceIssue m1=(MaintenanceIssue)o1;
				MaintenanceIssue m2=(MaintenanceIssue)o2;				
				return m2.getCreateDate().compareTo(m1.getCreateDate());
			}
			return 0;
		}
	}
	
	private static class VIRRecordComparator implements Comparator{

		public int compare(Object o1, Object o2) {
			if(o1 instanceof VIRRecord && o2 instanceof VIRRecord)
			{
				VIRRecord v1=(VIRRecord)o1;
				VIRRecord v2=(VIRRecord)o2;				
				return v2.getCreateDate().compareTo(v1.getCreateDate());
			}
			return 0;
		}
	}

	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}


	public LocationManagerI getLocationManagerService() {
		return locationManagerService;
	}

	public void setLocationManagerService(LocationManagerI locationManagerService) {
		this.locationManagerService = locationManagerService;
	}

	public EmployeeManagerI getEmployeeManagerService() {
		return employeeManagerService;
	}

	public void setEmployeeManagerService(EmployeeManagerI employeeManagerService) {
		this.employeeManagerService = employeeManagerService;
	}

	public ZoneManagerI getZoneManagerService() {
		return zoneManagerService;
	}

	public void setZoneManagerService(ZoneManagerI zoneManagerService) {
		this.zoneManagerService = zoneManagerService;
	}

}

package com.freshdirect.transadmin.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.routing.model.IDeliverySlot;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
import com.freshdirect.transadmin.model.EmployeeTeam;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.Scrib;
import com.freshdirect.transadmin.model.ScribLabel;
import com.freshdirect.transadmin.security.SecurityManager;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.util.scrib.PlanTree;
import com.freshdirect.transadmin.util.scrib.ScheduleEmployeeDetails;
import com.freshdirect.transadmin.web.model.CustomTimeOfDay;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;

public class ScribController extends AbstractMultiActionController
{
	private DispatchManagerI dispatchManagerService;
	private EmployeeManagerI employeeManagerService;
	private DomainManagerI domainManagerService;
	
	
	
	public ModelAndView copyScribHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException 
	{
		try {
			String sourceDate=request.getParameter("sourceDate");
			String destDate=request.getParameter("destinationDate");
			String sourceDay=request.getParameter("sDay");
			
			if(sourceDate!=null&&destDate!=null&&sourceDay!=null)
			{
				List toInsert=new ArrayList();
				List toDelate=new ArrayList();
				String[] sourceDates = TransStringUtil.getDatesEx(sourceDate, sourceDay);
				String[] destDates = TransStringUtil.getDatesEx(destDate, sourceDay);
				if(sourceDates!=null&&sourceDates.length>0&&!sourceDates[0].equalsIgnoreCase(destDates[0]))
				{	
					for(int i=0;i<sourceDates.length;i++)
					{
						Collection scribs=dispatchManagerService.getScribList(sourceDates[i]);
						Collection deleteScribs=dispatchManagerService.getScribList(destDates[i]);
						toDelate.addAll(deleteScribs);
						if(scribs!=null)
							for(Iterator j=scribs.iterator();j.hasNext();)
							{
								Scrib s=(Scrib)j.next();
								s.setScribDate(TransStringUtil.serverDateFormat.parse(destDates[i]));
								s.setScribId(null);
								toInsert.add(s);
								
							}
					}
					dispatchManagerService.removeEntity(toDelate);
					dispatchManagerService.saveEntityList(toInsert);
					saveMessage(request, getMessage("app.actionmessage.148", null));
				}
				
				
			}
			ModelAndView mav = new ModelAndView("copyScribView");
			return mav;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ModelAndView mav = new ModelAndView("copyScribView");
			saveMessage(request, getMessage("app.actionmessage.151", null));
			return mav;
		}
	}
	
	
	public ModelAndView scribHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException 
	{
		try {
			String daterange = request.getParameter("daterange");
			if(daterange==null)daterange=TransStringUtil.getCurrentDate();
			Date _weekDate = getWeekOf(daterange);	
			String day=request.getParameter("scribDay");
			if(day==null)day="All";
			String[] dates = TransStringUtil.getDatesEx(daterange,day);
			List allScribs=new ArrayList();
			Set scribDates = new TreeSet();
		
			if(dates!=null)
			{	
				for(int i=0;i<dates.length;i++)
				{					
					Collection scribs=dispatchManagerService.getScribList(dates[i]);
					ScribLabel scribLabel = dispatchManagerService.getScribLabelByDate(dates[i]);					
					if(scribs!=null)
					for(Iterator j=scribs.iterator();j.hasNext();)
					{
						Scrib s=(Scrib)j.next();
						//if(!(s.getScribDate().before(TransStringUtil.getServerDateString(TransStringUtil.getCurrentServerDate()))))
							scribDates.add(TransStringUtil.getServerDate(s.getScribDate()));
						if (scribLabel!=null)
							s.setScribLabel(scribLabel.getScribLabel());
						if(s.getSupervisorCode()!=null)
						{
							WebEmployeeInfo webEmp=employeeManagerService.getEmployee(s.getSupervisorCode());
							if(webEmp!=null&&webEmp.getEmpInfo()!=null)
							s.setSupervisorName(webEmp.getEmpInfo().getName());
						}
					}
					allScribs.addAll(scribs);					
				}
			}
			ModelAndView mav = new ModelAndView("scribView");
			mav.getModel().put("scriblist", allScribs);
			
			Collection regions = domainManagerService.getRegions();
			mav.getModel().put("regions", regions);
					
			mav.getModel().put("scribDates", scribDates);

			//get the predefined Scrib labels
			String scribLabels= TransportationAdminProperties.getScribHolidayLabels();
			String[] _scribLabels = StringUtil.decodeStrings(scribLabels);
			List sLabels=new ArrayList();
			for(String _slabel:_scribLabels){
				sLabels.add(_slabel);
			}
			mav.getModel().put("sLabels", sLabels);
			
			if ("y".equalsIgnoreCase(request.getParameter("p"))) {
				createPlan(request);
				saveMessage(request, getMessage("app.actionmessage.149", null));
			}
			request.setAttribute("weekDate", getClientDate(_weekDate));
			request.setAttribute("scribDay", day);
			return mav;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveMessage(request, getMessage("app.actionmessage.151", null));
			return new ModelAndView("scribView");
		}
	}
	
	private Map<String,List<Plan>> getPlansByRegion(Collection<Plan> plans) {
		
		Map<String,List<Plan>> plansByRegion=new HashMap<String, List<Plan>>();
		for (Iterator<Plan> iterator = plans.iterator(); iterator.hasNext();) {
			Plan p =  iterator.next();
			if(plansByRegion.containsKey(p.getRegion().getCode())){
				List<Plan> _plans = plansByRegion.get(p.getRegion().getCode());
				_plans.add(p);
			}else{
				List<Plan> _plans=new ArrayList<Plan>();
				_plans.add(p);
				plansByRegion.put(p.getRegion().getCode(), _plans);						
			}
		}
		return plansByRegion;
	}
	private Map<String,List<Scrib>> getScribsByRegion(Collection<Scrib> scribs) {
		
		Map<String,List<Scrib>> scribsByRegion=new HashMap<String, List<Scrib>>();
		for (Iterator<Scrib> iterator = scribs.iterator(); iterator.hasNext();) {
			Scrib s =  iterator.next();
			if(scribsByRegion.containsKey(s.getRegion().getCode())){
				List<Scrib> _scribs = scribsByRegion.get(s.getRegion().getCode());
				_scribs.add(s);
			}else{
				List<Scrib> _scribs=new ArrayList<Scrib>();
				_scribs.add(s);
				scribsByRegion.put(s.getRegion().getCode(), _scribs);						
			}
		}		
		return scribsByRegion;
	}
	private Map<String,List<ScheduleEmployeeDetails>> getScheduledEmployeesByRegion(Collection<ScheduleEmployeeDetails> scheduleEmployees) {
		
		Map<String,List<ScheduleEmployeeDetails>> scheduleEmployeesByRegion = new HashMap<String, List<ScheduleEmployeeDetails>>();
		for (Iterator<ScheduleEmployeeDetails> iterator = scheduleEmployees.iterator(); iterator.hasNext();) {
			ScheduleEmployeeDetails s =  iterator.next();
			if(scheduleEmployeesByRegion.containsKey(s.getSchedule().getRegion().getCode())){
				List<ScheduleEmployeeDetails> _scheduleEmployeeList = scheduleEmployeesByRegion.get(s.getSchedule().getRegion().getCode());
				_scheduleEmployeeList.add(s);
			}else{
				List<ScheduleEmployeeDetails> _scheduleEmployeeList=new ArrayList<ScheduleEmployeeDetails>();
				_scheduleEmployeeList.add(s);
				scheduleEmployeesByRegion.put(s.getSchedule().getRegion().getCode(), _scheduleEmployeeList);						
			}
		}		
		return scheduleEmployeesByRegion;
	}
	public void createPlan(HttpServletRequest request) throws Exception 
	{	
		
		String[] regions = StringUtil.decodeStrings(request.getParameter("dlvregion_selected"));
		String[] dates = StringUtil.decodeStrings(request.getParameter("dlvdates_selected"));
					
		Collection<EmployeeTeam> teamInfo=domainManagerService.getTeamInfo();
		Collection<Region> dlvregions = domainManagerService.getRegions();
		List<String> _regLst = new ArrayList<String>();
		
		if (dates!=null && dates.length > 0 && regions != null && regions.length > 0) {
			for (String scribDate : dates) {
				
				Map<String,List<Plan>> plansByRegion = getPlansByRegion(dispatchManagerService.getPlanList(scribDate));
				Map<String,List<Scrib>> scribsByRegion = getScribsByRegion(dispatchManagerService.getScribList(scribDate));
				Date date = TransStringUtil.serverDateFormat.parse(scribDate);
				String day = new SimpleDateFormat("EEE").format(date).toUpperCase();
				Map<String,List<ScheduleEmployeeDetails>> scheduleEmployeesByRegion = getScheduledEmployeesByRegion(employeeManagerService.getScheduledEmployees(day, scribDate));
				for (String region : regions) {
					_regLst.add(region);
					List<Plan> planList = plansByRegion.get(region);
					if(planList!=null){
						for (Iterator<Plan> i = planList.iterator(); i.hasNext();) {
							Plan p =  i.next();
							p.setUserId(SecurityManager.getUserName(request));
						}
						dispatchManagerService.removeEntity(planList);
					}					
					
					List<Scrib> scribs = scribsByRegion.get(region);				
									
					List<ScheduleEmployeeDetails> employees = scheduleEmployeesByRegion.get(region);
					
					if(employees!=null)filldate(date, employees);
					PlanTree tree = new PlanTree();
					if(scribs!=null)tree.prepare(scribs);
					if(employees!=null)tree.prepare(employees);
					tree.prepareTeam(teamInfo);
					Collection plans = tree.getPlan();
					for (Iterator i = plans.iterator(); i.hasNext();)
						getDispatchManagerService().savePlan((Plan) i.next());					
				}//end loop
				
				if("y".equalsIgnoreCase(request.getParameter("o"))){
					
						for (Iterator<Region> it = dlvregions.iterator(); it.hasNext();) {
							Region _r =  it.next();
							if(_regLst.contains(_r.getCode())){
								
							}else{
								Collection<Plan> planList = plansByRegion.get(_r.getCode());
								if(planList!=null){
									for (Iterator<Plan> i = planList.iterator(); i.hasNext();) {
										Plan p = i.next();
										p.setUserId(SecurityManager.getUserName(request));
									}
									if (planList.size() > 0)
										dispatchManagerService.removeEntity(planList);
								}
							}
						}					
				}
			}//end dates loop	
		}
	}
	
	public ModelAndView deleteScribHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException 
	{
		try {
			Set scribSet=new HashSet();
			String arrEntityList[] = getParamList(request);
			if (arrEntityList != null) {
				int arrLength = arrEntityList.length;
				for (int intCount = 0; intCount < arrLength; intCount++) 
				{
					Scrib p=dispatchManagerService.getScrib(arrEntityList[intCount]);					
					scribSet.add(p);
				}
			}
			dispatchManagerService.removeEntity(scribSet);
			saveMessage(request, getMessage("app.actionmessage.103", null));
			return scribHandler(request,response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Error in getiing Scrib List");
		}
	}
	
	
	
	
	public void filldate(Date date,Collection c)
	{
		for(Iterator it=c.iterator();it.hasNext();)
		{
			ScheduleEmployeeDetails detail=( ScheduleEmployeeDetails)it.next();
			detail.setDate(date);
		}
	}
			
	public ModelAndView scribSummaryHandler(HttpServletRequest request, HttpServletResponse response) 
														throws ServletException, ParseException   {
		
          String selectedDate = request.getParameter("selectedDate");
          String baseDate = request.getParameter("baseDate");
          
          if(TransStringUtil.isEmpty(selectedDate)) { 
        	  selectedDate = TransStringUtil.getCurrentDate();
          }
          
          Date _selectedDate = TransStringUtil.getDate(selectedDate);
          if(TransStringUtil.isEmpty(baseDate)) {
        	  baseDate = TransStringUtil.getCurrentDate();
          }
          Date _baseDate = TransStringUtil.getDate(baseDate);
          
          DeliveryServiceProxy dlvProxy = new DeliveryServiceProxy();
          Map<String, List<IDeliverySlot>> selectedWindows = null;
          Map<String, List<IDeliverySlot>> baseWindows = null;
		  
          /* try {
				selectedWindows = dlvProxy.getTimeslotsByDate(_selectedDate, null, null);
				baseWindows = dlvProxy.getTimeslotsByDate(_baseDate, null, null);
		  } catch (RoutingServiceException e) {				
				e.printStackTrace();
		  } */
                    
          Set<CustomTimeOfDay> allWindows = new TreeSet<CustomTimeOfDay>();
          Map<String, Map<CustomTimeOfDay, Integer>> selectedScribMapping = new TreeMap<String, Map<CustomTimeOfDay, Integer>>();
          Map<String, Map<CustomTimeOfDay, Integer>> baseScribMapping = new TreeMap<String, Map<CustomTimeOfDay, Integer>>();
          
          Collection<Scrib> selectedScribs = dispatchManagerService.getScribList(TransStringUtil.getServerDate(selectedDate));
          Collection<Scrib> baseScribs = dispatchManagerService.getScribList(TransStringUtil.getServerDate(baseDate));
          
          //relateTimeRange(allWindows, selectedWindows);
          //relateTimeRange(allWindows, baseWindows);
          
          relateFirstDeliveryTime(allWindows, selectedScribMapping, selectedScribs);
          relateFirstDeliveryTime(allWindows, baseScribMapping, baseScribs);
                   
          request.setAttribute("selectedDate", selectedDate);
          request.setAttribute("baseDate", baseDate);
          ModelAndView mav = new ModelAndView("scribSummaryView");
          
          mav.getModel().put("allWindows", allWindows);
          mav.getModel().put("selectedSummaryMapping", selectedScribMapping);
          mav.getModel().put("baseSummaryMapping", baseScribMapping);
          mav.getModel().put("selectedDate", selectedDate);
          mav.getModel().put("baseDate", baseDate);
          mav.getModel().put("pageId", "scribsummary");
          
          return mav;
    }
	
	/*private void relateTimeRange(Set<TimeRange> allWindows, Map<String, List<IDeliverySlot>> baseWindows) {

		if(baseWindows != null) {
			for(Map.Entry<String, List<IDeliverySlot>> slotMapping : baseWindows.entrySet()) {
				if(slotMapping.getValue() != null) {
					for(IDeliverySlot slot : slotMapping.getValue()) {
						allWindows.add(new TimeRange(slot.getStartTime(), slot.getStopTime()));
					}
				}
			}
		}
	}*/
	
	private void relateFirstDeliveryTime(Set<CustomTimeOfDay> allWindows, Map<String, Map<CustomTimeOfDay, Integer>> scribMapping
											, Collection<Scrib> scribs) {
		
		CustomTimeOfDay _timeOfDay = null;
		if(scribs != null) {        	  
      	  for(Scrib _scrib : scribs) {
      		  if(_scrib.getZone() != null) {
      			 _timeOfDay = new CustomTimeOfDay(_scrib.getFirstDeliveryTime());
      			allWindows.add(_timeOfDay);
      			if(!scribMapping.containsKey(_scrib.getZone().getZoneCode())) {
      				scribMapping.put(_scrib.getZone().getZoneCode(), new TreeMap<CustomTimeOfDay, Integer>());
      			}
      			if(!scribMapping.get(_scrib.getZone().getZoneCode()).containsKey(_timeOfDay)) {
      				scribMapping.get(_scrib.getZone().getZoneCode()).put(_timeOfDay, 0);
      			}
      			scribMapping.get(_scrib.getZone().getZoneCode()).put(_timeOfDay
      											, scribMapping.get(_scrib.getZone().getZoneCode()).get(_timeOfDay)
      																			+ _scrib.getCount());
      		  }
      	  }        	  
        }
	}
	
	public ModelAndView scribLabelHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException 
	{
		ModelAndView mav = new ModelAndView("scribLabelView");
		//get the predefined Scrib labels
		String scribLabels= TransportationAdminProperties.getScribHolidayLabels();
		String[] _scribLabels = StringUtil.decodeStrings(scribLabels);
		List sLabels=new ArrayList();
		for(String _slabel:_scribLabels){
			sLabels.add(_slabel);
		}
		mav.getModel().put("sLabels", sLabels);
		return mav;
	}
	
	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}
	public void setDispatchManagerService(DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
	}
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}
	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}
	public EmployeeManagerI getEmployeeManagerService() {
		return employeeManagerService;
	}
	public void setEmployeeManagerService(EmployeeManagerI employeeManagerService) {
		this.employeeManagerService = employeeManagerService;
	}
	
	
	
}

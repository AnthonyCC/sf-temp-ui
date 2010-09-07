package com.freshdirect.transadmin.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
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
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
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
import com.freshdirect.transadmin.web.model.TimeRange;
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
				String[] sourceDates=getDates(sourceDate, sourceDay);
				String[] destDates=getDates(destDate, sourceDay);
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
			String day=request.getParameter("scribDay");
			if(day==null)day="All";
			String[] dates=getDates(daterange,day);
			List allScribs=new ArrayList();
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
						if (scribLabel!=null)s.setScribLabel(scribLabel.getScribLabel());
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
			
			List scribDates = new ArrayList();
			for (Iterator it = allScribs.iterator(); it.hasNext();) {
				Scrib s = (Scrib) it.next();
				if (!s.getScribDate().before(TransStringUtil.getAdjustedWeekOf(Calendar.getInstance().getTime(), 0))) {
					if (!scribDates.contains(TransStringUtil.getServerDate(s.getScribDate())))
						scribDates.add(TransStringUtil.getServerDate(s.getScribDate()));
				}
			}
			mav.getModel().put("scribDates", scribDates);

			
			//get the predefined Scrib labels
			String scribLabels= TransportationAdminProperties.getScribHolidayLabels();
			String[] _scribLabels = StringUtil.decodeStrings(scribLabels);
			List sLabels=new ArrayList();
			for(String _slabel:_scribLabels){
				sLabels.add(_slabel);
			}
			mav.getModel().put("sLabels", sLabels);
			if ("y".equalsIgnoreCase(request.getParameter("l"))) {
				createPlan(request);
				saveMessage(request, getMessage("app.actionmessage.149", null));
				return mav;
			}
			return mav;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveMessage(request, getMessage("app.actionmessage.151", null));
			return new ModelAndView("scribView");
		}
	}
	
	public void createPlan(HttpServletRequest request) throws Exception 
	{		
		
		String _regions = request.getParameter("dlvregion_selected");
		String _dates = request.getParameter("dlvdates_selected");
		String[] regions = StringUtil.decodeStrings(_regions);
		String[] dates = StringUtil.decodeStrings(_dates);
		List _regLst = new ArrayList<String>();
		
		if (dates!=null && dates.length > 0 && regions != null && regions.length > 0) {
			for (String scribDate : dates) {

				for (String region : regions) {
					_regLst.add(region);
					Collection planList = dispatchManagerService
												.getPlanList(scribDate, region);
					for (Iterator i = planList.iterator(); i.hasNext();) {
						Plan p = (Plan) i.next();
						p.setUserId(SecurityManager.getUserName(request));
					}
					dispatchManagerService.removeEntity(planList);
					Date date = TransStringUtil.serverDateFormat.parse(scribDate);
					Collection scribs = dispatchManagerService.getScribList(scribDate, region);
					String day = new SimpleDateFormat("EEE").format(date).toUpperCase();
					Collection employees = employeeManagerService.getScheduledEmployees(day, scribDate);
					filldate(date, employees);

					PlanTree tree = new PlanTree();
					tree.prepare(scribs);
					tree.prepare(employees);
					tree.prepareTeam(domainManagerService.getTeamInfo());
					Collection plans = tree.getPlan();
					for (Iterator i = plans.iterator(); i.hasNext();)
						getDispatchManagerService().savePlan((Plan) i.next());					
				}//end regions loop
				if("y".equalsIgnoreCase(request.getParameter("o"))){
					Collection dlvregions = domainManagerService.getRegions();
					
						for (Iterator it = dlvregions.iterator(); it.hasNext();) {
							Region _r = (Region) it.next();
							if(_regLst.contains(_r.getCode())){
								
							}else{
								Collection planList = dispatchManagerService.getPlanList(scribDate, _r.getCode());
								for (Iterator i = planList.iterator(); i.hasNext();) {
									Plan p = (Plan) i.next();
									p.setUserId(SecurityManager.getUserName(request));
								}
								if(planList.size()>0)
									dispatchManagerService.removeEntity(planList);
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
	
	
	public String[] getDates(String date,String day) throws Exception
	{
		
		Date d=TransStringUtil.getDate(date);
		Calendar c=Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(d);
		if("All".equalsIgnoreCase(day))
		{
			String[] dates=new String[7];			
			for(int i=2;i<=8;i++)
			{
			c.set(Calendar.DAY_OF_WEEK , i);
			/*if(i==8)
			{
				c.set(Calendar.DAY_OF_WEEK , 7);
				c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR)+1);
			}*/
			String ds=TransStringUtil.getServerDate(c.getTime());
			dates[i-2]=ds;
			}
			return dates;
		}
		else
		{
			if(day==null)
			{
				return new String[]{TransStringUtil.getServerDate(c.getTime())};
			}
			else
			{
				try {
					int k=Integer.parseInt(day);
				//	if(k<8)
					{
					c.set(Calendar.DAY_OF_WEEK , k);
					}
				/*	else
					{
						c.set(Calendar.DAY_OF_WEEK , 7);
						c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR)+1);
					}*/
					String ds=TransStringUtil.getServerDate(c.getTime());
					return new String[]{ds};
				} catch (Exception e) {
					
				}
			}
		}
		return null;
		
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
      			 _timeOfDay = new CustomTimeOfDay(_scrib.getFirstDlvTime());
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

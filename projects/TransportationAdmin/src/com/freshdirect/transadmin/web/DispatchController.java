package com.freshdirect.transadmin.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
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
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.customer.ErpTruckMasterInfo;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.routing.model.GeoPoint;
import com.freshdirect.routing.model.IGeoPoint;
import com.freshdirect.routing.model.IRouteModel;
import com.freshdirect.routing.model.IRoutingSchedulerIdentity;
import com.freshdirect.routing.model.IRoutingStopModel;
import com.freshdirect.routing.model.RoutingSchedulerIdentity;
import com.freshdirect.routing.service.exception.RoutingServiceException;
import com.freshdirect.routing.service.proxy.DeliveryServiceProxy;
import com.freshdirect.routing.service.proxy.HandOffServiceProxy;
import com.freshdirect.routing.service.proxy.RoutingEngineServiceProxy;
import com.freshdirect.routing.util.RoutingServicesProperties;
import com.freshdirect.transadmin.datamanager.report.DrivingDirectionsReport;
import com.freshdirect.transadmin.datamanager.report.ReportGenerationException;
import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.EmployeeSubRoleType;
import com.freshdirect.transadmin.model.FDRouteMasterInfo;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.PlanResource;
import com.freshdirect.transadmin.model.RouteMapping;
import com.freshdirect.transadmin.model.RouteMappingId;
import com.freshdirect.transadmin.model.Scrib;
import com.freshdirect.transadmin.model.TrnRouteNumber;
import com.freshdirect.transadmin.model.TrnRouteNumberId;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.security.SecurityManager;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.EnumCachedDataType;
import com.freshdirect.transadmin.util.ModelUtil;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransStringUtil.DateFilterException;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.util.UPSDataCacheManager;
import com.freshdirect.transadmin.web.model.DispatchCommand;
import com.freshdirect.transadmin.web.model.WebDispatchStatistics;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;
import com.freshdirect.transadmin.web.model.WebPlanInfo;
import com.freshdirect.transadmin.web.util.TransWebUtil;

public class DispatchController extends AbstractMultiActionController {

	private DispatchManagerI dispatchManagerService;
	private EmployeeManagerI employeeManagerService;
	private DomainManagerI domainManagerService;
	private static DispatchComparator DISPATCH_COMPARATOR=new DispatchComparator();
	private static class DispatchComparator implements Comparator{


		public int compare(Object o1, Object o2) {

			if(o1 instanceof DispatchCommand && o2 instanceof DispatchCommand)
			{
				DispatchCommand p1=(DispatchCommand)o1;
				DispatchCommand p2=(DispatchCommand)o2;

				
				if(!p1.isDispatched() && !p2.isDispatched()) {
					return p1.getStartTimeEx().compareTo(p2.getStartTimeEx());
					//return (val==0)? p1.getRegionZone().compareTo(p2.getRegionZone()):val;
				}
				else if(p1.isDispatched() && !p2.isDispatched()) {
					return 1;
				} else if (!p1.isDispatched() && p2.isDispatched()) {
					return -1;
				} else if(p1.isDispatched() && p2.isDispatched()){
					int val= p1.getDispatchTimeEx().compareTo(p2.getDispatchTimeEx());
					return (val==0)? p1.getStartTimeEx().compareTo(p2.getStartTimeEx()):val;
				}
			}
			return 0;
		}

	}
	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}

	public void setDispatchManagerService(DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
	}
	public EmployeeManagerI getEmployeeManagerService() {
		return employeeManagerService;
	}

	public void setEmployeeManagerService(EmployeeManagerI employeeManagerService) {
		this.employeeManagerService = employeeManagerService;
	}
	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}


	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView planHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try{
	
			String daterange = request.getParameter("daterange");
			if(daterange==null||"".equals(daterange))daterange=TransStringUtil.getCurrentDate();
			String zoneLst = request.getParameter("zone");
			zoneLst = zoneLst==null?"":zoneLst;
			ModelAndView mav = new ModelAndView("planView");
		
			String day=request.getParameter("planDay");
			if(day==null)day="All";
			String[] dates=getDates(daterange,day);			
			Collection dataList= new ArrayList();
			List<Plan> plans=new ArrayList();
			if((!TransStringUtil.isEmpty(daterange)&& !TransStringUtil.isEmpty(day)) || !TransStringUtil.isEmpty(zoneLst)) {
	
				try 
				{						
					//String date = TransStringUtil.formatDateSearch(daterange);
					if(dates!=null)
					{	
						for(int i=0;i<dates.length;i++)
						{
							String dateQryStr = TransStringUtil.formatDateSearch(dates[i]);
							String zoneQryStr = StringUtil.formQueryString(Arrays.asList(StringUtil.decodeStrings(zoneLst)));
							Collection tempList= new ArrayList();
							Collection tempPlans= new ArrayList();
							if(dateQryStr != null || zoneQryStr != null) {
								tempList = getPlanInfo(dateQryStr,zoneQryStr);
								if("y".equalsIgnoreCase(request.getParameter("unavailable")))
								{
									tempPlans=dispatchManagerService.getPlan(dateQryStr, zoneQryStr);
									tempPlans=employeeManagerService.getUnAvailableEmployees(tempPlans, TransStringUtil.getServerDate(dates[i]));
								}
								if("y".equalsIgnoreCase(request.getParameter("kronos")))
								{									
									tempPlans=dispatchManagerService.getPlan(dateQryStr, zoneQryStr);									
								}							
							}
							plans.addAll(tempPlans);
							dataList.addAll(tempList);
						}//end for loop
						
						if("y".equalsIgnoreCase(request.getParameter("unavailable")))
						{							
							mav = new ModelAndView("unavailableView");
							mav.getModel().put("unavailable",plans);
							return mav;
						}
						if("y".equalsIgnoreCase(request.getParameter("kronos")))
						{
							try {
								String file=request.getParameter("file");
								Collections.sort(plans, new PlanDateComparator());
								updateKronos(plans,dates,file,request,response);
								return null;
								//saveMessage(request, getMessage("app.actionmessage.146", null));
							} catch (Exception e) {
								e.printStackTrace();
								saveMessage(request, getMessage("app.actionmessage.145", null));
							}
						}
						mav.getModel().put("planlist",dataList);
					}					
				} catch (Exception e) {
					e.printStackTrace();
					saveMessage(request, getMessage("app.actionmessage.123", null));
				}				
			}
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			saveMessage(request, getMessage("app.actionmessage.158", null));
			return new ModelAndView("planView");	
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
				String ds=TransStringUtil.getServerDate1(c.getTime());
				dates[i-2]=ds;
			}
			return dates;
		}
		else
		{
			if(day==null)
			{
				return new String[]{TransStringUtil.getServerDate1(c.getTime())};
			}
			else
			{
				try {
					int k=Integer.parseInt(day);
					{
					c.set(Calendar.DAY_OF_WEEK , k);
					}
					String ds=TransStringUtil.getServerDate1(c.getTime());
					return new String[]{ds};
				} catch (Exception e) {
					
				}
			}
		}
		return null;		
	}
	
	public static class PlanDateComparator implements Comparator<Plan> {

		@Override
		public int compare(Plan o1, Plan o2) {
			return(o1.getPlanDate().compareTo(o2.getPlanDate()));
		}
	}

	public static class PlanStartDateComparator implements Comparator<Plan> {

		@Override
		public int compare(Plan o1, Plan o2) {
			return(o1.getStartTime().compareTo(o2.getStartTime()));
		}
	}
	public static class PlanFirstDeliveryTimeComparator implements Comparator<Plan> {

		@Override
		public int compare(Plan o1, Plan o2) {
			return(o1.getFirstDeliveryTime().compareTo(o2.getFirstDeliveryTime()));
		}
	}
	public static class PlanMaxTimeComparator implements Comparator<Plan> {

		@Override
		public int compare(Plan o1, Plan o2) {
			return(o1.getMaxTime().compareTo(o2.getMaxTime()));
		}
	}	
	
	public void updateKronos(List<Plan> plans,String[] dates,String file,HttpServletRequest request, HttpServletResponse response) throws Exception
	{			
		
		Map<String, Scrib> scribMap = new HashMap<String, Scrib>();
		Map<String, Map<String, Scrib>> resMapScrib = new HashMap<String, Map<String, Scrib>>();
		Map<String, Map<String, Map<String, Scrib>>> kronos = new HashMap<String, Map<String, Map<String, Scrib>>>();	
				
		for(Iterator<Plan> i=plans.iterator();i.hasNext();)
		{
			Plan p = i.next();
			String planDate=TransStringUtil.getDate(p.getPlanDate());
			if(kronos.containsKey(planDate)) {
				resMapScrib=kronos.get(planDate);
			} else {
				resMapScrib = new HashMap<String, Map<String, Scrib>>();
			}
			Set<PlanResource> resources = p.getPlanResources();
			if(resources!=null) {
				for(Iterator<PlanResource> j=resources.iterator();j.hasNext();)
				{
					PlanResource r = j.next();
					boolean addEntry=false;
						if(!resMapScrib.containsKey(r.getId().getResourceId())) {
							scribMap = new HashMap<String, Scrib>();
							addEntry=true;
						} else {
							scribMap=resMapScrib.get(r.getId().getResourceId());
						}
						if(DispatchPlanUtil.isEligibleForKronosFileGeneration(domainManagerService.getEmployeeRole(r.getId().getResourceId())))
						{			
							Scrib s = new Scrib();
							//Set Scrib id for a resource
							s.setScribId(r.getId().getResourceId());
							
							//Set scheduled emp date
							getKronosEmployeeDate(p, r, s);
							
							s.setShiftType(getShiftForPlan(p));
							//set Employee Time	
							setKronosEmployeeTime(p, r, s);							
							//set shift duration
							double maxTime = getKronosEmployeeShiftDuration(p, r, s);	
							s.setShiftDuration(maxTime);
							
							scribMap.put(s.getShiftType(),s);
							if(addEntry) {
								resMapScrib.put(r.getId().getResourceId(), scribMap);
							}
						}
						
				}			
			}
			kronos.put(TransStringUtil.getDate(p.getPlanDate()),resMapScrib);
		}	
		generateKronosFiles(response, kronos);
						
	}	

	private void setKronosEmployeeTime(Plan p, PlanResource r, Scrib s)
			throws ParseException {
		if(r.getId().getAdjustmentTime()!=null)
			s.setStartTime(r.getId().getAdjustmentTime());
		else
		{
			List<Plan> resPlans = getPlansForDate(p, r, s);
			Collections.sort(resPlans,new PlanFirstDeliveryTimeComparator());
			if("003".equalsIgnoreCase(r.getEmployeeRoleType().getCode()))
			{
				s.setStartTime(new Date(resPlans.get(0).getFirstDeliveryTime().getTime()-30*60*1000));
			}
			else {
				
				s.setStartTime(resPlans.get(0).getStartTime());
			}
		}
	}

	private void getKronosEmployeeDate(Plan p, PlanResource r, Scrib s)	throws ParseException {
		
		boolean isYardWorker = false;
		for(Iterator subType=r.getEmployeeRoleType().getSubRoles().iterator();subType.hasNext();){
			EmployeeSubRoleType subRole=(EmployeeSubRoleType)subType.next();
			if("007".equalsIgnoreCase(subRole.getCode())){
				isYardWorker=true;
				break;
			}								
		}
		boolean isGreater=false;
		if(isYardWorker){
			if(r.getId().getAdjustmentTime()!=null)
				isGreater = TransStringUtil.checkHourOfDate(r.getId().getAdjustmentTime());
			else
				isGreater = TransStringUtil.checkHourOfDate(p.getStartTime());								
			if(isGreater){									
				s.setScribDate(TransStringUtil.getAdjustedDayOf(p.getPlanDate(),-1));
			} else s.setScribDate(p.getPlanDate());
		}else
			s.setScribDate(p.getPlanDate());
	}

	private double getKronosEmployeeShiftDuration(Plan p, PlanResource r, Scrib s) throws ParseException {
		double maxTime=0;
		if("Depot".equalsIgnoreCase(p.getRegion().getCode()))
		{
			Collection depotPlans = dispatchManagerService.getPlanList(TransStringUtil.getServerDate(p.getPlanDate()),p.getRegion().getCode());
			List<Plan> resPlans=new ArrayList<Plan>();
			for (Iterator<Plan> iterator = depotPlans.iterator(); iterator.hasNext();) {
				Plan _depotPlan = iterator.next();
					String shiftType = getShiftForPlan(_depotPlan);
					if(shiftType.equalsIgnoreCase(s.getShiftType())){
						Set _depotPlanRsr = _depotPlan.getPlanResources();
						for (Iterator<PlanResource> itr = _depotPlanRsr.iterator(); itr.hasNext();) {
							PlanResource _pr = itr.next();
							if(r.getId().getResourceId().equals(_pr.getId().getResourceId())){
								resPlans.add(_depotPlan);
							}
						}
					}
			}			
			for (Iterator<Plan>it =resPlans.iterator();it.hasNext();) {
				Plan _p = it.next();
				maxTime = maxTime + Double.parseDouble(TransStringUtil.formatTime1(_p.getMaxTime()));
			}
		}else{
			maxTime = Double.parseDouble(TransStringUtil.formatTime1(p.getMaxTime()));
		}
		return maxTime;
	}	

	private String getShiftForPlan(Plan p) throws ParseException {		
		int dayOfweek = TransStringUtil.getClientDayofWeek(TransStringUtil.getDatewithTime(p.getPlanDate()));
		double hourOfDay = Double.parseDouble(TransStringUtil.formatTimeFromDate(p.getFirstDeliveryTime()));
		if (hourOfDay < 12 && dayOfweek != 7) {
			return "AM";
		} else if (hourOfDay < 10 && dayOfweek == 7) {
			return "AM";
		} else
			return "PM";		
	}

	private List<Plan> getPlansForDate(Plan p, PlanResource r,Scrib s) throws ParseException {
		
		Collection depotPlans = dispatchManagerService.getPlanList(TransStringUtil.getServerDate(p.getPlanDate()));
		List<Plan> resPlans=new ArrayList<Plan>();
		for (Iterator<Plan> iterator = depotPlans.iterator(); iterator.hasNext();) {
			Plan _rPlan = iterator.next();
			String shiftType=getShiftForPlan(_rPlan);
			if(shiftType.equalsIgnoreCase(s.getShiftType())){
				Set _rPlanRsr = _rPlan.getPlanResources();
				for (Iterator<PlanResource> itr = _rPlanRsr.iterator(); itr.hasNext();) {
					PlanResource _pr = itr.next();
					if(r.getId().getResourceId().equals(_pr.getId().getResourceId())){
						resPlans.add(_rPlan);
					}
				}
			}
		}
		return resPlans;
	}

	private void generateKronosFiles(HttpServletResponse response, Map<String, Map<String, Map<String, Scrib>>> kronos)
			throws IOException, ParseException {
		
		response.setContentType("application/x-zip-compressed");
		response.setHeader("Content-Disposition", "attachment; filename=Upload_All.zip"); 
		
		OutputStream out=response.getOutputStream();
		ByteArrayOutputStream  f1 = new ByteArrayOutputStream();
		ByteArrayOutputStream  f2 = new ByteArrayOutputStream();
		ByteArrayOutputStream  f3 = new ByteArrayOutputStream();		
		
		String header1 = "Kronos ID , Date , Time , Shift , \n"; f1.write(header1.getBytes());
		String header2 = "Kronos ID , Date , \n";f2.write(header2.getBytes());
		String header3 = "Kronos ID \t Date \t Time \t Shift \t \n";f3.write(header3.getBytes());
		
		for(Iterator<String> dates = kronos.keySet().iterator();dates.hasNext();) {
			String date=dates.next();
			
			Map<String, Map<String, Scrib>> resMap=kronos.get(date);
			for(Iterator<String> resIds=kronos.get(date).keySet().iterator();resIds.hasNext(); ) {
				String resId=resIds.next();
				
				Map<String, Scrib> shiftMap=resMap.get(resId);
				for(Iterator<String> shifts=shiftMap.keySet().iterator();shifts.hasNext();) {
					String shift = shifts.next();
					Scrib scribEntry = shiftMap.get(shift);
					String line1 = scribEntry.getScribId() + ","
									+ TransStringUtil.getDate(scribEntry.getScribDate()) + ","
									+ TransStringUtil.getServerTime(scribEntry.getStartTime()) + ","				
									+ scribEntry.getShiftDuration() + "\n";
					String line2 = scribEntry.getScribId() + ","+ TransStringUtil.getDate(scribEntry.getScribDate()) + "\n";
					String line3 = scribEntry.getScribId() + "\t"
									+ TransStringUtil.getDate(scribEntry.getScribDate()) + "\t"
									+ TransStringUtil.getServerTime(scribEntry.getStartTime()) + "\t"				
									+ scribEntry.getShiftDuration() + "\n";
				
					f1.write(line1.getBytes());
					f2.write(line2.getBytes());
					f3.write(line3.getBytes());
				}
				f1.flush();
				f2.flush();
				f3.flush();
			}
			
			String line3 = "\n";
			f3.write(line3.getBytes());
			
			f1.flush();
			f2.flush();
			f3.flush();
		}
	
	
		ZipOutputStream zipout = new ZipOutputStream(out);
					 
		String[] filenames = new String[]{TransportationAdminProperties.getKronosUploadAllFileName(), TransportationAdminProperties.getKronosUploadAllEmptyFileName(), TransportationAdminProperties.getKronosUploadIndividualFileName()};
		byte[] buf = new byte[1024];
		
		for (int i = 0; i < filenames.length; i++) {
			InputStream in;
			if (i == 0) {
				in = new ByteArrayInputStream(f1.toByteArray());
			} else if (i == 1) {
				in = new ByteArrayInputStream(f2.toByteArray());
			} else
				in = new ByteArrayInputStream(f3.toByteArray());
			// Add ZIP entry to output stream.
			zipout.putNextEntry(new ZipEntry(filenames[i]));

			// Transfer bytes from the file to the ZIP file
			int len;
			while ((len = in.read(buf)) > 0) {
				zipout.write(buf, 0, len);
			}

			// Complete the entry
			zipout.closeEntry();
			in.close();
		}
		f1.close();
		f2.close();
		f3.close();
		zipout.flush();
		out.flush();
		zipout.close();
	}	
				
	private Collection getPlanInfo(String dateQryStr, String zoneQryStr) {

		Collection plans=dispatchManagerService.getPlan(dateQryStr, zoneQryStr);
		List termintedEmployees = getTermintedEmployeeIds();
		
		Collection planInfos=new ArrayList();
		Iterator it=plans.iterator();
		while(it.hasNext()) {

			Plan plan=(Plan)it.next();
			Zone zone=null;
			if(plan.getZone()!=null) {
				zone=domainManagerService.getZone(plan.getZone().getZoneCode());
			}
			WebPlanInfo planInfo=DispatchPlanUtil.getWebPlanInfo(plan, zone, employeeManagerService);
			planInfo.setTermintedEmployees(termintedEmployees);
			planInfos.add(planInfo);
		}


		return planInfos;
	}



	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView planDeleteHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		Set employeeSet=new HashSet();
		String arrEntityList[] = getParamList(request);
		if (arrEntityList != null) {
			int arrLength = arrEntityList.length;
			for (int intCount = 0; intCount < arrLength; intCount++) 
			{
				Plan p=dispatchManagerService.getPlan(arrEntityList[intCount]);
				p.setUserId(SecurityManager.getUserName(request));
				employeeSet.add(p);
			}
		}
		dispatchManagerService.removeEntity(employeeSet);
		saveMessage(request, getMessage("app.actionmessage.103", null));

		return planHandler(request, response);
	}

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView dispatchHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		String dispDate = request.getParameter("dispDate");
		String zone = request.getParameter("zone");
		String region = request.getParameter("region");
		ModelAndView mav = new ModelAndView("dispatchView");
		if(!TransStringUtil.isEmpty(dispDate)) {			
			//boolean punchInfo=getServerDate(dispDate).equals(TransStringUtil.getCurrentServerDate())?true:false;			
			Collection c=getDispatchInfos(getServerDate(dispDate), zone, region, false,TransWebUtil.isPunch(request, dispatchManagerService),TransWebUtil.isAirClick(request, dispatchManagerService));
			DispatchPlanUtil.setDispatchStatus(c,false);
			mav.getModel().put("dispatchInfos",c );
			mav.getModel().put("dispDate", dispDate);
		} else {
			//By default get the today's dispatches.
			Collection c=getDispatchInfos(getServerDate(TransStringUtil.getDispatchCurrentDate()), zone, region, false,TransWebUtil.isPunch(request, dispatchManagerService),TransWebUtil.isAirClick(request, dispatchManagerService));
			DispatchPlanUtil.setDispatchStatus(c,false);
			mav.getModel().put("dispatchInfos",c);
			mav.getModel().put("dispDate", TransStringUtil.getCurrentDate());
		}
		mav.getModel().put("zones", domainManagerService.getZones());
		mav.getModel().put("regions", domainManagerService.getRegions());
		return mav;
	}

	public ModelAndView dispatchSummaryHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		ModelAndView mav = new ModelAndView("dispatchSummaryView");
		Collection c=getDispatchInfos(getServerDate(TransStringUtil.getDispatchCurrentDate()), null, null, true,TransWebUtil.isPunch(request, dispatchManagerService),TransWebUtil.isAirClick(request, dispatchManagerService));
		//By default get the today's dispatches.
		DispatchPlanUtil.setDispatchStatus(c,false);
		mav.getModel().put("dispatchInfos",c);
		mav.getModel().put("dispDate", TransStringUtil.getCurrentDate());
		dispatchStatisticsHandler(request,response);
		return mav;
	}
	
	public ModelAndView dispatchStatisticsHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException 
	{
		
		try {
			Collection c1=getDispatchInfos(getServerDate(TransStringUtil.getDispatchCurrentDate()), null, null, true,TransWebUtil.isPunch(request, dispatchManagerService),TransWebUtil.isAirClick(request, dispatchManagerService));
			Collection c2=this.getDispatchManagerService().getUnassignedActiveEmployees();
			String date=TransStringUtil.formatDateSearch(TransStringUtil.getCurrentDate());
			Collection c3=dispatchManagerService.getPlan(date, null);
			Collection c4=employeeManagerService.getUnAvailableEmployees(c3, TransStringUtil.getCurrentServerDate());
			
			WebDispatchStatistics s=new WebDispatchStatistics();
			s.calculateDispatchRoute(c1);
			s.calculateUnassigned(c2);
			s.calculatePlanRoute(c3);
			s.calculatePaycode(c4);
			request.setAttribute("statistics", s);
		} catch (DateFilterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ModelAndView dispatchDashboardViewHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		int mode=0;
		String view="dispatchDashboardNewView";
		try {
			mode=Integer.parseInt(request.getParameter("mode"));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(mode==3) view="dispatchDashboardNewNRView";
			
		return processDashboardViewRequest(request, response, view,mode);
	}
	private ModelAndView processDashboardViewRequest(HttpServletRequest request, HttpServletResponse response, String view,int mode) {
		
		try {
			String dispDate = request.getParameter("dispDate");			
			if(TransStringUtil.isEmpty(dispDate)) {
				dispDate=TransStringUtil.getDispatchCurrentDate();
			}
			try {
				request.setAttribute("lastTime", TransStringUtil.getServerTime(new Date()));
			} catch (ParseException e1) {}
			//By default get the today's dispatches.
			Collection c=getDispatchInfos(getServerDate(dispDate), null, null, true,TransWebUtil.isPunch(request, dispatchManagerService),TransWebUtil.isAirClick(request, dispatchManagerService));
			if(mode==3)
			DispatchPlanUtil.setDispatchStatus(c,false);
			else DispatchPlanUtil.setDispatchStatus(c,true);
				
			Collection upsRouteInfos=UPSDataCacheManager.getInstance().getData(domainManagerService);
			if(upsRouteInfos!=null&&upsRouteInfos.size()>0)
			{
				Iterator iterator=c.iterator();			
				while(iterator.hasNext())	
				{
					DispatchCommand command = (DispatchCommand)iterator.next();
					command.setUPSRouteInfo(upsRouteInfos);
				}
			}
			ModelAndView mav=new ModelAndView(view);
			mav.getModel().put("dispatchInfos",DispatchPlanUtil.getsortedDispatchView(c,mode));
			mav.getModel().put("dispDate", dispDate);
			return mav;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ModelAndView("dispatchDashboardView");
	}
	public ModelAndView dispatchDashboardHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		return processDashboardRequest(request, response, "dispatchDashboardViewFull");
	}
	
	public ModelAndView dispatchDashboardScreenHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		return processDashboardRequest(request, response, "dispatchDashboardView");
		
	}	
	
	private ModelAndView processDashboardRequest(HttpServletRequest request, HttpServletResponse response, String view) {
		
		try {
			String dispDate = request.getParameter("dispDate");			
			if(TransStringUtil.isEmpty(dispDate)) {
				dispDate=TransStringUtil.getDispatchCurrentDate();
			}
			try {
				request.setAttribute("lastTime", TransStringUtil.getServerTime(new Date()));
			} catch (ParseException e1) {}
			//By default get the today's dispatches.
			Collection c=getDispatchInfos(getServerDate(dispDate), null, null, true,TransWebUtil.isPunch(request, dispatchManagerService),TransWebUtil.isAirClick(request, dispatchManagerService));
			DispatchPlanUtil.setDispatchStatus(c,true);
						
			
			ModelAndView mav=new ModelAndView(view);
			mav.getModel().put("dispatchInfos",DispatchPlanUtil.getsortedDispatch(c));
			mav.getModel().put("dispDate", dispDate);
			return mav;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ModelAndView("dispatchDashboardView");
	}

	private Collection getDispatchInfos(String dispDate, String zoneStr, String region, boolean isSummary, boolean needsPunchInfo,boolean needsAirClick){
		Collection dispatchInfos = new ArrayList();
		List termintedEmployees = getTermintedEmployeeIds();
		try {
		Collection dispatchList = dispatchManagerService.getDispatchList(dispDate, zoneStr, region);
		if(dispatchList!=null) Collections.sort((List)dispatchList,new DispatchPlanUtil.DispatchPunchTimeComparator());
		Collection punchInfo=null;
		Map htInData=null;
		Map htOutData=null;
		domainManagerService.refreshCachedData(EnumCachedDataType.TRUCK_DATA);
		//needsPunchInfo=false;
		if(needsPunchInfo && dispatchList!=null && !dispatchList.isEmpty())
			punchInfo=employeeManagerService.getPunchInfo(dispDate);
			Date dispDateTemp=TransStringUtil.serverDateFormat.parse(dispDate);
			if(needsAirClick)
			{
				htInData=dispatchManagerService.getHTInScan(dispDateTemp);
				htOutData=dispatchManagerService.getHTOutScan(dispDateTemp);
			}
		Iterator iter = dispatchList.iterator();
			while(iter.hasNext()){
				Dispatch dispatch = (Dispatch) iter.next();
				Zone zone=null;
				if(dispatch.getZone() != null) {
					zone=domainManagerService.getZone(dispatch.getZone().getZoneCode());
				}
				DispatchCommand command = DispatchPlanUtil.getDispatchCommand(dispatch, zone, employeeManagerService,punchInfo,htInData,htOutData);
				command.setTermintedEmployees(termintedEmployees);
				if(isSummary){
					FDRouteMasterInfo routeInfo = domainManagerService.getRouteMasterInfo(command.getRoute(), new Date());
					if(routeInfo != null){
						command.setNoOfStops(routeInfo.getNumberOfStops());
					}

				}
				ErpTruckMasterInfo truckInfo=domainManagerService.getERPTruck(command.getTruck());
				if(truckInfo!=null)
				{
					
					command.setLocation(truckInfo.getLocation());
				}
				dispatchInfos.add(command);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			throw new RuntimeException("Exception ocuurred while processing the dispatch list for requested date "+dispDate);
		}

		Collections.sort((List)dispatchInfos, DISPATCH_COMPARATOR);
		return dispatchInfos;
	}

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView dispatchDeleteHandler(HttpServletRequest request, HttpServletResponse response)
								throws ServletException, ParseException {

		Set dispatchSet=new HashSet();
		String arrEntityList[] = getParamList(request);
		//StringTokenizer splitter = null;
		if (arrEntityList != null) {
			int arrLength = arrEntityList.length;
			for (int intCount = 0; intCount < arrLength; intCount++) {
				//splitter = new StringTokenizer(arrEntityList[intCount], "$");
				Dispatch dispatch =dispatchManagerService.getDispatch(arrEntityList[intCount]);
				if(dispatch.getConfirmed()== null || dispatch.getConfirmed() == Boolean.FALSE)
				{
					dispatch.setUserId(SecurityManager.getUserName(request));
					dispatchSet.add(dispatch);					
				}
			}
			if(dispatchSet.size() == arrLength) {
				dispatchManagerService.removeEntity(dispatchSet);
				saveMessage(request, getMessage("app.actionmessage.103", null));
			} else {
				saveMessage(request, getMessage("app.actionmessage.136", null));
			}
		}
		return dispatchHandler(request, response);
	}

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	
	public ModelAndView dispatchConfirmHandler(HttpServletRequest request, HttpServletResponse response)
								throws ServletException, ParseException {

		Set dispatchSet=new HashSet();
		String arrEntityList[] = getParamList(request);
		StringTokenizer splitter = null;
		Dispatch tmpDispatch = null;
		int routeBlankCount = 0;
		
		Map ids=getCheckedInIds(request);
		Iterator keys=ids.keySet().iterator();
		while(keys.hasNext())
		{
			String key=(String)keys.next();
			tmpDispatch =dispatchManagerService.getDispatch(key);
			dispatchManagerService.evictDispatch(tmpDispatch);
			tmpDispatch.setUserId(SecurityManager.getUserName(request));
			List list=(List)ids.get(key);
			for(int i=0,n=list.size();i<n;i++)
			{
				String status=(String)list.get(i);
				if("confirmed".equalsIgnoreCase(status)&&!TransStringUtil.isEmpty(tmpDispatch.getRoute()))
				{
					Boolean confirm = tmpDispatch.getConfirmed();
					if( confirm == null || ! confirm.booleanValue() ) 
					{
						tmpDispatch.setConfirmed(Boolean.TRUE);
					} else {
						tmpDispatch.setConfirmed(Boolean.FALSE);
					}
					
				}
				if("phoneAssigned".equalsIgnoreCase(status))
				{
					tmpDispatch.setPhonesAssigned(Boolean.TRUE);
				}
				if("keysReady".equalsIgnoreCase(status))
				{
					tmpDispatch.setKeysReady(Boolean.TRUE);
				}
				if("dispatched".equalsIgnoreCase(status))
				{
					tmpDispatch.setDispatchTime(TransStringUtil.getServerTime(TransStringUtil.getServerTime(new Date())));
					tmpDispatch.setConfirmed(Boolean.TRUE);
				}
				if("checkedIn".equalsIgnoreCase(status))
				{
					tmpDispatch.setCheckedInTime(TransStringUtil.getServerTime(TransStringUtil.getServerTime(new Date())));
				}
				
				dispatchSet.add(tmpDispatch);
			}
		}
		dispatchManagerService.saveEntityList(dispatchSet);
		saveMessage(request, getMessage("app.actionmessage.104", null));
		
//		if (arrEntityList != null) {
//			int arrLength = arrEntityList.length;
//			for (int intCount = 0; intCount < arrLength; intCount++) {
//				//splitter = new StringTokenizer(arrEntityList[intCount], "$");
//				tmpDispatch =dispatchManagerService.getDispatch(arrEntityList[intCount]);
//				if(TransStringUtil.isEmpty(tmpDispatch.getRoute())){
//					routeBlankCount++;
//					break;
//				}
//				if(tmpDispatch != null) {
//					Boolean confirm = tmpDispatch.getConfirmed();
//					if( confirm == null || ! confirm.booleanValue() ) {
//						tmpDispatch.setConfirmed(Boolean.TRUE);
//					} else {
//						tmpDispatch.setConfirmed(Boolean.FALSE);
//					}
//					dispatchSet.add(tmpDispatch);
//				}
//			}
//			if(routeBlankCount > 0){
//				saveMessage(request, getMessage("app.actionmessage.138", null));
//			}else {
//				dispatchManagerService.saveEntityList(dispatchSet);
//				saveMessage(request, getMessage("app.actionmessage.104", null));
//			}
//		}

		return dispatchHandler(request, response);
	}
	public Map getCheckedInIds(HttpServletRequest request)
	{
		Map map=new HashMap();
		String ids=request.getParameter("id");
		StringTokenizer token=new StringTokenizer(ids,",");
		while(token.hasMoreTokens())
		{
			String tokenStr=token.nextToken();
			if(tokenStr!=null)
			{
				String key;
				if(tokenStr.indexOf("_")==-1)
				{
					key=tokenStr;
				}
				else
				{
					key=tokenStr.substring(0,tokenStr.indexOf("_"));
				}
				List list=(List)map.get(key);
				if(list==null){ list=new ArrayList(); map.put(key,list);}
				if(tokenStr.indexOf("_")!=-1)
				{
					String value=tokenStr.substring(tokenStr.indexOf("_")+1);
					list.add(value);
				}
				else
				{
					list.add("confirmed");
				}
			}
		}
		return map;
	}

	public ModelAndView routeRefreshHandler(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, ParseException {
		String dispDate = request.getParameter("dispDate");
		String isSummary = request.getParameter("summary");
		boolean changed = false;
		if(!TransStringUtil.isEmpty(dispDate)) {
			changed = dispatchManagerService.refreshRoute(TransStringUtil.getDate(dispDate));
		} else {
			//By default get the today's dispatches.
			changed = dispatchManagerService.refreshRoute(new Date());
		}
		if(changed){
			saveMessage(request, getMessage("app.actionmessage.134", null));
		}
		if(TransStringUtil.isEmpty(isSummary)){
			return dispatchHandler(request, response);
		}else{
			//System.out.println("Inside Summary");
			return dispatchSummaryHandler(request, response);
		}

	}

	public ModelAndView autoDispatchHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, ParseException {

		// if there is a dispatch record for the same date then check what user role
		// if not admin send an error message
		// if admin delete the existing record and run the autodispatch crap
		try {

			String daterange = request.getParameter("daterange");
			if (daterange == null || "".equals(daterange)|| TransStringUtil.isEmpty(daterange))
				daterange = TransStringUtil.getCurrentDate();
			String day = request.getParameter("planDay");
			if (day == null)
				day = "All";
			String[] dates = getDates(daterange, day);
			List _noplans = new ArrayList();
			List _unavailableEmp = new ArrayList();
				
			if(dates!=null){
					for(int i=0;i<dates.length;i++){
						String dispatchDate = TransStringUtil.getServerDate(dates[i]);
						Collection planList = dispatchManagerService.getPlanList(dispatchDate);
	
						if (planList == null || planList.size() == 0) {
							_noplans.add(dispatchDate);
						}
						if (TransportationAdminProperties.isAutoDispatchValidation()) {
							Collection unavailable = employeeManagerService
									.getUnAvailableEmployees(planList, dispatchDate);
							if (unavailable != null && unavailable.size() > 0) {
								_unavailableEmp.add(dispatchDate);																
							}
						}
						Collection dispList = dispatchManagerService.getDispatchList(dispatchDate, null, null);
						if (!SecurityManager.isUserAdmin(request)) {
							saveMessage(request, getMessage("app.actionmessage.140", null));
							return planHandler(request, response);
						}
						dispatchManagerService.autoDisptchRegion(dispatchDate);
										
					}
					if(_unavailableEmp.size() > 0)
						saveMessage(request, getMessage("app.actionmessage.147", new List[]{_unavailableEmp}));
					if(_noplans.size()> 0)
						saveMessage(request, getMessage("app.actionmessage.142", new List[]{_noplans}));
					saveMessage(request, getMessage("app.actionmessage.143",null));	
			}
			return planHandler(request, response);
		} catch (ParseException ep) {
			ep.printStackTrace();
			saveMessage(request, getMessage("app.error.115", new String[]{"Invalid Date"}));
			return planHandler(request,response);
		} catch (Exception e) {
			e.printStackTrace();
			saveMessage(request, getMessage("app.actionmessage.157", null));
			return planHandler(request,response);	
		}

	}

	public ModelAndView unassignedRouteHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException  {

		ModelAndView mav = new ModelAndView("unassignedRouteView");
		String routeDate = request.getParameter("routeDate");
		try {
			mav.getModel().put("routes", dispatchManagerService.getUnusedDispatchRoutes(TransStringUtil.getServerDate(routeDate)));
		}  catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mav.getModel().put("routes",dispatchManagerService.getUnusedDispatchRoutes(TransStringUtil.getCurrentServerDate()));
		}
		return mav;
	}
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView routeNumberHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		String routeDate = request.getParameter("routeDate");
		ModelAndView mav = new ModelAndView("routeNumberView");
		mav.getModel().put("routeDate", routeDate);
		List dataList = new ArrayList();
		
		if(!TransStringUtil.isEmpty(routeDate)) {
			Map routeInfoGrp = this.getDispatchManagerService().getRouteNumberGroup(getServerDate(routeDate), null, null);
			Iterator _iterator = routeInfoGrp.keySet().iterator();
			
			while(_iterator.hasNext()) {
				
				RouteMappingId key = (RouteMappingId)_iterator.next();
				TrnRouteNumber _routeNo = new TrnRouteNumber();
				_routeNo.setRouteNumberId(new TrnRouteNumberId(key.getRouteDate(), key.getCutOffId(), key.getGroupCode()));
				_routeNo.setCurrentVal(new BigDecimal(routeInfoGrp.get(key).toString()));
				dataList.add(_routeNo);
			}
			mav.getModel().put("routenumberlist",dataList);
		}

		return mav;
	}
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView drivingDirectionsHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		List routingRouteIds = Arrays.asList(StringUtil.decodeStrings(request.getParameter("routeId")));
		String routeDate = request.getParameter("rdate");
		
		try {
			if(routingRouteIds != null) {
				
				DeliveryServiceProxy proxy = new DeliveryServiceProxy();
				RoutingEngineServiceProxy engineProxy = new RoutingEngineServiceProxy();
				HandOffServiceProxy handOffProxy = new HandOffServiceProxy();
				
				Iterator _itr = routingRouteIds.iterator();
				String routingRouteId = null;
				Map directionRoutes = new TreeMap();
				IRouteModel _tmpRoute = null;
				
				while(_itr.hasNext()) {
					routingRouteId = (String)_itr.next();
					_tmpRoute = handOffProxy.getHandOffBatchStopsByRoute(TransStringUtil.getDate(routeDate), routingRouteId, true);
					if(_tmpRoute != null) {
						directionRoutes.put(routingRouteId, _tmpRoute);
					} else {
						Collection routes = domainManagerService.getRouteMapping(TransStringUtil.getServerDate(routeDate), routingRouteId);
						boolean hasRoutes = false;
						
						if(routes != null && routes.size() == 1) {
							
							RouteMapping routeMapping = (RouteMapping)routes.toArray()[0];	
													
							IRoutingSchedulerIdentity schedulerId = new RoutingSchedulerIdentity();
							schedulerId.setRegionId(RoutingServicesProperties.getDefaultTruckRegion());
									
							String sessionId = engineProxy.retrieveRoutingSession(schedulerId, routeMapping.getRoutingSessionID());
							
							List routingRoutes = proxy.getRoutes(TransStringUtil.getDate(routeDate), sessionId
																	, routeMapping.getRouteMappingId().getRoutingRouteID());
							
							if(routingRoutes != null && routingRoutes.size() > 0) {
								_tmpRoute = (IRouteModel)routingRoutes.get(0);
								
								if(_tmpRoute.getStops() != null && _tmpRoute.getStops().size() > 0) {
									directionRoutes.put(routingRouteId, _tmpRoute);
									hasRoutes = true;
								} 
							}
						}
						if(!hasRoutes) {
							directionRoutes.put(routingRouteId, null);
						}
					}
				}
				DrivingDirectionsReport reportEngine = new DrivingDirectionsReport();
				if(directionRoutes != null && directionRoutes.size() > 0) {
					Map directionsReportData = this.getRouteDirections(directionRoutes);
					reportEngine.generateDrivingDirectionsReport(response.getOutputStream(), directionsReportData);
					
				} else {
					reportEngine.generateError(response.getOutputStream(), "There are no valid routes to build Driving Direction. Please select a valid route or contact routing team for more information.");
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			saveMessage(request, getMessage("app.actionmessage.123", null));
		}
		response.setContentType("application/pdf");
		return null;
	}
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView gpsDrivingDirectionsHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		List routingRouteIds = Arrays.asList(StringUtil.decodeStrings(request.getParameter("routeId")));
		String routeDate = request.getParameter("rdate");
		ModelAndView mav = new ModelAndView("gpsAdminView");
		mav.getModel().put("gpsgpxxml","------ NO DIRECTIONS AVAILABLE --------");
		try {
			if(routingRouteIds != null && routingRouteIds.size() > 0) {

				DeliveryServiceProxy proxy = new DeliveryServiceProxy();
				RoutingEngineServiceProxy engineProxy = new RoutingEngineServiceProxy();
				HandOffServiceProxy handOffProxy = new HandOffServiceProxy();

				Iterator _itr = routingRouteIds.iterator();
				String routingRouteId = null;
				Map directionRoutes = new TreeMap();
				IRouteModel route = null;

				routingRouteId = (String)_itr.next();

				route = handOffProxy.getHandOffBatchStopsByRoute(TransStringUtil.getDate(routeDate), routingRouteId, true);
				if(route == null) {
					Collection routes = domainManagerService.getRouteMapping(TransStringUtil.getServerDate(routeDate), routingRouteId);

					if(routes != null && routes.size() == 1) {

						RouteMapping routeMapping = (RouteMapping)routes.toArray()[0];	

						IRoutingSchedulerIdentity schedulerId = new RoutingSchedulerIdentity();
						schedulerId.setRegionId(RoutingServicesProperties.getDefaultTruckRegion());

						String sessionId = engineProxy.retrieveRoutingSession(schedulerId, routeMapping.getRoutingSessionID());

						List routingRoutes = proxy.getRoutes(TransStringUtil.getDate(routeDate), sessionId
								, routeMapping.getRouteMappingId().getRoutingRouteID());
						route = (IRouteModel)routingRoutes.get(0);
					} 
					if(route != null) {

						if(route.getStops() != null && route.getStops().size() > 0) {
							if(route != null) {
								route.getStops().add(ModelUtil.getStop(Integer.MIN_VALUE, "DPT-FD", "", "",
										"", "40740250", "-73951989", true));
								route.getStops().add(ModelUtil.getStop(Integer.MAX_VALUE, "DPT-FD", "", "",
										"", "40740250", "-73951989", true));


								List points = new ArrayList();

								Iterator stopIterator = route.getStops().iterator();
								IRoutingStopModel _stop = null;
								IGeoPoint _geoPoint = null;

								while (stopIterator.hasNext()) {

									_stop = (IRoutingStopModel) stopIterator.next();
									_geoPoint = new GeoPoint();
									if(TransStringUtil.isValidInteger(_stop.getDeliveryInfo().getDeliveryLocation()
											.getBuilding().getGeographicLocation().getLatitude())) {
										_geoPoint.setLatitude(Integer.parseInt(_stop.getDeliveryInfo().getDeliveryLocation()
												.getBuilding().getGeographicLocation().getLatitude()));
									} else {
										_geoPoint.setLatitude((int)(Double.parseDouble(_stop.getDeliveryInfo().getDeliveryLocation()
												.getBuilding().getGeographicLocation().getLatitude()) * 1000000.0));

									}

									if(TransStringUtil.isValidInteger(_stop.getDeliveryInfo().getDeliveryLocation()
											.getBuilding().getGeographicLocation().getLongitude())) {
										_geoPoint.setLongitude(Integer.parseInt(_stop.getDeliveryInfo().getDeliveryLocation()
												.getBuilding().getGeographicLocation().getLongitude()));
									} else {
										_geoPoint.setLongitude((int)(Double.parseDouble(_stop.getDeliveryInfo().getDeliveryLocation()
												.getBuilding().getGeographicLocation().getLongitude()) * 1000000.0));

									}
									
									points.add(_geoPoint);
								}
								route.setDrivingDirection(proxy.buildDriverDirections(points));

								Object[] _stops = route.getStops().toArray();								
								IRoutingStopModel _nextStop = null;

								StringBuffer strBuf = new StringBuffer();
								strBuf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>");
								strBuf.append("<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" xmlns:gpxx=\"http://www.garmin.com/xmlschemas/GpxExtensions/v3\" xmlns:gpxtpx=\"http://www.garmin.com/xmlschemas/TrackPointExtension/v1\" creator=\"navi 265W\" version=\"1.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www.garmin.com/xmlschemas/GpxExtensionsv3.xsd\">");
								strBuf.append("<metadata><link href=\"http://www.freshdirect.com\">");
								strBuf.append("<text>FD DELIVERY ROUTES BY DATE</text></link><time>2010-02-11T16:19:07Z</time>");
								strBuf.append("</metadata>");

								strBuf.append("<rte>");
								strBuf.append("<name>").append("FD Route ").append(TransStringUtil.getServerDate(routeDate)).append("</name>");
								String strStopNo = "";
								String zipCode = "";
								String addLocation = "";
								if(_stops != null) {
									for(int intCount=0; intCount<_stops.length; intCount++) {
										_nextStop = (IRoutingStopModel)_stops[intCount];
										strBuf.append("<rtept lat=\"").append(TransStringUtil.getLong(_nextStop.getDeliveryInfo().getDeliveryLocation().getBuilding().getGeographicLocation().getLatitude())/1000000.0)
										.append("\" lon=\"").append(TransStringUtil.getLong(_nextStop.getDeliveryInfo().getDeliveryLocation().getBuilding().getGeographicLocation().getLongitude())/1000000.0).append("\">");

										if(_nextStop.getDeliveryInfo().getDeliveryLocation().getBuilding().getGeographicLocation().getLatitude() != null 
												&& _nextStop.getDeliveryInfo().getDeliveryLocation().getBuilding().getGeographicLocation().getLatitude().equalsIgnoreCase("40740250")) {
											strStopNo = "";
											zipCode = "";
											addLocation = "FreshDirect";
										} else{
											strStopNo = "["+_nextStop.getStopNo()+"]";
											zipCode = ","+_nextStop.getDeliveryInfo().getDeliveryLocation().getBuilding().getZipCode();
											addLocation = "-"+_nextStop.getDeliveryInfo().getDeliveryLocation().getBuilding().getStreetAddress1();
										}
										strBuf.append("<name>").append(strStopNo).append(addLocation)						        						
										.append(zipCode)
										.append("</name>");

										strBuf.append("</rtept>");

									}
								}
								strBuf.append("</rte>");
								strBuf.append("</gpx>");
								mav.getModel().put("gpsgpxxml",strBuf.toString());

							}
						} 
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			saveMessage(request, getMessage("app.actionmessage.123", null));
		}

		return mav;
	}
	
	private Map getRouteDirections(Map routes) throws ReportGenerationException {
		Map result = new TreeMap();
		DeliveryServiceProxy proxy = new DeliveryServiceProxy();
		try {
			Iterator _itr = routes.keySet().iterator();
			String _routeId = null;
			IRouteModel route = null;
			
			while(_itr.hasNext()) {
				_routeId = (String)_itr.next();
				route = (IRouteModel)routes.get(_routeId);
				if(route != null) {
					route.getStops().add(ModelUtil.getStop(Integer.MIN_VALUE, "DPT/FD", "", "",
							"", "40740250", "-73951989", true));
					route.getStops().add(ModelUtil.getStop(Integer.MAX_VALUE, "DPT/FD", "", "",
							"", "40740250", "-73951989", true));
		
					
					List points = new ArrayList();
					
					Iterator stopIterator = route.getStops().iterator();
					IRoutingStopModel _stop = null;
					IGeoPoint _geoPoint = null;
					
					while (stopIterator.hasNext()) {
						
						_stop = (IRoutingStopModel) stopIterator.next();
						_geoPoint = new GeoPoint();
						if(TransStringUtil.isValidInteger(_stop.getDeliveryInfo().getDeliveryLocation()
															.getBuilding().getGeographicLocation().getLatitude())) {
							_geoPoint.setLatitude(Integer.parseInt(_stop.getDeliveryInfo().getDeliveryLocation()
									.getBuilding().getGeographicLocation().getLatitude()));
						} else {
							_geoPoint.setLatitude((int)(Double.parseDouble(_stop.getDeliveryInfo().getDeliveryLocation()
									.getBuilding().getGeographicLocation().getLatitude()) * 1000000.0));
							
						}
						
						if(TransStringUtil.isValidInteger(_stop.getDeliveryInfo().getDeliveryLocation()
								.getBuilding().getGeographicLocation().getLongitude())) {
							_geoPoint.setLongitude(Integer.parseInt(_stop.getDeliveryInfo().getDeliveryLocation()
									.getBuilding().getGeographicLocation().getLongitude()));
						} else {
							_geoPoint.setLongitude((int)(Double.parseDouble(_stop.getDeliveryInfo().getDeliveryLocation()
									.getBuilding().getGeographicLocation().getLongitude()) * 1000000.0));

						}
						points.add(_geoPoint);
					}
					route.setDrivingDirection(proxy.buildDriverDirections(points));
					result.put(_routeId, route);
				} else {
					result.put(_routeId, null);
				}
			}
		} catch (RoutingServiceException exp) {
			exp.printStackTrace();
			throw new ReportGenerationException(
					"Unable to generate driver directions");
		}
		
		return result;
	}
		
	private List getTermintedEmployeeIds() {
		Collection termintedList = employeeManagerService.getTerminatedEmployees();
		List result = new ArrayList();
		if(termintedList != null) {
			Iterator iterator = termintedList.iterator();
			WebEmployeeInfo info = null;
			while(iterator.hasNext()) {
				//System.out.println(iterator.next());
				info = (WebEmployeeInfo)iterator.next();
				result.add(info.getEmployeeId());
			}
		}
		return result;
	}
	
	public ModelAndView unassignedPunchedInEmployeeHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		ModelAndView mav = new ModelAndView("unassignedActiveEmployeeView");
		mav.getModel().put("unassignedEmployees",this.getDispatchManagerService().getUnassignedActiveEmployees());
		mav.getModel().put("dispDate", TransStringUtil.getCurrentDate());
		
		return mav;
	}	

}

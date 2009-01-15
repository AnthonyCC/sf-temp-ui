package com.freshdirect.transadmin.web;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.transadmin.model.Dispatch;
import com.freshdirect.transadmin.model.FDRouteMasterInfo;
import com.freshdirect.transadmin.model.Plan;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.security.SecurityManager;
import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.util.DispatchPlanUtil;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.DispatchCommand;
import com.freshdirect.transadmin.web.model.WebPlanInfo;

public class DispatchController extends AbstractMultiActionController {
	
	private DispatchManagerI dispatchManagerService;
	private EmployeeManagerI employeeManagerService;
	private DomainManagerI domainManagerService;
	
	private static final DateFormat DATE_FORMAT=new SimpleDateFormat("MM/dd/yyyy");
	private static final String DRIVER = "001";
	private static final String HELPER = "002";
	private static final String RUNNER = "003";
		
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
		
		String daterange = request.getParameter("daterange");
		String zoneLst = request.getParameter("zone");
		ModelAndView mav = new ModelAndView("planView");
		if(!TransStringUtil.isEmpty(daterange) || !TransStringUtil.isEmpty(zoneLst)) {
			
			try {
				String dateQryStr = TransStringUtil.formatDateSearch(daterange);
				String zoneQryStr = StringUtil.formQueryString(Arrays.asList(StringUtil.decodeStrings(zoneLst)));
					//TransStringUtil.formatStringSearch(zoneLst);
				if(dateQryStr != null || zoneQryStr != null) {
					Collection dataList = getPlanInfo(dateQryStr,zoneQryStr);//dispatchManagerService.getPlan(dateQryStr, zoneQryStr);
					mav.getModel().put("planlist",dataList);
				}
			} catch (Exception e) {
				e.printStackTrace();
				saveMessage(request, getMessage("app.actionmessage.123", null));
			}
		}
		
		return mav;		
	}

	private Collection getPlanInfo(String dateQryStr, String zoneQryStr) {
		
		Collection plans=dispatchManagerService.getPlan(dateQryStr, zoneQryStr);
		Collection planInfos=new ArrayList();
		Iterator it=plans.iterator();
		while(it.hasNext()) {
			
			Plan plan=(Plan)it.next();
			Zone zone=null;
			if(plan.getZone()!=null) {
				zone=domainManagerService.getZone(plan.getZone().getZoneCode());
			}
			WebPlanInfo planInfo=DispatchPlanUtil.getWebPlanInfo(plan, zone, employeeManagerService);
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
			for (int intCount = 0; intCount < arrLength; intCount++) {
				employeeSet.add(dispatchManagerService.getPlan(arrEntityList[intCount]));
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
			mav.getModel().put("dispatchInfos", getDispatchInfos(getServerDate(dispDate), zone, region, false));
			mav.getModel().put("dispDate", dispDate);
		} else {
			//By default get the today's dispatches.
			mav.getModel().put("dispatchInfos",getDispatchInfos(TransStringUtil.getCurrentServerDate(), zone, region, false));
			mav.getModel().put("dispDate", TransStringUtil.getCurrentDate());
		}
		mav.getModel().put("zones", domainManagerService.getZones());
		mav.getModel().put("regions", domainManagerService.getRegions());		
		return mav;
	}
	
	public ModelAndView dispatchSummaryHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {		
		
		ModelAndView mav = new ModelAndView("dispatchSummaryView");
		//By default get the today's dispatches.
		mav.getModel().put("dispatchInfos",getDispatchInfos(TransStringUtil.getCurrentServerDate(), null, null, true));
		mav.getModel().put("dispDate", TransStringUtil.getCurrentDate());
		return mav;
	}
	
	private Collection getDispatchInfos(String dispDate, String zoneStr, String region, boolean isSummary){
		Collection dispatchInfos = new ArrayList();
		try {
		Collection dispatchList = dispatchManagerService.getDispatchList(dispDate, zoneStr, region);
		Iterator iter = dispatchList.iterator();
			while(iter.hasNext()){
				Dispatch dispatch = (Dispatch) iter.next();
				Zone zone=domainManagerService.getZone(dispatch.getZone().getZoneCode());
				DispatchCommand command = DispatchPlanUtil.getDispatchCommand(dispatch, zone, employeeManagerService);
				if(isSummary){
					FDRouteMasterInfo routeInfo = domainManagerService.getRouteMasterInfo(command.getRoute(), new Date());
					if(routeInfo != null){
						command.setNoOfStops(routeInfo.getNumberOfStops());	
					}
					
				}
				dispatchInfos.add(command);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			throw new RuntimeException("Exception ocuurred while processing the dispatch list for requested date "+dispDate);
		}
		
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
				if(dispatch.getConfirmed()== null || dispatch.getConfirmed() == Boolean.FALSE){
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
		if (arrEntityList != null) {			
			int arrLength = arrEntityList.length;
			for (int intCount = 0; intCount < arrLength; intCount++) {
				splitter = new StringTokenizer(arrEntityList[intCount], "$");
				tmpDispatch = dispatchManagerService.getDispatch(splitter.nextToken());
				if(tmpDispatch != null) {
					Boolean confirm = tmpDispatch.getConfirmed();
					if( confirm == null || ! confirm.booleanValue() ) {
						tmpDispatch.setConfirmed(Boolean.TRUE);
					} else {
						tmpDispatch.setConfirmed(Boolean.FALSE);
					}
					dispatchSet.add(tmpDispatch);
				}
			}
		}		
		dispatchManagerService.saveEntityList(dispatchSet);
		saveMessage(request, getMessage("app.actionmessage.104", null));

		return dispatchHandler(request, response);
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
			System.out.println("Inside Summary");
			return dispatchSummaryHandler(request, response);
		}
		
	}
	
	public ModelAndView autoDispatchHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, ParseException {
		
				// if there is a dispatch record for the same date then check what user role
				// if not admin send an error message
				// if admin delete the existing record and run the autodispatch crap
		
				
				String dispatchDate = request.getParameter("daterange");					
				try {			
					if(!TransStringUtil.isEmpty(dispatchDate)) {
						dispatchDate=TransStringUtil.getServerDate(dispatchDate);	
					}else
					{
						dispatchDate=TransStringUtil.getServerDate(new Date());
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
					saveMessage(request, getMessage("app.error.115", new String[]{"Invalid Date"}));
					return planHandler(request,response);
			    }			
					
				    Collection planList=dispatchManagerService.getPlanList(dispatchDate);
				
				    if(planList==null || planList.size()==0){
				    	saveMessage(request, getMessage("app.actionmessage.134", null));
				    	return planHandler(request,response);
				    }
				    		   
					Collection dispList=dispatchManagerService.getDispatchList(dispatchDate,null,null);								
					if(dispList!=null || dispList.size()>0){
						if(!SecurityManager.isUserAdmin(request)){
							  saveMessage(request, getMessage("app.actionmessage.140", null));
							  return planHandler(request,response);							  
						}								 													
					}
				    dispatchManagerService.autoDisptch(dispatchDate);								       
				   return planHandler(request,response);		
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
	
	
}

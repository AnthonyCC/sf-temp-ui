package com.freshdirect.transadmin.web;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.transadmin.model.EmployeeInfo;
import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.ScheduleEmployeeInfo;
import com.freshdirect.transadmin.model.TrnAdHocRoute;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.model.TrnTruck;
import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.service.LocationManagerI;
import com.freshdirect.transadmin.service.ZoneManagerI;
import com.freshdirect.transadmin.util.EnumCachedDataType;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.web.model.WebEmployeeInfo;

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
        Collection dataList = null;
       
        if("T".equalsIgnoreCase(empStatus)) 
        {
        	dataList = employeeManagerService.getTerminatedEmployees();
        } 
        else if("S".equalsIgnoreCase(empStatus)) 
        {
        	dataList = employeeManagerService.getScheduleEmployees();
        	String status=request.getParameter("status");
        	if(status==null)status="a";
        	if("a".equalsIgnoreCase(status)||"i".equalsIgnoreCase(status))
        	{
	        	for(Iterator it=dataList.iterator();it.hasNext();)
	    		{
	        		ScheduleEmployeeInfo sInfo=(ScheduleEmployeeInfo)it.next();
	    			if("a".equalsIgnoreCase(status)&&"false".equalsIgnoreCase(sInfo.getTrnStatus()))
	    			{
	    				it.remove();
	    			}
	    			if("i".equalsIgnoreCase(status)&&("true".equalsIgnoreCase(sInfo.getTrnStatus())))
	    			{
	    				it.remove();
	    			}
	    			if("i".equalsIgnoreCase(status)&&sInfo.getTrnStatus()==null&&"A".equalsIgnoreCase(sInfo.getStatus()))
	    			{
	    				it.remove();
	    			}
	    			if("a".equalsIgnoreCase(status)&&sInfo.getTrnStatus()==null&&"I".equalsIgnoreCase(sInfo.getStatus()))
	    			{
	    				it.remove();
	    			}
	    		}
        	}
        	request.setAttribute("status", status);
        	return new ModelAndView("scheduleView","employees",dataList);
        } 
        else 
        {
        	if("true".equalsIgnoreCase(request.getParameter("sync")))
        	{
        		employeeManagerService.syncEmployess();
        	}
        	dataList = employeeManagerService.getEmployees();
        }
        return new ModelAndView("employeeView","employees",dataList);
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

       // System.out.println("inside zoneHandler");
        String zoneType = request.getParameter("zoneType");
        Collection dataList = null;
        Collection activeZoneCodes = null;
       // System.out.println("./................."+zoneType);
        if("Active".equalsIgnoreCase(zoneType)) {
        	dataList = domainManagerService.getZones();
        	activeZoneCodes = zoneManagerService.getActiveZoneCodes();
        	if(dataList != null && activeZoneCodes != null) {
        		Iterator _iterator = dataList.iterator();
        		Zone _tmpZone = null;
        		while(_iterator.hasNext()) {
        			_tmpZone = (Zone)_iterator.next();
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
	public ModelAndView truckDeleteHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		Set truckSet=new HashSet();
		String arrEntityList[] = getParamList(request);
		TrnTruck tmpEntity = null;
		if (arrEntityList != null) {
			int arrLength = arrEntityList.length;
			for (int intCount = 0; intCount < arrLength; intCount++) {
				tmpEntity = domainManagerService.getTruck(arrEntityList[intCount]);
				tmpEntity.setObsolete(IS_OBSOLETE);
				truckSet.add(tmpEntity);
			}
		}
		domainManagerService.saveEntityList(truckSet);
		saveMessage(request, getMessage("app.actionmessage.103", null));
		return truckHandler(request, response);
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
		Collection dataLst = locationManagerService.getServiceTimesForZoneTypes(Arrays.asList(arrEntityList));
		Collection scenarioLst = locationManagerService.getScenariosForZoneTypes(Arrays.asList(arrEntityList));
		TrnZoneType tmpEntity = null;
		boolean hasError = false;
		if (arrEntityList != null && (dataLst == null || dataLst.size() ==0)
				&& (scenarioLst == null || scenarioLst.size() ==0)) {
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

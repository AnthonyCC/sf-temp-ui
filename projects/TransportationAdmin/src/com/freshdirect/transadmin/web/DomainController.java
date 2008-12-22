package com.freshdirect.transadmin.web;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.transadmin.model.Region;
import com.freshdirect.transadmin.model.TrnAdHocRoute;
import com.freshdirect.transadmin.model.TrnArea;
import com.freshdirect.transadmin.model.TrnCutOff;
import com.freshdirect.transadmin.model.TrnTruck;
import com.freshdirect.transadmin.model.TrnZoneType;
import com.freshdirect.transadmin.model.Zone;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.EmployeeManagerI;
import com.freshdirect.transadmin.service.LocationManagerI;
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
        System.out.println("inside employeeHandler");
        String empStatus = request.getParameter("empstatus");
        Collection dataList = null;
        System.out.println("./................."+empStatus);
        if("T".equalsIgnoreCase(empStatus)) {
        	dataList = employeeManagerService.getTerminatedEmployees();
        } else {
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
        System.out.println("inside terminated EmployeeHandler");
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
		return new ModelAndView("adHocRouteView","routes",dataList);
	}

	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView zoneHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {

		Collection dataList = domainManagerService.getZones();
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
			System.out.println("zoneType is "+zoneType.getName());
			Set ztr=zoneType.getZonetypeResources();
			if(ztr!=null) {
				Iterator _it=ztr.iterator();
				while(_it.hasNext()) {
					ZonetypeResource _ztr=(ZonetypeResource)_it.next();
					//System.out.println(_ztr.getEmployeeRoleType().getDescription());
					System.out.println("_ztr.getMaximumNo() :"+_ztr.getMaximumNo()+"_ztr.getRequiredNo() :"+_ztr.getRequiredNo());

				}
			}
		}*/
		return new ModelAndView("zoneTypeView","zonetypes",dataList);
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
		if(!TransStringUtil.isEmpty(routeDate)) {
			Collection dataList = domainManagerService.getRouteNumberGroup(getServerDate(routeDate), null, null);
			mav.getModel().put("routenumberlist",dataList);
		}

		return mav;
	}
	
	public ModelAndView compositeRouteHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException  {
		String routeType = request.getParameter("routetype");
		if("A".equalsIgnoreCase(routeType)) {
			return adHocRouteHandler(request, response);
		} else {
			return routeHandler(request, response);
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
		mav.getModel().put("routeDate", routeDate);								
		try {		
			if(!TransStringUtil.isEmpty(routeDate)) {				
				//DateFormat format= new SimpleDateFormat("MM/dd/yyyy");			
					Date rouDate=DATE_FORMAT.parse(routeDate);
					Collection dataList = domainManagerService.getRoutes(TransStringUtil.getServerDate(routeDate));
					mav.getModel().put("routes",dataList);															
			} else{				
				//routeDate=DATE_FORMAT.format(new Date());				
				System.out.println("requested date :"+routeDate);				
				Collection dataList = domainManagerService.getRoutes(TransStringUtil.getServerDate(new Date()));
				mav.getModel().put("routes",dataList);			
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

}

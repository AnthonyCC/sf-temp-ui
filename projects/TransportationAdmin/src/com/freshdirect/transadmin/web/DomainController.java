package com.freshdirect.transadmin.web;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.transadmin.model.TrnEmployee;
import com.freshdirect.transadmin.model.TrnRoute;
import com.freshdirect.transadmin.model.TrnTruck;
import com.freshdirect.transadmin.model.TrnZone;
import com.freshdirect.transadmin.service.DomainManagerI;

/**
 * <code>MultiActionController</code> that handles all non-form URL's.
 *
 * @author Sivachandar
 */
public class DomainController extends AbstractMultiActionController {
	

	private DomainManagerI domainManagerService;
	
	private static final String IS_OBSOLETE = "X";
				
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
	public ModelAndView employeeHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {		
		
		Collection dataList = domainManagerService.getEmployees();
		return new ModelAndView("employeeView","employees",dataList);
	}
		
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView routeHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		Collection dataList = domainManagerService.getRoutes();
		return new ModelAndView("routeView","routes",dataList);
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
	public ModelAndView truckHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		Collection dataList = domainManagerService.getTrucks();		
		return new ModelAndView("truckView","trucks",dataList);
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
		TrnEmployee tmpEntity = null;
		if (arrEntityList != null) {			
			int arrLength = arrEntityList.length;
			for (int intCount = 0; intCount < arrLength; intCount++) {
				tmpEntity = domainManagerService.getEmployee(arrEntityList[intCount]);
				tmpEntity.setObsolete(IS_OBSOLETE);
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
	public ModelAndView routeDeleteHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		
		Set routeSet=new HashSet();
		String arrEntityList[] = getParamList(request);
		TrnRoute tmpEntity = null;
		if (arrEntityList != null) {			
			int arrLength = arrEntityList.length;
			for (int intCount = 0; intCount < arrLength; intCount++) {
				tmpEntity = domainManagerService.getRoute(arrEntityList[intCount]);
				tmpEntity.setObsolete(IS_OBSOLETE);
				routeSet.add(tmpEntity);
			}
		}		
		domainManagerService.saveEntityList(routeSet);
		saveMessage(request, getMessage("app.actionmessage.103", null));
		return routeHandler(request, response);
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
		TrnZone tmpEntity = null;
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
	

	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}	
	
	
	
	
}
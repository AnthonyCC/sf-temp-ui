package com.freshdirect.transadmin.web;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.transadmin.service.DispatchManagerI;
import com.freshdirect.transadmin.util.TransStringUtil;

public class DispatchController extends AbstractMultiActionController {
	
	private DispatchManagerI dispatchManagerService;
		

	public DispatchManagerI getDispatchManagerService() {
		return dispatchManagerService;
	}

	public void setDispatchManagerService(DispatchManagerI dispatchManagerService) {
		this.dispatchManagerService = dispatchManagerService;
	}
	
	/**
	 * Custom handler for welcome
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response
	 */
	public ModelAndView planHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException {		
		
		Collection dataList = dispatchManagerService.getPlan();
		return new ModelAndView("planView","planlist",dataList);
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
		ModelAndView mav = new ModelAndView("dispatchView");
		if(!TransStringUtil.isEmpty(dispDate)) {
			Collection dataList = dispatchManagerService.getDispatchList(getCurrentDate(dispDate), null);
			mav.getModel().put("dispatchlist",dataList);
			mav.getModel().put("dispDate", dispDate);
			
		} else {
			mav.getModel().put("dispDate", TransStringUtil.getCurrentDate());
		}
		
		return mav;
	}
	
	private String getCurrentDate(String dispDate) {
		String retDate = null;
		try {
			retDate = TransStringUtil.getServerDate(dispDate);
		} catch(ParseException parExp) {
			parExp.printStackTrace();
		}
		return retDate;
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
		StringTokenizer splitter = null;
		if (arrEntityList != null) {			
			int arrLength = arrEntityList.length;
			for (int intCount = 0; intCount < arrLength; intCount++) {
				splitter = new StringTokenizer(arrEntityList[intCount], "$");
				dispatchSet.add(dispatchManagerService.getDispatch(splitter.nextToken()
							, TransStringUtil.getServerDate(splitter.nextToken())));
			}
		}		
		dispatchManagerService.removeEntity(dispatchSet);
		saveMessage(request, getMessage("app.actionmessage.103", null));

		return dispatchHandler(request, response);
	}
	
	
}

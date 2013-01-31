package com.freshdirect.transadmin.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.transadmin.util.TransStringUtil;

public class DashboardController extends AbstractMultiActionController {
	
	private static Logger LOGGER = LoggerFactory.getInstance(DashboardController.class);
		
	/**
	 * Custom handler for Event Log
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response	
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView dashboardHandler(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav =  new ModelAndView("dashboardDataView");
		mav.getModel().put("dispatchDate", TransStringUtil.getNextDate());
		return mav;
	}
	
}

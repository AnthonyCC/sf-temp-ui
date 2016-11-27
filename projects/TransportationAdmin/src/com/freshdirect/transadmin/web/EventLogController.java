package com.freshdirect.transadmin.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.transadmin.service.IEventLogManagerService;

public class EventLogController extends AbstractMultiActionController {
	
	private static Logger LOGGER = LoggerFactory.getInstance(EventLogController.class);
		
	/**
	 * Custom handler for Event Log
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @return a ModelAndView to render the response	
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView eventLogHandler(HttpServletRequest request, HttpServletResponse response) {
		
		LOGGER.info("######################### EventLogHandler ######################### ");
		
		String eventType = request.getParameter("eventtype");
		
		ModelAndView mav =  new ModelAndView("eventLogView");
		
		if("M".equalsIgnoreCase(eventType)) {
			mav =  new ModelAndView("motEventLogView");
		} else if("S".equalsIgnoreCase(eventType)) {
			mav =  new ModelAndView("shiftLogView");
		}
		
		return mav;
	}
	
}

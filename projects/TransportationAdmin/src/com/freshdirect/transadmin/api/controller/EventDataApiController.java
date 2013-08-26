package com.freshdirect.transadmin.api.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.framework.service.ServiceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.mail.ErpMailSender;
import com.freshdirect.transadmin.api.model.ListDataMessage;
import com.freshdirect.transadmin.api.model.Message;
import com.freshdirect.transadmin.model.EventLogRouteModel;
import com.freshdirect.transadmin.model.EventLogSubType;
import com.freshdirect.transadmin.model.EventLogType;
import com.freshdirect.transadmin.model.EventModel;
import com.freshdirect.transadmin.model.MotEventModel;
import com.freshdirect.transadmin.model.MotEventType;
import com.freshdirect.transadmin.security.SecurityManager;
import com.freshdirect.transadmin.service.DomainManagerI;
import com.freshdirect.transadmin.service.IEventLogManagerService;
import com.freshdirect.transadmin.service.exception.TransAdminServiceException;
import com.freshdirect.transadmin.util.TransAdminCacheManager;
import com.freshdirect.transadmin.util.TransStringUtil;
import com.freshdirect.transadmin.util.TransportationAdminProperties;
import com.freshdirect.transadmin.web.model.EventLogInfo;

public class EventDataApiController extends BaseApiController {
	
	private static Logger LOGGER = LoggerFactory.getInstance(EventDataApiController.class);
	
	private IEventLogManagerService eventLogService;
	
	private DomainManagerI domainManagerService;

	public IEventLogManagerService getEventLogService() {
		return eventLogService;
	}

	public void setEventLogService(IEventLogManagerService eventLogService) {
		this.eventLogService = eventLogService;
	}

	public DomainManagerI getDomainManagerService() {
		return domainManagerService;
	}

	public void setDomainManagerService(DomainManagerI domainManagerService) {
		this.domainManagerService = domainManagerService;
	}
	
	private static final String ACTION_GET_EVENTLOGS = "getEventLogs";
		
	private static final String ACTION_EVENT_ADD = "addEvent";
	
	private static final String ACTION_GET_ROUTE = "getRoutes";
	
	private static final String ACTION_GET_MOTEVENTTYPE = "getMotEventTypes";
	
	private static final String ACTION_GET_ADDHOCROUTE = "getAddHocRoutes";
	
	private static final String ACTION_GET_MOTEVENTLOGS = "getMotEventLogs";
	
	private static final String ACTION_MOTEVENT_ADD = "addMotEvent";
	
	private static final String ACTION_VERIFY_EVENT = "verifyMot";
	
	private static final String ACTION_GET_SHIFTEVENTLOG = "getShiftLogs";
	
	private static final String ACTION_SHIFTEVENT_ADD = "addShiftEvent";
	
	private static final String ACTION_GET_EVENTDATA = "getEventLogMtda";
	
	private static final String ACTION_UPDATE_EVENTDATA = "updateEventLogMtda";
	
	private static final String ACTION_GET_MOTEVENTDATA = "getMotEventLogMtda";
	
	private static final String ACTION_UPDATE_MOTEVENTDATA = "updateMotEventLogMtda";
	
	
	@Override
	protected ModelAndView processRequest(HttpServletRequest request,
			HttpServletResponse response, ModelAndView model, String action) {
		
		String userId = SecurityManager.getUserName(request);
		String daterange = request.getParameter("daterange");		
		String eventID = request.getParameter("eventID");
		String deliveryDate = request.getParameter("deliveryDate");

		Date eventDate = TransStringUtil.addDays(new Date(), TransportationAdminProperties.getEventLogDataLookUpDays());
		boolean dataFlag = false;
		if (daterange != null && !"".equals(daterange)) {
			try {
				eventDate = TransStringUtil.getDate(daterange);
				dataFlag = true;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if (ACTION_GET_EVENTLOGS.equals(action)) {

			ListDataMessage result = new ListDataMessage();
		
			List<EventModel> events = dataFlag ? getEventLogService().lookUpEvents(eventDate) 
															: getEventLogService().lookUpEventForDateRange(eventDate);
			if(events != null) {
				Collections.sort(events, new EventLogComparator());
			}
			result.setRows(events);
			setResponseMessage(model, result);
			
		} else if (ACTION_EVENT_ADD.equals(action)) {
			
			logEvent(request, response, model, userId);
			
		} else if (ACTION_GET_ROUTE.equals(action)) {
		

			if (deliveryDate == null || "".equals(deliveryDate)) {
				deliveryDate = TransStringUtil.getCurrentDate();
			}
		
			lookUpRoutes(model, deliveryDate, request, response);

		} else if (ACTION_GET_MOTEVENTTYPE.equals(action)) {
			ListDataMessage responseMessage = new ListDataMessage();

			List<MotEventType> eventTypes = eventLogService.lookUpMotEventTypes(null);
			responseMessage.setRows(eventTypes);
			setResponseMessage(model, responseMessage);

			return model;
	        
		} else if (ACTION_GET_MOTEVENTLOGS.equals(action)) {			
				
			ListDataMessage result = new ListDataMessage();							
			List<MotEventModel> events = dataFlag ? getEventLogService().lookUpMotEvents(eventDate)
															:  getEventLogService().lookUpMotEventForDateRange(eventDate);			
			if(events != null) {
				Collections.sort(events, new MotEventLogComparator());
			}
			result.setRows(events);
			setResponseMessage(model, result);
			
		} else if (ACTION_GET_ADDHOCROUTE.equals(action)) {
			
			lookUpAdHocRoutes(model, request, response);
			
		} else if (ACTION_MOTEVENT_ADD.equals(action)) {
			
			logMotEvent(request, response, model, userId);
			
		} else if (ACTION_VERIFY_EVENT.equals(action)) {

			verifyMotEvent(model, userId, eventID);
			
		} else if (ACTION_GET_SHIFTEVENTLOG.equals(action)) {

			ListDataMessage result = new ListDataMessage();
			List<EventModel> shiftEvents = new ArrayList<EventModel>();
			List<EventModel> events = dataFlag ? getEventLogService().lookUpEvents(eventDate) : getEventLogService().lookUpEventForDateRange(eventDate);
			if(events != null) {
				for(EventModel _event : events) {
					if("End of Shift Scanner Log".equals(_event.getEventType())) { 
						shiftEvents.add(_event);
					}
				}				
				Collections.sort(shiftEvents, new EventLogComparator());
			}
			result.setRows(shiftEvents);
			setResponseMessage(model, result);
			
		} else if (ACTION_SHIFTEVENT_ADD.equals(action)) {
			
			logShiftEvent(request, response, model, userId);
			
		} else if (ACTION_GET_EVENTDATA.equals(action)) {
			
			lookUpEventLogMetadata(model, request, response);
			
		} else if(ACTION_UPDATE_EVENTDATA.equals(action)) {
			
			updateEventData(request, response, model, userId);	
		} else if (ACTION_GET_MOTEVENTDATA.equals(action)) {
			
			lookUpMotEventLogMetadata(model, request, response);
			
		} else if(ACTION_UPDATE_MOTEVENTDATA.equals(action)) {
			
			updateMotEventData(request, response, model, userId);	
		}
		
		return model;
	}

	private void verifyMotEvent(ModelAndView model, String userId, String eventID) {
		Message responseMessage = null;
		try {
			MotEventModel event = getEventLogService().lookUpMotEventById(eventID);
			if (event != null) {
				event.setVerified(true);
				event.setVerifiedBy(userId);
				event.setVerifiedDate(new Date());
				getEventLogService().updateMotEventLog(event);
			}
		} catch (TransAdminServiceException e) {
			e.printStackTrace();
			responseMessage = Message.createFailureMessage("Verifying event failed.");
		}
		setResponseMessage(model, responseMessage);
	}

	private void logEvent(HttpServletRequest request,
			HttpServletResponse response, ModelAndView model, String userId) {
		Message responseMessage = null;
		try {
			EventModel event  = parseRequestObject(request, response, EventModel.class);
			String windowTime = event.getWindowTime();
			String[] strArray = null;
			Date windowStartTime = null, windowEndTime = null;
			if(windowTime != null ) {
				strArray = StringUtils.split(windowTime, "-");
				try {
					windowStartTime = TransStringUtil.getDatewithTime("01/01/1970 "+ strArray[0].trim());
					windowEndTime = TransStringUtil.getDatewithTime("01/01/1970 "+ strArray[1].trim());
				} catch (ParseException e) { }
			}
			event.setWindowStartTime(windowStartTime);
			event.setWindowEndTime(windowEndTime);
			if(event.getOrderNo() != null) {
				event.setRoute(event.getOrderNo());
			}
			if(event.getId() == null) { 
				event.setTransactionDate(new Date());
				event.setUserId(userId);
				eventLogService.logEvent(event);
				responseMessage = Message.createSuccessMessage("Event added successfully.");
				try {
					if(event.getEventType() != null) {
						EventLogSubType _tmpSubType = null;
						List<EventLogType> eventTypes = eventLogService.lookUpEventTypes(event.getEventType());
			    		if(eventTypes != null && eventTypes.size() > 0) {
			    			for(EventLogSubType _subType : eventTypes.get(0).getSubTypes()) {
			    				if(_subType.getId().equals(event.getEventSubType())){
			    					_tmpSubType = _subType;
			    					break;
			    				}
			    			}
			    			
			    		}
			    		if(_tmpSubType.getMsgGroup() != null && _tmpSubType.getMsgGroup().getEmail() != null && !"".equals(_tmpSubType.getMsgGroup().getEmail())
								&& _tmpSubType.getMsgGroup().getEmail() != null) {
			    			emailEventLog(event, _tmpSubType);
			    		}
					}
					
				} catch (ParseException e) {
					LOGGER.error("Parse event date failed with exception", e) ;
				}
			} else {
				eventLogService.updateEventLog(event);
				responseMessage = Message.createSuccessMessage("Event updated successfully.");
			}
		} catch (ServiceException e) {
			e.printStackTrace();
			responseMessage = Message.createFailureMessage("Updating event failed.");
		}
		
		setResponseMessage(model, responseMessage);
	}

	private void logMotEvent(HttpServletRequest request,
			HttpServletResponse response, ModelAndView model, String userId) {
		Message responseMessage = null;
		try {
			MotEventModel event  = parseRequestObject(request, response, MotEventModel.class);
			
			if(event.getId() == null) { 
				event.setTransactionDate(new Date());
				event.setUserId(userId);
				if(event.getOrderNo() != null) {
					event.setRoute(event.getOrderNo());
				}					
				eventLogService.logMotEvent(event);
				responseMessage = Message.createSuccessMessage("Event added successfully.");
				try {
					List<MotEventType> eventType = eventLogService.lookUpMotEventTypes(event.getEventType());
					if(eventType.get(0).getMsgGroup() != null && !"".equals(eventType.get(0).getMsgGroup().getEmail())
							&& eventType.get(0).getMsgGroup().getEmail() != null) {
						emailMotEventLog(event, eventType.get(0));
					}
				} catch (ParseException e) {
					LOGGER.error("Parse event date failed with exception", e) ;
				}
			} else {
				eventLogService.updateMotEventLog(event);
				responseMessage = Message.createSuccessMessage("Event updated successfully.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseMessage = Message.createFailureMessage("Logging event failed.");
		}
		
		setResponseMessage(model, responseMessage);
	}    
    
    private ModelAndView lookUpRoutes(ModelAndView model, String eventDate, HttpServletRequest request, HttpServletResponse response) {
    	ListDataMessage responseMessage = new ListDataMessage();
        List<EventLogRouteModel> routes = new ArrayList<EventLogRouteModel>();
        try {
        	Map<String, EventLogRouteModel> routeMapping = TransAdminCacheManager.getInstance().lookUpDeliveryRoutes(eventDate, eventLogService);
        	if(routeMapping != null)
        		routes.addAll(routeMapping.values());
        	
        	responseMessage.setRows(routes);

        } catch (TransAdminServiceException ex) {
        	responseMessage.addErrorMessage("Looking up delivery routes failed.");
        }
        setResponseMessage(model, responseMessage);
        return model;
    }
    

    private ModelAndView lookUpAdHocRoutes(ModelAndView model, HttpServletRequest request, HttpServletResponse response) {
    	ListDataMessage responseMessage = new ListDataMessage();
             
       	responseMessage.setRows(TransAdminCacheManager.getInstance().lookUpAddHocRoute(domainManagerService));
        setResponseMessage(model, responseMessage);
        return model;
    }
    
    private void logShiftEvent(HttpServletRequest request,
			HttpServletResponse response, ModelAndView model, String userId) {
		Message responseMessage = null;
		try {
			EventModel event  = parseRequestObject(request, response, EventModel.class);
			String windowTime = event.getWindowTime();
			String[] strArray = null;
			Date windowStartTime = null, windowEndTime = null;
			if(windowTime != null ) {
				strArray = StringUtils.split(windowTime, "-");
				try {
					windowStartTime = TransStringUtil.getDatewithTime("01/01/1970 "+ strArray[0].trim());
					windowEndTime = TransStringUtil.getDatewithTime("01/01/1970 "+ strArray[1].trim());
				} catch (ParseException e) { }
			}
		
			event.setWindowStartTime(windowStartTime);
			event.setWindowEndTime(windowEndTime);
			if(event.getId() == null) { 
				event.setTransactionDate(new Date());
				event.setUserId(userId);
				eventLogService.logEvent(event);
				responseMessage = Message.createSuccessMessage("Event added successfully.");
				try {
					if(event.getEventType() != null) {
						EventLogSubType _tmpSubType = null;
						List<EventLogType> eventTypes = eventLogService.lookUpEventTypes(event.getEventType());
			    		if(eventTypes != null) {
			    			for(EventLogSubType _subType : eventTypes.get(0).getSubTypes()) {
			    				if(_subType.getId().equals(event.getEventSubType())){
			    					_tmpSubType = _subType;
			    					break;
			    				}
			    			}			    			
			    		}
						if(_tmpSubType.getMsgGroup() != null && !"".equals(_tmpSubType.getMsgGroup().getEmail())
								&& _tmpSubType.getMsgGroup().getEmail() != null) {
							emailEventLog(event, _tmpSubType);
						}
					}
					
				} catch (ParseException e) {
					LOGGER.error("Parse event date failed with exception", e) ;
				}
			} else {
				eventLogService.updateEventLog(event);
				responseMessage = Message.createSuccessMessage("Event updated successfully.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseMessage = Message.createFailureMessage("Updating event failed.");
		}
		
		setResponseMessage(model, responseMessage);
	}
    
    private ModelAndView lookUpEventLogMetadata(ModelAndView model, HttpServletRequest request, HttpServletResponse response) {
    	ListDataMessage responseMessage = new ListDataMessage();
        List<EventLogInfo> result = new ArrayList<EventLogInfo>();
        try {
        	EventLogInfo info = new EventLogInfo();
        	
        	List<EventLogSubType> subTypes = new ArrayList<EventLogSubType>();    		
    		List<EventLogType> eventTypes = eventLogService.lookUpEventTypes(null);
    		if(eventTypes != null) {
    			for(EventLogType _type :  eventTypes) {
    				subTypes.addAll(_type.getSubTypes());
    			}
    		}
        	info.setEventType(eventTypes);
        	info.setEventSubType(subTypes);
        	info.setEventMessageGroup(eventLogService.lookUpEventMessageGroup(null));
        	
        	result.add(info);
        	responseMessage.setRows(result);

        } catch (TransAdminServiceException ex) {
        	responseMessage.addErrorMessage("Looking up event data failed.");
        }
        setResponseMessage(model, responseMessage);
        return model;
    }
    
    private void updateEventData(HttpServletRequest request,
			HttpServletResponse response, ModelAndView model, String userId) {
		Message responseMessage = null;
		try {
			EventLogInfo eventInfo  = parseRequestObject(request, response, EventLogInfo.class);
			if(eventInfo != null)
				eventLogService.logEventLogInfo(eventInfo.getEventType(), eventInfo.getEventSubType(), eventInfo.getEventMessageGroup());			
			responseMessage = Message.createSuccessMessage("Event data updated successfully.");
			
		} catch (Exception e) {
			e.printStackTrace();
			responseMessage = Message.createFailureMessage("Updating event data failed.");
		}		
		setResponseMessage(model, responseMessage);
	}
    
    private ModelAndView lookUpMotEventLogMetadata(ModelAndView model, HttpServletRequest request, HttpServletResponse response) {
    	ListDataMessage responseMessage = new ListDataMessage();
        List<EventLogInfo> result = new ArrayList<EventLogInfo>();
        try {
        	EventLogInfo info = new EventLogInfo();
        	    		
    		List<MotEventType> eventTypes = eventLogService.lookUpMotEventTypes(null);
    		
        	info.setMotEventType(eventTypes);
        	info.setEventMessageGroup(eventLogService.lookUpMotEventMessageGroup(null));
        	
        	result.add(info);
        	responseMessage.setRows(result);

        } catch (TransAdminServiceException ex) {
        	responseMessage.addErrorMessage("Looking up mot event data failed.");
        }
        setResponseMessage(model, responseMessage);
        return model;
    }
    
    private void updateMotEventData(HttpServletRequest request,
			HttpServletResponse response, ModelAndView model, String userId) {
		Message responseMessage = null;
		try {
			EventLogInfo eventInfo  = parseRequestObject(request, response, EventLogInfo.class);
			eventLogService.logMotEventLogInfo(eventInfo.getMotEventType(), eventInfo.getEventMessageGroup());			
			responseMessage = Message.createSuccessMessage("Event datat updated successfully.");
			
		} catch (Exception e) {
			e.printStackTrace();
			responseMessage = Message.createFailureMessage("Updating event data failed.");
		}		
		setResponseMessage(model, responseMessage);
	}
    
    private static void emailEventLog(EventModel event, EventLogSubType subType) throws ParseException {
		
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			
			StringBuffer buff = new StringBuffer();

			buff.append("<html>").append("\n").append("<body>").append("\n");
			
			buff.append("<table class=\"MsoNormalTable\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:400.0pt;margin-left:-1.15pt;border-collapse:collapse\">");
			
			buff.append("<tr style=\"height:18.75pt\">");
					buff.append("<td colspan=\"2\" valign=\"bottom\" style=\"border:solid windowtext 1.0pt;background:#92D050;padding:0in 5.4pt 0in 5.4pt;height:18.75pt;\">")
						.append("<p class=\"MsoNormal\" align=\"center\" style=\"text-align:center\"><b><span style=\"font-size:14.0pt;color:black\">Event Log</span></b></p>")
						.append("</td>");
			buff.append("</tr>");
			
			buff.append("<tr style=\"height:15.0pt\">");
					buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
						.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append("Event Date").append("</span></p>")
						.append("</td>");
					buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
						.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append(TransStringUtil.getDate(event.getEventDate())).append("</span></p>")
						.append("</td>");
			buff.append("</tr>");
			
			buff.append("<tr style=\"height:15.0pt\">");
					buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
						.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append("Route").append("</span></p>")
						.append("</td>");
					buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
						.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append(event.getRoute() != null ? event.getRoute() : "").append("</span></p>")
						.append("</td>");
			buff.append("</tr>");
	
			buff.append("<tr style=\"height:15.0pt\">");
					buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
						.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append("Window Time").append("</span></p>")
						.append("</td>");
					buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
						.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append(event.getWindowTime() != null ? event.getWindowTime() : "").append("</span></p>")
						.append("</td>");
			buff.append("</tr>");
			
			buff.append("<tr style=\"height:15.0pt\">");
					buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
						.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append("Truck").append("</span></p>")
						.append("</td>");
					buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
						.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append(event.getTruck() != null ? event.getTruck() : "").append("</span></p>")
						.append("</td>");
			buff.append("</tr>");
	
			buff.append("<tr style=\"height:15.0pt\">");
					buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
						.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append("Type").append("</span></p>")
						.append("</td>");
					buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
						.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append(subType.getEventTypeName()).append("</span></p>")
						.append("</td>");
			buff.append("</tr>");

			buff.append("<tr style=\"height:15.0pt\">");
					buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
						.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append("Sub Type").append("</span></p>")
						.append("</td>");
					buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
						.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append(subType != null ? subType.getName() : "").append("</span></p>")
						.append("</td>");
			buff.append("</tr>");
			
			buff.append("<tr style=\"height:15.0pt\">");
				buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
					.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append("Description").append("</span></p>")
					.append("</td>");
				buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
					.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append(event.getDescription() != null ? event.getDescription() : "").append("</span></p>")
					.append("</td>");
			buff.append("</tr>");
			
			buff.append("<tr style=\"height:15.0pt\">");
				buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
					.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append("Stops").append("</span></p>")
					.append("</td>");
				buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
					.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append(TransStringUtil.join(event.getStops(), ",")).append("</span></p>")
					.append("</td>");
			buff.append("</tr>");
			
			buff.append("<tr style=\"height:15.0pt\">");
				buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
					.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append("Created By").append("</span></p>")
					.append("</td>");
				buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
					.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append(event.getUserId()).append("</span></p>")
					.append("</td>");
			buff.append("</tr>");
			
			buff.append("</table>");
						
			buff.append("</body>").append("</html>");

			ErpMailSender mailer = new ErpMailSender();

			mailer.sendMail(TransportationAdminProperties.getEventLogMailFrom(), (subType.getMsgGroup() != null ? subType.getMsgGroup().getEmail() : "")
								, "", TransportationAdminProperties.getEventLogMailSubject(), buff.toString(), true, "");
			
		} catch (MessagingException e) {
			LOGGER.warn("Error Sending EventLog notification: ", e);
		}
    }
    
    private static void emailMotEventLog(MotEventModel event, MotEventType type) throws ParseException {
		
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
			String subject="Event Log:	"+ dateFormatter.format(new Date());

			StringBuffer buff = new StringBuffer();

			buff.append("<html>").append("\n").append("<body>").append("\n");
			
			buff.append("<table class=\"MsoNormalTable\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:400.0pt;margin-left:-1.15pt;border-collapse:collapse\">");
			
			buff.append("<tr style=\"height:18.75pt\">");
					buff.append("<td colspan=\"2\" valign=\"bottom\" style=\"border:solid windowtext 1.0pt;background:#92D050;padding:0in 5.4pt 0in 5.4pt;height:18.75pt;\">")
						.append("<p class=\"MsoNormal\" align=\"center\" style=\"text-align:center\"><b><span style=\"font-size:14.0pt;color:black\">MOT Event Log</span></b></p>")
						.append("</td>");
			buff.append("</tr>");
			
			buff.append("<tr style=\"height:15.0pt\">");
					buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
						.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append("Event Date").append("</span></p>")
						.append("</td>");
					buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
						.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append(TransStringUtil.getDate(event.getEventDate())).append("</span></p>")
						.append("</td>");
			buff.append("</tr>");
			
			buff.append("<tr style=\"height:15.0pt\">");
					buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
						.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append("Route").append("</span></p>")
						.append("</td>");
					buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
						.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append(event.getRoute() != null ? event.getRoute() : "").append("</span></p>")
						.append("</td>");
			buff.append("</tr>");
				
			buff.append("<tr style=\"height:15.0pt\">");
					buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
						.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append("Mot Truck").append("</span></p>")
						.append("</td>");
					buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
						.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append(event.getAddHocRoute() != null ? event.getAddHocRoute() : "").append("</span></p>")
						.append("</td>");
			buff.append("</tr>");
	
			buff.append("<tr style=\"height:15.0pt\">");
					buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
						.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append("Event Type").append("</span></p>")
						.append("</td>");
					buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
						.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append(type.getName()).append("</span></p>")
						.append("</td>");
			buff.append("</tr>");
			
			buff.append("<tr style=\"height:15.0pt\">");
				buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
					.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append("Description").append("</span></p>")
					.append("</td>");
				buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
					.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append(event.getDescription() != null ? event.getDescription() : "").append("</span></p>")
					.append("</td>");
			buff.append("</tr>");
			
			buff.append("<tr style=\"height:15.0pt\">");
				buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
					.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append("Stops").append("</span></p>")
					.append("</td>");
				buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
					.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append(TransStringUtil.join(event.getStops(), ",")).append("</span></p>")
					.append("</td>");
			buff.append("</tr>");
			
			buff.append("<tr style=\"height:15.0pt\">");
				buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
					.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append("Created By").append("</span></p>")
					.append("</td>");
				buff.append("<td valign=\"bottom\" style=\"border:solid windowtext 1.0pt;border-top:none;padding:0in 5.4pt 0in 5.4pt;height:15.0pt\">")
					.append("<p class=\"MsoNormal\"><span style=\"color:black\">").append(event.getUserId()).append("</span></p>")
					.append("</td>");
			buff.append("</tr>");
			
			buff.append("</table>");
						
			buff.append("</body>").append("</html>");

			ErpMailSender mailer = new ErpMailSender();

			mailer.sendMail(TransportationAdminProperties.getEventLogMailFrom(), (type.getMsgGroup() != null ? type.getMsgGroup().getEmail() : "")
								, "", "MOT Eventlog Notification", buff.toString(), true, "");
			
		} catch (MessagingException e) {
			LOGGER.warn("Error Sending MotEvent log notification email: ", e);
		}
    }
    
    private class EventLogComparator implements Comparator<EventModel> {		
		
		public int compare(EventModel obj1, EventModel obj2){						
			return obj2.getTransactionDate().compareTo(obj1.getTransactionDate());
		}		
	}

    private class MotEventLogComparator implements Comparator<MotEventModel> {
    	
		public int compare(MotEventModel obj1, MotEventModel obj2){
			return obj2.getTransactionDate().compareTo(obj1.getTransactionDate());
		}		
	}
}

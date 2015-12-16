<%@page import="com.freshdirect.webapp.taglib.location.LocationHandlerTag"%>
<%@page import="com.freshdirect.framework.webapp.ActionError"%>
<%@page import="com.freshdirect.framework.webapp.ActionResult"%>
<%@page import="com.freshdirect.common.address.AddressModel"%>
<%@page import="com.freshdirect.framework.util.log.LoggerFactory"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ taglib uri="freshdirect" prefix="fd"
%>
<fd:CheckLoginStatus noRedirect="true"/><%

//out.println( "test " );

Logger LOGGER = LoggerFactory.getInstance("locationhandler.jsp");

LOGGER.debug("karl's silly test: ");

// serve only AJAX requests
if (request.getHeader("X-Requested-With") != null) {
	// Prevent caching AJAX responses on browser-side
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	
	LOGGER.debug("karl's other test ");
	
	try{
		String action = request.getParameter("action");
		%><fd:LocationHandler action='<%=action%>'/><%
		String serverError = (String)pageContext.getAttribute(LocationHandlerTag.SERVER_ERROR_ATTR);
		if(serverError == null) {
			ActionResult result = (ActionResult)pageContext.getAttribute(LocationHandlerTag.ACTION_RESULT_ATTR);
			
			LOGGER.debug("karl's test with line 33, action = " + action);
			
			if (result.isFailure()){
				response.setStatus(400);
				%><div class="invisible error message" data-type="error"><%
				for (ActionError error : result.getErrors()){
					%><div class="error-message"><p><b class="erroritem red" data-errortype="<%=error.getType()%>"><%=error.getDescription()%></b></p></div><%
				}		
				%></div><%
			} else if("ifDeliveryZone".equals(action)){
				String zipcode_temp = (String)request.getParameter("zipcode");
				
				LocationHandlerTag x = new LocationHandlerTag();
				//out.println( x.zipper(zipcode_temp) );
				
				LOGGER.debug("karl's test with line 48, zipcode_temp = " + zipcode_temp);
				
				out.println( x.hasFdxService(zipcode_temp) );
				
				LOGGER.debug("karl's test with line 52, x.hasFdxService(zipcode_temp) = " + x.hasFdxService(zipcode_temp));
				
				return;
				
			} else if(!"setZipCode".equals(action) && !"selectAddress".equals(action) ) { // because reload happens
				AddressModel selectedAddress = (AddressModel)pageContext.getAttribute(LocationHandlerTag.SELECTED_ADDRESS_ATTR);
				%><span class="invisible addresstext"><%= LocationHandlerTag.formatAddressShortText(selectedAddress) %></span>
				<span class="invisible addresszip"><%= selectedAddress.getZipCode() %></span>
				<jsp:include page="/shared/locationbar/location_messages.jsp"/><%
				if ("futureZoneNotification".equals(action)){
					%><fd:CmRegistration force="true" wrapIntoScriptTag="true" email='<%= request.getParameter("futureZoneNotificationEmail") %>' address="<%=selectedAddress%>"/><%	
				} 
			}		
		} else {
			LOGGER.debug("what the hell, there was an error? it seems to consist of: " + serverError);
			
			response.setStatus(400);
			%><div class="invisible error message" data-type="error"><div class="error-message"><p><%= serverError  %></p></div></div><%
		}
		
	} catch (Exception exc) {
		response.setStatus(500);
		%>Internal error occurred.<%
	}
}
%>
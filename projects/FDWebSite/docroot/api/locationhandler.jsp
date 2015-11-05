<%@page import="com.freshdirect.webapp.taglib.location.LocationHandlerTag"%>
<%@page import="com.freshdirect.framework.webapp.ActionError"%>
<%@page import="com.freshdirect.framework.webapp.ActionResult"%>
<%@page import="com.freshdirect.common.address.AddressModel"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@ taglib uri="freshdirect" prefix="fd"
%>
<fd:CheckLoginStatus noRedirect="true"/><%

// serve only AJAX requests
if (request.getHeader("X-Requested-With") != null) {
	// Prevent caching AJAX responses on browser-side
	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Pragma", "no-cache");
	try{
		String action = request.getParameter("action");
		%><fd:LocationHandler action='<%=action%>'/><%
		String serverError = (String)pageContext.getAttribute(LocationHandlerTag.SERVER_ERROR_ATTR);
		if(serverError == null) {
			ActionResult result = (ActionResult)pageContext.getAttribute(LocationHandlerTag.ACTION_RESULT_ATTR);
			
			
			if (result.isFailure()){
				response.setStatus(400);
				%><div class="invisible error message" data-type="error"><%
				for (ActionError error : result.getErrors()){
					%><div class="error-message"><p><b class="erroritem red" data-errortype="<%=error.getType()%>"><%=error.getDescription()%></b></p></div><%
				}		
				%></div><%
			} else if("ifDeliveryZone".equals(action)){
				out.println(LocationHandlerTag.isDeliveryZone);
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
			response.setStatus(400);
			%><div class="invisible error message" data-type="error"><div class="error-message"><p><%= serverError  %></p></div></div><%
		}
		
	} catch (Exception exc) {
		response.setStatus(500);
		%>Internal error occurred.<%
	}
}
%>
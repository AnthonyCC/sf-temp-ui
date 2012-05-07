<%@ page language="java" contentType="text/plain; charset=UTF-8" pageEncoding="UTF-8"
%><%@ taglib uri='freshdirect' prefix='fd'
%><%@page import="com.freshdirect.framework.util.log.LoggerFactory"
%><%@page import="com.freshdirect.framework.webapp.ActionError"
%><%@page import="java.util.List"
%><%@page import="com.freshdirect.fdstore.customer.FDCartLineI"
%><%@page import="org.apache.log4j.Logger"
%><%@page import="com.freshdirect.fdstore.customer.EnumQuickbuyStatus"
%><fd:CheckLoginStatus noRedirect="true"/><%!
final Logger LOGGER = LoggerFactory.getInstance("/quickbuy/ajax_add_to_cart.jsp");

String getStatusMessage(EnumQuickbuyStatus status) {
	StringBuilder buf = new StringBuilder();
	if (status.isError())
		buf.append("<span class=\"error\">");
	else if (status.isWarning())
		buf.append("<span class=\"warning\">");
	else buf.append("<span class=\"ok\">");

	buf.append(status.getMessage());
	
	buf.append("</span>");

	return buf.toString();
}
%><%
if (session.getAttribute("fd.user") != null) {
	try {
%><fd:FDShoppingCart result="result" id="cart" action='addMultipleToCart'><%
	if (result.isSuccess()) {
		List<FDCartLineI> cartLines = cart.getRecentOrderLines();
		if (cartLines.size() > 0) {
			LOGGER.info("add to cart successful");
%><%= getStatusMessage(EnumQuickbuyStatus.ADDED_TO_CART) %><% 
		} else {
			LOGGER.warn("no items were added - this is unexpected");
%><%= getStatusMessage(EnumQuickbuyStatus.NO_OP) %><%
		}
	} else {
		ActionError error = result.getErrors().iterator().next();
		if (error.getType().startsWith("quantity")) {
			LOGGER.info("quantity not specified");
%><%= getStatusMessage(EnumQuickbuyStatus.SPECIFY_QUANTITY) %><%
		}
	}
%></fd:FDShoppingCart><%
	} catch (Exception e) {
		LOGGER.error("error processing request: " + request.getQueryString(), e);
%><%= getStatusMessage(EnumQuickbuyStatus.ERROR) %><%
	}
} else {
	LOGGER.warn("user not logged in");
%><%= getStatusMessage(EnumQuickbuyStatus.NOT_LOGGED_IN) %><%
}
%>

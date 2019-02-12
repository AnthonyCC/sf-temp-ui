<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>

<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri="logic" prefix="logic" %>
<%@ taglib uri="freshdirect" prefix="fd" %>
<%
	// Get the OrderModel using the orderId from the request
	boolean showSignature = false;
	String orderId = request.getParameter("orderId");
	FDSessionUser user = (FDSessionUser)session.getAttribute(SessionName.USER);
	String successPage = "/main/order_details.jsp?orderId="+orderId;
	String action = request.getParameter("action");
	if(action == null){
		action = "add_return";
	}else{
		action.trim();
	}
%>

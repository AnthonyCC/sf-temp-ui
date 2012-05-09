<%@page import="java.util.ArrayList"
%><%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><%@page import="java.util.Date"
%><%@page import="java.text.SimpleDateFormat"
%><%@page import="com.freshdirect.fdstore.standingorders.FDStandingOrder"
%><%@page import="com.freshdirect.framework.util.StringUtil"
%><%@page import="com.freshdirect.framework.core.PrimaryKey"
%><%@page import="com.freshdirect.fdstore.standingorders.FDStandingOrdersManager"
%><%@page import="com.freshdirect.fdstore.standingorders.EnumStandingOrderFrequency"
%><%@page import="com.freshdirect.fdstore.customer.FDUserI"
%><%@page import="com.freshdirect.webapp.taglib.fdstore.FDSessionUser"
%><%@page import="org.json.JSONArray"
%><%@page import="org.json.JSONObject"
%><%@page import="com.metaparadigm.jsonrpc.SerializerState"
%><%@page import="com.metaparadigm.jsonrpc.JSONSerializer"
%><%@page import="com.freshdirect.webapp.util.json.EnumStandingOrderFrequencyJSONSerializer"
%><%@page import="com.freshdirect.webapp.taglib.fdstore.SessionName"
%><%@page import="com.freshdirect.fdstore.customer.FDActionInfo"
%><%@page import="java.util.List"
%><%@page import="com.freshdirect.fdstore.customer.FDCustomerManager"
%><%@page import="com.freshdirect.fdstore.customer.FDOrderHistory"
%><%@page import="com.freshdirect.webapp.taglib.fdstore.AccountActivityUtil"
%><%@page import="com.freshdirect.fdstore.customer.FDOrderInfoI"
%><%@page import="com.freshdirect.webapp.taglib.fdstore.ModifyOrderControllerTag"
%><%@page import="java.util.UUID"
%><%@page import="com.freshdirect.framework.util.DateUtil"
%><%@page import="com.freshdirect.customer.EnumTransactionSource"
%><%@page import="com.freshdirect.framework.webapp.ActionResult"
%><%@ taglib uri="freshdirect" prefix="fd"
%><%

	FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
	if (user == null) {
		response.setStatus(500);
		%>Your session was expired. Please reload the page.<%
		return;			
	}

	// serve only AJAX requests!
	if (request.getHeader("X-Requested-With") != null) {
		// Prevent caching AJAX responses on browser-side
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");

		String action = request.getParameter("action");
		
		try {
			if ("delete".equalsIgnoreCase(action)) {
				// Standing Order ID
				String soId = request.getParameter("soId");
	
				if (soId != null && !"".equals(soId)) {
					FDStandingOrder so = FDStandingOrdersManager.getInstance().load(new PrimaryKey(soId));
					if (!so.isDeleted()) {
						FDActionInfo info = AccountActivityUtil.getActionInfo(session);
						FDStandingOrdersManager.getInstance().delete(info, so);
					}
				}				
			} else if ("isLocked".equalsIgnoreCase(action)){
				// Standing Order ID
				
				String soId = request.getParameter("soId");
				if (soId != null && soId != "") {
					String lockId = FDStandingOrdersManager.getInstance().getLockId(soId);
					
					if (lockId!=null){
						%>true<%
					} else {
						%>false<%
					}
				} else {
					throw new IllegalArgumentException("soId cannot be null or blank");
				}
			} else if ("setFrequencyCur".equalsIgnoreCase(action)) {
				// frequency enum ordinal
				String freqOrd = request.getParameter("freqOrd");
				
				if (freqOrd != null && !"".equals(freqOrd)) {
					int ord = Integer.parseInt(freqOrd);
					
					user.getCurrentStandingOrder().setFrequency(ord);
				}
			} 
		} catch (Exception exc) {
			response.setStatus(500);
			%>Internal error occurred.<%
		}
	}
%>
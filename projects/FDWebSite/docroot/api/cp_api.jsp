<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.freshdirect.framework.webapp.ActionError"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.FDSessionUser"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.FDCustomerCouponUtil"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ page import='com.freshdirect.framework.webapp.*' %>
<%@ page import="com.freshdirect.webapp.util.*"%>
<%--

	@author Sivachandar

	This is invoked when clip coupon is done in frontend.
	
--%>
<%!
    boolean doClip(HttpServletRequest request, HttpSession session) {
		boolean result = false;
		String couponId = (String) request.getParameter("cpid");
		JspLogger.ACCESS.debug("doClip >"+couponId);
		FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
		if(user != null && couponId != null && couponId.trim().length() > 0) {
			result = FDCustomerCouponUtil.clipCoupon(session, couponId);
		}
		
      	return result;
    }
    %>
    

<%
try {
	JspLogger.ACCESS.debug("Try to invoke cp_api.jsp");
	// serve only AJAX requests!
	if (request.getHeader("X-Requested-With") != null) {
			// Prevent caching AJAX responses on browser-side
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			boolean apiResult = false;
			if(request.getParameter("action") != null) {
				if("clip".equals(request.getParameter("action"))) {
					apiResult = doClip(request, session);
				}
			}		
			if (apiResult) {
				//Do Coupon Clip here
				%>true<%
			} else {
				// not logged in...
				response.setStatus(500);
				%>false<%
			}
	} else {
		// General failure
		response.setStatus(500);
		%>false<%
	}
} catch (Exception exc) {
	response.setStatus(500);
	%>Internal error occurred.<%
}	
%>
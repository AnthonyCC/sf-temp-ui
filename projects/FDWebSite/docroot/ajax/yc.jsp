<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.fdstore.util.EnumSiteFeature"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName"%>
<%@ taglib uri="freshdirect" prefix="fd"%>
<%--
	API file that refreshes Your Cart

	@author segabor
--%>
<%
	// serve only AJAX requests!
	if (request.getHeader("X-Requested-With") != null) {
		// Prevent caching AJAX responses on browser-side
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");

		FDUserI u = (FDUserI) session.getAttribute(SessionName.USER);
		if (u != null) {
%><%@ include file="/common/template/includes/your_cart.jspf" %>
<%
		}
	}
%>

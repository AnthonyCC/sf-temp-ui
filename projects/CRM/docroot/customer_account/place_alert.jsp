<%@ page import='java.util.*' %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.webapp.util.JspTableSorter" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%	
	String successPage = request.getParameter("successPage");
	if (successPage == null) successPage = "/main/customer_alert_list.jsp";

	String action = NVL.apply(request.getParameter("action"), "");	
	
	FDUserI user = (FDSessionUser) session.getAttribute(SessionName.USER);
	FDIdentity identity = user.getIdentity();		
	
	String alertType = NVL.apply(request.getParameter("alert_type"), "");	
	String customerAlertId = NVL.apply(request.getParameter("customer_alert_id"), "");	
	
%>


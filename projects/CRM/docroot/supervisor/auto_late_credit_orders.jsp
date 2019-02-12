<%@ page import='java.text.*' %>
<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.customer.*' %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@page import="com.freshdirect.fdstore.customer.CustomerCreditModel"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='crm' prefix='crm' %>

<%
	//System.out.println("*******************"+session.getAttribute("LATE_CREDITS"));
	if("submitted".equals(request.getParameter("action")) && session.getAttribute("LATE_CREDITS") != null && "done".equals((String)session.getAttribute("LATE_CREDITS"))) {		
		session.removeAttribute("LATE_CREDITS");
%>
	<jsp:include page="auto_late_dlv_credits.jsp" />
<% } else { %>

<tmpl:insert template='/template/supervisor_resources.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Auto Late Delivery Credits</tmpl:put>

<tmpl:put name='content' direct='true'>

</tmpl:put>

</tmpl:insert>

<% } %>

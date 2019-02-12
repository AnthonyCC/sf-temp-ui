<%@ page import='java.text.*' %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<%@ page import="com.freshdirect.customer.EnumSaleStatus" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>

<tmpl:insert template='/template/top_nav.jsp'>

<tmpl:put name='title' direct='true'>Supervisor Resources > Re-Submit Customers</tmpl:put>

<tmpl:put name='content' direct='true'>
	<% 
		boolean isPost = "POST".equalsIgnoreCase(request.getMethod());
		int customersToDisplay = 0;
	%>
	<jsp:include page="/includes/admintools_nav.jsp" />

	
</tmpl:put>
</tmpl:insert>

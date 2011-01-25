
<%@page import="com.freshdirect.crm.CrmAgentModel"%>
<%@page import="com.freshdirect.webapp.taglib.crm.CrmSession"%>
<%@page import="com.freshdirect.crm.CrmAgentRole"%>

<% 
	String agentId = CrmSession.getCurrentAgentStr(request.getSession());
	CrmAgentRole crmRole= CrmSession.getCurrentAgentRole(request.getSession());
	boolean isTRNUser = crmRole.equals(CrmAgentRole.getEnum(CrmAgentRole.TRN_CODE));
%>
<% if(isTRNUser){ %>
	<jsp:forward page="/promotion/promo_ws_view.jsp"/>
<% } else  {%>
	<jsp:forward page="/promotion/promo_view.jsp"/>
<% } %>	
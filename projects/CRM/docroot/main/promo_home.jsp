
<%@page import="com.freshdirect.crm.CrmAgentModel"%>
<%@page import="com.freshdirect.webapp.taglib.crm.CrmSession"%>
<%@page import="com.freshdirect.crm.CrmAgentRole"%>

<jsp:forward page="/promotion/promo_view.jsp"/>
<%-- 
	CrmAgentModel agent = CrmSession.getCurrentAgent(request.getSession());
	boolean isTRNUser = agent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.TRN_CODE));
%>
<% if(isTRNUser){ %>
	<jsp:forward page="/promotion/promo_ws_view.jsp"/>
<% } else  {%>
	<jsp:forward page="/promotion/promo_view.jsp"/>
<% } --%>	
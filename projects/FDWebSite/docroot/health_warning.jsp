<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus/>
<tmpl:insert template='/common/template/no_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Alcohol Information</tmpl:put>
<tmpl:put name='content' direct='true'>
<% String successPage = request.getParameter("successPage");
    request.setAttribute("listPos", "SystemMessage,SideCartBottom");
%>
<fd:HealthWarningController successPage="<%=successPage%>" result="result">
	<fd:IncludeMedia name="/media/editorial/site_pages/health_warning.html"/>
</fd:HealthWarningController>
</tmpl:put>

</tmpl:insert>
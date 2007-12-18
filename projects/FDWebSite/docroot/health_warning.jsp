<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/common/template/right_nav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Alcohol Information</tmpl:put>
<tmpl:put name='content' direct='true'>
<% String successPage = request.getParameter("successPage");%>
<fd:HealthWarningController successPage="<%=successPage%>" result="result">
	<fd:IncludeMedia name="/media/editorial/site_pages/health_warning.html"/>
</fd:HealthWarningController>
</tmpl:put>

</tmpl:insert>
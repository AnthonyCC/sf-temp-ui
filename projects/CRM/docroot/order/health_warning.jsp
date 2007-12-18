<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/template/top_nav.jsp'>
<tmpl:put name='title' direct='true'>Beer provided by W.B.L., Inc. - Wine & Spirits provided by Best Cellars</tmpl:put>
<tmpl:put name='content' direct='true'>
<% String successPage = request.getParameter("successPage");%>
<fd:HealthWarningController successPage="<%=successPage%>" result="result">
	<div class="content_scroll" align="center">
		<fd:IncludeMedia name="/media/editorial/site_pages/health_warning.html"/>
	</div>
</fd:HealthWarningController>
</tmpl:put>
</tmpl:insert>
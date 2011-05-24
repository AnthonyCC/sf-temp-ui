<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus/>
<tmpl:insert template='/common/template/blank.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Alcohol Information</tmpl:put>
	<tmpl:put name='content' direct='true'>
		<fd:IncludeMedia name="/media/editorial/site_pages/health_warning.html"/>
	</tmpl:put>
</tmpl:insert>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus />
<tmpl:insert template='/common/template/dnav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - About Us: Press</tmpl:put>
<tmpl:put name='content' direct='true'>

	<fd:IncludeMedia name="/media/editorial/about/press/recent.html" />

</tmpl:put>
</tmpl:insert>
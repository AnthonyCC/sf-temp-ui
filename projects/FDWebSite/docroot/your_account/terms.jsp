<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus id="user" guestAllowed="true" />
<tmpl:insert template='/common/template/blank.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - Terms And Conditions</tmpl:put>
<tmpl:put name='content' direct='true'>
	<fd:IncludeMedia name="/media/editorial/site_pages/sms/terms_long.html" />
</tmpl:put>
</tmpl:insert>
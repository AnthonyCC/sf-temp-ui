<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus />

<tmpl:insert template='/common/template/giftcard.jsp'>
	<tmpl:put name='title' direct='true'>FreshDirect - Terms and Conditions FreshDirect Gift Cards</tmpl:put>
	<tmpl:put name='content' direct='true'>
		<fd:IncludeMedia name="/media/editorial/giftcards/media_includes/terms_and_conditions.html" />
	</tmpl:put>
</tmpl:insert>
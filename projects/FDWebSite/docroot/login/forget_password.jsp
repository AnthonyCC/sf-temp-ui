<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_FORGET_PASSWORD_TOTAL = 700;
%>
<fd:CheckLoginStatus guestAllowed='true' recognizedAllowed='true' id='user' />
<fd:ForgotPasswordController results="result" successPage='/login/forget_password_confirmation.jsp' password="password">	
	<tmpl:insert template='/common/template/no_nav.jsp'>
		<tmpl:put name='title' direct='true'>FreshDirect - Forgot Your Password? - Enter E-mail Address</tmpl:put>
		<tmpl:put name='content' direct='true'>
			<%@ include file="/login/includes/forget_password.jspf" %>
		</tmpl:put>
	</tmpl:insert>
</fd:ForgotPasswordController>
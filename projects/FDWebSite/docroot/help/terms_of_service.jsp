<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus />
<tmpl:insert template='/common/template/dnav.jsp'>
    <tmpl:put name='content' direct='true'>
	<%
		final int W_USER_AGREEMENT_TOTAL = 970;
	%>
	<%@ include file="/shared/includes/registration/i_user_agreement.jspf"%>	
	</tmpl:put>
</tmpl:insert>

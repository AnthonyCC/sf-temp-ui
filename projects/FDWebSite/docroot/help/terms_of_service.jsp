<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/common/template/dnav.jsp'>

    <tmpl:put name='title' direct='true'>FreshDirect - Help - Customer Agreement</tmpl:put>

    <tmpl:put name='content' direct='true'>

<%@ include file="/shared/includes/registration/i_user_agreement.jspf"%>	
	
	
	</tmpl:put>
</tmpl:insert>

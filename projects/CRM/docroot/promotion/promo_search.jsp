<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='template' prefix='tmpl' %>

<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>Search Promotions</tmpl:put>
	
	<tmpl:put name='content' direct='true'>
	<crm:GetCurrentAgent id='currentAgent'>
		<%@ include file="/includes/promotions/i_promo_nav.jspf" %>
	
	</crm:GetCurrentAgent>
	</tmpl:put>
</tmpl:insert>
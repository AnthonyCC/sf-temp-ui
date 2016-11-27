<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
int W_USER_AGREEMENT_TOTAL = 970;
final int W_POPUP_TOTAL = 500;
%>

<fd:CheckLoginStatus guestAllowed='true'/>
<%
String template = "/common/template/dnav.jsp";
	
String type = request.getParameter("type");
if (type != null && !"".equals(type) && type.equalsIgnoreCase("popup")) {
	template = "/shared/template/large_pop_no_resize.jsp";
  W_USER_AGREEMENT_TOTAL = W_POPUP_TOTAL;
}
%>

<tmpl:insert template='<%=template%>'>
    <tmpl:put name='content' direct='true'>
    <%@ include file="/shared/includes/registration/i_platform_agreement.jspf"%>	
	</tmpl:put>
</tmpl:insert>

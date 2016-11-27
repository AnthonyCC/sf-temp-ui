<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_PRIVACY_POLICY_TOTAL = 970;
final int W_PRIVACY_POLICY_POPUP_TOTAL = 500;
%>

<fd:CheckLoginStatus guestAllowed='true'/>
<%
FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
String help = " Help -";
String template = "/common/template/dnav.jsp";
int tableWidth = W_PRIVACY_POLICY_TOTAL;
	
String type = request.getParameter("type");
if (type != null && !"".equals(type) && type.equalsIgnoreCase("popup")) {
	template = "/shared/template/large_pop_no_resize.jsp";
	help = "";
	tableWidth = W_PRIVACY_POLICY_POPUP_TOTAL;
}
%>

<tmpl:insert template='<%=template%>'>
    <tmpl:put name='content' direct='true'>
		<table width="<%=tableWidth%>" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>
				<script type="text/javascript">
					if (!window['_page_options']) {	var _page_options = {}; };
					_page_options['csEmail'] = '<fd:GetServiceEmail />';
					_page_options['csPhone'] = '<% if (user != null) { %><%=user.getCustomerServiceContact()%><%}%>';
					_page_options['csIsCT'] = <% if (user != null) { %><%=user.isChefsTable()%><%}else{%>false<%}%>;
				</script>
				<% if ( request.getParameter("lang") != null) { %>
					<% if ("espanol".equalsIgnoreCase(request.getParameter("lang"))) { %>
					<tmpl:put name='seoMetaTag' direct='true'>
       					<fd:SEOMetaTag pageId="privacy_policy" language='es-ES'/>
 					 </tmpl:put>
					
						<fd:IncludeMedia name="/media/editorial/site_pages/privacy_policy/privacy_policy_espanol.html" />
					<% } else { %>
						<tmpl:put name='seoMetaTag' direct='true'>
      						<fd:SEOMetaTag pageId="privacy_policy" language='en-US'/>
  						</tmpl:put>
						<fd:IncludeMedia name="/media/editorial/site_pages/privacy_policy/privacy_policy.html" />
					<% } %>
				<% } else { %>
					<tmpl:put name='seoMetaTag' direct='true'>
      					<fd:SEOMetaTag pageId="privacy_policy" language='en-US'/>
  					</tmpl:put>
					<fd:IncludeMedia name="/media/editorial/site_pages/privacy_policy/privacy_policy.html" />
				<% } %>
			</td>
		</tr>
		</table>
	</tmpl:put>
</tmpl:insert>

<%-- promotion details in popup --%>
<%@ page import='java.text.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import="com.freshdirect.fdstore.promotion.management.*" %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.SessionName' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");

	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);

	String promoCode = request.getParameter("promoCode");
%>
<% if (!"".equals(promoCode)) { %>
	<fd:GetPromotionNew id="promotion" promotionId="<%=promoCode%>">
		<tmpl:insert template='/shared/template/small_pop.jsp'>
			<tmpl:put name='title' direct='true'>FreshDirect - <%= promoCode %> Promotion</tmpl:put>
				<tmpl:put name='content' direct='true'>
					<fd:IncludeMedia name='<%= "/media/editorial/promotions/" + promoCode + ".html" %>'>
					<table cellpadding="0" cellspacing="0" border="0" width="100%" class="promoPopupTerms">
						<tr>
							<td><%= promotion.getTerms() %></td>
						</tr>
					</table>
					</fd:IncludeMedia>
				</tmpl:put>
		</tmpl:insert>
	</fd:GetPromotionNew>
<% } %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.freshdirect.fdstore.promotion.EnumPromotionStatus"%>
<%@ page import="com.freshdirect.webapp.taglib.promotion.PromoFilterCriteria"%>
<%@ page import="com.freshdirect.webapp.taglib.promotion.PromoNewRow"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName"%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/template/top_nav.jsp'>
	<tmpl:put name='title' direct='true'>Publish Promotions</tmpl:put>
	<tmpl:put name='content' direct='true'>
	<crm:GetCurrentAgent id='currentAgent'>
		<fd:GetPublishablePromotions id="promoRows">
		<%
			final boolean isPublishInvoked = "publish".equalsIgnoreCase(request.getParameter("action"));
			final int s = promoRows.size();
			
			String promoId = "";
		%>
		<%-- PROMO HEADER --%>
		<fd:GetPromotionNew id = "promotion" promotionId="<%= promoId %>">
			<%@ include file="/includes/promotions/i_promo_nav.jspf" %>
		</fd:GetPromotionNew>
		
		<div class="promo_page_content-view_content2 noBorder">

			<div class="promo_page_header">
				<br />
				<table>
					<tr>
						<td class="promo_page_header_text">Promotion&nbsp;Publish&nbsp;</td>
						<td>
							<span class="padL8R16">Environment: DEV</span>
						</td>
						<td>
							<div class="publish-btn-publish promo_btn_brn"><a href="#" onclick="$('promo-publish').submit(); return false;">PUBLISH <img src="/media_stat/crm/images/orange_r_arrow.gif" height="12px" width="12px" alt="" /></a></div>
						</td>
					</tr>
				</table>
			</div>
			<div>
			<%	if (!isPublishInvoked) { %>
				<div class="publish-msg-candidate">
				<% if (s == 0) { %>
					No candidate promotions found.
				<% } else { %>
					<%= s %> promotion<%= s > 1 ? "s" : "" %> found.
				<% } %>
				</div>
			<% }
			if (s > 0) { %>
				<crm:PromoPublish result="result" actionName="default" publishResult="publishResult">
				<% if (isPublishInvoked) {
					if (result.isSuccess()) { %>
					<div class="title18 publish-msg-success">Promotions have been successfully published!</div>
					<% } else { %>
					<br />
					<fd:ErrorHandler id="errorMsg" result="<%= result %>" name="promo.publish">
						<%@ include file="/includes/i_error_messages.jspf" %>
					</fd:ErrorHandler>
				<% }
				}

				if (!isPublishInvoked || (isPublishInvoked && !result.isSuccess())) { %>
					<form name="promo-publish-form" id="promo-publish" method="POST">
						<input type="hidden" name="action" value="publish" />
						<table>
							<tr class="publish-rows-hdr">
								<td>&nbsp;</td>
								<th>Name</th>
								<th>Code</th>
								<th>Redemption Code</th>
								<th>Start Date</th>
								<th>Expiration Date</th>
								<th>Status</th>
							</tr>
						<%
						int k = 0;
						for (PromoNewRow p : promoRows) {
							String cssK = k%2 == 0 ? "publish-rows-odd" : "publish-rows-even";
						%>
							<tr id="<%= p.getId() %>-row">
								<td><input type="checkbox" name="<%= p.getCode() %>"/></td>
								<td class="<%= cssK %>"><a href="/promotion/promo_details.jsp?promoId=<%= p.getCode() %>"><%= p.getName() %></a></td>
								<td class="<%= cssK %>"><%= p.getCode() %></td>
								<td class="<%= cssK %>" style="text-align: center;"><%= p.getRedemptionCode() %></td>
								<td class="<%= cssK %>"><%= p.getStart() %></td>
								<td class="<%= cssK %>"><%= p.getExpire() %></td>
								<td class="<%= cssK %>"><%= p.getStatus().getName() %></td>
							</tr>
						<%
							k++;
						}
						%>
						</table>
					</form>
				<% } %>
				</crm:PromoPublish>
			<% } %>
			</div>
		</div>
		</fd:GetPublishablePromotions>
	</crm:GetCurrentAgent>
	</tmpl:put>
</tmpl:insert>

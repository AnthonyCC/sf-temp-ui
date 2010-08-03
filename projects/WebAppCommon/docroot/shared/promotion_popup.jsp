<%-- promotion details in popup --%>
<%@ page import='java.text.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import="com.freshdirect.fdstore.promotion.management.*" %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.SessionName' %>
<%@ page import='java.text.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.fdstore.promotion.*'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Cache-Control", "no-cache");

	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);

	String promoCode = request.getParameter("promoCode");

	Promotion promotion = (Promotion) PromotionFactory.getInstance().getPromotion(promoCode);
	if (promotion == null) promotion = (Promotion) user.getEligibleSignupPromotion();
	if (promotion == null) promotion = (Promotion) PromotionFactory.getInstance().getPromotion("SIGNUP");
%>
<% if (!"".equals(promoCode)) { %>
	<fd:GetPromotionNew id="promotionNew" promotionId="<%=promoCode%>">
		<tmpl:insert template='/shared/template/small_pop.jsp'>
			<tmpl:put name='title' direct='true'>FreshDirect - <%= promotion.getName() %></tmpl:put>
				<tmpl:put name='content' direct='true'>
					<style>
						.promoPopupTerms {
							width: 330px;
						}
						.promoPopupTerms th {
							text-align: center;
						}
					</style>
					<fd:IncludeMedia name='<%= "/media/editorial/promotions/" + promoCode + ".html" %>'>
					<% if ( promotionNew.getTerms() == null || "".equals(promotionNew.getTerms()) ) { %>
					<% 
						DateFormat expirationDateFormat = new SimpleDateFormat("MMMM d, yyyy");
						String expDate = promotion != null && promotion.getExpirationDate() != null? "Offer expires on " + expirationDateFormat.format(promotion.getExpirationDate()):"Limited Time Offer";
						%>
						<table cellpadding="0" cellspacing="0" border="0" width="100%">
						<tr><td class="title14"><b><%= promotion.getDescription() %></b></td></tr>
						<tr><td><br>
							Web orders only. May not be combined with any other promotional offer. One per household. Delivery and billing address must match. Available in selected zones. Other restrictions may apply. <%= expDate %>.
							<br>
						<%
						List rules = promotion.getHeaderDiscountRules();
						if (rules != null && !rules.isEmpty()) {
							HeaderDiscountRule discountRule = (HeaderDiscountRule)rules.iterator().next();
							int maxAmount = (int)discountRule.getMaxAmount();
							int minSubtotal = (int)discountRule.getMinSubtotal();
							%>
							In order to receive $<%=maxAmount%> off an order, the order subtotal must be greater than $<%=minSubtotal%>.
							<br>
						<%}%>
						</td></tr>
						</table>
					<% }else{ %>
						<table cellpadding="0" cellspacing="0" border="0" width="100%" class="promoPopupTerms">
							<tr>
								<th class="title14"><%= promotionNew.getDescription() %></th>
							</tr>
							<tr>
								<td><%= promotionNew.getTerms() %></td>
							</tr>
						</table>
					<% } %>
					</fd:IncludeMedia>
				</tmpl:put>
		</tmpl:insert>
	</fd:GetPromotionNew>
<% } %>
<%@ page import="java.util.*"%>
<%@ page import="com.freshdirect.fdstore.promotion.management.FDPromoZipRestriction"%>
<%@ page import="com.freshdirect.fdstore.promotion.management.FDPromoCustStrategyModel"%>
<%@ page import="com.freshdirect.fdstore.promotion.management.FDPromotionAttributeParam"%>
<%@ page import="com.freshdirect.fdstore.promotion.management.FDPromoContentModel"%>
<%@ page import="com.freshdirect.fdstore.promotion.management.FDPromoDlvZoneStrategyModel"%>
<%@ page import="com.freshdirect.fdstore.promotion.management.FDPromoDlvTimeSlotModel"%>
<%@ page import="com.freshdirect.fdstore.promotion.management.FDPromoDlvDateModel"%>
<%@ page import="com.freshdirect.fdstore.promotion.EnumPromotionStatus"%>
<%@ page import="com.freshdirect.fdstore.FDStoreProperties"%>
<%@ page import="com.freshdirect.fdstore.promotion.EnumPromotionProfileAttribute"%>
<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>Promotion Details</tmpl:put>
	<tmpl:put name='content' direct='true'>
	<crm:GetCurrentAgent id='currentAgent'>
		<%
			String promoId = request.getParameter("promoId");
			
		%>
		<fd:GetPromotionNew id = "promotion" promotionId="<%= promoId %>">
		<%
			String actionName = request.getParameter("actionName");
			String statusStr = request.getParameter("status");
			EnumPromotionStatus status =null;
			if(null != statusStr){
				status = EnumPromotionStatus.getEnum(statusStr);
			}
			
			PromotionStateGraph graph = new PromotionStateGraph(promotion);		
		%>
		<crm:PromotionStateSwitch result="result" actionName="<%= actionName%>" graph="<%= graph %>" status="<%= status %>" promotion="<%= promotion%>">
		
		<%@ include file="/includes/promotions/i_promo_nav.jspf" %>
		
		<%-- Top-Row, page specific nav --%>
		<form method='POST' name="frmPromoDetails" id="frmPromoDetails">
		
		<br />

		<div class="promo_page_content-view_content noBorder" style="width: 100%;">
			<div class="errContainer">
				<fd:ErrorHandler result="<%=result%>" name="promo.store" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="startDateEmpty" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="endDateEmpty" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="usageCountEmpty" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="offerTypeEmpty" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="discountEmpty" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="dcpdEmpty" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="dcrEmpty" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="prodNameEmpty" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="catNameEmpty" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="addressTypeEmpty" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="wsZoneRequired" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="minSubTotalEmpty" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="maxAmountEmpty" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="combineOfferRequired" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
			</div>
			<div class="promo_page_header noBorder">
				<table width="100%" border="0">
					<tr>
						<td class="promo_page_header_text vTop">Promotion&nbsp;Detail&nbsp;</td>
						<td width="80%" class="gray10pt vTop">
							<%@ include file="/includes/promotions/i_promo_detail_top.jspf" %>
						</td>
						<%@ include file="/includes/promotions/i_promo_status.jspf" %>
						<td width="1%" rowspan="2"><img width="16" height="0" src="/media_stat/crm/images/clear.gif" alt="" /></td>
					</tr>
					<tr>
						<td colspan="2">
							<%-- Promotion details, name-usage --%>
							<div class="padLR4">
								<%@ include file="/includes/promotions/i_promo_detail_topAdditionals.jspf" %>
							</div>
						</td>
					</tr>
				</table>
			</div>

			<%-- Promotion details, offer--%>
			<div class="padLR4">
				<%@ include file="/includes/promotions/i_promo_detail_offer.jspf" %>
			</div>
			<%-- Promotion details, customer requirements--%>
			<div class="padLR4">
			
			
				<%@ include file="/includes/promotions/i_promo_detail_custreq.jspf" %>
			
			</div>
			<%-- Promotion details, cart requirements--%>
			<div class="padLR4">
				<%@ include file="/includes/promotions/i_promo_detail_cartreq.jspf" %>
			</div>
			<%-- Promotion details, delivery requirements--%>
			<div class="padLR4">
				<%@ include file="/includes/promotions/i_promo_detail_dlvreq.jspf" %>
			</div>
			<%-- Promotion details, payment requirements--%>
			<div class="padLR4">
				<%@ include file="/includes/promotions/i_promo_detail_payreq.jspf" %>
			</div>
		</div>
		</form>
		</crm:PromotionStateSwitch>
	</fd:GetPromotionNew>
	</crm:GetCurrentAgent>
	</tmpl:put>
</tmpl:insert>
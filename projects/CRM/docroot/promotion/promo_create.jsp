<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>Create Promotion</tmpl:put>
	
	<tmpl:put name='content' direct='true'>
	<crm:GetCurrentAgent id='currentAgent'>
		
		<%
		String promoId = request.getParameter("promoId");		
		String successPage ="/promotion/promo_details.jsp?promoId=";
		
		%>
		<fd:GetPromotionNew id = "promotion" promotionId="<%= promoId %>">
		<%@ include file="/includes/promotions/i_promo_nav.jspf" %>
		
		<div class="promo_page_content-view_content2 noBorder">
			<fd:PromotionBasicInfoController result="result" promotion="<%= promotion %>" actionName="createBasicPromo" successPage="<%= successPage %>">
				<fd:ErrorHandler result="<%=result%>" name="nameEmpty" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="descriptionEmpty" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="redemptionCodeEmpty" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="redemptionCodeDuplicate" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="offerDescEmpty" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="audiDescEmpty" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="termsEmpty" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="windowStrgEmpty" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
					</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="usageError" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="startDateEmpty" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="endDateEmpty" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="endDateBefore" id="errorMsg">
					 <%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<form method='POST' name="frmPromoEditBasic">
				<%-- Top-Row, page specific nav --%>
				<div class="promo_page_header">
					<br />
					<table width="100%">
						<tr>
							<td class="promo_page_header_text">New&nbsp;Promotion&nbsp;</td>
							<td width="90%" class="gray10pt">
								<%@ include file="/includes/promotions/i_promo_edit_top.jspf" %>
							</td>
							<td>
								<img width="300" height="0" src="/media_stat/crm/images/clear.gif" alt="" />
								<input type="submit" value="CREATE PROMOTION" onclick="" class="promo_btn_brn fleft noMargLR" />
								<input type="button" value="CANCEL" onclick="promotionListView();" class="promo_btn_wht fright noMargLR" />
							</td>
							<td width="1%"><img width="16" height="0" src="/media_stat/crm/images/clear.gif" alt="" /></td>
						</tr>
					</table>
				</div>
				<%-- Promotion create, edit basic (with slight difs for new) --%>
				<div class="padLR4">
					<%@ include file="/includes/promotions/i_promo_edit_basic.jspf" %>
				</div>
				</form>
			</fd:PromotionBasicInfoController>
		</div>
	</fd:GetPromotionNew>
	</crm:GetCurrentAgent>
	</tmpl:put>
</tmpl:insert>
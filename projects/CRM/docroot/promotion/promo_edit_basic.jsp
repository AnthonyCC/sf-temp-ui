<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import='com.freshdirect.webapp.crm.security.*' %>
<tmpl:insert template='/template/top_nav.jsp'>
	<% String promoId = request.getParameter("promoId");%>
<fd:GetPromotionNew id="promotion" promotionId="<%=promoId%>">
	<tmpl:put name='title' direct='true'>Edit Basic Information</tmpl:put>

	<tmpl:put name='content' direct='true'>
	
	<%
	String successPage ="/promotion/promo_edit.jsp?promoId="+promoId;
	String userId = CrmSecurityManager.getUserName(request);
	%>
		<fd:PromotionBasicInfoController result="result" promotion="<%= promotion %>" actionName="editBasicPromo" successPage="<%= successPage %>">
		<%@ include file="/includes/promotions/i_promo_nav.jspf" %>
		
		<form method='POST' name="frmPromoEditBasic" id="frmPromoEditBasic">
		<%-- Top-Row, page specific nav --%>
		<div class="promo_page_header">
			<br />
			<table width="100%">
				<tr>
					<td class="promo_page_header_text">Edit&nbsp;Basic&nbsp;Information&nbsp;</td>
					<td width="90%" class="gray10pt">
						<%@ include file="/includes/promotions/i_promo_edit_top.jspf" %>
					</td>
					<td>
						<img width="300" height="0" src="/media_stat/crm/images/clear.gif" alt="" />
						<input type="submit" value="SAVE CHANGES" onclick="" class="promo_btn_brn fleft noMargLR w130px" />
						<input type="button" class="promo_btn_wht fright noMargL w150px" onclick="this.form.reset()" value="UNDO ALL CHANGES" />
					</td>
					<td width="1%"><img width="16" height="0" src="/media_stat/crm/images/clear.gif" alt="" /></td>
				</tr>
			</table>
		</div>
		<%-- Promotion edit, edit basic (with slight difs for new) --%>
		
		<div class="promo_page_content-view_content4 noBorder">
			<div class="padLR4">
				<div class="errContainer">
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
				</div>
				<%@ include file="/includes/promotions/i_promo_edit_basic.jspf" %>
			</div>
		</div>
		</form>
		</fd:PromotionBasicInfoController>
	
	
	</tmpl:put>
	</fd:GetPromotionNew>
</tmpl:insert>
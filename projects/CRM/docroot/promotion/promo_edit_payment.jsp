<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import="com.freshdirect.fdstore.promotion.*" %>
<%@ page import="com.freshdirect.fdstore.promotion.management.*" %>
<tmpl:insert template='/template/top_nav.jsp'>
<% String promoId = request.getParameter("promoId");%>
<fd:GetPromotionNew id="promotion" promotionId="<%=promoId%>">
	<tmpl:put name='title' direct='true'>Edit Payment Requirement</tmpl:put>
	
	<tmpl:put name='content' direct='true'>
	<crm:GetCurrentAgent id='currentAgent'>
	<%
	String successPage ="/promotion/promo_edit.jsp?promoId="+promoId;
	%>
		<fd:PromotionCustReqController result="result" promotion="<%= promotion %>" actionName="promoPayment" successPage="<%= successPage %>">		
		<%@ include file="/includes/promotions/i_promo_nav.jspf" %>
		<fd:ErrorHandler result="<%=result%>" name="minOrdersNumber" id="errorMsg">
       		 <%@ include file="/includes/i_error_messages.jspf" %>   
        </fd:ErrorHandler>
		<form method='POST' name="frmPromoPaymentReq" id="frmPromoPaymentReq">
		<%-- Top-Row, page specific nav --%>
		<div class="promo_page_header">
			<br />
			<table width="100%">
				<tr>
					<td class="promo_page_header_text">Edit&nbsp;Payment&nbsp;Requirement&nbsp;</td>
					<td width="90%" class="gray10pt">
						<%@ include file="/includes/promotions/i_promo_edit_top.jspf" %>
					</td>
					<td>
						<img width="300" height="0" src="/media_stat/crm/images/clear.gif" alt="" />
						<input type="submit" value="SAVE CHANGES" onclick="" class="promo_btn_brn fleft noMargLR" />
						<input type="button" class="promo_btn_wht fright noMargL" onclick="this.form.reset()" value="UNDO ALL CHANGES" />
					</td>
					<td width="1%"><img width="16" height="0" src="/media_stat/crm/images/clear.gif" alt="" /></td>
				</tr>
			</table>
		</div>
		<%-- Promotion edit, edit payment requirement --%>
			<%@ include file="/includes/promotions/i_promo_edit_payreq.jspf" %>
		</form>
		</fd:PromotionCustReqController>
	</crm:GetCurrentAgent>
	</tmpl:put>
	</fd:GetPromotionNew>
</tmpl:insert>
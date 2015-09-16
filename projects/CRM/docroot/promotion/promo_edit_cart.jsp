<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import="com.freshdirect.fdstore.promotion.*" %>
<%@ page import="com.freshdirect.fdstore.promotion.management.*" %>
<%@ page import="java.util.*" %>
<tmpl:insert template='/template/top_nav.jsp'>
<% String promoId = request.getParameter("promoId");%>
<fd:GetPromotionNew id="promotion" promotionId="<%=promoId%>">
	<tmpl:put name='title' direct='true'>Edit Cart Requirement</tmpl:put>
	
	<tmpl:put name='content' direct='true'>
	<crm:GetCurrentAgent id='currentAgent'>
		<%
		String successPage ="/promotion/promo_edit.jsp?promoId=";
		%>
		<fd:PromotionOfferController result="result" promotion="<%= promotion %>" actionName="promoCart" successPage="<%= successPage %>">
		<%@ include file="/includes/promotions/i_promo_nav.jspf" %>
		
		<form method='POST' name="frmPromoCartReq" id="frmPromoCartReq">
		<%-- Top-Row, page specific nav --%>
		<div class="promo_page_header">
			<br />
			<table width="100%">
				<tr>
					<td class="promo_page_header_text">Edit&nbsp;Cart&nbsp;Requirement&nbsp;</td>
					<td width="90%" class="gray10pt">
						<%@ include file="/includes/promotions/i_promo_edit_top.jspf" %>
					</td>
					<td>
						<img width="300" height="0" src="/media_stat/crm/images/clear.gif" alt="" />
						<input type="submit" value="SAVE CHANGES" onclick="return checkBatch()" class="promo_btn_brn fleft noMargLR w130px" />
						<input type="button" class="promo_btn_wht fright noMargL w150px" onclick="this.form.reset()" value="UNDO ALL CHANGES" />
					</td>
					<td width="1%"><img width="16" height="0" src="/media_stat/crm/images/clear.gif" alt="" /></td>
				</tr>
			</table>
		</div>
		<div class="promo_page_content-view_content noBorder">
			<div class="padLR4">
				<div class="errContainer">
					<fd:ErrorHandler result="<%=result%>" name="OrderSubtotalNumber" id="errorMsg">
						<%@ include file="/includes/i_error_messages.jspf" %>   
					</fd:ErrorHandler>
					<fd:ErrorHandler result="<%=result%>" name="dcpdSubtotalNumber" id="errorMsg">
						<%@ include file="/includes/i_error_messages.jspf" %>   
					</fd:ErrorHandler>
					<fd:ErrorHandler result="<%=result%>" name="invalidDepts" id="errorMsg">
						 <%@ include file="/includes/i_error_messages.jspf" %>   
					</fd:ErrorHandler>
					<fd:ErrorHandler result="<%=result%>" name="invalidCats" id="errorMsg">
						 <%@ include file="/includes/i_error_messages.jspf" %>   
					</fd:ErrorHandler>
					
					<fd:ErrorHandler result="<%=result%>" name="invalidSKUs" id="errorMsg">
						 <%@ include file="/includes/i_error_messages.jspf" %>   
					</fd:ErrorHandler>
					<fd:ErrorHandler result="<%=result%>" name="invalidBrands" id="errorMsg">
						 <%@ include file="/includes/i_error_messages.jspf" %>   
					</fd:ErrorHandler>
					<fd:ErrorHandler result="<%=result%>" name="invalidExcludeSkus" id="errorMsg">
						 <%@ include file="/includes/i_error_messages.jspf" %>
					</fd:ErrorHandler>
					<fd:ErrorHandler result="<%=result%>" name="minQtyError" id="errorMsg">
						 <%@ include file="/includes/i_error_messages.jspf" %>
					</fd:ErrorHandler>
				</div>
				<%-- Promotion edit, edit cart requirement --%>
					<%@ include file="/includes/promotions/i_promo_edit_cartreq.jspf" %>
			</div>
		</div>
		</form>
		</fd:PromotionOfferController>
	</crm:GetCurrentAgent>
	</tmpl:put>
	</fd:GetPromotionNew>
</tmpl:insert>
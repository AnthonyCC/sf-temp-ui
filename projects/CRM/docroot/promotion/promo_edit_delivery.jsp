<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.fdstore.promotion.*" %>
<%@ page import="com.freshdirect.fdstore.promotion.management.*" %>
<%@ page import="com.freshdirect.delivery.model.*" %>
<tmpl:insert template='/template/top_nav.jsp'>
<% String promoId = request.getParameter("promoId");%>

<fd:GetPromotionNew id="promotion" promotionId="<%=promoId%>">
	<tmpl:put name='title' direct='true'>Edit Delivery Requirement</tmpl:put>
	
	<tmpl:put name='content' direct='true'>
	<crm:GetCurrentAgent id='currentAgent'>
		<%
		String successPage ="/promotion/promo_edit.jsp?promoId="+promoId;
		%>
		<fd:PromotionDlvReqController result="result " promotion="<%= promotion %>" successPage="<%= successPage %>">
		
		<%@ include file="/includes/promotions/i_promo_nav.jspf" %>
		
		<% //quick fix because of broken build : availableDeliveryZones is alreday declared in previous include file %>
		<% //List availableDeliveryZones  = (List)pageContext.getAttribute("availableDeliveryZones"); %>
		<form method='POST' name="frmPromoDlvReq" id="frmPromoDlvReq">
		<%-- Top-Row, page specific nav --%>
		<div class="promo_page_header">
			<br />
			<table width="100%">
				<tr>
					<td class="promo_page_header_text">Edit&nbsp;Delivery&nbsp;Requirement&nbsp;</td>
					<td width="90%" class="gray10pt">
						<%@ include file="/includes/promotions/i_promo_edit_top.jspf" %>
					</td>
					<td>
						<img width="300" height="0" src="/media_stat/crm/images/clear.gif" alt="" />
						<input type="submit" value="SAVE CHANGES" onclick="selectZones()" class="promo_btn_brn fleft noMargLR w130px" />
						<input type="button" class="promo_btn_wht fright noMargL w150px" onclick="this.form.reset()" value="UNDO ALL CHANGES" />
					</td>
					<td width="1%"><img width="16" height="0" src="/media_stat/crm/images/clear.gif" alt="" /></td>
				</tr>
			</table>
		</div>
		<%-- Promotion edit, edit delivery requirement --%>
		<div class="promo_page_content-view_content4 noBorder">
			<div class="errContainer">
				<fd:ErrorHandler result="<%=result%>" name="addressTypeEmpty" id="errorMsg">
					<%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="startDateGreater" id="errorMsg">
					<%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="daysEmpty" id="errorMsg">
					<%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="zonesEmpty" id="errorMsg">
					<%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="zipCodesEmpty" id="errorMsg">
					<%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="zipCodeTypeEmpty" id="errorMsg">
					<%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="zip_length" id="errorMsg">
					<%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="zip_pattern" id="errorMsg">
					<%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="timeslotFormatError" id="errorMsg">
					<%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
				<fd:ErrorHandler result="<%=result%>" name="timeslotError" id="errorMsg">
					<%@ include file="/includes/i_error_messages.jspf" %>   
				</fd:ErrorHandler>
			</div>
			<%@ include file="/includes/promotions/i_promo_edit_dlvreq.jspf" %>
		</div>
		</form>
		</fd:PromotionDlvReqController>
	</crm:GetCurrentAgent>
	</tmpl:put>
	</fd:GetPromotionNew>
</tmpl:insert>
<%@ taglib uri='crm' prefix='crm' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ page import="com.freshdirect.smartstore.fdstore.VariantSelection" %>
<%@ page import="com.freshdirect.deliverypass.EnumDlvPassStatus" %>
<%@ page import="com.freshdirect.fdstore.promotion.EnumPromotionProfileAttribute"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerManager"%>
<%@ page import='com.freshdirect.webapp.crm.security.*' %>
<%
	//fetch profiles
	Map profileAttributeNames = FDCustomerManager.loadProfileAttributeNames();
	//sort them
	List<String> profileAttributeNamesSorted = new ArrayList<String>(profileAttributeNames.keySet());
	Collections.sort(profileAttributeNamesSorted);
%>
<tmpl:insert template='/template/top_nav.jsp'>
	<% String promoId = request.getParameter("promoId");
	String userId = CrmSecurityManager.getUserName(request);%>
	<fd:GetPromotionNew id="promotion" promotionId="<%=promoId%>">
	<tmpl:put name='title' direct='true'>Edit Customer Requirement</tmpl:put>
	
	<tmpl:put name='content' direct='true'>
	<%
	String successPage ="/promotion/promo_edit.jsp?promoId="+promoId;
	%>
	
	<fd:PromotionCustReqController result="result" promotion="<%= promotion %>" actionName="promoCustReq" successPage="<%= successPage %>">
		<%@ include file="/includes/promotions/i_promo_nav.jspf" %>
		
		<form method='POST' id="frmPromoCustReq" name="frmPromoCustReq" id="frmPromoCustReq">
		<%-- Top-Row, page specific nav --%>
			<div class="promo_page_header">
				<br />
				<table width="100%">
					<tr>
						<td class="promo_page_header_text">Edit&nbsp;Customer&nbsp;Requirement&nbsp;</td>
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
			<div class="promo_page_content-view_content4 noBorder">
				<div class="padLR4">
					<div class="errContainer">
						<fd:ErrorHandler result="<%=result%>" name="orderRangesStart" id="errorMsg">
							<%@ include file="/includes/i_error_messages.jspf" %>   
						</fd:ErrorHandler>
						<fd:ErrorHandler result="<%=result%>" name="orderRangesEnd" id="errorMsg">
							<%@ include file="/includes/i_error_messages.jspf" %>   
						</fd:ErrorHandler>
						<fd:ErrorHandler result="<%=result%>" name="orderRanges" id="errorMsg">
							<%@ include file="/includes/i_error_messages.jspf" %>   
						</fd:ErrorHandler>
						<fd:ErrorHandler result="<%=result%>" name="dpStartDateFormat" id="errorMsg">
							<%@ include file="/includes/i_error_messages.jspf" %>   
						</fd:ErrorHandler>
						<fd:ErrorHandler result="<%=result%>" name="dpEndDateFormat" id="errorMsg">
							<%@ include file="/includes/i_error_messages.jspf" %>   
						</fd:ErrorHandler>
						<fd:ErrorHandler result="<%=result%>" name="dpDates" id="errorMsg">
							<%@ include file="/includes/i_error_messages.jspf" %>   
						</fd:ErrorHandler>
					</div>
					<%-- Promotion edit, customer requirements --%>
					<%@ include file="/includes/promotions/i_promo_edit_custreq.jspf" %>
				</div>
			</div>
		</form>
		</fd:PromotionCustReqController>
	
	</tmpl:put>
	</fd:GetPromotionNew>
</tmpl:insert>
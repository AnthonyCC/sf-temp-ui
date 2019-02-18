<%@page import="com.freshdirect.common.context.MasqueradeContext"%>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.storeapi.content.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.crm.*" %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession" %>
<%@ page import='com.freshdirect.framework.util.FormatterUtil' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<% //expanded page dimensions
final int W_ADD_GIFTCARD_TOTAL = 970;
%>

<fd:CheckLoginStatus id="user" guestAllowed='false' recognizedAllowed='false'/>

<tmpl:insert template='/common/template/giftcard.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Gift Card : Buy in Bulk"/>
  </tmpl:put>
<%--   <tmpl:put name='title' direct='true'>FreshDirect - Gift Card : Buy in Bulk</tmpl:put> --%>
	<tmpl:put name='pageType' direct='true'>gc_add_bullk</tmpl:put>
	<tmpl:put name='content' direct='true'>
		<style>
			.W_ADD_GIFTCARD_TOTAL { width: <%= W_ADD_GIFTCARD_TOTAL %>px; }
		</style>
		<%
			//used?
			String success_page = "/gift_card/purchase/add_bulk_giftcard.jsp";
		
			MasqueradeContext mctx = user.getMasqueradeContext();
            final boolean hasCustomerCase = mctx != null && mctx.isHasCustomerCase(); // CrmSession.hasCustomerCase(session);
             
			String action_name = "addBulkSavedRecipient";
			if(null != request.getParameter("recipId") && !"".equals(request.getParameter("recipId"))) {
				action_name = "editBulkSavedRecipient";
			}
			if("GET".equals(request.getMethod())&& null != request.getParameter("deleteId") && !"".equals(request.getParameter("deleteId"))) {
				action_name = "deleteBulkSavedRecipient";
			}
            
		%>

		<fd:GiftCardController actionName='<%=action_name%>' result="result"  successPage='/gift_card/purchase/add_bulk_giftcard.jsp'>
        <%
            if(user.getGiftCart().getDeliveryAddress()!=null) {
                 UserValidationUtil.validateBulkRecipientListEmpty(request, result); 
                 user.getGiftCart().setDeliveryAddress(null);
             }          
        %>             
			<fd:ErrorHandler result='<%=result%>' field='<%=checkGiftCardForm%>'>
				<% String errorMsg=SystemMessageList.MSG_MISSING_INFO; %>	
				<%@ include file="/includes/i_error_messages.jspf" %>
			</fd:ErrorHandler>
			<% String[] checkErrorType=new String[]{"gc_amount_minimum","gc_amount_maximum"}; %>
			<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
				<%@ include file="/includes/i_error_messages.jspf" %>
			</fd:ErrorHandler>


			<table width="100%" cellspacing="0" cellpadding="0" border="0" class="gc_tableBody">
				<tr>
					<td class="text11 botBordLineBlack" width="50%">
						<span class="title18">Enter Gift Card Info</span><br />
						Provide your recipients' personal info and details of your gift.
					</td>
					<td class="botBordLineBlack" align="right" width="50%">
                       <% if(hasCustomerCase){  %> 
						<a href="/gift_card/purchase/purchase_bulk_giftcard.jsp"><img width="99" height="37" border="0" alt="continue" src="/media_stat/images/giftcards/purchase/arrow_continue.gif" /></a>
                        <% }else{ %>
							<b>(Note : You need to create case to purchase Gift Card)</b>&nbsp;
						<% } %>
					</td>
				</tr>
				<tr>
					<td colspan="2" style="padding: 2px;">
						<img style="margin: 2px 0;" width="675" height="1" border="0" alt="" src="/media_stat/images/layout/clear.gif" /><br />
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<form name="giftcard_form" id="giftcard_form" method="post">
							<input type="hidden" name="actionName" value="">
							<input type="hidden" id="deleteId" name="deleteId" value="">
							<%@ include file="/gift_card/purchase/includes/i_bulk_giftcard_fields.jspf" %>
							<%@ include file="/gift_card/purchase/includes/bulk_recipient_list.jsp" %>
						</form>
					</td>
				</tr>
				<tr>
					<td colspan="2" class="botBordLineOrange"><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" border="0" /></td>
				</tr>
				<tr>
					<td colspan="2" align="right">
						<table border="0">
							<tr>
								<% if(hasCustomerCase){  %> 
									<td width="95%" align="right">
										When all of your gift card information is set, click 'Continue' to <br />
										confirm it and choose a payment method.
									</td>
									<td valign="bottom" width="5%" align="right">
										<a href="/gift_card/purchase/purchase_bulk_giftcard.jsp"><img src="/media_stat/images/giftcards/purchase/btn_continue.gif" width="80" height="25" hspace="4" vspace="4" alt="continue" border="0"></a>
									</td>
								<% }else{ %>
									<td>
										<b>(Note : You need to create case to purchase Gift Card)</b>&nbsp;
									</td>
								<% } %>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</fd:GiftCardController>

	</tmpl:put>
</tmpl:insert>
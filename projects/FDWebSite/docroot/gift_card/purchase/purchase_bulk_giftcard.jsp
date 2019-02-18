<%@ page import='java.util.*' %>
<%@ page import="com.freshdirect.payment.*" %>
<%@ page import="com.freshdirect.payment.fraud.*" %>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.storeapi.content.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.crm.*" %>
<%@ page import='com.freshdirect.framework.util.FormatterUtil' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus id="user" guestAllowed='false' recognizedAllowed='false'/>
<%
	request.setAttribute("listPos", "CategoryNote");
%>

<tmpl:insert template='/common/template/giftcard.jsp'>
  <tmpl:put name="seoMetaTag" direct='true'>
    <fd:SEOMetaTag title="FreshDirect - Gift Card : Buy in Bulk"/>
  </tmpl:put>
<%--   <tmpl:put name='title' direct='true'>FreshDirect - Gift Card : Buy in Bulk</tmpl:put> --%>
    <tmpl:put name='content' direct='true'>

<%-- 	<jsp:include page="/includes/giftcard_nav.jsp" /> --%>

	<table width="100%" cellspacing="0" cellpadding="0" border="0" class="gc_tableBody">
		<tr>
			<td class="text11" width="675">
				<span class="title18"><img src="/media_stat/images/giftcards/purchase/purchase_gift_cards.gif" alt="purchase gift cards" /></span><br />
				Please enter your credit card information below.
			</td>
			<td width="99">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding: 2px;" class="botBordLineBlack">
				<img style="margin: 2px 0;" width="675" height="1" border="0" alt="" src="/media_stat/images/layout/clear.gif" /><br />
			</td>
		</tr>
		<tr>
			<td colspan="2" style="padding: 2px;">
				<form name="submitForm" id="submitForm" method="post">
    
                 <%
                        String action_name = "";
                        if(null != request.getParameter("deleteId") && !"".equals(request.getParameter("deleteId"))) {
                            action_name = "deleteBulkSavedRecipient";
                        } 
                %>

                <fd:GiftCardController actionName='<%=action_name%>' result='result'>
                </fd:GiftCardController>

				<%@ include file="/gift_card/purchase/includes/bulk_recipient_list.jsp" %>

				<%
					String actionName =  request.getParameter("actionName"); 
					String pageName="bulkPurchase";
					request.setAttribute("giftcard", "true");
					UserUtil.initializeBulkGiftCart(user);
				%>

				<fd:CheckoutController actionName="gc_submitGiftCardBulkOrder" result="result" successPage="/gift_card/purchase/receipt.jsp" ccdProblemPage="/gift_card/purchase/purchase_bulk_giftcard.jsp">
						<!-- error message handling here -->
						<table width="100%" cellspacing="0" cellpadding="0" border="0" class="gc_tableBody">
							<tr>
								<td width="675" class="text11">
									<fd:ErrorHandler result='<%=result%>' name='gc_order_amount_fraud' id='errorMsg'>
							            <%@ include file="/includes/i_error_messages.jspf" %>	
							        </fd:ErrorHandler>        
							        <fd:ErrorHandler result='<%=result%>' name='gc_order_count_fraud' id='errorMsg'>
							            <%@ include file="/includes/i_error_messages.jspf" %>	
							        </fd:ErrorHandler>    
							        <fd:ErrorHandler result='<%=result%>' name='address_verification_failed' id='errorMsg'>
							            <%@ include file="/includes/i_error_messages.jspf" %>	
							        </fd:ErrorHandler>
							         <fd:ErrorHandler result='<%=result%>' name='authorization_failed' id='errorMsg'>
							            <%@ include file="/includes/i_error_messages.jspf" %>	
							        </fd:ErrorHandler>
							        <fd:ErrorHandler result='<%=result%>' name='fraud_check_failed' id='errorMsg'>
							                <% 
							                StringBuffer sbErrorMsg= new StringBuffer(); 
							                sbErrorMsg.append("<br>Checkout prevented because:<br>");
							                sbErrorMsg.append(errorMsg);
							                sbErrorMsg.append("<br>");
							                errorMsg = sbErrorMsg.toString();
							                %>
							            <%@ include file="/includes/i_error_messages.jspf"%>                 
							        </fd:ErrorHandler>    
							        <fd:ErrorHandler result='<%=result%>' name='limitReached' id='errorMsg'>
							           <%@ include file="/includes/i_error_messages.jspf" %>
							        </fd:ErrorHandler>  
                                    <% String[] checkPaymentForm = {"system", "order_minimum", "payment_inadequate", "technical_difficulty", "paymentMethodList", "payment", "declinedCCD", "matching_addresses", "expiration","bil_apartment","bil_address1","cardNum"}; %>
                            		<fd:ErrorHandler result='<%=result%>' field='<%=checkPaymentForm%>' id='errorMsg'>
                            			<%@ include file="/includes/i_error_messages.jspf" %>	
		                            </fd:ErrorHandler>                                
								<fd:ErrorHandler result='<%=result%>' name='technical_difficulty' id='errorMsg'><span class="text11rbold"><%=errorMsg%></span></fd:ErrorHandler>
								</td>
							</tr>
						</table>
					<fd:PaymentMethodController actionName='<%=actionName%>' result='result'>

						<%
							boolean hasCheck = false;
							Collection paymentMethods = null;
							FDIdentity identity = null;

							if(user!=null  && user.getIdentity()!=null) {
								identity = user.getIdentity();
								paymentMethods = FDCustomerManager.getPaymentMethods(identity);	
							}

							//these booleans are used in /includes/ckt_acct/i_credit_cardfields.jspf
							// set both to false -- gift card purchase is allowed only with a credit card
							boolean isECheckRestricted = false;
							boolean isCheckEligible = false;
						%>
						<table width="100%" cellspacing="0" cellpadding="0" border="0" class="gc_tableBody">
							<tr>
								<td>
									<%
										String serviceType = "personal"; 
										String value1 = "personal"; 
										String value2 = "professional"; 
											String value1Selected = "checked";
											String value2Selected = "";
										if (value1.equals(serviceType)) {
											value1Selected = "checked";
										} else if(value2.equals(serviceType)) {
											value2Selected = "checked";
										}
									%>
									Purchase Type
									<input type="radio" class="text11" name="serviceType" id="Personal" value="<%=value1%>" <%= value1Selected %> />Personal
									<input type="radio" class="text11" name="serviceType" id="Professional" value="<%=value2%>" <%= value2Selected %> />Corporate
								</td>
							</tr>
						</table>



						<table width="100%" cellspacing="0" cellpadding="0" border="0" class="gc_tableBody">
							<tr valign="top">
								<td width="693"><img src="/media_stat/images/navigation/choose_credit_card.gif" width="135" height="9" border="0" alt="CHOOSE CREDIT CARD">&nbsp;&nbsp;&nbsp;<br />
										<img src="/media_stat/images/layout/999966.gif" alt="" width="675" height="1" border="0" vspace="3"><br />
									<font class="space2pix"><br /></font>  
								</td>   
							</tr>

							<tr valign="middle">
								<td class="text11">If you need to enter another credit card: <a class="cssbutton green small" href="/gift_card/purchase/includes/gc_add_creditcard.jsp?pageName=bulkPurchase">
									ADD NEW CARD</a>
								</td>
							</tr>
							<tr>
								<td>
									<form name=creditcard_form method="post">
									<input type="hidden" name="actionName" value="">
									<input type="hidden" name="deletePaymentId" value="">
									<% String standingOrderDependencyIds = "[]"; %>
									<%@ include file="/includes/ckt_acct/i_creditcard_select.jspf" %>
									</form>
								</td>
							</tr>
							<tr>
								<td class="botBordLineOrange">
									<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="17" border="0" /><br />
								</td>
							</tr>
							<tr>
								<td><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="8" border="0" /></td>
							</tr>
							<tr>
								<td align="right">
									<input type="image" name="form_action_name" src="/media_stat/images/giftcards/purchase/gc_submit_order.gif" width="90" height="25"  hspace="4" vspace="4" alt="continue" border="0" onclick="$('submitForm').submit();">
								</td>
							</tr>
						</table>
						<input type="hidden" name="isSubmit">
					</form>
					</fd:PaymentMethodController>
				</fd:CheckoutController>
			</td>
		</tr>
	</table>


</tmpl:put>
</tmpl:insert>
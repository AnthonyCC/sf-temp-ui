<%@page import="com.freshdirect.framework.util.StringUtil"%>
<%@ page import="com.freshdirect.fdstore.customer.FDCSContactHours" %>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='java.text.*' %>
<%@ page import="java.util.*" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri="fd-data-potatoes" prefix="potato"%>
<%@ taglib uri="https://developers.google.com/closure/templates" prefix="soy" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import='com.freshdirect.webapp.util.*' %>
<%@ page import='com.freshdirect.fdstore.customer.*'%>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='java.text.*' %>
<%@ page import='com.freshdirect.fdstore.*' %>
<%@ page import='com.freshdirect.customer.*'%>
<%@ page import='com.freshdirect.fdstore.rollout.EnumRolloutFeature'%>
<%@ page import='com.freshdirect.fdstore.rollout.FeatureRolloutArbiter'%>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='bean' prefix='bean' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="fd-data-potatoes" prefix="potato"%>

<fd:CheckLoginStatus />

<%
	DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy");
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	FDIdentity identity  = user.getIdentity();
%>

<potato:contactUs/>

<%
	String[] checkContactForm_i_contact_us = {"subject", "message", "email", "first_name", "last_name", "home_phone"};
	int orderMaxQuantity = FDStoreProperties.getOrderComplaintDropdownLimit();
%>

<style>
    .chat-message-popup{
        width: 400px;
        font-family: Verdana;
        padding: 30px;
        box-sizing: border-box;
    }
    .mm-page-overlay .chat-popup{
    	width: auto;
    }
    .chat-message-popup-header{
        font-size: 22px;
        font-weight: bold;
        padding: 0 0 24px;
    }
    .chat-message-popup-input-header{
        font-size: 14px;
        font-weight: bold;
    }
    .chat-message-popup-name .chat-message-popup-input{
        color: #333333;
        font-size: 16px;
    }
    .chat-message-popup-name .chat-message-popup-input span{
        color: #666666;
        font-size: 16px;
    }
    .chat-message-popup-phone-ext-optional{
    	font-weight: normal;
    	color: #999999;
    }
    .chat-message-popup-subject,
    .chat-message-popup-order,
    .form-group.subject-container,
    .chat-message-popup-text,
    .form-group.email,
    .form-group.message,
    .form-group.phone_number,
    .chat-message-popup .buttons-container{
        margin: 25px 0 0;
    }
    .chat-message-popup-login a{
        font-size: 14px;
        margin: 7px 0 0;
        color: #408244;
    }
    .form-group.subject-container label,
    .chat-message-popup-order label,
    .form-group.message label,
    .form-group.first_name label,
    .form-group.email label{
        display: block;
        text-align: left;
    }
    .form-group.subject-container select,
    .chat-message-popup-order select#order-field,
    .form-group.message textarea,
    .form-group.first_name input,
	.form-group.email input{
        width: 340px;
        font-size: 16px;
        color: #444444;
    }

    .chat-message-popup-phone-ext{
        float: right;
    }
    .chat-message-popup-phone-ext-opt{
        font-weight: normal;
        color: #666666;
    }
    .form-group.phone_number #home_phone{
    	width: 216px;
    }
    .form-group.phone_number #home_phone_ex{
        width: 100px;
		margin: 0 0 0 20px;
    }
    .chat-message-popup-order span{
        float: right;
        width: 95px;
    }
    .chat-message-popup .buttons-container button{
        font-size: 18px;
        font-family: Verdana;
        text-shadow: 1px 1px 0 #3f8045;
        width: 340px;
        height: 48px;
        margin: 0;
    }    
    .chat-message-popup-text{
        font-size: 14px;
        text-align: center;
    }
    #contact_fd_contact{
    	padding: 0;
    }
</style>

<fd:ContactFdController result="result" successPage='/help/contact_fd_thank_you.jsp'>
<div id="help_contact_error_messages">
	<fd:ErrorHandler result='<%=result%>' name='kana' id='errorMsg'>
		<%@ include file="/includes/i_error_messages.jspf" %>
	</fd:ErrorHandler>

	<fd:ErrorHandler result='<%=result%>' field='<%=checkContactForm_i_contact_us%>'>
		<% String errorMsg = SystemMessageList.MSG_MISSING_INFO; %>
		<%@ include file="/includes/i_error_messages.jspf" %>
	</fd:ErrorHandler>

	<fd:ErrorHandler result='<%=result%>' name='technical_difficulty' id='errorMsg'>
		<%@ include file="/includes/i_error_messages.jspf" %>	
	</fd:ErrorHandler>
</div>
<div class="chat-message-popup">
    <div class="chat-message-popup-header">Send Us a Message</div>
	<form fdform method="post" name="contact_fd" id="contact_fd_contact" fdform-displayerrorafter fdform-disabled-if-missing-required fdform-v-required fdform-submit="sendNewMessage"><fieldset id="help_fieldset"><legend class="offscreen">Contact FreshDirect</legend>

		<script>
			$jq('#prodReqContent').ready(function() { $jq('#prodReqContent').hide(); });
			$jq(document).ready(function() {
				$jq('#contact_subject').change(function() {
					var selectedOpt = $jq('#contact_subject option').filter(':selected');
					if ( selectedOpt.text() == 'Product Request' ) {
                        $jq('#prodReqContent').show();
                        $jq('#prodReqNonContent').hide();
                        $jq('#test1').attr('disabled', 'disabled').addClass('disabled');
                    } else {
                        $jq('#prodReqContent').hide();
                        $jq('#prodReqNonContent').show();
                        $jq('#test1').attr('disabled', null).removeClass('disabled');
                    }
				});
				$jq('#contact_subject').change();
			});
			function sendNewMessage(){
				$jq.ajax({
				    url: '/includes/chat_message_popup.jsp',
				    type: 'POST',
				    data: { email: $jq('#email').text(),
				    		first_name: $jq("#first_name").text(),
				    		home_phone: $jq("#home_phone").text(),
				    		home_phone_ext: $jq("#home_phone_ex").text(),
				    		salePK: $jq("#order-field").val(),
				    		message: $jq("#message").val(),
				    		subject: $jq("#subject").prop('selectedIndex')-1,
				    		sendMessage: 'true'
						  },
					success: function(){
								doOverlayDialogNew("/includes/chat_message_success_popup.jsp");
					        },
					error: function(){
					        	doOverlayDialogNew("/includes/error_message_popup.jsp");
					        }
				});
			}
		</script>
		
		
		<% if (identity == null) { %>
			<div class="form-elements-wrapper main-content flex">
				<p class="help-contact-form login-link green font16 verdana-font">Have an account? <a href="/login/login.jsp">Sign in</a></p>
				<div class="form-group-name-container flex">
					<div class="form-group first_name flex">
						<label for="first_name" class="bold inline">Name</label>
						<input type="text" name="first_name" id="first_name" required class="font16" placeholder="Jane" aria-describedby="firstname_error" size="45" value="${contactUsPotato.customerData.firstName} ${contactUsPotato.customerData.lastName}" />
						<fd:ErrorHandler result='<%=result%>' name='first_name' id='errorMsg'><span id="firstname_error" class="bold error"><%=errorMsg%></span></fd:ErrorHandler>
					</div>
				</div>
				<div class="form-group email flex">	
					<label for="email" class="bold inline">E-mail</label>
					<input type="email" name="email" id="email" required class="font16" placeholder="name@website.com" aria-describedby="emailid_error" size="45" value="${contactUsPotato.customerData.email}" />
					<fd:ErrorHandler result='<%=result%>' name='email' id='errorMsg'><span id="emailid_error" class="bold error"><%=errorMsg%></span></fd:ErrorHandler>
				</div>
				<div class="form-group phone_number flex">
					<div class="flex chat-message-popup-phone-labels">
						<label for="home_phone" class="bold inline chat-message-popup-phone-main">Phone</label>
						<label for="home_phone_ext" class="bold inline chat-message-popup-phone-ext">Ext. <span class="chat-message-popup-phone-ext-optional">Optional</span><span class="offscreen">extension for phone number</span></label>
					</div>
					<div class="flex chat-message-popup-phone-inputs">
						<input type="tel" name="home_phone" id="home_phone" class="font16" placeholder="555-555-5555" size="21" min="0" value="${contactUsPotato.customerData.homePhone}" maxlength="15" />
						<input type="number" placeholder="1234" name="home_phone_ext" id="home_phone_ex" class="font16" size="4" min="0" value="${contactUsPotato.customerData.homePhoneExt}" maxlength="6" />
					</div>
				</div>
			</div>
		<% }else{ %>
				<div class="form-group customer-name-container flex">
					<label class="bold">Customer Name</label>
					<div class="customer-name customer-name-container font16 verdana-font"> <span id="first_name">${contactUsPotato.customerData.firstName} ${contactUsPotato.customerData.lastName}</span><span class="customer-email">(<span id="email">${contactUsPotato.customerData.email}</span>)</span></div>
				</div>
				<div class="form-group customer-name-container login font16 flex">
					<label class="login-label">Not ${contactUsPotato.customerData.firstName}? <a class="login-link" href="/login/login.jsp">Sign in</a></label>
				</div>
				<input type="hidden" name="home_phone" id="home_phone" class="font16" placeholder="555-555-5555" size="21" min="0" value="${contactUsPotato.customerData.homePhone}" maxlength="15" />
				<input type="hidden" placeholder="1234" name="home_phone_ext" id="home_phone_ex" class="font16" size="4" min="0" value="${contactUsPotato.customerData.homePhoneExt}" maxlength="6" />
		<% } %>
		<div class="form-group subject-message-container flex">
			<div class="form-group subject-container flex">
				<label for="subject" class="bold inline text-right">Subject</label>
				<div class="select-wrapper">
					<select class="customsimpleselect" aria-describedby="subject_error" name="subject" id="subject" required onchange="">
						<option value="">Select Subject</option>
						<logic:iterate id="subject" indexId="idx" collection="<%= ContactFdControllerTag.selections %>" type="com.freshdirect.webapp.taglib.fdstore.ContactFdControllerTag.Selection">
							<option value="<%= idx %>" <%= idx.intValue() == (Integer)((Map<String, Object>)((Map<String, Object>)pageContext.getAttribute("contactUsPotato")).get("customerData")).get("subjectIndex") ? "selected" : "" %>><%= subject.getDescription() %></option>
						</logic:iterate>
					</select>
				</div>
				<div id="subject_error" class="bold error">
					<fd:ErrorHandler result='<%=result%>' name='subject' id='errorMsg'>
						<%--if error occured with subject selection, tracking is needed, because subject will be changed automatically--%>
						<%=errorMsg%>
					</fd:ErrorHandler>
				</div>
			</div>
			
			<% if (identity != null) { %>
				<div class="chat-message-popup-order">
                    <fd:OrderHistoryInfo id='orderHistoryInfo'>
                        <%if (orderHistoryInfo.size() > 0) {%>
                            <label for="order-field" class="bold inline text-right">Order Number: <span class="optionalaccess">(optional)</span></label>
                            <div class="select-wrapper">
                                <select class="customsimpleselect" name="salePK" id="order-field">
                                    <option value="">Select an Order</option>
                                    <logic:iterate id="orderInfo" indexId="idx" collection="<%= orderHistoryInfo %>" type="com.freshdirect.fdstore.customer.FDOrderInfoI">
                                        <% if (idx.intValue() == orderMaxQuantity) break; %>
                                        <option value="<%= orderInfo.getErpSalesId() %>">#<%= orderInfo.getErpSalesId() %> - <%=orderInfo.getOrderStatus().getDisplayName()%> - <%= dateFormatter.format( orderInfo.getRequestedDate() ) %></option>
                                    </logic:iterate>
                                </select>
                            </div>
                        <% } %>
                    </fd:OrderHistoryInfo>
                </div>
			<% } %>

			<div class="form-group message flex">
				<label for="message" id="msg-textarea-label" class="bold inline">How can we help you?</label>
				<textarea id="message" required class="font16" placeholder="Please be specific" cols="45" aria-describedby="message_error" rows="5" name="message" >${contactUsPotato.customerData.body}</textarea>
				<div id="message_error" class="bold error">
					<fd:ErrorHandler result='<%=result%>' name='message' id='errorMsg'>
						<%=errorMsg%>
					</fd:ErrorHandler>
				</div>
			</div>
		</div>


		<div class="separator buttons-container center flex">
			<button type="submit" id="test1" name="sendMessage" class="cssbutton green" >Send Message</button>
		</div>
	</fieldset>
	</form>
	<div class="chat-message-popup-text">We generally respond within 1 to 3 hours during our business day.</div>
</div>
</fd:ContactFdController>
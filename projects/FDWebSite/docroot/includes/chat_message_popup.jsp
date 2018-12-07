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
<%@ page import="com.freshdirect.framework.util.StringUtil"%>
<%@ page import='com.freshdirect.fdstore.FDStoreProperties' %>
<%@ page import='java.text.*' %>
<%@ page import="java.util.*" %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='logic' prefix='logic' %>
    
<%
	DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yy");
	
	FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);
	FDIdentity identity  = user.getIdentity();
        ErpCustomerInfoModel cm = null;
        if(identity != null){
            cm = FDCustomerFactory.getErpCustomerInfo(identity);
	}

    
%>		

<%
int subjectIndex = -1;
String email = "";
String firstName = "";
String lastName = "";
String homePhone = "";
String homePhoneExt = "";
String workPhone = "";
String workPhoneExt = "";
String altPhone = "";
String altPhoneExt = "";
String body = "";

if (cm != null) {
	email = cm.getEmail();
	lastName = cm.getLastName();
	firstName = cm.getFirstName();
	homePhone = cm.getHomePhone()==null?"":cm.getHomePhone().getPhone();
	homePhoneExt = cm.getHomePhone()==null?"":cm.getHomePhone().getExtension();
	altPhone = cm.getOtherPhone()==null?"":cm.getOtherPhone().getPhone();
	altPhoneExt = cm.getOtherPhone()==null?"":cm.getOtherPhone().getExtension();
}

if(request.getParameter("subject")!=null){
	try {
		subjectIndex = Integer.parseInt(request.getParameter("subject"));
	} catch (NumberFormatException Ex){
		subjectIndex = 0;
	}
}
if(request.getParameter("email")!=null){
	email=request.getParameter("email");
}
if(request.getParameter("first_name")!=null){
	firstName=request.getParameter("first_name");
}
if(request.getParameter("last_name")!=null){
	lastName=request.getParameter("last_name");
}
if(request.getParameter("home_phone")!=null){
	homePhone=request.getParameter("home_phone");
}
if(request.getParameter("home_phone_ext")!=null){
	homePhoneExt=request.getParameter("home_phone_ext");
}
if(request.getParameter("work_phone")!=null){
	workPhone=request.getParameter("work_phone");
}
if(request.getParameter("work_phone_ext")!=null){
	workPhoneExt=request.getParameter("work_phone_ext");
}
if(request.getParameter("alt_phone")!=null){
	altPhone=request.getParameter("alt_phone");
}
if(request.getParameter("alt_phone_ext")!=null){
	altPhoneExt=request.getParameter("alt_phone_ext");
}
if(request.getParameter("message")!=null){
	body=request.getParameter("message");
}

String successPage = "/help/contact_fd_thank_you.jsp";
String overlay = "false";
if(request.getParameter("overlay")!=null){
	overlay=request.getParameter("overlay");
}

boolean mobWeb = FeatureRolloutArbiter.isFeatureRolledOut(EnumRolloutFeature.mobweb, user) && JspMethods.isMobile(request.getHeader("User-Agent"));
String pageTemplate = "/common/template/dnav.jsp";
if (mobWeb) {
	pageTemplate = "/common/template/mobileWeb.jsp"; //mobWeb template
	String oasSitePage = (request.getAttribute("sitePage") != null) ? request.getAttribute("sitePage").toString() : "www.freshdirect.com/help/contact_fd.jsp";
	if (oasSitePage.startsWith("www.freshdirect.com/") && !oasSitePage.startsWith("www.freshdirect.com/mobileweb/")) {
		request.setAttribute("sitePage", oasSitePage.replace("www.freshdirect.com/", "www.freshdirect.com/mobileweb/")); //change for OAS	
	}
}

if ("true".equalsIgnoreCase(overlay)) {
	pageTemplate = "/common/template/no_nav_html5.jsp"; //instead of mobWeb template
	successPage += "?overlay=true";
}

%>

<%
	String[] checkContactForm_i_contact_us = {"subject", "message", "email", "first_name", "last_name", "home_phone"};
	String faqSections_i_contact_us = FDStoreProperties.getFaqSections();
	String csNumberMedia_i_contact_us = user.getCustomerServiceContactMediaPath();
	int orderMaxQuantity = FDStoreProperties.getOrderComplaintDropdownLimit();
%>


<style>
    .chat-message-popup{
        width: 400px;
        font-family: Verdana;
        padding: 30px;
        box-sizing: border-box;
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
    .chat-message-popup-name{
        
    }
    .chat-message-popup-email{
        
    }
    .chat-message-popup-phone{
        
    }
    .chat-message-popup-phone-ext{
        
    }
    .chat-message-popup-subject,
    .chat-message-popup-order,
    .chat-message-popup-message,
    button.chat-message-popup-send,
    .chat-message-popup-text,
    .chat-message-popup-email,
    .chat-message-popup-phone{
        margin: 25px 0 0;
    }
    .chat-message-popup-login a{
        font-size: 14px;
        margin: 7px 0 0;
        color: #408244;
    }
    .chat-message-popup-subject label,
    .chat-message-popup-order label,
    .chat-message-popup-message label,
    .chat-message-popup-name label,
    .chat-message-popup-email label{
        display: block;
        text-align: left;
    }
    .chat-message-popup-subject select,
    .chat-message-popup-order select#order-field,
    .chat-message-popup-message textarea,
    .chat-message-popup-name input,
    .chat-message-popup-email input{
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
    .chat-message-popup-order span{
        float: right;
        width: 95px;
    }
    button.chat-message-popup-send{
        font-size: 18px;
        font-family: Verdana;
        text-shadow: 1px 1px 0 #3f8045;
        width: 340px;
        height: 48px;
    }
    .chat-message-popup-text{
        font-size: 14px;
        text-align: center;
    }
</style>

<fd:ContactFdController result="result" successPage='/help/contact_fd_thank_you.jsp'>
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
        
    <div class="chat-message-popup">
        <div class="chat-message-popup-header">Send Us a Message</div>
        <form fdform method="post" name="contact_fd" id="contact_fd_contact" fdform-displayerrorafter>
            <% if (identity != null) { %>
                <div class="chat-message-popup-name">
                    <label class="chat-message-popup-input-header">Customer Name</label>
                    <div class="chat-message-popup-input"><%=firstName%> <%=lastName%> <span>(<%=email%>)</span></div>
                </div>
                
                <div class="chat-message-popup-login">
                    <a href="#" fd-login-nosignup fd-login-successpage-current>Not <%=firstName%>? Sign In</a>
                </div>
                
                <div class="chat-message-popup-subject">
                    <label for="subject" class="bold inline text-right">Subject</label>
                    <div class="select-wrapper">
                        <select class="customsimpleselect" aria-describedby="subject_error" name="subject" id="subject" onchange="">
                            <option value="">Select Subject:</option>
                            <logic:iterate id="subject" indexId="idx" collection="<%= ContactFdControllerTag.selections %>" type="com.freshdirect.webapp.taglib.fdstore.ContactFdControllerTag.Selection">
                                <option value="<%= idx %>" <%= idx.intValue() == subjectIndex ? "selected" : "" %>><%= subject.getDescription() %></option>
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

            <% } else { %>

            <div class="chat-message-popup-login">
                <a href="#" fd-login-required fd-login-nosignup fd-login-successpage-current>Have an account? Sign in</a>
            </div>
                
            <div class="chat-message-popup-name">
                <label for="first_name" class="bold inline">Name</label>
                <input type="text" name="first_name" id="first_name" aria-describedby="firstname_error" value="<%=firstName%>" />
                <fd:ErrorHandler result='<%=result%>' name='first_name' id='errorMsg'><span id="firstname_error" class="bold error"><%=errorMsg%></span></fd:ErrorHandler>
            </div>
            <div class="chat-message-popup-email">
                <label for="email" class="bold inline">Email</label>
                <input type="text" name="email" id="email" aria-describedby="emailid_error" size="34" value="<%=email%>" />
                <fd:ErrorHandler result='<%=result%>' name='email' id='errorMsg'><span id="emailid_error" class="bold error"><%=errorMsg%></span></fd:ErrorHandler>
            </div>
            <div class="chat-message-popup-phone">
                <div class="chat-message-popup-phone-labels">
                    <label for="home_phone" class="chat-message-popup-phone-main">Phone</label>
                    <label for="home_phone_ext" class="chat-message-popup-phone-ext">Ext. <span class="chat-message-popup-phone-ext-opt">Optional</span><span class="offscreen">extension for home phone number</span></label>
                </div>
                <div class="chat-message-popup-phone-inputs">
                    <input type="text" name="home_phone" id="home_phone" size="21" value="<%=homePhone%>" maxlength="15" />
                    <input type="text" name="home_phone_ext" id="home_phone_ex" size="4" value="<%=homePhoneExt%>" maxlength="6" />
                </div>
            </div>
            <div class="chat-message-popup-subject">
                <label for="subject" class="bold inline text-right">Subject</label>
                <div class="select-wrapper">
                    <select class="customsimpleselect" aria-describedby="subject_error" name="subject" id="subject" onchange="">
                        <option value="">Select Subject:</option>
                        <logic:iterate id="subject" indexId="idx" collection="<%= ContactFdControllerTag.selections %>" type="com.freshdirect.webapp.taglib.fdstore.ContactFdControllerTag.Selection">
                            <option value="<%= idx %>" <%= idx.intValue() == subjectIndex ? "selected" : "" %>><%= subject.getDescription() %></option>
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

            <% } %>

            <div class="chat-message-popup-message">
                <label for="message" id="msg-textarea-label" class="bold inline">How can we help you?</label>
                <textarea id="message" cols="40" aria-describedby="message_error" rows="5" name="message" onKeyPress="limitText(this, 2048)" onChange="limitText(this, 2048)"><%= body%></textarea>
                <div id="message_error" class="bold error">
                    <fd:ErrorHandler result='<%=result%>' name='message' id='errorMsg'><%=errorMsg%></fd:ErrorHandler>
                </div>
            </div>

            <button class="chat-message-popup-send cssbutton cssbutton-flat green nontransparent">Send Message</button>
        </form>
        <div class="chat-message-popup-text">We generally respond within 1 to 3 hours during our business day.</div>
    </div>
</fd:ContactFdController>

<script>
</script>

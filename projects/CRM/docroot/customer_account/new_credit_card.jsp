<%@ page import="java.util.Calendar,
                 com.freshdirect.customer.ErpPaymentMethodI,
                 com.freshdirect.payment.PaymentManager"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.PaymentMethodName" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.AddressName"%>
<%@ page import="com.freshdirect.common.customer.EnumCardType"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SystemMessageList"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName"%>

<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri="crm" prefix="crm" %>
<%@ taglib uri="freshdirect" prefix="fd" %>

<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>Account Details > Add Credit Card</tmpl:put>
	
	<tmpl:put name="header" direct="true"><jsp:include page="/includes/customer_header.jsp" /></tmpl:put>

    	<tmpl:put name='content' direct='true'>
			<crm:GetFDUser id="user">
	            <%
	                ErpPaymentMethodI paymentMethod = PaymentManager.createInstance();  // defaulting to ErpCreditCardModel 
	                String actionName = "addPaymentMethod";
	                String retPage = request.getParameter("returnPage");
	                if (retPage == null) {
	                	retPage = "/main/account_details.jsp";
	                }
	            %>
				<crm:CrmPaymentMethodController paymentMethod="<%=paymentMethod%>" result="result" actionName="<%=actionName%>" successPage="<%=retPage%>">
<% 
if (!result.hasError("payment_method_fraud") && !result.hasError("technical_difficulty")) {%>
<fd:ErrorHandler result='<%=result%>' field='<%=listOfFields%>'>
<% String errorMsg= SystemMessageList.MSG_MISSING_INFO; 
	if( result.hasError("auth_failure") ) {
		errorMsg=result.getError("auth_failure").getDescription();
	} %>	
	<%@ include file="/includes/i_error_messages.jspf" %>
</fd:ErrorHandler>
<%} else {%>
	<fd:ErrorHandler result='<%=result%>' field='<%=checkErrorType%>' id='errorMsg'>
	
		<%@ include file="/includes/i_error_messages.jspf" %>
	</fd:ErrorHandler>

<%} %>
<%@ include file="/includes/credit_card_details.jspf" %>
				</crm:CrmPaymentMethodController>
			</crm:GetFDUser>
		<br clear="all">
	    </tmpl:put>

</tmpl:insert>
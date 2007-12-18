<%@ page import="java.util.Calendar,
                 com.freshdirect.customer.ErpPaymentMethodI,
                 com.freshdirect.customer.ErpPaymentMethodModel"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.PaymentMethodName" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.AddressName"%>
<%@ page import="com.freshdirect.common.customer.EnumCardType"%>

<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri="crm" prefix="crm" %>
<%@ taglib uri="freshdirect" prefix="fd" %>

<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>Account Details > Edit Credit Card</tmpl:put>

	<tmpl:put name="header" direct="true"><jsp:include page="/includes/customer_header.jsp" /></tmpl:put>
		
	<tmpl:put name='content' direct='true'>
    <%
		String paymentId = request.getParameter("paymentId");
		String actionName = "editPaymentMethod";
	    String retPage = request.getParameter("returnPage");
	    if (retPage == null) {
	    	retPage = "/main/account_details.jsp";
	    }
    %>
	<crm:GetFDUser id="user">
		<crm:CrmGetPaymentMethod id="paymentMethod" paymentId="<%=paymentId%>" user="<%=user%>">
			<crm:CrmPaymentMethodController paymentMethod="<%=paymentMethod%>" result="result" actionName="<%=actionName%>" successPage="<%=retPage%>">
            	<%@ include file="/includes/credit_card_details.jspf" %>
			</crm:CrmPaymentMethodController>
        </crm:CrmGetPaymentMethod>
	</crm:GetFDUser>
	<br clear="all">
	</tmpl:put>

</tmpl:insert>
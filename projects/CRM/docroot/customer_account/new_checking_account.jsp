<%@ page import="java.util.Calendar,
                 com.freshdirect.customer.ErpPaymentMethodI,
                 com.freshdirect.payment.PaymentManager,
                 com.freshdirect.payment.EnumPaymentMethodType"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.PaymentMethodName" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.AddressName"%>
<%@ page import="com.freshdirect.common.customer.EnumCardType"%>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.PaymentMethodUtil" %>
<%@ page import="com.freshdirect.fdstore.customer.FDIdentity" %>

<%@ taglib uri="template" prefix="tmpl" %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri="crm" prefix="crm" %>
<%@ taglib uri="freshdirect" prefix="fd" %>

<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>Account Details > Add Checking Account</tmpl:put>
	
	<tmpl:put name="header" direct="true"><jsp:include page="/includes/customer_header.jsp" /></tmpl:put>

    	<tmpl:put name='content' direct='true'>
			<crm:GetFDUser id="user">
	            <%
	                ErpPaymentMethodI paymentMethod = PaymentManager.createInstance(EnumPaymentMethodType.ECHECK); 
	                String actionName = "addPaymentMethod";
	                FDIdentity identity = user.getIdentity();
	                String retPage = request.getParameter("returnPage");
	                if (retPage == null) {
	                	retPage = "/main/account_details.jsp";
	                }
	            %>
				<crm:CrmPaymentMethodController paymentMethod="<%=paymentMethod%>" result="result" actionName="<%=actionName%>" successPage="<%=retPage%>">
	            	<%@ include file="/includes/checking_account_details.jspf" %>
				</crm:CrmPaymentMethodController>
			</crm:GetFDUser>
		<br clear="all">
	    </tmpl:put>

</tmpl:insert>
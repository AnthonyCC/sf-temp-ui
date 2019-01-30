<%@ page import='java.util.*' %>
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
			
		<br clear="all">
	    </tmpl:put>

</tmpl:insert>
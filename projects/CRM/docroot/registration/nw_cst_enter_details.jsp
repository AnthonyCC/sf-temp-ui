<%@ page import="com.freshdirect.common.address.AddressModel" %>
<%@ page import="com.freshdirect.common.customer.EnumCardType" %>
<%@ page import="com.freshdirect.common.customer.EnumServiceType" %>
<%@ page import="com.freshdirect.customer.EnumDeliverySetting" %>
<%@ page import="com.freshdirect.delivery.*" %>
<%@ page import="com.freshdirect.delivery.depot.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.promotion.*" %>
<%@ page import="com.freshdirect.fdstore.survey.*" %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.payment.EnumBankAccountType" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*"%>
<%@ page import="com.freshdirect.webapp.util.AccountUtil" %>
<%@ page import="com.freshdirect.webapp.util.JspMethods" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
	String serviceType = request.getParameter("serviceType");
%>

<tmpl:insert template='/template/top_nav.jsp'>

	<tmpl:put name='title' direct='true'>New Customer > Enter Details</tmpl:put>
	<tmpl:put name='content' direct='true'>

	<% FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER); %>
	
	<fd:RegistrationController actionName="register" successPage='/main/account_details.jsp' result='result' fraudPage='/registration/proceed_w_caution.jsp'>
	<%=serviceType%>
		<form name="registration" method="POST" action="nw_cst_enter_details.jsp">
			<input type="hidden" name="terms" value="true" />
			<%@ include file="/registration/includes/i_header.jspf" %>
			
			<div class="content_scroll" style="padding: 0px; height: 85%;">
				<%@ include file="/registration/includes/i_name_and_contact_info.jspf" %><br />
				<%@ include file="/registration/includes/i_delivery_address_info.jspf" %><br />
				<%@ include file="/registration/includes/i_payment_info.jspf" %><br />
			</div>
		</form>
	</fd:RegistrationController>
	</tmpl:put>

</tmpl:insert>

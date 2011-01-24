<%@ page import='java.text.*, java.util.*' %>

<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.crm.CrmAgentModel" %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.adapter.FDInvoiceAdapter"%>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.framework.core.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.JspLogger" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.payment.EnumPaymentMethodType" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%@ page import='com.freshdirect.crm.*' %>
<%@ page import="com.freshdirect.framework.webapp.ActionError"%>
<%@ page import='java.util.HashSet' %>

<%@ taglib uri='crm' prefix='crm' %>

<% 
// remove current cust
session.setAttribute(SessionName.USER,null); %>
<tmpl:insert template='/template/top_nav.jsp'>

<%	//
	// Get the OrderModel using the orderId from the request
	//
	String orderId = request.getParameter("orderId");
        //
        // clear any lingering search terms
        //
		session.removeAttribute(SessionName.LIST_SEARCH_RAW);
		
		CrmSession.getSessionStatus(session).setSaleId(orderId);

		FDOrderI order = FDCustomerManager.getOrder(orderId);
		
		// !!! DEBUG
		JspLogger.CC_GENERIC.debug("###### FINISHED RETREIVING ORDER FROM FDCUSTOMERMANAGER " + new java.util.Date() + " ######");
		
		FDOrderI cart = order;
		//
		// Remove and replace any existing RECENT_ , RECENT_ORDER_NUMBER in session
		//
		session.removeAttribute(SessionName.RECENT_ORDER);
		session.removeAttribute(SessionName.RECENT_ORDER_NUMBER);
		session.setAttribute(SessionName.RECENT_ORDER_NUMBER, orderId);

        FDIdentity identity = null;
		//
		// Get customer info from the order
		//
		String custId = order.getCustomerId();
        FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
        if (user == null || user.getIdentity() == null || (user.getIdentity() != null && !custId.equals(user.getIdentity().getErpCustomerPK()))) {
            FDCustomerModel _fdCustomer = FDCustomerFactory.getFDCustomerFromErpId(custId);
            identity = new FDIdentity(custId, _fdCustomer.getPK().getId()); %>
            
            <fd:LoadUser newIdentity="<%= identity %>" />
            
<%            user = (FDSessionUser) session.getAttribute(SessionName.USER); %>
<%      }
		
		// !!! DEBUG
		JspLogger.CC_GENERIC.debug("###### FINISHED RETREIVING FDCUSTOMER FROM FDCUSTOMERFACTORY " + new java.util.Date() + " ######");
%> 
    <tmpl:put name='title' direct='true'>Order <%= orderId %> Cases</tmpl:put>
		
    	<tmpl:put name='content' direct='true'>
			<% CrmCaseTemplate template = new CrmCaseTemplate();
			String agentId = CrmSecurityManager.getUserName(request);
			String agentRole = CrmSecurityManager.getUserRole(request);%>
    		
    			<% template.setSalePK(new PrimaryKey(orderId)); %>

			<crm:FindCases id='cases' template='<%= template %>'>
			
			<%@ include file="/includes/order_nav.jspf"%>	
			
			<%@ include file="/includes/order_summary.jspf"%>
			
			<%@ include file="/includes/case_list.jspf"%>

			</crm:FindCases>
			<iframe id="case_summary" name="case_summary" src="/includes/case_summary.jsp?<%=NVL.apply(request.getQueryString(),"")%>" width="100%" height="240" scrolling="no" FrameBorder="0"></iframe>
			
			
		</tmpl:put>
	
</tmpl:insert>
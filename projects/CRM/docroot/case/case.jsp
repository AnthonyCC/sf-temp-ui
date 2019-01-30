<%@ page import='java.util.Iterator' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.webapp.util.CCFormatter'%>
<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession" %>
<%@ page import="com.freshdirect.customer.ErpSaleInfo" %>
<%@ page import="com.freshdirect.customer.ErpOrderHistory" %>
<%@ page import='com.freshdirect.webapp.crm.security.CrmSecurityManager'%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ include file="/includes/i_globalcontext.jspf" %>
<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>Case Details</tmpl:put>
	<crm:GetCase id="cm" caseId='<%= request.getParameter("case") %>'>
    <%
    	String userId = CrmSecurityManager.getUserName(request);
    	String userRole = CrmSecurityManager.getUserRole(request);
    	CrmAgentRole crmRole = CrmAgentRole.getEnumByLDAPRole(userRole);
        String fdCustId = "";
        String erpCustId = "";
        if(cm.getCustomerPK() != null){
            erpCustId = cm.getCustomerPK().getId();
            fdCustId = CrmSession.getFDCustomerId(session, erpCustId);
        }
        boolean isSecurityQueue= false;
        if(null !=cm){
	    	if (!cm.isAnonymous()) {
	    		isSecurityQueue=  cm.getSubject().getQueue().getCode().equals("SEQ");
	        } else { 
	        	isSecurityQueue = "SEQ".equals(request.getParameter("queue"));
	        }
        }
        boolean isSecuredCase = isSecurityQueue || cm.isPrivateCase();
        boolean isPrivateCase = cm.isPrivateCase();
    %>
	<%-- get user put in session --%>
	
    </crm:GetCase>
</tmpl:insert>

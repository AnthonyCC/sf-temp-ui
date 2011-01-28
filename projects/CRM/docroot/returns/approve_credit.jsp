<%@ page import='java.text.*, java.util.*' %>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.adapter.*" %>
<%@ page import="com.freshdirect.framework.core.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.util.*" %>
<%@ page import='com.freshdirect.webapp.crm.security.*' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%  
    FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
    
    // Get the order and complaint IDs from the session
    ErpPaymentMethodI paymentMethod = null;
    String orderId = request.getParameter("orderId");
    String complaintId = request.getParameter("complaintId");
    if (complaintId == null) {
        complaintId = (String) session.getAttribute(SessionName.COMPLAINT_ID);
    } else {
        session.setAttribute(SessionName.COMPLAINT_ID, complaintId);
    }
    boolean inPopup = "true".equalsIgnoreCase(request.getParameter("inPopup"));
    
    /* REPLACE THIS FINDER LOGIC ONCE THE KANA COMMUNICATION IS HOOKED UP */

	FDOrderI order = FDCustomerManager.getOrder(orderId);    

	//  Need to do this here now because this page can be called from pending_credit_list.jsp
	if (inPopup) {
		FDIdentity identity = null;
		//
		// Get customer info from the order
		//
		String custId = order.getCustomerId();
		if (user == null || user.getIdentity() == null || (user.getIdentity() != null && !custId.equals(user.getIdentity().getErpCustomerPK()))) {
			FDCustomerModel _fdCustomer = FDCustomerFactory.getFDCustomerFromErpId(custId);
			identity = new FDIdentity(custId, _fdCustomer.getPK().getId()); %>			
        	<fd:LoadUser newIdentity="<%= identity %>" />        	
<%      }
	}
		//
	    // Get delivery address info, payment info
	    //
	    paymentMethod = order.getPaymentMethod();
%>
<%-- ~~~~~~~~~~~~~~ Get APPROVED complaints ~~~~~~~~~~~~~~ --%>
<fd:ComplaintGrabber order="<%= order %>" complaints="complaints" lineComplaints="lineComplaints" deptComplaints="deptComplaints" miscComplaints="miscComplaints" fullComplaints="fullComplaints" restockComplaints="restockComplaints" retrieveApproved="true" retrievePending="false" retrieveRejected="false">
<%-- ~~~~~~~~~~~~~~ Get PENDING complaints ~~~~~~~~~~~~~~ --%>
<fd:ComplaintGrabber order="<%= order %>" complaints="pendingComplaints" lineComplaints="pendingLineComplaints" deptComplaints="pendingDeptComplaints" miscComplaints="pendingMiscComplaints" fullComplaints="pendingFullComplaints" restockComplaints="pendingRestockComplaints" retrieveApproved="false" retrievePending="true" retrieveRejected="false">
<% 
    // Get intended action for this request
    String action = NVL.apply(request.getParameter("actionName"), "approve").trim();
    
    if (pendingComplaints.size() > 0) {
	    ErpComplaintModel pendComplaint = (ErpComplaintModel) pendingComplaints.iterator().next();
	    ActionResult createComplaintResult = new ActionResult();
	    pageContext.setAttribute("createComplaintResult", createComplaintResult);
	    String successPage = (!inPopup) ?  "/main/order_details.jsp?orderId="+orderId : "/main/close_window.jsp";
%>
<tmpl:insert template='<%=(!inPopup) ? "/template/top_nav.jsp" : "/template/large_pop.jsp"%>'>
<tmpl:put name='title' direct='true'>Order <%= orderId %> Approve Credit</tmpl:put>
<fd:IssueCredits action="<%= action %>" result="result" successPage='<%= successPage %>' complaintModel='<%= pendComplaint %>'>

<tmpl:put name='content' direct='true'>
<script language="JavaScript" type="text/javascript">
    function setThisAction(formObj, actionValue) {
        formObj.actionName.value = actionValue;
    }
</script>
<% if (!inPopup) { %>
    <%@ include file="/includes/order_nav.jspf"%>
<% } %>
<div class="sub_nav">
    <table width="100%" cellpadding="0" cellspacing="2" border="0" class="sub_nav_text">
    <form name="approve_credit" method="POST">
        <input type="hidden" name="actionName" value="">
        <input type="hidden" name="orderId" value="<%= orderId %>">
        <TR>
            <td align="center">
                <crm:GetCurrentAgent id="currentAgent">
				<%if(CrmSecurityManager.hasAccessToPage(currentAgent.getRole().getLdapRoleName(),"approveCredit")){%>
                    <input type="submit" class="submit" value="APPROVE THIS CREDIT" onClick="setThisAction(document.approve_credit,'approve');"> 
                <%}%>
                <%if(CrmSecurityManager.hasAccessToPage(currentAgent.getRole().getLdapRoleName(),"rejectCredit")){%>
                <input type="submit" class="clear" value="REJECT THIS CREDIT" onClick="setThisAction(document.approve_credit,'reject');"><br>
                <% } %>
                </crm:GetCurrentAgent>
            </td>
        </TR>
    </table>
</div>
    <fd:ErrorHandler result='<%=result%>' name='approval_error' id='errorMsg'>
        <div class="error_detail"><%= errorMsg %></div>
    </fd:ErrorHandler>
    <fd:ErrorHandler result='<%=result%>' name='technical_difficulty' id='errorMsg'>
        <div class="error_detail"><%= errorMsg %></div>
    </fd:ErrorHandler>
<%@ include file="/includes/order_summary.jspf"%>
<div class="content_scroll" style="padding: 0px; height: 68%;">
<%@ include file="/includes/i_complaint_info.jspf"%>
</form>
</div>
</tmpl:put>
</fd:IssueCredits>
</tmpl:insert>
<%
	} else {
%>
       <STRONG>Complaint is no longer pending</STRONG>
<%
	}
%>
</fd:ComplaintGrabber>
</fd:ComplaintGrabber>

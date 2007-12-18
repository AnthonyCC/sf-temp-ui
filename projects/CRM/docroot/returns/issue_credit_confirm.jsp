<%@ page import='java.text.*, java.util.*' %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.customer.adapter.*" %>
<%@ page import="com.freshdirect.framework.core.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.webapp.taglib.crm.CrmSession" %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%FDUserI user = (FDUserI)session.getAttribute(SessionName.USER);%>

<tmpl:insert template='/template/top_nav.jsp'>
<%
    String orderId = (String) request.getParameter("orderId");
    FDOrderI order = CrmSession.getOrder(session, orderId, true);
    ErpPaymentMethodI paymentMethod = order.getPaymentMethod();
%>
<%-- ~~~~~~~~~~~~~~ Get APPROVED complaints ~~~~~~~~~~~~~~ --%>
<fd:ComplaintGrabber order="<%= order %>" complaints="complaints" lineComplaints="lineComplaints" deptComplaints="deptComplaints" miscComplaints="miscComplaints" fullComplaints="fullComplaints" restockComplaints="restockComplaints" retrieveApproved="true" retrievePending="false" retrieveRejected="false">
<%-- ~~~~~~~~~~~~~~ Get PENDING complaints ~~~~~~~~~~~~~~ --%>
<fd:ComplaintGrabber order="<%= order %>" complaints="pendingComplaints" lineComplaints="pendingLineComplaints" deptComplaints="pendingDeptComplaints" miscComplaints="pendingMiscComplaints" fullComplaints="pendingFullComplaints" restockComplaints="pendingRestockComplaints" retrieveApproved="false" retrievePending="true" retrieveRejected="false">

<tmpl:put name='title' direct='true'>Order <%= orderId%> Issue Credit Confirmation</tmpl:put>

<tmpl:put name='content' direct='true'>
<%@ include file="/includes/order_nav.jspf"%>
<%@ include file="/includes/order_summary.jspf"%>
<%= order.hasCreditIssued() == 2 ? "<div class=\"sub_nav\"><span class=\"error\">This credit has been submitted for supervisor approval.</span></div>" : "" %>
<div class="content_scroll" style="padding: 0px; height: 70%;">
<%@ include file="/includes/i_complaint_info.jspf"%>
</div>
</tmpl:put>
</fd:ComplaintGrabber>
</fd:ComplaintGrabber>
</tmpl:insert>

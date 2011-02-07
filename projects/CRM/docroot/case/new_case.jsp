<%@ page import="com.freshdirect.framework.core.PrimaryKey" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.SessionName" %>
<%@ page import="com.freshdirect.crm.*" %>
<%@ page import='com.freshdirect.webapp.crm.security.CrmSecurityManager'%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>
<tmpl:insert template='/template/top_nav_changed_dtd.jsp'>

	<tmpl:put name='title' direct='true'>New Case</tmpl:put>

	<tmpl:put name='content' direct='true'>
		<%
		String userId = CrmSecurityManager.getUserName(request);
    	String userRole = CrmSecurityManager.getUserRole(request);
    	CrmAgentRole crmRole = CrmAgentRole.getEnumByLDAPRole(userRole);
		CrmCaseModel cm = new CrmCaseModel();
		cm.setState(CrmCaseState.getEnum(CrmCaseState.CODE_NEW));
		cm.setOrigin(CrmCaseOrigin.getEnum(CrmCaseOrigin.CODE_PHONE));
		String custId = request.getParameter("erpCustId");
		String saleId = request.getParameter("orderId");
		if(custId != null){
			cm.setCustomerPK("".equals(custId) ? null : new PrimaryKey(custId));
		}
		if(saleId != null){
			cm.setSalePK("".equals(saleId) ? null : new PrimaryKey(saleId));
		}
		boolean isSecurityQueue= false;
		boolean isSecuredCase = false;
		boolean isPrivateCase = false;
		String lastPage = "/main/index.jsp";
			if (custId!=null && !"".equals(custId)) lastPage = "/main/case_history.jsp";
			if (saleId!=null && !"".equals(custId)) lastPage = "/main/order_details.jsp?orderId=" + saleId;
		%>
	
		<div class="case_header"><span class="case_header_title_text"><b>New Case</b>&nbsp; <%= custId!=null && !"".equals(custId) ? "for &nbsp;Customer # <span class=\"case_header_value\">" + custId + "</span>" : ""%><%= saleId!=null && !"".equals(custId) ? "&nbsp; & &nbsp;Order # <span class=\"case_header_value\">" + saleId + " - <a href=\"javascript:pop('/main/order_details.jsp?orderId=" + saleId + "&for=print','600','680')\" class=\"case_header_value\">Details</a></span>" : ""%></span> <a href="<%= lastPage %>" class="cancel" style="margin-left:40px; color:#FFFFFF; border-color:#FFFFFF;">CANCEL</a></div>

		<div class="case_content" <%= (session.getAttribute(SessionName.USER) != null) ? "style='height: 78%;'" : "" %>>

			<crm:GetCurrentAgent id='currAgent'>
				<% cm.setAssignedAgentPK( currAgent.getPK() ); %>
			</crm:GetCurrentAgent>
			<%@ include file="/includes/case_details.jspf" %>
			
		</div>

	</tmpl:put>

</tmpl:insert>
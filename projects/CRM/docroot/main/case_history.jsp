<%@ page import="com.freshdirect.fdstore.customer.FDUserI"%>
<%@ page import="com.freshdirect.framework.util.NVL" %>
<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.framework.core.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ page import='com.freshdirect.webapp.taglib.crm.CrmSession' %>
<%@ page import="com.freshdirect.fdstore.FDStoreProperties" %>
<%@ page import='com.freshdirect.webapp.crm.security.CrmSecurityManager' %>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='crm' prefix='crm' %>
<tmpl:insert template='/template/top_nav.jsp'>
    <tmpl:put name='title' direct='true'>Case History</tmpl:put>
        <tmpl:put name='content' direct='true'>
        <%
            CrmCaseTemplate template = new CrmCaseTemplate();
            FDUserI user = (FDSessionUser) session.getAttribute(SessionName.USER);
            template.setCustomerPK( new PrimaryKey(user.getIdentity().getErpCustomerPK()));
            template.setSortBy(request.getParameter("sortBy"));
            template.setSortOrder(request.getParameter("sortOrder"));
            template.setStartRecord(Integer.parseInt(NVL.apply(request.getParameter("startRecord"), "0")));
            template.setEndRecord(Integer.parseInt(NVL.apply(request.getParameter("endRecord"), FDStoreProperties.getCaseListLength(request.getRequestURI().indexOf("case_history.jsp")>-1))));
            boolean hasCase = false;
            String userId = CrmSecurityManager.getUserName(request);
            String userRole = CrmSecurityManager.getUserRole(request);
            
        %>
			<crm:FindCases id='cases' template='<%= template %>'>
			<table width="100%" cellpadding="0" cellspacing="0" border="0" class="sub_nav">
				<tr valign="middle">
					<td width="40%" class="sub_nav_text"></td>
					<crm:GetLockedCase id="cm">
						<% if (cm!= null) hasCase = true; %>
					</crm:GetLockedCase>
                    <td width="20%" class="sub_nav_text"><a href="/case/<%= hasCase ? "unlock" : "new_case"%>.jsp?<%= hasCase ? "redirect=new_case&" : ""%>erpCustId=<%=user.getIdentity().getErpCustomerPK()%>" class="new">NEW CASE</a></td>
					<td width="40%" align="right" class="sub_nav_text">
						<b>P</b> <span class="legend">PRIORITY:
						<img src="/media_stat/crm/images/priority_hi.gif" width="11" height="11" hspace="3">HIGH
						<img src="/media_stat/crm/images/priority_md.gif" width="11" height="11" hspace="3">MEDIUM
						<img src="/media_stat/crm/images/priority_lo.gif" width="11" height="11" hspace="3">LOW
						</span>
					</td>
				</tr>
			</table>	
			<%@ include file ="/includes/case_list.jspf"%>
			</crm:FindCases>
			<iframe id="case_summary" name="case_summary" src="/includes/case_summary.jsp" width="100%" height="280" FrameBoarder="0"></iframe>
			<br clear="all">
	    </tmpl:put>
</tmpl:insert>
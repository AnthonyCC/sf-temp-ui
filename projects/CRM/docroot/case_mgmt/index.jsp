<%@ page import='com.freshdirect.crm.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='crm' prefix='crm' %>
<%@ page import='com.freshdirect.webapp.taglib.crm.CrmSession' %>
<%@ page import='com.freshdirect.framework.util.NVL' %>
<%@ page import='com.freshdirect.webapp.util.CCFormatter'%>
<%@ page import='com.freshdirect.crm.CrmCaseState'%>
<% 
// remove current cust
session.setAttribute(SessionName.USER,null); %>

<tmpl:insert template='/template/case_mgmt.jsp'>

    <tmpl:put name='title' direct='true'>Case Management</tmpl:put>

    	<tmpl:put name='content' direct='true'>
			<crm:SearchCaseController/>
			<%
            CrmCaseTemplate template = null;
            if (request.getParameter("action") !=null && !"".equals(request.getParameter("action"))){
                template = CrmSession.getSearchTemplate(session);
            }
            else{
                template = new CrmCaseTemplate();
            }
            %>
			<crm:FindCases id='cases' template='<%= template %>'>
            	<%@ include file="/includes/case_list.jspf"%>
			<br><br>
			<iframe id="case_summary" name="case_summary" src="/includes/case_summary.jsp?<%=NVL.apply(request.getQueryString(),"")%>" width="100%" height="240" scrolling="no" FrameBorder="0"></iframe>
			<jsp:include page='/includes/case_search.jsp'/>
            </crm:FindCases>
	    </tmpl:put>

</tmpl:insert>


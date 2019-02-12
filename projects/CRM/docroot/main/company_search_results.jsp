<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.webapp.taglib.fdstore.*" %>
<%@ page import="com.freshdirect.framework.webapp.*" %>
<%@ page import="com.freshdirect.fdstore.customer.FDCustomerOrderInfo"%>
<%@ page import="com.freshdirect.crm.CrmAgentRole"%>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<%
    String sortLink = "/main/company_search_results.jsp?companyName="+NVL.apply(request.getParameter("companyName"),"").trim();
    boolean sortReversed = "true".equalsIgnoreCase(request.getParameter("reverse"));
    sortLink += (!sortReversed) ? "&reverse=true" : "";
    
%>

<tmpl:insert template='/template/top_nav.jsp'>
    <tmpl:put name='title' direct='true'>Customer Search Results</tmpl:put>
    <tmpl:put name='content' direct='true'>
<%
    boolean isGuest = false; 
    String originalPage = "/main/index.jsp";
%>
<crm:GetCurrentAgent id="currentAgent">
    <%--  isGuest = currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.GUE_CODE));  --%> 
</crm:GetCurrentAgent>

</tmpl:put>
</tmpl:insert>
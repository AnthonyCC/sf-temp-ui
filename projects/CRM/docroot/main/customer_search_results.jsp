<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdlogistics.model.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@ page import="com.freshdirect.crm.CrmAgentRole"%>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<%
    boolean sortReversed = "true".equalsIgnoreCase(request.getParameter("reverse"));
    String search = request.getParameter("search");
    
    // get search params from request object
    FDCustomerSearchCriteria sc = new FDCustomerSearchCriteria();
	sc.setFirstName(NVL.apply(request.getParameter("firstName"), "").trim());
	sc.setLastName(NVL.apply(request.getParameter("lastName"), "").trim());
	sc.setEmail(NVL.apply(request.getParameter("email"), "").trim());
	sc.setPhone(NVL.apply(request.getParameter("phone"), "").trim());
	sc.setCustomerId(NVL.apply(request.getParameter("customerId"), "").trim());
	sc.setOrderNumber(NVL.apply(request.getParameter("orderNumber"), "").trim());
    sc.setAddress(NVL.apply(request.getParameter("address"), "").trim());
	sc.setZipCode(NVL.apply(request.getParameter("zipCode"), "").trim());
	sc.setDepotCode(NVL.apply(request.getParameter("depotCode"), "").trim());
    sc.setApartment(NVL.apply(request.getParameter("apartment"), "").trim());
    sc.setSapId(NVL.apply(request.getParameter("customerSAPId"), "").trim());

	// construct the search criteria string buffer
	StringBuffer criteriaBuf = new StringBuffer();
    StringBuffer searchQuery = new StringBuffer("?search=").append(search);
	
	for(Iterator i = sc.getCriteriaMap().entrySet().iterator(); i.hasNext(); ) {
        Map.Entry e = (Map.Entry) i.next();
        criteriaBuf.append(" ").append(e.getKey()).append(": ").append("<b>").append(e.getValue()).append("</b> | ");
        searchQuery.append("&").append(StringUtils.uncapitalise(StringUtils.deleteWhitespace((String)e.getKey()))).append("=").append(e.getValue());
    }
%>

<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>Customer Search Results</tmpl:put>
    
    <tmpl:put name='content' direct='true'>
<%
boolean isGuest = false; 
%>
<crm:GetCurrentAgent id="currentAgent">
	<%-- isGuest = currentAgent.getRole().equals(CrmAgentRole.getEnum(CrmAgentRole.GUE_CODE)); --%> 
</crm:GetCurrentAgent>
<% String originalPage = "/main/main_index.jsp"; %>
<
</tmpl:put>

</tmpl:insert>
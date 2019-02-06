<%@ page import="java.util.*" %>
<%@ page import="com.freshdirect.fdstore.customer.*" %>
<%@ page import="com.freshdirect.fdstore.*" %>
<%@ page import="com.freshdirect.customer.*" %>
<%@ page import="com.freshdirect.fdlogistics.model.*" %>
<%@ page import="com.freshdirect.webapp.util.CCFormatter"%>
<%@page import="com.freshdirect.webapp.util.JspMethods"%>
<%@ page import="com.freshdirect.crm.CrmAgentRole"%>
<%@ page import="com.freshdirect.framework.util.NVL"%>
<%@ page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.text.DateFormat"%>

<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='crm' prefix='crm' %>

<%!  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); %>
<%  
    // get search params from request object
    boolean quickSearch = NVL.apply(request.getParameter("search"), "").equalsIgnoreCase("quick");
    boolean sortReversed = "true".equalsIgnoreCase(request.getParameter("reverse"));
    
    FDOrderSearchCriteria sc = new FDOrderSearchCriteria();
    sc.setFirstName(NVL.apply(request.getParameter("firstName"), "").trim());
    sc.setLastName(NVL.apply(request.getParameter("lastName"), "").trim());
    sc.setEmail(NVL.apply(request.getParameter("email"), "").trim());
    sc.setPhone(NVL.apply(request.getParameter("phone"), "").trim());
    sc.setGiftCardNumber(NVL.apply(request.getParameter("giftCardNumber"), "").trim());
    //sc.setCertificateNumber(NVL.apply(request.getParameter("certNumber"), "").trim());
    sc.setOrderNumber(NVL.apply(request.getParameter("orderNumber"), "").trim());
    sc.setDepotLocationId(request.getParameter("depotLocationId"));
    sc.setCorporate("true".equalsIgnoreCase(request.getParameter("corporate")));
	sc.setChefsTable("true".equalsIgnoreCase(request.getParameter("chefsTable")));
   
    String deliveryDate = NVL.apply(request.getParameter("deliveryDate"),"").trim();
    if(deliveryDate !=""){
        try {
            sc.setDeliveryDate(dateFormat.parse(deliveryDate));
        } catch (Exception e) { }
    }
    
    // construct the search criteria string buffer
	StringBuffer criteriaBuf = new StringBuffer();
    StringBuffer searchQuery = new StringBuffer("?search=").append(NVL.apply(request.getParameter("search"),""));
    
    for(Iterator i = sc.getCriteriaMap().entrySet().iterator(); i.hasNext(); ) {
        Map.Entry e = (Map.Entry) i.next();
        criteriaBuf.append(" ").append(e.getKey()).append(": ").append("<b>").append(e.getValue()).append("</b> | ");
        searchQuery.append("&").append(StringUtils.uncapitalise(StringUtils.deleteWhitespace((String)e.getKey()))).append("=").append(e.getValue());
    }
    
%>

<tmpl:insert template='/template/top_nav.jsp'>

    <tmpl:put name='title' direct='true'>Order Search Results</tmpl:put>

<tmpl:put name='content' direct='true'>

<%
boolean isGuest = false;
boolean isExec = false;
String guestView = "&for=print";
%>
<crm:GetCurrentAgent id="currentAgent">
     
</crm:GetCurrentAgent>
<% String originalPage = "/main/main_index.jsp"; %>


</tmpl:put>

</tmpl:insert>

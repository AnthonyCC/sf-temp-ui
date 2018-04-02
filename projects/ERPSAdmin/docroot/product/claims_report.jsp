<%@ page import='com.freshdirect.content.nutrition.*' %>
<%@ page import='com.freshdirect.erp.ErpFactory' %>
<%@ page import='java.util.*' %>
<%@ page import='com.freshdirect.webapp.util.*'%>
<%@ page import='com.freshdirect.framework.util.*'%>
<%@ page import='java.text.*'%>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='logic' prefix='logic' %>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%
    Date now = new Date();
    
    List claimsReport = ErpFactory.getInstance().generateClaimsReport();
    
    JspTableSorter sort = new JspTableSorter(request);
    
    if(sort.getSortBy() != null)
            Collections.sort(claimsReport, sort.isAscending() ? (Comparator)new MapComparator(sort.getSortBy()) : new ReverseComparator(new MapComparator(sort.getSortBy())));
            
    String contentType = "text/html";
    if("true".equalsIgnoreCase(request.getParameter("xls"))) contentType = "application/vnd.ms-excel";

    response.setContentType(contentType);
%>
<html>
    <head>
        <title>ERPSY Daisy: Claims Report</title>
        
        <STYLE TYPE="text/css">
        <!--
        TD {font-size:12px; font-family: Verdana, Arial, sans-serif;}
        -->
        </STYLE>
    </head>
    <body>
    <%if(request.getParameter("xls") == null){%>
    <a href='reports.jsp'>Back</a><br>
    <table CLASS="noBorder"><tr>
    <form method="POST" action="<%=request.getRequestURI() + "?xls=true"%>"><td CLASS="noBorder"><input class="submit" type='submit' value="Convert to excel"></td></form>
    </tr></table>
    <%}%>
    
    <h1>ERPSy Daisy Claims Report<br></h1>
    Generated <%=DateFormat.getDateInstance().format(now)%> at <%=DateFormat.getTimeInstance().format(now)%><br><br>
    <table border='0' cellspacing='0' cellpadding='4'>
        <tr>
            <td></td>
            <td><a href="?<%= sort.getFieldParams("SKUCODE") %>">SkuCode</a></td>
            <td><a href="?<%= sort.getFieldParams("FULL_NAME") %>">Full Name</td>
            <td><a href="?<%= sort.getFieldParams("UNAVAILABILITY_STATUS") %>">Unavail Status</td>
            <td><a href="?<%= sort.getFieldParams("HAS_CLAIM") %>">Has Claim</td>
            <td><a href="?<%= sort.getFieldParams("HAS_ORGANIC") %>">Has Organic</td>
        </tr>
        <tr height='1' bgColor='#000000'>
            <td colspan='15'></td>
        </tr>
        <tr height='15'>
            <td colspan='15'></td>
        </tr>
        <logic:iterate id="valuesMap" collection="<%= claimsReport %>" type="java.util.Map" indexId="index">
        <%String bgColor = index.intValue() % 2 == 0 ? "#EEEEEE" : "#FFFFFF";%>
        
        <%if(request.getParameter("xls") == null){%>
            <tr height='1' bgColor='#CCCCCC'>
                <td colspan='15'></td>
            </tr>
        <%}%>
        
        <tr valign='top' bgColor='<%=bgColor%>'>
            <td><img src='/media_stat/layout/template/clear.gif' width='1' height='50'></td>
            <td><%=valuesMap.get("SKUCODE")%></td>
            <td><%=valuesMap.get("FULL_NAME")%></td>
            <td><%=valuesMap.get("UNAVAILABILITY_STATUS")%></td>
            <td><%=valuesMap.get("HAS_CLAIM")%></td>
            <td><%=valuesMap.get("HAS_ORGANIC")%></td>
        </tr>

        </logic:iterate>
    </table>
    
    </body>
</html>

<%!
 private static class MapComparator implements Comparator {
    private String key;
    
    public MapComparator(String key){
        this.key = key;
    }
    
    public int compare(Object o1, Object o2) {
        String value1 = (String) ((Map) o1).get(key);
        String value2 = (String) ((Map) o2).get(key);
        
        if(value1 == null && value2 == null)
            return 0;
        if(value1 == null)
            return -1;
        if(value2 == null)
            return 1;
        
        return value1.compareTo(value2);
    }
 }
%>

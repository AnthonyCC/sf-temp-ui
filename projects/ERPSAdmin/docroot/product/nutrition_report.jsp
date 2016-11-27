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
    
    List nutritionReport = ErpFactory.getInstance().generateNutritionReport();
    
    JspTableSorter sort = new JspTableSorter(request);
    
    if(sort.getSortBy() != null)
            Collections.sort(nutritionReport, sort.isAscending() ? (Comparator)new MapComparator(sort.getSortBy()) : new ReverseComparator(new MapComparator(sort.getSortBy())));
            
    String contentType = "text/html";
    if("true".equalsIgnoreCase(request.getParameter("xls"))) contentType = "application/vnd.ms-excel";

    response.setContentType(contentType);
%>
<html>
    <head>
        <title>ERPSY Daisy: Nutrition Report</title>
        
        <STYLE TYPE="text/css">
        <!--
        TD {font-size:10px; font-family: Verdana, Arial, sans-serif;}
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
    
    <h1>ERPSy Daisy Nutrition Report<br></h1>
    Generated <%=DateFormat.getDateInstance().format(now)%> at <%=DateFormat.getTimeInstance().format(now)%><br><br>
    <table border='0' cellspacing='0' cellpadding='4'>
        <tr>
            <td></td>
            <td><a href="?<%= sort.getFieldParams("SKUCODE") %>">SkuCode</a></td>
            <td><a href="?<%= sort.getFieldParams("MATERIAL_NUMBER") %>">Material Number</td>
            <td><a href="?<%= sort.getFieldParams("DESCRIPTION") %>">Description</td>
            <td><a href="?<%= sort.getFieldParams("STATUS") %>">Status</td>
            <td><a href="?<%= sort.getFieldParams("SOURCE") %>">Source</td>
            <td><a href="?<%= sort.getFieldParams("HAS_NUTRITION") %>">Has Nutrition</td>
            <td><a href="?<%= sort.getFieldParams("IS_HIDDEN") %>">Is Hidden</td>
            <td><a href="?<%= sort.getFieldParams("HAS_CLAIMS") %>">Has Claims</td>
            <td><a href="?<%= sort.getFieldParams("HAS_ALLERGENS") %>">Has Allergens</td>
            <td><a href="?<%= sort.getFieldParams("HAS_ORGANIC") %>">Has Organic</td>
            <td><a href="?<%= sort.getFieldParams("KOSHER_SYMBOL") %>">Kosher Symbol</td>
            <td><a href="?<%= sort.getFieldParams("KOSHER_TYPE") %>">Kosher Type</td>
            <td>Notes</td>
            <td>Heating<img src='/media_stat/layout/template/clear.gif' width='500' height='1'></td>
        </tr>
        <tr height='1' bgColor='#000000'>
            <td colspan='15'></td>
        </tr>
        <tr height='15'>
            <td colspan='15'></td>
        </tr>
        <logic:iterate id="valuesMap" collection="<%= nutritionReport %>" type="java.util.Map" indexId="index">
        <%String bgColor = index.intValue() % 2 == 0 ? "#EEEEEE" : "#FFFFFF";%>
        <%if(request.getParameter("xls") == null){%>
            <tr height='1' bgColor='#CCCCCC'>
                <td colspan='15'></td>
            </tr>
        <%}%>
        
        <tr valign='top' bgColor='<%=bgColor%>'>
            <td><img src='/media_stat/layout/template/clear.gif' width='1' height='50'></td>
            <td><%=valuesMap.get("SKUCODE")%></td>
            <td><%=valuesMap.get("MATERIAL_NUMBER")%></td>
            <td><%=valuesMap.get("DESCRIPTION")%></td>
            <td><%=valuesMap.get("STATUS")%></td>
            <td><%=valuesMap.get("SOURCE")%></td>
            <td><%=valuesMap.get("HAS_NUTRITION")%></td>
            <td><%=valuesMap.get("IS_HIDDEN")%></td>
            <td><%=valuesMap.get("HAS_CLAIMS")%></td>
            <td><%=valuesMap.get("HAS_ALLERGENS")%></td>
            <td><%=valuesMap.get("HAS_ORGANIC")%></td>
            <td><%=valuesMap.get("KOSHER_SYMBOL")%></td>
            <td><%=valuesMap.get("KOSHER_TYPE")%></td>
            <td><%=valuesMap.get("NOTES")%></td>
            <td><%=valuesMap.get("HEATING")%></td>
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

<%@ page import='com.freshdirect.fdstore.content.*'%>
<%
String servletContext = request.getContextPath();
String pageURI = request.getRequestURI();
String folderLink = pageURI.substring(0, pageURI.lastIndexOf("/")+1) + "attributes.jsp";

//get current selected
String brandNodeId = request.getParameter("nodeId");
String selected = "style='background-color:#FFFFFF;'";
List brandList = ContentFactory.getInstance().getStore().getBrands();
Collections.sort(brandList,ContentNodeModel.FULL_NAME_COMPARATOR);
%>

<table width="100%" cellpadding="0" cellspacing="0" border="0">
<tr>
<td class="tree"> 
<%
if (brandList!=null  && brandList.size()>0) {
   for(Iterator itrBrands = brandList.iterator(); itrBrands.hasNext(); ) {
        BrandModel brand = (BrandModel)itrBrands.next();
%>
<div class="dept" <% if (brand.getPK().getId().equalsIgnoreCase(brandNodeId)) { %><%=selected%><% } %>><a name="<%=brand%>" href="<%= folderLink %>?nodeId=<%=brand.getPK().getId()%>">&curren; <%=brand.getFullName()%></a></div>
<% }
 }%>
</td>
</tr>

</table>
<br>
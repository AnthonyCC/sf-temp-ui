<%@ page import='com.freshdirect.fdstore.content.*'%>
<%
String servletContext = request.getContextPath();
String pageURI = request.getRequestURI();
String folderLink = pageURI.substring(0, pageURI.lastIndexOf("/")+1) + "view_edit.jsp";

	if (pageURI.indexOf("pop_") > -1) {
	folderLink= "#";
	}
	
//get current selected
String thisDomain = request.getParameter("dom");
List domainList = ContentFactory.getInstance().getStore().getDomains();
String selected = "style='background-color:#FFFFFF;'";
%>

<table width="100%" cellpadding="0" cellspacing="0" border="0">
<tr>
<td class="tree"> 
<%
if (domainList!=null  && domainList.size()>0) {
   for(int i=0;i<domainList.size();i++) {
        Domain domain = (Domain)domainList.get(i);
%>
<div class="dept" ><a href="<%= folderLink %>?dom=dom2">&curren; <%=domain.getLabel() %></a></div>
<%
    }

}
%>
<div class="dept" <% if ("dom1".equalsIgnoreCase(thisDomain)) { %><%=selected%><% } %>><a href="<%= folderLink %>?dom=dom1">&curren; Domain 1</a></div>
<div class="dept" <% if ("dom2".equalsIgnoreCase(thisDomain)) { %><%=selected%><% } %>><a href="<%= folderLink %>?dom=dom2">&curren; Domain 2</a></div>
<div class="dept" <% if ("dom3".equalsIgnoreCase(thisDomain)) { %><%=selected%><% } %>><a href="<%= folderLink %>?dom=dom3">&curren; Domain 3</a></div>
<div class="dept" <% if ("dom4".equalsIgnoreCase(thisDomain)) { %><%=selected%><% } %>><a href="<%= folderLink %>?dom=dom4">&curren; Domain 4</a></div>
</td>
</tr>

</table>
<br>
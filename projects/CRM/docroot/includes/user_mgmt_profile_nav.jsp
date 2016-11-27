<%
String servletContext = request.getContextPath();
String pageURI = request.getRequestURI();
String currentDir = pageURI.substring(0, pageURI.lastIndexOf("/")+1);
String agent = "";
String agent_pk = request.getParameter("agent_pk");
	if (agent_pk != null || !"".equals(agent_pk)) {
		agent = "?agent_pk=" + agent_pk + "&action=searchCase";
	}
%>

<% if (pageURI.indexOf("account") < 0) { %>
<a href="<%=currentDir%>account_setting.jsp<%= agent %>" class="user_mgmt_tab">Account Setting</a>
<% } else { %>
<div class="user_mgmt_tab_on" style="margin-left: 2px;">Account Setting</div>
<% } %>

<% if (pageURI.indexOf("worklist") < 0) { %>
<a href="<%=currentDir%>worklist.jsp<%= agent %>" class="user_mgmt_tab">Worklist</a>
<% } else { %>
<div class="user_mgmt_tab_on">Worklist</div>
<% } %>
<br clear="all">
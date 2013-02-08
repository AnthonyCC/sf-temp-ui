<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='freshdirect' prefix='fd' %>
<tmpl:insert template="/common/template/no_shell_strict.jsp">
	<tmpl:put name="title">FreshDirect</tmpl:put>
	<tmpl:put name="customCss"><link rel="stylesheet" href="/media/editorial/welcome/welcome-eng.css" type="text/css" /></tmpl:put>
	<tmpl:put name="content">
	<% if ( request.getParameter("lang") != null) { %>
		<% if ("espanol".equalsIgnoreCase(request.getParameter("lang"))) { %> 
			<fd:IncludeMedia name="/media/editorial/welcome/welcome-sp.html" />
		<% } else { %>
			<fd:IncludeMedia name="/media/editorial/welcome/welcome-en.html" />
		<% } %>
	<% } else { %>
		<fd:IncludeMedia name="/media/editorial/welcome/welcome-en.html" />
	<% } %>
	</tmpl:put>
</tmpl:insert>



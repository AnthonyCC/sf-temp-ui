<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<tmpl:insert template='/template/kbit.jsp'>

    <tmpl:put name='title' direct='true'>Help FreshDirect Store Products</tmpl:put>
	
    <tmpl:put name='content' direct='true'>
	<% String show = request.getParameter("show"); %>
	
	<% if ("meat".equalsIgnoreCase(show)) { %>
		Some meat related info
	
	<% } %>
	
	
	</tmpl:put>

</tmpl:insert>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<%
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader ("Expires", 0);
%>

<tmpl:insert template='/template/kbit.jsp'>

    <tmpl:put name='title' direct='true'>Help - FreshDirect Store Products</tmpl:put>
	
    <tmpl:put name='content' direct='true'>
	<% String show = request.getParameter("show"); %>
	
	<% if ("mea".equalsIgnoreCase(show)) { %>
		<jsp:include page="meat.jsp"/>
	<% } %>
	
	<% if ("bak".equalsIgnoreCase(show)) { %>
		<jsp:include page="bakery.jsp"/>
	<% } %>
	
	<% if ("veg".equalsIgnoreCase(show)) { %>
		<jsp:include page="vegetable.jsp"/>
	<% } %>
	
	<% if ("cof".equalsIgnoreCase(show)) { %>
		<jsp:include page="coffee.jsp"/>
	<% } %>
	
	<% if ("fro".equalsIgnoreCase(show)) { %>
		<jsp:include page="frozen.jsp"/>
	<% } %>
	
	
	</tmpl:put>

</tmpl:insert>
						
						

						
	
	

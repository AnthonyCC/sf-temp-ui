<%@ page import="com.freshdirect.fdstore.myfd.blog.MyFdFeed"%>
<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed='true' pixelNames="TheSearchAgency" /><%

request.setAttribute("listPos", "SystemMessage,WDelivery,WAffiliate");

%><tmpl:insert template="/common/template/page_template_optimized.jsp">
	<tmpl:put name="title">FreshDirect</tmpl:put>
	<tmpl:put name='content' direct='true'>
		
	    <div class="content span-24">
		<% if ( request.getParameter("lang") != null) { %>
			<% if ("espanol".equalsIgnoreCase(request.getParameter("lang"))) { %> 
				<fd:IncludeMedia name="/media/editorial/welcome/welcome-sp.html" />
			<% } else { %>
				<fd:IncludeMedia name="/media/editorial/welcome/welcome-en.html" />
			<% } %>
		<% } else { %>
			<fd:IncludeMedia name="/media/editorial/welcome/welcome-en.html" />
		<% } %>
	    </div>
	</tmpl:put>
		
	<tmpl:put name='extraHead' direct='true'>
		<fd:css href="/assets/css/common/page.css" />
		<fd:css href="/media/editorial/welcome/welcome-eng.css" />
	</tmpl:put>

	<tmpl:put name='extraBody' direct='true'>
	</tmpl:put>

	<tmpl:put name='nav' direct='true'>
	</tmpl:put>		
	
</tmpl:insert>

<% /* add this here since there's no log out page now */ %>
<iframe src="<%=MyFdFeed.getInstance().getBlogUrl()%>/?autologout" width="0" height="0" frameBorder="0"/>
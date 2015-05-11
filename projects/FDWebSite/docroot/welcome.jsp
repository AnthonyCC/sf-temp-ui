<%@ taglib uri='template' prefix='tmpl'%>
<%@ taglib uri='freshdirect' prefix='fd' %>
<fd:CheckLoginStatus guestAllowed='true' pixelNames="TheSearchAgency" id="user" /><%

request.setAttribute("listPos", "SystemMessage,WDelivery,WAffiliate");
request.setAttribute("noyui", true);

response.sendRedirect("/"); //temproraly 302 redirect 

%><tmpl:insert template="/common/template/page_template_optimized.jsp">
	<tmpl:put name="title">FreshDirect</tmpl:put>
	<tmpl:put name='content' direct='true'>
	    <div class="content span-24">
		<% if ( request.getParameter("lang") != null) { %>
			<% if ("espanol".equalsIgnoreCase(request.getParameter("lang"))) { %> 
			<tmpl:put name='seoMetaTag' direct='true'>
       					<fd:SEOMetaTag language='es-ES'/>
 					 </tmpl:put>
				<fd:IncludeMedia name="/media/editorial/welcome/welcome-sp.html" />
			<% } else { %>
			<tmpl:put name='seoMetaTag' direct='true'>
      					 <fd:SEOMetaTag language='en-US'/>
  					</tmpl:put>
				<fd:IncludeMedia name="/media/editorial/welcome/welcome-en.html" />
			<% } %>
		<% } else { %>
		<tmpl:put name='seoMetaTag' direct='true'>
      					 <fd:SEOMetaTag language='en-US'/>
  					</tmpl:put>
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

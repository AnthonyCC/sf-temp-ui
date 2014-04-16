<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ taglib uri='template' prefix='tmpl' %>
<%@ taglib uri='freshdirect' prefix='fd' %>

<fd:CheckLoginStatus id="user" />

<tmpl:insert template='/common/template/large_pop.jsp'>
 <% 
          boolean fromZipCheck = "yes".equalsIgnoreCase(request.getParameter("zipCheck"));
          boolean isPopup = true; 
		  boolean isHamptons = "hamptons".equalsIgnoreCase(request.getParameter("location"));
		  boolean isLbi = "lbi".equalsIgnoreCase(request.getParameter("location"));
		  %>
	<tmpl:put name='title' direct='true'>FreshDirect - <%=(isHamptons||isLbi)?"Summer Services":"Pickup"%></tmpl:put>
		<tmpl:put name='content' direct='true'>
          <div align="left">
		  <% if (isHamptons) { %>
		  	<fd:IncludeMedia name="/media/editorial/summer_services/hamptons/hamptons_service.html" />
		  <% } else if (isLbi) { %>
		  	<fd:IncludeMedia name="/media/editorial/summer_services/long_beach_island/long_beach_island_service.html" />
		  <% } else { %>
		  	<%@ include file="/shared/includes/delivery/i_lic_pickup.jspf"%>
		  <% } %>
		  <br><br></div>
          	</tmpl:put>
</tmpl:insert>

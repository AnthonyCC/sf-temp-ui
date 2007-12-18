<%@ page import='com.freshdirect.fdstore.customer.*' %>
<%@ page import='com.freshdirect.webapp.taglib.fdstore.*'%>
<%@ taglib uri='freshdirect' prefix='fd' %>
<%@ taglib uri='template' prefix='tmpl' %>

<fd:CheckLoginStatus />
<%	
	FDUserI user = (FDUserI) session.getAttribute(SessionName.USER);
	String aboutSection;
	aboutSection = request.getParameter("catId");
%>
<tmpl:insert template='/common/template/left_dnav.jsp'>
<tmpl:put name='title' direct='true'>FreshDirect - About FreshDirect</tmpl:put>
<tmpl:put name='content' direct='true'>
<img src="/media_stat/images/layout/clear.gif" width="1" height="12"><br>
	<% if (aboutSection.equals("about_fdstory_promise")){
	%>
		<%@ include file="/about/includes/ourPromise.jspf" %>
	<%
	}
	else if(aboutSection.equals("about_fdstory_selection")){%>

		<%@ include file="/about/includes/selection.jspf" %>
	
	<%} 
	else if(aboutSection.equals("about_fdstory_facility")){%>
	
		<%@ include file="/about/includes/ourFacility.jspf" %>
	
	<%} 
	else if(aboutSection.equals("about_fdstory_experts")){%>
	
		<%@ include file="/about/includes/ourExperts.jspf" %>
	
	<%} 
	else if(aboutSection.equals("about_fdstory_delivery")){%>
	
		<%@ include file="/about/includes/delivery.jspf" %>
	
	<%} 
	else if(aboutSection.equals("about_fdstory_guarantee")){%>
	
		<%@ include file="/about/includes/freshness.jspf" %>
	
	<%} 
	else if(aboutSection.equals("about_fdstory_store")){%>
	
		<%@ include file="/about/includes/onlineStore.jspf" %>
	
	<%} 
	else{%>
	
		<%@ include file="/about/includes/onlineStore.jspf" %>
	
	<%} 

%>				
<br><img src="/media_stat/images/layout/clear.gif" width="1" height="12">
</tmpl:put>
</tmpl:insert>


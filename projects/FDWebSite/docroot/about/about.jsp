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
    <tmpl:put name="seoMetaTag" direct='true'>
        <fd:SEOMetaTag title="FreshDirect - About FreshDirect"/>
    </tmpl:put>
<%--     <tmpl:put name='title'>FreshDirect - About FreshDirect</tmpl:put> --%>
    <tmpl:put name='content' direct='true'>
<img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="12"><br>
<div align="right">
	<% if ("about_fdstory_promise".equals(aboutSection)){
	%>
		<%@ include file="/about/includes/ourPromise.jspf" %>
	<%
	}
	else if("about_fdstory_selection".equals(aboutSection)){%>

		<%@ include file="/about/includes/selection.jspf" %>
	
	<%} 
	else if("about_fdstory_facility".equals(aboutSection)){%>
	
		<%@ include file="/about/includes/ourFacility.jspf" %>
	
	<%} 
	else if("about_fdstory_experts".equals(aboutSection)){%>
	
		<%@ include file="/about/includes/ourExperts.jspf" %>
	
	<%} 
	else if("about_fdstory_delivery".equals(aboutSection)){%>
	
		<%@ include file="/about/includes/delivery.jspf" %>
	
	<%} 
	else if("about_fdstory_guarantee".equals(aboutSection)){%>
	
		<%@ include file="/about/includes/freshness.jspf" %>
	
	<%} 
	else if("about_fdstory_store".equals(aboutSection)){%>
	
		<%@ include file="/about/includes/onlineStore.jspf" %>
	
	<%} 
	else{%>
	
		<%@ include file="/about/includes/onlineStore.jspf" %>
	
	<%} 

%>
</div>				
<br><img src="/media_stat/images/layout/clear.gif" alt="" width="1" height="12">
</tmpl:put>
</tmpl:insert>

